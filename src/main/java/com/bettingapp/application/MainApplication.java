package com.bettingapp.application;

import com.bettingapp.server.Server;

/**
 * Created with IntelliJ IDEA.
 * User: shettiarachchi
 * Date: 20/10/18
 * Time: 12:48 AM
 * This acts as the main entry point for the system and start the server as the first step.
 */
public class MainApplication {

    public static void main(String[] args) {

        Server.startServer();
    }
}
