package ru.otus.java.basic.http;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private static final Logger logger = LogManager.getLogger(Server.class.getName());

    private final int port;
    private final ExecutorService connectionsPool;
    private final Dispatcher dispatcher;

    public Server(int port) {
        this.port = port;
        this.connectionsPool = Executors.newCachedThreadPool();
        this.dispatcher = new Dispatcher();
    }

    public void start() {
        try(ServerSocket serverSocket = new ServerSocket(port)) {
            logger.info("Server started to port {}", port);
            while (true) {
                Socket connection = serverSocket.accept();
                new RequestHandler(connectionsPool, connection, dispatcher);
            }
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        } finally {
            connectionsPool.shutdown();
        }
    }
}