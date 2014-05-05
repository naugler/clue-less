package com.blakjack.clueless.gui;

import com.blakjack.clueless.common.Player;
import com.blakjack.clueless.common.Room;
import java.awt.Color;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class GameBoardPanel extends JPanel {
    
    private static final int TOP_MARGIN = 110; //margins don't count in tile size ;)
    private static final int LEFT_MARGIN = 125; //margins don't count in tile size ;)
    private static final int PIECE_SIZE = 25;
    private final int tileHeight;
    private final int tileWidth;
    
    private final ImageIcon gameboard;
    private List<Player> currentPlayers;
    
    public GameBoardPanel() {
        gameboard = new ImageIcon(getClass().getClassLoader().getResource("gameboard3.png"));
        //I don't know why this +20 is required, but it is?
        int width = gameboard.getIconWidth()+20;
        int height = gameboard.getIconHeight();
        setPreferredSize(new Dimension(width, height));
        tileHeight = (height-2*TOP_MARGIN)/5+10; //+5 for good measure?
        tileWidth = (width-2*LEFT_MARGIN)/5+5; //+5 for good measure?
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
            Map<Room, Integer> positionCounts = new HashMap<Room, Integer>();
            
//            int boardWidth = gameboard.getIconWidth() - 2*MARGINS;
//            int boardHeight = gameboard.getIconHeight() - 2*MARGINS;
//            int tileWidth = boardWidth / 5;
//            int tileHeight = boardHeight / 5;
            for (Player key : currentPlayers) {
            	Room room = key.getRoom();
                int playersHere = 0;
                if (positionCounts.containsKey(room)) {
                    playersHere = positionCounts.get(room);
                }
                positionCounts.put(room, ++playersHere);
                
                Rectangle bounds = getBoundsForPosition(room, playersHere);
//                int tileX = pos%5;
//                int tileY = pos/5;
                
                //draw the piece!
//                int width = (int)(tileWidth*PIECE_TILE_RATIO);
//                int height = (int)(tileHeight*PIECE_TILE_RATIO);
//                int x = tileX*tileWidth+(tileWidth-width)/2+MARGINS+(playersHere*3);
//                int y = tileY*tileHeight+(tileHeight-height)/2+MARGINS+(playersHere*3);
                arg0.setColor(key.getCharacter().getColor());
                arg0.fillOval((int)bounds.getX(), (int)bounds.getY(), 
                        (int)bounds.getWidth(), (int)bounds.getHeight());
                arg0.setColor(Color.BLACK);
                arg0.drawOval((int)bounds.getX(), (int)bounds.getY(), 
                        (int)bounds.getWidth(), (int)bounds.getHeight());
            }
        }
    }
    
    private Rectangle getBoundsForPosition(Room room, int occupants) {
        int x = 0, y = 0;
        --occupants;    //1 occupant means 0 offset.
        
        if (room.isHome()) {
            //silly special cases for home rooms.
            String name = room.getName().toLowerCase();
            if (name.contains("scarlet")) {
                x=3; y=-1;
            } else if (name.contains("plum")) {
                x=-1; y=1;
            } else if (name.contains("mustard")) {
                x=5; y=1;
            } else if (name.contains("peacock")) {
                x=-1; y=3;
            } else if (name.contains("green")) {
                x=1; y=5;
            } else if (name.contains("white")) {
                x=3; y=5;
            }
        } else {
            while (!room.isHome()) {
                if (room.getLeft() != null) {
                    room = room.getLeft();
                    ++x;
                } else if (room.getUp() != null) {
                    room = room.getUp();
                    ++y;
                } else {
                    break;
                }
            }
        }
        
        return new Rectangle(x*tileWidth+occupants*3+LEFT_MARGIN, 
                y*tileHeight+occupants*3+TOP_MARGIN, 
                PIECE_SIZE, PIECE_SIZE);
    }

}
