package ru.otus.java.basic.http.processors;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.otus.java.basic.http.DefaultErrorDto;
import ru.otus.java.basic.http.HttpRequest;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class DefaultBadRequestProcessor implements RequestProcessor{
    private static DefaultErrorDto error;

    public static void setErrorText(DefaultErrorDto newError) {
        error = newError;
    }

    @Override
    public void execute(HttpRequest httpRequest, OutputStream out) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        String errorJson = objectMapper.writeValueAsString(error);

        String response =
                    "HTTP/1.1 400 Bad Request\r\n" +
                    "Content-type: application/json\r\n" +
                    "\r\n" +
                    errorJson;

        out.write(response.getBytes(StandardCharsets.UTF_8));
    }
}
