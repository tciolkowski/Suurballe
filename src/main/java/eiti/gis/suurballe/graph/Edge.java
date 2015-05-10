package eiti.gis.suurballe.graph;

public class Edge {

    private final Vertex from;

    private final Vertex to;
    private final double weight;

    public Edge(Vertex from, Vertex to, double weight) {
        this.from = from;
        this.to = to;
        this.weight = weight;
    }

    public Vertex getSource() {
        return from;
    }

    public Vertex getTarget() {
        return to;
    }

    public double getWeight() {
        return weight;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Edge edge = (Edge) o;
        return Double.compare(edge.weight, weight) == 0 &&
                from.equals(edge.from) &&
                to.equals(edge.to);
    }

    @Override
    public int hashCode() {
        int result = 31 * from.hashCode() + to.hashCode();
        long temp = Double.doubleToLongBits(weight);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return from + " -> " + to + " (" + weight + ")";
    }
}
