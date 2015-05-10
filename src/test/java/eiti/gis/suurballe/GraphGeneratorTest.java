package eiti.gis.suurballe;

import eiti.gis.suurballe.graph.Graph;
import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;

public class GraphGeneratorTest {

    GraphGenerator generator = new GraphGenerator();

    @Test
    public void shouldGenerateValidGraph() {
        long smallNumber = 10;
        long bigNumber = 200;
        Graph small = generator.generateGraph(smallNumber, 0.8);
        Graph big = generator.generateGraph(bigNumber, 0.2);

        assertThat(small.getNumberOfVertices() == smallNumber);
        assertThat(big.getNumberOfVertices() == bigNumber);
    }
}
