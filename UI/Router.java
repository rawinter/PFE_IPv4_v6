import io.jbotsim.core.Message;
import io.jbotsim.core.Node;

public class Router extends Node {
    public int IPType;
    public boolean Converter = false;

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


    public boolean hasConv(){
        return Converter;
    }
}
