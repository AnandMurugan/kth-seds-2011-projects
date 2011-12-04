/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fish.server;

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
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.collections.MultiMap;
import org.apache.commons.collections.map.MultiValueMap;

/**
 *
 * @author julio
 */
public class ClientConnectionHandler extends Thread {
    private Socket clientSocket;
    private MultiMap files;
    private boolean alive = true;

    public ClientConnectionHandler(Socket clientSocket, MultiMap files) {
        this.clientSocket = clientSocket;
        this.files = files;
    }

    @Override
    public void run() {
        BufferedReader in = null;
        BufferedWriter out = null;
        ObjectInputStream listIn = null;
        ObjectOutputStream listOut = null;
        try {
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
            listIn = new ObjectInputStream(clientSocket.getInputStream());
            listOut = new ObjectOutputStream(clientSocket.getOutputStream());

            String str;
            while (alive) {
                if ((str = in.readLine()) == null) {
                    break;
                }

                StringTokenizer tokens = new StringTokenizer(str, ";");
                String msgTypeString = tokens.nextToken();
                FishMessageType msgType = FishMessageType.valueOf(msgTypeString);
                switch (msgType) {
                    case CLIENT_SHARE:
                        out.write(FishMessageType.SERVER_OK.name());
                        out.newLine();
                        out.flush();

                        try {
                            List<File> clientFiles = (List<File>) listIn.readObject();
                            String host = clientSocket.getInetAddress().getHostAddress();
                            for (File f : clientFiles) {
                                files.put(f.getName(), new FileInfo(host, f));
                            }

                            out.write(FishMessageType.SERVER_OK.name());
                            out.newLine();
                            out.flush();
                        } catch (ClassNotFoundException ex) {
                            Logger.getLogger(ClientConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        break;
                    case CLIENT_UNSHARE:
                        String host = clientSocket.getInetAddress().getHostAddress();

                        for (String key : (Set<String>) files.keySet()) {
                            for (FileInfo fi : (Collection<FileInfo>) ((MultiValueMap) files).getCollection(key)) {
                                if (host.equals(fi.getOwnerHost())) {
                                    files.remove(key, fi);
                                }
                            }
                        }

                        out.write(FishMessageType.SERVER_OK.name());
                        out.newLine();
                        out.flush();
                        break;
                    case CLIENT_FIND_ALL:
                        break;
                    case CLIENT_FIND:
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

    public void setAlive(boolean alive) {
        this.alive = alive;
    }
}
