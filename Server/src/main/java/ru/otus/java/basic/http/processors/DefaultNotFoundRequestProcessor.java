package ru.otus.java.basic.http.processors;

import ru.otus.java.basic.http.HttpRequest;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class DefaultNotFoundRequestProcessor implements RequestProcessor{
    @Override
    public void execute(HttpRequest httpRequest, OutputStream out) throws IOException {
        String response = """
                    HTTP/1.1 404 Not Found
                    Content-type: text/html
                    
                    <html><body><h1>Page Not Found</h1></body></html>""";

        out.write(response.getBytes(StandardCharsets.UTF_8));
    }
}
