package ru.otus.java.basic.http.processors;

import ru.otus.java.basic.http.HttpRequest;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class RootRequestProcessor implements RequestProcessor{
    @Override
    public void execute(HttpRequest httpRequest, OutputStream out) throws IOException {
        String response = """
                    HTTP/1.1 200 OK
                    Content-type: text/html
                    
                    <html><body><h1>Root page</h1></body></html>""";

        out.write(response.getBytes(StandardCharsets.UTF_8));
    }
}
