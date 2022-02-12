package graph.junitTests;

import graph.Graph;
import org.junit.Before;
import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.Timeout;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

public class GraphTest {
    private Graph g;

    @Rule public Timeout globalTimeout = Timeout.seconds(10); // 10 seconds max per method tested

    @Before
    public void setUp() {
        g = new Graph();
    }

    @Test
    public void testAddNullNode() {
        assertFalse(g.addNode(null));
    }

    @Test
    public void testAddNonUniqueNode() {
        g.addNode("n1");

        assertFalse(g.addNode("n1"));
    }

    @Test
    public void testAddNullEdge() {
        g.addNode("n1");
        g.addNode("n2");

        assertFalse(g.addEdge(null, "n1", "n2"));
    }

    @Test
    public void testAddEdgeWithNonexistentNodes() {
        assertFalse(g.addEdge("e1", "n1", "n2"));
    }

    @Test
    public void testAddEdgeWithNonUniqueLabel() {
        g.addNode("n1");
        g.addNode("n2");
        g.addEdge("e1", "n1", "n2");

        assertFalse(g.addEdge("e1", "n1", "n2"));
    }

    @Test (expected = NullPointerException.class)
    public void testGetChildrenWithNullParent() {
        g.getChildren(null);
    }

    @Test (expected = NullPointerException.class)
    public void testGetParentsWithNullChild() {
        g.getParents(null);
    }

    @Test
    public void testGetParentsOneParent() {
        g.addNode("n1");
        g.addNode("n2");
        g.addEdge("e1", "n1", "n2");

        List<String> parents = g.getParents("n2");

        assertEquals(1, parents.size());
        assertEquals("n1", parents.get(0));
    }

    @Test
    public void testGetParentsTwoDiffParents() {
        g.addNode("n1");
        g.addNode("n2");
        g.addNode("n3");
        g.addEdge("e1", "n1", "n3");
        g.addEdge("e2", "n2", "n3");

        List<String> parents = g.getParents("n3");
        Collections.sort(parents);

        assertEquals(2, parents.size());
        assertEquals("n1", parents.get(0));
        assertEquals("n2", parents.get(1));
    }

    @Test (expected = NullPointerException.class)
    public void testGetEdgesByLabelWithNullLabel() {
        g.getEdgesByLabel(null);
    }

    @Test
    public void testGetEdgesByLabelOneEdge() {
        g.addNode("n1");
        g.addNode("n2");
        g.addEdge("e1", "n1", "n2");

        List<Graph.Edge> edgesByLabel = g.getEdgesByLabel("e1");

        assertEquals(1, edgesByLabel.size());
        assertEquals(new Graph.Edge("e1", "n1", "n2"), edgesByLabel.get(0));
    }

    @Test
    public void testGetEdgesByLabelTwoEdgesDiffPairs() { // Check if lack of sorting causes bug
        g.addNode("n1");
        g.addNode("n2");
        g.addNode("n3");
        g.addNode("n4");
        g.addEdge("e1", "n1", "n2");
        g.addEdge("e1", "n3", "n4");

        List<Graph.Edge> edgesByLabel = g.getEdgesByLabel("e1");

        assertEquals(2, edgesByLabel.size());

        assertEquals(new Graph.Edge("e1", "n1", "n2"), edgesByLabel.get(0));
        assertEquals(new Graph.Edge("e1", "n3", "n4"), edgesByLabel.get(1));
    }

    @Test (expected = NullPointerException.class)
    public void testGetIncomingEdgesWithNullNode() {
        g.getIncomingEdges(null);
    }

    @Test
    public void testGetIncomingEdgesWithOneEdge() {
        g.addNode("n1");
        g.addNode("n2");
        g.addEdge("e1", "n1", "n2");

        List<Graph.Edge> incomingEdges = g.getIncomingEdges("n2");

        assertEquals(1, incomingEdges.size());
        assertEquals(new Graph.Edge("e1", "n1", "n2"), incomingEdges.get(0));
    }

    @Test
    public void testGetIncomingEdgesWithTwoEdges() {
        g.addNode("n1");
        g.addNode("n2");
        g.addNode("n3");
        g.addEdge("e1", "n1", "n3");
        g.addEdge("e2", "n2", "n3");

        List<Graph.Edge> incomingEdges = g.getIncomingEdges("n3");

        assertEquals(2, incomingEdges.size());

        Graph.Edge e1 = new Graph.Edge("e1", "n1", "n3");
        Graph.Edge e2 = new Graph.Edge("e2", "n2", "n3");

        if (incomingEdges.get(0).getSrc().equals("n1")) {
            assertEquals(e1, incomingEdges.get(0));
            assertEquals(e2, incomingEdges.get(1));
        } else {
            assertEquals(e1, incomingEdges.get(1));
            assertEquals(e2, incomingEdges.get(0));
        }
    }

    @Test (expected = NullPointerException.class)
    public void testGetOutgoingEdgesWithNullNode() {
        g.getOutgoingEdges(null);
    }

    @Test
    public void testGetOutgoingEdgesWithOneEdge() {
        g.addNode("n1");
        g.addNode("n2");
        g.addEdge("e1", "n1", "n2");

        List<Graph.Edge> outgoingEdges = g.getOutgoingEdges("n1");

        assertEquals(1, outgoingEdges.size());
        assertEquals(new Graph.Edge("e1", "n1", "n2"), outgoingEdges.get(0));
    }

    @Test
    public void testGetOutgoingEdgesWithTwoEdges() {
        g.addNode("n1");
        g.addNode("n2");
        g.addNode("n3");
        g.addEdge("e1", "n1", "n2");
        g.addEdge("e2", "n1", "n3");

        List<Graph.Edge> outgoingEdges = g.getOutgoingEdges("n1");

        Graph.Edge e1 = new Graph.Edge("e1", "n1", "n2");
        Graph.Edge e2 = new Graph.Edge("e2", "n1", "n3");

        assertEquals(2, outgoingEdges.size());

        if (outgoingEdges.get(0).getDst().equals("n2")) {
            assertEquals(e1, outgoingEdges.get(0));
            assertEquals(e2, outgoingEdges.get(1));
        } else {
            assertEquals(e1, outgoingEdges.get(1));
            assertEquals(e2, outgoingEdges.get(0));
        }
    }

    @Test (expected = NullPointerException.class)
    public void testContainsNodeWithNullNode() {
        g.containsNode(null);
    }

    @Test
    public void testContainsNodeWithExistingNode() {
        g.addNode("n1");

        assertTrue(g.containsNode("n1"));
    }

    @Test
    public void testContainsNodeWithNonexistentNode() {
        g.addNode("n1");

        assertFalse(g.containsNode("n2"));
    }

    @Test (expected = NullPointerException.class)
    public void testContainsEdgeWithNullEdge() {
        g.containsEdge(null, null, null);
    }

    @Test
    public void testContainsEdgeWithExistingEdge() {
        g.addNode("n1");
        g.addNode("n2");
        g.addEdge("e1", "n1", "n2");

        assertTrue(g.containsEdge("e1", "n1", "n2"));
    }

    @Test
    public void testContainsEdgeWithNonexistentEdge() {
        g.addNode("n1");
        g.addNode("n2");
        g.addEdge("e1", "n1", "n2");

        boolean b = g.containsEdge("e2", "n1", "n2");

        assertFalse(b);
    }

    @Test (expected = NullPointerException.class)
    public void testEdgeConstructorWithNull() {
        new Graph.Edge(null, null, null);
    }

    @Test
    public void testEdgeGetLabel() {
        Graph.Edge edge = new Graph.Edge("e1", "n1", "n2");
        assertEquals("e1", edge.getLabel());
    }

    @Test
    public void testEdgeGetSrc() {
        Graph.Edge edge = new Graph.Edge("e1", "n1", "n2");
        assertEquals("n1", edge.getSrc());
    }

    @Test
    public void testEdgeGetDst() {
        Graph.Edge edge = new Graph.Edge("e1", "n1", "n2");
        assertEquals("n2", edge.getDst());
    }

    @Test
    public void testEdgeHashCodeWithEqualEdges() {
        Graph.Edge edge1 = new Graph.Edge("e1", "n1", "n2");
        Graph.Edge edge2 = new Graph.Edge("e1", "n1", "n2");

        assertEquals(edge1.hashCode(), edge2.hashCode());
    }

    @Test
    public void testEdgeEqualsWithEqualEdges() {
        Graph.Edge edge1 = new Graph.Edge("e1", "n1", "n2");
        Graph.Edge edge2 = new Graph.Edge("e1", "n1", "n2");

        assertEquals(edge1, edge2);
    }

    @Test
    public void testEdgeEqualsWithUnequalEdges() {
        Graph.Edge edge1 = new Graph.Edge("e1", "n1", "n2");
        Graph.Edge edge2 = new Graph.Edge("e2", "n3", "n4");

        assertNotEquals(edge1, edge2);
    }
}
