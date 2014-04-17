package com.blakjack.clueless.gui;

import com.blakjack.clueless.common.Player;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.Collections;
import java.util.List;
import javax.swing.ImageIcon;

import javax.swing.JPanel;

public class GameBoardPanel extends JPanel {
    
    private final ImageIcon gameboard;
    private List<Player> currentPlayers;
    
    public GameBoardPanel() {
        gameboard = new ImageIcon(getClass().getClassLoader().getResource("gameboard.png"));
        setPreferredSize(new Dimension(gameboard.getIconWidth(), gameboard.getIconHeight()));
    }

    // Gameboard image never changes we just need the pixel offsets
    // List of colorful dots (these do change)
    public void setPlayerPositions(List<Player> players) {
        //start everyone in their starting positions
        currentPlayers = Collections.synchronizedList(players);
    }

    @Override
    public void paint(Graphics arg0) {
        super.paint(arg0);
        arg0.drawImage(gameboard.getImage(), 0, 0, null);
        
        if (currentPlayers != null) {
            for (Player player : currentPlayers) {
                player.getPosition();
                
                //draw the piece!
            }
        }
    }

}
