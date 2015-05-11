package eiti.gis.suurballe;

import eiti.gis.suurballe.graph.Edge;
import eiti.gis.suurballe.graph.Vertex;
import org.junit.Before;
import org.junit.Test;

import static org.fest.assertions.api.Assertions.assertThat;

public class PathTest {

    Path path;

    @Before
    public void setUp() {
        path = new Path();
    }

    @Test
    public void shouldAddFirstEdge() {
        Edge edge = new Edge(new Vertex(1), new Vertex(2), 1.0);

        path.prepend(edge);

        assertThat(path.getEdges()).containsExactly(edge);
    }

    @Test
    public void shouldPrependEdges() {
        Edge edge1 = new Edge(new Vertex(15), new Vertex(18), 1.0);
        Edge edge2 = new Edge(new Vertex(18), new Vertex(3), 2.0);
        Edge edge3 = new Edge(new Vertex(3), new Vertex(212), 3.0);

        path.prepend(edge3);
        path.prepend(edge2);
        path.prepend(edge1);

        assertThat(path.getEdges()).containsExactly(edge1, edge2, edge3);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowWhenInvalidEdgePrepended() {
        Edge invalid = new Edge(new Vertex(1), new Vertex(2), 1.0);
        Edge edge = new Edge(new Vertex(3), new Vertex(4), 1.0);
        path.prepend(edge);

        path.prepend(invalid);
    }
}
