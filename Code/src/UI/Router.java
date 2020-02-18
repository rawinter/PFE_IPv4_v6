package UI;

import io.jbotsim.core.Message;
import io.jbotsim.core.Node;

public class Router extends Node {
    public boolean Converter = false;
    public boolean connectedComponents=false;
    public ConnectedComponent component;
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
}
