package eiti.gis.suurballe;

import eiti.gis.suurballe.graph.Graph;
import eiti.gis.suurballe.graph.Vertex;

import java.util.Random;

public class GraphGenerator {

    private static final double MIN_EDGE_WEIGHT = 0.01;
    private static final double MAX_EDGE_WEIGHT = 100;

    public Graph generateGraph(long numberOfVertices, double density) {
        parameterCheck(numberOfVertices, density);
        Graph graph = new Graph();
        for (long i = 1; i <= numberOfVertices; i++)
            graph.addVertex(new Vertex(i));
        addEdges(density, graph);
//        System.out.println("Generated graph with " + graph.getNumberOfVertices() + " vertices and " +
//                graph.getNumberOfEdges() + " edges");
        return graph;
    }

    private void addEdges(double density, Graph graph) {
        Random random = new Random();
        Iterable<Vertex> vertices = graph.getVertices();
        vertices.forEach(from -> vertices.forEach(to -> {
            if (!from.equals(to) && density > random.nextDouble()) {
                double weight = random.nextDouble() * (MAX_EDGE_WEIGHT - MIN_EDGE_WEIGHT) + MIN_EDGE_WEIGHT;
                graph.addEdge(from, to, weight);
            }
        }));
    }

    private void parameterCheck(long numberOfVertices, double density) {
        if (numberOfVertices < 1)
            throw new IllegalArgumentException("Number of vertices must be positive!");
        if (density < 0.0 || density > 1.0)
            throw new IllegalArgumentException("Density must be between 0.0 and 1.0!");
    }
}
