/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.blakjack.clueless.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.net.InetAddress;
import java.net.UnknownHostException;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 *
 * @author nauglrj1
 */
public class LoginPanel extends JPanel {

    private final JTextField username = new JTextField();
    private final JTextField address = new JTextField();
    private final JTextField port = new JTextField();
    
    public LoginPanel(boolean startServer) {
        initComponents();
        if (startServer) {
            address.setEnabled(false);
            try {
                address.setText(InetAddress.getLocalHost().getHostAddress());
            } catch (UnknownHostException ex) {
                ex.printStackTrace(System.err);
            }
        }
    }
    
    private void initComponents() {
        setLayout(new GridBagLayout());
        
        GridBagConstraints c = new GridBagConstraints();
        c.anchor = GridBagConstraints.WEST;
        add(new JLabel("Username: "), c);
        c.gridy = 1;
        add(new JLabel("Address: "), c);
        c.gridy = 2;
        add(new JLabel("Port: "), c);
        
        c.gridx = 1;
        c.gridy = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 2;
        add(username, c);
        c.gridy = 1;
        add(address, c);
        c.gridy = 2;
        add(port, c);
    }
    
    public String getUsername() {
        return username.getText();
    }
    
    public String getAddress() {
        return address.getText();
    }
    
    public int getPort() {
        return Integer.parseInt(port.getText());
    }
    
}

