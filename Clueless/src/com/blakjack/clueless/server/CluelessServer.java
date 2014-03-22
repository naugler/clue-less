/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.blakjack.clueless.server;

import com.blakjack.clueless.Connection;
import com.blakjack.clueless.Connection.ConnectionEvent;
import com.blakjack.clueless.Connection.ConnectionEventListener;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class starts up a socket acceptor and handles new connections
 * 
 * @author nauglrj1
 */
public class CluelessServer {
    
    private Thread acceptorThread = null;
    private final GameEngine game = new GameEngine();
    
    private final Map<String, Connection> connections = new HashMap<String, Connection>();
    
    public CluelessServer(int port) {
        try {
            SocketAcceptor socketAcceptor = new SocketAcceptor(port);
            socketAcceptor.addSocketAcceptorListener(new SocketAcceptor.SocketAcceptorListener() {
                @Override
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
    
    public void start() {
        acceptorThread.start();
    }
    
    public void stop() {
        System.out.print("Shutting down server...");
        acceptorThread.interrupt();
        for (Connection connection : connections.values()) {
            connection.close();
        }
        System.out.println("done");
    }
    
    private void handleConnection(Connection connection) {
        connection.addConnectionEventListener(game);
        connection.addMessageHandler(game);
    }
    
}
