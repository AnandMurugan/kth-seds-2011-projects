/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fish.client;

import fish.common.RejectedException;
import fish.server.Server;
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
import java.util.List;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.collections.MultiMap;
import org.apache.commons.collections.map.MultiValueMap;

/**
 * {@code Client} class represents a peer in a FISH.
 * 
 * @author Igor
 */
public class Client {
    /**
     * Default server host
     */
    public final static String DEFAULT_HOST = "localhost";
    /**
     * Default server port
     */
    public final static int DEFAULT_PORT = Server.DEFAULT_PORT;
    /**
     * Default peer port
     */
    public final static int DEFAULT_PEER_PORT = 8081;
    /*CLI*/
    private final static String USAGE = "java fish.client.Client <shared_file_path> [<server_address> [<server_port>]]";
    private final static PrintStream out = System.out;
    private final static BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
    /*Networking*/
    private final static int LINGER = 100;
    private Socket socketToServer;
    private Socket inSocketToPeer;
    private ServerSocket outSocketToPeer;
    /*Sharing*/
    private MultiMap sharedFiles;

    /**
     * Creates a FISH client.
     *
     * @param host FISH server host
     * @param port FISH server port
     * @param sharedFilePath Path to the shared folder
     */
    public Client(String host, int port, String sharedFilePath) {
        /*Setting shared files*/
        setSharedFiles(sharedFilePath);

        /*Connecting to the server*/
        connectToServer(host, port);

        /*Sending list of shared files to server*/
        share();

        /*Starting handling requests from other peers*/
        startPeerRequestHandling();
    }

    private void setSharedFiles(String sharedFilePath) {
        sharedFiles = new MultiValueMap();

        long t1 = System.currentTimeMillis();
        File shared = new File(sharedFilePath);
        searchLocalSharedFiles(shared);//recursive call
        long t2 = System.currentTimeMillis();
        float t = (t2 - t1) / 1000.0f;

        out.println("INFO: Found " + ((MultiValueMap) sharedFiles).totalSize() + " local files for sharing in " + t + " seconds.");
    }

    private void searchLocalSharedFiles(File shared) {
        if (shared.isDirectory()) {
            File[] files = shared.listFiles();
            for (File f : files) {
                searchLocalSharedFiles(f);
            }
        } else {
            sharedFiles.put(shared.getName(), shared);
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
    }

    private void unshare() {
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
        new Thread(requestHandlingTask).start();
        out.println("INFO: Started handling requests from peers.");
    }

    private void run() {
        String inputString = null;
        try {
            InetAddress clientIp = InetAddress.getLocalHost();
            inputString = "fish-client@" + clientIp + ">";
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
                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
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
        int userInputTokenNo = 1;
        while (tokenizer.hasMoreTokens()) {
            switch (userInputTokenNo) {
                case 1:
                    try {
                        String commandNameString = tokenizer.nextToken();
                        commandName = CommandName.valueOf(commandNameString.toUpperCase().trim());
                    } catch (IllegalArgumentException ex) {
                        out.println("ERROR: Unrecognized command.");
                        return null;
                    }
                    break;
                case 2:
                    args.add(tokenizer.nextToken());
                    break;
                default:
                    out.println("ERROR: Illegal command.");
                    return null;
            }
            userInputTokenNo++;
        }
        return new Command(commandName, args.toArray(new String[]{}));
    }

    void execute(Command command) throws RejectedException {
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
                int fileId;
                try {
                    fileId = Integer.parseInt(command.getArgs()[0]);
                } catch (ArrayIndexOutOfBoundsException ex) {
                    out.println("ERROR: No FileID are specified.");
                    return;
                } catch (NumberFormatException ex) {
                    out.println("EROR: Wrong FileID.");
                    return;
                }
                //TODO
                return;
            case EXIT:
                unshare();
                System.exit(0);
            case MYLIST:
                //TODO
                return;
            case LASTLIST:
                //TODO
                return;
            case FIND:
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
        String sharedFilePath = null;
        String host = DEFAULT_HOST;
        int port = DEFAULT_PORT;
        switch (args.length) {
            case 3:
                try {
                    port = Integer.parseInt(args[2]);
                } catch (NumberFormatException e) {
                    out.println(USAGE);
                    System.exit(1);
                }
            case 2:
                host = args[1];
            case 1:
                sharedFilePath = args[0];
                break;
            default:
                out.println(USAGE);
                System.exit(1);
        }

        new Client(host, port, sharedFilePath).run();
    }

    private enum CommandName {
        HELP("Print out the list of commands"),
        SHARE("Start sharing files (if already started, update list of shared files)"),
        UNSHARE("Stop sharing files"),
        LIST("Show list of all shared files from server"),
        DOWNLOAD("Download a file with given FileID", new String[]{"<FileID> - id of the file from the last retrieved shared file list"}),
        EXIT("Stop sharing and close client"),
        MYLIST("Show list of my files under shared file path"),
        LASTLIST("Show last retrieved shared file list"),
        FIND("Get list of shared files by given name", new String[]{"<name> - name mask"});
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
