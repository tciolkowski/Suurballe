package eiti.gis.suurballe;

import eiti.gis.suurballe.graph.Edge;
import eiti.gis.suurballe.graph.Graph;
import eiti.gis.suurballe.graph.Vertex;

import java.util.*;

import static java.util.Collections.unmodifiableMap;

public class Dijkstra {

    private Map<Vertex, Double> verticesWithDistances = new HashMap<>();
    private NavigableSet<Vertex> unvisitedVertices = new TreeSet<>(new Comparator<Vertex>() {
        @Override
        public int compare(Vertex v1, Vertex v2) {
            int result = Double.compare(verticesWithDistances.get(v1), verticesWithDistances.get(v2));
            return (result == 0) ? 1 : result;
        }
    });
    private Vertex source;
    private Vertex destination;

    public Map<Vertex, Double> getDistanceMap() {
        return unmodifiableMap(verticesWithDistances);
    }

    public Path findShortestPath(Graph graph, long from, long to) {
        initialize(graph, from, to);
        checkIfSourceAndDestinationFound(from, to);
        Map<Vertex, Vertex> predecessors = new HashMap<>();
        runDijkstra(graph, predecessors);
        checkIfPathFound();
        return buildPath(graph, predecessors);
    }

    private void initialize(Graph graph, long from, long to) {
        Iterable<Vertex> vertices = graph.getVertices();
        vertices.forEach(v -> {
            if (v.getId() == from) {
                source = v;
                verticesWithDistances.put(v, 0.0);
            } else {
                verticesWithDistances.put(v, Double.POSITIVE_INFINITY);
                if (v.getId() == to)
                    destination = v;
            }
            unvisitedVertices.add(v);
        });
    }

    private void checkIfSourceAndDestinationFound(long from, long to) {
        if (source == null || destination == null)
            throw new IllegalArgumentException(
                    "Graph does not contain vertices with ids: " + from + " and/or " + to);
    }

    private void checkIfPathFound() {
        if (verticesWithDistances.get(destination).equals(Double.POSITIVE_INFINITY))
            throw new PathNotFoundException();
    }

    private void runDijkstra(Graph graph, Map<Vertex, Vertex> predecessors) {
        while (!unvisitedVertices.isEmpty()) {
            Vertex vertex = unvisitedVertices.pollFirst();
            Map<Vertex, Double> neighboursWithDistances = graph.getNeighboursWithDistances(vertex);
            for (Map.Entry<Vertex, Double> entry : neighboursWithDistances.entrySet()) {
                Vertex neighbour = entry.getKey();
                Double current = verticesWithDistances.get(neighbour);
                Double alt = verticesWithDistances.get(vertex) + entry.getValue();
                if (alt < current) {
                    updateDijkstraDistance(neighbour, alt);
                    predecessors.put(neighbour, vertex);
                }
            }
        }
    }

    private Path buildPath(Graph graph, Map<Vertex, Vertex> predecessors) {
        Path path = new Path();
        Vertex from = destination;
        Vertex to = destination;
        while (!from.equals(source)) {
            from = predecessors.get(to);
            Edge e = graph.getEdge(from, to);
            path.prepend(e);
            to = from;
        }
        return path;
    }

    private void updateDijkstraDistance(Vertex vertex, Double newDistance) {
        verticesWithDistances.put(vertex, newDistance);
        unvisitedVertices.remove(vertex);
        unvisitedVertices.add(vertex);
    }
}