package MinHeap;

import java.util.ArrayList;
import java.util.Arrays;

public class MinHeapTest {
    public static <T> void main(String[] args) throws Exception {
        ArrayList<Float> arrayList = new ArrayList<>();
        arrayList.add(0, (float) 10);
        arrayList.add(1,(float)101);
        arrayList.add(2,(float)13);
        arrayList.add(3,(float)16);
        arrayList.add(4,(float)24);
        arrayList.add(5,(float)71);
        arrayList.add(6,(float)11);
        arrayList.add(7,(float)2);
        arrayList.add(8,(float)8);


        MinHeap MinHeap = new MinHeap(arrayList);

        System.out.println("MinHeap array is : \n" + MinHeap + "\n");

        T elem = (T) MinHeap.extractMin();

        System.out.println("MinHeap array after extractMin() " + elem.toString() + " is : \n" + MinHeap + "\n");


    }


}
