/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fish.server;

import fish.common.FileInfo;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.apache.commons.collections.map.MultiValueMap;

/**
 *
 * @author julio
 */
public class FishServer {
    public static final Integer DEFAULT_PORT = 8080;
    private Map<String, MultiValueMap> allClientFiles;
    @PersistenceContext(unitName = "FishPU")
    private EntityManager em;

    public FishServer() {
        this.allClientFiles = new HashMap<String, MultiValueMap>();
    }

    public void run() {
        System.out.println("FISH server started.");

        boolean listening = true;
        ServerSocket serverSocket = null;

        try {
            serverSocket = new ServerSocket(DEFAULT_PORT);

            while (listening) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("New client accepted.");

                String clientAddr = clientSocket.getInetAddress().getHostAddress();
                MultiValueMap oneClientFiles;
                if (!allClientFiles.containsKey(clientAddr)) {
                    allClientFiles.put(clientAddr, new MultiValueMap());
                }
                Thread clientConnectionThread = new Thread(new ClientConnectionHandler(clientSocket, allClientFiles, em));
                clientConnectionThread.setDaemon(true);
                clientConnectionThread.start();

                //TODO add here thread that will check liveness of the client
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

    public static void main(String args[]) {
        new FishServer().run();
    }
}
