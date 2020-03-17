package UI;

import Algorithms.ExactAlgorithm;
import Algorithms.GloutonAlgorithm;

import Algorithms.SpanningTreeDistributed;
import io.jbotsim.core.Link;
import io.jbotsim.core.Node;
import io.jbotsim.core.Topology;
import io.jbotsim.ui.JViewer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Window extends JViewer implements ActionListener, ItemListener {
    private JPanel pan = new JPanel();
    private JMenuBar menuBar = new JMenuBar();
    private JMenu menuLoadandSave = new JMenu("Load and Save");
    private JMenu generation = new JMenu("Generation");
    private JMenu algorithm = new JMenu("Algorithm");


    private JMenuItem gloutonAlgorithm = new JMenuItem("Launch glouton algorithm");
    private JMenuItem distributedAlgorithm = new JMenuItem("Launch distributed algorithm");
    private JMenuItem exactAlgorithm = new JMenuItem("Launch exact algorithm");
    private JMenuItem save = new JMenuItem("Save network");
    private JMenuItem load = new JMenuItem("Load network");

    private JMenuItem treeConnexite = new JMenuItem("Make the connexe component appear");

    private JMenuItem networkGeneration = new JMenuItem("Network generation");
    private JMenuItem converter = new JMenuItem("Adding converter");
    private JMenuItem stopConverter = new JMenuItem("Stop adding converter");
    private JMenuItem IPv4 = new JMenuItem("Adding IPv4 Router");
    private JMenuItem IPv6 = new JMenuItem("Adding IPv6 Router");
    private JMenuItem pretreatment = new JMenuItem("Pretreatment");


    Topology tp;
    List<RouterIPv4> routerIPv4 = new ArrayList<>();
    List<RouterIPv6> routerIPv6= new ArrayList<>();
    List<Link> links = new ArrayList<>();
    String Algo;
    double x = window.getWidth();
    double y = window.getHeight();
    int nbIPv4 = 10,nbIPv6 = 10;
    boolean saved = false;



    public Window(Topology topo) {
        super(topo, true);
        window.addComponentListener(new ComponentListener() {
            @Override
            public void componentResized(ComponentEvent componentEvent) {
                System.out.println("resized");
                double propX = window.getWidth()/x;
                double propY = window.getHeight()/y;
                for(Node n : tp.getNodes()){
                    n.setLocation((propX*n.getLocation().getX()),(propY*n.getLocation().getY()));
                }
                x = window.getWidth();
                y = window.getHeight();
            }

            @Override
            public void componentMoved(ComponentEvent componentEvent) {

            }

            @Override
            public void componentShown(ComponentEvent componentEvent) {

            }

            @Override
            public void componentHidden(ComponentEvent componentEvent) {

            }
        });
        tp = topo;


        menuLoadandSave.add(load);
        menuLoadandSave.add(save);

        generation.add(networkGeneration);
        generation.add(IPv4);
        generation.add(IPv6);
        generation.add(converter);
        generation.add(stopConverter);
        stopConverter.setEnabled(false);

        algorithm.add(treeConnexite);
        algorithm.add(pretreatment);
        algorithm.addSeparator();
        algorithm.add(gloutonAlgorithm);
        algorithm.add(distributedAlgorithm);
        algorithm.add(exactAlgorithm);

        menuBar.add(menuLoadandSave);
        menuBar.add(generation);
        menuBar.add(algorithm);

        window.setJMenuBar(menuBar);

        pan.setLayout(new BorderLayout());

        load.addActionListener(this);
        save.addActionListener(this);
        networkGeneration.addActionListener(this);
        IPv4.addActionListener(this);
        IPv6.addActionListener(this);
        converter.addActionListener(this);
        stopConverter.addActionListener(this);
        treeConnexite.addActionListener(this);
        pretreatment.addActionListener(this);
        gloutonAlgorithm.addActionListener(this);
        distributedAlgorithm.addActionListener(this);
        exactAlgorithm.addActionListener(this);

        load.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D,KeyEvent.CTRL_DOWN_MASK));
        save.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,KeyEvent.CTRL_DOWN_MASK));
        networkGeneration.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N,KeyEvent.CTRL_DOWN_MASK));

        window.getContentPane().setBackground(Color.BLACK);
        window.setContentPane(pan);
        window.setExtendedState(Frame.MAXIMIZED_BOTH);
        window.add(jtp,BorderLayout.CENTER);
        window.setVisible(true);


    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        if(actionEvent.getSource() == IPv6){
            tp.setDefaultNodeModel(RouterIPv6.class);
        }
        if(actionEvent.getSource() == IPv4){
            tp.setDefaultNodeModel(RouterIPv4.class);
        }
        if(actionEvent.getSource() == networkGeneration){
            try {
                nbIPv4 = Integer.parseInt(JOptionPane.showInputDialog(null, "number of ipv4 router", "Network generation", JOptionPane.QUESTION_MESSAGE));
            } catch (NumberFormatException e) {
                System.out.println("No number specified default at 10");
            } catch (HeadlessException e) {
                System.out.println("Headless");
            }
            try {
                nbIPv6 = Integer.parseInt(JOptionPane.showInputDialog(null, "number of ipv6 router", "Network generation", JOptionPane.QUESTION_MESSAGE));
            } catch (NumberFormatException e) {
                System.out.println("No number specified default at 10");
            } catch (HeadlessException e) {
                System.out.println("Headless");
            }
            NetworkGeneration(tp);
        }
        if(actionEvent.getSource() == converter){
            converter.setEnabled(false);
            stopConverter.setEnabled(true);
            tp.executeCommand("Place Converter");

        }
        if(actionEvent.getSource() == stopConverter){
            converter.setEnabled(true);
            stopConverter.setEnabled(false);
            tp.executeCommand("Stop placing Converter");
        }
        if(actionEvent.getSource() == gloutonAlgorithm) {
            if(!connexity(tp)){
                System.out.println("the graph is not connexe can't continue");
            }
            else {
                SavingRouter();

                GloutonAlgorithm glouton = new GloutonAlgorithm(tp);
                glouton.algorithm();

                RedoingTheNetwork();
            }
        }
        if(actionEvent.getSource() == distributedAlgorithm) {
            if(!connexity(tp)){
                System.out.println("the graph is not connexe can't continue");
            }
            else {
                //SavingRouter();

                SpanningTreeDistributed algorithm = new SpanningTreeDistributed(tp);
                algorithm.setupNewResearch();
                boolean checkEnd = algorithm.newSpanningTree();
                /*if(checkEnd) {
                    algorithm.findPotentialConverter();
                }*/

                //Il suffit de faire un booléen dans la fonction newSpanningTree() de l'algo pour voir si c'est le dernier appel pour faire RedoingTheNetwork()
                //De plus, il faut gérer le rajout des noeuds dans la topologie et faire en sorte de bien les ajouter dans les composantes connexes correspondantes
                //RedoingTheNetwork();
            }
        }
        if(actionEvent.getSource() == exactAlgorithm) {
            /*if(!connexity(tp)){
                System.out.println("the graph is not connexe can't continue");
            }
            else {
//                SavingRouter();

            ExactAlgorithm exact = new ExactAlgorithm(tp);
            exact.algorithm();

//                RedoingTheNetwork();
            }*/
            SavingRouter();
        }
        if(actionEvent.getSource() == treeConnexite){
            tp.executeCommand("Find every Connected Component");
        }
        if(actionEvent.getSource() == save){
            tp.executeCommand("Save topology");
        }
        if(actionEvent.getSource() == load){
            tp.executeCommand("Load topology");
        }
        if(actionEvent.getSource() == pretreatment){
            if(!saved) {
                SavingRouter();
                saved = true;
            }
            else{
                RedoingTheNetwork();
                saved = false;
            }
        }
    }

    private void RedoingTheNetwork() {
        tp.clearLinks();
        for(Node n : routerIPv4){
            if(!tp.getNodes().contains(n)){
                tp.addNode(n);
            }
        }
        for(Node n : routerIPv6){
            if(!tp.getNodes().contains(n)){
                tp.addNode(n);
            }
        }
        for(Link l : links){
            tp.addLink(l);
        }
    }

    private void SavingRouter() {
        routerIPv4.clear();
        routerIPv6.clear();
        links.clear();
        for(Node r : tp.getNodes()){
            if(r instanceof RouterIPv4){
                routerIPv4.add((RouterIPv4) r);
            }
            if(r instanceof RouterIPv6){
                routerIPv6.add((RouterIPv6) r);
            }
        }
        links.addAll(tp.getLinks());

        Pretreatment(tp);
    }


    private void NetworkGeneration(Topology tp) {
        Random random = new Random();
        int PlacedV4 = 0;
        int PlacedV6 = 0;
        tp.clear();
        for (int i = 0; i < nbIPv4+nbIPv6; i++) {
            if(PlacedV6 > PlacedV4) {
                RouterIPv4 r4 = new RouterIPv4();
                r4.setLocation(random.nextInt(tp.getWidth() - (tp.getWidth() / 5)) + (tp.getWidth() / 10), random.nextInt(tp.getHeight() - (tp.getHeight() / 5)) + (tp.getHeight() / 10));
                tp.addNode(r4);
                PlacedV4++;
            }
            else {
                RouterIPv6 r6 = new RouterIPv6();
                r6.setLocation(random.nextInt(tp.getWidth() - (tp.getWidth() / 5)) + (tp.getWidth() / 10), random.nextInt(tp.getHeight() - (tp.getHeight() / 5)) + (tp.getHeight() / 10));
                tp.addNode(r6);
                PlacedV6++;
            }
        }

        boolean valid = false;
        while(!valid) {
            int alea1 = random.nextInt(nbIPv4+nbIPv6);
            int alea2 = random.nextInt(nbIPv4+nbIPv6);
            while(alea1 == alea2){
                alea2 = random.nextInt(nbIPv4+nbIPv6);
            }
            Link l = new Link(tp.getNodes().get(alea1), tp.getNodes().get((alea2)));
            if (!tp.getLinks().contains(l)) {
                tp.addLink(l);
            }
            java.util.List<Node> nodesToTest = new ArrayList<>();
            List<Node> nodesMarked = new ArrayList<>();
            nodesToTest.add(tp.getNodes().get(0));
            markNodes(nodesToTest, nodesMarked);
            valid = true;
            for (Node n : tp.getNodes()) {
                if (!nodesMarked.contains(n)) {
                    valid = false;
                    break;
                }
            }
        }
    }

    private void Pretreatment(Topology tp) {
        boolean Modified = true;

        List<Node> nodesToTest;
        List<Node> neighborNodes = new ArrayList<>();
        while (Modified) {
            Modified = false;
            nodesToTest = tp.getNodes();

            boolean candidat;
            while (!nodesToTest.isEmpty()) {
                candidat = false;
                Node currentNode = nodesToTest.remove(0);
                neighborNodes.clear();
                neighborNodes = currentNode.getNeighbors();
                if (currentNode instanceof RouterIPv4) {
                    if (neighborNodes.size() == 1 && neighborNodes.get(0) instanceof RouterIPv4) {
                        tp.removeNode(currentNode);
                        Modified = true;
                    }
                    else {
                        for (Node n : neighborNodes) {
                            if (n instanceof RouterIPv6) {
                                candidat = true;
                                break;
                            }
                        }
                    }
                }
                if (currentNode instanceof RouterIPv6) {
                    if (neighborNodes.size() == 1 && neighborNodes.get(0) instanceof RouterIPv6) {
                        tp.removeNode(currentNode);
                        Modified = true;
                    }
                    else{
                        for(Node n : neighborNodes){
                            if (n instanceof RouterIPv4) {
                                candidat = true;
                                break;
                            }
                        }
                    }
                }
                if(!candidat && !Modified){
                    for(Node n1 : neighborNodes){
                        for(Node n2 : neighborNodes) {
                            if(n1 != n2){
                                Link l = new Link(n1,n2);
                                if(!tp.getLinks().contains(l)){
                                    tp.addLink(l);
                                }
                            }
                        }
                    }
                    tp.removeNode(currentNode);
                    Modified = true;
                }
            }
        }
    }

    @Override
    public void itemStateChanged(ItemEvent itemEvent) {
        if(itemEvent.getItem().equals("Router IPv4")){
            tp.setDefaultNodeModel(RouterIPv4.class);
            System.out.println("Adding IPv4 UI.Router");
        }
        else
        if(itemEvent.getItem().equals("Router IPv6")){
            tp.setDefaultNodeModel(RouterIPv6.class);
            System.out.println("Adding IPv6 UI.Router");
        }
        else {
            Algo = (String) itemEvent.getItem();
        }
    }

    public boolean connexity(Topology tp) {
        boolean valid = true;
        List<Node> nodesToTest = new ArrayList<>();
        List<Node> nodesMarked = new ArrayList<>();
        nodesToTest.add(tp.getNodes().get(0));
        markNodes(nodesToTest, nodesMarked);
        for (Node n : tp.getNodes()) {
            if (!nodesMarked.contains(n)) {
                valid = false;
                break;
            }
        }
        return valid;
    }

    private void markNodes(List<Node> nodesToTest, List<Node> nodesMarked) {
        while (!nodesToTest.isEmpty()) {
            Node currentNode = nodesToTest.remove(0);
            nodesMarked.add(currentNode);
            for(Node n : currentNode.getNeighbors()){
                if(!nodesToTest.contains(n) && !nodesMarked.contains(n)){
                    nodesToTest.add(n);
                }
            }
        }
    }

}
