/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.blakjack.clueless.server;

import com.blakjack.clueless.common.Connection;
import com.blakjack.clueless.common.Connection.ConnectionEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * This class starts up a socket acceptor and handles new connections
 * 
 * @author nauglrj1
 */
public class CluelessServer {
    
    private final int port;
    private SocketAcceptor socketAcceptor;
    private final GameEngine game = new GameEngine();
    
    private final List<Connection> connections = new ArrayList<Connection>();
    
    public CluelessServer(int port) {
        this.port = port;
    }
    
    public void start() throws IOException{
        socketAcceptor = new SocketAcceptor(port);
        socketAcceptor.addSocketAcceptorListener(new SocketAcceptor.SocketAcceptorListener() {
            @Override
            public void event(Connection connection) {
                handleConnection(connection);
            }
        });
        new Thread(socketAcceptor).start();
    }
    
    public void stop() {
        System.out.println("Shutting down server...");
        if (socketAcceptor != null) {
            socketAcceptor.close();
        }
        for (Connection connection : connections) {
            connection.close();
        }
        System.out.println("done");
    }
    
    private void handleConnection(Connection connection) {
        connections.add(connection);
        connection.addConnectionEventListener(new Connection.ConnectionEventListener() {
            @Override
            public void event(Connection connection, Connection.ConnectionEvent event) {
                if (event == ConnectionEvent.CLOSED) {
                    connections.remove(connection);
                }
            }
        });
        connection.addConnectionEventListener(game);
        connection.addMessageHandler(game);
    }
    
}
