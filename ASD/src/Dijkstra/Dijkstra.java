package Dijkstra;

import MinHeap.*;
import Graph.*;

import java.security.Key;
import java.util.Comparator;
import java.util.*;

public class Dijkstra<T extends Comparable<T>,G> {


    public List<Arch<T, G>> Dijkstra(Graph<T, G> g, Comparator<Arch<T, G>> comparator) throws Exception{
        T start = (T) Main.start;
        T end = (T) Main.end;
        if(g==null || comparator==null) throw new Exception("exception in Dijkstra");
        List<Arch<T, G>> result = new ArrayList<>();
        List<Arch<T, G>> archesList = g.get_arch(); // qui prendo tutti gli archi che ho inserito nel graph
        MinHeap<G> minHeap = new MinHeap<G>();
        HashMap<T,G> map = new HashMap<>(); //T end, G peso




        while(!minHeap.isEmpty() || start != end){
            Arch<T, G> tempArch;
            if (!Objects.equals(start, end)){
                List<T> adiacent = g.getAdjacentVertex(start);
                for (T t : adiacent) { //inserrisco gli adiacent di start
                    minHeap.insert(g.getEdgeLabel(start, t));
                    map.put(t, g.getEdgeLabel(start, t)); //creo un hashmap con i valori solo di quelli adiacenti
                }

                G value = minHeap.extractMin(); //prendo il peso corrispondente da start a end

                T tempArch2 = g.getKeysJava8(map,value); //prendo il valore di "END" dall'hashmap dei valori adiacenti

                tempArch = new Arch<>(start,tempArch2,value);

                result.add(tempArch);

                start = tempArch2;

                map.clear();

            }


        }

        return result;
    }

}

//PROBLEMA NELL'HASARCH, DA CAPIRE IL PERCHè

