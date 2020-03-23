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

public class GloutonAlgorithm extends AlgorithmNonDistributed {
    private ArrayList<Link> candidatesLinks= new ArrayList<>();

    public GloutonAlgorithm(Topology tp){ super(tp);}

    //:COMMENT : choose the router with the most candidate links
    public Node chooseDegrees(Topology tp){
        Router tmp=(Router)tp.getNodes().get(0);
        for (Node n : tp.getNodes()){
            Router r = (Router)n;
            if(r.getCandidateLinkNumber()>=tmp.getCandidateLinkNumber()){
                tmp=r;
            }
        }
        return tmp;

    }

    public void placeConverterV1(Topology tp){
        Router r=(Router)chooseDegrees(tp);
        placeConverterOnRouter(tp,r);
    }

    //COMMENT : Set the variable CandidateNumberLink for all router by
    // calculing all the candidates Link of a router
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
    //: COMMENT : Determine and add all the possible candidats Link of the topology
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
        countCandidatesLink(tp);
    }
    //:COMMENT : Remove candidates links
    public void removeLinkCandidates(Topology tp){
        candidatesLinks.clear();
    }

    //COMMENT : Place a converter on the router r
    public void placeConverterOnRouter(Topology tp,Router r){
        r.addConverter();
        r.resetCandidateLinkNumber();
        removeLinkCandidates(tp);
        setConnectedComponents(getConnectedComponents(tp));
        candidatLink(tp);
        countCandidatesLink(tp);

    }

    public void algorithm(){
        Topology tp= getTopology();
        candidatLink(tp);
        setConnectedComponents(getConnectedComponents(tp));
        //We check first for all the leaf in the graph if its
        //parent is the same type of router or not and place
        //converter on it
        for(Node n: tp.getNodes()){
            if(n.getNeighbors().size()==1){
                Router u=(Router)n;
                Router v=(Router)n.getNeighbors().get(0);
                if((!u.getClass().equals(v.getClass()) && (!u.hasConverter() && !v.hasConverter() ))){
                    placeConverterOnRouter(tp,v);
                }
            }
        }
        while(getComponent().size()>1){
            placeConverterV1(tp);
        }
        return;

    }
}