package UI;

import io.jbotsim.core.Message;

public class RouterIPv4 extends Router {

    @Override
    public void onStart() {
        super.onStart();
        setIcon("Code/Ressources/images/Temp-IPv4.png");
    }

    @Override
    public void onMessage(Message message) {
        super.onMessage(message);
        if(message.getSender() instanceof  RouterIPv4){
            //:COMMENT:CanCommunicate
        }
        else{
            if(message.getSender() instanceof RouterIPv6){
                if(((RouterIPv6) message.getSender()).hasConverter() || hasConverter()){
                    //:COMMENT:CanCommunicate
                }
                else{
                    //:COMMENT:CantCommunicate
                }
            }
        }
    }

    @Override
    public void addConverter() {
        super.addConverter();
        setIcon("Code/Ressources/images/Temp-Converter.png");
    }

    @Override
    public String toString() {
        return super.toString() + " IPv4";
    }
}
