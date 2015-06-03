package eiti.gis.suurballe.io;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import eiti.gis.suurballe.graph.Graph;
import eiti.gis.suurballe.graph.Vertex;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import static java.lang.System.currentTimeMillis;
import static java.nio.file.Files.readAllBytes;
import static java.nio.file.Paths.get;

public class GraphLoader {

    public static class LoadingResult {

        public static LoadingResult successful(Graph g, long hintFrom, long hintTo) {
            return new LoadingResult(g, hintFrom, hintTo);
        }

        public static LoadingResult failed() {
            return new LoadingResult();
        }

        private LoadingResult(Graph g, long hintFrom, long hintTo) {
            graph = g;
            this.hintFrom = hintFrom;
            this.hintTo = hintTo;
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

    public LoadingResult loadGraph(URL url) throws URISyntaxException {
        return loadGraph(Paths.get(url.toURI()).toString());
    }

    public LoadingResult loadGraph(String filePath) {
        long start = currentTimeMillis();
        System.out.println("Loading graph from file: " + filePath);
        try {
            String fileContent = new String(readAllBytes(get(filePath)));
            JsonGraph jsonGraph = new Gson().fromJson(fileContent, JsonGraph.class);
            Graph graph = buildGraph(jsonGraph);
            System.out.println("Graph: " +
                            "vertices: " + jsonGraph.vertices.size() + " edges: " + jsonGraph.edges.size() +
                            " from: " + jsonGraph.pathHint.from + " to: " + jsonGraph.pathHint.to
            );
            System.out.println("Loaded in: " + (currentTimeMillis() - start) + " [ms]");
            JsonGraph.PathHint pathHint = jsonGraph.pathHint;
            return LoadingResult.successful(graph, pathHint.from, pathHint.to);
        } catch (JsonSyntaxException | IOException e) {
            System.err.println(e.toString());
            return LoadingResult.failed();
        }
    }

    private Graph buildGraph(JsonGraph jsonGraph) { // TODO: validation?
        final int numberOfVertices = jsonGraph.vertices.size();
        Map<Long, Vertex> vertices = new HashMap<>(numberOfVertices);
        Graph graph = new Graph();

        jsonGraph.vertices.forEach(id -> {
            Vertex vertex = new Vertex(id);
            vertices.put(id, vertex);
            graph.addVertex(vertex);
        });
        jsonGraph.edges.forEach(e -> {
            Vertex from = vertices.get(e.from);
            Vertex to = vertices.get(e.to);
            graph.addEdge(from, to, e.weight);
        });
        return graph;
    }
}