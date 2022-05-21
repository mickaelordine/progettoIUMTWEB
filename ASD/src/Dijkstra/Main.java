package Dijkstra;

import Graph.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {
    public static String start = "pagliarazzi";
    public static String end = "testa dell'acqua";
    private static String filepath = "italian_dist_graph.csv";
    private static final Charset ENCODING = StandardCharsets.UTF_8;

    public static void main(String[] args) {
        Graph<String,Float> graph = new Graph<String, Float>(false);
        Path inputFilePath = Paths.get(filepath);

        System.out.println("Loading data from file...");

        try (BufferedReader fileinputrReader = Files.newBufferedReader(inputFilePath, ENCODING)) {
            String line = null;

            while ((line = fileinputrReader.readLine()) != null) {
                String[] lineElements = line.split(",");

                graph.add_arch(lineElements[0], lineElements[1], Float.parseFloat(lineElements[2]));
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("DONE\n");

        System.out.println("Graph generated:\n" + graph);

        try {
            System.out.println("Applying Dijkstra ... ");

            List<Arch<String, Float>> resultArches = new Dijkstra<String, Float>().Dijkstra(graph, new FloatCompare());

            System.out.println("Arches found: \n" + resultArches);
            System.out.println("Number of arches found: " + resultArches.size() + "\n" + printResult(resultArches));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public static String printResult(List<Arch<String, Float>> list) {
        Map<String, Integer> temp = new HashMap<>();
        int contVertex = 0;
        float Weight = 0;

        for (Arch<String, Float> edge : list) {
            System.out.println(edge);

            if (!temp.containsKey(edge.getStart())) {
                temp.put(edge.getStart(), 0);
                contVertex++;
            }

            if (!temp.containsKey(edge.getEnd())) {
                temp.put(edge.getEnd(), 0);
                contVertex++;
            }

            Weight += edge.getWeight();
        }

        return "Nuber of vertex: " + contVertex + "\nTotal weight " + Weight / 1000 + "km";
    }
}
