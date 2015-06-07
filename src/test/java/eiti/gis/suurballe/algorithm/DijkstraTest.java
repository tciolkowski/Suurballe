package eiti.gis.suurballe.algorithm;

import eiti.gis.suurballe.graph.Graph;
import eiti.gis.suurballe.graph.Vertex;
import org.junit.Before;
import org.junit.Test;

import static eiti.gis.suurballe.Utils.edge;
import static org.fest.assertions.api.Assertions.assertThat;

public class DijkstraTest {

    Dijkstra dijkstra;

    @Before
    public void setUp() {
        dijkstra = new Dijkstra();
    }

    @Test
    public void shouldFindShortestPath() {
        Graph graph = buildGraph();

        Path path = dijkstra.findShortestPath(graph, 1, 4);

        assertThat(path.getEdges()).containsExactly(edge(1, 3, 1), edge(3, 2, 1), edge(2, 4, 1));
    }

    @Test
    public void shouldFindShortestDistanceToEachVertex() {
        Graph graph = buildGraph();

        dijkstra.findShortestPath(graph, 1, 4);

        assertThat(dijkstra.getDistanceMap().get(new Vertex(1))).isEqualTo(0);
        assertThat(dijkstra.getDistanceMap().get(new Vertex(2))).isEqualTo(2);
        assertThat(dijkstra.getDistanceMap().get(new Vertex(3))).isEqualTo(1);
        assertThat(dijkstra.getDistanceMap().get(new Vertex(4))).isEqualTo(3);
    }

    private Graph buildGraph() {
        Graph graph = new Graph();
        graph.addVertices(1, 2, 3, 4);
        graph.addEdge(1, 2, 10);
        graph.addEdge(1, 3, 1);
        graph.addEdge(3, 2, 1);
        graph.addEdge(2, 4, 1);
        graph.addEdge(3, 4, 10);
        return graph;
    }
}
