package eiti.gis.suurballe.graph;

import java.util.*;

import static java.util.Collections.unmodifiableSet;

public class Graph {

    private final Map<Vertex, Map<Vertex, Double>> vertices = new HashMap<>();

    public void addVertex(Vertex v) {
        vertices.put(v, new HashMap<>());
    }

    public void addEdge(Vertex from, Vertex to, double weight) {
        parameterCheck(from, to, weight);
        Map<Vertex, Double> n = vertices.get(from);
        n.put(to, weight);
    }

    private void parameterCheck(Vertex from, Vertex to, double weight) {
        if (weight < 0)
            throw new IllegalArgumentException("Edges with negative weight are not allowed");
        if (from.equals(to))
            throw new IllegalArgumentException("Loops are not allowed");
        if (!vertices.containsKey(from) || !vertices.containsKey(to))
            throw new IllegalArgumentException("Cannot add edge between " + from + " and " + to +
                    "; vertex doesn't belong to graph.");
    }

    public Iterable<Vertex> getVertices() {
        return unmodifiableSet(vertices.keySet());
    }

    public Iterable<Vertex> getNeighbours(Vertex v) {
        return unmodifiableSet(vertices.get(v).keySet());
    }

    public Iterable<Edge> getEdges() {
        Collection<Edge> edges = new ArrayList<>();
        for (Map.Entry<Vertex, Map<Vertex, Double>> entry : vertices.entrySet()) {
            edges.addAll(edgesFrom(entry));
        }
        return edges;
    }

    private Collection<Edge> edgesFrom(Map.Entry<Vertex, Map<Vertex, Double>> entry) {
        List<Edge> edges = new ArrayList<>();
        Vertex from = entry.getKey();
        for (Map.Entry<Vertex, Double> e : entry.getValue().entrySet()) {
            Vertex to = e.getKey();
            Double weight = e.getValue();
            edges.add(new Edge(from, to, weight));
        }
        return edges;
    }

    public void reverseEdge(Vertex from, Vertex to) {

    }
}
