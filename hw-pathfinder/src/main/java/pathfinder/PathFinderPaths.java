package pathfinder;

import graph.Graph;
import pathfinder.datastructures.Path;

import java.util.*;

/**
 * PathFinderPaths is a path finding utility for finding the least-cost path between a
 * pair of nodes
 */
public class PathFinderPaths {
    // This class does not represent an ADT

    /**
     * Finds the least-cost path from src to dst
     *
     * @param graph the graph to search for a path
     * @param src the source node to start the path
     * @param dst the destination node to end the path
     * @param <N> the type of nodes
     * @return the least-cost path from src to dst
     * @spec.requires no negative edges in graph
     */
    public static <N> Path<N> findPath(Graph<N, Double> graph, N src, N dst) {
        // Uses Dijkstra's shortest path algorithm

        PriorityQueue<Path<N>> active = new PriorityQueue<>(new Comparator<>() {
            @Override
            public int compare(Path<N> p1, Path<N> p2) {
                return Double.compare(p1.getCost(), p2.getCost());
            }
        });

        Set<N> finished = new HashSet<>();

        active.add(new Path<>(src));

        // Inv: All paths from src to a given node from 0 to i-1
        // have been examined for a least-cost path to dst
        while (!active.isEmpty()) {
            Path<N> minPath = active.remove();
            N minDest = minPath.getEnd();

            if (minDest.equals(dst)) {
                return minPath;
            }

            if (finished.contains(minDest)) {
                continue;
            }

            // Inv: All edges from 0 to j-1 have had their minimum-cost
            // path from src examined
            for (Graph.Edge<N, Double> edge : graph.getOutgoingEdges(minDest)) {
                if (!finished.contains(edge.getDst())) {
                    Path<N> newPath = minPath.extend(edge.getDst(), edge.getLabel());
                    active.add(newPath);
                }
            }

            finished.add(minDest);
        }

        return null;
    }
}
