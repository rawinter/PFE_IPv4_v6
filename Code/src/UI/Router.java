package UI;

import Algorithms.SpanningTreeDistributed;
import DataStructure.SpanningTreeStructure;
import io.jbotsim.core.*;

import java.util.ArrayList;
import java.util.List;

public class Router extends Node {
    public boolean Converter = false;
    public ConnectedComponent component=null;
    public int candidateLinkNumber=0;

    //:COMMENT:Needed for the Spanning Tree method
    Router parent = null;
    SpanningTreeDistributed algorithmSpanningTreeDistributed;
    //

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onSelection() {
        super.onSelection();
    }

    @Override
    public void onClock() {
        super.onClock();
    }

    @Override
    public void onMessage(Message message) {
        if (message.getFlag().equals("TreeCreation") || message.getFlag().equals("TreeCreationLastLink")) {
            if(algorithmSpanningTreeDistributed == null)
                algorithmSpanningTreeDistributed = new SpanningTreeDistributed();
            parent = algorithmSpanningTreeDistributed.spanningTreeDistributed(message, this, parent);
        }
    }

    public void addConverter(){Converter = true;}

    public boolean hasConverter(){
        return Converter;
    }

    public void setCandidateLinkNumberCandidate(int i){ candidateLinkNumber=i; }

    public int getCandidateLinkNumber(){ return candidateLinkNumber; }

    public ConnectedComponent getComponent(){return component; }

    public void resetCandidateLinkNumber(){
        candidateLinkNumber=0;
        for (Node n : this.getNeighbors()){
            Router r= (Router)n;
            if(r instanceof RouterIPv4 && this instanceof RouterIPv6 || r instanceof RouterIPv6 && this instanceof RouterIPv4)
            r.decrementCandidateLinkNumber();
        }
    }

    public void decrementCandidateLinkNumber(){candidateLinkNumber--;}

    public void setComponent(ConnectedComponent cc){ component=cc; }

    public void spanningTree(List<Router> routersList, List<ConnectedComponent> connectedComponentsList)
    {
        parent = this;
        sendAll(new Message(new SpanningTreeStructure(routersList, connectedComponentsList), "TreeCreation"));
    }
}
