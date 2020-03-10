package DataStructure;

import UI.Router;

import java.util.ArrayList;

public class MessageContentIPv4 {
    String commandIPv4;
    ArrayList<Router> component;

    public MessageContentIPv4(String command) {
        this.commandIPv4 = command;
    }

    public String getCommandIPv4() {
        return commandIPv4;
    }

    public void setComponent(ArrayList<Router> component) {
        this.component = component;
    }

    public ArrayList<Router> getComponent() {
        return component;
    }
}
