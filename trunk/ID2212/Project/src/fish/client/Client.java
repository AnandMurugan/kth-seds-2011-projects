/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fish.client;

import fish.common.FileInfo;
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
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
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
public final class Client {
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
    /*FISHing*/
    private Map<String, File> mySharedFiles;
    private boolean sharing;
    private List<FileInfo> foundSharedFiles;

    /**
     * Creates a FISH client.
     *
     * @param host FISH server host
     * @param port FISH server port
     * @param sharedFilePath Path to the shared folder
     */
    public Client(String host, int port, String sharedFilePath) {
        mySharedFiles = new HashMap<String, File>();
        sharing = false;

        /*Connecting to the server*/
        //connectToServer(host, port);

        /*Setting shared files*/
        if (sharedFilePath != null && !sharedFilePath.isEmpty()) {
            addSharedFiles(sharedFilePath, true);
        }

        /*Sending list of shared files to server*/
        share();

        /*Starting handling requests from other peers*/
        startPeerRequestHandling();
    }

    /**
     * Adds files under given path to the list of shared files. These changes are
     * local only. To notify FISH server about changes, {@link Client#share share} methods should be
     * called.
     * 
     * @param sharedFilePath Path to files
     * @param recursive If {@code true}, then file search would be recursive (looking
     * into subfolders)
     * 
     * @return The number of added files
     */
    public int addSharedFiles(String sharedFilePath, boolean recursive) {
        long t1 = System.currentTimeMillis();
        File shared = new File(sharedFilePath);
        int count = modifySharedFiles(shared, recursive, false, 0);
        long t2 = System.currentTimeMillis();
        float t = (t2 - t1) / 1000.0f;

        out.println("INFO: Added " + count + " local files for sharing in " + t + " seconds.\n");

        return count;
    }

    /**
     * Removes files under given path from the list of shared files. These changes are
     * local only. To notify FISH server about changes, {@link Client#share share} methods should be
     * called.
     * 
     * @param sharedFilePath Path to files
     * @param recursive If {@code true}, then file search would be recursive (looking
     * into subfolders)
     * 
     * @return The number of removed files
     */
    public int removeSharedFiles(String sharedFilePath, boolean recursive) {
        long t1 = System.currentTimeMillis();
        File shared = new File(sharedFilePath);
        int count = modifySharedFiles(shared, recursive, true, 0);
        long t2 = System.currentTimeMillis();
        float t = (t2 - t1) / 1000.0f;

        out.println("INFO: Removed " + count + " local files for sharing in " + t + " seconds.\n");

        return count;
    }

    private int modifySharedFiles(File shared, boolean recursive, boolean removal, int count) {
        if (shared.isDirectory()) {
            File[] files = shared.listFiles();
            if (files == null) {
                return count;
            }
            for (File f : files) {
                if (recursive) {
                    count = modifySharedFiles(f, recursive, removal, count);
                } else {
                    if (f.isFile()) {
                        try {
                            f = f.getCanonicalFile();
                            Object old;
                            if (!removal) {
                                old = mySharedFiles.put(f.getPath(), f);
                                if (old == null) {
                                    ++count;
                                }
                            } else {
                                old = mySharedFiles.remove(f.getPath());
                                if (old != null) {
                                    ++count;
                                }
                            }
                        } catch (IOException ex) {
                            out.println("ERROR: " + ex.getMessage() + "\n");
                        }
                    }
                }
            }
        } else {
            try {
                shared = shared.getCanonicalFile();
                Object old;
                if (!removal) {
                    old = mySharedFiles.put(shared.getPath(), shared);
                    if (old == null) {
                        ++count;
                    }
                } else {
                    old = mySharedFiles.remove(shared.getPath());
                    if (old != null) {
                        ++count;
                    }
                }
            } catch (IOException ex) {
                out.println("ERROR: " + ex.getMessage() + "\n");
            }
        }
        return count;
    }

    private void connectToServer(String host, int port) {
        try {
            socketToServer = new Socket(host, port);
            socketToServer.setSoLinger(true, LINGER);
        } catch (UnknownHostException ex) {
            out.println("ERROR: Host not found.\n");
            System.exit(2);
        } catch (IOException ex) {
            out.println("ERROR: " + ex.getMessage() + "\n");
            System.exit(2);
        }
        out.println("INFO: Connected to the server.\n");
    }

    /**
     * Sends FISH server the list of shared files 
     */
    public void share() {
        //TODO
        if (!sharing) {
            sharing = true;
        } else {
        }
    }

    /**
     * Tells FISH server to remove all client's shared files
     */
    public void unshare() {
        //TODO
        sharing = false;
    }

    /**
     * Retrieves the list of all shared files from FISH server (client's shared 
     * files are not included).
     */
    public List<FileInfo> obtainSharedFileList() {
        //TODO
        return null;
    }

    /**
     * Retrieves the list of all shared from FISH server by filename mask 
     * (client's shared files are not included)
     * 
     * @param mask File name mask
     */
    public List<FileInfo> obtainSharedFileList(String mask) {
        //TODO
        return null;
    }

    /**
     * Downloads a shared file from peer and saves it under given name.
     * 
     * @param index An index of file in the list of shared files
     * @param filename A filepath under which downloaded file would be written (saved)
     */
    public void download(int index, String filename) {
        //TODO
    }

    private void startPeerRequestHandling() {
        Runnable requestHandlingTask = new Runnable() {
            @Override
            public void run() {
                try {
                    outSocketToPeer = new ServerSocket(DEFAULT_PEER_PORT);
                    while (true) {
                        Socket clientSocket = outSocketToPeer.accept();
                        new PeerRequestHandler(clientSocket, mySharedFiles).start();
                    }
                } catch (IOException ex) {
                    out.println("ERROR: " + ex.getMessage() + "\n");
                    System.exit(3);
                }
            }
        };
        Thread prh = new Thread(requestHandlingTask, "Peer-Request-Handler");
        prh.setDaemon(true);
        prh.start();
        out.println("INFO: Started handling requests from peers.\n");
    }

    /**
     * Gets the collection of client's files for sharing
     * 
     * @return Collection of files for sharing
     */
    public Collection<File> getMySharedFiles() {
        return mySharedFiles.values();
    }

    /**
     * Print the list of local files that are marked for sharing.
     */
    public void printMyFiles() {
        if (mySharedFiles == null || mySharedFiles.isEmpty()) {
            out.println("No files for sharing were added.\n");
            return;
        }

        int size = mySharedFiles.size();
        String[][] table = new String[size + 1][4];
        fillRow(table[0], "Name", "Size", "Date", "Path");
        int i = 1;
        for (File f : mySharedFiles.values()) {
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
        out.printf("%s\n", line);
        out.printf("Total: %d files.\n\n", size);
    }

    /**
     * Print the last found list of shared files.
     */
    public void printFoundFiles() {
        if (foundSharedFiles == null || foundSharedFiles.isEmpty()) {
            out.println("No shared files were found.\n");
            return;
        }

        int size = foundSharedFiles.size();
        String[][] table = new String[size + 1][4];
        fillRow(table[0], "FileID", "Name", "Size", "Owner");
        int i = 1;
        for (FileInfo fi : foundSharedFiles) {
            fillRow(table[i],
                    Integer.toString(i),
                    fi.getName(),
                    Long.toString(fi.getSize()),
                    fi.getOwnerAddress().getHostString());
            ++i;
        }

        int[] max = calcMaxLengthInColums(table);
        int lineLength = 3 * max.length + 1;
        for (int l : max) {
            lineLength += l;
        }

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
        out.printf("%s\n", line);
        out.printf("Total: %d files.\n\n", size);

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

    /**
     * Starts CLI for FISH client
     */
    public void run() {
        String inputString = null;
        try {
            InetAddress clientIp = InetAddress.getLocalHost();
            inputString = clientIp.getHostName() + "@" + clientIp.getHostAddress() + ">";
        } catch (UnknownHostException ex) {
            out.println("ERROR: Failed to get this host IP.\n");
            System.exit(4);
        }

        while (true) {
            out.print(inputString);
            try {
                String userInput = in.readLine();
                execute(parse(userInput));
            } catch (RejectedException ex) {
                out.println("ERROR: " + ex.getMessage() + "\n");
            } catch (IOException ex) {
                out.println("ERROR: " + ex.getMessage() + "\n");
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
        String[] args = new String[tokenizer.countTokens() - 1];

        try {
            String commandNameString = tokenizer.nextToken().toUpperCase().trim();
            commandName = CommandName.valueOf(commandNameString);
        } catch (IllegalArgumentException ex) {
            System.out.println("ERROR: Illegal command.\n");
            return null;
        }
        int i = 0;
        while (tokenizer.hasMoreTokens()) {
            args[i++] = tokenizer.nextToken();
        }

        return new Command(commandName, args);
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
                obtainSharedFileList();
                printFoundFiles();
                return;
            case DOWNLOAD:
                try {
                    int fileId = Integer.parseInt(command.getArgs()[0]);
                    String filepath = command.getArgs()[1];
                    download(fileId, filepath);
                } catch (ArrayIndexOutOfBoundsException ex) {
                    out.println("ERROR: Not enough parameters.\n");
                } catch (NumberFormatException ex) {
                    out.println("ERROR: Wrong fileID.\n");
                }
                return;
            case EXIT:
                unshare();
                System.exit(0);
            case MYFILES:
                printMyFiles();
                return;
            case LAST:
                printFoundFiles();
                return;
            case FIND:
                try {
                    String mask = command.getArgs()[0];
                    obtainSharedFileList(mask);
                    printFoundFiles();
                } catch (ArrayIndexOutOfBoundsException ex) {
                    out.println("ERROR: Not enough parameters.\n");
                }
                return;
            case ADD:
                try {
                    String filepath = command.getArgs()[0];
                    boolean r = Boolean.parseBoolean(command.getArgs()[1]);
                    addSharedFiles(filepath, r);
                    if (sharing) {
                        share();
                    }
                } catch (ArrayIndexOutOfBoundsException ex) {
                    out.println("ERROR: Not enough parameters.\n");
                }
                return;
            case REMOVE:
                try {
                    String filepath = command.getArgs()[0];
                    boolean r = Boolean.parseBoolean(command.getArgs()[1]);
                    removeSharedFiles(filepath, r);
                    if (sharing) {
                        share();
                    }
                } catch (ArrayIndexOutOfBoundsException ex) {
                    out.println("ERROR: Not enough parameters.\n");
                }
                return;
        }
    }

    /**
     * Main method: creates and runs FISH client.
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
            out.println("ERROR: Wrong port number.\n");
            System.exit(1);
        }
    }

    private enum CommandName {
        /*Begin commands*/
        HELP("Print out the list of commands"),
        SHARE("Start sharing files (if already started, update list of shared files)"),
        UNSHARE("Stop sharing files"),
        ADD("Add files for sharing under given path",
        new String[]{"<path> - path to files", "<recursive> - true/false"}),
        REMOVE("Remove files for sharing under given path",
        new String[]{"<path> - path to files", "<recursive> - true/false"}),
        LIST("Show list of all shared files from server"),
        DOWNLOAD("Download a file with given FileID",
        new String[]{"<fileID> - id of the file from the last retrieved shared file list", "<filepath> - path to file to save"}),
        EXIT("Stop sharing and close client"),
        MYFILES("Show list of my files under shared file path"),
        LAST("Show last retrieved shared file list"),
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
