package org.igorkos.test;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static final int DEFAULT_PORT = 8080;

    private static ServerSocket serverSocket;

    public static void main(String[] args) throws IOException {
        int port = DEFAULT_PORT;
        if (args.length > 0) {
            port = Integer.parseInt(args[0]);
        }

        serverSocket = new ServerSocket(port);
        System.out.println("Waiting for connections on port " + port + "...");
        while (true) {
            Socket socket = serverSocket.accept();
            System.out.println("Accepted connection from " + socket.getInetAddress().getHostAddress());
            socket.close();
        }
    }
}
