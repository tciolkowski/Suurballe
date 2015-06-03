package eiti.gis.suurballe;

import eiti.gis.suurballe.algorithm.Path;
import eiti.gis.suurballe.algorithm.PathNotFoundException;
import eiti.gis.suurballe.algorithm.Suurballe;
import eiti.gis.suurballe.graph.Edge;
import eiti.gis.suurballe.graph.Vertex;
import eiti.gis.suurballe.io.GraphLoader;
import org.junit.Test;

import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

import static org.fest.assertions.api.Assertions.assertThat;

public class SuurballeIntegrationTest {

    GraphLoader loader = new GraphLoader();
    Suurballe suurballe = new Suurballe();

    private class SuurballeGraphTest {
        String graphFile;
        Path p1, p2;

        public SuurballeGraphTest(String graphFile) {
            this.graphFile = graphFile;
        }

        public SuurballeGraphTest(String graphFile, Path p1, Path p2) {
            this.graphFile = graphFile;
            this.p1 = p1;
            this.p2 = p2;
        }
    }

    private void checkSuurballe(SuurballeGraphTest test) throws URISyntaxException {
        URL resource = getClass().getResource(test.graphFile);
        assertThat(resource).isNotNull();

        GraphLoader.LoadingResult result = loader.loadGraph(resource);
        assertThat(result.hasBeenSuccessful()).isTrue();

        List<Path> paths = suurballe.findVertexDisjointPaths(
                result.getGraph(), result.getPathHintFrom(), result.getPathHintTo());
        if(test.p1 != null && test.p2 != null)
            assertThat(paths).containsOnly(test.p1, test.p2);
    }

    @Test
    public void shouldFindTwoVertexDisjointPaths() throws URISyntaxException {
        Path p1 = new Path(edge(1, 2, 1.0), edge(2, 4, 2.0), edge(4, 6, 1.0));
        Path p2 = new Path(edge(1, 3, 1.0), edge(3, 5, 1.0), edge(5, 6, 3.0));
        checkSuurballe(new SuurballeGraphTest("/twoDisjointSimple.json", p1, p2));
    }

    @Test(expected = PathNotFoundException.class)
    public void shouldThrowPathNotFoundException() throws URISyntaxException {
        checkSuurballe(new SuurballeGraphTest("/singleJointVertex.json"));
    }

    private Edge edge(long fromId, long toId, double weight) {  // TODO: move to utils
        return new Edge(new Vertex(fromId), new Vertex(toId), weight);
    }
}
