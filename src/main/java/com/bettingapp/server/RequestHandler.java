package com.bettingapp.server;

import com.bettingapp.betting.BettingService;
import com.bettingapp.common.Utils;
import com.bettingapp.common.model.RequestDetails;
import com.bettingapp.common.model.ResponseDetails;
import com.bettingapp.service.Service;
import com.bettingapp.session.SessionService;
import com.sun.net.httpserver.HttpExchange;

import java.io.*;
import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created with IntelliJ IDEA.
 * User: shettiarachchi
 * Date: 25/10/18
 * Time: 6:12 PM
 * To change this template use File | Settings | File Templates.
 */
public class RequestHandler implements Runnable {

    public static final String REQUEST_METHOD_GET = "GET";

    public static final String REQUEST_METHOD_POST = "POST";

    public static final List<String> GET_KEYWORD_LIST = Arrays.asList("session", "highstakes");

    public static final List<String> POST_KEYWORD_LIST = Arrays.asList("stake");

    private HttpExchange exchange;

    public RequestHandler(HttpExchange exchange) {
        this.exchange = exchange;
    }

    @Override
    public void run() {
        RequestDetails requestDetails;
        ResponseDetails response = null;
        OutputStream os = null;

        try {
            requestDetails = getRequestDetailsObj(exchange);

            if (REQUEST_METHOD_GET.equals(requestDetails.getType())) {
                response = handleGet(requestDetails);
            } else if (REQUEST_METHOD_POST.equals(requestDetails.getType())) {
                response = handlePost(requestDetails);
            }

            exchange.sendResponseHeaders(response.getCode(), response.getBody().getBytes().length);
            os = exchange.getResponseBody();
            os.write(response.getBody().getBytes());

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private RequestDetails getRequestDetailsObj(HttpExchange exchange) {

        RequestDetails requestDetails = new RequestDetails();
        URI requestURI = exchange.getRequestURI();

        requestDetails.setType(exchange.getRequestMethod());
        String[] variables = Utils.getPathVariables(requestURI.getPath());
        if (variables != null && variables.length > 2) {
            requestDetails.setId(Integer.valueOf(variables[1]));
            requestDetails.setAction(variables[2]);
        }

        if (requestURI.getQuery() != null) {
            requestDetails.setQueryParameterMap(Utils.getQueryParameterMap(requestURI.getQuery()));
        }
        if (exchange.getRequestBody() != null) {
            requestDetails.setContent(getRequestBodyAsString(exchange.getRequestBody()));
        }
        return requestDetails;
    }

    private String getRequestBodyAsString(InputStream inputStream) {
        String content = "";
        BufferedReader bufferedReader = null;
        InputStreamReader inputStreamReader = null;
        try {

            inputStreamReader = new InputStreamReader(inputStream);
            bufferedReader = new BufferedReader(inputStreamReader);
            content = bufferedReader.lines().collect(Collectors.joining());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStreamReader != null) {
                    inputStreamReader.close();
                }

                if (bufferedReader != null) {
                    bufferedReader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return content;
    }

    private static ResponseDetails handleGet(RequestDetails requestDetails) {
        ResponseDetails response;
        Service service = null;
        if (GET_KEYWORD_LIST.contains(requestDetails.getAction())) {
            if ("session".equalsIgnoreCase(requestDetails.getAction())) {
                service = SessionService.getInstance();
            } else if ("highstakes".equalsIgnoreCase(requestDetails.getAction())) {
                service = BettingService.getInstance();
            }
        }
        response = service.serveRequest(requestDetails);
        return response;
    }

    private static ResponseDetails handlePost(RequestDetails requestDetails) {
        ResponseDetails response;
        Service service = null;
        if (POST_KEYWORD_LIST.contains(requestDetails.getAction())) {
            if ("stake".equalsIgnoreCase(requestDetails.getAction())) {
                service = BettingService.getInstance();
            }
        }
        response = service.serveRequest(requestDetails);
        return response;
    }
}
