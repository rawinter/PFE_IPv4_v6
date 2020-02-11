import io.jbotsim.core.Color;
import io.jbotsim.core.Link;
import io.jbotsim.core.Node;
import io.jbotsim.core.Topology;
import io.jbotsim.core.event.CommandListener;
import io.jbotsim.core.event.SelectionListener;
import io.jbotsim.core.event.StartListener;
import io.jbotsim.ui.JViewer;

public class Main implements SelectionListener, StartListener, CommandListener {

    public static final String COMPUTE_ALGORITHM = "Compute Algorithm";
    Topology tp;
    Router start;

    public Main() {
        tp = new Topology();
        tp.setDefaultNodeModel(Router.class);
        tp.addSelectionListener(this);
        tp.addStartListener(this);
        tp.addCommand(COMPUTE_ALGORITHM);
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
        if(s.equals(COMPUTE_ALGORITHM)){
            //Where to launch the algorithm
        }

    }
}
