package Algorithms;

import UI.ConnectedComponent;
import UI.Router;
import UI.RouterIPv4;
import UI.RouterIPv6;
import io.jbotsim.core.Link;
import io.jbotsim.core.Node;
import io.jbotsim.core.Topology;

import java.util.ArrayList;

public class NonDeterministicAlgorithm implements AlgorithmModel {

    private Topology tp;
    private ArrayList<ConnectedComponent> component = new ArrayList<>();
    private ArrayList<Link> candidatesLinks= new ArrayList<>();

    public NonDeterministicAlgorithm(Topology tp){ this.tp=tp; }

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
        ArrayList<ConnectedComponent> components = new ArrayList<>();
        for (Node r : tp.getNodes()){
            Router r1 = (Router)r;
            if(r1.getComponent()==null){
                ConnectedComponent cc1= new ConnectedComponent();
                cc1.addRouter(r1);
                r1.component=cc1;
                components.add(cc1);
            }
            for (Node rv : r1.getNeighbors()){
                Router r2 = (Router)rv;
                if (r2 instanceof RouterIPv4 && r1 instanceof RouterIPv4||
                        r2 instanceof RouterIPv6 && r1 instanceof RouterIPv6 || r2.hasConverter() || r1.hasConverter()){
                    for (ConnectedComponent cc : components){
                        if (cc.equals(r1.getComponent()) && !cc.contains(r2)){
                            cc.addRouter(r2);
                            r2.component=cc;
                        }
                    }
                }
            }
        }
        //System.out.println(components.size());
        return components;

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
            if (l.destination instanceof RouterIPv4 && l.source instanceof RouterIPv6 ||
                    l.destination instanceof RouterIPv6 && l.source instanceof RouterIPv4) {
                candidatesLinks.add(l);
            }
        }
        countCandidatesLink(tp);
    }

    //:COMMENT : Remove candidates links of a router r
    public void removeLinkCandidates(Topology tp,Router r){
        ArrayList<Link> tmp= new ArrayList<>();
        for (Link l : candidatesLinks){
            if(!(l.destination.equals(r) || l.source.equals(r))){
                tmp.add(l);
            }
        }
        candidatesLinks=tmp;
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
        //System.out.println(r.getID() +" " );
        r.addConverter();
        r.resetCandidateLinkNumber();
        removeLinkCandidates(tp,r);
        resetConnectedComponents(tp);
        setConnectedComponents(getConnectedComponents(tp));
    }

    public void placeConverterLeaf(Topology tp,Router r){
        r.addConverter();
        r.resetCandidateLinkNumber();
        removeLinkCandidates(tp,r);
        resetConnectedComponents(tp);
        System.out.println(component.size() +"avant");
        setConnectedComponents(getConnectedComponents(tp));
        System.out.println(component.size() +"après");
    }


    public void algorithm(){
        setConnectedComponents(getConnectedComponents(tp));
        for (ConnectedComponent cc : component){
            System.out.println(cc.toString());
        }
        candidatLink(tp);
        for(Node n: tp.getNodes()){
            if(n.getNeighbors().size()==1){
                Router u=(Router)n;
                Router v=(Router)n.getNeighbors().get(0);
                if(u instanceof RouterIPv6 && v instanceof RouterIPv4 || u instanceof RouterIPv4 && v instanceof RouterIPv6 ){
                    placeConverterLeaf(tp,v);
                }
            }
        }
        while(stillaRouterToChoose()){
            placeConverterV1(tp);
        }
    }
}