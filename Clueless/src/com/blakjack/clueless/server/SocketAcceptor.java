/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.blakjack.clueless.server;

import com.blakjack.clueless.common.Connection;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

/**
 * This Runnable class accepts new connections.
 * 
 * @author nauglrj1
 */
public class SocketAcceptor implements Runnable {
    
    public static interface SocketAcceptorListener {
        void event(Connection connection);
    }
    
    private final ServerSocket serverSocket;
    private final List<SocketAcceptorListener> listeners = new ArrayList<SocketAcceptorListener>();
    
    public SocketAcceptor(int port) throws IOException {
        serverSocket = new ServerSocket(port);
    }
    
    public void addSocketAcceptorListener(SocketAcceptorListener listener) {
        listeners.add(listener);
    }
    
    public void removeSocketAcceptorListener(SocketAcceptorListener listener) {
        listeners.remove(listener);
    }
    
    private void fireSocketAcceptorEvent(Connection connection) {
        for (SocketAcceptorListener listener : listeners) {
            listener.event(connection);
        }
    }
    
    public void close() {
        try {
            serverSocket.close();
        } catch (IOException ex) {
            System.err.println("Failed to close server socket");
            ex.printStackTrace(System.err);
        }
    }
    
    @Override
    public void run() {
        System.out.println("Accepting connections on "+serverSocket.getInetAddress().getHostAddress()+":"+serverSocket.getLocalPort()+" ...");
        while(!serverSocket.isClosed()) {
            try {
                Socket newSocket = serverSocket.accept();
                Connection newConnection = new Connection();
                newConnection.open(newSocket);
                fireSocketAcceptorEvent(newConnection);
            } catch (SocketException ex) {
                //closing time.
            } catch (IOException ex) {
                System.err.println("Failed to open connection");
                ex.printStackTrace(System.err);
            }
        }
    }
    
}
