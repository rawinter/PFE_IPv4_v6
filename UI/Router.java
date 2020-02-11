import io.jbotsim.core.Color;
import io.jbotsim.core.Message;
import io.jbotsim.core.Node;

public class Router extends Node {
    public int IPType;
    public boolean Convertisseur = false;

    @Override
    public void onStart() {
        super.onStart();
        double rand = Math.random();
        if(rand > 0.5) {
            IPType = 4;
            setColor(Color.blue);
        }
        else {
            IPType = 6;
            setColor(Color.red);
        }
    }

    @Override
    public void onSelection() {
        super.onSelection();

    }

    @Override
    public void onClock() {
        super.onClock();
    }

    @Override
    public void onMessage(Message message) {
        super.onMessage(message);
        Router sender;
        if(message.getSender() instanceof Router){
            sender = (Router)message.getSender();
            if(sender.IPType != IPType && (!Convertisseur || !sender.Convertisseur)){
            //cannot communicate
            }
            else {
            //can communicate
            }
        }
    }

}
