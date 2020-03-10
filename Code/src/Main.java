import Algorithms.ExactAlgorithm;
import Algorithms.GloutonAlgorithm;
import Algorithms.SpanningTreeDistributed;
import UI.*;
import io.jbotsim.core.*;
import io.jbotsim.core.event.CommandListener;
import io.jbotsim.core.event.SelectionListener;
import io.jbotsim.core.event.StartListener;
import io.jbotsim.ui.JViewer;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
    static final String NETWORK_GENERATION_10 = "Network generation 10 Router Max";
    static final String NETWORK_GENERATION_20 = "Network generation 20 Router Max";
    static final String NETWORK_GENERATION_30 = "Network generation 30 Router Max";
    static final String PRETREATMENT = "Pretreatment of the graph";
    Topology tp;
    Router start;
    Window win;
    public boolean converter = false;

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

    @Override
    public void onSelection(Node node) {
        if(!converter) {
            boolean removedLink = false;
            if (start != null) {
                Link l = new Link(start, node);
                for (Link link : tp.getLinks()) {
                    if (link.equals(l)) {
                        tp.removeLink(l);
                        removedLink = true;
                    }
                }
                if (!removedLink)
                    tp.addLink(l);
                start = null;
            } else
                start = (Router) node;
        }
        else{
            if(node instanceof Router){
                node.setIcon("Code/Ressources/images/Temp-Converter.png");
            }
        }
    }

    public static void main(String[] args) { new Main(); }

    @Override
    public void onStart() {
        tp.setCommunicationRange(-1);
    }

    @Override
    public void onCommand(String s) {
        if(s.equals(ALGORITHM_DISTRIBUTE)){
            SpanningTreeDistributed algorithm = new SpanningTreeDistributed(tp);
            for(Node node : tp.getNodes()) {
                Router router = (Router) node;
                router.spanningTreeCreation = true;
            }
            algorithm.newSpanningTree();
        }
        if(s.equals(ALGORITHM_MACHINE_LEARNING)){
            //Where to launch the algorithm
        }
        if(s.equals(ALGORITHM_PROB)){
            //Where to launch the algorithm
        }
        if(s.equals(ALGORITHM_EXACT)){
            //Where to launch the algorithm
            ExactAlgorithm exact=new ExactAlgorithm(tp);
            exact.algorithm();
        }
        if(s.equals(ADD_IPV4)){
            tp.setDefaultNodeModel(RouterIPv4.class);
            System.out.println("Adding IPv4 UI.Router");
        }
        if(s.equals(ADD_IPV6)){
            tp.setDefaultNodeModel(RouterIPv6.class);
            System.out.println("Adding IPv6 UI.Router");
        }
        if(s.equals(CONVERTER)){
            converter = true;
            System.out.println("Adding Converter");
            tp.removeCommand(CONVERTER);
            tp.addCommand(STOP_CONVERTER);
        }
        if(s.equals(STOP_CONVERTER)){
            converter = false;
            System.out.println("Stop Adding Converter");
            tp.removeCommand(STOP_CONVERTER);
            tp.addCommand(CONVERTER);
        }
        if(s.equals(NETWORK_GENERATION)){

        }

        if(s.equals(PRETREATMENT)){
//            Pretreatment();
        }

        if(s.equals(COVERING_TREE)) {
            /*Random random = new Random();
            List<Router> routersList = new ArrayList<>();
            for(Node node : tp.getNodes())
                routersList.add((Router) node);
            List<ConnectedComponent> connectedComponentsList = new ArrayList<>();
            //while(!routersList.isEmpty())
            //{
                int randomNumber = random.nextInt(routersList.size());
                Router parent = routersList.get(randomNumber);
                routersList.remove(parent);
                parent.spanningTree(routersList, connectedComponentsList);
            //}*/
            for(Link link : tp.getLinks()) {
                Router source = (Router) link.source;
                Router destination = (Router) link.destination;
                if(source.getClass().equals(destination.getClass()) && source instanceof RouterIPv4) {
                    source.getCommonLinkWith(destination).setWidth(4);
                    source.getCommonLinkWith(destination).setColor(Color.RED);
                }
                else if(source.getClass().equals(destination.getClass()) && source instanceof RouterIPv6) {
                    source.getCommonLinkWith(destination).setWidth(4);
                    source.getCommonLinkWith(destination).setColor(Color.BLUE);
                }
                else {
                    source.getCommonLinkWith(destination).setColor(Color.GREEN);
                }
            }
        }
    }




}
