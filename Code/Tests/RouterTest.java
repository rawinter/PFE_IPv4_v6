import UI.Router;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RouterTest {

    @Test
    void addConverter() {
        Router router = new Router();
        router.addConverter();
        assertTrue(router.hasConverter());
    }

}