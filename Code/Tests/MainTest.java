import UI.Router;
import UI.RouterIPv4;
import UI.RouterIPv6;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MainTest {

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

    @Test
    void onCommandAddRouter() {
        Main main = new Main();
        main.tp.addNode(new Router());
        assertEquals(1, main.tp.getNodes().size());

    }

}