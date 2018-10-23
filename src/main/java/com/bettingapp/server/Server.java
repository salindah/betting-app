package com.bettingapp.server;

import com.bettingapp.betting.BettingService;
import com.bettingapp.common.model.RequestDetails;
import com.bettingapp.common.model.ResponseDetails;
import com.bettingapp.service.Service;
import com.bettingapp.session.SessionService;
import com.sun.net.httpserver.*;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.URI;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created with IntelliJ IDEA.
 * User: shettiarachchi
 * Date: 20/10/18
 * Time: 12:47 AM
 * To change this template use File | Settings | File Templates.
 */
public class Server {

    public static final String REQUEST_METHOD_GET = "GET";

    public static final String REQUEST_METHOD_POST = "POST";

    public static final List<String>  GET_KEYWORD_LIST = Arrays.asList("session", "highstakes");

    public static final List<String>  POST_KEYWORD_LIST = Arrays.asList("stake");

    public static void startServer() {

        try {
            HttpServer server = HttpServer.create(new InetSocketAddress(8001), 0);
            HttpContext context = server.createContext("/");
            context.setHandler(Server::handleRequest);
            server.start();
            System.out.println("HTTP server started...");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void handleRequest(HttpExchange exchange) throws IOException {
        RequestDetails requestDetails = getRequestDetailsObj(exchange);
        ResponseDetails response = null;
        if(REQUEST_METHOD_GET.equals(requestDetails.getType())){
            response = handleGet(requestDetails);
        } else if (REQUEST_METHOD_POST.equals(requestDetails.getType())){
            response = handlePost(requestDetails);
        }

        exchange.sendResponseHeaders(response.getCode(), response.getBody().getBytes().length);//response code and length
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBody().getBytes());
        os.close();
    }

    private static RequestDetails getRequestDetailsObj(HttpExchange exchange){

        RequestDetails requestDetails = new RequestDetails();
        URI requestURI = exchange.getRequestURI();

        requestDetails.setType(exchange.getRequestMethod());
        String[] variables =  getPathVariables(requestURI.getPath());
        if(variables != null && variables.length > 2){
            requestDetails.setId(Integer.valueOf(variables[1]));
            requestDetails.setAction(variables[2]);
        }

        if(requestURI.getQuery() != null){
            requestDetails.setQueryParameterMap(getQueryParameterMap(requestURI.getQuery()));
        }
        if(exchange.getRequestBody() != null){
            requestDetails.setContent(getRequestBodyAsString(exchange.getRequestBody()));
        }

        System.out.println(requestURI.getQuery());
        System.out.println(requestDetails.getQueryParameterMap());
        return requestDetails;
    }

    private static String getRequestBodyAsString(InputStream inputStream){
        String content = "";
        BufferedReader bufferedReader = null;
        InputStreamReader inputStreamReader = null;
        try{

            inputStreamReader = new InputStreamReader(inputStream);
            bufferedReader = new BufferedReader(inputStreamReader);
            content = bufferedReader.lines().collect(Collectors.joining());
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            try {
                if(inputStreamReader != null){
                    inputStreamReader.close();
                }

                if(bufferedReader != null){
                    bufferedReader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return content;
    }

    private static HashMap<String, String> getQueryParameterMap(String query){
        HashMap<String, String> map = new HashMap<>();

        String[] queryArray = query.split("&");
        for(String parameter : queryArray){
            String[] keyValue = parameter.split("=");
            map.put(keyValue[0], keyValue[1]);
        }
        return map;
    }

    private static ResponseDetails handleGet(RequestDetails requestDetails){
        ResponseDetails response;
        Service service = null;
        if(GET_KEYWORD_LIST.contains(requestDetails.getAction())){
            if("session".equalsIgnoreCase(requestDetails.getAction())){
                service = SessionService.getInstance();
            }
        }
        response = service.serveRequest(requestDetails);
        System.out.println("response= " + response);
        return response;
    }

    private static ResponseDetails handlePost(RequestDetails requestDetails){
        ResponseDetails response;
        Service service = null;
        if(POST_KEYWORD_LIST.contains(requestDetails.getAction())){
            if("stake".equalsIgnoreCase(requestDetails.getAction())){
                service = BettingService.getInstance();
            }
        }
        response = service.serveRequest(requestDetails);
        System.out.println("response= " + response);
        return response;
    }


    private static String[] getPathVariables(String path){
        if(path != null && path.length() > 1){
           return path.split("/");
        }
        return null;
    }

    private static void printRequestInfo(HttpExchange exchange) {

        System.out.println("-- headers --");
        Headers requestHeaders = exchange.getRequestHeaders();
        requestHeaders.entrySet().forEach(System.out::println);

        System.out.println("-- principle --");
        HttpPrincipal principal = exchange.getPrincipal();
        System.out.println(principal);

        System.out.println("-- HTTP method --");
        String requestMethod = exchange.getRequestMethod();
        System.out.println(requestMethod);

        System.out.println("-- query --");
        URI requestURI = exchange.getRequestURI();
        String query = requestURI.getQuery();
        System.out.println(query);

        System.out.println("-- path --");
        System.out.println(requestURI.getPath());

        exchange.getRequestBody();
    }
}
