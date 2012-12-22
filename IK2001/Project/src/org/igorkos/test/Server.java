package org.igorkos.test;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static final int PORT = 8080;
    private static ServerSocket serverSocket;

    public static void main(String[] args) throws IOException {
        serverSocket = new ServerSocket(PORT);
        System.out.println("Waiting for connections...");
        while(true) {
            Socket socket = serverSocket.accept();
            System.out.println("Accepted connection from "+socket.getInetAddress().getHostAddress());
            socket.close();
        }
    }
}
