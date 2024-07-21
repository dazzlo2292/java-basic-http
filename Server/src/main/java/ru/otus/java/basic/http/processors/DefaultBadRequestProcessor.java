package ru.otus.java.basic.http.processors;

import ru.otus.java.basic.http.HttpRequest;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class DefaultBadRequestProcessor implements RequestProcessor{
    private static String errorText;

    public static void setErrorText(String error) {
        errorText = error;
    }

    @Override
    public void execute(HttpRequest httpRequest, OutputStream out) throws IOException {
        String response =
                    "HTTP/1.1 400 Bad Request\n\r" +
                    "Content-type: text/json\n\r" +
                    "\n\r" +
                    "{\n\r" +
                    "   errorText : " + errorText +
                    "\n\r}";

        out.write(response.getBytes(StandardCharsets.UTF_8));
    }
}
