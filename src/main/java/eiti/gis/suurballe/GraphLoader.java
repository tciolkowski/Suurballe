package eiti.gis.suurballe;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class GraphLoader {

    public static class LoadingResult {

        public static LoadingResult successful(Graph g, long hintFrom, long hintTo) {
            return new LoadingResult(g, hintFrom, hintTo);
        }

        public static LoadingResult failed() {
            return new LoadingResult();
        }

        private LoadingResult(Graph g, long hintTo, long hintFrom) {
            graph = g;
            this.hintTo = hintTo;
            this.hintFrom = hintFrom;
        }

        private LoadingResult() {}

        public boolean hasBeenSuccessful() {
            return graph != null;
        }

        private Graph graph;
        private long hintFrom;
        private long hintTo;

        public Graph getGraph() {
            return graph;
        }

        public long getPathHintFrom() {
            return hintFrom;
        }

        public long getPathHintTo() {
            return hintTo;
        }
    }

    public LoadingResult loadGraph(String filePath) {
        long start = System.currentTimeMillis();
        System.out.println("Loading graph from file: " + filePath);

        String fileContent;
        JsonGraph jsonGraph;
        Graph graph;
        try {
            fileContent = new String(Files.readAllBytes(Paths.get(filePath)));
            jsonGraph = new Gson().fromJson(fileContent, JsonGraph.class);
            graph = buildGraph(jsonGraph);
        } catch (JsonSyntaxException | IOException e) {
            System.err.println(e.toString());
            return LoadingResult.failed();
        }
        System.out.println("Graph: " +
                "vertices: " + jsonGraph.vertices.size() + " edges: " + jsonGraph.edges.size() +
                " from: " + jsonGraph.pathHint.from + " to: " + jsonGraph.pathHint.to
        );
        System.out.println("Loaded in: " + (System.currentTimeMillis() - start) + " [ms]");
        JsonGraph.PathHint pathHint = jsonGraph.pathHint;
        return LoadingResult.successful(graph, pathHint.from, pathHint.to);
    }

    private Graph buildGraph(JsonGraph jsonGraph) { // TODO: validation?
        final int numberOfVertices = jsonGraph.vertices.size();
        Map<Long, Graph.Vertex> vertices = new HashMap<>(numberOfVertices);
        final Graph graph = new Graph();

        jsonGraph.vertices.forEach((Long id) -> {
            Graph.Vertex vertex = new Graph.Vertex(id);
            vertices.put(id, vertex);
            graph.addVertex(vertex);
        });
        jsonGraph.edges.forEach((JsonGraph.Edge e) -> {
            Graph.Vertex from = vertices.get(e.from);
            Graph.Vertex to = vertices.get(e.to);
            graph.addEdge(from, to, e.weight);
        });
        return graph;
    }
}