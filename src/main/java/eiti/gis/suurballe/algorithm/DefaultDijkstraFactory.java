package eiti.gis.suurballe.algorithm;

public class DefaultDijkstraFactory implements DijkstraFactory {

    @Override
    public Dijkstra get() {
        return new Dijkstra();
    }
}
