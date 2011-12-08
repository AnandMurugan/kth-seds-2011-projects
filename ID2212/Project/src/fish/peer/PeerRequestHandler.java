/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fish.peer;

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
import java.util.List;
import java.util.Map;
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

    public PeerRequestHandler(
            Socket peerSocket,
            Map<String, File> myFiles,
            MultiValueMap myFileInfos,
            List<PeerAddress> myNeighbours,
            Map<PeerAddress, List<PeerAddress>> myNeighbourNeighbours,
            List<Socket> neighbourSockets) {
        this.peerSocket = peerSocket;
        this.myFiles = myFiles;
        this.myFileInfos = myFileInfos;
        this.myNeighbours = myNeighbours;
        this.myNeighbourNeighbours = myNeighbourNeighbours;
        this.neighbourSockets = neighbourSockets;

        this.setDaemon(true);
    }

    @Override
    public void run() {
        BufferedReader in = null;
        BufferedWriter out = null;
        ObjectInputStream listIn = null;
        ObjectOutputStream listOut = null;
        List<PeerAddress> peerNeighbours;
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

                        Socket peerSocketForPeers = new Socket(peerAddress.getHost(), peerAddress.getPort());
                        neighbourSockets.add(peerSocketForPeers);
                        for (Socket socket : neighbourSockets) {
                            if (socket == peerSocketForPeers) {
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
                        //Send OK message
                        out.write(FishMessageType.PEER_OK.name());
                        out.newLine();
                        out.flush();

                        //Get list of peer's neighbours
                        listIn = new ObjectInputStream(peerSocket.getInputStream());
                        peerNeighbours = (List<PeerAddress>) listIn.readObject();
                        myNeighbourNeighbours.put(peerAddress, peerNeighbours);
                        break;
                }
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(PeerRequestHandler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(PeerRequestHandler.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                peerSocket.close();
            } catch (IOException ex) {
                Logger.getLogger(PeerRequestHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private boolean match(String name, Pattern p) {
        Matcher m = p.matcher(name);

        return m.find();
    }
}
