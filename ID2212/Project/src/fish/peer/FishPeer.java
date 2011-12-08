/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fish.peer;

import fish.common.DownloadRequestHandler;
import fish.common.FileInfo;
import fish.common.FishMessageType;
import fish.common.RejectedException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.collections.map.MultiValueMap;

/**
 * {@code Client} class represents a peer in a FISH.
 * 
 * @author Igor
 */
public final class FishPeer {
    /*CLI*/
    private final static String USAGE_SHORT = "java fish.client.FishPeer";
    private final static PrintStream out = System.out;
    private final static BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
    /*Networking*/
    private ServerSocket downloadServerSocket;
    private ServerSocket peerServerSocket;
    private InetAddress localAddr;
    private List<Socket> neighbourSockets;
    /*FISHing*/
    private Map<String, File> myFiles;
    private MultiValueMap myFileInfos;
    private boolean sharing;
    private List<FileInfo> foundSharedFiles;
    private List<PeerAddress> myNeighbours;
    private Map<PeerAddress, List<PeerAddress>> myNeighbourNeighbours;

    /**
     * Creates a FISH peer.
     *
     * @param sharedFilePath Path to the shared file
     */
    public FishPeer(String sharedFilePath) {
        myFiles = Collections.synchronizedMap(new HashMap<String, File>());
        myFileInfos = new MultiValueMap();
        myNeighbours = Collections.synchronizedList(new ArrayList<PeerAddress>());
        myNeighbourNeighbours = Collections.synchronizedMap(new HashMap<PeerAddress, List<PeerAddress>>());
        sharing = false;
        try {
            localAddr = InetAddress.getLocalHost();
        } catch (UnknownHostException ex) {
            Logger.getLogger(FishPeer.class.getName()).log(Level.SEVERE, null, ex);
        }
        neighbourSockets = Collections.synchronizedList(new ArrayList<Socket>());

        /*Starting handling download requests from other peers*/
        startDownloadRequestHandling();

        /*Starting handling all requests from other peers*/
        startRequestHandling();

        /*Starting requesting liveness to peers*/
        //startLivenessRequesting();

        if (sharedFilePath != null && !sharedFilePath.isEmpty()) {
            /*Setting shared files*/
            addSharedFiles(sharedFilePath, true);

            /*Start sharing*/
            share();
        }
    }

    /**
     * Creates a FISH peer and connects it to existing peer.
     *
     * @param host FISH peer host
     * @param port FISH peer port
     * @param sharedFilePath Path to the shared file
     */
    public FishPeer(String host, int port, String sharedFilePath) {
        this(sharedFilePath);

        /*Connecting to the server*/
        connectToPeer(host, port);
    }

    /**
     * Adds files under given path to the list of shared files.
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

        updateMyFileInfos();

        out.println("Added " + count + " local files for sharing in " + t + " seconds.\n");

        return count;
    }

    /**
     * Removes files under given path from the list of shared files.
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

        updateMyFileInfos();

        out.println("Removed " + count + " local files for sharing in " + t + " seconds.\n");

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
                                old = myFiles.put(f.getPath(), f);
                                if (old == null) {
                                    ++count;
                                }
                            } else {
                                old = myFiles.remove(f.getPath());
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
                    old = myFiles.put(shared.getPath(), shared);
                    if (old == null) {
                        ++count;
                    }
                } else {
                    old = myFiles.remove(shared.getPath());
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

    private void updateMyFileInfos() {
        myFileInfos.clear();
        for (Entry<String, File> entry : myFiles.entrySet()) {
            String path = entry.getKey();
            File file = entry.getValue();
            FileInfo fi = new FileInfo(
                    downloadServerSocket.getInetAddress().getHostAddress(),
                    downloadServerSocket.getLocalPort(),
                    file.getName(),
                    file.length(),
                    path);

            myFileInfos.put(file.getName(), fi);
        }
    }

    public boolean connectToPeer(String host, int port) {
        try {
            connect(host, port);
            out.println("Connected to the peer.\n");
            return true;
        } catch (Exception ex) {
            out.println("ERROR: " + ex.getMessage() + "\n");
            return false;
        }
    }

    void connect(String host, int port) throws IOException, ClassNotFoundException, RejectedException {
        //Establish conenction
        Socket peerSocket = new Socket(host, port, localAddr, 0);
        PeerAddress pa = new PeerAddress(host, port);
        myNeighbours.add(pa);
        neighbourSockets.add(peerSocket);

        BufferedWriter peerOut = new BufferedWriter(new OutputStreamWriter(peerSocket.getOutputStream()));
        BufferedReader peerIn = new BufferedReader(new InputStreamReader(peerSocket.getInputStream()));
        String msg;
        FishMessageType responseType;

        //Send message type
        peerOut.write(FishMessageType.PEER_CONNECT.name());
        peerOut.newLine();
        peerOut.flush();

        //Get OK message
        msg = peerIn.readLine();
        responseType = FishMessageType.valueOf(msg);
        if (responseType != FishMessageType.PEER_OK) {
            throw new RejectedException("Peer didn't respond with OK");
        }

        //Send peer my port for communication
        String portStr = Integer.toString(peerServerSocket.getLocalPort());
        peerOut.write(portStr);
        peerOut.newLine();
        peerOut.flush();

        //Get OK message
        msg = peerIn.readLine();
        responseType = FishMessageType.valueOf(msg);
        if (responseType != FishMessageType.PEER_OK) {
            throw new RejectedException("Peer didn't respond with OK");
        }

        //Send peer list of my neighbours
        for (Socket socket : neighbourSockets) {
            BufferedReader socketIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedWriter socketOut = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            ObjectOutputStream listSocketOut = null;

            socketOut.write(FishMessageType.PEER_NEIGHBOURS.name());
            socketOut.newLine();
            socketOut.flush();

            msg = socketIn.readLine();
            if (FishMessageType.valueOf(msg) != FishMessageType.PEER_OK) {
                //TODO
            }

            listSocketOut = new ObjectOutputStream(socket.getOutputStream());
            listSocketOut.writeObject(myNeighbours);
        }
//        ObjectOutputStream listOut = new ObjectOutputStream(peerSocket.getOutputStream());
//        listOut.writeObject(myNeighbours);

        //Get NEIGHBOURS message
        msg = peerIn.readLine();
        responseType = FishMessageType.valueOf(msg);
        if (responseType != FishMessageType.PEER_NEIGHBOURS) {
            throw new RejectedException("Peer didn't respond with NEIGHBOURS");
        }

        //Send OK message
        peerOut.write(FishMessageType.PEER_OK.name());
        peerOut.newLine();
        peerOut.flush();

        //Get list of peer's neighbours
        ObjectInputStream listIn = new ObjectInputStream(peerSocket.getInputStream());
        List<PeerAddress> peerNeighbours = (List<PeerAddress>) listIn.readObject();
        myNeighbourNeighbours.put(pa, peerNeighbours);
    }

    public void share() {
        sharing = true;
        out.printf("Sharing is ENABLED.\n\n");
    }

    public void unshare() {
        sharing = false;
        out.printf("Sharing is DISABLED.\n\n");
    }

    /**
     * Retrieves the list of all shared files from FISH server (client's shared 
     * files are not included).
     * @return 
     * @throws RejectedException
     * @throws IOException 
     * @throws ClassNotFoundException  
     */
//    public List<FileInfo> find(int ttl) throws RejectedException, IOException, ClassNotFoundException {
//        /*FOR DEBUGGING ONLY start*/
////        List<FileInfo> list = new ArrayList<FileInfo>();
////        for (File f : mySharedFiles.values()) {
//////            try {
////            list.add(new FileInfo(/*InetAddress.getLocalHost().getHostAddress()*/"localhost", f));
//////            } catch (UnknownHostException ex) {
//////                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
//////            }
////        }
////        foundSharedFiles = list;
//        /*FOR DEBUGGING ONLY end*/
//        BufferedWriter serverOut = null;
//        BufferedReader serverIn = null;
//        ObjectInputStream listIn = null;
////        try {
//        serverOut = new BufferedWriter(
//                new OutputStreamWriter(socketToServer.getOutputStream()));
//        serverIn = new BufferedReader(
//                new InputStreamReader(socketToServer.getInputStream()));
//
//        serverOut.write(FishMessageType.CLIENT_FIND_ALL.name());
//        serverOut.newLine();
//        serverOut.flush();
//
////            String response = serverIn.readLine();
////            if ((response == null)
////                    || (response.isEmpty())
////                    || (FishMessageType.SERVER_OK != FishMessageType.valueOf(response))) {
////                throw new RejectedException("Server did not respond with OK");
////            }
//
//        listIn = new ObjectInputStream(socketToServer.getInputStream());
//        foundSharedFiles = (List<FileInfo>) listIn.readObject();
//        //System.out.println(Integer.toString(socketToServer.getInputStream().available()));
//
//        out.printf("%d files were found.\n\n", foundSharedFiles.size());
////        } catch (ClassNotFoundException ex) {
////            Logger.getLogger(FishClient.class.getName()).log(Level.SEVERE, null, ex);
////        } catch (UnknownHostException ex) {
////            out.println("ERROR: Host not found.");
////        } catch (IOException ex) {
////            out.println("ERROR: " + ex.getMessage());
////        } finally {
//////            if (listIn != null) {
//////                listIn.close();
//////            }
//////            if (serverIn != null) {
//////                serverIn.close();
//////            }
//////            if (serverOut != null) {
//////                serverOut.close();
//////            }
////        }
//        return foundSharedFiles;
//    }
    /**
     * Retrieves the list of all shared from FISH server by filename mask 
     * (client's shared files are not included)
     * 
     * @param mask File name mask
     * @return 
     * @throws RejectedException
     * @throws IOException
     * @throws ClassNotFoundException  
     */
//    public List<FileInfo> find(String mask, int ttl) throws RejectedException, IOException, ClassNotFoundException {
//        BufferedWriter serverOut = null;
//        BufferedReader serverIn = null;
//        ObjectInputStream listIn = null;
////        try {
//        serverOut = new BufferedWriter(
//                new OutputStreamWriter(socketToServer.getOutputStream()));
//        serverIn = new BufferedReader(
//                new InputStreamReader(socketToServer.getInputStream()));
//
//        serverOut.write(FishMessageType.CLIENT_FIND + ";" + mask);
//        serverOut.newLine();
//        serverOut.flush();
//
////            String response = serverIn.readLine();
////            if ((response == null)
////                    || (response.isEmpty())
////                    || (FishMessageType.SERVER_OK != FishMessageType.valueOf(response))) {
////                throw new RejectedException("Server did not respond with OK");
////            }
//
//        listIn = new ObjectInputStream(socketToServer.getInputStream());
//        foundSharedFiles = (List<FileInfo>) listIn.readObject();
//        //System.out.println(Integer.toString(socketToServer.getInputStream().available()));
//
//        out.printf("%d files were found.\n\n",
//                foundSharedFiles.size());
////        } catch (ClassNotFoundException ex) {
////            Logger.getLogger(FishClient.class.getName()).log(Level.SEVERE, null, ex);
////        } catch (UnknownHostException ex) {
////            out.println("ERROR: Host not found.");
////        } catch (IOException ex) {
////            out.println("ERROR: " + ex.getMessage());
////        } finally {
//////            if (listIn != null) {
//////                listIn.close();
//////            }
//////            if (serverIn != null) {
//////                serverIn.close();
//////            }
//////            if (serverOut != null) {
//////                serverOut.close();
//////            }
////        }
//        return foundSharedFiles;
//    }
    /**
     * Downloads a shared file from peer and saves it under given name.
     * 
     * @param index An index of file in the list of shared files
     * @param filename A filepath under which downloaded file would be written (saved)
     * @throws RejectedException 
     * @throws IOException  
     */
    public void download(int index, String filename) throws RejectedException, IOException {
        if (foundSharedFiles == null || foundSharedFiles.isEmpty()) {
            throw new RejectedException("Empty local shared file list");
        }

        BufferedWriter peerOut = null;
        BufferedReader peerIn = null;
        FileOutputStream fileOut = null;
        InputStream fileIn = null;

        FileInfo fi = foundSharedFiles.get(index);
        String host = fi.getOwnerHost();
        int port = fi.getOwnerPort();
        long size = fi.getSize();

        Socket downloadSocket = new Socket(host, port);

        peerOut = new BufferedWriter(
                new OutputStreamWriter(downloadSocket.getOutputStream()));
        peerIn = new BufferedReader(
                new InputStreamReader(downloadSocket.getInputStream()));

        String request = FishMessageType.PEER_DOWNLOAD.name() + ";" + fi.getLocalKey();
        peerOut.write(request);
        peerOut.newLine();
        peerOut.flush();

        String response = peerIn.readLine();
        if ((response == null)
                || (response.isEmpty())
                || (FishMessageType.PEER_OK != FishMessageType.valueOf(response))) {
            throw new RejectedException("Peer did not respond with OK");
        }

        out.println("Downloading...");
        fileIn = downloadSocket.getInputStream();
        File file = new File(filename);
        fileOut = new FileOutputStream(file);
        byte[] buf = new byte[1024];
        int len;
        long done = 0l;
        int i = 1;
        long t1 = System.currentTimeMillis();
        while ((len = fileIn.read(buf)) > 0) {
            fileOut.write(buf, 0, len);
            done += len;
            if (((float) done / size) >= i * 0.1f) {
                out.print(".");
                i++;
            }
        }
        long t2 = System.currentTimeMillis();
        float t = (t2 - t1) / 1e3f;
        float rate = (size / t) / 1e6f;

        out.println("\nDone!");
        out.printf("\nFile \"%s\" has been downloaded successfully from %s into \"%s\" in %f seconds (avarage download speed - %fMbps).\n\n",
                fi.getName(),
                fi.getOwnerHost(),
                file.getCanonicalPath(),
                t,
                rate);
        if (fileIn != null) {
            fileIn.close();
        }
        if (fileOut != null) {
            fileOut.close();
        }
        if (peerIn != null) {
            peerIn.close();
        }
        if (peerOut != null) {
            peerOut.close();
        }

        if (downloadSocket != null) {
            downloadSocket.close();
        }
    }

    private void startDownloadRequestHandling() {
        try {
            downloadServerSocket = new ServerSocket(0);
            Runnable requestHandlingTask = new Runnable() {
                @Override
                public void run() {
                    try {
                        while (true) {
                            Socket clientSocket = downloadServerSocket.accept();
                            if (sharing) {
                                new DownloadRequestHandler(clientSocket, myFiles).start();
                            }
                        }
                    } catch (IOException ex) {
                        out.println("ERROR: " + ex.getMessage() + "\n");
                    }
                }
            };
            Thread prh = new Thread(requestHandlingTask, "Download-Handler");
            prh.setDaemon(true);
            prh.start();
            out.println("Started handling download requests from peers on port " + downloadServerSocket.getLocalPort() + ".\n");
        } catch (IOException ex) {
            out.println("ERROR: " + ex.getMessage() + "\n");
            System.exit(3);
        }
    }

    private void startRequestHandling() {
        try {
            peerServerSocket = new ServerSocket(0);
            final FishPeer thisPeer = this;
            Runnable requestHandlingTask = new Runnable() {
                @Override
                public void run() {
                    try {
                        while (true) {
                            Socket peerSocket = peerServerSocket.accept();
                            new PeerRequestHandler(
                                    peerSocket,
                                    myFiles,
                                    myFileInfos,
                                    myNeighbours,
                                    myNeighbourNeighbours,
                                    neighbourSockets,
                                    thisPeer).start();
                        }
                    } catch (IOException ex) {
                        out.println("ERROR: " + ex.getMessage() + "\n");
                    }
                }
            };

            Thread rh = new Thread(requestHandlingTask, "Request-Handler");
            rh.setDaemon(true);
            rh.start();
            out.println("Started handling requests from peers on port " + peerServerSocket.getLocalPort() + ".\n");
        } catch (IOException ex) {
            out.println("ERROR: " + ex.getMessage() + "\n");
            System.exit(3);
        }
    }

    /**
     * Gets the collection of client's files for sharing
     * 
     * @return Collection of files for sharing
     */
    public Collection<File> getMySharedFiles() {
        return myFiles.values();
    }

    /**
     * Print the list of local files that are marked for sharing.
     */
    public void printMyFiles() {
        if (myFiles == null || myFiles.isEmpty()) {
            out.println("No files for sharing were added.\n");
            return;
        }

        int size = myFiles.size();
        String[][] table = new String[size + 1][4];
        fillRow(table[0], "Name", "Size", "Date", "Path");
        int i = 1;
        for (File f : myFiles.values()) {
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
                    fi.getOwnerHost() + ":" + fi.getOwnerPort());
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
     * Print the list of neighbours.
     */
    public void printNeighbours() {
        for (PeerAddress pa : myNeighbours) {
            out.println(pa);
        }
        out.println();
    }

    public void printNeighbourNeighbours() {
        for (Entry<PeerAddress, List<PeerAddress>> entry : myNeighbourNeighbours.entrySet()) {
            PeerAddress n = entry.getKey();
            List<PeerAddress> nList = entry.getValue();
            out.println(n);
            for (PeerAddress nn : nList) {
                out.println("\t" + nn);
            }
        }
        out.println();
    }

    /**
     * Starts CLI for FISH client
     */
    public void run() {
        String inputString = null;
        try {
            InetAddress clientIp = InetAddress.getLocalHost();
            inputString = clientIp.getHostName() + "@" + clientIp.getHostAddress() + ":" + peerServerSocket.getLocalPort() + "(" + downloadServerSocket.getLocalPort() + ")" + ">";
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
            case FINDALL:
//                try {
//                    find(0);
//                    printFoundFiles();
//                } catch (RejectedException ex) {
//                    out.println("ERROR: Search rejected. Reason: " + ex.getMessage() + "\n");
//                } catch (ClassNotFoundException ex) {
//                    out.println("ERROR: " + ex.getMessage() + "\n");
//                } catch (IOException ex) {
//                    out.println("ERROR: " + ex.getMessage() + "\n");
//                }
                return;
            case DOWNLOAD:
                try {
                    int fileId = Integer.parseInt(command.getArgs()[0]) - 1;
                    String filepath = command.getArgs()[1];
                    download(fileId, filepath);
                } catch (IOException ex) {
                    out.println("ERROR: " + ex.getMessage() + "\n");
                } catch (ArrayIndexOutOfBoundsException ex) {
                    out.println("ERROR: Not enough parameters.\n");
                } catch (NumberFormatException ex) {
                    out.println("ERROR: Wrong fileID.\n");
                } catch (RejectedException ex) {
                    out.println("ERROR: Downloading rejected. Reason: " + ex.getMessage() + "\n");
                }
                return;
            case EXIT:
                System.exit(0);
            case MYFILES:
                printMyFiles();
                return;
            case LAST:
                printFoundFiles();
                return;
            case FIND:
//                try {
//                    String mask = command.getArgs()[0];
//                    find(mask, 0);
//                    printFoundFiles();
//                } catch (RejectedException ex) {
//                    out.println("ERROR: Search rejected. Reason: " + ex.getMessage() + "\n");
//                } catch (ClassNotFoundException ex) {
//                    out.println("ERROR: " + ex.getMessage() + "\n");
//                } catch (IOException ex) {
//                    out.println("ERROR: " + ex.getMessage() + "\n");
//                } catch (ArrayIndexOutOfBoundsException ex) {
//                    out.println("ERROR: Not enough parameters.\n");
//                }
                return;
            case ADD:
                try {
                    String filepath = command.getArgs()[0];
                    boolean r = Boolean.parseBoolean(command.getArgs()[1]);
                    addSharedFiles(filepath, r);
                } catch (ArrayIndexOutOfBoundsException ex) {
                    out.println("ERROR: Not enough parameters.\n");
                }
                return;
            case REMOVE:
                try {
                    String filepath = command.getArgs()[0];
                    boolean r = Boolean.parseBoolean(command.getArgs()[1]);
                    removeSharedFiles(filepath, r);
                } catch (ArrayIndexOutOfBoundsException ex) {
                    out.println("ERROR: Not enough parameters.\n");
                }
                return;
            case N:
                printNeighbours();
                return;
            case NN:
                printNeighbourNeighbours();
                return;
            case CONNECT:
                try {
                    String host = command.getArgs()[0];
                    int port = Integer.parseInt(command.getArgs()[1]);
                    connectToPeer(host, port);
                } catch (NumberFormatException ex) {
                    out.println("ERROR: Wrong port.\n");
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
                withDescription("set the IP address of FISH peer").
                withLongOpt("host").
                create('h');
        Option portOption = OptionBuilder.withArgName("port").
                hasArg().
                withDescription("set port number of FISH peer").
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
            if ((line.hasOption("h")) && (line.hasOption("p"))) {
                host = line.getOptionValue("h");
                port = Integer.parseInt(line.getOptionValue("p"));
                new FishPeer(host, port, sharedFilePath).run();
            } else {
                new FishPeer(sharedFilePath).run();
            }
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
        SHARE("Start sharing files"),
        UNSHARE("Stop sharing files"),
        ADD("Add files for sharing under given path",
        new String[]{"<path> - path to files", "<recursive> - true/false"}),
        REMOVE("Remove files for sharing under given path",
        new String[]{"<path> - path to files", "<recursive> - true/false"}),
        FINDALL("Search all files from peers",
        new String[]{"<TTL> - Time To Live ('radius' of lookup)"}),
        DOWNLOAD("Download a file with given FileID",
        new String[]{"<fileID> - id of the file from the last retrieved shared file list", "<filepath> - path to file to save"}),
        EXIT("Stop sharing and close peer"),
        MYFILES("Show list of my files under shared file path"),
        LAST("Show last retrieved shared file list"),
        FIND("Search shared files by given name",
        new String[]{"<name> - name mask", "<TTL> - Time To Live ('radius' of lookup)"}),
        N("Print list of neighbours"),
        NN("Print lists of all neighbour neighbours"),
        CONNECT("Connect to peer",
        new String[]{"<host> - peer host", "<port> - peer port"});
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
