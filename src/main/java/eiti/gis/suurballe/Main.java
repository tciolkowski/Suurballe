package eiti.gis.suurballe;

public class Main {

    public static void main(String[] args) {
        new Main().invoke(args);
    }

    private static final String INVALID_ARGS_COUNT_ERROR = "Invalid number of arguments!";
    private static final String DEFAULT_PATH_OPTIONS = "fs";

    private String filePath;

    private String pathOptions = DEFAULT_PATH_OPTIONS;
    private long fromVerticeId;
    private long toVerticeId;
    private Suurballe suurballe = new Suurballe();

    private long numberOfVertices;
    private GraphLoader graphLoader = new GraphLoader();

    public void invoke(String[] args) {
        if(args.length < 2) {
            invalidInput(INVALID_ARGS_COUNT_ERROR);
        } else {
            try {
                interpretArguments(args);
            } catch (IllegalArgumentException e) {
                invalidInput(e.getMessage());
            }
        }
    }

    public void interpretArguments(String[] args) {
        filePath = args[1];
        switch (args[0]) {
            case "find":
                interpretFindCommand(args);
                runSuurballeAlgorithm();
                break;
            case "gen":
                interpretGenCommand(args);
                generateGraph();
                break;
            default:
                throw new IllegalArgumentException("Unknown command: " + args[0]);
        }
    }

    private void interpretFindCommand(String[] args) {
        if(args.length >= 2 && args.length <= 5) {
            if(args.length == 3 || args.length == 5) {
                interpretPathOptions(args);
            }
            if(args.length == 4 || args.length == 5) {
                interpretVerticeIds(args);
            }
        }
        else throw new IllegalArgumentException(INVALID_ARGS_COUNT_ERROR);
    }

    private void interpretVerticeIds(String[] args) {
        try {
            fromVerticeId = Long.valueOf(args[2]);
            toVerticeId = Long.valueOf(args[3]);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid vertices ids: " + args[2] + " " + args[3]);
        }
    }

    private String interpretPathOptions(String[] args) {
        String lastArg = args[args.length - 1];
        String regex = "(s*f*)*";   // TODO
        if(!lastArg.matches(regex))
            throw new IllegalArgumentException("Unknown path options: " + lastArg.replaceAll(regex, ""));
        return lastArg;
    }

    private void interpretGenCommand(String[] args) {
        if(args.length == 3) {
            try {
                numberOfVertices = Long.valueOf(args[2]);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Invalid number of vertices: " + args[2]);
            }
        }
        else throw new IllegalArgumentException(INVALID_ARGS_COUNT_ERROR);
    }

    private void runSuurballeAlgorithm() {
        GraphLoader.LoadingResult result = graphLoader.loadGraph(filePath);
        if(result.hasBeenSuccessful()) {
            long from = (fromVerticeId > 0) ? fromVerticeId : result.getPathHintFrom();
            long to = (toVerticeId > 0) ? toVerticeId : result.getPathHintTo();
            suurballe.findPaths(result.getGraph(), from, to);
            // TODO: get resultPaths, print them -> pathOptions
        }
    }

    private void generateGraph() {
        // TODO: String outputFileName, long numberOfVertices
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
        System.out.println("    path options [fs]: f - file, s - screen");
        System.out.println("or: java -jar suurballe gen filename [number of vertices]");
        System.out.println("    (to generate graph file)");
    }
}
