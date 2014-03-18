/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.blakjack.clueless.client;

import com.blakjack.clueless.Connection.MessageHandler;
import java.awt.MenuBar;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 *
 * @author nauglrj1
 */
public class ClientFrame extends JFrame implements MessageHandler {
    
    public ClientFrame() {
        super();
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        initializeComponents();
    }
    
    private void initializeComponents() {
        this.setSize(600, 400);
        MenuBar bar = new MenuBar();
//        bar.add(new JMenu("New Game"));
        
        this.setMenuBar(bar);
        this.add(new JLabel("hello world"));
    }

    @Override
    public void handle(Object msg) {
        
    }
    
}
