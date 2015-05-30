package eiti.gis.suurballe;

public class DefaultDijkstraFactory implements DijkstraFactory {

    @Override
    public Dijkstra get() {
        return new Dijkstra();
    }
}
