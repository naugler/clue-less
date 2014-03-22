/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.blakjack.clueless.client;

import com.blakjack.clueless.CluelessMessage;
import com.blakjack.clueless.CluelessMessage.Type;
import com.blakjack.clueless.Connection;
import com.blakjack.clueless.Connection.MessageHandler;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Date;
import javax.net.SocketFactory;

/**
 * This class attempts to connect to a CluelessServer.
 * 
 * @author nauglrj1
 */
public class CluelessClient {
    
    private final String username;
    private final Connection connection;

    public CluelessClient(String username, String address, int port) {
        this.username = username;
        Socket clientSocket = null;
        try {
            SocketFactory socketFactory = SocketFactory.getDefault();
            clientSocket = socketFactory.createSocket(address, port);
        } catch (UnknownHostException ex) {
            System.err.println("Unknown host:");
            ex.printStackTrace(System.err);
        } catch (IOException ex) {
            System.err.println("Failed to open socket:");
            ex.printStackTrace(System.err);
        }
        
        connection = new Connection(clientSocket);
    }
    
    public void start() {
        connection.open();
        CluelessMessage login = new CluelessMessage(Type.LOGIN);
        login.setField("source", username);
        send(login);
    }
    
    public void stop() {
        System.out.print("Shutting down client...");
        connection.close();
        System.out.println("done");
    }
    
    public void addMessageHandler(MessageHandler handler) {
        connection.addMessageHandler(handler);
    }
    
    public void send(CluelessMessage message) {
        message.setField("source", username);
        message.setField("date", new Date(System.currentTimeMillis()));
        connection.send(message);
    }
    
}
