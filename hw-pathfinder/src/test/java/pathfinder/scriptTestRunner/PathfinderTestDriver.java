/*
 * Copyright (C) 2022 Hal Perkins.  All rights reserved.  Permission is
 * hereby granted to students registered for University of Washington
 * CSE 331 for use solely during Winter Quarter 2022 for purposes of
 * the course.  No other use, copying, distribution, or modification
 * is permitted without prior written consent. Copyrights for
 * third-party components of this work must be honored.  Instructors
 * interested in reusing these course materials should contact the
 * author.
 */

package pathfinder.scriptTestRunner;

import graph.Graph;
import pathfinder.PathFinderPaths;
import pathfinder.datastructures.Path;

import java.io.*;
import java.util.*;

/**
 * This class implements a test driver that uses a script file format
 * to test an implementation of Dijkstra's algorithm on a graph.
 */
public class PathfinderTestDriver {
    // ***************************
    // ***  JUnit Test Driver  ***
    // ***************************

    // This is a container for related functionality, not an ADT

    /**
     * String -> Graph: maps the names of graphs to the actual graph
     **/
    private final Map<String, Graph<String, Double>> graphs = new HashMap<>();
    private final PrintWriter output;
    private final BufferedReader input;

    /**
     * @spec.requires r != null && w != null
     * @spec.effects Creates a new GraphTestDriver which reads command from
     * {@code r} and writes results to {@code w}
     **/
    // Leave this constructor public
    public PathfinderTestDriver(Reader r, Writer w) {
        input = new BufferedReader(r);
        output = new PrintWriter(w);
    }

    /**
     * @throws IOException if the input or output sources encounter an IOException
     * @spec.effects Executes the commands read from the input and writes results to the output
     **/
    // Leave this method public
    public void runTests() throws IOException {
        String inputLine;
        while((inputLine = input.readLine()) != null) {
            if((inputLine.trim().length() == 0) ||
                    (inputLine.charAt(0) == '#')) {
                // echo blank and comment lines
                output.println(inputLine);
            } else {
                // separate the input line on white space
                StringTokenizer st = new StringTokenizer(inputLine);
                if(st.hasMoreTokens()) {
                    String command = st.nextToken();

                    List<String> arguments = new ArrayList<>();
                    while(st.hasMoreTokens()) {
                        arguments.add(st.nextToken());
                    }

                    executeCommand(command, arguments);
                }
            }
            output.flush();
        }
    }

    private void executeCommand(String command, List<String> arguments) {
        try {
            switch(command) {
                case "CreateGraph":
                    createGraph(arguments);
                    break;
                case "AddNode":
                    addNode(arguments);
                    break;
                case "AddEdge":
                    addEdge(arguments);
                    break;
                case "ListNodes":
                    listNodes(arguments);
                    break;
                case "ListChildren":
                    listChildren(arguments);
                    break;
                case "FindPath":
                    findPath(arguments);
                    break;
                default:
                    output.println("Unrecognized command: " + command);
                    break;
            }
        } catch(Exception e) {
            String formattedCommand = command;
            formattedCommand += arguments.stream().reduce("", (a, b) -> a + " " + b);
            output.println("Exception while running command: " + formattedCommand);
            e.printStackTrace(output);
        }
    }

    private void createGraph(List<String> arguments) {
        if(arguments.size() != 1) {
            throw new CommandException("Bad arguments to CreateGraph: " + arguments);
        }

        String graphName = arguments.get(0);
        createGraph(graphName);
    }

    private void createGraph(String graphName) {
        graphs.put(graphName, new Graph<>());

        output.println("created graph " + graphName);
    }

    private void addNode(List<String> arguments) {
        if(arguments.size() != 2) {
            throw new CommandException("Bad arguments to AddNode: " + arguments);
        }

        String graphName = arguments.get(0);
        String nodeName = arguments.get(1);

        addNode(graphName, nodeName);
    }

    private void addNode(String graphName, String nodeName) {
        Graph<String, Double> graph = graphs.get(graphName);

        graph.addNode(nodeName);

        output.println("added node " + nodeName + " to " + graphName);
    }

    private void addEdge(List<String> arguments) {
        if(arguments.size() != 4) {
            throw new CommandException("Bad arguments to AddEdge: " + arguments);
        }

        String graphName = arguments.get(0);
        String parentName = arguments.get(1);
        String childName = arguments.get(2);
        Double edgeLabel = Double.parseDouble(arguments.get(3)); // +=+ Would the exceptions thrown count as undefined behavior?

        addEdge(graphName, parentName, childName, edgeLabel);
    }

    private void addEdge(String graphName, String parentName, String childName,
                         Double edgeLabel) {
        Graph<String, Double> graph = graphs.get(graphName);

        graph.addEdge(edgeLabel, parentName, childName);

        String fLabel = String.format("%.3f", edgeLabel);
        output.println("added edge " + fLabel + " from " + parentName + " to " + childName + " in " + graphName);
    }

    private void listNodes(List<String> arguments) {
        if(arguments.size() != 1) {
            throw new CommandException("Bad arguments to ListNodes: " + arguments);
        }

        String graphName = arguments.get(0);
        listNodes(graphName);
    }

    private void listNodes(String graphName) {
        Graph<String, Double> graph = graphs.get(graphName);

        List<String> nodes = new ArrayList<>();

        for (String node: graph) {
            nodes.add(node);
        }

        Collections.sort(nodes);

        output.print(graphName + " contains:");

        for (String node: nodes) {
            output.print(" " + node);
        }

        output.println();
    }

    private void listChildren(List<String> arguments) {
        if(arguments.size() != 2) {
            throw new CommandException("Bad arguments to ListChildren: " + arguments);
        }

        String graphName = arguments.get(0);
        String parentName = arguments.get(1);
        listChildren(graphName, parentName);
    }

    private void listChildren(String graphName, String parentName) {
        Graph<String, Double> graph = graphs.get(graphName);

        List<Graph.Edge<String, Double>> edges = graph.getOutgoingEdges(parentName);

        Collections.sort(edges, new Comparator<>() {
            @Override
            public int compare(Graph.Edge<String, Double> e1, Graph.Edge<String, Double> e2) {
                int dstComparison = e1.getDst().compareTo(e2.getDst());

                if (dstComparison != 0) {
                    return dstComparison;
                }

                return e1.getLabel().compareTo(e2.getLabel());
            }
        });

        output.print("the children of " + parentName + " in " + graphName + " are:");

        for (Graph.Edge<String, Double> edge : edges) {
            String fLabel = String.format("%.3f", edge.getLabel());
            output.print(" " + edge.getDst() + "(" + fLabel + ")");
        }

        output.println();
    }

    private void findPath(List<String> arguments) {
        if(arguments.size() != 3) {
            throw new CommandException("Bad arguments to FindPath: " + arguments);
        }

        String graphName = arguments.get(0);
        String src = arguments.get(1);
        String dst = arguments.get(2);

        findPath(graphName, src, dst);
    }

    private void findPath(String graphName, String src, String dst) {
        Graph<String, Double> graph = graphs.get(graphName);

        if (!graph.containsNode(src) || !graph.containsNode(dst)) {
            if (!graph.containsNode(src)) {
                output.println("unknown: " + src);
            }

            if (!graph.containsNode(dst)) {
                output.println("unknown: " + dst);
            }

            return;
        }

        output.println("path from " + src + " to " + dst + ":");

        if (!src.equals(dst)) {
            Path<String> path = PathFinderPaths.findPath(graph, src, dst);

            if (path == null) {
                output.println("no path found");
            } else {
                for (Path<String>.Segment segment : path) {
                    String fCost = String.format("%.3f", segment.getCost());
                    output.println(segment.getStart() + " to " + segment.getEnd() +
                                   " with weight " + fCost);
                }
                String fCost = String.format("%.3f", path.getCost());
                output.println("total cost: " + fCost);
            }
        }
    }

    /**
     * This exception results when the input file cannot be parsed properly
     **/
    static class CommandException extends RuntimeException {

        public CommandException() {
            super();
        }

        public CommandException(String s) {
            super(s);
        }

        public static final long serialVersionUID = 3495;
    }
}
