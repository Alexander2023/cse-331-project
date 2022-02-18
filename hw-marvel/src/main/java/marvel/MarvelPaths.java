package marvel;

import graph.Graph;

import java.util.*;

/**
 * Path finding utility for a graph
 */
public class MarvelPaths {
    private static final String marvelFile = "marvel.csv";

    // This is a container for related functionality, not an ADT

    /**
     * Runs a program that allows for finding paths between Marvel characters
     *
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        Graph graph = buildGraph(marvelFile);

        System.out.println("Welcome to the Marvel universe!");

        Scanner console = new Scanner(System.in);

        promptUser();
        String input = console.next();
        while (!input.equals("q")) {
            String[] split = input.split(",");

            if (split.length == 2) {
                String src = split[0].toUpperCase();
                String dst = split[1].toUpperCase();

                if (!graph.containsNode(src) || !graph.containsNode(dst)) {
                    System.out.println("The Marvel graph doesn't contain one or more of these characters");
                } else {
                    List<Graph.Edge> path = findPath(graph, src, dst);

                    if (path == null) {
                        System.out.println("No path exists between " + src + " and " + dst);
                    } else {
                        System.out.println("The path from " + src + " to " + dst + " is:");

                        for (Graph.Edge edge : path) {
                            System.out.println(edge.getSrc() + " to " + edge.getDst() + " via " + edge.getLabel());
                        }
                    }
                }
            } else {
                System.out.println("Input format not recognized");
            }

            promptUser();
            input = console.next();
        }
    }

    /**
     * Prints the standard prompt for user input
     */
    private static void promptUser() {
        System.out.println();
        System.out.print("Enter a pair of characters separated by a comma ");
        System.out.println("and no spaces (i.e. char1,char2) or q to quit");
    }

    /**
     * Builds a graph from the data in the file
     *
     * @param fileName the file to build the graph
     * @return a graph of the data
     * @spec.requires filename is a valid file in the resources/data folder
     */
    public static Graph buildGraph(String fileName) {
        Graph graph = new Graph();
        Map<String, List<String>> parsedData = MarvelParser.parseData(fileName);

        // All characters in books from 0 to i-1 have edges to and from
        // all other characters in the same book
        for (String book : parsedData.keySet()) {
            List<String> characters = parsedData.get(book);

            // Inv: All characters from 0 to j-1 have been added to graph
            for (String character : characters) {
                graph.addNode(character);
            }

            if (characters.size() > 1) {
                // Inv: All characters from 0 to j-1 have edges to and
                // from all other characters
                for (int i = 0; i < characters.size() - 1; i++) {
                    // Inv: All characters from j+1 to k-1, where j < k,
                    // have edges to and from the jth character
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

    /**
     * Finds the shortest path between src and dst in the graph
     *
     * @param graph the graph to search for the path
     * @param src the source node to start the path
     * @param dst the destination node to end the path
     * @return shortest path of edges from the src to dst
     * @spec.requires graph != null, src != null, dst != null,
     * src and dst are existing nodes within graph
     */
    public static List<Graph.Edge> findPath(Graph graph, String src, String dst) {
        Queue<String> workList = new LinkedList<>();
        Map<String, List<Graph.Edge>> visited = new HashMap<>();

        workList.add(src);
        visited.put(src, new ArrayList<>());

        // Inv: All paths from edges of src from 0 to i-1
        // have been examined for a path to dst
        while (!workList.isEmpty()) {
            String node = workList.remove();

            if (node.equals(dst)) {
                return visited.get(node);
            }

            List<Graph.Edge> outgoingEdges = graph.getOutgoingEdges(node);

            Collections.sort(outgoingEdges, new Comparator<>() {
                @Override
                public int compare(Graph.Edge e1, Graph.Edge e2) {
                    int dstComparison = e1.getDst().compareTo(e2.getDst());

                    if (dstComparison != 0) {
                        return dstComparison;
                    }

                    return e1.getLabel().compareTo(e2.getLabel());
                }
            });

            // Inv: All dst nodes of edges from 0 to j-1 have been visited
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
