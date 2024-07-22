package ru.otus.java.basic.http;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.concurrent.ExecutorService;

public class RequestHandler {
    private final byte[] buffer;
    private final Dispatcher dispatcher;
    private final OutputStream out;
    private final InputStream in;

    public RequestHandler(ExecutorService pool, Socket connection) throws IOException {
        this.buffer = new byte[8192];
        this.dispatcher = new Dispatcher();
        this.out = connection.getOutputStream();
        this.in = connection.getInputStream();

        pool.execute(()-> {
            try {
                int n = in.read(buffer);
                String rawRequest = new String(buffer, 0, n);

                HttpRequest httpRequest = new HttpRequest(rawRequest);
                httpRequest.printInfo(false);

                dispatcher.execute(httpRequest, out);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                if (out != null) {
                    try {
                        out.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                try {
                    connection.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}