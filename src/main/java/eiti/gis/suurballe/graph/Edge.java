package eiti.gis.suurballe.graph;

public class Edge {

    private final Vertex source;

    private final Vertex target;

    private final double weight;

    public Edge(Vertex source, Vertex target, double weight) {
        this.source = source;
        this.target = target;
        this.weight = weight;
    }

    public Vertex getSource() {
        return source;
    }

    public Vertex getTarget() {
        return target;
    }

    public double getWeight() {
        return weight;
    }

    public Edge reversed() {
        return new Edge(target, source, weight);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Edge edge = (Edge) o;
        return Double.compare(edge.weight, weight) == 0 &&
                source.equals(edge.source) &&
                target.equals(edge.target);
    }

    @Override
    public int hashCode() {
        int result = 31 * source.hashCode() + target.hashCode();
        long temp = Double.doubleToLongBits(weight);
        return 31 * result + (int) (temp ^ (temp >>> 32));
    }

    @Override
    public String toString() {
        return source + " --" + weight + "--> " + target;
    }
}
