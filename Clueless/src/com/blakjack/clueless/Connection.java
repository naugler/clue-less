/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.blakjack.clueless;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * This class manages a single connection. This is where the reading and writing
 * of messages takes place.
 * 
 * @author nauglrj1
 */
public class Connection {
    
    public enum ConnectionEvent {
        OPEN,
        CLOSED,
    }
    
    public static interface ConnectionEventListener {
        void event(Connection connection, ConnectionEvent event);
    }
    
    public static interface MessageHandler {
        void handle(Object msg);
    }
    
    private final Socket socket;
    private ObjectInputStream reader = null;
    private ObjectOutputStream writer = null;
    private Thread listenerThread = null;
    private String username = null;
    
    private final List<ConnectionEventListener> listeners 
            = new ArrayList<ConnectionEventListener>();
    private final List<MessageHandler> handlers
            = new ArrayList<MessageHandler>();

    public Connection(Socket initSocket) {
        this.socket = initSocket;
        
        listenerThread = new Thread(new Runnable() {
            @Override
            public void run() {
//                boolean needName = true;
//                while(!Thread.interrupted() && needName) {
                    try {
                        Object msg = reader.readObject();
                        System.out.println("msg: "+msg);
                        System.out.println("msg is a "+msg.getClass().getCanonicalName());
                        username = (String)msg;
//                        needName = false;
                    } catch (IOException ex) {
                        System.err.println(ex.getMessage());
                        close();
                    } catch (ClassNotFoundException ex) {
                        System.err.println(ex.getMessage());
                        close();
                    }
//                }
                
                while(!Thread.interrupted()) {
                    try {
                        Object msg = reader.readObject();
                        fireMessage(msg);
                    } catch (IOException ex) {
                        System.err.println(ex.getMessage());
                        close();
                    } catch (ClassNotFoundException ex) {
                        System.err.println(ex.getMessage());
                        close();
                    }
                }
            }
        });
    }
    
    public void addConnectionEventListener(ConnectionEventListener listener) {
        listeners.add(listener);
    }
    
    public void removeConnectionEventListener(ConnectionEventListener listener) {
        listeners.remove(listener);
    }
    
    private void fireConnectionEvent(ConnectionEvent event) {
        for (ConnectionEventListener listener : listeners) {
            listener.event(this, event);
        }
    }
    
    public void addMessageHandler(MessageHandler handler) {
        handlers.add(handler);
    }
    
    public void removeMessageHandler(MessageHandler handler) {
        handlers.remove(handler);
    }
    
    private void fireMessage(Object msg) {
        for (MessageHandler handler : handlers) {
            handler.handle(msg);
        }
    }
    
    public void open() {
        System.out.println("Opening connection to "+this);
        ObjectInputStream initReader = null;
        ObjectOutputStream initWriter = null;
        try {
            initWriter = new ObjectOutputStream(socket.getOutputStream());
            initWriter.flush();
            initReader = new ObjectInputStream(socket.getInputStream());
        } catch (IOException ex) {
            close();
        }
        reader = initReader;
        writer = initWriter;
        startDataListener();
        fireConnectionEvent(ConnectionEvent.OPEN);
    }
    
    public void close() {
        System.out.println("Closing connection to "+this);
        stopDataListener();
        try {
            socket.close();
        } catch (IOException ex) {
            System.err.println("Error closing connection:");
            ex.printStackTrace(System.err);
        }
        fireConnectionEvent(ConnectionEvent.CLOSED);
    }
    
    public void send(Serializable msg) {
        try {
            writer.writeObject(msg);
            writer.flush();
        } catch (IOException ex) {
            System.err.println("Failed to write object "+msg);
            ex.printStackTrace(System.err);
        }
    }
    
    public Socket getSocket() {
        return socket;
    }
    
    private void startDataListener() {
        listenerThread.start();
    }
    
    private void stopDataListener() {
        listenerThread.interrupt();
    }
    
    public String getUsername() {
        return username;
    }
    
    @Override
    public String toString() {
        return socket.getInetAddress().getHostAddress()+":"+socket.getPort();
    }
    
}
