package pathfinder;

import graph.Graph;
import pathfinder.datastructures.Path;

import java.util.*;

public class PathFinderPaths {
    public static <E> Path<E> findPath(Graph<E, Double> graph, E src, E dst) { // +=+ Check <E>
        PriorityQueue<Path<E>> active = new PriorityQueue<>(new Comparator<>() {
            @Override
            public int compare(Path<E> p1, Path<E> p2) {
                return Double.compare(p1.getCost(), p2.getCost());
            }
        });

        Set<E> finished = new HashSet<>();

        active.add(new Path<>(src));

        while (!active.isEmpty()) {
            Path<E> minPath = active.poll(); // +=+ Poll vs remove
            E minDest = minPath.getEnd();

            if (minDest.equals(dst)) {
                return minPath;
            }

            if (finished.contains(minDest)) {
                continue;
            }

            for (Graph.Edge<E, Double> edge : graph.getOutgoingEdges(minDest)) {
                if (!finished.contains(edge.getDst())) {
                    Path<E> newPath = minPath.extend(edge.getDst(), edge.getLabel()); // +=+ Check
                    active.add(newPath);
                }
            }

            finished.add(minDest);
        }

        return null;
    }
}
