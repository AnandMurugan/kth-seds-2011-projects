/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fish.server;

import fish.common.FileInfo;
import fish.common.FishMessageType;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.persistence.EntityManager;
import org.apache.commons.collections.map.MultiValueMap;

/**
 * Class that handles all requests from client to server
 * @author julio
 */
public class ClientConnectionHandler extends Thread {
    private Socket clientSocket;
    private String clientPeerHost;
    private int clientPeerPort;
    private final Map<String, MultiValueMap> allFilesMap;
    private boolean alive = true;
    private final EntityManager em;

    /**
     * Creates a new {@code ClientConnectionHandler
     * 
     * @param clientSocket socket to a client
     * @param clientPeerHost client's host
     * @param clientPeerPort client's port
     * @param files shared file directory
     * @param em an {@code EntityManager}
     */
    public ClientConnectionHandler(
            Socket clientSocket,
            String clientPeerHost,
            int clientPeerPort,
            Map<String, MultiValueMap> files,
            EntityManager em) {
        this.clientSocket = clientSocket;
        this.clientPeerHost = clientPeerHost;
        this.clientPeerPort = clientPeerPort;
        this.allFilesMap = files;
        this.em = em;
    }

    @Override
    public void run() {
        System.out.println("Handling new client started.");

        String clientHost = clientPeerHost;
        String client = clientHost + ":" + clientPeerPort;
        final MultiValueMap clientFilesMultiMap = allFilesMap.get(client);
        /*final List<FileInfo> clientFileInfoDataPersist = em.createNamedQuery(FileInfo.GET_FILE_INFO_LIST_BY_OWNER, FileInfo.class).
        setParameter("owner", clientAddr).
        getResultList();
         */
        BufferedReader in = null;
        BufferedWriter out = null;
        ObjectInputStream listIn = null;
        ObjectOutputStream listOut = null;
        try {
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));

            String str;
            while (alive) {
                if ((str = in.readLine()) == null) {
                    break;
                }
                System.out.println(str);

                StringTokenizer tokens = new StringTokenizer(str, ";");
                String msgTypeString = tokens.nextToken();
                FishMessageType msgType = FishMessageType.valueOf(msgTypeString);
                switch (msgType) {
                    case CLIENT_SHARE:
                        out.write(FishMessageType.SERVER_OK.name());
                        out.newLine();
                        out.flush();

                        try {
                            listIn = new ObjectInputStream(clientSocket.getInputStream());
                            List<FileInfo> clientFiles = (List<FileInfo>) listIn.readObject();
                            //System.out.println(Integer.toString(clientSocket.getInputStream().available()));
                            //EntityTransaction transaction = null;

                            em.getTransaction().begin();
                            int numberModified = em.createNamedQuery(FileInfo.DELETE_ALL_FILE_INFO_DATA_BY_OWNER).
                                    setParameter("ownerHost", clientHost).
                                    setParameter("ownerPort", clientPeerPort).
                                    executeUpdate();
                            System.out.println(numberModified + " FileInfo entries of [" + client + "] will be deleted from the DB.");

                            synchronized (allFilesMap) {
                                clientFilesMultiMap.clear();
                                for (FileInfo fi : clientFiles) {
                                    clientFilesMultiMap.put(fi.getName(), fi);
                                    em.persist(fi);
                                }
                            }
                            em.getTransaction().commit();
                            out.write(FishMessageType.SERVER_OK.name());
                            out.newLine();
                            out.flush();
                        } catch (ClassNotFoundException ex) {
                            Logger.getLogger(ClientConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        break;
                    case CLIENT_UNSHARE:
                        synchronized (allFilesMap) {
                            clientFilesMultiMap.clear();
                        }
                        em.getTransaction().begin();
                        int numberModified = em.createNamedQuery(FileInfo.DELETE_ALL_FILE_INFO_DATA_BY_OWNER).
                                setParameter("ownerHost", clientHost).
                                setParameter("ownerPort", clientPeerPort).
                                executeUpdate();
                        System.out.println(numberModified + " FileInfo entries of [" + client + "] will be deleted from the DB.");
                        em.getTransaction().commit();
                        out.write(FishMessageType.SERVER_OK.name());
                        out.newLine();
                        out.flush();
                        break;
                    case CLIENT_FIND_ALL:
//                        out.write(FishMessageType.SERVER_OK.name());
//                        out.newLine();
//                        out.flush();

                        List<FileInfo> listAll = new ArrayList<FileInfo>();
                        synchronized (allFilesMap) {
                            for (String key : allFilesMap.keySet()) {
                                if (!key.equals(client)) {
                                    listAll.addAll(allFilesMap.get(key).values());
                                }
                            }
                        }
                        listOut = new ObjectOutputStream(clientSocket.getOutputStream());
                        //listOut.reset();
                        listOut.writeObject(listAll);
                        break;
                    case CLIENT_FIND:
                        String mask = tokens.nextToken();
                        Pattern p = Pattern.compile(mask);

//                        out.write(FishMessageType.SERVER_OK.name());
//                        out.newLine();
//                        out.flush();

                        List<FileInfo> list = new ArrayList<FileInfo>();
                        synchronized (allFilesMap) {
                            for (String key : allFilesMap.keySet()) {
                                if (!key.equals(client)) {
                                    for (String name : (Set<String>) allFilesMap.get(key).keySet()) {
                                        if (match(name, p)) {
                                            list.addAll(((MultiValueMap) allFilesMap.get(key)).getCollection(name));
                                        }
                                    }
                                }
                            }
                        }
                        listOut = new ObjectOutputStream(clientSocket.getOutputStream());
                        //listOut.reset();
                        listOut.writeObject(list);
                        break;
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(ClientConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
                if (out != null) {
                    out.close();
                }
            } catch (IOException ex) {
                Logger.getLogger(ClientConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

//    /**
//     * 
//     * @param alive
//     */
//    public void setAlive(boolean alive) {
//        this.alive = alive;
//    }

    private boolean match(String name, Pattern p) {
        //return name.equalsIgnoreCase(mask);
        Matcher m = p.matcher(name);

        return m.find();
    }
}
