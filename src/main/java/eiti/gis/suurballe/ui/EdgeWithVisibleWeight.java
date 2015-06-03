package eiti.gis.suurballe.ui;

import org.jgrapht.graph.DefaultWeightedEdge;

public class EdgeWithVisibleWeight extends DefaultWeightedEdge {

    private double weight;

    public EdgeWithVisibleWeight(double weight) {
        super();
        this.weight = weight;
    }

    @Override
    public String toString() {
        return Double.toString(getWeight());
    }

    @Override
    protected double getWeight() {
        return weight;
    }
}
