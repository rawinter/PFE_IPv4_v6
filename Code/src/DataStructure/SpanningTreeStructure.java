package DataStructure;

import UI.ConnectedComponent;
import UI.Router;

import java.util.List;

public class SpanningTreeStructure {
    public List<Router> routersList;
    public List<ConnectedComponent> connectedComponentsList;

    public SpanningTreeStructure (List<Router> routersList, List<ConnectedComponent> connectedComponentsList) {
        this.routersList = routersList;
        this.connectedComponentsList = connectedComponentsList;
    }
}
