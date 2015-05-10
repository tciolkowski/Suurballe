package eiti.gis.suurballe;

import eiti.gis.suurballe.graph.Edge;
import eiti.gis.suurballe.graph.Vertex;
import org.junit.Test;

import java.util.Collection;
import java.util.Queue;

import static org.fest.assertions.Assertions.assertThat;

public class PathTest {

    @Test
    public void shouldAddFirstEdge() {
        Edge edge = new Edge(new Vertex(1), new Vertex(2), 1.0);
        Path path = new Path();

        path.prepend(edge);

        Collection<Edge> edges = path.getEdges();
        assertThat(edges.size() == 1);
        assertThat(edges.contains(edge));
    }

    @Test
    public void shouldPrependEdges() {
        Edge edge1 = new Edge(new Vertex(15), new Vertex(18), 1.0);
        Edge edge2 = new Edge(new Vertex(18), new Vertex(3), 2.0);
        Edge edge3 = new Edge(new Vertex(3), new Vertex(212), 3.0);
        Path path = new Path();

        path.prepend(edge3);
        path.prepend(edge2);
        path.prepend(edge1);

        Queue<Edge> edges = path.getEdges();
        assertThat(edges.size() == 3);
        assertThat(edges.remove().equals(edge1));
        assertThat(edges.remove().equals(edge2));
        assertThat(edges.remove().equals(edge3));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowWhenInvalidEdgePrepended() {
        Edge invalid = new Edge(new Vertex(1), new Vertex(2), 1.0);
        Edge edge = new Edge(new Vertex(3), new Vertex(4), 1.0);
        Path path = new Path();
        path.prepend(edge);

        path.prepend(invalid);
    }
}
