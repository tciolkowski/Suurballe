package eiti.gis.suurballe;

import eiti.gis.suurballe.graph.Vertex;

import java.util.ArrayDeque;
import java.util.Deque;

public class Path {

    private Deque<Vertex> vertices = new ArrayDeque<>();

    public void prepend(Vertex vertex) {
        vertices.addFirst(vertex);
    }

    @Override
    public String toString() {
        return "TODO";
    }
}
