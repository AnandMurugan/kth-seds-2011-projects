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
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.collections.MultiMap;
import org.apache.commons.collections.map.MultiValueMap;

/**
 *
 * @author julio
 */
public class ClientConnectionHandler extends Thread {
    private Socket clientSocket;
    private final MultiMap files;
    private boolean alive = true;

    public ClientConnectionHandler(Socket clientSocket, MultiMap files) {
        this.clientSocket = clientSocket;
        this.files = files;
    }

    @Override
    public void run() {
        System.out.println("Handling new client started.");
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
                            List<File> clientFiles = (List<File>) listIn.readObject();
                            System.out.println(Integer.toString(clientSocket.getInputStream().available()));
                            String host = clientSocket.getInetAddress().getHostAddress();
                            synchronized (files) {
                                for (File f : clientFiles) {
                                    FileInfo fi = new FileInfo(host, f);
                                    if (!((MultiValueMap) files).containsValue(f.getName(), fi)) {
                                        files.put(f.getName(), fi);
                                    }
                                }
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

                        synchronized (files) {
                            //TODO resolve concurrent issue
                            for (String key : (Set<String>) files.keySet()) {
                                for (FileInfo fi : (Collection<FileInfo>) ((MultiValueMap) files).getCollection(key)) {
                                    if (host.equals(fi.getOwnerHost())) {
                                        files.remove(key, fi);
                                    }
                                }
                            }
                        }

                        out.write(FishMessageType.SERVER_OK.name());
                        out.newLine();
                        out.flush();
                        break;
                    case CLIENT_FIND_ALL:
//                        out.write(FishMessageType.SERVER_OK.name());
//                        out.newLine();
//                        out.flush();

                        listOut = new ObjectOutputStream(clientSocket.getOutputStream());
                        listOut.reset();
                        synchronized (files) {
                            listOut.writeObject(new ArrayList((Collection<FileInfo>) files.values()));
                        }
                        break;
                    case CLIENT_FIND:
                        String mask = tokens.nextToken();

//                        out.write(FishMessageType.SERVER_OK.name());
//                        out.newLine();
//                        out.flush();

                        List<FileInfo> list = new ArrayList<FileInfo>();
                        synchronized (files) {
                            for (String name : (Set<String>) files.keySet()) {
                                if (match(name, mask)) {
                                    list.addAll(((MultiValueMap) files).getCollection(name));
                                }
                            }
                        }

                        listOut = new ObjectOutputStream(clientSocket.getOutputStream());
                        listOut.reset();
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

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    private boolean match(String name, String mask) {
        //return name.equalsIgnoreCase(mask);
        Pattern p = Pattern.compile(mask);
        Matcher m = p.matcher(name);

        return m.find();
    }
}
