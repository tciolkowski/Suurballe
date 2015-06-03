package eiti.gis.suurballe;

import com.google.gson.Gson;
import eiti.gis.suurballe.graph.Graph;
import eiti.gis.suurballe.graph.Vertex;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import static java.util.stream.Collectors.toList;

public class GraphWriter {

    public void writeToFile(Graph graph, String filename) {
        writeToFile(toJsonString(graph), filename);
    }

    public String toJsonString(Graph graph) {
        JsonGraph jg = new JsonGraph();
        jg.vertices = graph.getVertices().stream().map(Vertex::getId).collect(toList());
        jg.edges = graph.getEdges().stream()
                .map(e -> new JsonGraph.Edge(e.getSource().getId(), e.getTarget().getId(), e.getWeight()))
                .collect(toList());
        return new Gson().toJson(jg);
    }

    public void writeToFile(String jsonString, String filename) {
        try(OutputStreamWriter pw = new OutputStreamWriter(new FileOutputStream(filename))) {
            pw.write(jsonString);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
