/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.blakjack.clueless.gui;

import com.blakjack.clueless.common.Card;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

/**
 * This class shows the user's Clue-Less cards
 * 
 * @author nauglrj1
 */
public class CardPanel extends JPanel {
    
    private static final int VERT_GAP = 30;
    private static final int HORIZ_GAP = 20;
    
    ImageIcon card1Image;
    ImageIcon card2Image;
    ImageIcon card3Image;
    
    public CardPanel() {
        setPreferredSize(new Dimension(150,250));
    }
    
    public void setCards(Card newCard1, Card newCard2, Card newCard3) {
        //load images for cards
        card1Image = new ImageIcon(getClass().getClassLoader().getResource(newCard1+".png"));
        card2Image = new ImageIcon(getClass().getClassLoader().getResource(newCard2+".png"));
        card3Image = new ImageIcon(getClass().getClassLoader().getResource(newCard3+".png"));
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        
        Graphics2D g2 = (Graphics2D)g;
        if (card1Image != null) {
            g2.drawImage(card1Image.getImage(), HORIZ_GAP, VERT_GAP, this);
        }
        if (card2Image != null) {
            g2.drawImage(card2Image.getImage(), HORIZ_GAP*2, VERT_GAP*2, this);
        }
        if (card3Image != null) {
            g2.drawImage(card3Image.getImage(), HORIZ_GAP*3, VERT_GAP*3, this);
        }
    }
    
    
    
}
