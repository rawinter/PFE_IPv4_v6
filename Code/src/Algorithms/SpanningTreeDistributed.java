package Algorithms;

import DataStructure.SpanningTreeStructure;
import UI.ConnectedComponent;
import UI.Router;
import UI.RouterIPv4;
import UI.RouterIPv6;
import io.jbotsim.core.Color;
import io.jbotsim.core.Message;
import io.jbotsim.core.Node;

public class SpanningTreeDistributed implements AlgorithmModel {

    public Router spanningTreeDistributed(Message message, Router origin, Router parent) {
        if(message.getFlag().equals("TreeCreation"))
        {
            if (parent == null) {
                SpanningTreeStructure content = (SpanningTreeStructure) message.getContent();
                parent = (Router) message.getSender();
                content.routersList.remove(origin);
                if (parent.getClass().equals(origin.getClass())) {
                    origin.getCommonLinkWith(message.getSender()).setWidth(4);
                    if (origin instanceof RouterIPv4) {
                        origin.getCommonLinkWith(message.getSender()).setColor(Color.RED);
                    }
                    if (origin instanceof RouterIPv6) {
                        origin.getCommonLinkWith(message.getSender()).setColor(Color.BLUE);
                    }
                    for (ConnectedComponent component : content.connectedComponentsList) {
                        if (component.contains(parent)) {
                            component.addRouter(origin);
                        }
                    }
                }
                else {
                    origin.getCommonLinkWith(message.getSender()).setColor(Color.GREEN);
                    ConnectedComponent newOne = new ConnectedComponent();
                    newOne.addRouter(origin);
                    content.connectedComponentsList.add(newOne);
                }

                origin.sendAll(new Message(new SpanningTreeStructure(content.routersList, content.connectedComponentsList), "TreeCreation"));

            }
            else {
                for (Node neighbor : origin.getOutNeighbors())
                {
                    if (origin.getCommonLinkWith(neighbor).getColor().equals(Color.RED) || origin.getCommonLinkWith(neighbor).getColor().equals(Color.BLUE) || origin.getCommonLinkWith(neighbor).getColor().equals(Color.GREEN)) {
                        //:COMMENT:Need to modify this statement to simplify it
                    }
                    else {
                        origin.send(neighbor, new Message(this,"TreeCreationLastLink"));
                        break;
                    }
                }
            }
        }
        //:COMMENT:This type of message is used to color the link which does not belong to the spanning tree
        if(message.getFlag().equals("TreeCreationLastLink")) {
            Router sender = (Router) message.getSender();
            if(sender.getClass().equals(origin.getClass())) {
                origin.getCommonLinkWith(sender).setWidth(4);
                if(origin instanceof RouterIPv4) {
                    origin.getCommonLinkWith(message.getSender()).setColor(Color.RED);
                }
                if (origin instanceof RouterIPv6) {
                    origin.getCommonLinkWith(message.getSender()).setColor(Color.BLUE);
                }
            }
            else {
                origin.getCommonLinkWith(message.getSender()).setColor(Color.GREEN);
            }
        }

        return parent;
    }
}
