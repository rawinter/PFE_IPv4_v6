package Algorithms;

import UI.ConnectedComponent;
import UI.Router;
import UI.RouterIPv4;
import UI.RouterIPv6;
import io.jbotsim.core.Link;
import io.jbotsim.core.Node;
import io.jbotsim.core.Topology;

import java.util.*;

public class ExactAlgorithm extends AlgorithmNonDistributed {
    public static ArrayList<Integer> candidatsNodes = new ArrayList<Integer>();
    public static int converterToPlace;
   static ArrayList<Integer> solution=new ArrayList<>();

    public ExactAlgorithm(Topology tp) { super(tp);}

       static void CombinationRepetitionUtil(ArrayList<Integer> chosen, ArrayList<Integer> arr,
                                          int index, int r, int start, int end,Topology tp) {
        // Since index has become r, current combination is
        // ready to be test if it is a good solution
            // we add it to the solution variable
        if (solution.isEmpty()) {
            if (index == r) {
                ArrayList<Integer> id = new ArrayList<>();
                for (int i = 0; i < r; i++) {
                    id.add(arr.get(chosen.get(i)));
                }
                boolean redondance = false;
                //We check if the list contains two times the same id_nodes
                for (int i = 0; i < id.size(); i++) {
                    if (id.size() > 1 && i < id.size() - 1) {
                        if (id.get(i) == id.get(i + 1)) {
                            redondance = true;
                        }
                    }
                }
                //if the list is correctly set and don't have redondance of an ID into it
                // we can check if it's a solution
                solution=id;
                if (!redondance) {
                    setConverter(solution,tp);
                    setConnectedComponents(getConnectedComponents(tp));
                    //If == 1 means that we have the good solution
                    if (getConnectedComponents(tp).size() == 1) {

                    } else {
                        reinitialiseConverter(candidatsNodes);
                        //solution.clear();
                    }

                }
                return;
            }

            // permutation of one element one by one, and recursive call
            for (int i = start; i <= end; i++) {
                chosen.set(index, i);
                CombinationRepetitionUtil(chosen, arr, index + 1,
                        r, i, end,tp);
            }
            return;
        }
    }
    // Determine and add the Node that possess a candidat Link
    public List<Node> NodeCandidates(Topology topology){
        List<Node> nodes = new ArrayList<>();
        for(Link l : topology.getLinks()){
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

    // calcul all the combination of the Arraylist int
  static  void CombinationRepetition(ArrayList <Integer> list, int n, int r,Topology tp) {
        ArrayList<Integer> chosen = new ArrayList<>();
        for(int i=0;i<list.size();i++){
            chosen.add(0);
        }
        // Call the recursive function
        CombinationRepetitionUtil(chosen, list, 0, r, 0, n - 1,tp);
    }

    public void defineConverterToPlace(Topology topology){ converterToPlace=getConnectedComponents(topology).size(); }

    public int getNbConverterToplace(){ return converterToPlace; }

    //Set the variable converter to false for all nodes
    public static void reinitialiseConverter(List<Integer> candidatsNodes){
        for(int i=0;i<candidatsNodes.size();i++){
            for(Node n : getTopology().getNodes()){
                if (n.getID()==candidatsNodes.get(i)){
                    Router r=(Router)n;
                    r.setConverter();
                }
            }
        }
    }

    //Set a converter on each router of the list
    public static void setConverter(List<Integer> list, Topology tp){
       /* for(int i=0;i<list.size();i++){
            Node n = tp.getNodes().get(list.get(i));
            Router r = (Router) n;
            placeConverterOnRouterExact(tp, r);
        }*/
        while(!list.isEmpty()){
            for (Node n : tp.getNodes()){
                if (n.getID()==list.get(0)){
                    Router r = (Router) n;
                    placeConverterOnRouterExact(tp, r);

                }
            }
            list.remove(list.get(0));
        }
    }

    //COMMENT : Place a converter on the router r
    // if instanceof Glouton we do other treatment.
    public static void placeConverterOnRouterExact(Topology tp,Router r){
        r.addConverter();
        setConnectedComponents(getConnectedComponents(tp));

    }
    public void algorithm() {
        Topology tp=getTopology();
        List <Node> nodes = NodeCandidates(tp);
        for (int i=0;i<nodes.size();i++){
            candidatsNodes.add(nodes.get(i).getID());
        }
        ArrayList<ConnectedComponent> connectedComponents=getConnectedComponents(tp);
        setConnectedComponents(connectedComponents);
        defineConverterToPlace(tp);

        int k=1;
        while(k<getNbConverterToplace() && solution.isEmpty()){
            CombinationRepetition(candidatsNodes, candidatsNodes.size(), k,tp);
            k++;
        }

        return ;
    }
}


