/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.blakjack.clueless.gui;

import com.blakjack.clueless.common.Player;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.Collection;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.MatteBorder;
import javax.swing.border.TitledBorder;

/**
 *
 * @author nauglrj1
 */
public class PlayersPanel extends JPanel {
    
    private static class PlayerPanel extends JPanel {
        
        public PlayerPanel(Player player, boolean turn) {
            Dimension size = new Dimension(90, 60);
            setPreferredSize(size);
            setLayout(new BorderLayout());
            
            setBorder(new MatteBorder(3, 3, 3, 3, player.getCharacter().getColor()));
            JLabel character = new JLabel(player.getCharacter().getName(), JLabel.CENTER);
            add(character, BorderLayout.NORTH);
            JLabel userName = new JLabel(player.getUsername(), JLabel.CENTER);
            add(userName, BorderLayout.CENTER);
            if (turn) {
                JLabel turnLabel = new JLabel("IN TURN", JLabel.CENTER);
                turnLabel.setForeground(Color.red);
                add(turnLabel, BorderLayout.SOUTH);
            }
        }
        
    }
    
    public PlayersPanel() {
        Dimension size = new Dimension(0, 100);
        setMinimumSize(size);
        setPreferredSize(size);
        
        TitledBorder border = new TitledBorder("Players");
        setBorder(border);
    }
    
    public void setPlayers(Collection<Player> players, int turn) {
        removeAll();
        setLayout(new FlowLayout(FlowLayout.CENTER));
        int count = turn;
        for (Player player : players) {
            System.out.println("adding player");
            add(new PlayerPanel(player, count == 0));
            --count;
        }
        revalidate();
        repaint();
    }
    
    public void setTurn(int turn) {
        
    }
    
}
