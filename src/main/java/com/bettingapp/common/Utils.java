package com.bettingapp.common;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpPrincipal;

import java.net.URI;
import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: shettiarachchi
 * Date: 22/10/18
 * Time: 10:52 PM
 * To change this template use File | Settings | File Templates.
 */
public class Utils {

    public static HashMap<String, String> getQueryParameterMap(String query){
        HashMap<String, String> map = new HashMap<>();

        String[] queryArray = query.split("&");
        for(String parameter : queryArray){
            String[] keyValue = parameter.split("=");
            map.put(keyValue[0], keyValue[1]);
        }
        return map;
    }

    public static String[] getPathVariables(String path){
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
