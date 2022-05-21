package Dijkstra;

import Graph.Arch;

import java.util.Comparator;

public class FloatCompare implements Comparator<Arch<String,Float>> {
    @Override
    public int compare(Arch<String, Float> o1, Arch<String, Float> o2) {
        return Float.compare(o1.getWeight(),o2.getWeight());
    }
}
