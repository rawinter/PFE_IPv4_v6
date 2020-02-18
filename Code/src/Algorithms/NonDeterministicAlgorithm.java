package Algorithms;

import UI.ConnectedComponent;
import UI.Router;

import java.util.ArrayList;

public class NonDeterministicAlgorithm implements AlgorithmModel {

    private ArrayList<ConnectedComponent> components = new ArrayList<ConnectedComponent>();



    public void addComponents(ConnectedComponent c){
        components.add(c);
    }
    //:COMMENT:Fuse two components into one
    public void FuseComponents(ConnectedComponent c1,ConnectedComponent c2){
        if (components.contains(c1) && components.contains(c2)) {
            ConnectedComponent c3 = new ConnectedComponent();
            for (Router r1 : c1.getRouters()){
                c3.addRouter(r1);
             }
            for(Router r2 : c2.getRouters()){
                c3.addRouter(r2);

            }
            components.remove(c1);
            components.remove(c2);
            addComponents(c3);

        }
    }




}
