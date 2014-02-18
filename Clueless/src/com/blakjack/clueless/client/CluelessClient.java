/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.blakjack.clueless.client;

import com.blakjack.clueless.Connection;
import com.blakjack.clueless.NullMessage;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Timer;
import java.util.TimerTask;

/**
 * This Main class attempts to connect to a CluelessServer.
 * Currently it is connecting to the local address, but it should eventually be
 * made smarter.
 * 
 * @author nauglrj1
 */
public class CluelessClient {
    
    private static final int PORT = 10001;
    
    private static CluelessClient client;
    
    private final Connection connection;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        System.out.println("Starting client...");
        
        client = new CluelessClient();
        client.connect();
    }
    
    public CluelessClient() {
        Connection initConnection = null;
        try {
            InetAddress address = InetAddress.getLocalHost();
            Socket clientSocket = new Socket(address, PORT);
            initConnection = new Connection(clientSocket);
            new Timer().scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    ping();
                }
            }, 1000, 1000);
        } catch (UnknownHostException ex) {
            System.err.println("Unknown host:");
            ex.printStackTrace(System.err);
        } catch (IOException ex) {
            System.err.println("Failed to open socket:");
            ex.printStackTrace(System.err);
        }
        
        connection = initConnection;
    }
    
    private void connect() {
        connection.addMessageHandler(new ClientMessageHandler());
        connection.open();
    }
    
    private void ping() {
        System.out.println("ping");
        connection.send(new NullMessage());
    }
    
}
