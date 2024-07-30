package ru.otus.java.basic.http.processors;

import ru.otus.java.basic.http.BadRequestException;
import ru.otus.java.basic.http.HttpRequest;
import ru.otus.java.basic.http.app.ItemsRepository;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class DeleteItemRequestProcessor implements RequestProcessor{

    private final ItemsRepository itemsRepository;

    public DeleteItemRequestProcessor(ItemsRepository itemsRepository) {
        this.itemsRepository = itemsRepository;
    }

    @Override
    public void execute(HttpRequest httpRequest, OutputStream out) throws IOException {
            if (httpRequest.getCountParameters() != 1) {
                throw new BadRequestException("Expected one parameter \"id\"");
            }
            if (!httpRequest.containsParameter("id")) {
                throw new BadRequestException("Parameter \"id\" is missing!");
            }
            long id;
            try {
                id = Long.parseLong(httpRequest.getParameter("id"));
                itemsRepository.delete(id);
            } catch (NumberFormatException e) {
                throw new BadRequestException("Parameter \"id\" has incorrect type!");
            }

            String response = """
                            HTTP/1.1 200 OK\r
                            Content-type: text/json\r
                            \r
                            {
                               code:ok\
                            }""";

            out.write(response.getBytes(StandardCharsets.UTF_8));
    }
}
