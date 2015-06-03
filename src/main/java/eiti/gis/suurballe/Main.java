package eiti.gis.suurballe;

import eiti.gis.suurballe.graph.Graph;
import eiti.gis.suurballe.ui.GraphApplet;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        new Main().invoke(args);
    }

    private static final String INVALID_ARGS_COUNT_MESSAGE = "Invalid number of arguments!";

    private static final String DEFAULT_PATH_OPTIONS = "fp";

    private String pathOptions = DEFAULT_PATH_OPTIONS;
    private long fromVerticeId;
    private long toVerticeId;

    private GraphLoader graphLoader = new GraphLoader();

    public void invoke(String[] args) {
        if(args.length < 2) {
            invalidInput(INVALID_ARGS_COUNT_MESSAGE);
        } else {
            try {
                interpretArguments(args);
            } catch (IllegalArgumentException e) {
                invalidInput(e.getMessage());
            }
        }
    }

    public void interpretArguments(String[] args) {
        String filePath = args[1];
        switch (args[0]) {
            case "find":
                interpretFindCommand(args);
                runSuurballeAlgorithm(filePath);
                break;
            case "gen":
                generateGraph(filePath, getNumberOfVertices(args));
                break;
            default:
                throw new IllegalArgumentException("Unknown command: " + args[0]);
        }
    }

    private void interpretFindCommand(String[] args) {
        if(args.length >= 2 && args.length <= 5) {
            if(args.length == 3 || args.length == 5) {
                pathOptions = interpretPathOptions(args);
            }
            if(args.length == 4 || args.length == 5) {
                interpretVerticeIds(args);
            }
        }
        else throw new IllegalArgumentException(INVALID_ARGS_COUNT_MESSAGE);
    }

    private void interpretVerticeIds(String[] args) {
        try {
            fromVerticeId = Long.parseLong(args[2]);
            toVerticeId = Long.parseLong(args[3]);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid vertices ids: " + args[2] + " " + args[3]);
        }
    }

    private String interpretPathOptions(String[] args) {
        String lastArg = args[args.length - 1];
        String regex = "(p*f*v*)*";   // TODO
        if(!lastArg.matches(regex))
            throw new IllegalArgumentException("Unknown path options: " + lastArg.replaceAll(regex, ""));
        return lastArg;
    }

    private long getNumberOfVertices(String[] args) {
        if(args.length == 3) {
            try {
                return Long.parseLong(args[2]);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Invalid number of vertices: " + args[2]);
            }
        }
        else throw new IllegalArgumentException(INVALID_ARGS_COUNT_MESSAGE);
    }

    private void runSuurballeAlgorithm(String filePath) {
        GraphLoader.LoadingResult result = graphLoader.loadGraph(filePath);
        if(result.hasBeenSuccessful()) {
            Graph initialGraph = Graph.copyOf(result.getGraph());

            long from = (fromVerticeId > 0) ? fromVerticeId : result.getPathHintFrom();
            long to = (toVerticeId > 0) ? toVerticeId : result.getPathHintTo();
            Suurballe suurballe = new Suurballe();

            List<Path> paths;
            try {
                paths = suurballe.findVertexDisjointPaths(result.getGraph(), from, to);
                if(pathOptions.contains("f")) printPathsToFile(paths, filePath.replace(".json", "-path.txt"));
                if(pathOptions.contains("s")) paths.forEach(System.out::println);
            } catch(PathNotFoundException e) {
                System.out.println("Graph does not contain two vertex-disjoint paths from vertex " + from + " to vertex" + to);
            }
            GraphApplet.visualizeGraph(initialGraph);
        }
    }

    private void printPathsToFile(List<Path> paths, String filePath) {
        // TODO
    }

    private void generateGraph(String filePath, long numberOfVertices) {
        GraphGenerator generator = new GraphGenerator();
        Graph graph = generator.generateGraph(numberOfVertices, 5 / numberOfVertices);
        GraphWriter writer = new GraphWriter();
        writer.writeToFile(graph, filePath);
    }

    private void invalidInput(String errorMessage) {
        System.err.println(errorMessage + "\n");
        printUsage();
    }

    private void printUsage() {
        System.out.println("Usage: java -jar suurballe find filename [from] [to] [path options]");
        System.out.println("    (to find paths)");
        System.out.println("    where: ");
        System.out.println("    from - id of origin vertex, to - id of destination vertex");
        System.out.println("    path options [fpv]: f - file, p - print, v - visualize");
        System.out.println("or: java -jar suurballe gen filename [number of vertices]");
        System.out.println("    (to generate graph file)");
    }
}
