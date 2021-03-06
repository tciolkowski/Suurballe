package eiti.gis.suurballe.graph;

import org.junit.Before;
import org.junit.Test;

import static org.fest.assertions.api.Assertions.assertThat;

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
        graph.addVertices(new Vertex(1), new Vertex(2));

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

        graph.addEdge(new Vertex(1), new Vertex(1), 10);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowWhenEdgeHasNegativeWeight() {
        graph.addVertices(new Vertex(1), new Vertex(2));

        graph.addEdge(new Vertex(1), new Vertex(2), -10);
    }

    @Test
    public void shouldGetAllEdges() {
        graph.addVertices(new Vertex(1), new Vertex(2), new Vertex(3));

        graph.addEdge(new Vertex(1), new Vertex(2), 10);
        graph.addEdge(new Vertex(3), new Vertex(2), 5);

        assertThat(graph.getEdges()).containsOnly(new Edge(new Vertex(1), new Vertex(2), 10),
                new Edge(new Vertex(3), new Vertex(2), 5));
    }

    @Test
    public void shouldReverseEdge() {
        graph.addVertices(new Vertex(1), new Vertex(2), new Vertex(3));
        graph.addEdge(new Vertex(1), new Vertex(2), 10);
        graph.addEdge(new Vertex(2), new Vertex(3), 5);

        graph.reverseEdge(new Vertex(1), new Vertex(2));

        assertThat(graph.getEdges()).containsOnly(new Edge(new Vertex(2), new Vertex(1), 10),
                                                  new Edge(new Vertex(2), new Vertex(3), 5));
        assertThat(graph.getNeighbours(new Vertex(2))).containsOnly(new Vertex(1), new Vertex(3));
        assertThat(graph.getNeighbours(new Vertex(1))).doesNotContain(new Vertex(2), new Vertex(3));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowWhenEdgeIsNotInGraph() {
        graph.addVertices(1, 2);

        graph.reverseEdge(new Vertex(1), new Vertex(2));
    }

    @Test
    public void shouldGetSpecificEdge() {
        Vertex v1 = new Vertex(1);
        Vertex v2 = new Vertex(2);
        Vertex v3 = new Vertex(3);

        graph.addVertices(v1, v2, v3);

        final double weight = 1;
        graph.addEdge(v1, v2, weight);
        graph.addEdge(v2, v3, 2);

        Edge edge = graph.getEdge(v1, v2);
        assertThat(edge.getSource()).isEqualTo(v1);
        assertThat(edge.getTarget()).isEqualTo(v2);
        assertThat(edge.getWeight()).isEqualTo(weight);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowWhenGettingNonExistentEdge() {
        Vertex v1 = new Vertex(1);
        Vertex v2 = new Vertex(2);

        graph.addVertices(v1, v2);

        final double weight = 1;
        graph.addEdge(v1, v2, weight);

        graph.getEdge(v1, new Vertex(3));
    }

    @Test
    public void shouldGetCorrectNumberOfEdges() {
        Vertex v1 = new Vertex(1);
        Vertex v2 = new Vertex(2);
        Vertex v3 = new Vertex(3);

        graph.addVertices(v1, v2, v3);

        final double weight = 1;
        graph.addEdge(v1, v2, weight);
        graph.addEdge(v2, v3, weight);
        graph.addEdge(v3, v1, weight);

        assertThat(graph.getNumberOfEdges()).isEqualTo(3);
    }

    @Test
    public void shouldGetEdgesDirectedToVertex() {
        Vertex v1 = new Vertex(1);
        Vertex v2 = new Vertex(2);
        Vertex v3 = new Vertex(3);

        graph.addVertices(v1, v2, v3);

        graph.addEdge(v1, v2, 1);
        graph.addEdge(v2, v3, 1);
        graph.addEdge(v3, v1, 1);

        assertThat(graph.getEdgesDirectedTo(v1)).containsOnly(new Edge(v3, v1, 1));
    }

    @Test
    public void shouldRemoveEdge() {
        Vertex v1 = new Vertex(1);
        Vertex v2 = new Vertex(2);
        Vertex v3 = new Vertex(3);

        graph.addVertices(v1, v2, v3);

        graph.addEdge(v1, v2, 1);
        graph.addEdge(v2, v3, 1);
        graph.addEdge(v3, v1, 1);

        graph.removeEdge(v3, v1);

        assertThat(graph.getEdges()).containsOnly(new Edge(v1, v2, 1), new Edge(v2, v3, 1));
    }

    @Test
    public void shouldReturnDeepCopyOfGraph() {
        Vertex v1 = new Vertex(1);
        Vertex v2 = new Vertex(2);
        Vertex v3 = new Vertex(3);

        graph.addVertices(v1, v2, v3);

        graph.addEdge(v1, v2, 1);
        graph.addEdge(v2, v3, 1);
        graph.addEdge(v3, v1, 1);

        Graph copy = Graph.copyOf(this.graph);

        assertThat(copy.getVertices()).isEqualTo(graph.getVertices());
        assertThat(copy.getEdges()).isEqualTo(graph.getEdges());
        assertThat(copy.vertices).isNotSameAs(graph.vertices);
    }
}