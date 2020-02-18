import io.jbotsim.core.Message;
import io.jbotsim.ui.icons.Icons;

public class RouterIPv4 extends Router {
    @Override
    public void onStart() {
        super.onStart();
        setIcon(Icons.SHEEP);
    }

    @Override
    public void onMessage(Message message) {
        super.onMessage(message);
        if(message.getSender() instanceof  RouterIPv4){
            //:COMMENT:CanCommunicate
        }
        else{
            if(message.getSender() instanceof RouterIPv6){
                if(((RouterIPv6) message.getSender()).hasConv() || hasConv()){
                    //:COMMENT:CanCommunicate
                }
                else{
                    //:COMMENT:CantCommunicate
                }
            }
        }
    }
}
