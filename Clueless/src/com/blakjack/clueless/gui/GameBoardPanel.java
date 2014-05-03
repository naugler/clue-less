package com.blakjack.clueless.gui;

import com.blakjack.clueless.common.Player;
import java.awt.Color;

import java.awt.Dimension;
import java.awt.Graphics;
import java.util.Collections;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class GameBoardPanel extends JPanel {
    
    private final int MARGINS = 88; //margins don't count in tile size ;)
    private final double PIECE_TILE_RATIO = 0.25; //size of the piece as % of tile size
    
    private final ImageIcon gameboard;
    private List<Player> currentPlayers;
    
    public GameBoardPanel() {
        gameboard = new ImageIcon(getClass().getClassLoader().getResource("gameboard.png"));
        setPreferredSize(new Dimension(gameboard.getIconWidth(), gameboard.getIconHeight()));
    }

    // Gameboard image never changes we just need the pixel offsets
    // List of colorful dots (these do change)
    public void setPlayerPositions(List<Player> players) {
        currentPlayers = Collections.synchronizedList(players);
    }

    @Override
    public void paint(Graphics arg0) {
        super.paint(arg0);
        arg0.drawImage(gameboard.getImage(), 0, 0, null);
        
        if (currentPlayers != null) {
            int boardWidth = gameboard.getIconWidth() - 2*MARGINS;
            int boardHeight = gameboard.getIconHeight() - 2*MARGINS;
            int tileWidth = boardWidth / 5;
            int tileHeight = boardHeight / 5;
            for (Player key : currentPlayers) {
            	int pos = key.getPosition();
                int tileX = pos%5;
                int tileY = pos/5;
                
                //draw the piece!
                int width = (int)(tileWidth*PIECE_TILE_RATIO);
                int height = (int)(tileHeight*PIECE_TILE_RATIO);
                int x = tileX*tileWidth+(tileWidth-width)/2+MARGINS;
                int y = tileY*tileHeight+(tileHeight-height)/2+MARGINS;
                arg0.setColor(key.getCharacter().getColor());
                arg0.fillOval(x, y, width, height);
                arg0.setColor(Color.BLACK);
                arg0.drawOval(x, y, width, height);
            }
        }
    }

}
