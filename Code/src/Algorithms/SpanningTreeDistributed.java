package Algorithms;

import DataStructure.MessageContentIPv4;
import DataStructure.MessageContentIPv6;
import UI.Router;
import UI.RouterIPv4;
import UI.RouterIPv6;
import io.jbotsim.core.Message;
import io.jbotsim.core.Node;
import io.jbotsim.core.Topology;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SpanningTreeDistributed implements AlgorithmModel {

    private List<Node> allNodes;
    private Topology tp;

    public SpanningTreeDistributed(Topology tp) {
        this.tp = tp;
    }

    public boolean newSpanningTree() {
        allNodes = tp.getNodes();
        Random random = new Random();
        List<Router> routersList = new ArrayList<>();
        boolean needToPerform = false;
        for (Node node : allNodes) {
            Router router = (Router) node;
            if(router.spanningTreeCreation) {
                needToPerform = true;
                routersList.add(router);
            }
        }
        if(needToPerform) {
            int randomNumber = random.nextInt(routersList.size());
            Router parent = routersList.get(randomNumber);
            routersList.remove(parent);
            parent.spanningTreeInit(this);
            return true;
        }
        else
            return false;
    }
}
