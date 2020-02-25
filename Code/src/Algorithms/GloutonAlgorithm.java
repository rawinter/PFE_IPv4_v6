package Algorithms;

import UI.ConnectedComponent;
import UI.Router;
import UI.RouterIPv4;
import UI.RouterIPv6;
import io.jbotsim.core.Link;
import io.jbotsim.core.Node;
import io.jbotsim.core.Topology;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class GloutonAlgorithm implements AlgorithmModel {

    private Topology tp;
    private ArrayList<ConnectedComponent> component = new ArrayList<>();
    private ArrayList<Link> candidatesLinks= new ArrayList<>();

    public GloutonAlgorithm(Topology tp){ this.tp=tp; }

    public void addComponents(ConnectedComponent c){ component.add(c); }

    public void setConnectedComponents(ArrayList<ConnectedComponent> l){
        component= l;
    }

    public void resetConnectedComponents(Topology tp){
        for(Node n: tp.getNodes()){
            Router r = (Router)n;
            r.setComponent(null);
        }
    }

    public ArrayList<ConnectedComponent> getConnectedComponents(Topology tp){
        System.out.println();
        System.out.println("Lien candidats :" + candidatesLinks.size());
        for(Link l : candidatesLinks)
            System.out.println("Source : " + l.source.getID() + ", Destination : " + l.destination.getID());
        System.out.println();
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

    public ArrayList<Router> recursiveSameClassNeighbor(ArrayList<Router> connected, Router previous, List<Node> nodes) {
        List<Node> neighbors = previous.getNeighbors();
        for(Router router : connected) {
            if(neighbors.contains(router)) {
                neighbors.remove(router);
            }
        }
        for (Node n : neighbors){
            Router actual = (Router) n;
            if (previous.getClass().equals(actual.getClass()) || actual.hasConverter() || previous.hasConverter()){
                if(!connected.contains(actual)) {
                    connected.add(actual);
                    recursiveSameClassNeighbor(connected, actual, nodes);
                }
                nodes.remove(n);
            }
        }
        return connected;
    }

    //:COMMENT : Increment the variables candidateLinkNumber of each router if it has a candidateLink
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

    public void candidatLink(Topology tp) {
        for (Link l : tp.getLinks()) {
            Router source = (Router) l.source;
            Router destination = (Router) l.destination;
            boolean containInComponent = false;
            if (!(source.getClass().equals(destination.getClass()) || source.hasConverter() || destination.hasConverter())) {
                for(ConnectedComponent cc : component){
                    if(cc.contains(source) && cc.contains(destination))
                    {
                        containInComponent = true;
                    }
                }
                if(!containInComponent)
                    candidatesLinks.add(l);
            }
        }
        countCandidatesLink(tp);
    }

    //:COMMENT : Remove candidates links of a router r
    public void removeLinkCandidates(Topology tp,Router r){
        candidatesLinks.clear();
        countCandidatesLink(tp);
    }

    //:COMMENT : choose the router with the most candidate links
    public Node chooseDegrees(){
        Router tmp=(Router)tp.getNodes().get(0);
        for (Node n : tp.getNodes()){
            Router r = (Router)n;
            if(r.getCandidateLinkNumber()>=tmp.getCandidateLinkNumber()){
                tmp=r;
            }
        }
        return tmp;
    }

    //:COMMENT : Stop condition, if still a router which can becomes a converter
    public boolean stillaRouterToChoose(){
        for(Node n : tp.getNodes()){
            Router r=(Router)n;
            if(r.getCandidateLinkNumber()>0){
                return true;
            }
        }
        return false;
    }

    public void placeConverterV1(Topology tp){
        Router r=(Router)chooseDegrees();
        System.out.println(r.getID() +" " );
        r.addConverter();
        r.resetCandidateLinkNumber();
        removeLinkCandidates(tp,r);
        System.out.println("V1");
        for (ConnectedComponent cc : component){
            System.out.println(cc.toString());
        }
        System.out.println();
        resetConnectedComponents(tp);
        setConnectedComponents(getConnectedComponents(tp));
        candidatLink(tp);
    }

    public void placeConverterLeaf(Topology tp,Router r){
        System.out.println(r.getID() +" " );
        r.addConverter();
        r.resetCandidateLinkNumber();
        removeLinkCandidates(tp,r);
        System.out.println("Leaf");
        for (ConnectedComponent cc : component){
            System.out.println(cc.toString());
        }
        System.out.println();
        resetConnectedComponents(tp);
        setConnectedComponents(getConnectedComponents(tp));
        candidatLink(tp);
    }


    public void algorithm(){
        candidatLink(tp);
        setConnectedComponents(getConnectedComponents(tp));
        for(Node n: tp.getNodes()){
            if(n.getNeighbors().size()==1){
                Router u=(Router)n;
                Router v=(Router)n.getNeighbors().get(0);
                if(u instanceof RouterIPv6 && v instanceof RouterIPv4 || u instanceof RouterIPv4 && v instanceof RouterIPv6 ){
                    placeConverterLeaf(tp,v);
                }
            }
        }
        while(!candidatesLinks.isEmpty()){
            placeConverterV1(tp);
        }
    }
}