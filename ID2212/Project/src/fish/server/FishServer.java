/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fish.server;

import fish.common.FileInfo;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import org.apache.commons.collections.map.MultiValueMap;

/**
 *
 * @author julio
 */
public class FishServer {
    public static final Integer DEFAULT_PORT = 8080;
    public final static int LIVENESS_CHECK_INTERVAL = 5000;
    //public final static int DEFAULT_LIVENESS_PORT = 8082;
    private Map<String, MultiValueMap> allClientFiles;
    final private EntityManager em = javax.persistence.Persistence.createEntityManagerFactory("FishPU").createEntityManager();

    public static void main(String args[]) {
        try {
            Class.forName("org.apache.derby.jdbc.ClientDriver");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(FishServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        new FishServer().run();
    }

    public FishServer() {
        this.allClientFiles = new HashMap<String, MultiValueMap>();
    }

    public void run() {
        System.out.println("FISH server started.");
        System.out.println("Restoring shared files directory from DB...");
        //A very tricky situation here:
        //the code in this place is running in the very beginning
        //and EntityManager seems to may not have time to "prepare" named queries.
        //Until we find more correct solution, this thread will sleep a bit...
        try {
            TimeUnit.MILLISECONDS.sleep(1000);
        } catch (Exception ex) {
            Logger.getLogger(FishServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        restoreDirectoryInfo();
        System.out.println("Shared files directory was successfully restored from DB.");

        boolean listening = true;
        ServerSocket serverSocket = null;

        try {
            serverSocket = new ServerSocket(DEFAULT_PORT);

            while (listening) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("New client accepted.");

                String clientPeerHost = getPeerHost(clientSocket);
                int[] ports = getPeerAndLivenessPorts(clientSocket);
                if (ports == null) {
                    System.out.println("New client rejected (failed to get peer port).");
                    clientSocket.close();
                    continue;
                }
                String clientKey = clientPeerHost + ":" + ports[0];

                if (!allClientFiles.containsKey(clientKey)) {
                    allClientFiles.put(clientKey, new MultiValueMap());
                }

                Thread clientConnectionThread =
                        new Thread(new ClientConnectionHandler(clientSocket, clientPeerHost, ports[0], allClientFiles, em));
                clientConnectionThread.setDaemon(true);
                clientConnectionThread.start();

                Thread livenessConnectionThread =
                        new Thread(new LivenessConnectionHandler(clientPeerHost, ports[0], ports[1], em));
                livenessConnectionThread.setDaemon(true);
                livenessConnectionThread.start();
            }

            serverSocket.close();
        } catch (IOException ex) {
            Logger.getLogger(FishServer.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(1);
        }
    }

    private void restoreDirectoryInfo() {
        List<FileInfo> fileInfoData = em.createNamedQuery(FileInfo.GET_ALL_FILE_INFO_LIST, FileInfo.class).getResultList();

        for (FileInfo f : fileInfoData) {
            if (allClientFiles.containsKey(f.getOwnerHost())) {
                MultiValueMap ownerFileDirectory = allClientFiles.get(f.getOwnerHost());
                ownerFileDirectory.put(f.getName(), f);
            } else {
                MultiValueMap ownerFileDirectory = new MultiValueMap();
                ownerFileDirectory.put(f.getName(), f);
                allClientFiles.put(f.getOwnerHost(), ownerFileDirectory);
            }
        }
    }

    private String getPeerHost(Socket clientSocket) {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            return br.readLine();
        } catch (Exception ex) {
            Logger.getLogger(FishServer.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    private int[] getPeerAndLivenessPorts(Socket clientSocket) {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            int peerPort = Integer.parseInt(br.readLine());
            int livenessPort = Integer.parseInt(br.readLine());
            return new int[]{peerPort, livenessPort};
        } catch (Exception ex) {
            Logger.getLogger(FishServer.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
}
