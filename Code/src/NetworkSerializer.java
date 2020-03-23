import UI.Router;
import UI.RouterIPv4;
import UI.RouterIPv6;
import io.jbotsim.core.Link;
import io.jbotsim.core.Node;
import io.jbotsim.core.Point;
import io.jbotsim.core.Topology;
import io.jbotsim.io.TopologySerializer;

import java.util.HashMap;
import java.util.Iterator;

public class NetworkSerializer implements TopologySerializer {
    @Override
    public String exportToString(Topology topology) {
        StringBuilder res = new StringBuilder();
        res.append("cR ").append(topology.getCommunicationRange()).append("\n");
        res.append("sR ").append(topology.getSensingRange()).append("\n");
        res.append("width ").append(topology.getWidth()).append(":  height ").append(topology.getHeight()).append("\n");
        Iterator var3 = topology.getNodes().iterator();

        while(var3.hasNext()){
            Node n = (Node) var3.next();
            if(n instanceof RouterIPv4){
                Point p2d = new Point();
                p2d.setLocation(n.getLocation().getX(), n.getLocation().getY());
                res.append(n.toString()).append(" ").append(p2d.toString().substring(p2d.toString().indexOf("[") - 1)).append(" IpType 4 ").append("\n");
            }
            else{
                if(n instanceof RouterIPv6){
                    Point p2d = new Point();
                    p2d.setLocation(n.getLocation().getX(), n.getLocation().getY());
                    res.append(n.toString()).append(" ").append(p2d.toString().substring(p2d.toString().indexOf("[") - 1)).append(" IpType 6 ").append("\n");
                }
            }
        }


        var3 = topology.getLinks().iterator();

        while(var3.hasNext()) {
            Link l = (Link)var3.next();
            if (!l.isWireless()) {
                res.append(l.toString()).append("\n");
            }
        }

        return res.toString();
    }

    @Override
    public void importFromString(Topology topology, String data) {
        topology.setCommunicationRange(Double.parseDouble(data.substring(data.indexOf(" ") + 1, data.indexOf("\n"))));
        data = data.substring(data.indexOf("\n") + 1);
        topology.setSensingRange(Double.parseDouble(data.substring(data.indexOf(" ") + 1, data.indexOf("\n"))));
        data = data.substring(data.indexOf("\n") + 1);
        double X = Double.parseDouble(data.substring(data.indexOf("width") + 6, data.indexOf(":")));
        double Y = Double.parseDouble(data.substring(data.indexOf("height") + 7, data.indexOf("\n")));
        data = data.substring(data.indexOf("\n") + 1);
        HashMap nodeTable = new HashMap();

        while(data.indexOf("[") > 0) {
            double propX = topology.getWidth()/X;
            double propY = topology.getHeight()/Y;
            double x = (new Double(data.substring(data.indexOf("x") + 3, data.indexOf(", y")))) * propX;
            double y = 0.0D;
            double z = 0.0D;

            double IpType = 0.0D;
            if (data.contains("z")) {
                y = (new Double(data.substring(data.indexOf("y") + 3, data.indexOf(", z")))) * propY;
                z = new Double(data.substring(data.indexOf("z") + 3, data.indexOf("]")));
            } else {
                y = (new Double(data.substring(data.indexOf("y") + 3, data.indexOf("]")))) * propY;
            }
            IpType = new Double(data.substring(data.indexOf("IpType") + 7,data.indexOf("\n")));

            try {
                Router node;

                String model = "";
                if(IpType == 4){
                   node = new RouterIPv4();
                }
                else {
                    node = new RouterIPv6();
                }
                node.setLocation(x, y, z);
                topology.addNode(node);
                String id = data.substring(0, data.indexOf(" "));
                node.setProperty("id", id);
                nodeTable.put(id, node);
                data = data.substring(data.indexOf("\n") + 1);
            } catch (Exception var12) {
            }
        }

        while(data.indexOf("--") > 0) {
            Node n1 = (Node)nodeTable.get(data.substring(0, data.indexOf(" ")));
            Node n2 = (Node)nodeTable.get(data.substring(data.indexOf(">") + 2, data.indexOf("\n")));
            Link.Orientation orientation = data.indexOf("<") > 0 && data.indexOf("<") < data.indexOf("\n") ? Link.Orientation.UNDIRECTED : Link.Orientation.DIRECTED;
            topology.addLink(new Link(n1, n2, orientation, Link.Mode.WIRED));
            data = data.substring(data.indexOf("\n") + 1);
        }

    }
}
