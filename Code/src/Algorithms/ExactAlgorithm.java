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
    public static ArrayList<ArrayList<Integer>> combinations= new ArrayList<ArrayList<Integer>> ();
    public static ArrayList<Integer> candidatsNodes = new ArrayList<Integer>();
    public static int converterToPlace;

    public ExactAlgorithm(Topology tp) { super(tp);
        ArrayList<ArrayList<Integer>> combinations = new ArrayList<ArrayList<Integer>> ();
        ArrayList<Integer> candidatsNodes = new ArrayList<Integer> ();}


    static void CombinationRepetitionUtil(ArrayList<Integer> chosen, ArrayList<Integer> arr,
                                          int index, int r, int start, int end) {
        // Since index has become r, current combination is
        // ready to be printed, print
        if (index == r) {
            ArrayList<Integer> id = new ArrayList<>();
            for (int i = 0; i < r; i++) {
                id.add(arr.get(chosen.get(i)));
            }
            boolean redondance=false;
            for(int i=0;i<id.size();i++){
                if(id.size()>1 && i < id.size()-1) {
                    if (id.get(i)==id.get(i+1)){
                        redondance=true;
                    }
                }
            }
            if (!redondance){ combinations.add(id);}
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
        ArrayList<Integer> chosen = new ArrayList<>();
        for(int i=0;i<list.size();i++){
            chosen.add(0);
        }
        // Call the recursice function
        CombinationRepetitionUtil(chosen, list, 0, r, 0, n - 1);
    }

    public void placeConverterOnRouterExact(Topology tp,Router r){
        r.addConverter();
    }

    public int algorithm() {
        List <Node> nodes = NodeCandidates();
        for (int i=0;i<nodes.size();i++){
            candidatsNodes.add(nodes.get(i).getID());
        }
        Topology tp= getTopology();
        ArrayList<ConnectedComponent> connectedComponents=getConnectedComponents(getTopology());
        setConnectedComponents(connectedComponents);
        defineConverterToPlace();
        for(int k=1;k<getNbConverterToplace();k++) {
            CombinationRepetition(candidatsNodes, candidatsNodes.size(), k);
            for (List<Integer> permutate : combinations) {
                for (int object = 0; object < permutate.size(); object++) {
                    Node n = this.getTopology().getNodes().get(permutate.get(object));
                    Router r = (Router) n;
                    placeConverterOnRouterExact(getTopology(), r);
                }
                resetConnectedComponents(tp);
                setConnectedComponents(getConnectedComponents(tp));
                if (this.getComponent().size() == 1) {
                    return 0;
                } else {
                    for (Node n : nodes) {
                        Router r = (Router) n;
                        r.setConverter();
                        this.setConnectedComponents(connectedComponents);
                    }
                }
            }
            combinations = new ArrayList<>();
        }
        return 0;
    }
}


