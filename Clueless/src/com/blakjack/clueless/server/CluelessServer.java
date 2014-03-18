/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.blakjack.clueless.server;

import com.blakjack.clueless.Connection;
import com.blakjack.clueless.Connection.MessageHandler;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

/**
 * This Main class starts up a socket acceptor and handles new connections
 * 
 * @author nauglrj1
 */
public class CluelessServer {
    
    private static final int PORT = 10001;
    
    private static CluelessServer server;
    
    private Thread acceptorThread = null;
    private Server serverthingy = new Server();
    
    private final Map<String, Connection> connections = new HashMap<String, Connection>();
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        System.out.println("Starting server...");
        
        server = new CluelessServer();
        server.start();
    }
    
    public CluelessServer() {
        try {
            InetAddress address = InetAddress.getLocalHost();
            SocketAcceptor socketAcceptor = new SocketAcceptor(address, PORT);
            socketAcceptor.addSocketAcceptorListener(new SocketAcceptor.SocketAcceptorListener() {
                public void event(Connection connection) {
                    handleConnection(connection);
                }
            });
            acceptorThread = new Thread(socketAcceptor);
        } catch (UnknownHostException ex) {
            System.err.println("Unknown host:");
            ex.printStackTrace(System.err);
        } catch (IOException ex) {
            System.err.println("Failed to open socket:");
            ex.printStackTrace(System.err);
        }
    }
    
    private void start() {
        acceptorThread.start();
    }
    
    private void handleConnection(Connection connection) {
        String username = connection.getUsername();
        if (username != null) {
            System.out.println("adding connection from user: "+username);
            connection.addMessageHandler(serverthingy);
            connections.put(username, connection);
        }
    }
    
}
