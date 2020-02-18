import io.jbotsim.core.Color;
import io.jbotsim.core.Link;
import io.jbotsim.core.Node;
import io.jbotsim.core.Topology;
import io.jbotsim.core.event.CommandListener;
import io.jbotsim.core.event.SelectionListener;
import io.jbotsim.core.event.StartListener;
import io.jbotsim.ui.JViewer;

public class Main implements SelectionListener, StartListener, CommandListener {

    static final String ALGORITHM_NON_DETERMINISTIC = "Algorithm non deterministic";
    static final String ALGORITHM_DISTRIBUTE = "Algorithm distribute";
    static final String ALGORITHM_MACHINE_LEARNING = "Algorithm machine learning";
    static final String ALGORITHM_PROB = "Algorithm prob";
    static final String ALGORITHM_EXACT = "Algorithm exact";
    static final String ADD_IPV4 = "Add IPv4 Router";
    static final String ADD_IPV6 = "Add IPv6 Router";
    Topology tp;
    Router start;

    public Main() {
        tp = new Topology();
        tp.setDefaultNodeModel(Router.class);
        tp.addSelectionListener(this);
        tp.addStartListener(this);
        tp.addCommandListener(this);
        tp.addCommand(ADD_IPV4);
        tp.addCommand(ADD_IPV6);
        tp.addCommand(ALGORITHM_NON_DETERMINISTIC);
        tp.addCommand(ALGORITHM_DISTRIBUTE);
        tp.addCommand(ALGORITHM_MACHINE_LEARNING);
        tp.addCommand(ALGORITHM_PROB);
        tp.addCommand(ALGORITHM_EXACT);
        new JViewer(tp);
        tp.start();
    }

    @Override
    public void onSelection(Node node) {
        boolean removedLink = false;
        if (start != null) {
            Link l = new Link(start,node);
            for(Link link : tp.getLinks()){
                if(link.equals(l)){
                    tp.removeLink(l);
                    removedLink = true;
                }
            }
            if(!removedLink)
                tp.addLink(l);
            start = null;
        }
        else
            start = (Router) node;
    }

    public static void main(String[] args) {
        new Main();
    }

    @Override
    public void onStart() {
        tp.setCommunicationRange(0);
    }

    @Override
    public void onCommand(String s) {
        if(s.equals(ALGORITHM_NON_DETERMINISTIC)){
            //Where to launch the algorithm
        }
        if(s.equals(ALGORITHM_DISTRIBUTE)){
            //Where to launch the algorithm
        }
        if(s.equals(ALGORITHM_MACHINE_LEARNING)){
            //Where to launch the algorithm
        }
        if(s.equals(ALGORITHM_PROB)){
            //Where to launch the algorithm
        }
        if(s.equals(ALGORITHM_EXACT)){
            //Where to launch the algorithm
        }
        if(s.equals(ADD_IPV4)){
            tp.setDefaultNodeModel(RouterIPv4.class);
            System.out.println("Adding IPv4 Router");
        }
        if(s.equals(ADD_IPV6)){
            tp.setDefaultNodeModel(RouterIPv6.class);
            System.out.println("Adding IPv6 Router");
        }

    }
}
