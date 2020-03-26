
import Algorithms.MessageCommunicationDisplay;
import UI.*;
import io.jbotsim.core.*;
import io.jbotsim.core.event.CommandListener;
import io.jbotsim.core.event.SelectionListener;
import io.jbotsim.core.event.StartListener;


public class Main implements SelectionListener, StartListener, CommandListener {

    static final String ALGORITHM_DISTRIBUTE = "Distributed Algorithm";
    static final String ALGORITHM_MACHINE_LEARNING = "Algorithm machine learning";
    static final String ALGORITHM_PROB = "Algorithm prob";
    static final String ALGORITHM_EXACT = "Algorithm exact";
    static final String ADD_IPV4 = "Add IPv4 Router";
    static final String ADD_IPV6 = "Add IPv6 Router";
    static final String CONVERTER = "Place Converter";
    static final String STOP_CONVERTER = "Stop placing Converter";
    static final String NETWORK_GENERATION = "Network generation";
    static final String COVERING_TREE = "Find every Connected Component";
    static final String PRETREATMENT = "Pretreatment of the graph";
    Topology tp;
    Router start;
    Window win;
    public boolean converter = false;
    boolean colored = false;

    public Main() {
        tp = new Topology();
        tp.setSerializer(new NetworkSerializer());
        tp.setDefaultNodeModel(RouterIPv4.class);
        tp.addSelectionListener(this);
        tp.addStartListener(this);
        tp.addCommandListener(this);
        win = new Window(tp);
        tp.removeAllCommands();
        tp.addCommand("Save topology");
        tp.addCommand("Load topology");

        tp.start();
    }

    private MessageCommunicationDisplay algorithmDisplay = new MessageCommunicationDisplay();



    @Override
    public void onSelection(Node node) {
        if (!win.getPathing()) {
            boolean removedLink = false;
            if (start != null) {
                Link l = new Link(start, node);
                for (Link link : tp.getLinks()) {
                    if (link.equals(l)) {
                        tp.removeLink(l);
                        removedLink = true;
                        win.textupdate();
                    }
                }
                if (!removedLink)
                    tp.addLink(l);
                win.textupdate();

                start = null;
            } else
                start = (Router) node;
        }
        else{
            Router router = (Router) node;
            algorithmDisplay.setTopology(tp);
            algorithmDisplay.messageDisplay(router);
        }
    }

    public static void main(String[] args) { new Main(); }

    @Override
    public void onStart() {
        tp.setCommunicationRange(-1);
    }

    @Override
    public void onCommand(String s) {
//        This command is kept because the code need to be here
        if(s.equals(COVERING_TREE)) {
            if (!colored) {
                for (Link link : tp.getLinks()) {
                    Router source = (Router) link.source;
                    Router destination = (Router) link.destination;
                    if (source.getClass().equals(destination.getClass()) && source instanceof RouterIPv4) {
                        source.getCommonLinkWith(destination).setWidth(4);
                        Color c = new Color(85, 189, 73);
                        source.getCommonLinkWith(destination).setColor(c);
                    } else if (source.getClass().equals(destination.getClass()) && source instanceof RouterIPv6) {
                        source.getCommonLinkWith(destination).setWidth(4);
                        Color c = new Color(15, 141, 212);
                        source.getCommonLinkWith(destination).setColor(c);
                    } else {
                        source.getCommonLinkWith(destination).setWidth(3);
                        Color c = new Color(227, 35, 35);
                        source.getCommonLinkWith(destination).setColor(c);
                    }
                }
                colored = !colored;
            }
            else {
                for (Link link : tp.getLinks()) {
                    link.setWidth(Link.DEFAULT_WIDTH);
                    link.setColor(Link.DEFAULT_COLOR);
                }
                colored = !colored;
            }
        }
    }
}
