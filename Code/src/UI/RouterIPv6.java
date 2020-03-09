package UI;

import io.jbotsim.core.Message;


public class RouterIPv6 extends Router {

    @Override
    public void onStart() {
        super.onStart();
        setIcon("Code/Ressources/images/Temp-IPv6.png");
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

    @Override
    public void addConverter() {
        super.addConverter();
        setIcon("Code/Ressources/images/Temp-Converter.png");
    }

    @Override
    public String toString() {
        return super.toString() + " IPv6";
    }
}
