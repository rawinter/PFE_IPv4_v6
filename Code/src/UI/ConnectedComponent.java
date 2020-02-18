package UI;

import java.util.ArrayList;

public class ConnectedComponent {
    private ArrayList<Router> routers = new ArrayList<Router>();


    public void addRouter(Router r){
        routers.add(r);
    }
    public void deleteRouter(Router r){
        if (routers.contains(r)){
            routers.remove(r);
        }
    }
    public ArrayList<Router> getRouters(){
        return routers;
    }



}
