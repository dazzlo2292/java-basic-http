package ru.otus.java.basic.http.processors;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.otus.java.basic.http.BadRequestException;
import ru.otus.java.basic.http.HttpRequest;
import ru.otus.java.basic.http.app.Item;
import ru.otus.java.basic.http.app.ItemsRepository;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class GetAllItemsRequestProcessor implements RequestProcessor{
    private ItemsRepository itemsRepository;

    public GetAllItemsRequestProcessor(ItemsRepository itemsRepository) {
        this.itemsRepository = itemsRepository;
    }

    @Override
    public void execute(HttpRequest httpRequest, OutputStream out) throws IOException {
        List<Item> items = itemsRepository.getItems();
        ObjectMapper objectMapper = new ObjectMapper();
        String itemsJson = objectMapper.writeValueAsString(items);

        String response =
                    "HTTP/1.1 200 OK\r\n" +
                    "Content-type: application/json\r\n" +
                    "\r\n" +
                    itemsJson;

        out.write(response.getBytes(StandardCharsets.UTF_8));
    }
}
