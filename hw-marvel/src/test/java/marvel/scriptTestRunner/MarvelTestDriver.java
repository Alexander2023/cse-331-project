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

package marvel.scriptTestRunner;

import graph.Graph;
import marvel.MarvelPaths;

import java.io.*;
import java.util.*;

/**
 * This class implements a testing driver which reads test scripts from
 * files for testing Graph, the Marvel parser, and your BFS algorithm.
 */
public class MarvelTestDriver {
    // ***************************
    // ***  JUnit Test Driver  ***
    // ***************************

    // This is a container for related functionality, not an ADT

    /**
     * String -> Graph: maps the names of graphs to the actual graph
     **/
    private final Map<String, Graph> graphs = new HashMap<>();
    private final PrintWriter output; // Should these be kept as Reader/Writer?
    private final BufferedReader input;

    /**
     * @spec.requires r != null && w != null
     * @spec.effects Creates a new GraphTestDriver which reads command from
     * {@code r} and writes results to {@code w}
     **/
    // Leave this constructor public
    public MarvelTestDriver(Reader r, Writer w) {
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
                case "LoadGraph":
                    loadGraph(arguments);
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
        graphs.put(graphName, new Graph());

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
        Graph graph = graphs.get(graphName);

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
        String edgeLabel = arguments.get(3);

        addEdge(graphName, parentName, childName, edgeLabel);
    }

    private void addEdge(String graphName, String parentName, String childName,
                         String edgeLabel) {
        Graph graph = graphs.get(graphName);

        graph.addEdge(edgeLabel, parentName, childName);

        output.println("added edge " + edgeLabel + " from " + parentName + " to " + childName + " in " + graphName);
    }

    private void listNodes(List<String> arguments) {
        if(arguments.size() != 1) {
            throw new CommandException("Bad arguments to ListNodes: " + arguments);
        }

        String graphName = arguments.get(0);
        listNodes(graphName);
    }

    private void listNodes(String graphName) {
        Graph graph = graphs.get(graphName);

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
        Graph graph = graphs.get(graphName);

        List<Graph.Edge> edges = graph.getOutgoingEdges(parentName);

        Collections.sort(edges, new Comparator<>() {
            @Override
            public int compare(Graph.Edge e1, Graph.Edge e2) {
                int dstComparison = e1.getDst().compareTo(e2.getDst());

                if (dstComparison != 0) {
                    return dstComparison;
                }

                return e1.getLabel().compareTo(e2.getLabel());
            }
        });

        output.print("the children of " + parentName + " in " + graphName + " are:");

        for (Graph.Edge edge : edges) {
            output.print(" " + edge.getDst() + "(" + edge.getLabel() + ")");
        }

        output.println();
    }

    private void loadGraph(List<String> arguments) {
        if(arguments.size() != 2) {
            throw new CommandException("Bad arguments to LoadGraph: " + arguments);
        }

        String graphName = arguments.get(0);
        String file = arguments.get(1);

        loadGraph(graphName, file);
    }

    private void loadGraph(String graphName, String file) {
        graphs.put(graphName, MarvelPaths.buildGraph(file));

        output.println("loaded graph " + graphName);
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
        Graph graph = graphs.get(graphName);

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
            List<Graph.Edge> path = MarvelPaths.findPath(graph, src, dst);

            if (path == null) {
                output.println("no path found");
            } else {
                for (Graph.Edge edge : path) {
                    output.println(edge.getSrc() + " to " + edge.getDst() + " via " + edge.getLabel());
                }
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
