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
    public void shouldReverseEdge() {
        graph.addVertex(new Vertex(1));
        graph.addVertex(new Vertex(2));
        graph.addVertex(new Vertex(3));
        graph.addEdge(new Vertex(1), new Vertex(2), 10);
        graph.addEdge(new Vertex(2), new Vertex(3), 5);

        graph.reverseEdge(new Vertex(1), new Vertex(2));

        assertThat(graph.getEdges()).containsOnly(new Edge(new Vertex(2), new Vertex(1), 10),
                                                  new Edge(new Vertex(2), new Vertex(3), 5));
        assertThat(graph.getNeighbours(new Vertex(2))).containsOnly(new Vertex(1), new Vertex(3));
        assertThat(graph.getNeighbours(new Vertex(1))).excludes(new Vertex(2), new Vertex(3));
    }

}