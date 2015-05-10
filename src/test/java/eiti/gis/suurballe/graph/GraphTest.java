package eiti.gis.suurballe.graph;

import org.junit.Before;
import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;

public class GraphTest {

    Graph graph;

    @Before
    public void setUp() {
        graph = new Graph();
    }

    @Test
    public void shouldAddVertex() {
        graph.addVertex(new Vertex(1));

        assertThat(graph.getVertices()).contains(new Vertex(1));
    }

    @Test
    public void shouldAddEdge() {
        graph.addVertex(new Vertex(1));
        graph.addVertex(new Vertex(2));

        graph.addEdge(new Vertex(1), new Vertex(2), 10);

        assertThat(graph.getNeighbours(new Vertex(1))).contains(new Vertex(2));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowWhenVertexToAddEdgeToDoesNotExist() {
        graph.addVertex(new Vertex(1));

        graph.addEdge(new Vertex(1), new Vertex(2), 10);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowWhenEdgeIsLoop() {
        graph.addVertex(new Vertex(1));
        graph.addVertex(new Vertex(2));

        graph.addEdge(new Vertex(1), new Vertex(1), 10);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowWhenEdgeHasNegativeWeight() {
        graph.addVertex(new Vertex(1));
        graph.addVertex(new Vertex(2));

        graph.addEdge(new Vertex(1), new Vertex(2), -10);
    }

    @Test
    public void shouldGetAllEdges() {
        graph.addVertex(new Vertex(1));
        graph.addVertex(new Vertex(2));
        graph.addVertex(new Vertex(3));

        graph.addEdge(new Vertex(1), new Vertex(2), 10);
        graph.addEdge(new Vertex(3), new Vertex(2), 5);

        assertThat(graph.getEdges()).containsOnly(new Edge(new Vertex(1), new Vertex(2), 10),
                                                  new Edge(new Vertex(3), new Vertex(2), 5));
    }

    @Test
    public void shouldGetSpecificEdge() {
        Vertex v1 = new Vertex(1);
        Vertex v2 = new Vertex(2);
        Vertex v3 = new Vertex(3);

        graph.addVertex(v1);
        graph.addVertex(v2);
        graph.addVertex(v3);

        final double weight = 1;
        graph.addEdge(v1, v2, weight);
        graph.addEdge(v2, v3, 2);

        Edge edge = graph.getEdge(v1, v2);
        assertThat(edge.getSource().equals(v1));
        assertThat(edge.getTarget().equals(v2));
        assertThat(edge.getWeight() == weight);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowWhenGettingNonExistentEdge() {
        Vertex v1 = new Vertex(1);
        Vertex v2 = new Vertex(2);

        graph.addVertex(v1);
        graph.addVertex(v2);

        final double weight = 1;
        graph.addEdge(v1, v2, weight);

        graph.getEdge(v1, new Vertex(3));
    }

    @Test
    public void shouldGetCorrectNumberOfEdges() {
        Vertex v1 = new Vertex(1);
        Vertex v2 = new Vertex(2);
        Vertex v3 = new Vertex(3);

        graph.addVertex(v1);
        graph.addVertex(v2);
        graph.addVertex(v3);

        final double weight = 1;
        graph.addEdge(v1, v2, weight);
        graph.addEdge(v2, v3, weight);
        graph.addEdge(v3, v1, weight);

        assertThat(graph.getNumberOfEdges() == 3);
    }
}