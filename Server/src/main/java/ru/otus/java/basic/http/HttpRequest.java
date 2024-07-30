package ru.otus.java.basic.http;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

public class HttpRequest {
    private static final Logger logger = LogManager.getLogger(Server.class.getName());

    private final String request;
    private String endpoint;
    private HttpMethod method;
    private String body;
    private Map<String, String> parameters;
    private Map<String, String> headers;

    public String getRoutingKey() {
        return method + " " + endpoint;
    }

    public String getBody() {
        return body;
    }

    public String getHeader(String key) {
        return headers.get(key);
    }

    public String getParameter(String key) {
        return parameters.get(key);
    }

    public boolean containsParameter(String key) {
        return parameters.containsKey(key);
    }

    public int getCountParameters() {
        return parameters.size();
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
        this.headers = new HashMap<>();

        int startHeadersIndex = rawRequest.indexOf("\r\n") + 2;
        int endHeadersIndex = rawRequest.indexOf("\r\n\r\n");
        String allHeaders = rawRequest.substring(startHeadersIndex, endHeadersIndex);
        String[] differentHeaders = allHeaders.split("\r\n");
        for (String header : differentHeaders) {
            String headerKey;
            String headerValue;
            String[] keyValue = header.split(":");
            headerKey = keyValue[0].trim();
            if (headerKey.equals("Host")) {
                headerValue = keyValue[1].trim() + ":" + keyValue[2].trim();
                headers.put(headerKey, headerValue);
                continue;
            }
            headerValue = keyValue[1].trim();
            headers.put(headerKey, headerValue);
        }

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
        if (this.method == HttpMethod.POST) {
            this.body = rawRequest.substring(rawRequest.indexOf("\r\n\r\n") + 4);
        }

        logger.info("\nMethod: {}\nURL: {}\nBody: \n{}", method, endpoint, body);
        logger.debug(request);
    }
}
