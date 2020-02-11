import io.jbotsim.core.Node;
import io.jbotsim.core.Topology;

import java.util.List;

public class Algorithm {
    private List<Node> ListOfRouters;
    public void PlacementConvertisseur(Topology tp){
        ListOfRouters = tp.getNodes();
    }
}
