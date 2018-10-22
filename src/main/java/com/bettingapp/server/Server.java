package com.bettingapp.server;

import com.bettingapp.service.Service;
import com.bettingapp.session.SessionService;
import com.sun.net.httpserver.*;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.util.Arrays;
import java.util.List;

/**
 * Created with IntelliJ IDEA. User: shettiarachchi Date: 20/10/18 Time: 12:47 AM To change this
 * template use File | Settings | File Templates.
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

        URI requestURI = exchange.getRequestURI();
        String requestMethod = exchange.getRequestMethod();

        String response = "Test";
        if(requestMethod.equals(REQUEST_METHOD_GET)){
            response = handleGet(requestURI.getPath());
        } else if (requestMethod.equals(REQUEST_METHOD_POST)){
            response = handlePost(requestURI.getPath());
        }

        //printRequestInfo(exchange);


        exchange.sendResponseHeaders(200, response.getBytes().length);//response code and length
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

    private static String handleGet(String path){
        String[] variables =  getPathVariables(path);
        String response = "";
        if(variables != null && variables.length > 2){
            String id = variables[1];
            String action = variables[2];

            Service service = null;
            if(GET_KEYWORD_LIST.contains(action)){
                if( action.equalsIgnoreCase("session")){
                    service = SessionService.getInstance();
                }
            }

            response = service.serveRequest(Integer.valueOf(id), action);
            System.out.println("response= " + response);
        }
        return response;
    }

    private static String handlePost(String path){
        String[] variables =  getPathVariables(path);
        if(variables != null && variables.length > 1){
            System.out.println(variables);
            String id = variables[1];
            String action = variables[2];

            if(POST_KEYWORD_LIST.contains(action)){


            }

            System.out.println("id= " + id);
            System.out.println("action= " + action);


        }
        return "";
    }


    private static String[] getPathVariables(String path){
        if(path != null && path.length() > 1){
           return path.split("/");
        }
        return null;
    }


    private void delegateAction(String action, int id){


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
