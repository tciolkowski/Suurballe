package eiti.gis.suurballe.algorithm;

import eiti.gis.suurballe.graph.Edge;
import eiti.gis.suurballe.graph.Vertex;

import java.util.*;

public class Path {

    private Deque<Edge> edges;

    public Path() {
        edges = new ArrayDeque<>();
    }

    public Path(Edge... edges) {
        this();
        Collections.addAll(this.edges, edges);
    }

    public Path(Collection<Edge> edges) { // TODO: validation
        this.edges = new ArrayDeque<>(edges);
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Path path = (Path) o;
        return equals(edges, path.edges);
    }

    private boolean equals(Deque<Edge> d1, Deque<Edge> d2) { // because ArrayDeque does not implement equals
        if(d1 instanceof ArrayDeque) {
            if(d1.size() != d2.size())
                return false;
            Iterator<Edge> i1 = d1.iterator();
            Iterator<Edge> i2 = d2.iterator();
            while (i1.hasNext()) {
                if(!i1.next().equals(i2.next()))
                    return false;
            }
            return true;
        }
        return d1.equals(d2);
    }

    @Override
    public int hashCode() {
        return edges.hashCode();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        edges.forEach(e -> {
            sb.append(e.getSource()).append("--");
            sb.append(e.getWeight()).append("-->");
            if(edges.peekLast().equals(e)) {
                sb.append(e.getTarget());
            }
        });
        return sb.toString();
    }
}
