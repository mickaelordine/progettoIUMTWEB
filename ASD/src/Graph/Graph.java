package Graph;
import java.util.*;
import java.util.stream.Collectors;

/*
- Creazione di un grafo vuoto – O(1)
- Aggiunta di un nodo – O(1)
- Aggiunta di un arco – O(1)
- Verifica se il grafo è diretto – O(1)
- Verifica se il grafo contiene un dato nodo – O(1)
- Verifica se il grafo contiene un dato arco – O(1)  (*)   fine a O(n)
- Cancellazione di un nodo – O(n)
- Cancellazione di un arco – O(1)  (*) fino a O(n)
- Determinazione del numero di nodi – O(1)
- Determinazione del numero di archi – O(n)
- Recupero dei nodi del grafo – O(n)
- Recupero degli archi del grafo – O(n)
- Recupero nodi adiacenti di un dato nodo – O(1)  (*) fino a O(n)
- Recupero etichetta associata a una coppia di nodi – O(1) (*) fino a O(n)
 */


public class Graph<T,G> {

    private Map<T,List<Arch<T,G>>> map;
    private boolean bidirectionalFlag;

    public Graph(boolean bidirectionalFlag) {
        map = new HashMap<>(); //O(1)
        this.bidirectionalFlag = bidirectionalFlag;
    }

    public boolean isBidirectional() {
        return bidirectionalFlag;
    } //O(1)


    //check if the vertex is already present in the hashmap, otherwise it will add it
    public void add_vertex(T newValue) throws Exception{
        if (newValue == null)
            throw new Exception("[ERR] add_vertex(): Null vertex cannot be added in the Graph");

        if (hasVertex(newValue))
            throw new Exception("[ERR] add_vertex(): Vertex already exists in this Graph");

        map.put(newValue, new LinkedList<>()); //O(1)
    }

    //check if is present the key in the hashmap
    public boolean hasVertex(T s) throws Exception {
        if (s == null)
            throw new Exception("vertex cannot be NULL");
        return map.containsKey(s); //O(1)
    }

    // This function adds the edge between source to destination node.
    public void add_arch(T src, T dest, G i) throws Exception {
        if (src == null || dest == null || i == null)
            throw new Exception("you can't insert arches with null sources/destinations");

        if (!map.containsKey(src))
            add_vertex(src);

        if (!map.containsKey(dest))
            add_vertex(dest);

        map.get(src).add(new Arch<>(src, dest, i));     //O(1)

        if (isBidirectional()) {
            map.get(dest).add(new Arch<>(dest, src, i));    //O(1)
        }
    }

    public boolean hasArch(T src, T dest) throws Exception {
        if (src == null || dest == null)
            throw new Exception("[ERR] hasArch(): source and destination cannot be NULL");

        if (map.get(src) != null)
            return map.get(src).contains(new Arch<>(src, dest)); //O(1)*(n)

        return false;
    }

    //used to remove Node
    public void remove_vertex(T val) throws Exception {
        if (val == null)
            throw new Exception("Error: a null vertex cannot be removed");

        if (map.containsKey(val)) {
            for (T k : map.keySet()) { //O(n)
                if (hasArch(k, val)) {
                    remove_arch(val, k);
                }
            }
            map.remove(val);
        } else {
            throw new Exception("Error: vertex not found. Can't remove this vertex: " + val);
        }
    }

    //used to remove Edge from s to d
    public void remove_arch(T src, T dest) throws Exception {
        if (src == null || dest == null)
            throw new Exception("Error: a null arch cannot be removed");

        if (!map.containsKey(src) || !map.containsKey(dest))
            throw new Exception("Error: cannot remove an arch with a null boundary");

        if (hasArch(src, dest)) {
            map.get(src).remove(new Arch<>(src,dest)); //O(1) //dest
        }
        if (isBidirectional())
            map.get(dest).remove(new Arch<>(dest, src)); //O(1) //src
    }

    public int get_n_vertex() {
        return map.keySet().size();
    } //O(1)

    public int get_n_arch() {
        int count = 0;
        for (T v : map.keySet()) { //O(n)
            count += map.get(v).size();
        }
        if (this.bidirectionalFlag) {
            count = count / 2;
        }
        return count;
    }

    public ArrayList<T> get_vertexes() {
        ArrayList<T> result = new ArrayList<>();

        for (T k : map.keySet()) //O(n)
            result.add(k);

        return result;
    }

    public  List<Arch<T,G>> get_arch(){ //O(n)

        List<Arch<T, G>> list = new ArrayList<>();

        for (T k : map.keySet())
            for(int i = 0; i<map.get(k).size();i++)
                list.add(map.get(k).get(i));

        return list;
    }

    //getAdjacentNode
    public List<T> getAdjacentVertex(T vertex) throws Exception {
        if (vertex == null)
            throw new Exception("Error: vertex null. Adjacent list cannot be printed");

        if (!map.containsKey(vertex))
            throw new Exception("Error: vertex does not exist. Adjacent list cannot be printed");

        List<T> adj_list = new ArrayList<>();

        for (int i = 0; i < map.get(vertex).size(); i++)//O(n)
            adj_list.add(map.get(vertex).get(i).getEnd());

        return adj_list;
    }

    //getEdgeLabel
    public G getEdgeLabel(T v1, T v2) throws Exception {
        if (v1 == null || v2 == null)
            throw new Exception("Error: get_w_btw: Parameters cannot be null");

        if (hasArch(v1, v2)) {
            for (int i = 0; i < map.get(v1).size(); i++) //O(n)
                if (map.get(v1).get(i).getEnd() == v2)
                    return map.get(v1).get(i).getWeight();
        } else
            throw new Exception("Error: get_w_btw: arch (" + v1 + ", " + v2 + ") doesn't exist");

        return null;
    }

    public Map<T, List<Arch<T, G>>> getMap() {
        return map;
    }


    public T getKeysJava8(
            HashMap<T, G> map, G value) {

        return (T) map
                .entrySet()
                .stream()
                .filter(entry -> Objects.equals(entry.getValue(), value))
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());

    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (T v : map.keySet()) {
            builder.append("Vertex: " + v + "\n\tAdj_list:");
            for (int i = 0; i < map.get(v).size(); i++) {
                builder.append("\n\t\t\t" + map.get(v).get(i));
            }
            builder.append("\n");
        }
        return (builder.toString());
    }



}
