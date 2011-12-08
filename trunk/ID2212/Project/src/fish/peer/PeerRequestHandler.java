/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fish.peer;

import fish.common.FileInfo;
import fish.common.FishMessageType;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
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
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.collections.map.MultiValueMap;

/**
 *
 * @author julio
 */
public class PeerRequestHandler extends Thread {
    private Socket peerSocket;
    private Map<String, File> myFiles;
    private MultiValueMap myFileInfos;
    private List<PeerAddress> myNeighbours;
    private Map<PeerAddress, List<PeerAddress>> myNeighbourNeighbours;
    private PeerAddress peerAddress;
    private List<Socket> neighbourSockets;
    private FishPeer myFishPeer;

    public PeerRequestHandler(
            Socket peerSocket,
            Map<String, File> myFiles,
            MultiValueMap myFileInfos,
            List<PeerAddress> myNeighbours,
            Map<PeerAddress, List<PeerAddress>> myNeighbourNeighbours,
            List<Socket> neighbourSockets,
            FishPeer myFishPeer) {
        this.peerSocket = peerSocket;
        this.myFiles = myFiles;
        this.myFileInfos = myFileInfos;
        this.myNeighbours = myNeighbours;
        this.myNeighbourNeighbours = myNeighbourNeighbours;
        this.neighbourSockets = neighbourSockets;
        this.myFishPeer = myFishPeer;

        this.setDaemon(true);
    }

    @Override
    public void run() {
        BufferedReader in = null;
        BufferedWriter out = null;
        ObjectInputStream listIn = null;
        ObjectOutputStream listOut = null;

        List<PeerAddress> peerNeighbours;
        Socket psfp = null; //peerSocketForPeers - the one which is used for handling requests
        int ttl;
        String senderHost;
        int senderPort;

        try {
            in = new BufferedReader(new InputStreamReader(peerSocket.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(peerSocket.getOutputStream()));

            String str;
            while (true) {
                if ((str = in.readLine()) == null) {
                    break;
                }

                FishMessageType msgType = FishMessageType.valueOf(str);
                switch (msgType) {
                    case PEER_CONNECT:
                        out.write(FishMessageType.PEER_OK.name());
                        out.newLine();
                        out.flush();

                        str = in.readLine();
                        int port = Integer.parseInt(str);
                        peerAddress = new PeerAddress(peerSocket.getInetAddress().getHostAddress(), port);
                        myNeighbours.add(peerAddress);

                        out.write(FishMessageType.PEER_OK.name());
                        out.newLine();
                        out.flush();

                        str = in.readLine();
                        if (FishMessageType.valueOf(str) != FishMessageType.PEER_NEIGHBOURS) {
                            //TODO
                        }

                        out.write(FishMessageType.PEER_OK.name());
                        out.newLine();
                        out.flush();

                        listIn = new ObjectInputStream(peerSocket.getInputStream());
                        peerNeighbours = (List<PeerAddress>) listIn.readObject();
                        myNeighbourNeighbours.put(peerAddress, peerNeighbours);

                        psfp = new Socket(peerAddress.getHost(), peerAddress.getPort());
                        BufferedWriter psfpOut = new BufferedWriter(new OutputStreamWriter(psfp.getOutputStream()));
                        psfpOut.write(FishMessageType.PEER_ADDRESS.name());
                        psfpOut.newLine();
                        psfpOut.write(peerSocket.getLocalAddress().getHostAddress());
                        psfpOut.newLine();
                        psfpOut.write(Integer.toString(peerSocket.getLocalPort()));
                        psfpOut.newLine();
                        psfpOut.flush();

                        neighbourSockets.add(psfp);
                        for (Socket socket : neighbourSockets) {
                            if (socket.equals(psfp)) {
                                socket = peerSocket;
                            }
                            BufferedReader socketIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                            BufferedWriter socketOut = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                            ObjectOutputStream listSocketOut = null;

                            socketOut.write(FishMessageType.PEER_NEIGHBOURS.name());
                            socketOut.newLine();
                            socketOut.flush();

                            str = socketIn.readLine();
                            if (FishMessageType.valueOf(str) != FishMessageType.PEER_OK) {
                                //TODO
                            }

                            listSocketOut = new ObjectOutputStream(socket.getOutputStream());
                            listSocketOut.writeObject(myNeighbours);
                        }
                        break;
                    case PEER_NEIGHBOURS:
                        out.write(FishMessageType.PEER_OK.name());
                        out.newLine();
                        out.flush();

                        listIn = new ObjectInputStream(peerSocket.getInputStream());
                        peerNeighbours = (List<PeerAddress>) listIn.readObject();
                        myNeighbourNeighbours.put(peerAddress, peerNeighbours);
                        break;
                    case PEER_ADDRESS:
                        str = in.readLine();
                        String remotePeerHost = str;

                        str = in.readLine();
                        int remotePeerPort = Integer.parseInt(str);

                        peerAddress = new PeerAddress(remotePeerHost, remotePeerPort);
                        break;
                    case PEER_FIND:
                        str = in.readLine();
                        ttl = Integer.parseInt(str);
                        str = in.readLine();
                        String mask = str;
                        str = in.readLine();
                        senderHost = str;
                        str = in.readLine();
                        senderPort = Integer.parseInt(str);

                        if (myFishPeer.isSharing()) {
                            List<FileInfo> listMask = new ArrayList<FileInfo>();
                            Pattern p = Pattern.compile(mask);
                            for (String filename : (Set<String>) myFileInfos.keySet()) {
                                if (match(filename, p)) {
                                    listMask.addAll(myFileInfos.getCollection(filename));
                                }
                            }
                            if (!listMask.isEmpty()) {
                                Socket senderSocket = new Socket(senderHost, senderPort);
                                listOut = new ObjectOutputStream(senderSocket.getOutputStream());
                                listOut.writeObject(listMask);
                                //senderSocket.close();
                            }
                        }

                        --ttl;
                        if (ttl > 0) {
                            Socket prev = findSocketByPeerAddress();
                            for (Socket socket : neighbourSockets) {
                                if (!socket.equals(prev)) {
                                    BufferedWriter socketOut = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

                                    socketOut.write(FishMessageType.PEER_FIND.name());
                                    socketOut.newLine();
                                    socketOut.write(Integer.toString(ttl));
                                    socketOut.newLine();
                                    socketOut.write(mask);
                                    socketOut.newLine();
                                    socketOut.write(senderHost);
                                    socketOut.newLine();
                                    socketOut.write(Integer.toString(senderPort));
                                    socketOut.newLine();
                                    socketOut.flush();
                                }
                            }
                        }
                        break;
                    case PEER_FIND_ALL:
                        str = in.readLine();
                        ttl = Integer.parseInt(str);
                        str = in.readLine();
                        senderHost = str;
                        str = in.readLine();
                        senderPort = Integer.parseInt(str);

                        if (myFishPeer.isSharing()) {
                            List<FileInfo> listAll = new ArrayList<FileInfo>(myFileInfos.values());
                            if (!listAll.isEmpty()) {
                                Socket senderSocket = new Socket(senderHost, senderPort);
                                listOut = new ObjectOutputStream(senderSocket.getOutputStream());
                                listOut.writeObject(listAll);
                                //senderSocket.close();
                            }
                        }

                        --ttl;
                        if (ttl > 0) {
                            Socket prev = findSocketByPeerAddress();
                            for (Socket socket : neighbourSockets) {
                                if (!socket.equals(prev)) {
                                    BufferedWriter socketOut = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

                                    socketOut.write(FishMessageType.PEER_FIND_ALL.name());
                                    socketOut.newLine();
                                    socketOut.write(Integer.toString(ttl));
                                    socketOut.newLine();
                                    socketOut.write(senderHost);
                                    socketOut.newLine();
                                    socketOut.write(Integer.toString(senderPort));
                                    socketOut.newLine();
                                    socketOut.flush();
                                }
                            }
                        }
                        break;
                }
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(PeerRequestHandler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            //Peer died! Well, we assume that...
            System.out.println("\nPeer died!");
            List<PeerAddress> list = myNeighbourNeighbours.get(peerAddress);

            myNeighbourNeighbours.remove(peerAddress);
            myNeighbours.remove(peerAddress);
            neighbourSockets.remove(findSocketByPeerAddress());

            int pos = list.indexOf(new PeerAddress(peerSocket.getLocalAddress().getHostAddress(), peerSocket.getLocalPort()));
            if (pos > 0) {
                PeerAddress newNeighbour = list.get(0);
                try {
                    myFishPeer.connect(newNeighbour.getHost(), newNeighbour.getPort());
                    System.out.println("\nReconnect successful!");
                } catch (Exception ex1) {
                    System.out.println("\nReconnect failed!");
                }
            } else if (pos == 0) {
                try {
                    myFishPeer.propagateNeighbourList();
                } catch (Exception ex1) {
                    System.out.println("\nMulticasting neighbour list failed!");
                }

            }
        } finally {
            try {
                peerSocket.close();
            } catch (IOException ex) {
                Logger.getLogger(PeerRequestHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private Socket findSocketByPeerAddress() {
        Socket toDelete = null;
        for (Socket s : neighbourSockets) {
            if (s.getInetAddress().getHostAddress().equals(peerAddress.getHost())
                    && s.getPort() == peerAddress.getPort()) {
                toDelete = s;
                break;
            }
        }
        return toDelete;
    }

    private boolean match(String name, Pattern p) {
        Matcher m = p.matcher(name);

        return m.find();
    }
}
