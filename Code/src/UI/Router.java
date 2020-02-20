package UI;

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
        if(message.getFlag().equals("TreeCreation")) {
            if (parent == null) {
                SpanningTreeStructure content = (SpanningTreeStructure) message.getContent();
                parent = (Router) message.getSender();
                content.routersList.remove(this);
                if (parent.getClass().equals(this.getClass())) {
                    getCommonLinkWith(message.getSender()).setWidth(4);
                    if (this instanceof RouterIPv4) {
                        getCommonLinkWith(message.getSender()).setColor(Color.RED);
                    }
                    if (this instanceof RouterIPv6) {
                        getCommonLinkWith(message.getSender()).setColor(Color.BLUE);
                    }
                    for (ConnectedComponent component : content.connectedComponentsList) {
                        if (component.contains(parent)) {
                            component.addRouter(this);
                        }
                    }
                }
                else {
                    getCommonLinkWith(message.getSender()).setColor(Color.GREEN);
                    ConnectedComponent newOne = new ConnectedComponent();
                    newOne.addRouter(this);
                    content.connectedComponentsList.add(newOne);
                }

                sendAll(new Message(new SpanningTreeStructure(content.routersList, content.connectedComponentsList), "TreeCreation"));

            }
            else {
                for (Node neighbor : this.getOutNeighbors())
                {
                    if (getCommonLinkWith(neighbor).getColor().equals(Color.RED) || getCommonLinkWith(neighbor).getColor().equals(Color.BLUE) || getCommonLinkWith(neighbor).getColor().equals(Color.GREEN)) {
                        //:COMMENT:Need to modify this statement to simplify it
                    }
                    else {
                        send(neighbor, new Message(this,"TreeCreationLastLink"));
                        break;
                    }
                }
            }
        }
        //:COMMENT:This type of message is used to color the link which does not belong to the spanning tree
        if(message.getFlag().equals("TreeCreationLastLink")) {
            Router sender = (Router) message.getSender();
            if(sender.getClass().equals(this.getClass())) {
                getCommonLinkWith(sender).setWidth(4);
                if(this instanceof RouterIPv4) {
                    getCommonLinkWith(message.getSender()).setColor(Color.RED);
                }
                if (this instanceof RouterIPv6) {
                    getCommonLinkWith(message.getSender()).setColor(Color.BLUE);
                }
            }
            else {
                getCommonLinkWith(message.getSender()).setColor(Color.GREEN);
            }
        }
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

    public void spanningTree(List<Router> routersList, List<ConnectedComponent> connectedComponentsList)
    {
        parent = this;
        sendAll(new Message(new SpanningTreeStructure(routersList, connectedComponentsList), "TreeCreation"));
    }
}
