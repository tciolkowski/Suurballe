package eiti.gis.suurballe;

import eiti.gis.suurballe.graph.Graph;

public class Suurballe {

    void findVertexDisjointPaths(Graph graph, long from, long to) {
        Dijkstra dijkstra = new Dijkstra();
        // TODO
        Path path1 = dijkstra.findShortestPath(graph, from, to);
    }
}
