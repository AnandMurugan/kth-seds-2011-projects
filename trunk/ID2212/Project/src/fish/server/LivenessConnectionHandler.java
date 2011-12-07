/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fish.server;

import fish.common.FileInfo;
import java.net.Socket;
import java.util.concurrent.TimeUnit;
import javax.persistence.EntityManager;

/**
 *
 * @author julio
 */
public class LivenessConnectionHandler extends Thread {
    private String clientHost;
    private boolean isAlive;
    private int clientPeerPort;
    private int clientLivenessPort;
    private final EntityManager em;

    /**
     * 
     * @param clientHost
     * @param clientPeerPort
     * @param clientLivenessPort
     * @param em
     */
    public LivenessConnectionHandler(String clientHost, int clientPeerPort, int clientLivenessPort, EntityManager em) {
        this.clientHost = clientHost;
        this.isAlive = true;
        this.clientPeerPort = clientPeerPort;
        this.clientLivenessPort = clientLivenessPort;
        this.em = em;
    }

    @Override
    public void run() {
        String client = clientHost + ":" + clientPeerPort;
        Socket clientLivenessSocket = null;
        while (isAlive) {
            try {
                TimeUnit.MILLISECONDS.sleep(FishServer.LIVENESS_CHECK_INTERVAL);
                clientLivenessSocket = new Socket(clientHost, clientLivenessPort);
                clientLivenessSocket.close();
                System.out.println("Client " + client + " is alive.");
            } catch (Exception ex) {
                System.out.println("ERROR: Client is not available.\n");
                isAlive = false;
                System.out.println("ERROR: Client " + client + " is not responding.");
                em.getTransaction().begin();
                int numberModified = em.createNamedQuery(FileInfo.DELETE_ALL_FILE_INFO_DATA_BY_OWNER).
                        setParameter("ownerHost", clientHost).
                        setParameter("ownerPort", clientPeerPort).
                        executeUpdate();
                System.out.println(numberModified + " FileInfo entries of [" + client + "] will be deleted from the DB.");
                em.getTransaction().commit();
//            } catch (IOException ex) {
//                System.out.println("ERROR: " + ex.getMessage() + "\n");
//                isAlive = false;
//                System.out.println("ERROR: Client " + clientHost + " is not responding.");
//                // TODO. Remove file info entries from server cache and DB
//            } catch (InterruptedException ex) {
            }
        }
    }
}
