package eiti.gis.suurballe.ui;

import eiti.gis.suurballe.graph.Graph;
import org.jgraph.JGraph;
import org.jgrapht.ListenableGraph;
import org.jgrapht.ext.JGraphModelAdapter;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.ListenableDirectedGraph;

import javax.swing.*;
import java.awt.*;

import static javax.swing.JOptionPane.INFORMATION_MESSAGE;
import static javax.swing.JOptionPane.showMessageDialog;

public class GraphApplet extends JApplet {

    private static final Color DEFAULT_BACKGROUND_COLOR = Color.decode("#FAFBFF");
    private Graph graph;

    public GraphApplet(Graph graph) throws HeadlessException {
        super();
        this.graph = graph;
    }

    public static void visualizeGraph(Graph graph) {
        GraphApplet ga = new GraphApplet(graph);
        ga.init();
        ga.start();
        ga.setPreferredSize(new Dimension(640, 480));
        showMessageDialog(null, ga, "Suurballe", INFORMATION_MESSAGE);
    }

    public void init() {
        ListenableGraph<Integer, DefaultEdge> listenableGraph
                = new ListenableDirectedGraph<>(EdgeWithVisibleWeight.class);
        JGraphModelAdapter<Integer, DefaultEdge> adapter = new JGraphModelAdapter<>(listenableGraph);

        JGraph jgraph = new JGraph(adapter);
        jgraph.setBackground(DEFAULT_BACKGROUND_COLOR);
        getContentPane().add(jgraph);

        graph.getVertices().forEach(v -> listenableGraph.addVertex((int) v.getId()));
        graph.getEdges().forEach(e -> listenableGraph.addEdge((int) e.getSource().getId(),
                (int) e.getTarget().getId(), new EdgeWithVisibleWeight(e.getWeight())));
    }
}
