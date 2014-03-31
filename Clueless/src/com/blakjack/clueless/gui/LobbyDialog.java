/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.blakjack.clueless.gui;

import com.blakjack.clueless.client.UserEngine;
import com.blakjack.clueless.common.CluelessMessage;
import com.blakjack.clueless.common.Connection;
import com.blakjack.clueless.common.Connection.ConnectionEvent;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author nauglrj1
 */
public class LobbyDialog extends JDialog implements Connection.MessageHandler {
    
    private static final String[] COLUMNS = new String[] {"", "User", "Character", "Status"};
    private final DefaultTableModel playerModel = new DefaultTableModel(COLUMNS,6);
    private final JTable playerTable = new JTable(playerModel);
    private final JButton leaveButton = new JButton("Leave Game");
    private final JButton startButton = new JButton("Start Game");
    
    private final boolean startServer;
    private final UserEngine client;
    
    public LobbyDialog(boolean startServer, UserEngine client) {
        setTitle("Game Lobby");
        this.startServer = startServer;
        this.client = client;
        initComponents();
        pack();
        client.getClient().addConnectionEventListener(new Connection.ConnectionEventListener() {
            @Override
            public void event(Connection connection, Connection.ConnectionEvent event) {
                if (event == ConnectionEvent.CLOSED) {
                    LobbyDialog.this.setVisible(false);
                }
            }
        });
        
    }
    
    private void initComponents() {
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.out.println("lobby closed");
                super.windowClosed(e);
                close();
            }
        });
        setLayout(new BorderLayout());
        add(playerTable, BorderLayout.CENTER);
        
        JPanel buttonPanel = new JPanel();
        leaveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                close();
            }
        });
        buttonPanel.add(leaveButton, BorderLayout.WEST);
        
        if (startServer) {
            startButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    CluelessMessage msg = new CluelessMessage(CluelessMessage.Type.START);
                    client.getClient().send(msg);
                }
            });
            buttonPanel.add(startButton, BorderLayout.EAST);
        }
        
        add(buttonPanel, BorderLayout.SOUTH);
    }

    @Override
    public void handle(Connection connection, CluelessMessage msg) {
        CluelessMessage.Type type = (CluelessMessage.Type)msg.getField("type");
        switch (type) {
            case START:
                setVisible(false);
                break;
            case UPDATE:
                //clear table model
                startButton.setEnabled(false);
                for (int i = playerModel.getRowCount()-1; i >= 0; --i) {
                    System.out.println("removing row");
                    playerModel.removeRow(i);
                }
                //add all players in update
                for (int i = 0; i < 6; ++i) {
                    String username = (String)msg.getField("player"+i+"username");
                    if (username != null) {
                        String character = (String)msg.getField("player"+i+"character");
                        playerModel.addRow(new Object[]{i+1, username, character, "Ready"});
                        if (i > 1) {
                            startButton.setEnabled(true);
                        }
                    }
                }
                playerModel.fireTableDataChanged();
                break;
            default:
        }
    }
    
    public void close() {
        int retval = JOptionPane.YES_OPTION;
        if (startServer) {
            retval = JOptionPane.showConfirmDialog(LobbyDialog.this, 
                    "Leaving the game will close lobby\nfor other players. Are you sure?", 
                    "Leave Game", 
                    JOptionPane.YES_NO_OPTION);
        }
        if (retval == JOptionPane.YES_OPTION) {
            client.shutdown();
            LobbyDialog.this.setVisible(false);
        }
    }
    
}
