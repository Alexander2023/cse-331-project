package marvel;

import graph.Graph;

import java.util.*;

/**
 * Path finding utility for a graph
 */
public class MarvelPaths {

    // This is a container for related functionality, not an ADT

    public static void main(String[] args) {

    }

    public static Graph buildGraph(String fileName) {
        Graph graph = new Graph();
        Map<String, List<String>> parsedData = MarvelParser.parseData(fileName);

        for (String book : parsedData.keySet()) {
            List<String> characters = parsedData.get(book);

            for (String character : characters) {
                graph.addNode(character);
            }

            if (characters.size() > 1) {
                for (int i = 0; i < characters.size() - 1; i++) {
                    for (int j = i + 1; j < characters.size(); j++) {
                        String src = characters.get(i);
                        String dst = characters.get(j);

                        if (!src.equals(dst)) {
                            graph.addEdge(book, src, dst);
                            graph.addEdge(book, dst, src);
                        }
                    }
                }
            }
        }

        return graph;
    }

    public static List<Graph.Edge> findPath(Graph graph, String src, String dst) {
        Queue<String> workList = new LinkedList<>();
        Map<String, List<Graph.Edge>> visited = new HashMap<>();

        workList.add(src);
        visited.put(src, new ArrayList<>());

        while (!workList.isEmpty()) {
            String node = workList.remove();

            if (node.equals(dst)) {
                return visited.get(node);
            }

            List<Graph.Edge> outgoingEdges = graph.getOutgoingEdges(node);

            Collections.sort(outgoingEdges, new Comparator<Graph.Edge>() { // are comparators or streams preferred?
                @Override
                public int compare(Graph.Edge e1, Graph.Edge e2) {
                    int dstComparison = e1.getDst().compareTo(e2.getDst());

                    if (dstComparison != 0) {
                        return dstComparison;
                    }

                    return e1.getLabel().compareTo(e2.getLabel());
                }
            });

            for (Graph.Edge edge : outgoingEdges) {
                if (!visited.containsKey(edge.getDst())) {
                    List<Graph.Edge> copy = new ArrayList<>(visited.get(node));
                    copy.add(edge);

                    visited.put(edge.getDst(), copy);
                    workList.add(edge.getDst());
                }
            }
        }

        return null;
    }
}
