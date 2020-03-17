package Algorithms;

import UI.Router;
import io.jbotsim.core.Node;
import io.jbotsim.core.Topology;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class SpanningTreeDistributed implements AlgorithmModel {

    private Topology tp;
    boolean initCentral = false;
    Router central = null;

    public SpanningTreeDistributed(Topology tp) {
        this.tp = tp;
    }

    public void setupNewResearch() {
        for (Node node : tp.getNodes()) {
            Router router = (Router) node;
            router.parent = null;
            router.children.clear();
            router.childrenCopy.clear();
            router.componentNeighbor.clear();
            router.finalComponent.clear();
            router.componentNumber = -1;
            router.candidateLinkNumber = 0;
            router.spanningTreeCreation = true;
            router.countCandidateLink = false;
            router.placingConverter = false;
            router.needToBeConverter = null;
        }
    }

    public boolean newSpanningTree() {
        Random random = new Random();
        List<Router> routersList = new ArrayList<>();
        boolean needToPerform = false;
        for (Node node : tp.getNodes()) {
            Router router = (Router) node;
            if(router.spanningTreeCreation) {
                needToPerform = true;
                routersList.add(router);
            }
        }

        if(needToPerform) {
            Router parent = central;
            if(central == null || central.spanningTreeCreation == false) {
                parent = routersList.get(0);
            }
            parent.spanningTreeInit(this);
            return false;
        }
        else {
            return true;
        }
    }

    public boolean findPotentialConverter() {
        if(initCentral == false) {
            for (Node node : tp.getNodes()) {
                Router router = (Router) node;
                if(router.getParent() == router && router.finalComponent.size() > 1) {
                    central = router;
                    initCentral = true;
                    break;
                }
            }
            if(central == null) {
                Router router = (Router) tp.getNodes().get(0);
                central = router;
            }
        }
        HashMap<Router, Integer> component = central.finalComponent;

        for(Router router : component.keySet()) {
            router.countCandidateLink = true;
            for(Router child : router.children) {
                router.childrenCopy.add(child);
            }
        }
        central.findPotentialConverter();

        return true;
    }

    public void placeConverter(Router converter) {
        for(Router router : central.finalComponent.keySet()) {
            router.placingConverter = true;
            router.countCandidateLink = false;
        }
        central.placeConverter(converter);
    }
}
