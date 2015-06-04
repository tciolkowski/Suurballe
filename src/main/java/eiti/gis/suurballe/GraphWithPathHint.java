package eiti.gis.suurballe;

import eiti.gis.suurballe.graph.Graph;

public class GraphWithPathHint {
    private Graph graph;
    private long pathHintFrom;
    private long pathHintTo;

    public Graph getGraph() {
        return graph;
    }

    public long getPathHintFrom() {
        return pathHintFrom;
    }

    public long getPathHintTo() {
        return pathHintTo;
    }

    public GraphWithPathHint(Graph graph, long pathHintFrom, long pathHintTo) {
        this.graph = graph;
        this.pathHintFrom = pathHintFrom;
        this.pathHintTo = pathHintTo;
    }
}
