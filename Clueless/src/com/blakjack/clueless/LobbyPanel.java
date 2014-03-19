/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.blakjack.clueless;

import com.blakjack.clueless.Connection.ConnectionEvent;
import com.blakjack.clueless.client.CluelessClient;
import java.awt.Component;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;

/**
 *
 * @author nauglrj1
 */
public class LobbyPanel extends JPanel {
    
    private final DefaultListModel playerModel = new DefaultListModel();
    private final JList playerList = new JList(playerModel);
    
    public LobbyPanel() {
        initComponents();
    }
    
    private void initComponents() {
        playerList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                Connection c = (Connection)value;
                return super.getListCellRendererComponent(list, c.getUsername(), index, isSelected, cellHasFocus);
            }
        });
        
        add(playerList);
        
    }
    
    private void handleConnectionEvent(Connection connection, ConnectionEvent event) {
        if (event == ConnectionEvent.OPEN) {
            playerModel.addElement(connection);
        } else if (event == ConnectionEvent.CLOSED) {
            playerModel.addElement(connection);
        }
    }
}
