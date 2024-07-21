package ru.otus.java.basic.http;

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

    public Dispatcher() {
        this.processors = new HashMap<>();
        this.defaultNotFoundRequestProcessor = new DefaultNotFoundRequestProcessor();
        this.defaultInternalServerRequestProcessor = new DefaultInternalServerErrorRequestProcessor();
        this.defaultBadRequestProcessor = new DefaultBadRequestProcessor();
        processors.put("/", new RootRequestProcessor());
        processors.put("/ping", new PingRequestProcessor());
        processors.put("/calc", new CalculatorRequestProcessor());
    }

    public void execute(HttpRequest httpRequest, OutputStream out) throws IOException {
        try {
            if (!processors.containsKey(httpRequest.getEndpoint())) {
                defaultNotFoundRequestProcessor.execute(httpRequest, out);
                return;
            }
            processors.get(httpRequest.getEndpoint()).execute(httpRequest, out);
        } catch (BadRequestException e) {
            e.printStackTrace();
            DefaultBadRequestProcessor.setErrorText(e.getMessage());
            defaultBadRequestProcessor.execute(httpRequest, out);
        } catch (Exception e) {
            e.printStackTrace();
            defaultInternalServerRequestProcessor.execute(httpRequest, out);
        }
    }
}
