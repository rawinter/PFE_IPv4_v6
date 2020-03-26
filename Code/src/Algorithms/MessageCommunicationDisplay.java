package Algorithms;

import UI.Router;
import io.jbotsim.contrib.algos.Connectivity;
import io.jbotsim.core.Color;
import io.jbotsim.core.Link;
import io.jbotsim.core.Node;
import io.jbotsim.core.Topology;

import java.util.ArrayList;
import java.util.HashMap;

public class MessageCommunicationDisplay {

    HashMap<Router,Router> componentSender = new HashMap<>();
    Router sender;
    Topology tp;

    public void setTopology(Topology tp) {
        this.tp = tp;
    }

    public void messageDisplay(Router base) {
        if(sender == null) {
            for(Link link : tp.getLinks()) {
                link.setWidth(Link.DEFAULT_WIDTH);
            }
            ArrayList<Router> allRouters = new ArrayList<>();
            for (Node node : tp.getNodes()) {
                allRouters.add((Router) node);
                ((Router) node).spanningTreeCreation = true;
            }
            componentSender.put(base, base);
            base.parentMessageDisplay = base;
            findComponent(base);
            for (Router router : allRouters) {
                router.spanningTreeCreation = false;
            }
            sender = base;
        }
        else {
            if(componentSender.containsKey(base)) {
                displayPath(base.parentMessageDisplay, base);
            }
            else {
                System.out.println("Pas de chemin existant");
            }
            componentSender.clear();
            sender = null;
        }
    }

    private boolean findComponent(Router router) {
        if(router.spanningTreeCreation) {
            router.spanningTreeCreation = false;
            for(Node node : router.getNeighbors()) {
                Router neighbor = (Router) node;
                if((neighbor.getClass().equals(router.getClass()) || neighbor.hasConverter() || router.hasConverter()) && neighbor.spanningTreeCreation) {
                    componentSender.put(neighbor, router);
                    neighbor.parentMessageDisplay = router;
                    findComponent(neighbor);
                }
            }
        }
        return true;
    }

    private void displayPath(Router router, Router sender) {
        Link link = tp.getLink(router, sender);
        link.setWidth(5);
        if(router.parentMessageDisplay != router) {
            displayPath(router.parentMessageDisplay, router);
        }
    }

}
