/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fish.client;

import fish.common.RejectedException;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

/**
 * {@code Client} class represents a peer in a FISH.
 * 
 * @author Igor
 */
public class Client {
    /*CLI*/
    private final static String USAGE_SHORT = "java fish.client.Client";
    private final static String USAGE = "java fish.client.Client [-path <shared_file_path>] [-host <server_address>] [-port <server_port>]";
    private final static PrintStream out = System.out;
    private final static BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
    /*Networking*/
    /**
     * Default server host
     */
    public final static String DEFAULT_HOST = "localhost";
    /**
     * Default server port
     */
    public final static int DEFAULT_PORT = 8080;//Server.DEFAULT_PORT;
    /**
     * Default peer port
     */
    public final static int DEFAULT_PEER_PORT = 8081;
    private final static int LINGER = 100;
    private Socket socketToServer;
    private Socket inSocketToPeer;
    private ServerSocket outSocketToPeer;
    /*Sharing*/
    private Map<Integer, File> sharedFiles;
    private boolean sharing;

    /**
     * Creates a FISH client.
     *
     * @param host FISH server host
     * @param port FISH server port
     * @param sharedFilePath Path to the shared folder
     */
    public Client(String host, int port, String sharedFilePath) {
        sharedFiles = new ConcurrentHashMap<Integer, File>();
        sharing = false;

        /*Setting shared files*/
        if (sharedFilePath != null && !sharedFilePath.isEmpty()) {
            addSharedFiles(sharedFilePath, false);
        }

        /*Connecting to the server*/
        connectToServer(host, port);

        /*Sending list of shared files to server*/
        share();

        /*Starting handling requests from other peers*/
        startPeerRequestHandling();
    }

    private void addSharedFiles(String sharedFilePath, boolean recursive) {
        long t1 = System.currentTimeMillis();
        File shared = new File(sharedFilePath);
        modifySharedFiles(shared, recursive, false);
        long t2 = System.currentTimeMillis();
        float t = (t2 - t1) / 1000.0f;

        out.println("INFO: Added " + sharedFiles.size() + " local files for sharing in " + t + " seconds.");
    }

    private void removeSharedFiles(String sharedFilePath, boolean recursive) {
        long t1 = System.currentTimeMillis();
        File shared = new File(sharedFilePath);
        modifySharedFiles(shared, recursive, true);
        long t2 = System.currentTimeMillis();
        float t = (t2 - t1) / 1000.0f;

        out.println("INFO: Removed " + sharedFiles.size() + " local files for sharing in " + t + " seconds.");
    }

    private void modifySharedFiles(File shared, boolean recursive, boolean removal) {
        if (shared.isDirectory()) {
            File[] files = shared.listFiles();
            for (File f : files) {
                if (recursive) {
                    modifySharedFiles(f, recursive, removal);
                } else {
                    if (f.isFile()) {
                        if (!removal) {
                            sharedFiles.put(shared.hashCode(), shared);
                        } else {
                            sharedFiles.remove(shared.hashCode());
                        }
                    }
                }
            }
        } else {
            if (!removal) {
                sharedFiles.put(shared.hashCode(), shared);
            } else {
                sharedFiles.remove(shared.hashCode());
            }
        }
    }

    private void connectToServer(String host, int port) {
        try {
            socketToServer = new Socket(host, port);
            socketToServer.setSoLinger(true, LINGER);
        } catch (UnknownHostException ex) {
            out.println("ERROR: Host not found");
            System.exit(2);
        } catch (IOException ex) {
            out.println("ERROR: " + ex.getMessage());
            System.exit(2);
        }
        out.println("INFO: Connected to the server.");
    }

    private void share() {
        if (!sharing) {
            sharing = true;
        } else {
        }
    }

    private void unshare() {
        sharing = false;
    }

    private void startPeerRequestHandling() {
        Runnable requestHandlingTask = new Runnable() {
            @Override
            public void run() {
                try {
                    outSocketToPeer = new ServerSocket(DEFAULT_PEER_PORT);
                    while (true) {
                        Socket clientSocket = outSocketToPeer.accept();
                        new PeerRequestHandler(clientSocket, sharedFiles).start();
                    }
                } catch (IOException ex) {
                    out.println("ERROR: " + ex.getMessage());
                    System.exit(3);
                }
            }
        };
        new Thread(requestHandlingTask, "Peer-Request-Handler").start();
        out.println("INFO: Started handling requests from peers.");
    }

    private void printMyFiles() {
        int size = sharedFiles.size();
        String[][] table = new String[size + 1][4];
        fillRow(table[0], "File", "Size", "Date", "Path");
        int i = 1;
        for (File f : sharedFiles.values()) {
            fillRow(table[i],
                    f.getName(),
                    Long.toString(f.length()),
                    new Date(f.lastModified()).toString(),
                    f.getPath());
            ++i;
        }

        int[] max = calcMaxLengthInColums(table);
        int lineLength = 3 * max.length + 1;
        for (int l : max) {
            lineLength += l;
        }

        Arrays.sort(table, 1, size + 1, new Comparator<String[]>() {
            @Override
            public int compare(String[] o1, String[] o2) {
                return o1[3].compareToIgnoreCase(o2[3]);
            }
        });

        String format = "| %-" + max[0] + "s | %-" + max[1] + "s | %-" + max[2] + "s | %-" + max[3] + "s |\n";
        char[] lineArr = new char[lineLength];
        Arrays.fill(lineArr, '-');
        String line = new String(lineArr);

        out.printf("%s\n", line);
        out.printf(format, table[0][0], table[0][1], table[0][2], table[0][3]);
        out.printf("%s\n", line);
        for (int j = 1; j < size + 1; j++) {
            out.printf(format, table[j][0], table[j][1], table[j][2], table[j][3]);
        }
        out.printf("%s\n\n", line);
    }

    private void fillRow(String[] row, String... data) {
        int n = row.length;
        System.arraycopy(data, 0, row, 0, n);
    }

    private int[] calcMaxLengthInColums(String[][] table) {
        int columns = table[0].length;
        int[] max = new int[columns];
        for (String[] row : table) {
            for (int i = 0; i < columns; i++) {
                if (row[i].length() > max[i]) {
                    max[i] = row[i].length();
                }
            }
        }
        return max;
    }

    public void run() {
        String inputString = null;
        try {
            InetAddress clientIp = InetAddress.getLocalHost();
            inputString = clientIp.getHostName() + "@" + clientIp.getHostAddress() + ">";
        } catch (UnknownHostException ex) {
            out.println("ERROR: Failed to get this host IP.");
            System.exit(4);
        }

        while (true) {
            out.print(inputString);
            try {
                String userInput = in.readLine();
                execute(parse(userInput));
            } catch (RejectedException ex) {
                throw new UnsupportedOperationException("TODO");
            } catch (IOException ex) {
                out.println("ERROR: " + ex.getMessage());
            }
        }
    }

    private Command parse(String userInput) {
        if (userInput == null) {
            return null;
        }

        StringTokenizer tokenizer = new StringTokenizer(userInput);
        if (tokenizer.countTokens() == 0) {
            return null;
        }

        CommandName commandName = null;
        List<String> args = new ArrayList<String>();

        try {
            String commandNameString = tokenizer.nextToken().toUpperCase().trim();
            commandName = CommandName.valueOf(commandNameString);
        } catch (IllegalArgumentException ex) {
            System.out.println("ERROR: Illegal command.");
            return null;
        }
        while (tokenizer.hasMoreTokens()) {
            args.add(tokenizer.nextToken());
        }

        return new Command(commandName, args.toArray(new String[]{}));
    }

    private void execute(Command command) throws RejectedException {
        if (command == null) {
            return;
        }

        switch (command.getCommandName()) {
            case HELP:
                for (CommandName commandName : CommandName.values()) {
                    out.println(commandName + "\n\t" + commandName.getDescription());
                    for (String param : commandName.getParamDescriptions()) {
                        out.println("\t " + param);
                    }
                }
                out.println();
                return;
            case SHARE:
                share();
                return;
            case UNSHARE:
                unshare();
                return;
            case LIST:
                //TODO 
                return;
            case DOWNLOAD:
                try {
                    int fileId = Integer.parseInt(command.getArgs()[0]);
                    String filepath = command.getArgs()[1];
                } catch (ArrayIndexOutOfBoundsException ex) {
                    out.println("ERROR: Not enough parameters.");
                    return;
                } catch (NumberFormatException ex) {
                    out.println("EROR: Wrong fileID.");
                    return;
                }
                //TODO
                return;
            case EXIT:
                unshare();
                System.exit(0);
            case MYFILES:
                printMyFiles();
                return;
            case LASTLIST:
                //TODO
                return;
            case FIND:
                //TODO
                return;
            case ADDFILES:
                //TODO
                return;
            case REMOVEFILES:
                //TODO
                return;
        }
    }

    /**
     * Main method.
     * 
     * @param args Command-line arguments provided by user 
     */
    public static void main(String[] args) {
        // create the command line parser
        CommandLineParser parser = new GnuParser();

        // create the Options
        Options options = new Options();
        Option pathOption = OptionBuilder.withArgName("filepath").
                hasArg().
                withDescription("add files for sharing under given filepath").
                withLongOpt("path").
                create('P');
        Option hostOption = OptionBuilder.withArgName("address").
                hasArg().
                withDescription("set the IP address of FISH server").
                withLongOpt("host").
                create('h');
        Option portOption = OptionBuilder.withArgName("port").
                hasArg().
                withDescription("set port number of FISH server").
                withLongOpt("port").
                create('p');
        options.addOption(pathOption);
        options.addOption(hostOption);
        options.addOption(portOption);

        // automatically generate the help statement
        HelpFormatter formatter = new HelpFormatter();

        try {
            String sharedFilePath;
            String host;
            int port;

            // parse the command line arguments
            CommandLine line = parser.parse(options, args);
            sharedFilePath = line.getOptionValue("P", null);
            host = line.getOptionValue("h", DEFAULT_HOST);
            port = Integer.parseInt(line.getOptionValue("p", Integer.toString(DEFAULT_PORT)));

            new Client(host, port, sharedFilePath).run();
        } catch (ParseException ex) {
            formatter.printHelp(USAGE_SHORT, options);
            System.exit(1);
        } catch (NumberFormatException ex) {
            out.println("ERROR: Wrong port number.");
            System.exit(1);
        }
    }

    private enum CommandName {
        /*Begin commands*/
        HELP("Print out the list of commands"),
        SHARE("Start sharing files (if already started, update list of shared files)"),
        UNSHARE("Stop sharing files"),
        ADDFILES("Add files for sharing under given path",
        new String[]{"<path> - path to files", "<recursive> - true/false"}),
        REMOVEFILES("Remove files for sharing under given path",
        new String[]{"<path> - path to files", "<recursive> - true/false"}),
        LIST("Show list of all shared files from server"),
        DOWNLOAD("Download a file with given FileID",
        new String[]{"<fileID> - id of the file from the last retrieved shared file list", "<filepath> - path to file to save"}),
        EXIT("Stop sharing and close client"),
        MYFILES("Show list of my files under shared file path"),
        LASTLIST("Show last retrieved shared file list"),
        FIND("Get list of shared files by given name",
        new String[]{"<name> - name mask"});
        /*End commands*/
        private String description;
        private String[] paramDescriptions;

        private CommandName(String description) {
            this.description = description;
            this.paramDescriptions = new String[]{};
        }

        private CommandName(String description, String[] paramDescriptions) {
            this.description = description;
            this.paramDescriptions = paramDescriptions;
        }

        public String getDescription() {
            return description;
        }

        public String[] getParamDescriptions() {
            return paramDescriptions;
        }
    };

    private class Command {
        private CommandName commandName;
        private String[] args;

        private CommandName getCommandName() {
            return commandName;
        }

        public String[] getArgs() {
            return args;
        }

        private Command(CommandName commandName, String[] args) {
            this.commandName = commandName;
            this.args = args;
        }
    }
}
