package UI;

import Algorithms.SpanningTreeDistributed;
import DataStructure.MessageContent;
import io.jbotsim.core.*;

import java.util.ArrayList;
import java.util.HashMap;

public class Router extends Node {
    public boolean Converter = false;

    // : COMMENT : Need to Glouton Algorithm
    public int candidateLinkNumber = 0;

    //:COMMENT:Needed for the spanning Tree method (Distributed)
    public Router parent = null;
    //

    //:COMMENT:Needed for the aggregation of the component (Distributed)
    public SpanningTreeDistributed algorithm; //:COMMENT:Only use by the parent
    public ArrayList<Router> children = new ArrayList<>();
    public ArrayList<Router> childrenCopy = new ArrayList<>();
    public ArrayList<Integer> componentNeighbor = new ArrayList<>();
    public HashMap<Router, Integer> finalComponent = new HashMap<>();
    public int componentNumber;
    public Router needToBeConverter;
    public boolean spanningTreeCreation;
    public boolean countCandidateLink;
    public boolean placingConverter;
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
        super.onMessage(message);
    }

    public void addConverter() {
        Converter = true;
    }

    public boolean hasConverter() {
        return Converter;
    }

    public void setCandidateLinkNumberCandidate(int i) {
        candidateLinkNumber = i;
    }

    public int getCandidateLinkNumber() {
        return candidateLinkNumber;
    }

    // Set for the current router his candidate link to 0
    // and decrement for each candidat router in his neighbor list
    public void resetCandidateLinkNumber() {
        candidateLinkNumber = 0;
        for (Node n : this.getNeighbors()) {
            Router r = (Router) n;
            if (r instanceof RouterIPv4 && this instanceof RouterIPv6 || r instanceof RouterIPv6 && this instanceof RouterIPv4)
                r.decrementCandidateLinkNumber();
        }
    }

    // COMMENT : Decrement the variable needed in GloutonAlgorithm
    public void decrementCandidateLinkNumber() {
        candidateLinkNumber--;
    }

    public void setConverter() {
        this.Converter = false;
    }

    public Router getParent() {
        return parent;
    }

    public String messageHandler(Message message, Router origin) {
        MessageContent content = (MessageContent) message.getContent();
        if (message.getFlag().equals("IPv4")) {
            Router sender = (Router) message.getSender();
            if (origin.hasConverter() || sender.hasConverter() || origin.getClass().equals(sender.getClass())) {
                if (content.getCommand().equals("Aggregation")) {
                    return "Aggregation";
                } else if (content.getCommand().equals("TreeCreation")) {
                    if (origin.getParent() == null) {
                        return "Sender is new parent";
                    } else {
                        return "Already got a parent";
                    }
                } else if (content.getCommand().equals("Finding potential Converter")) {
                    return "Finding potential Converter";
                } else if (content.getCommand().equals("Checking Candidate Link")) {
                    return "Error language";
                } else if (content.getCommand().equals("Place Converter")) {
                    return "Place Converter";
                }
            }
            else {
                return "Error language";
            }
        } else if (message.getFlag().equals("IPv6")) {
            Router sender = (Router) message.getSender();
            if (origin.hasConverter() || sender.hasConverter() || origin.getClass().equals(sender.getClass())) {
                if (content.getCommand().equals("Aggregation")) {
                    return "Aggregation";
                } else if (content.getCommand().equals("TreeCreation")) {
                    if (origin.getParent() == null) {
                        return "Sender is new parent";
                    } else {
                        return "Already got a parent";
                    }
                } else if (content.getCommand().equals("Finding potential Converter")) {
                    return "Finding potential Converter";
                } else if (content.getCommand().equals("Checking Candidate Link")) {
                    return "Error language";
                } else if (content.getCommand().equals("Place Converter")) {
                    return "Place Converter";
                }
            }
            else {
                return "Error language";
            }
        }
        //:COMMENT:Should not be here
        return null;
    }

    public void spanningTreeInit(SpanningTreeDistributed algorithm) {
        parent = this;
        this.algorithm = algorithm;
        componentNumber = this.getID();
        for (Node node : this.getNeighbors()) {
            Router router = (Router) node;
            children.add(router);
            childrenCopy.add(router);
        }
        MessageContent content = new MessageContent("TreeCreation");
        if (this instanceof RouterIPv4) {
            content.setComponentNumber(componentNumber);
            for (Router child : children) {
                send(child, new Message(content, "IPv4"));
            }
        } else {
            content.setComponentNumber(componentNumber);
            for (Router child : children) {
                send(child, new Message(content, "IPv6"));
            }
        }
    }

    public void findPotentialConverter() {
        MessageContent content = new MessageContent("Finding potential Converter");
       if(parent == this) {
           checkingCandidateLink();
       }
       if(!children.isEmpty()) {
            for (Node node : this.getNeighbors()) {
                Router router = (Router) node;
                if (children.contains(router)) {
                    if (this instanceof RouterIPv4) {
                        send(router, new Message(content, "IPv4"));
                    }
                    if (this instanceof RouterIPv6) {
                        send(router, new Message(content, "IPv6"));
                    }
                }
            }
        }
        else if(childrenCopy.isEmpty()){
            if (this instanceof RouterIPv4) {
                send(parent, new Message(content, "IPv4"));
            }
            if (this instanceof RouterIPv6) {
                send(parent, new Message(content, "IPv6"));
            }
        }
    }

    public void checkingCandidateLink() {
        MessageContent content = new MessageContent("Checking Candidate Link");
        for (Node node : this.getNeighbors()) {
            Router router = (Router) node;
            if ((router != parent) && (!children.contains(router)) && (router != this) && !(router.getClass().equals(this.getClass()))) {
                childrenCopy.add(router);
                if (this instanceof RouterIPv4) {
                    content.setComponentNumber(componentNumber);
                    send(router, new Message(content, "IPv4"));
                }
                if (this instanceof RouterIPv6) {
                    content.setComponentNumber(componentNumber);
                    send(router, new Message(content, "IPv6"));
                }
            }
        }
    }

    public void placeConverter(Router converter) {
        if(parent == this && this == converter) {
            addConverter();
            algorithm.setupNewResearch();
            algorithm.newSpanningTree();
        }
        else {
            for (Node node : this.getNeighbors()) {
                Router router = (Router) node;
                if (children.contains(router)) {
                    childrenCopy.add(router);
                    MessageContent content = new MessageContent("Place Converter");
                    if (this instanceof RouterIPv4) {
                        content.setConverterRouter(converter);
                        send(router, new Message(content, "IPv4"));
                    }
                    if (this instanceof RouterIPv6) {
                        content.setConverterRouter(converter);
                        send(router, new Message(content, "IPv6"));
                    }
                }
            }
        }
    }

    public void aggregation() {
        if (spanningTreeCreation) {
            if (childrenCopy.isEmpty()) {
                int numberOfCandidateLink = 0;
                for (Node node : this.getNeighbors()) {
                    Router router = (Router) node;
                    int routerComponentNumber = router.componentNumber;
                    if (!(router.getClass().equals(this.getClass())) && routerComponentNumber != this.componentNumber)
                        numberOfCandidateLink++;
                }
                finalComponent.put(this, numberOfCandidateLink);
                if (parent != this) {
                    MessageContent content = new MessageContent("Aggregation");
                    if (this instanceof RouterIPv4) {
                        content.setComponent(finalComponent);
                        send(parent, new Message(content, "IPv4"));
                    }
                    if (this instanceof RouterIPv6) {
                        content.setComponent(finalComponent);
                        send(parent, new Message(content, "IPv6"));
                    }
                    spanningTreeCreation = false;
                } else {
                    if (childrenCopy.isEmpty()) {
                        spanningTreeCreation = false;
                        boolean checkEnd = algorithm.newSpanningTree();
                        if (checkEnd) {
                            algorithm.findPotentialConverter();
                        }
                    }
                }
            }
        }
        else if(countCandidateLink) {
            if (parent != this) {
                if(childrenCopy.isEmpty()) {
                    countCandidateLink = false;
                    MessageContent content = new MessageContent("Aggregation");
                    if (this instanceof RouterIPv4) {
                        finalComponent.put(this, candidateLinkNumber);
                        content.setComponent(finalComponent);
                        send(parent, new Message(content, "IPv4"));
                    }
                    if (this instanceof RouterIPv6) {
                        finalComponent.put(this, candidateLinkNumber);
                        content.setComponent(finalComponent);
                        send(parent, new Message(content, "IPv6"));
                    }
                }
            }
            else {
                if (childrenCopy.isEmpty()) {
                    countCandidateLink = false;
                    int maxCandidateLink = 0;
                    Router converter = null;
                    for (Router router : finalComponent.keySet()) {
                        if (finalComponent.get(router) > maxCandidateLink) {
                            maxCandidateLink = finalComponent.get(router);
                            converter = router;
                        }
                    }
                    if(converter != null) {
                        algorithm.placeConverter(converter);
                    }
                    else
                        System.out.println("FINISH");
                        algorithm.end();
                }
            }
        }
        else if(placingConverter){
            if (parent != this) {
                if(childrenCopy.isEmpty()) {
                    placingConverter = false;
                    MessageContent content = new MessageContent("Aggregation");
                    content.setConverterRouter(needToBeConverter);
                    if (this instanceof RouterIPv4) {
                        send(parent, new Message(content, "IPv4"));
                    }
                    if (this instanceof RouterIPv6) {
                        send(parent, new Message(content, "IPv6"));
                    }
                }
            }
            else {
                if (childrenCopy.isEmpty()) {
                    placingConverter = false;
                    algorithm.setupNewResearch();
                    algorithm.newSpanningTree();
                }
            }
        }
    }
}
