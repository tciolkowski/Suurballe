package eiti.gis.suurballe.algorithm;

import eiti.gis.suurballe.graph.Edge;
import eiti.gis.suurballe.graph.Graph;
import eiti.gis.suurballe.graph.Vertex;
import org.junit.Test;

import java.util.*;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SuurballeTest {

    DijkstraFactory dijkstraFactory = mock(DijkstraFactory.class);

    Suurballe suurballe = new Suurballe(dijkstraFactory);

    @Test
    public void shouldPrepareForVertexDisjointVersion() {
        Graph graph = new Graph();
        graph.addVertices(1, 2, 3, 4);
        graph.addEdge(1, 2, 10);
        graph.addEdge(1, 3, 1);
        graph.addEdge(3, 2, 1);
        graph.addEdge(2, 4, 1);
        graph.addEdge(3, 4, 10);

        suurballe.prepareForVertexDisjointVersion(graph);

        assertThat(graph.getVertices()).hasSize(8);
        assertThat(graph.getEdges()).containsOnly(
                edge(1, -1, 0), edge(2, -2, 0), edge(3, -3, 0), edge(4, -4, 0),
                edge(-1, 2, 10), edge(-1, 3, 1), edge(-3, 2, 1), edge(-2, 4, 1), edge(-3, 4, 10));
    }

    @Test
    public void shouldModifyWeightOfEachEdge() {
        Graph graph = new Graph();
        graph.addVertices(1, -1, 2, -2, 3, -3, 4, -4);
        graph.addEdge(1, -1, 0);
        graph.addEdge(2, -2, 0);
        graph.addEdge(3, -3, 0);
        graph.addEdge(4, -4, 0);

        graph.addEdge(-1, 3, 1);
        graph.addEdge(-1, 2, 10);
        graph.addEdge(-3, 2, 1);
        graph.addEdge(-2, 4, 1);
        graph.addEdge(-3, 4, 10);

        Map<Vertex, Double> distanceMap = new HashMap<>();
        distanceMap.put(new Vertex(1), 0.0);
        distanceMap.put(new Vertex(-1), 0.0);
        distanceMap.put(new Vertex(2), 2.0);
        distanceMap.put(new Vertex(-2), 2.0);
        distanceMap.put(new Vertex(3), 1.0);
        distanceMap.put(new Vertex(-3), 1.0);
        distanceMap.put(new Vertex(4), 3.0);
        distanceMap.put(new Vertex(-4), 3.0);
        suurballe.modifyWeightOfEachEdge(graph, distanceMap);

        assertThat(graph.getEdges()).containsOnly(
                edge(1, -1, 0), edge(2, -2, 0), edge(3, -3, 0), edge(4, -4, 0),
                edge(-1, 2, 8), edge(-1, 3, 0), edge(-3, 2, 0), edge(-2, 4, 0), edge(-3, 4, 8));
    }

    @Test
    public void shouldRemoveEdgesDirectedIntoSource() {
        Graph graph = new Graph();
        graph.addVertices(1, -1, 2, -2, 3, -3, 4, -4);
        graph.addEdge(1, -1, 0);
        graph.addEdge(2, -2, 0);
        graph.addEdge(3, -3, 0);
        graph.addEdge(4, -4, 0);
        graph.addEdge(-1, 3, 0);
        graph.addEdge(-3, 2, 0);
        graph.addEdge(-2, 4, 0);
        graph.addEdge(-1, 2, 8);
        graph.addEdge(-3, 4, 8);

        Vertex source = new Vertex(-1);
        suurballe.removeEdgesDirectedIntoSource(graph, source);

        assertThat(graph.getEdgesDirectedTo(source)).isEmpty();
        assertThat(graph.getEdges()).containsOnly(edge(2, -2, 0), edge(3, -3, 0), edge(4, -4, 0),
                edge(-1, 2, 8), edge(-1, 3, 0), edge(-3, 2, 0), edge(-2, 4, 0), edge(-3, 4, 8));
    }

    @Test
    public void shouldReverseZeroLengthEdgesInPath() {
        Graph graph = new Graph();
        graph.addVertices(1, -1, 2, -2, 3, -3, 4, -4);
        graph.addEdge(2, -2, 0);
        graph.addEdge(3, -3, 0);
        graph.addEdge(4, -4, 0);
        graph.addEdge(-1, 3, 0);
        graph.addEdge(-3, 2, 0);
        graph.addEdge(-2, 4, 0);
        graph.addEdge(-1, 2, 8);
        graph.addEdge(-3, 4, 8);

        Path path = new Path(edge(-1, 3, 0), edge(3, -3, 0), edge(-3, 2, 0),
                edge(2, -2, 0), edge(-2, 4, 0), edge(4, -4, 0));
        suurballe.reverseZeroLengthEdgesInPath(graph, path);

        assertThat(graph.getEdges()).containsOnly(edge(-2, 2, 0), edge(-3, 3, 0), edge(-4, 4, 0),
                edge(-1, 2, 8), edge(3, -1, 0), edge(2, -3, 0), edge(4, -2, 0), edge(-3, 4, 8));
    }

    @Test
    public void shouldUntwinePaths() {
        List<Edge> edges1 = new ArrayList<>();
        Collections.addAll(edges1, edge(-1, 3, 1), edge(3, -3, 0), edge(-3, 2, 0), edge(2, -2, 0), edge(-2, 4, 1));
        List<Edge> edges2 = new ArrayList<>();
        Collections.addAll(edges2, edge(-1, 2, 1), edge(2, -3, 0), edge(-3, 4, 8));

        List<Edge> e1 = suurballe.untwinePaths(edges1, edges2);
        List<Edge> e2 = suurballe.untwinePaths(edges2, edges1);

        assertThat(e1).containsExactly(edge(-1, 3, 1), edge(3, -3, 0), edge(-3, 4, 8));
        assertThat(e2).containsExactly(edge(-1, 2, 1), edge(2, -2, 0), edge(-2, 4, 1));
    }

    @Test
    public void shouldMergeTwinVertices() {
        List<Edge> edges = new ArrayList<>();
        Collections.addAll(edges, edge(-1, 3, 1), edge(3, -3, 0), edge(-3, 4, 8));

        Collection<Edge> merged = suurballe.mergeVertices(edges);

        assertThat(merged).containsExactly(edge(1, 3, 1), edge(3, 4, 8));
    }

    @Test
    public void shouldFindTwoVertexDisjoint() {
        Graph graph = new Graph();
        graph.addVertices(1, 2, 3, 4);
        graph.addEdge(1, 2, 10);
        graph.addEdge(1, 3, 1);
        graph.addEdge(3, 2, 1);
        graph.addEdge(2, 4, 1);
        graph.addEdge(3, 4, 10);

        Path path1 = new Path(edge(1, -1, 0), edge(-1, 3, 1), edge(3, -3, 0), edge(-3, 2, 1),
                edge(2, -2, 0), edge(-2, 4, 1));
        Path path2 = new Path(edge(-1, 2, 8), edge(2, -3, 0), edge(-3, 4, 8));

        Map<Vertex, Double> distanceMap = new HashMap<>();
        distanceMap.put(new Vertex(1), 0.0);
        distanceMap.put(new Vertex(-1), 0.0);
        distanceMap.put(new Vertex(2), 2.0);
        distanceMap.put(new Vertex(-2), 2.0);
        distanceMap.put(new Vertex(3), 1.0);
        distanceMap.put(new Vertex(-3), 1.0);
        distanceMap.put(new Vertex(4), 3.0);
        distanceMap.put(new Vertex(-4), 3.0);
        Dijkstra d1 = mock(Dijkstra.class);
        when(d1.findShortestPath(graph, 1, 4)).thenReturn(path1);
        when(d1.getDistanceMap()).thenReturn(distanceMap);
        Dijkstra d2 = mock(Dijkstra.class);
        when(d2.findShortestPath(graph, -1, 4)).thenReturn(path2);
        when(dijkstraFactory.get()).thenReturn(d1, d2);

        List<Path> paths = suurballe.findVertexDisjointPaths(graph, 1, 4);

        Path p1 = new Path(edge(1, 2, 10.0), edge(2, 4, 1.0));
        Path p2 = new Path(edge(1, 3, 1.0), edge(3, 4, 10.0));

        assertThat(paths).containsOnly(p1, p2);
    }

    private static Edge edge(long fromId, long toId, double weight) {
        return new Edge(new Vertex(fromId), new Vertex(toId), weight);
    }
}
