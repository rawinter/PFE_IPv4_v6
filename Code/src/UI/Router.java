package UI;

import io.jbotsim.core.Message;
import io.jbotsim.core.Node;

public class Router extends Node {
    public boolean Converter = false;
    public ConnectedComponent component=null;
    public int candidateLinkNumber=0;

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

    public void addConverter(){Converter = true;}

    public boolean hasConverter(){
        return Converter;
    }

    public void incrementCandidate(){ candidateLinkNumber++; }

    public int getCandidateLinkNumber(){ return candidateLinkNumber; }

    public ConnectedComponent getComponent(){return component; }

    public void resetCandidateLinkNumber(){candidateLinkNumber=0;}

    public void decrementCandidateLinkNumber(){candidateLinkNumber--;}

    public void setComponent(ConnectedComponent cc){ component=cc; }
}
