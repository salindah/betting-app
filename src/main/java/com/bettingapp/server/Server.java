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
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Collectors;

/**
 * Created with IntelliJ IDEA.
 * User: shettiarachchi
 * Date: 20/10/18
 * Time: 12:47 AM
 * To change this template use File | Settings | File Templates.
 */
public class Server {

    private static final Integer SERVER_PORT = 8001;

    private static final Integer THREAD_POOL_SIZE = 20;

    private static ThreadPoolExecutor executor = null;

    public static void startServer() {

        try {
            initializeThreadPool();
            HttpServer server = HttpServer.create(new InetSocketAddress(SERVER_PORT), 0);
            HttpContext context = server.createContext("/");
            context.setHandler(Server::handleRequest);
            server.start();
            System.out.println("HTTP server started...");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private static void initializeThreadPool(){
        executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(THREAD_POOL_SIZE);
    }

    private static void handleRequest(HttpExchange exchange) throws IOException {
        executor.submit(new RequestHandler(exchange));
    }
}
