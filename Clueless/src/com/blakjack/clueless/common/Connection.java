/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.blakjack.clueless.common;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

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
        void handle(Connection connection, CluelessMessage msg);
    }
    
    private Socket socket;
    private ObjectInputStream reader = null;
    private ObjectOutputStream writer = null;
    private Thread listenerThread = null;
    
    private final List<ConnectionEventListener> listeners 
            = new ArrayList<ConnectionEventListener>();
    private final List<MessageHandler> handlers
            = Collections.synchronizedList(new ArrayList<MessageHandler>());

    public Connection() {
        
        listenerThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while(!Thread.interrupted()) {
                    try {
                        Object obj = reader.readObject();
                        if (obj instanceof CluelessMessage) {
                            fireMessage((CluelessMessage)obj);
                        } else {
                            System.err.println("Unknown message: "+obj);
                        }
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
        synchronized(handlers) {
            handlers.add(handler);
        }
    }
    
    public void removeMessageHandler(MessageHandler handler) {
        synchronized(handlers) {
            handlers.remove(handler);
        }
    }
    
    private void fireMessage(CluelessMessage msg) {
        synchronized(handlers) {
            for (MessageHandler handler : handlers) {
                handler.handle(this, msg);
            }
        }
    }
    
    public void open(Socket socket) {
        this.socket = socket;
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
            if (socket != null) {
                socket.close();
            }
        } catch (IOException ex) {
            System.err.println("Error closing connection:");
            ex.printStackTrace(System.err);
        }
        fireConnectionEvent(ConnectionEvent.CLOSED);
    }
    
    public void send(Serializable msg) {
        try {
            synchronized(writer) {
                writer.writeObject(msg);
                writer.flush();
            }
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
    
    @Override
    public String toString() {
        if (socket != null) {
            return socket.getInetAddress().getHostAddress()+":"+socket.getPort();
        } else {
            return "null:null";
        }
    }

    @Override
    public int hashCode() {
        int hash = 7;
        return 79 * hash + Objects.hashCode(this.socket);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Connection other = (Connection) obj;
        return !Objects.equals(this.socket, other.socket);
    }
    
}
