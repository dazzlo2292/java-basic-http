package ru.otus.java.basic.http;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private final int port;
    private final Dispatcher dispatcher;

    public Server(int port) {
        this.port = port;
        this.dispatcher = new Dispatcher();
    }

    public void start() {
        try(ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server started to port " + port);
            try (Socket connection = serverSocket.accept()) {
                byte[] buffer = new byte[8192];
                int n = connection.getInputStream().read(buffer);
                String rawRequest = new String(buffer, 0, n);

                HttpRequest httpRequest = new HttpRequest(rawRequest);
                httpRequest.printInfo(false);

                dispatcher.execute(httpRequest, connection.getOutputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
