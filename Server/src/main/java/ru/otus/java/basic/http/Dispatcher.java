package ru.otus.java.basic.http;

import ru.otus.java.basic.http.app.ItemsRepository;
import ru.otus.java.basic.http.processors.*;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

public class Dispatcher {
    private final Map<String, RequestProcessor> processors;
    private final RequestProcessor defaultNotFoundRequestProcessor;
    private final RequestProcessor defaultInternalServerRequestProcessor;
    private final RequestProcessor defaultBadRequestProcessor;

    private ItemsRepository itemsRepository;

    public Dispatcher() {
        this.itemsRepository = new ItemsRepository();

        this.processors = new HashMap<>();
        this.defaultNotFoundRequestProcessor = new DefaultNotFoundRequestProcessor();
        this.defaultInternalServerRequestProcessor = new DefaultInternalServerErrorRequestProcessor();
        this.defaultBadRequestProcessor = new DefaultBadRequestProcessor();
        processors.put("GET /", new RootRequestProcessor());
        processors.put("GET /ping", new PingRequestProcessor());
        processors.put("GET /calc", new CalculatorRequestProcessor());
        processors.put("GET /items", new GetAllItemsRequestProcessor(itemsRepository));
        processors.put("POST /items", new CreateNewItemRequestProcessor(itemsRepository));
    }

    public void execute(HttpRequest httpRequest, OutputStream out) throws IOException {
        try {
            if (!processors.containsKey(httpRequest.getRoutingKey())) {
                defaultNotFoundRequestProcessor.execute(httpRequest, out);
                return;
            }
            processors.get(httpRequest.getRoutingKey()).execute(httpRequest, out);
        } catch (BadRequestException e) {
            e.printStackTrace();
            DefaultBadRequestProcessor.setErrorText(new DefaultErrorDto("request_error",e.getMessage()));
            defaultBadRequestProcessor.execute(httpRequest, out);
        } catch (Exception e) {
            e.printStackTrace();
            defaultInternalServerRequestProcessor.execute(httpRequest, out);
        }
    }
}
