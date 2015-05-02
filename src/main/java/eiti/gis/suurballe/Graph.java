package eiti.gis.suurballe;

public class Graph {

    // TODO

    public static class Vertex {
        private long id;

        public Vertex(long id) {
            this.id = id;
        }
    }

    public void addVertex(Vertex v) {
        // TODO
    }

    public void addEdge(Vertex from, Vertex to, double weight) {
        // TODO: check for loops, negative weights -> throw IllegalArgumentException
    }
}
