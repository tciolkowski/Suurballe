package eiti.gis.suurballe.graph;

import org.junit.Test;

import static org.fest.assertions.api.Assertions.assertThat;

public class EdgeTest {

    @Test
    public void shouldReturnReversedEdge() {
        Edge edge = new Edge(new Vertex(1), new Vertex(2), 5.0);

        Edge reversed = edge.reversed();

        assertThat(reversed.getSource()).isEqualTo(new Vertex(2));
        assertThat(reversed.getTarget()).isEqualTo(new Vertex(1));
        assertThat(reversed.getWeight()).isEqualTo(5);
    }
}