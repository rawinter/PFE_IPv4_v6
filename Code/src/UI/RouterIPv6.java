package UI;

import io.jbotsim.core.Message;
import io.jbotsim.ui.icons.Icons;

public class RouterIPv6 extends Router {
    @Override
    public void onStart() {
        super.onStart();
        setIcon(Icons.WOLF);
    }

    @Override
    public void onMessage(Message message) {
        super.onMessage(message);
        if(message.getSender() instanceof  RouterIPv6){
            //:COMMENT:CanCommunicate
        }
        else{
            if(message.getSender() instanceof RouterIPv4){
                if(((RouterIPv4) message.getSender()).hasConverter()  || hasConverter()){
                    //:COMMENT:CanCommunicate
                }
                else{
                    //:COMMENT:CantCommunicate
                }
            }
        }
    }


}
