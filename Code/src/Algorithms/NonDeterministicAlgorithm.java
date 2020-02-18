package Algorithms;

import UI.ConnectedComponent;
import UI.Router;
import UI.RouterIPv4;
import UI.RouterIPv6;
import io.jbotsim.core.Link;
import io.jbotsim.core.Topology;

import java.util.ArrayList;

public class NonDeterministicAlgorithm implements AlgorithmModel {

    private Topology tp;
    private ArrayList<ConnectedComponent> components = new ArrayList<ConnectedComponent>();
    private ArrayList<Link> candidatesLinks= new ArrayList<Link>();



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
    public void candidatLink(){
        for (Link l : tp.getLinks() ){
            if (l.destination instanceof RouterIPv4 && l.source instanceof RouterIPv6){
                candidatesLinks.add(l);
            }
        }

    }
    public void algorithm(){
        //: TODO : BRIAN DIETRICH
    }



}
