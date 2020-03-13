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
    private ArrayList<Link> candidatesLinks= new ArrayList<>();
    private List<Node> candidatesNodes = new ArrayList<>();
    private static int converterToPlace;
    public int numberConnectedComponents;

    public AlgorithmNonDistributed (Topology tp){
        this.topology=tp;
    }
    public List<Node> NodeCandidates(){
        List<Node> nodes = new ArrayList<>();
        for(Link l : topology.getLinks()){
            Router source = (Router)l.source;
            Router destination= (Router)l.destination;
            if(source instanceof RouterIPv4 && destination instanceof RouterIPv6
                    ||source instanceof RouterIPv6 && destination instanceof RouterIPv4 ){
                if(!nodes.contains(source))
                    nodes.add(l.source);
                if (!nodes.contains(destination))
                    nodes.add(l.destination);
            }
        }
        return nodes;
    }

    public Topology getTopology(){ return topology; }

    public static ArrayList<ConnectedComponent> getComponent(){ return components;}

    public static void setConnectedComponents(ArrayList<ConnectedComponent> l){ components= l; }

    public List<Node> getCandidatesNodes(){ return candidatesNodes; }

    public static void resetConnectedComponents(Topology tp){
        for(Node n: tp.getNodes()){
            Router r = (Router)n;
            r.setComponent(null);
        }
    }
    public static ArrayList<ConnectedComponent> getConnectedComponents(Topology tp){
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

    public static ArrayList<Router> recursiveSameClassNeighbor(ArrayList<Router> connected, Router previous, List<Node> nodes) {
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

    public void defineConverterToPlace(){ converterToPlace=getConnectedComponents(topology).size()-1; }

    public int getNbConverterToplace(){ return converterToPlace; }

    public void countCandidatesLink(Topology tp) {
        for (Node n : tp.getNodes()) {
            int cpt=0;
            Router r=(Router)n;
            for (Link l : candidatesLinks) {
                if (l.source.equals(n) || l.destination.equals(n)) {
                    cpt++;
                }
            }
            r.setCandidateLinkNumberCandidate(cpt);
        }
    }
    //:COMMENT : Remove candidates links of a router r
    public void removeLinkCandidates(Topology tp,Router r){
        candidatesLinks.clear();
        countCandidatesLink(tp);
    }
    public void candidatLink(Topology tp) {
        for (Link l : tp.getLinks()) {
            Router source = (Router) l.source;
            Router destination = (Router) l.destination;
            boolean containInComponent = false;
            if (!(source.getClass().equals(destination.getClass()) ||
                    source.hasConverter() || destination.hasConverter())) {
                for(ConnectedComponent cc : getComponent()){
                    if(cc.contains(source) && cc.contains(destination))
                    {
                        containInComponent = true;
                    }
                }
                if(!containInComponent)
                    candidatesLinks.add(l);
            }
        }
        if (this instanceof GloutonAlgorithm) {
            countCandidatesLink(tp);
        }
    }


    public void placeConverterOnRouter(Topology tp,Router r){
        r.addConverter();
        r.resetCandidateLinkNumber();
        removeLinkCandidates(tp,r);
        resetConnectedComponents(tp);
        setConnectedComponents(getConnectedComponents(tp));

        candidatLink(tp);
    }






}
