package UI;

import DataStructure.MessageContent;
import io.jbotsim.core.Message;
import io.jbotsim.core.Node;

import java.util.HashMap;

public class RouterIPv4 extends Router {

    @Override
    public void onStart() {
        super.onStart();
        setIconSize(20);
        setIcon("Code/Ressources/images/IPv4.png");
    }

    @Override
    public void onMessage(Message message) {
        super.onMessage(message);
        String result = messageHandler(message, this);
        Router sender = (Router) message.getSender();
        MessageContent content = (MessageContent) message.getContent();
        if(result.equals("Aggregation")) {
            if(spanningTreeCreation == true) {
                HashMap<Router, Integer> messageComponent = content.getComponent();
                for (Router router : messageComponent.keySet()) {
                    if (!finalComponent.containsKey(router)) {
                        finalComponent.put(router, messageComponent.get(router));
                    }
                }
            }
            else if(countCandidateLink == true) {
                if(parent == this) {
                    for(Router router : ((MessageContent) message.getContent()).getComponent().keySet()) {
                        finalComponent.replace(router, ((MessageContent) message.getContent()).getComponent().get(router));
                    }
                }
                else {
                    for(Router router : ((MessageContent) message.getContent()).getComponent().keySet()) {
                        finalComponent.replace(router, ((MessageContent) message.getContent()).getComponent().get(router));
                    }
                }
            }
            else if(placingConverter == true) {
                if(content.getConverterRouter() == this) {
                    addConverter();
                }
                needToBeConverter = content.getConverterRouter();
            }
            childrenCopy.remove(message.getSender());
            aggregation();
        }
        else if (result.equals("Sender is new parent")) {
            parent = (Router) message.getSender();
            componentNumber = content.getComponentNumber();
            for(Node node : this.getNeighbors()) {
                Router router = (Router) node;
                if(!router.equals(parent)) {
                    children.add(router);
                    childrenCopy.add(router);
                    if(sender instanceof RouterIPv6)
                        send(router, new Message(content, "IPv6"));
                    else
                        send(router, new Message(content, "IPv4"));
                }
            }
            if(children.isEmpty()) {
                aggregation();
            }
        }
        else if (result.equals("Error language")) {
            if (children.contains(message.getSender())) {
                children.remove(message.getSender());
                childrenCopy.remove(message.getSender());
                if (childrenCopy.isEmpty())
                    aggregation();
            }
            else if(countCandidateLink && childrenCopy.contains(message.getSender())) {
                childrenCopy.remove(message.getSender());
                MessageContent senderMessageContent = (MessageContent) message.getContent();
                int senderComponentNumber = senderMessageContent.getComponentNumber();
                if(!componentNeighbor.contains(senderComponentNumber) && senderComponentNumber != this.componentNumber && !sender.hasConverter() && !this.hasConverter()) {
                    candidateLinkNumber++;
                    componentNeighbor.add(senderComponentNumber);
                }
                if(parent == this) {
                    finalComponent.replace(this,candidateLinkNumber);
                }
                if (childrenCopy.isEmpty())
                    aggregation();
            }
            else {
                MessageContent newMessageContent = new MessageContent("TreeCreation");
                newMessageContent.setComponentNumber(componentNumber);
                send(message.getSender(), new Message(newMessageContent, "IPv4"));
            }
        }
        else if (result.equals("Already got a parent")){
            if (!children.isEmpty() && children.contains(message.getSender())) {
                children.remove(message.getSender());
                childrenCopy.remove(message.getSender());
                if(children.isEmpty())
                    aggregation();
            }
        }
        else if(result.equals("Finding potential Converter")) {
            if(message.getSender().equals(parent)) {
                checkingCandidateLink();
                findPotentialConverter();
            }
            else {
                childrenCopy.remove(message.getSender());
                aggregation();
            }
        }
        else if(result.equals("Place Converter")) {
            if(children.isEmpty()) {
                if(content.getConverterRouter() == this) {
                    addConverter();
                }
                needToBeConverter = content.getConverterRouter();
                aggregation();
            }
            else {
                this.placeConverter(content.getConverterRouter());
            }
        }
        else if(result.equals(null)) {
            //:COMMENT:Should not be here, only used to debug
        }
    }

    @Override
    public void addConverter() {
        super.addConverter();
        setIconSize(20);
        setIcon("Code/Ressources/images/Converter.png");
    }

    @Override
    public String toString() {
        return super.toString();
    }

    @Override
    public void setConverter() {
        super.setConverter();
        setIcon("Code/Ressources/images/IPv4.png");
    }
}
