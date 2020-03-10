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
    public Node chooseDegrees(){
        Topology tp= getTopology();
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
        Router r=(Router)chooseDegrees();
        r.addConverter();
        r.resetCandidateLinkNumber();
        removeLinkCandidates(tp,r);
        resetConnectedComponents(tp);
        setConnectedComponents(getConnectedComponents(tp));
        candidatLink(tp);
    }


    public void algorithm(){
        Topology tp= getTopology();
        candidatLink(tp);
        setConnectedComponents(getConnectedComponents(tp));
        for(Node n: tp.getNodes()){
            if(n.getNeighbors().size()==1){
                Router u=(Router)n;
                Router v=(Router)n.getNeighbors().get(0);
                if(!u.getClass().equals(v.getClass())){
                    placeConverterOnRouter(tp,v);
                }
            }
        }
        while(getComponent().size()>1){
            placeConverterV1(tp);
        }
    }
}