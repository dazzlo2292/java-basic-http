package ru.otus.java.basic.http;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.otus.java.basic.http.app.ItemsRepository;
import ru.otus.java.basic.http.processors.*;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

public class Dispatcher {
    private static final Logger logger = LogManager.getLogger(Server.class.getName());

    private final Map<String, RequestProcessor> processors;
    private final RequestProcessor defaultNotFoundRequestProcessor;
    private final RequestProcessor defaultInternalServerRequestProcessor;
    private final RequestProcessor defaultBadRequestProcessor;

    public Dispatcher() {
        ItemsRepository itemsRepository = new ItemsRepository();

        this.processors = new HashMap<>();
        this.defaultNotFoundRequestProcessor = new DefaultNotFoundRequestProcessor();
        this.defaultInternalServerRequestProcessor = new DefaultInternalServerErrorRequestProcessor();
        this.defaultBadRequestProcessor = new DefaultBadRequestProcessor();
        processors.put("GET /", new RootRequestProcessor());
        processors.put("GET /ping", new PingRequestProcessor());
        processors.put("GET /calc", new CalculatorRequestProcessor());
        processors.put("GET /items", new GetAllItemsRequestProcessor(itemsRepository));
        processors.put("POST /items", new CreateNewItemRequestProcessor(itemsRepository));
        processors.put("DELETE /items", new DeleteItemRequestProcessor(itemsRepository));
    }

    public void execute(HttpRequest httpRequest, OutputStream out) throws IOException {
        try {
            if (!processors.containsKey(httpRequest.getRoutingKey())) {
                defaultNotFoundRequestProcessor.execute(httpRequest, out);
                return;
            }
            processors.get(httpRequest.getRoutingKey()).execute(httpRequest, out);
        } catch (BadRequestException e) {
            logger.error(e.getMessage(), e);
            DefaultBadRequestProcessor.setErrorText(new DefaultErrorDto("request_error",e.getMessage()));
            defaultBadRequestProcessor.execute(httpRequest, out);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            defaultInternalServerRequestProcessor.execute(httpRequest, out);
        }
    }
}
