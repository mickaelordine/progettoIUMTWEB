package Graph;

import java.util.*;

public class Arch<T,G> {
    T start;
    T end;
    G weight;

    public Arch(T start, T end){
        this.start = start;
        this.end = end;
    }

    public Arch(T start, T end, G weight) {
        this.start = start;
        this.end = end;
        this.weight = weight;
    }

    public T getStart() {
        return start;
    }

    public void setStart(T start) {
        this.start = start;
    }

    public T getEnd() {
        return end;
    }

    public void setEnd(T end) {
        this.end = end;
    }

    public G getWeight() {
        return weight;
    }

    public void setWeight(G weight) {
        this.weight = weight;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        return false;
    }

    @Override
    public String toString() {
        return "start: " + start + "\t end: " + end + "\t weight:" + weight;
    }



}
