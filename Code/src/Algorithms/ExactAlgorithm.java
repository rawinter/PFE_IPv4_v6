package Algorithms;

import UI.Router;
import UI.RouterIPv4;
import UI.RouterIPv6;
import io.jbotsim.core.Link;
import io.jbotsim.core.Node;
import io.jbotsim.core.Topology;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class ExactAlgorithm implements AlgorithmModel {
    private Topology tp;
    public static ArrayList<ArrayList<Integer>> combinations= new ArrayList<ArrayList<Integer>> ();
    public static ArrayList<Integer> candidatsNodes= new ArrayList<Integer> ();
    public static int converterToPlace;

    public ExactAlgorithm(Topology tp) { this.tp = tp;
        ArrayList<ArrayList<Integer>> combinations = new ArrayList<ArrayList<Integer>> ();
        ArrayList<Integer> candidatsNodes = new ArrayList<Integer> ();}

    public boolean canCommunicate(Topology tp) {
        List<Node> nodeCanCommunicate = new ArrayList<>();
        Node actual = tp.getNodes().get(0);
        nodeCanCommunicate.add(actual);
        List<Node> nodes = tp.getNodes();
        nodes.remove(actual);
        List<Node> canCommunicate = canCommunicateRecursive(tp, actual, nodeCanCommunicate,nodes);
        return canCommunicate.size() == tp.getNodes().size();
    }

    public List<Node> canCommunicateRecursive(Topology tp, Node a, List<Node> nodeCanCommunicate,List<Node> nodes) {
        Router actual = (Router) a;
        for (Node n : a.getNeighbors()) {
            if (nodes.contains(n)) {
                Router neighbor = (Router) n;
                if (neighbor.getClass().equals(actual) || neighbor.hasConverter() || actual.hasConverter()) {
                    nodeCanCommunicate.add(neighbor);
                    nodes.remove(n);
                    return canCommunicateRecursive(tp, neighbor, nodeCanCommunicate,nodes);
                }
            }
        }
        return nodeCanCommunicate;
    }

    //:COMMENT : Calculate all the combinaisons of nodes
    public static void permute(ArrayList<Integer> arr, int k){
        ArrayList<Integer>  tmp = (ArrayList<Integer>) arr.clone();
        for(int i = k; i < arr.size(); i++){
            java.util.Collections.swap(tmp, i, k);
            permute(tmp, k+1);
            java.util.Collections.swap(tmp, k, i);
        }
        if (k == arr.size() -1){
            combinations.add(tmp);
        }
    }

    public List <Node> NodeCandidates(){
        List<Node> nodes = new ArrayList<>();
        for(Link l : tp.getLinks()){
            Router source = (Router)l.source;
            Router destination= (Router)l.destination;
            if(source instanceof RouterIPv4 && destination instanceof RouterIPv6
                    ||source instanceof RouterIPv6 && destination instanceof RouterIPv4 ){
                if(!nodes.contains(source))
                    nodes.add(l.source);
                if (!nodes.contains(destination))
                    nodes.add(l.destination);
            }
        }
        return nodes;
    }

    public int algorithm() {
        List <Node> nodes = NodeCandidates();
        for (int i=0;i<nodes.size();i++){
            candidatsNodes.add(nodes.get(i).getID());
        }

        //System.out.println(candidatsNodes.size());

        permute(this.candidatsNodes,0);

       /* for(ArrayList<Integer> permutation: this.combinations){
           System.out.println(permutation.toString());
        }
        System.out.println(combinations.size());*/
        for(int converter=1;converter<nodes.size();converter++){
            int converterTo=converter;
            //System.out.println(converterTo+ " nombre de converter a placer");
            for(List <Integer> permutate : combinations){
                for(int object=0;object<converterTo;object++){
                    //System.out.println(converterTo+ " nombre de converter a placer");
                    Node n = tp.getNodes().get(permutate.get(object));
                    Router r = (Router)n;
                    r.addConverter();
                }
                if(canCommunicate(tp)) {
                    //System.out.println(converterTo + "nombre de convertisseur a placer \n ");
                    System.out.println("J ai reussi le placement avec la combinaison suivante\n");
                    System.out.println(permutate.toString());
                    for(Node n : nodes){
                        Router r=(Router)n;
                        if (r.hasConverter()){
                            System.out.println("le routeur"+ r.getID() + " a un convertisseur");
                        }
                    }
                    return 0;
                }
                else{
                    for(Node n : nodes){
                        Router r = (Router)n;
                        r.setConverter();
                    }
                }
            }
        }
        return 0;
    }
}
