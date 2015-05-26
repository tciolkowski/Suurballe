package eiti.gis.suurballe;

import eiti.gis.suurballe.graph.Edge;
import eiti.gis.suurballe.graph.Graph;
import eiti.gis.suurballe.graph.Vertex;

import java.util.ArrayList;

public class Suurballe {

    public void findVertexDisjointPaths(Graph graph, long from, long to) {
        prepareForVertexDisjointVersion(graph);

        // TODO
        Dijkstra dijkstra = new Dijkstra();
        Path path1 = dijkstra.findShortestPath(graph, from, to);
    }

    /**
     * Modifies the graph by splitting each vertex into two vertices: v and v2 (where v2.id == -v.id)
     * joined by an edge (v, v2) of length 0. Vertex v keeps all incoming edges from original vertex, and
     * v2 takes all outgoing edges.
     */
    private void prepareForVertexDisjointVersion(Graph graph) {
        Iterable<Vertex> vertices = new ArrayList<>(graph.getVertices());
        vertices.forEach(v -> {
            Iterable<Edge> edges = graph.getEdgesFrom(v);
            graph.removeEdgesFrom(v);

            Vertex v2 = new Vertex(-v.getId());
            graph.addVertex(v2);
            graph.addEdge(v, v2, 0);

            edges.forEach(e -> graph.addEdge(v2, e.getTarget(), e.getWeight()));
        });
    }
}
