package com.blakjack.clueless.gui;

import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.ImageIcon;

import javax.swing.JPanel;

public class GameBoardPanel extends JPanel {
    
    private final ImageIcon gameboard;
    
    public GameBoardPanel() {
        gameboard = new ImageIcon(getClass().getClassLoader().getResource("gameboard.png"));
        setPreferredSize(new Dimension(gameboard.getIconWidth(), gameboard.getIconHeight()));
    }

    // Gameboard image never changes we just need the pixel offsets
    // List of colorful dots (these do change)
    public void setGameBoard() {
        //start everyone in their starting positions
    }

    @Override
    public void paint(Graphics arg0) {
        super.paint(arg0);
        arg0.drawImage(gameboard.getImage(), 0, 0, null);
    }

}
