package eiti.gis.suurballe;

import eiti.gis.suurballe.graph.Edge;
import eiti.gis.suurballe.graph.Graph;
import org.jgrapht.DirectedGraph;
import org.jgrapht.alg.DijkstraShortestPath;
import org.jgrapht.graph.DefaultDirectedWeightedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.*;

import static org.fest.assertions.api.Assertions.assertThat;

public class DijkstraTest {

    static List<Graph> graphs = new ArrayList<>();
    static List<DirectedGraph<Long, DefaultEdge>> libGraphs = new ArrayList<>();
    static GraphGenerator generator = new GraphGenerator();

    @BeforeClass
    public static void setUp() {
        addGraphs(3, 1.0);
        addGraphs(6, 1.0);
        addGraphs(10, 0.8);
        addGraphs(100, 0.4);    // about 40 edges from every vertex
        addGraphs(200, 0.2);
        addGraphs(1000, 0.04);
    }

    private static void addGraphs(long numberOfEdges, double density) {
        Graph graph = generator.generateGraph(numberOfEdges, density);
        graphs.add(graph);
        DefaultDirectedWeightedGraph<Long, DefaultEdge> libGraph
                = new DefaultDirectedWeightedGraph<>(DefaultEdge.class);
        graph.getVertices().forEach(v -> libGraph.addVertex(v.getId()));
        graph.getEdges().forEach(e -> {
            DefaultEdge libEdge = new DefaultWeightedEdge();
            libGraph.addEdge(e.getSource().getId(), e.getTarget().getId(), libEdge);
            libGraph.setEdgeWeight(libEdge, e.getWeight());
        });
        libGraphs.add(libGraph);
    }

    @Test(expected = PathNotFoundException.class)
    public void shouldThrowWhenPathNotFound() {
        Graph graph = generator.generateGraph(20, 0.0);
        long from = getRandomVerticeId(graph);
        long to = from;
        while(to == from) {
            to = getRandomVerticeId(graph);
        }
        new Dijkstra().findShortestPath(graph, from, to);
    }

    @Test
    public void shouldFindSamePaths() {
        for(int i=0; i<graphs.size(); i++) {
            Graph graph = graphs.get(i);
            DirectedGraph<Long, DefaultEdge> libGraph = libGraphs.get(i);

            Dijkstra dijkstra = new Dijkstra();
            long from = getRandomVerticeId(graph);
            long to = from;
            while(to == from) {
                to = getRandomVerticeId(graph);
            }
            Path path = dijkstra.findShortestPath(graph, from, to);
            List<DefaultEdge> libPath = DijkstraShortestPath.findPathBetween(libGraph, from, to);

            checkEquality(path, libPath, libGraph);
        }
    }

    private void checkEquality(Path path, List<DefaultEdge> libPath, DirectedGraph<Long, DefaultEdge> libGraph) {
        Queue<Edge> edges = path.getEdges();
        assertThat(edges).hasSize(libPath.size());

        Iterator<Edge> iter = edges.iterator();
        Iterator<DefaultEdge> libIter = libPath.iterator();
        while(iter.hasNext()) {
            Edge edge = iter.next();
            DefaultEdge libEdge = libIter.next();
            checkEquality(edge, libEdge, libGraph);
        }
    }

    private void checkEquality(Edge edge, DefaultEdge libEdge, DirectedGraph<Long, DefaultEdge> libGraph) {
        long src1 = edge.getSource().getId();
        Long src2 = libGraph.getEdgeSource(libEdge);
        assertThat(src2).isEqualTo(src1);

        long target1 = edge.getTarget().getId();
        Long target2 = libGraph.getEdgeTarget(libEdge);
        assertThat(target1).isEqualTo(target2);
    }

    private long getRandomVerticeId(Graph graph) {
        Random random = new Random();
        return random.nextInt((int) graph.getNumberOfVertices()) + 1;
    }
}
