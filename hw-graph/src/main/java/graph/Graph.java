package graph;

import java.util.*;

/**
 * Graph is a generic mutable finite set of immutable nodes connected by directed edges with immutable
 * labels. Behavior of Graph is unspecified when either node or edge label types are mutable.
 *
 * Each Graph can be described by
 * [[n1 - n1_a (L1_a), n1_b (L1_b), ...], [n2 - n2_a (L2_a), n2_b (L2_b), ...], ...],
 * where [] is an empty graph, [[n1]] is a one node graph containing the node n1 with no edges,
 * [[n1 - n1_a (L1_a), n1_b (L1_b), ...], [n1_a], [n1_b], ...] is a graph containing the nodes
 * n1, n1_a, n1_b, ... where n1_a, n1_b, ... are the nodes to which n1 has an edge pointing from
 * itself to each of the nodes, and so on. Graph can also be described constructively, with the
 * append operation, ':' such that [n1]:G is the result of putting the node n1 in the Graph G.
 *
 * Nodes in Graph can only have edges between other existing nodes. Labels of edges are not unique,
 * but no more than one edge between a source and destination node pair can have the same label.
 * All nodes in Graph are unique.
 *
 * @param <N> the type of nodes
 * @param <E> the type of edge labels
 */
public class Graph<N, E> implements Iterable<N> {
    public static final boolean DEBUG = false;

    private Map<N, Set<Edge<N, E>>> graph;

    // Abstraction Function:
    // A map of key/value pairs where keys are the nodes in the graph
    // and values are a collection of the outgoing edges associated with
    // a given node
    //
    // Rep Invariant:
    // graph != null &&
    // graph doesn't contain null nodes &&
    // graph doesn't contain null collections of outgoing edges &&
    // graph doesn't contain null edges &&
    // graph doesn't contain duplicate nodes &&
    // no edges with same src and dst nodes have duplicate labels

    /**
     * Checks that the rep is maintained
     */
    private void checkRep() {
        assert graph != null;

        if (DEBUG) {
            for (N node : graph.keySet()) {
                assert node != null;

                Set<Edge<N, E>> outgoingEdges = graph.get(node);

                assert outgoingEdges != null;

                for (Edge<N, E> edge : outgoingEdges) {
                    assert edge != null;
                }
            }
        }
    }

    /**
     * @spec.effects Constructs a new Graph, []
     */
    public Graph() {
        graph = new HashMap<>();
        checkRep();
    }

    /**
     * Adds node into this
     *
     * @param nodeData the data for the node to add into this
     * @return true if and only if nodeData is added successfully to this. Note that null
     * values and non-unique nodeData (present in graph) will be ignored.
     * @spec.modifies this
     * @spec.effects this_post = [nodeData]:G
     */
    public boolean addNode(N nodeData) {
        checkRep();

        if (nodeData == null || graph.containsKey(nodeData)) {
            checkRep();
            return false;
        }

        graph.put(nodeData, new HashSet<>());

        checkRep();

        return true;
    }

    /**
     * Adds edge to existing nodes in this
     *
     * @param label the label of the edge
     * @param src the source node from which the edge starts
     * @param dst the destination node to which the edge points
     * @return true if and only if the edge src - dst (label) is added successfully to this.
     * Note that null values, edges between nonexistent nodes, and non-unique labels (present
     * in existing edge between src and dst) will be ignored.
     * @spec.modifies this
     * @spec.effects If this = [[src], [dst]], then this_post = [[src - dst (label)], [dst]]
     */
    public boolean addEdge(E label, N src, N dst) {
        checkRep();

        if (label == null || src == null || dst == null ||
            !graph.containsKey(src) || !graph.containsKey(dst)) {
            checkRep();
            return false;
        }

        boolean result = graph.get(src).add(new Edge<>(label, src, dst));

        checkRep();

        return result;
    }

    /**
     * Returns the children of parent
     *
     * @param parent the parent node whose children are to be received
     * @return all nodes ni such that parent - ni (Li) where i is arbitrary
     * @throws NullPointerException if parent == null
     */
    public List<N> getChildren(N parent) {
        checkRep();

        if (parent == null) {
            throw new NullPointerException();
        }

        List<N> children = new ArrayList<>();

        // Inv: children contains all destination nodes of
        // outgoing edges of parent from 0 to i-1
        for (Edge<N, E> edge : graph.get(parent)) {
            children.add(edge.getDst());
        }

        checkRep();

        return children;
    }

    /**
     * Returns the parents of child
     *
     * @param child the child node whose parents are to be received
     * @return all nodes ni such that ni - child (Li) where i is arbitrary
     * @throws NullPointerException if child == null
     */
    public List<N> getParents(N child) {
        checkRep();

        if (child == null) {
            throw new NullPointerException();
        }

        List<N> parents = new ArrayList<>();

        // Inv: parents contains all source nodes of outgoing
        // edges of nodes from 0 to i-1 where edge.getDst() == child
        for (N node : graph.keySet()) {
            // Inv: parents contains all source nodes of edges from
            // 0 to j-1 where edge.getDst() == child
            for (Edge<N, E> edge : graph.get(node)) {
                if (edge.getDst().equals(child)) {
                    parents.add(edge.getSrc());
                }
            }
        }

        checkRep();

        return parents;
    }

    /**
     * Returns the edges that possess label
     *
     * @param label the label to check if edges possess
     * @return all edges ni - nj (label) where i,j are arbitrary
     * @throws NullPointerException if label == null
     */
    public List<Edge<N, E>> getEdgesByLabel(N label) {
        checkRep();

        if (label == null) {
            throw new NullPointerException();
        }

        List<Edge<N, E>> edges = new ArrayList<>();

        // Inv: edges contains outgoing edges of all nodes from
        // 0 to i-1 where edge.getLabel() == label
        for (N node : graph.keySet()) {
            // Inv: edges contains all edges from 0 to j-1 where edge.getLabel() == label
            for (Edge<N, E> edge : graph.get(node)) {
                if (edge.getLabel().equals(label)) {
                    edges.add(edge);
                }
            }
        }

        checkRep();

        return edges;
    }

    /**
     * Returns the incoming edges of nodeData
     *
     * @param nodeData the node to which to find the edges pointing to
     * @return all edges ni - nodeData (Li) where i is arbitrary
     * @throws NullPointerException if nodeData == null
     */
    public List<Edge<N, E>> getIncomingEdges(N nodeData) {
        checkRep();

        if (nodeData == null) {
            throw new NullPointerException();
        }

        List<Edge<N, E>> edges = new ArrayList<>();

        // Inv: edges contains outgoing edges of all nodes from
        // 0 to i-1 where edge.getDst() == nodeData
        for (N node : graph.keySet()) {
            // Inv: edges contains all edges from 0 to j-1 where edge.getDst() == nodeData
            for (Edge<N, E> edge : graph.get(node)) {
                if (edge.getDst().equals(nodeData)) {
                    edges.add(edge);
                }
            }
        }

        checkRep();

        return edges;
    }

    /**
     * Returns the outgoing edges of nodeData
     *
     * @param nodeData the node to which to find the edges pointing from
     * @return all edges nodeData - ni (Li) where i is arbitrary
     * @throws NullPointerException if nodeData == null
     */
    public List<Edge<N, E>> getOutgoingEdges(N nodeData) {
        checkRep();

        if (nodeData == null) {
            throw new NullPointerException();
        }

        List<Edge<N, E>> edges = new ArrayList<>();

        if (graph.containsKey(nodeData)) {
            edges.addAll(graph.get(nodeData));
        }

        checkRep();

        return edges;
    }

    /**
     * Returns whether nodeData is present in this
     *
     * @param nodeData the data of the node to check if present in this
     * @return true if and only if nodeData is present in this
     * @throws NullPointerException if nodeData == null
     */
    public boolean containsNode(N nodeData) {
        checkRep();

        if (nodeData == null) {
            throw new NullPointerException();
        }

        boolean result = graph.containsKey(nodeData);

        checkRep();

        return result;
    }

    /**
     * Returns whether edge with label is present from src node to dst node
     *
     * @param label the label of the edge to check if present from src node to dst node
     * @param src the source node to check if edge with label exists from
     * @param dst the destination node to check if edge with label exists to
     * @return true if and only if src - dst (label) is an edge in this
     * @throws NullPointerException if label == null || src == null || dst == null
     */
    public boolean containsEdge(E label, N src, N dst) {
        checkRep();

        if (label == null || src == null || dst == null) {
            throw new NullPointerException();
        }

        Edge<N, E> edgeToCheck = new Edge<>(label, src, dst);

        // Inv: All nodes from 0 to i-1 don't contain
        // edgeToCheck as one of their outgoing edges
        for (N node : graph.keySet()) {
            if (graph.get(node).contains(edgeToCheck)) {
                checkRep();
                return true;
            }
        }

        checkRep();

        return false;
    }

    /**
     * Returns an iterator of the nodes contained in the graph
     *
     * @return an iterator of a read-only view of the nodes contained in
     * the graph in no particular order
     */
    @Override
    public Iterator<N> iterator() {
        checkRep();

        Set<N> copy = Collections.unmodifiableSet(graph.keySet());

        Iterator<N> nodeIterator = copy.iterator();

        checkRep();

        return nodeIterator;
    }

    /**
     * Edge is a generic immutable connection between two immutable nodes with direction and
     * an immutable label. Behavior of Edge is unspecified when either node or edge label
     * types are mutable.
     *
     * Each Edge can be described by src - dst (L), where src points to dst and L is the label
     *
     * @param <N> the type of nodes
     * @param <E> the type of edge labels
     */
    public static class Edge<N, E> {
        private final E label;
        private final N src;
        private final N dst;

        // Abstraction Function:
        // label is the weight of the edge,
        // src is the source node from which the directed edge starts,
        // dst is the destination node to which the directed edge ends
        //
        // Rep Invariant:
        // label != null && src != null && dst != null

        /**
         * Checks that the rep is maintained
         */
        private void checkRep() {
            assert label != null;
            assert src != null;
            assert dst != null;
        }

        /**
         * @param label the label to apply to the connection from src to dst
         * @param src the src node from which the edge starts
         * @param dst the dst node to which the edge ends
         * @throws NullPointerException if label == null || src == null || dst == null
         * @spec.effects Constructs a new Edge, src - dst (label)
         */
        public Edge(E label, N src, N dst) {
            if (label == null || src == null || dst == null) {
                throw new NullPointerException();
            }

            this.label = label;
            this.src = src;
            this.dst = dst;

            checkRep();
        }

        /**
         * Returns the label of this
         *
         * @return if this = src - dst (L), then returns L
         */
        public E getLabel() {
            checkRep();

            return label;
        }

        /**
         * Returns the src node of this
         *
         * @return if this = src - dst (L), then returns src
         */
        public N getSrc() {
            checkRep();

            return src;
        }

        /**
         * Returns the dst node of this
         *
         * @return if this = src - dst (L), then returns dst
         */
        public N getDst() {
            checkRep();

            return dst;
        }

        /**
         * Returns the hash code of this
         *
         * @return a hash code that all objects equal to this will also
         */
        @Override
        public int hashCode() {
            checkRep();

            int result = label.hashCode();
            result = 31 * result + src.hashCode();
            result = 31 * result + dst.hashCode();

            checkRep();

            return result;
        }

        /**
         * Returns whether obj is equal to this
         *
         * @param obj the object to be compared for equality
         * @return true if and only if obj is an instance of Edge and this and obj represent
         * the same edge
         */
        @Override
        public boolean equals(Object obj) {
            checkRep();

            if (!(obj instanceof Edge<?, ?>)) {
                checkRep();
                return false;
            }

            Edge<?, ?> edge = (Edge<?, ?>) obj;

            boolean result = this.label.equals(edge.label) &&
                             this.src.equals(edge.src) && this.dst.equals(edge.dst);

            checkRep();

            return result;
        }
    }
}
