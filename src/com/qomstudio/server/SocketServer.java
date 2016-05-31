package com.qomstudio.server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by robin on 5/31/16.
 */
public class SocketServer {
    public static void main(String[] args) {
        SocketServer socketServer = new SocketServer();
        socketServer.startServer();
    }

    private void startServer() {
        ServerSocket serverSocket = null;
        Socket socket = null;

        try {
            serverSocket = new ServerSocket(9090);
            System.out.println("Server started...");
            while (true) {
                socket = serverSocket.accept();
                manageConnection(socket);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void manageConnection(final Socket socket) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                BufferedReader reader = null;
                BufferedWriter writer = null;
                try {
                    System.out.println("client " + socket.hashCode() + " connected");
                    reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                    String receivedMsg;
                    while ((receivedMsg = reader.readLine()) != null) {
                        System.out.println("client " + socket.hashCode() + ": " + receivedMsg);
                        writer.write("server reply: " + receivedMsg + "\n");
                        writer.flush();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        reader.close();
                        writer.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }
}
