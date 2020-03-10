package Algorithms;

import UI.Router;
import UI.RouterIPv4;
import UI.RouterIPv6;
import io.jbotsim.core.Link;
import io.jbotsim.core.Node;
import io.jbotsim.core.Topology;

import java.util.*;

public class ExactAlgorithm implements AlgorithmModel {
    private Topology tp;
    public static ArrayList<ArrayList<Integer>> combinations= new ArrayList<ArrayList<Integer>> ();
    public static ArrayList<Integer> candidatsNodes = new ArrayList<Integer>();
    static List<List<Integer>> powerSet = new LinkedList<List<Integer>>();
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
           // System.out.println(combinations.toString());
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

    static void CombinationRepetitionUtil(ArrayList<Integer> chosen, ArrayList<Integer> arr,
                                          int index, int r, int start, int end) {
        // Since index has become r, current combination is
        // ready to be printed, print
        if (index == r) {
            ArrayList<Integer> id = new ArrayList<>();
            for (int i = 0; i < r; i++) {
                id.add(arr.get(chosen.get(i)));
            }
            combinations.add(id);
            return;
        }

        // One by one choose all elements (without considering
        // the fact whether element is already chosen or not)
        // and recur
        for (int i = start; i <= end; i++) {
            chosen.set(index,i);
            CombinationRepetitionUtil(chosen, arr, index + 1,
                    r, i, end);
        }
        return;
    }
    static void CombinationRepetition(ArrayList <Integer> list, int n, int r) {
        // Allocate memory
        ArrayList<Integer> chosen = new ArrayList<>();
        for(int i=0;i<list.size();i++){
            chosen.add(0);
        }

        // Call the recursice function
        CombinationRepetitionUtil(chosen, list, 0, r, 0, n - 1);
    }


    public int algorithm() {


        List <Node> nodes = NodeCandidates();
        for (int i=0;i<nodes.size();i++){
            candidatsNodes.add(nodes.get(i).getID());
        }

        for(int k=1;k<=candidatsNodes.size();k++){
            CombinationRepetition(candidatsNodes, candidatsNodes.size(), k);
        }

        //permute(this.candidatsNodes,2);
        /*for(int k=1;k<=candidatsNodes.size();k++){
            this.powerSet.addAll(combination(candidatsNodes,k));}
        System.out.println(this.powerSet.toString());*/

        /*for(ArrayList<Integer> permutation: this.combinations){
           System.out.println(permutation.toString());
        }*/
        for(int converter=1;converter<nodes.size();converter++){
            int converterTo=converter;
            //System.out.println(converterTo+ " nombre de converter a placer");
            for(List <Integer> permutate : combinations){
                for(int object=0;object<converterTo;object++){

                        //System.out.println(converterTo+ " nombre de converter a placer");
                        System.out.println(permutate);
                        Node n = tp.getNodes().get(permutate.get(object));
                        Router r = (Router) n;
                        r.addConverter();

                    }
                    if(canCommunicate(tp)) {
                        //System.out.println(converterTo + "nombre de convertisseur a placer \n ");
                        //System.out.println("J ai reussi le placement avec la combinaison suivante\n");
                       // System.out.println(permutate.toString());
                        for (Node n : nodes) {
                            Router r = (Router) n;
                            /*if (r.hasConverter()) {
                                System.out.println("le routeur" + r.getID() + " a un convertisseur");
                            }*/
                        }
                        return 0;
                    }
                    else {
                        for (Node n : nodes) {
                            Router r = (Router) n;
                            r.setConverter();
                        }

                }
            }
        }
        return 0;
    }
}


