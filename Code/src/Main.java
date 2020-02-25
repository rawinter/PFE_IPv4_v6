import Algorithms.GloutonAlgorithm;
import Algorithms.NonDeterministicAlgorithm;
import UI.ConnectedComponent;
import UI.Router;
import UI.RouterIPv4;
import UI.RouterIPv6;
import io.jbotsim.core.*;
import io.jbotsim.core.event.CommandListener;
import io.jbotsim.core.event.SelectionListener;
import io.jbotsim.core.event.StartListener;
import io.jbotsim.ui.JViewer;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main implements SelectionListener, StartListener, CommandListener {

    static final String ALGORITHM_NON_DETERMINISTIC = "Algorithm Glouton";
    static final String ALGORITHM_DISTRIBUTE = "Algorithm distribute";
    static final String ALGORITHM_MACHINE_LEARNING = "Algorithm machine learning";
    static final String ALGORITHM_PROB = "Algorithm prob";
    static final String ALGORITHM_EXACT = "Algorithm exact";
    static final String ADD_IPV4 = "Add IPv4 Router";
    static final String ADD_IPV6 = "Add IPv6 Router";
    static final String CONVERTER = "Place Converter";
    static final String STOP_CONVERTER = "Stop placing Converter";
    static final String NETWORK_GENERATION = "Network generation";
    static final String COVERING_TREE = "Find every Connected Component";
    Topology tp;
    Router start;
    boolean converter = false;

    public Main() {
        tp = new Topology();
        tp.setSerializer(new NetworkSerializer());
        tp.setDefaultNodeModel(RouterIPv4.class);
        tp.addSelectionListener(this);
        tp.addStartListener(this);
        tp.addCommandListener(this);
        new JViewer(tp);
        tp.removeAllCommands();
        tp.addCommand("Save topology");
        tp.addCommand("Load topology");
        tp.addCommand("-"); //:COMMENT:Add a line to separate the commands
        tp.addCommand(NETWORK_GENERATION);
        tp.addCommand(ADD_IPV4);
        tp.addCommand(ADD_IPV6);
        tp.addCommand(CONVERTER);
        tp.addCommand("-");
        tp.addCommand(ALGORITHM_NON_DETERMINISTIC);
        tp.addCommand(ALGORITHM_DISTRIBUTE);
        tp.addCommand(ALGORITHM_MACHINE_LEARNING);
        tp.addCommand(ALGORITHM_PROB);
        tp.addCommand(ALGORITHM_EXACT);
        tp.addCommand(COVERING_TREE);

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
        if(s.equals(ALGORITHM_NON_DETERMINISTIC)){
            //Where to launch the algorithm
            //NonDeterministicAlgorithm non_deterministic = new NonDeterministicAlgorithm(tp);
            //non_deterministic.algorithm();
            GloutonAlgorithm glouton = new GloutonAlgorithm(tp);
            glouton.algorithm();
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
        if(s.equals(COVERING_TREE)) {
            Random random = new Random();
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
            //}
        }
    }

    private void NetworkGeneration(int nb, Topology tp) {

        Random random = new Random();
        tp.clear();
        int rand = random.nextInt(nb)+2;
        for (int i = 0; i < rand; i++) {
            if(random.nextBoolean()) {
                RouterIPv4 r4 = new RouterIPv4();
                r4.setLocation(random.nextInt(tp.getWidth() - (tp.getWidth() / 5)) + (tp.getWidth() / 10), random.nextInt(tp.getHeight() - (tp.getHeight() / 5)) + (tp.getHeight() / 10));
                tp.addNode(r4);
            }
            else {
                RouterIPv6 r6 = new RouterIPv6();
                r6.setLocation(random.nextInt(tp.getWidth() - (tp.getWidth() / 5)) + (tp.getWidth() / 10), random.nextInt(tp.getHeight() - (tp.getHeight() / 5)) + (tp.getHeight() / 10));
                tp.addNode(r6);
            }
        }

        boolean valid = false;
        while(!valid) {
            System.out.println("while(!valid)");
            int alea1 = random.nextInt(rand);
            int alea2 = random.nextInt(rand);
            while(alea1 == alea2){
                alea2 = random.nextInt(rand);
            }
            Link l = new Link(tp.getNodes().get(alea1), tp.getNodes().get((alea2)));
            if (!tp.getLinks().contains(l)) {
                tp.addLink(l);
            }
            List<Node> nodesToTest = new ArrayList<>();
            List<Node> nodesMarked = new ArrayList<>();
            nodesToTest.add(tp.getNodes().get(0));
            while (!nodesToTest.isEmpty()) {
                System.out.println("nodesToTest.isempty()");
                Node currentNode = nodesToTest.remove(0);
                nodesMarked.add(currentNode);
                for(Node n : currentNode.getNeighbors()){
                    if(!nodesToTest.contains(n) && !nodesMarked.contains(n)){
                        nodesToTest.add(n);
                    }
                }
            }
            valid = true;
            for (Node n : tp.getNodes()) {
                if (!nodesMarked.contains(n)) {
                    valid = false;
                    break;
                }
            }
        }

//        while(tp.getLinks().size() < (2*rand)-1){
//            System.out.println("while(tp.getlink");
//            int alea1 = random.nextInt(rand);
//            int alea2 = random.nextInt(rand);
//            while(alea1 == alea2){
//                alea2 = random.nextInt(rand);
//            }
//            Link l = new Link(tp.getNodes().get(alea1), tp.getNodes().get((alea2)));
//            if (!tp.getLinks().contains(l)) {
//                tp.addLink(l);
//            }
//        }
    }
}
