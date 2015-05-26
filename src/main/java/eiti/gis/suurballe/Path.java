package eiti.gis.suurballe;

import eiti.gis.suurballe.graph.Edge;
import eiti.gis.suurballe.graph.Vertex;

import java.util.ArrayDeque;
import java.util.Collections;
import java.util.Deque;
import java.util.Queue;

public class Path {

    private Deque<Edge> edges = new ArrayDeque<>();

    public Path() {}

    public Path(Edge... edges) {
        Collections.addAll(this.edges, edges);
    }

    public Queue<Edge> getEdges() {
        return edges;
    }

    public void prepend(Edge e) {
        if(edges.isEmpty() || getSourceVertex().equals(e.getTarget())) {
            edges.addFirst(e);
        } else {
            throw new IllegalArgumentException("Edge " + e + " cannot be added before edge " + edges.getFirst());
        }
    }

    public int getNumberOfEdges() {
        return edges.size();
    }

    private Vertex getSourceVertex() {
        return edges.getFirst().getSource();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Path ( ");
        edges.forEach(e -> {
            sb.append(e.getSource().toString()).append(" -- ");
            sb.append(e.getWeight()).append(" --> ");
            if(edges.peekLast().equals(e)) {
                sb.append(e.getTarget());
            }
        });
        sb.append(" )");
        return sb.toString();
    }
}
