package UI;

import DataStructure.MessageContentIPv6;
import io.jbotsim.core.Message;
import io.jbotsim.core.Node;

import java.util.ArrayList;

public class RouterIPv6 extends Router {

    @Override
    public void onStart() {
        super.onStart();
        setIcon("Code/Ressources/images/Temp-IPv6.png");
    }

    @Override
    public void onMessage(Message message) {
        super.onMessage(message);
        String result = spanningTreeCreation(message, this);
        if(result.equals("AggregationIPv6")) {
            MessageContentIPv6 content = (MessageContentIPv6) message.getContent();
            ArrayList<Router> messageComponent = content.getComponent();
            for (Router router : messageComponent) {
                if (!finalComponent.contains(router)) {
                    finalComponent.add(router);
                }
            }
            childrenCopy.remove(message.getSender());
            if (parent != this) {
                if (childrenCopy.isEmpty()) {
                    finalComponent.add(this);
                    content.setComponent(finalComponent);
                    spanningTreeCreation = false;
                    send(parent, new Message(content, "IPv6"));
                }
            }
            else {
                if (childrenCopy.isEmpty()) {
                    finalComponent.add(this);
                    spanningTreeCreation = false;
                    System.out.println("-------------------------------");
                    System.out.println("Component of the node " + this.getID() + " :");
                    for (Router router : finalComponent) {
                        System.out.print(" " + router.getID() + ",");
                    }
                    System.out.println(".");
                    System.out.println("-------------------------------");
                    algorithm.newSpanningTree();
                }
            }
        }
        else if (result.equals("Sender is new parent")) {
            parent = (RouterIPv6) message.getSender();
            MessageContentIPv6 messageContent = (MessageContentIPv6) message.getContent();
            for(Node node : this.getNeighbors()) {
                Router router = (Router) node;
                if(!router.equals(parent)) {
                    children.add(router);
                    childrenCopy.add(router);
                    send(router, new Message(messageContent, "IPv6"));
                }
            }
        }
        else if (result.equals("Error language")) {
            if (!children.isEmpty() && children.contains(message.getSender())) {
                children.remove(message.getSender());
                childrenCopy.remove(message.getSender());
                if(children.isEmpty())
                    aggregation = true;
            }
            else
                send(message.getSender(), new Message(new MessageContentIPv6("TreeCreationIPv6"), "IPv6"));
        }
        else if (result.equals("Already got a parent")){
            if (!children.isEmpty() && children.contains(message.getSender())) {
                children.remove(message.getSender());
                childrenCopy.remove(message.getSender());
                if(children.isEmpty())
                    aggregation = true;
            }
        }
        else if (result.equals(null)) {
            //:COMMENT:Should not be here, only used to debug
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

    @Override
    public void setConverter() {
        super.setConverter();
        setIcon("Code/Ressources/images/Temp-IPv6.png");
    }
}
