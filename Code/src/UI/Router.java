package UI;

import Algorithms.SpanningTreeDistributed;
import DataStructure.MessageContentIPv4;
import DataStructure.MessageContentIPv6;
import DataStructure.SpanningTreeStructure;
import io.jbotsim.core.*;

import java.util.ArrayList;
import java.util.List;

public class Router extends Node {
    public boolean Converter = false;
    public ConnectedComponent component=null;
    public int candidateLinkNumber=0;
    public boolean marked=false;
    //:COMMENT:Needed for the spanning Tree method
    Router parent = null;
    ArrayList<Router> componentRouter;
    public static final Object lock = new Object();
    //

    //:COMMENT:Needed for the aggregation
    public ArrayList<Router> children = new ArrayList<>();
    public ArrayList<Router> childrenCopy = new ArrayList<>();
    public boolean spanningTreeCreation = false;
    public boolean aggregation = false;
    ArrayList<Router> finalComponent = new ArrayList<>();
    public SpanningTreeDistributed algorithm; //:COMMENT:Only use by the parent
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
        if(aggregation) {
            if(childrenCopy.isEmpty()) {
                ArrayList<Router> ownComponent = new ArrayList<>();
                ownComponent.add(this);
                if(parent != this) {
                    if(this instanceof RouterIPv4) {
                        MessageContentIPv4 content = new MessageContentIPv4("AggregationIPv4");
                        content.setComponent(ownComponent);
                        send(parent, new Message(content, "IPv4"));
                    }
                    if(this instanceof RouterIPv6) {
                        MessageContentIPv6 content = new MessageContentIPv6("AggregationIPv6");
                        content.setComponent(ownComponent);
                        send(parent, new Message(content, "IPv6"));
                    }
                    spanningTreeCreation = false;
                    aggregation = false;
                }
                else {
                    finalComponent.add(this);
                    aggregation = false;
                    spanningTreeCreation = false;
                    System.out.println("-------------------------------");
                    System.out.println("Component of the node " + this.getID() + " :");
                    for (Router router : finalComponent) {
                        System.out.print(" " + router.getID() + ",");
                    }
                    System.out.println(".");
                    System.out.println("-------------------------------");
                    algorithm.newSpanningTree();
                }
            }
        }
    }

    @Override
    public void onMessage(Message message) {
        super.onMessage(message);
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

    public void setConverter(){
        this.Converter=false;
    }

    public Router getParent() {
        return parent;
    }

    public void spanningTreeInit(SpanningTreeDistributed algorithm) {
        parent = this;
        this.algorithm = algorithm;
        for(Node node : this.getNeighbors()) {
            Router router = (Router) node;
            children.add(router);
            childrenCopy.add(router);
        }
        if(this instanceof RouterIPv4) {
            MessageContentIPv4 messageContent = new MessageContentIPv4("TreeCreationIPv4");
            sendAll(new Message(messageContent, "IPv4"));
        }
        else {
            MessageContentIPv6 messageContent = new MessageContentIPv6("TreeCreationIPv6");
            sendAll(new Message(messageContent, "IPv6"));
        }
    }

    public String spanningTreeCreation(Message message, Router origin) {
        if (message.getFlag().equals("IPv4")) {
            MessageContentIPv4 content = (MessageContentIPv4) message.getContent();
            if (!origin.getClass().equals(message.getSender().getClass())) {
                return "Error language";
            }
            else if(content.getCommandIPv4().equals("AggregationIPv4") && origin instanceof RouterIPv4) {
                return "AggregationIPv4";
            }
            else if(content.getCommandIPv4().equals("TreeCreationIPv4") && origin instanceof RouterIPv4) {
                if (origin.getParent() == null) {
                    return "Sender is new parent";
                } else {
                    return "Already got a parent";
                }
            }

        }
        else if (message.getFlag().equals("IPv6")) {
            MessageContentIPv6 content = (MessageContentIPv6) message.getContent();
            if (!origin.getClass().equals(message.getSender().getClass())) {
                return "Error language";
            }
            else if(content.getCommandIPv6().equals("AggregationIPv6") && origin instanceof RouterIPv6) {
                return "AggregationIPv6";
            }
            else if(content.getCommandIPv6().equals("TreeCreationIPv6") && origin instanceof RouterIPv6) {
                if (origin.getParent() == null) {
                    return "Sender is new parent";
                } else {
                    return "Already got a parent";
                }
            }
        }
        //:COMMENT:Should not be here
        return null;
    }
}
