package eiti.gis.suurballe;

import eiti.gis.suurballe.graph.Edge;
import eiti.gis.suurballe.graph.Graph;
import eiti.gis.suurballe.graph.Vertex;

import java.util.*;

import static java.util.stream.Collectors.toList;

public class Suurballe {

    public List<Path> findVertexDisjointPaths(Graph graph, long from, long to) {
        prepareForVertexDisjointVersion(graph);
        Dijkstra dijkstra = new Dijkstra();
        Path path1 = dijkstra.findShortestPath(graph, from, to);

        Vertex source = new Vertex(-from);
        Map<Vertex, Double> distanceMap = dijkstra.getDistanceMap();
        modifyWeightOfEachEdge(graph, distanceMap);
        removeEdgesDirectedIntoSource(graph, source);
        reverseZeroLengthEdgesInPath(graph, path1);
        dijkstra = new Dijkstra();
        Path path2 = dijkstra.findShortestPath(graph, -from, to);
        Collection<Edge> edges1 = path1.getEdges().stream()
                .map(e -> new Edge(e.getSource(), e.getTarget(), e.getWeight() - distanceMap.get(e.getTarget()) + distanceMap.get(e.getSource())))
                .collect(toList());
        Collection<Edge> edges2 = path2.getEdges();
        List<Edge> e1 = untwinePaths(new ArrayList<>(edges1), new ArrayList<>(edges2));
        List<Edge> e2 = untwinePaths(new ArrayList<>(edges2), new ArrayList<>(edges1));
        Collection<Edge> e11 = restoreWeights(mergeVertices(e1), distanceMap);
        Collection<Edge> e22 = restoreWeights(mergeVertices(e2), distanceMap);
        return Arrays.asList(new Path(e11), new Path(e22));
    }

    private Collection<Edge> restoreWeights(Collection<Edge> edges, Map<Vertex, Double> distanceMap) {
        return edges.stream()
                .map(e -> new Edge(e.getSource(), e.getTarget(), e.getWeight() + distanceMap.get(e.getTarget())
                                - distanceMap.get(e.getSource())
                        )
                )
                .collect(toList());
    }

    /**
     * Modifies the graph by splitting each vertex into two vertices: v and v2 (where v2.id == -v.id)
     * joined by an edge (v, v2) of length 0. Vertex v keeps all incoming edges from original vertex, and
     * v2 takes all outgoing edges.
     */
    protected void prepareForVertexDisjointVersion(Graph graph) {
        Iterable<Vertex> vertices = new ArrayList<>(graph.getVertices());
        vertices.forEach(v -> {
            Iterable<Edge> edges = graph.getEdgesFrom(v);
            graph.removeEdgesFrom(v);

            Vertex v2 = new Vertex(-v.getId());
            graph.addVertex(v2);
            graph.addEdge(v, v2, 0);

            edges.forEach(e -> graph.addEdge(v2, e.getTarget(), e.getWeight()));
        });
    }

    public void modifyWeightOfEachEdge(Graph graph, Map<Vertex, Double> distanceMap) {
        graph.getEdges().forEach(e -> {
            double newWeight = e.getWeight() - distanceMap.get(e.getTarget()) + distanceMap.get(e.getSource());
            graph.addEdge(e.getSource(), e.getTarget(), newWeight);
        });
    }

    public void removeEdgesDirectedIntoSource(Graph graph, Vertex source) {
        for (Edge e : graph.getEdgesDirectedTo(source)) {
            graph.removeEdge(e.getSource(), e.getTarget());
        }
    }

    public void reverseZeroLengthEdgesInPath(Graph graph, Path path) {
        path.getEdges().stream()
                .filter(e -> graph.containsEdge(e.getSource(), e.getTarget()))
                .filter(e -> graph.getEdge(e.getSource(), e.getTarget()).getWeight() == 0)
                .forEach(edge -> graph.reverseEdge(edge.getSource(), edge.getTarget()));
    }

    public List<Edge> untwinePaths(List<Edge> edges1, List<Edge> edges2) {
        List<Edge> edges = new ArrayList<>();
        ListIterator<Edge> iter = edges1.listIterator();
        List<Edge> other = edges2;
        while (iter.hasNext()) {
            Edge edge = iter.next();
            if (other.indexOf(edge.reversed()) == -1) {
                edges.add(edge);
            } else {
                other = (other == edges2) ? edges2 : edges1;
                iter = other.listIterator(other.indexOf(edge.reversed()) + 1);
            }
        }
        return edges;
    }

    public Collection<Edge> mergeVertices(Collection<Edge> edges) {
        Collection<Edge> merged = new ArrayList<>();
        for (Iterator<Edge> iterator = edges.iterator(); iterator.hasNext(); ) {
            Edge edge = iterator.next();
            long id = edge.getSource().getId();
            if (id != -edge.getTarget().getId()) {
                if(id < 0)
                    edge = new Edge(new Vertex(-id), edge.getTarget(), edge.getWeight());
                merged.add(edge);
            } else {
                Edge next = iterator.next();
                merged.add(new Edge(edge.getSource(), next.getTarget(), next.getWeight()));
            }
        }
        return merged;
    }
}
