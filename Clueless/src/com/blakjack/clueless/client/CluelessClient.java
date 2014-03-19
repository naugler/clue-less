/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.blakjack.clueless.client;

import com.blakjack.clueless.GameFrame;
import com.blakjack.clueless.Connection;
import com.blakjack.clueless.NullMessage;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import javax.net.SocketFactory;

/**
 * This Main class attempts to connect to a CluelessServer.
 * Currently it is connecting to the local address, but it should eventually be
 * made smarter.
 * 
 * @author nauglrj1
 */
public class CluelessClient {
    
    private final Connection connection;

    public CluelessClient(String address, int port) {
        Connection initConnection = null;
        try {
            SocketFactory socketFactory = SocketFactory.getDefault();
            Socket clientSocket = socketFactory.createSocket(address, port);
            initConnection = new Connection(clientSocket);
        } catch (UnknownHostException ex) {
            System.err.println("Unknown host:");
            ex.printStackTrace(System.err);
        } catch (IOException ex) {
            System.err.println("Failed to open socket:");
            ex.printStackTrace(System.err);
        }
        
        connection = initConnection;
    }
    
    public void start(String username) {
        connection.addMessageHandler(new ClientMessageHandler());
        connection.open();
        connection.send(username);
    }
    
    public void stop() {
        System.out.print("Shutting down client...");
        connection.close();
        System.out.println("done");
    }
    
    private void ping() {
        System.out.println("ping");
        connection.send(new NullMessage());
    }
    
}
