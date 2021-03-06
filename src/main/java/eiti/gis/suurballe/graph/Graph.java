package eiti.gis.suurballe.graph;

import java.util.*;

import static java.util.Collections.unmodifiableMap;
import static java.util.Collections.unmodifiableSet;
import static java.util.stream.Collectors.toList;

public class Graph {

    protected final Map<Vertex, Map<Vertex, Double>> vertices;

    public Graph() {
        vertices = new HashMap<>();
    }

    private Graph(Graph g) {
        this();
        for (Map.Entry<Vertex, Map<Vertex, Double>> entry : g.vertices.entrySet()) {
            this.vertices.put(entry.getKey(), new HashMap<>(entry.getValue()));
        }
    }

    public void addVertex(long id) {
        addVertex(new Vertex(id));
    }

    public void addVertex(Vertex v) {
        vertices.put(v, new HashMap<>());
    }

    public void removeVertex(Vertex v) {
        removeEdgesDirectedTo(v);
        vertices.remove(v);
    }

    private void removeEdgesDirectedTo(Vertex v) {
        for (Edge edge : getEdgesDirectedTo(v)) {
            removeEdge(edge.getSource(), edge.getTarget());
        }
    }

    public void addVertices(Vertex... vertices) {
        for (Vertex v : vertices)
            addVertex(v);
    }

    public void addVertices(long... ids) {
        for (long id : ids)
            addVertex(id);
    }

    public void addEdge(long from, long to, double weight) {
        addEdge(new Vertex(from), new Vertex(to), weight);
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

    public long getNumberOfVertices() {
        return vertices.size();
    }

    public long getNumberOfEdges() {
        long edges = 0;
        for (Map<Vertex, Double> neighbours : vertices.values()) {
            edges += neighbours.size();
        }
        return edges;
    }

    public Collection<Vertex> getVertices() {
        return unmodifiableSet(vertices.keySet());
    }

    public Iterable<Vertex> getNeighbours(Vertex v) {
        return unmodifiableSet(vertices.get(v).keySet());
    }

    public Map<Vertex, Double> getNeighboursWithDistances(Vertex v) {
        return unmodifiableMap(vertices.get(v));
    }

    public Iterable<Edge> getEdgesFrom(Vertex v) {
        return vertices.get(v).entrySet().stream()
                .map(e -> new Edge(v, e.getKey(), e.getValue()))
                .collect(toList());
    }

    /**
     * This method is rather expensive O(e), where e is number of edges in graph
     */
    public Iterable<Edge> getEdgesDirectedTo(Vertex v) {
        List<Edge> edges = new ArrayList<>();
        for (Map.Entry<Vertex, Map<Vertex, Double>> entry : vertices.entrySet()) {
            Map<Vertex, Double> value = entry.getValue();
            for (Map.Entry<Vertex, Double> e : value.entrySet()) {
                if (e.getKey().equals(v)) {
                    edges.add(new Edge(entry.getKey(), e.getKey(), e.getValue()));
                }
            }
        }
        return edges;
    }

    public Collection<Edge> getEdges() {
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

    public void removeEdgesFrom(Vertex v) {
        vertices.get(v).clear();
    }

    public void reverseEdge(Vertex from, Vertex to) {
        Map<Vertex, Double> f = vertices.get(from);
        Double weight = f.remove(to);
        if (weight == null)
            throw new IllegalArgumentException("No edge from " + from + " to " + to);
        Map<Vertex, Double> t = vertices.get(to);
        t.put(from, weight);
    }

    public boolean containsEdge(Vertex from, Vertex to) {
        Map<Vertex, Double> m = vertices.get(from);
        return m != null && m.containsKey(to);
    }

    public Edge getEdge(long from, long to) {
        return getEdge(new Vertex(from), new Vertex(to));
    }

    public Edge getEdge(Vertex from, Vertex to) {
        IllegalArgumentException exception
                = new IllegalArgumentException("No edge from " + from.getId() + " to " + to.getId());
        if(!vertices.containsKey(from))
            throw exception;
        Map<Vertex, Double> neighbours = vertices.get(from);
        if(!neighbours.containsKey(to))
            throw exception;
        Double weight = neighbours.get(to);
        return new Edge(from, to, weight);
    }

    public void removeEdge(Vertex from, Vertex to) {
        vertices.get(from).remove(to);
    }

    public static Graph copyOf(Graph g) {
        return new Graph(g);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        return vertices.equals(((Graph) o).vertices);
    }

    @Override
    public String toString() {
        return "Graph{" +
                "vertices=" + vertices +
                '}';
    }
}
