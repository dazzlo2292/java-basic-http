package ru.otus.java.basic.http.processors;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.otus.java.basic.http.BadRequestException;
import ru.otus.java.basic.http.HttpRequest;
import ru.otus.java.basic.http.app.Item;
import ru.otus.java.basic.http.app.ItemsRepository;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class CreateNewItemRequestProcessor implements RequestProcessor{
    private ItemsRepository itemsRepository;

    public CreateNewItemRequestProcessor(ItemsRepository itemsRepository) {
        this.itemsRepository = itemsRepository;
    }

    @Override
    public void execute(HttpRequest httpRequest, OutputStream out) throws IOException {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Item newItem = itemsRepository.add(objectMapper.readValue(httpRequest.getBody(), Item.class));

            String itemJson = objectMapper.writeValueAsString(newItem);

            String response =
                    "HTTP/1.1 201 Created\r\n" +
                            "Content-type: application/json\r\n" +
                            "\r\n" +
                            itemJson;

            out.write(response.getBytes(StandardCharsets.UTF_8));
        } catch (JsonParseException e) {
            e.printStackTrace();
            throw new BadRequestException("Incorrect JSON format");
        }
    }
}
