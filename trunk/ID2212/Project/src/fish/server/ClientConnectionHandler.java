/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fish.server;

import fish.common.FishMessageType;
import java.awt.TrayIcon.MessageType;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.collections.MultiMap;

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
        try {
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));

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
                        break;
                    case CLIENT_UNSHARE:
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
