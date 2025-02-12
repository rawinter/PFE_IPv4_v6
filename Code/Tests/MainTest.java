import Algorithms.ExactAlgorithm;
import UI.Router;
import UI.RouterIPv4;
import UI.RouterIPv6;
import io.jbotsim.core.Link;
import io.jbotsim.core.Node;
import io.jbotsim.core.Topology;
import org.junit.jupiter.api.Test;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MainTest {

//    Testing the modification of the Node model
    @Test
    void onCommandAddIPv4() {
        Main main = new Main();
        main.onCommand(Main.ADD_IPV4);
        assertEquals(main.tp.getDefaultNodeModel(), RouterIPv4.class);

    }

    @Test
    void onCommandAddIPv6() {
        Main main = new Main();
        main.onCommand(Main.ADD_IPV6);
        assertEquals(main.tp.getDefaultNodeModel(), RouterIPv6.class);

    }

    //:COMMENT:Testing the adding of a router on the topology
    @Test
    void onCommandAddRouter() {
        Main main = new Main();
        main.tp.addNode(new Router());
        assertEquals(1, main.tp.getNodes().size());

    }

    //:COMMENT:Non-regression test created to assure that the Network generation do not add same links multiple time
    @Test
    void networkGenerationLinkDuplication(){
        Main main = new Main();
        main.onCommand(Main.NETWORK_GENERATION);
        List<Link> Links = main.tp.getLinks();
        while(!Links.isEmpty()){
            Link toTest = Links.remove(0);
            assertFalse(Links.contains(toTest));
        }
    }

    //:COMMENT:Testing of the network generation
//    @Test
//    void networkGeneration(){
//        Main main = new Main();
//        main.onCommand(Main.NETWORK_GENERATION);
//        assertFalse(main.tp.getNodes().isEmpty());
//        assertFalse(main.tp.getLinks().isEmpty());
//    }
//
//
    //:COMMENT:Testing for the connexite of a generated network
//    @Test
//    void Connexite(){
//        Main main = new Main();
//        main.onCommand(Main.NETWORK_GENERATION);
//        boolean valid = true;
//        List<Node> nodesToTest = new ArrayList<>();
//        List<Node> nodesMarked = new ArrayList<>();
//        nodesToTest.add(main.tp.getNodes().get(0));
//        while (!nodesToTest.isEmpty()) {
//            Node currentNode = nodesToTest.remove(0);
//            nodesMarked.add(currentNode);
//            for(Node n : currentNode.getNeighbors()){
//                if(!nodesToTest.contains(n) && !nodesMarked.contains(n)){
//                    nodesToTest.add(n);
//                }
//            }
//        }
//        for (Node n : main.tp.getNodes()) {
//            if (!nodesMarked.contains(n)) {
//                valid = false;
//                break;
//            }
//        }
//        assertTrue(valid);
//    }

    //:COMMENT:Testing of the command to place converter
    @Test
    void CommandsAddingConverter(){
        Main main = new Main();
        main.onCommand(Main.CONVERTER);
        assertTrue(main.converter);
        main.onCommand(Main.STOP_CONVERTER);
        assertFalse(main.converter);
    }



    //:COMMENT:Testing that the router have the correct icons
    @Test
    void RouterIcons(){
        Main main = new Main();
        main.tp.addNode(new RouterIPv4());
        assertEquals(main.tp.getNodes().get(0).getIcon(), "Code/Ressources/images/Temp-IPv4.png");
        main.tp.clear();
        main.tp.addNode(new RouterIPv6());
        assertEquals(main.tp.getNodes().get(0).getIcon(),"Code/Ressources/images/Temp-IPv6.png");
        main.onCommand(Main.CONVERTER);
        main.onSelection(main.tp.getNodes().get(0));
        assertEquals(main.tp.getNodes().get(0).getIcon(),"Code/Ressources/images/Temp-Converter.png");
    }

}