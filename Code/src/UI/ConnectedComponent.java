package UI;

import java.util.ArrayList;

public class ConnectedComponent {
    private ArrayList<Router> routers = new ArrayList<>();


    public void addRouter(Router r){ routers.add(r); }

    public boolean contains(Router r){
        return routers.contains(r);
    }

    public String toString(){
        StringBuilder tmp=new StringBuilder();
        for (Router r: routers){
            tmp.append(r.getID()).append(" ");
        }
        tmp.append(" end ");
        return tmp.toString();

    }



}
