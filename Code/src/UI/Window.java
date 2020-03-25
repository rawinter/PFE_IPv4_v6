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

    private JTextField nbConv = new JTextField();
    private JTextField nbComp = new JTextField();

    private JMenu Legend = new JMenu("Legend");
    private JMenuItem v4 = new JMenuItem("IPv4 Router");
    private JMenuItem v6 = new JMenuItem("IPv6 Router");
    private JMenuItem Conv = new JMenuItem("Router with a converter");


    private JMenuItem gloutonAlgorithm = new JMenuItem("Launch glouton algorithm");
    private JMenuItem distributedAlgorithm = new JMenuItem("Launch distributed algorithm");
    private JMenuItem exactAlgorithm = new JMenuItem("Launch exact algorithm");
    private JButton gloutonAlgo = new JButton("Launch glouton algorithm");
    private JButton distributedAlgo = new JButton("Launch distributed algorithm");
    private JButton exactAlgo = new JButton("Launch exact algorithm");
    private JMenuItem save = new JMenuItem("Save network");
    private JMenuItem load = new JMenuItem("Load network");
    private JMenuItem reset = new JMenuItem("Reset the topology");
    private JMenuItem clear = new JMenuItem("clear");

    private JMenuItem treeConnexite = new JMenuItem("Make the connexe component appear");

    private JMenuItem networkGeneration = new JMenuItem("Network generation");
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
    Font font = new Font("Garamond", Font.BOLD, 15);



    public Window(Topology topo) {
        super(topo, true);
        window.addComponentListener(new ComponentListener() {
            @Override
            public void componentResized(ComponentEvent componentEvent) {
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

        algorithm.add(treeConnexite);
        algorithm.add(pretreatment);
        algorithm.add(clear);
        algorithm.add(reset);
        algorithm.addSeparator();
        algorithm.add(gloutonAlgorithm);
        algorithm.add(distributedAlgorithm);
        algorithm.add(exactAlgorithm);

        menuBar.add(menuLoadandSave);
        menuBar.add(generation);
        menuBar.add(algorithm);
        menuBar.add(gloutonAlgo);
        menuBar.add(distributedAlgo);
        menuBar.add(exactAlgo);
        menuBar.add(nbConv);
        menuBar.add(nbComp);
        menuBar.add(Legend);
        nbConv.setEnabled(false);
        nbConv.setFont(font);
        nbComp.setEnabled(false);
        nbComp.setFont(font);
        Legend.setFont(font);
        ImageIcon tmpimg = new ImageIcon("Code/Ressources/images/IPv4.png");
        Image image = tmpimg.getImage();
        Image scaledimg = image.getScaledInstance(20, 20, java.awt.Image.SCALE_SMOOTH);
        tmpimg = new ImageIcon(scaledimg);
        v4.setIcon(tmpimg);
        Legend.add(v4);
        tmpimg = new ImageIcon("Code/Ressources/images/IPv6.png");
        image = tmpimg.getImage();
        scaledimg = image.getScaledInstance(20, 20, java.awt.Image.SCALE_SMOOTH);
        tmpimg = new ImageIcon(scaledimg);
        v6.setIcon(tmpimg);
        Legend.add(v6);
        tmpimg = new ImageIcon("Code/Ressources/images/Converter.png");
        image = tmpimg.getImage();
        scaledimg = image.getScaledInstance(20, 20, java.awt.Image.SCALE_SMOOTH);
        tmpimg = new ImageIcon(scaledimg);
        Conv.setIcon(tmpimg);
        Legend.add(Conv);
        menuBar.add(Legend);



        window.setJMenuBar(menuBar);

        pan.setLayout(new BorderLayout());

        load.addActionListener(this);
        save.addActionListener(this);
        networkGeneration.addActionListener(this);
        IPv4.addActionListener(this);
        IPv6.addActionListener(this);
        treeConnexite.addActionListener(this);
        pretreatment.addActionListener(this);
        gloutonAlgorithm.addActionListener(this);
        distributedAlgorithm.addActionListener(this);
        exactAlgorithm.addActionListener(this);
        gloutonAlgo.addActionListener(this);
        distributedAlgo.addActionListener(this);
        exactAlgo.addActionListener(this);
        clear.addActionListener(this);
        reset.addActionListener(this);

        load.setFont(font);
        save.setFont(font);
        networkGeneration.setFont(font);
        IPv4.setFont(font);
        IPv6.setFont(font);
        treeConnexite.setFont(font);
        pretreatment.setFont(font);
        gloutonAlgorithm.setFont(font);
        distributedAlgorithm.setFont(font);
        exactAlgorithm.setFont(font);
        gloutonAlgo.setFont(font);
        distributedAlgo.setFont(font);
        exactAlgo.setFont(font);
        generation.setFont(font);
        algorithm.setFont(font);
        menuLoadandSave.setFont(font);
        clear.setFont(font);
        reset.setFont(font);

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
        if(actionEvent.getSource() == clear){
            tp.clear();
            nbConv.setText("");
            nbComp.setText("");
        }
        if(actionEvent.getSource() == reset){
            for(Node n : tp.getNodes()){
                if(n instanceof Router){
                    if(((Router) n).hasConverter()){
                        ((Router) n).setConverter();
                    }
                }
            }
            nbConv.setText("Number of converter : 0");
            nbConv.setHorizontalAlignment(JTextField.CENTER);
        }
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
                JOptionPane.showMessageDialog(null,"No number specified default at 10","Error",JOptionPane.ERROR_MESSAGE);
            } catch (HeadlessException e) {
                System.out.println("Headless");
            }
            try {
                nbIPv6 = Integer.parseInt(JOptionPane.showInputDialog(null, "number of ipv6 router", "Network generation", JOptionPane.QUESTION_MESSAGE));
            } catch (NumberFormatException e) {
                System.out.println("No number specified default at 10");
                JOptionPane.showMessageDialog(null,"No number specified default at 10","Error",JOptionPane.ERROR_MESSAGE);
            } catch (HeadlessException e) {
                System.out.println("Headless");
            }
            NetworkGeneration(tp);
        }
        if(actionEvent.getSource() == gloutonAlgorithm || actionEvent.getSource() == gloutonAlgo) {
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
        if(actionEvent.getSource() == distributedAlgorithm || actionEvent.getSource() == distributedAlgo) {
            if(!connexity(tp)){
                System.out.println("the graph is not connexe can't continue");
            }
            else {
                SavingRouter();

                SpanningTreeDistributed algorithm = new SpanningTreeDistributed(tp, this);
                for (Node node : tp.getNodes()) {
                    Router router = (Router) node;
                    router.spanningTreeCreation = true;
                }
                algorithm.newSpanningTree();

                //RedoingTheNetwork(); //:COMMENT:Used at the end of the algorithm
            }
        }
        if(actionEvent.getSource() == exactAlgorithm || actionEvent.getSource() == exactAlgo) {
            if(!connexity(tp)){
                System.out.println("the graph is not connexe can't continue");
            }
            else {
//                SavingRouter();

            ExactAlgorithm exact = new ExactAlgorithm(tp);
            exact.algorithm();

//                RedoingTheNetwork();
            }
        }
        if(actionEvent.getSource() == treeConnexite){
            tp.executeCommand("Find every Connected Component");
            nbComp.setText("Number of connexe component : " + numberOfConnectedComponent());
            nbComp.setHorizontalAlignment(JTextField.CENTER);
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

    public void RedoingTheNetwork() {
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
        int c = 0;
        for(Node n : tp.getNodes()){
            if(n instanceof Router){
                if(((Router) n).hasConverter()){
                    c++;
                }
            }
        }
        nbConv.setText("Number of converter : " + c);
        nbConv.setHorizontalAlignment(JTextField.CENTER);
        nbComp.setText("Number of connexe component : " + numberOfConnectedComponent());
        nbComp.setHorizontalAlignment(JTextField.CENTER);
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
            for(Node n : tp.getNodes()){
                for(Node m : tp.getNodes()){
                    if(n != m){
                        Link l = new Link(n,m);
                        if(!tp.getLinks().contains(l)){
                            if (Math.random() < 0.2){
                                tp.addLink(l);
                            }
                        }
                    }
                }
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
        try {
            nodesToTest.add(tp.getNodes().get(0));
        } catch (Exception e) {
            System.out.println("there is no graph please generate or create one");
             JOptionPane.showMessageDialog(null,"there is no graph please generate or create one","Error",JOptionPane.ERROR_MESSAGE);
        }
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

    private int numberOfConnectedComponent() {
        boolean checkEnd = false;
        int totalConnectedComponent = 0;
        ArrayList<Router> allRouters = new ArrayList<>();
        for(Node node : tp.getNodes()) {
            allRouters.add((Router) node);
            ((Router) node).spanningTreeCreation = true;
        }
        while(!checkEnd) {
            for(Router router : allRouters) {
                if(router.spanningTreeCreation) {
                    findComponent(router);
                    totalConnectedComponent++;
                    break;
                }
            }
            checkEnd = true;
            for(Router router : allRouters) {
                if(router.spanningTreeCreation) {
                    checkEnd = false;
                    break;
                }
            }
        }
        return totalConnectedComponent;
    }

    public boolean findComponent(Router router) {
        if(router.spanningTreeCreation) {
            router.spanningTreeCreation = false;
            for(Node node : router.getNeighbors()) {
                Router neighbor = (Router) node;
                if(neighbor.getClass().equals(router.getClass()) && neighbor.spanningTreeCreation) {
                    findComponent(neighbor);
                }
            }
        }
        return true;
    }

}
