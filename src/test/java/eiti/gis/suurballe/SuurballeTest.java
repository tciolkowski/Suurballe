package eiti.gis.suurballe;

import eiti.gis.suurballe.graph.Edge;
import eiti.gis.suurballe.graph.Vertex;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.Before;
import org.junit.Test;

import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;

import static org.fest.assertions.api.Assertions.assertThat;

public class SuurballeTest {

    GraphLoader graphLoader;
    Suurballe suurballe;

    @Before
    public void setUp() {
        graphLoader = new GraphLoader();
        suurballe = new Suurballe();
    }

    @Test
    public void shouldFindTwoVertexDisjointPathsInDiamondGraph() throws URISyntaxException {
        String path = getPath(getClass().getResource("/diamond.json"));
        GraphLoader.LoadingResult result = graphLoader.loadGraph(path);
        assertThat(result.hasBeenSuccessful()).isTrue();

        long from = result.getPathHintFrom();
        long to = result.getPathHintTo();
        Pair<Path, Path> paths = suurballe.findVertexDisjointPaths(result.getGraph(), from, to);

        Path p1 = new Path(edge(1, 2, 10.0), edge(2, 4, 1.0));
        Path p2 = new Path(edge(1, 3, 1.0), edge(3, 4, 10.0));

        assertThat(areEqual(Pair.of(p1, p2), paths));
    }

    private boolean areEqual(Pair<Path, Path> p1, Pair<Path, Path> p2) {
        return p1.equals(p2) || (p1.getLeft().equals(p2.getRight()) && p1.getRight().equals(p2.getLeft()));
    }

    private Edge edge(long fromId, long toId, double weight) {
        return new Edge(new Vertex(fromId), new Vertex(toId), weight);
    }

    private String getPath(URL url) throws URISyntaxException {
        return Paths.get(url.toURI()).toString();
    }
}
