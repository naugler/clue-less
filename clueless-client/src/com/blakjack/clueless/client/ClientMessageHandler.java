/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.blakjack.clueless.client;

import com.blakjack.clueless.Connection.MessageHandler;

/**
 * This is just a placeholder message handler. When messages are sent to the
 * client, they will be handled here.
 * 
 * @author nauglrj1
 */
public class ClientMessageHandler implements MessageHandler {

    public void handle(Object o) {
        System.out.println("Got a message! Here it is: ");
        System.out.println(o);
    }
    
}
