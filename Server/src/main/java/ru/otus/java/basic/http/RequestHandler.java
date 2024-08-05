package ru.otus.java.basic.http;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.concurrent.ExecutorService;

public class RequestHandler {
    private static final Logger logger = LogManager.getLogger(RequestHandler.class.getName());

    private final byte[] buffer;
    private final Socket connection;
    private final OutputStream out;
    private final InputStream in;

    public RequestHandler(ExecutorService pool, Socket connection, Dispatcher dispatcher) throws IOException {
        this.buffer = new byte[8192];
        this.connection = connection;
        this.out = connection.getOutputStream();
        this.in = connection.getInputStream();

        pool.execute(()-> {
            try {
                int n = in.read(buffer);
                if (n < 1) {
                    return;
                }
                String rawRequest = new String(buffer, 0, n);

                HttpRequest httpRequest = new HttpRequest(rawRequest);

                dispatcher.execute(httpRequest, out);
            } catch (IOException e) {
                logger.error(e.getMessage(), e);
            } finally {
                disconnect();
            }
        });
    }

    private void disconnect() {
        if (in != null) {
            try {
                in.close();
            } catch (IOException e) {
                logger.error(e.getMessage(), e);
            }
        }

        if (out != null) {
            try {
                out.close();
            } catch (IOException e) {
                logger.error(e.getMessage(), e);
            }
        }

        try {
            connection.close();
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
    }
}