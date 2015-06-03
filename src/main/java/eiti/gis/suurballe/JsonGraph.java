package eiti.gis.suurballe;

import java.util.List;

public class JsonGraph {

    static class PathHint {
        long from;
        long to;
    }
    static class Edge {
        long from;
        long to;
        double weight;

        public Edge() { }

        public Edge(long from, long to, double weight) {
            this.from = from;
            this.to = to;
            this.weight = weight;
        }
    }

    PathHint pathHint;
    List<Long> vertices;
    List<Edge> edges;
}
