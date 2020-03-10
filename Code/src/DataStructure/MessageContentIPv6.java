package DataStructure;

import UI.Router;

import java.util.ArrayList;

public class MessageContentIPv6 {
    String commandIPv6;
    ArrayList<Router> component;

    public MessageContentIPv6(String command) {
        this.commandIPv6 = command;
    }

    public String getCommandIPv6() {
        return commandIPv6;
    }

    public void setComponent(ArrayList<Router> component) {
        this.component = component;
    }

    public ArrayList<Router> getComponent() {
        return component;
    }
}
