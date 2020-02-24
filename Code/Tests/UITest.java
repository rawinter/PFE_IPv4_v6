import UI.Router;
import io.jbotsim.core.Link;
import io.jbotsim.core.Topology;
import io.jbotsim.ui.JViewer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UITest {

    @Test
    void AddLink() {
        Topology tp = new Topology();
        tp.setCommunicationRange(-1);
        Router r1 = new Router();
        Router r2 = new Router();
        tp.addNode(r1);
        tp.addNode(r2);
        Link l = new Link(r1,r2);
        tp.addLink(l);
        assertEquals(1, tp.getLinks().size());

    }
    @Test
    void DelLink() {
        Topology tp = new Topology();
        tp.setCommunicationRange(-1);
        Router r1 = new Router();
        Router r2 = new Router();
        tp.addNode(r1);
        tp.addNode(r2);
        Link l = new Link(r1,r2);
        tp.addLink(l);
        tp.removeLink(l);
        assertEquals(0, tp.getLinks().size());

    }
}
