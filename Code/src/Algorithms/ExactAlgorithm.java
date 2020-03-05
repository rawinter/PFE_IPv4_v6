package Algorithms;

import UI.ConnectedComponent;
import UI.Router;
import UI.RouterIPv4;
import UI.RouterIPv6;
import io.jbotsim.core.Link;
import io.jbotsim.core.Node;
import io.jbotsim.core.Topology;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ExactAlgorithm implements AlgorithmModel {
    private Topology tp;
    private ArrayList<ConnectedComponent> component = new ArrayList<>();
    private ArrayList<Link> candidatesLinks= new ArrayList<>();
    public ExactAlgorithm(Topology tp) { this.tp = tp; }

    public boolean canCommunicate(Topology tp) {
        List<Node> nodeCanCommunicate = new ArrayList<>();
        Node actual = tp.getNodes().get(0);
        //System.out.println("le premier noeud est "+ actual.getID());
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
    public List<List<Integer>> permute(List<Integer> arr, int k,List<List<Integer>> p ){
        for(int i = k; i < arr.size(); i++){
            java.util.Collections.swap(arr, i, k);
            permute(arr, k+1,p);
            java.util.Collections.swap(arr, k, i);
        }
        if (k == arr.size() -1){
            p.add(arr);
           // System.out.println(arr.toString());
        }

        return p;
    }

    public List <Node> NodeCandidates(){
        List<Node> nodes = new ArrayList<>();
        for(Link l : tp.getLinks()){
            Router source = (Router)l.source;
            Router destination= (Router)l.destination;
            if(source instanceof RouterIPv4 && destination instanceof RouterIPv6 ||source instanceof RouterIPv6 && destination instanceof RouterIPv4 ){
                if(!nodes.contains(source))
                    nodes.add(l.source);
                if (!nodes.contains(destination))
                    nodes.add(l.destination);
            }
        }
        return nodes;
    }

    public void candidatLink(Topology tp) {
        for (Link l : tp.getLinks()) {
            Router source = (Router) l.source;
            Router destination = (Router) l.destination;
            boolean containInComponent = false;
            if (!(source.getClass().equals(destination.getClass()) || source.hasConverter() || destination.hasConverter())) {
                for(ConnectedComponent cc : component){
                    if(cc.contains(source) && cc.contains(destination))
                    {
                        containInComponent = true;
                    }
                }
                if(!containInComponent)
                    candidatesLinks.add(l);
            }
        }
        //countCandidatesLink(tp);
    }

    public ArrayList<ConnectedComponent> getConnectedComponents(Topology tp){
        ArrayList<ConnectedComponent> components = new ArrayList<>();
        List<Node> nodes = tp.getNodes();
        while(!nodes.isEmpty()){
            Router r1 = (Router) nodes.get(0);
            nodes.remove(r1);
            ConnectedComponent cc1= new ConnectedComponent();
            ArrayList<Router> connected = new ArrayList<>();
            connected.add(r1);
            ArrayList<Router> result = recursiveSameClassNeighbor(connected, r1, nodes);
            for(Router router : result)
                cc1.addRouter(router);
            components.add(cc1);
        }
        return components;

    }

    public ArrayList<Router> recursiveSameClassNeighbor(ArrayList<Router> connected, Router previous, List<Node> nodes) {
        List<Node> neighbors = previous.getNeighbors();
        for(Router router : connected) {
            if(neighbors.contains(router)) {
                neighbors.remove(router);
            }
        }
        for (Node n : neighbors){
            Router actual = (Router) n;
            if (previous.getClass().equals(actual.getClass()) || actual.hasConverter() || previous.hasConverter()){
                if(!connected.contains(actual)) {
                    connected.add(actual);
                    recursiveSameClassNeighbor(connected, actual, nodes);
                }
                nodes.remove(n);
            }
        }
        return connected;
    }


    public void algorithm() {
        List <Node> nodes = NodeCandidates();
        List<Integer> p = new ArrayList<>();
        // Je récupère les identifiants des noeud candidats pour y placer des convertisseurs plus tard
        for (int i=0;i<nodes.size();i++){
            p.add(nodes.get(i).getID());
        }
        candidatLink(tp);
        //je récupère le nombrre de composantes connexes pour déterminer le nombre maximal de convertisseur à placer
        component=getConnectedComponents(tp);
        List<List<Integer>> perm = new ArrayList<List<Integer>>();
        List <List<Integer>> permutation=permute(p,0,perm);
        //J'ai un problème ici mon tableau de permutation contient toujours la même permutation ....
        for(int i=0;i<permutation.size();i++){
            System.out.println(permutation.get(i).toString());
        }

        //j'incremente a chaque fois le nombre de convertisseur dont je peux avoir besoin
        for(int converter=1;converter<component.size();converter++){
            int converterTo=converter;
            //je parcours toutes les permutations et parcours seulement un nombre de noeud égal au nombre de convertisseur à placer
            for(int permutate=0;permutate<permutation.size();permutate++){
                List<Integer> tmp=permutation.get(permutate);
                for(int object=0;object<converterTo;object++){
                    Node n = tp.getNodes().get(tmp.get(object));
                    Router r = (Router)n;
                    r.addConverter();
                }
                //Si j'arrive à communiquer depuis n'importe quel noeud vers tout les autres alors c'est bon !
                if(canCommunicate(tp)){
                    System.out.println("J ai reussi le placement \n");
                    break;
                }
                //sinon j'enlève tout les convertisseurs et je recommence un placement
                else{
                    for(Node n : nodes){
                        Router r = (Router)n;
                        r.setConverter();
                    }
                }
            }
        }
    }
}
