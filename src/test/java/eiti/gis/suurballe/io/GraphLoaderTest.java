package eiti.gis.suurballe.io;

import eiti.gis.suurballe.graph.Edge;
import eiti.gis.suurballe.graph.Graph;
import eiti.gis.suurballe.graph.Vertex;
import eiti.gis.suurballe.io.GraphLoader;
import org.junit.Test;

import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;

import static org.fest.assertions.api.Assertions.assertThat;

public class GraphLoaderTest {

    @Test
    public void shouldLoadGraphFromJsonFile() throws Exception {
        GraphLoader graphLoader = new GraphLoader();

        String path = getPath(getClass().getResource("/graph.json"));
        GraphLoader.LoadingResult result = graphLoader.loadGraph(path);
        assertThat(result.hasBeenSuccessful()).isTrue();

        Graph graph = result.getGraph();

        assertThat(graph.getVertices()).containsOnly(new Vertex(1), new Vertex(2), new Vertex(3));

        Edge[] expectedEdges = { edge(1, 2, 3.14), edge(2, 3, 1.2) };
        assertThat(graph.getEdges()).containsOnly(expectedEdges);

        assertThat(result.getPathHintFrom()).isEqualTo(1);
        assertThat(result.getPathHintTo()).isEqualTo(3);
    }

    private Edge edge(long fromId, long toId, double weight) {
        return new Edge(new Vertex(fromId), new Vertex(toId), weight);
    }

    private String getPath(URL url) throws URISyntaxException {
        return Paths.get(url.toURI()).toString();
    }
}