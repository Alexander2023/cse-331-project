package graph;

import java.util.Iterator;

/**
 * Graph is a mutable finite set of nodes connected by edges.
 * Each Graph can be described by
 * [[n1 -> n1_a (L1_a), n1_b (L1_b), ...], [n2 -> n2_a (L2_a), n2_b (L2_b), ...], ...],
 * where [] is an empty graph, [[n1]] is a one node graph containing the node n1 with no edges,
 * [[n1 -> n1_a (L1_a), n1_b (L1_b), ...], [n1_a], [n1_b], ...] is a graph containing the nodes
 * n1, n1_a, n1_b, ... where n1_a, n1_b, ... are the nodes to which n1 has an edge pointing from
 * itself to each of the nodes, and so on. Graph can also be described constructively, with the
 * append operation, ':' such that [n1]:G is the result of putting the node n1 in the Graph G
 * and L1[n1_a]:n1 is the result of adding an edge from node n1 to node n1_a with label L1.
 *
 * Nodes in Graph can only have edges between other existing nodes. Labels of edges are not unique,
 * but no more than one edge between a source and destination node pair can have the same label.
 * All nodes in Graph are unique.
 */
public class Graph implements Iterable<String> {
    /**
     * @spec.effects Constructs a new Graph, []
     */
    public Graph() {
        // TODO: Fill in this method, then remove the RuntimeException
        throw new RuntimeException("Graph.constructor is not yet implemented");
    }

    /**
     * Adds node into this
     *
     * @param nodeData the data for the node to add into this
     * @spec.requires nodeData != null && nodeData is unique (not present in graph)
     * @spec.modifies this
     * @spec.effects this_post = [nodeData]:G
     */
    public void addNode(String nodeData) {
        // TODO: Fill in this method, then remove the RuntimeException
        throw new RuntimeException("Graph.addNode() is not yet implemented");
    }

    /**
     * Adds edge to existing nodes in this
     *
     * @param label the label of the edge
     * @param src the source node from which the edge starts
     * @param dst the destination node to which the edge points
     * @spec.requires label != null && src != null && dst != null && [src, dst]:G &&
     * label is unique (not present in existing edge between src and dst)
     * @spec.modifies this
     * @spec.effects If this = [[src], [dst]], then this_post = [[src -> dst (label)], [dst]]
     */
    public void addEdge(String label, String src, String dst) {
        // TODO: Fill in this method, then remove the RuntimeException
        throw new RuntimeException("Graph.addEdge() is not yet implemented");
    }

    /**
     * Returns the children of parent
     *
     * @param parent the parent node whose children are to be received
     * @return if this = [[n1 -> n1_a (L1_a), n1_b (L1_b), ...], [n1_a], [n1_b], ...] where
     * n1 = parent, then returns [n1_a, n1_b, ...]
     * @spec.requires parent != null
     */
    public String[] getChildren(String parent) {
        // TODO: Fill in this method, then remove the RuntimeException
        throw new RuntimeException("Graph.getChildren() is not yet implemented");
    }

    /**
     * Returns the parents of child
     *
     * @param child the child node whose parents are to be received
     * @return if this = [[child], [n1 -> child (L1)], [n2 -> child (L2)], ...],
     * then returns [n1, n2, ...]
     * @spec.requires child != null
     */
    public String[] getParents(String child) {
        // TODO: Fill in this method, then remove the RuntimeException
        throw new RuntimeException("Graph.getParents() is not yet implemented");
    }

    /**
     * Returns the nodes connected, both to and from, edge containing label
     *
     * @param label the label of the edge whose connected nodes are to be received
     * @return if this = [[n1 -> n1_a (L1_a), n1_b (L1_b), ...]] where L1_a = label,
     * then returns [n1, n1_a]
     * @spec.requires label != null
     */
    public String[] getNodesByLabel(String label) {
        // TODO: Fill in this method, then remove the RuntimeException
        throw new RuntimeException("Graph.getNodesByLabel() is not yet implemented");
    }

    /**
     * Returns the edges that possess label
     *
     * @param label the label to check if edges possess
     * @return if this = [[n1 -> n1_a (label), n1_b (label), ...], [n1_a], [n1_b], ...],
     * then returns [n1 -> n1_a (label), n1 -> n1_b (label), ...]
     * @return returns all edges such that ni -> nj (label) where i,j are arbitrary +=+
     * @spec.requires label != null
     */
    public Edge[] getEdgesByLabel(String label) {
        // TODO: Fill in this method, then remove the RuntimeException
        throw new RuntimeException("Graph.getEdgesByLabel() is not yet implemented");
    }

    /**
     * Returns the incoming edges of nodeData
     *
     * @param nodeData the node to which to find the edges pointing to
     * @return if this = [[n1 -> n1_a (L1_a), n1_b (L1_b), ...]],
     * then returns
     * @spec.requires nodeData != null
     */
    public Edge[] getIncomingEdges(String nodeData) {
        // TODO: Fill in this method, then remove the RuntimeException
        throw new RuntimeException("Graph.getIncomingEdges() is not yet implemented");
    }

    public Edge[] getOutgoingEdges(String nodeData) {
        // TODO: Fill in this method, then remove the RuntimeException
        throw new RuntimeException("Graph.getOutgoingEdges() is not yet implemented");
    }

    /**
     * Returns whether nodeData is present in this
     *
     * @param nodeData the data of the node to check if present in this
     * @return true if and only if nodeData is present in this
     * @spec.requires nodeData != null
     */
    public boolean containsNode(String nodeData) {
        // TODO: Fill in this method, then remove the RuntimeException
        throw new RuntimeException("Graph.containsNode() is not yet implemented");
    }

    /**
     * Returns whether edge with label is present from src node to dst node
     *
     * @param label the label of the edge to check if present from src node to dst node
     * @param src the source node to check if edge with label exists from
     * @param dst the destination node to check if edge with label exists to
     * @return true if and only if src -> dst (label) is an edge in this
     * @spec.requires label != null, src != null, dst != null
     */
    public boolean containsEdge(String label, String src, String dst) {
        // TODO: Fill in this method, then remove the RuntimeException
        throw new RuntimeException("Graph.containsEdge() is not yet implemented");
    }

    /**
     * Returns an iterator of the nodes contained in the graph
     *
     * @return an iterator of the nodes contained in the graph in no particular order
     */
    @Override
    public Iterator<String> iterator() {
        // TODO: Fill in this method, then remove the RuntimeException
        throw new RuntimeException("Graph.iterator() is not yet implemented");
    }


    /**
     * Edge is an immutable connection between two nodes
     *
     * Each Edge can be described by src -> dst (L), where src points to dst and L is the label
     */
    public class Edge {
        /**
         * @param label the label to apply to the connection from src to dst
         * @param src the src node from which the edge starts
         * @param dst the dst node to which the edge ends
         * @spec.requires label != null, src != null, dst != null
         * @spec.effects Constructs a new Edge, src -> dst (L)
         */
        public Edge(String label, String src, String dst) {
            // TODO: Fill in this method, then remove the RuntimeException
            throw new RuntimeException("Edge.constructor is not yet implemented");
        }

        /**
         * Returns the label of this
         *
         * @return if this = src -> dst (L), then returns L
         */
        public String getLabel() {
            // TODO: Fill in this method, then remove the RuntimeException
            throw new RuntimeException("Edge.getLabel() is not yet implemented");
        }

        /**
         * Returns the src node of this
         *
         * @return if this = src -> dst (L), then returns src
         */
        public String getSrc() {
            // TODO: Fill in this method, then remove the RuntimeException
            throw new RuntimeException("Edge.getSrc() is not yet implemented");
        }

        /**
         * Returns the dst node of this
         *
         * @return if this = src -> dst (L), then returns dst
         */
        public String getDst() {
            // TODO: Fill in this method, then remove the RuntimeException
            throw new RuntimeException("Edge.getDst() is not yet implemented");
        }

        /**
         * Returns the hash code of this
         *
         * @return a hash code that all objects equal to this will also
         */
        @Override
        public int hashCode() {
            // TODO: Fill in this method, then remove the RuntimeException
            throw new RuntimeException("Edge.hashCode() is not yet implemented");
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
            // TODO: Fill in this method, then remove the RuntimeException
            throw new RuntimeException("Edge.equals() is not yet implemented");
        }
    }
}
