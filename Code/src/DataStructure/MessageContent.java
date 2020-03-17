package DataStructure;

import UI.Router;

import java.util.HashMap;

public class MessageContent {

    int componentNumber;
    Router converter;
    boolean placeConverter = false;
    String command;
    HashMap<Router, Integer> component;

    public MessageContent(String command) {
        this.command = command;
    }

    public String getCommand() {
        return command;
    }

    public void setComponent(HashMap<Router, Integer>component) {
        this.component = component;
    }

    public HashMap<Router, Integer> getComponent() {
        return component;
    }

    public void setComponentNumber(int componentNumber) {
        this.componentNumber = componentNumber;
    }

    public int getComponentNumber() {
        return componentNumber;
    }

    public void setConverterRouter(Router converter) {
        this.converter = converter;
    }

    public Router getConverterRouter() {
        return converter;
    }

    public void setPlaceConverter(boolean placeConverter) { this.placeConverter = placeConverter;}

    public boolean getPlaceConverter() { return placeConverter;}
}
