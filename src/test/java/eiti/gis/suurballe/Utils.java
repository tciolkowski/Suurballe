package eiti.gis.suurballe;

import eiti.gis.suurballe.graph.Edge;
import eiti.gis.suurballe.graph.Vertex;

public class Utils {

    private Utils() {}

    public static Edge edge(long fromId, long toId, double weight) {
        return new Edge(new Vertex(fromId), new Vertex(toId), weight);
    }
}
