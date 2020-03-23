package Algorithms;

import UI.ConnectedComponent;
import UI.Router;
import UI.RouterIPv4;
import UI.RouterIPv6;
import io.jbotsim.core.Link;
import io.jbotsim.core.Node;
import io.jbotsim.core.Topology;

import java.util.ArrayList;
import java.util.List;

public abstract class AlgorithmNonDistributed  {
    private static  Topology topology;
    private static ArrayList<ConnectedComponent> components = new ArrayList<>();

    public AlgorithmNonDistributed (Topology tp){
        this.topology=tp;
    }



    public Topology getTopology(){ return topology; }

    public static ArrayList<ConnectedComponent> getComponent(){ return components;}

    public static void setConnectedComponents(ArrayList<ConnectedComponent> l){ components= l; }

    //COMMENT :  return a list of the connected components of the topology
    public  ArrayList<ConnectedComponent> getConnectedComponents(Topology tp){
        ArrayList<ConnectedComponent> components = new ArrayList<>();
        List<Node> nodes = tp.getNodes();
        while(!nodes.isEmpty()){
            Router r1 = (Router) nodes.get(0);
            nodes.remove(r1);
            ConnectedComponent cc1= new ConnectedComponent();
            ArrayList<Router> connected = new ArrayList<>();
            connected.add(r1);
            ArrayList<Router> result = recursiveSameClassNeighbor(connected, r1, nodes);
            for(Router router : result)
                cc1.addRouter(router);
            components.add(cc1);
        }

        return components;
    }

    // COMMENT : recursive function that check if neighbor is same type of router and add
    // into the current component
    public  ArrayList<Router> recursiveSameClassNeighbor(ArrayList<Router> connected, Router previous, List<Node> nodes) {
        List<Node> neighbors = previous.getNeighbors();
        for(Router router : connected) {
            if(neighbors.contains(router)) {
                neighbors.remove(router);
            }
        }
        for (Node n : neighbors){
            Router actual = (Router) n;
            if (previous.getClass().equals(actual.getClass())
                    || actual.hasConverter() || previous.hasConverter()){
                if(!connected.contains(actual)) {
                    connected.add(actual);
                    recursiveSameClassNeighbor(connected, actual, nodes);
                }
                nodes.remove(n);
            }
        }
        return connected;
    }
















}
