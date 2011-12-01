/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fish.client;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

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
    public final static int DEFAULT_PORT = 8080;
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
    private Map<String, File> sharedFiles;

    /**
     * Creates a FISH client.
     *
     * @param host FISH server host
     * @param port FISH server port
     * @param sharedFilePath Path to the shared folder
     */
    public Client(String host, int port, String sharedFilePath) {
        /*Setting shared files*/
        sharedFiles = new HashMap<String, File>();

        File shared = new File(sharedFilePath);
        if (shared.isDirectory()) {
            File[] files = shared.listFiles(new FilesOnlyFileFilter());
            for (File f : files) {
                sharedFiles.put(f.getName(), f);
            }
        } else {
            sharedFiles.put(shared.getName(), shared);
        }

        /*Connecting to the server*/
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

        /*Sending list of shared files to server*/
        /*Starting handling requests from other peers*/
    }

    private void run() {
        while (true) {
        }
    }

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

    private class FilesOnlyFileFilter implements FileFilter {
        @Override
        public boolean accept(File pathname) {
            return pathname.isFile();
        }
    }
}
