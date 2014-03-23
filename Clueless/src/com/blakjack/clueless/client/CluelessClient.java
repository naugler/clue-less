/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.blakjack.clueless.client;

import com.blakjack.clueless.common.CluelessMessage;
import com.blakjack.clueless.common.CluelessMessage.Type;
import com.blakjack.clueless.common.Connection;
import com.blakjack.clueless.common.Connection.MessageHandler;
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
    private final String address;
    private final int port;
    private final Connection connection;

    public CluelessClient(String username, String address, int port) {
        this.username = username;
        this.address = address;
        this.port = port;
        connection = new Connection();
    }
    
    public void start() throws UnknownHostException, IOException {
        SocketFactory socketFactory = SocketFactory.getDefault();
        Socket clientSocket = socketFactory.createSocket(address, port);
        connection.open(clientSocket);
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
    
    public void addConnectionEventListener(Connection.ConnectionEventListener listener) {
        connection.addConnectionEventListener(listener);
    }
    
    public void send(CluelessMessage message) {
        message.setField("source", username);
        message.setField("date", new Date(System.currentTimeMillis()));
        connection.send(message);
    }
    
}
