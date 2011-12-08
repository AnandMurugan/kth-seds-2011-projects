/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fish.common;

import fish.common.FishMessageType;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Igor
 */
public class DownloadRequestHandler extends Thread {
    private Socket clientSocket;
    private Map<String, File> sharedFiles;

    /**
     * 
     * @param clientSocket
     * @param sharedFiles
     */
    public DownloadRequestHandler(Socket clientSocket, Map<String, File> sharedFiles) {
        this.clientSocket = clientSocket;
        this.sharedFiles = sharedFiles;
    }

    @Override
    public void run() {
        BufferedReader clientIn = null;
        BufferedWriter clientOut = null;
        FileInputStream fileIn = null;
        OutputStream fileOut = null;
        try {
            clientIn = new BufferedReader(
                    new InputStreamReader(clientSocket.getInputStream()));
            clientOut = new BufferedWriter(
                    new OutputStreamWriter(clientSocket.getOutputStream()));

            String request = clientIn.readLine();
            StringTokenizer st = new StringTokenizer(request, ";");
            String type = st.nextToken();
            if ((st.countTokens() < 1)
                    || (FishMessageType.PEER_DOWNLOAD != FishMessageType.valueOf(type))) {
                clientOut.write(FishMessageType.PEER_ERROR.toString());
                clientOut.newLine();
                clientOut.flush();

                clientIn.close();
                clientOut.close();
                return;
            }

            String key = st.nextToken();
            File file = sharedFiles.get(key);
            if (file == null) {
                clientOut.write(FishMessageType.PEER_ERROR.toString());
                clientOut.newLine();
                clientOut.flush();

                clientIn.close();
                clientOut.close();
                return;
            } else {
                clientOut.write(FishMessageType.PEER_OK.toString());
                clientOut.newLine();
                clientOut.flush();
            }
            fileIn = new FileInputStream(file);
            fileOut = clientSocket.getOutputStream();
            byte[] buf = new byte[1024];
            int len;
            while ((len = fileIn.read(buf)) > 0) {
                fileOut.write(buf, 0, len);
                fileOut.flush();
            }
        } catch (IOException ex) {
            Logger.getLogger(DownloadRequestHandler.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (fileIn != null) {
                    fileIn.close();
                }
                if (fileOut != null) {
                    fileOut.close();
                }
                if (clientIn != null) {
                    clientIn.close();
                }
                if (clientOut != null) {
                    clientOut.close();
                }

                clientSocket.close();
            } catch (IOException ex) {
                Logger.getLogger(DownloadRequestHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
