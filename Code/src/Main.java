import UI.Router;
import UI.RouterIPv4;
import UI.RouterIPv6;
import io.jbotsim.core.Link;
import io.jbotsim.core.Node;
import io.jbotsim.core.Topology;
import io.jbotsim.core.event.CommandListener;
import io.jbotsim.core.event.SelectionListener;
import io.jbotsim.core.event.StartListener;
import io.jbotsim.ui.JViewer;

public class Main implements SelectionListener, StartListener, CommandListener {

    static final String ALGORITHM_NON_DETERMINISTIC = "AlgorithmModel non deterministic";
    static final String ALGORITHM_DISTRIBUTE = "AlgorithmModel distribute";
    static final String ALGORITHM_MACHINE_LEARNING = "AlgorithmModel machine learning";
    static final String ALGORITHM_PROB = "AlgorithmModel prob";
    static final String ALGORITHM_EXACT = "AlgorithmModel exact";
    static final String ADD_IPV4 = "Add IPv4 Router";
    static final String ADD_IPV6 = "Add IPv6 Router";
    static final String CONVERTER = "Place Converter";
    static final String STOP_CONVERTER = "Stop placing Converter";
    static final String NETWORK_GENERATION = "Network generation";
    Topology tp;
    Router start;
    boolean converter = false;

    public Main() {
        tp = new Topology();
        tp.setDefaultNodeModel(RouterIPv4.class);
        tp.addSelectionListener(this);
        tp.addStartListener(this);
        tp.addCommandListener(this);
        new JViewer(tp);
        tp.start();
        tp.addCommand(NETWORK_GENERATION);
        tp.addCommand(ADD_IPV4);
        tp.addCommand(ADD_IPV6);
        tp.addCommand(ALGORITHM_NON_DETERMINISTIC);
        tp.addCommand(ALGORITHM_DISTRIBUTE);
        tp.addCommand(ALGORITHM_MACHINE_LEARNING);
        tp.addCommand(ALGORITHM_PROB);
        tp.addCommand(ALGORITHM_EXACT);
        tp.addCommand(CONVERTER);
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

            NetworkGeneration(10,tp);
            System.out.println("Generation");
        }
    }

    private void NetworkGeneration(int nb, Topology tp){
        tp.clear();
        double rand = Math.random();
        for(int i = 0; i < nb*rand; i++){
            RouterIPv4 r = new RouterIPv4();
            r.setLocation(Math.random()*tp.getWidth(),Math.random()*tp.getHeight());
            tp.addNode(r);
        }
        for(int i = 0; i< (nb-(nb*rand)); i++){
            RouterIPv6 r = new RouterIPv6();
            r.setLocation(Math.random()*tp.getWidth(),Math.random()*tp.getHeight());
            tp.addNode(r);
        }

        for(int j = 0 ; j < (2*nb); j++){
            int alea = (int) (Math.random()*(nb-2));
            Link l = new Link(tp.getNodes().get(alea),tp.getNodes().get((alea+1)));
            tp.addLink(l);

        }
        System.out.println(tp.getLinks());
    }
}
