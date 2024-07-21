package ru.otus.java.basic.http;

import java.util.HashMap;
import java.util.Map;

public class HttpRequest {
    private final String request;
    private String endpoint;
    private HttpMethod method;
    private Map<String, String> parameters;

    public String getEndpoint() {
        return endpoint;
    }

    public String getParameter(String key) {
        return parameters.get(key);
    }

    public boolean containsParameter(String key) {
        return parameters.containsKey(key);
    }

    public HttpRequest(String request) {
        this.request = request;
        parse(request);
    }

    private void parse(String rawRequest) {
        int startIndex = rawRequest.indexOf(" ");
        int endIndex = rawRequest.indexOf(' ', startIndex + 1);
        this.method = HttpMethod.valueOf(rawRequest.substring(0, startIndex));
        this.endpoint = rawRequest.substring(startIndex + 1, endIndex);
        this.parameters = new HashMap<>();
        if (endpoint.contains("?")) {
            String[] rawParams = endpoint.split("[?]");
            this.endpoint = rawParams[0];
            String[] differentParams = rawParams[1].split("&");
            for (String param : differentParams)
            {
                String[] keyValue = param.split("=");
                this.parameters.put(keyValue[0], keyValue[1]);
            }
        }
    }

    public void printInfo(boolean rawRequest) {
        System.out.println("Method: " + method + "\nURL: " + endpoint);
        if (rawRequest) {
            System.out.println(request);
        }
    }
}
