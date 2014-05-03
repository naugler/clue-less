/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.blakjack.clueless.gui;

import com.blakjack.clueless.common.CluelessMessage;
import com.blakjack.clueless.common.Connection;
import com.blakjack.clueless.common.Connection.ConnectionEvent;
import com.blakjack.clueless.common.Player;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
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
    private final Player player;
    
    public LobbyDialog(Frame owner, boolean startServer, Player client) {
        super(owner);
        setLocationRelativeTo(owner);
        setTitle("Game Lobby");
        this.startServer = startServer;
        this.player = client;
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
        
        setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        
    }
    
    private void initComponents() {
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
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
                    player.getClient().send(msg);
                }
            });
            buttonPanel.add(startButton, BorderLayout.EAST);
        }
        
        add(buttonPanel, BorderLayout.SOUTH);
    }

    @Override
    public void handle(Connection connection, final CluelessMessage msg) {
        CluelessMessage.Type type = (CluelessMessage.Type)msg.getField("type");
        switch (type) {
            case START:
                setVisible(false);
                break;
            case UPDATE:
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {//clear table model
                        startButton.setEnabled(false);
                        for (int i = playerModel.getRowCount()-1; i >= 0; --i) {
                            playerModel.removeRow(i);
                        }
                        //add all players in update
                        List<Player> gameStatus = (List<Player>) msg.getField("status");
                        if (gameStatus != null) {
                            int count = 0;
                            for (Player player : gameStatus) {
                                String username = player.getUsername();
                                String character = player.getCharacter().getName();
                                playerModel.addRow(new Object[]{count+1, username, character, "Ready"});
                            }
                            startButton.setEnabled(gameStatus.size() > 2);
                        }
                        playerModel.fireTableDataChanged();
                    }
                });
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
            player.shutdown();
            LobbyDialog.this.setVisible(false);
        }
    }
    
}
