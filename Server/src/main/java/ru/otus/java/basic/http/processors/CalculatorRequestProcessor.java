package ru.otus.java.basic.http.processors;

import ru.otus.java.basic.http.BadRequestException;
import ru.otus.java.basic.http.HttpRequest;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class CalculatorRequestProcessor implements RequestProcessor{
    @Override
    public void execute(HttpRequest httpRequest, OutputStream out) throws IOException {
        if (!httpRequest.containsParameter("x")) {
            throw new BadRequestException("Parameter \"x\" is missing!");
        }
        if (!httpRequest.containsParameter("y")) {
            throw new BadRequestException("Parameter \"y\" is missing!");
        }
        int x,y;
        try {
            x = Integer.parseInt(httpRequest.getParameter("x"));
        } catch (NumberFormatException e) {
            throw new BadRequestException("Parameter \"x\" has incorrect type!");
        }
        try {
            y = Integer.parseInt(httpRequest.getParameter("y"));
        } catch (NumberFormatException e) {
            throw new BadRequestException("Parameter \"y\" has incorrect type!");
        }

        String result = x + " + " + y + " = " + (x + y);

        String response =
                    "HTTP/1.1 200 OK\n\r" +
                    "Content-type: text/html\n\r" +
                    "\n\r" +
                    "<html><body><h1>" + result + "</h1></body></html>";

        out.write(response.getBytes(StandardCharsets.UTF_8));
    }
}
