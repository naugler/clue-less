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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

/**
 * This class shows the user's Clue-Less cards
 * 
 * @author nauglrj1
 */
public class CardPanel extends JPanel {
    
    private static final int VERT_GAP = 30;
    private static final int HORIZ_GAP = 20;
    private int mouseX = Integer.MIN_VALUE;
    private int mouseY = Integer.MIN_VALUE;
    
    Map<Card, ImageIcon> cards = new HashMap<Card, ImageIcon>();
    
    public CardPanel() {
        TitledBorder border = new TitledBorder("Clue Cards");
        setBorder(border);
        setPreferredSize(new Dimension(150,250));
        
        //just some in-place testing...
//        setCards(Card.MUSTARD, Card.GREEN, Card.PEACOCK);
    }
    
    public void setCards(List<Card> newCards) {
        cards.clear();
        for (Card card : newCards) {
        	System.out.println("card_"+card.name().toLowerCase()+".png");
        	System.out.println(getClass().getClassLoader().getResource("card_"+card.name().toLowerCase()+".png"));
            cards.put(card, new ImageIcon(getClass().getClassLoader().getResource("card_"+card.name().toLowerCase()+".png")));
        }
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        
        //calculate card size
        Graphics2D g2 = (Graphics2D)g;
        int count = 1;
        for (ImageIcon cardImage : cards.values()) {
            if (cardImage != null) {
                g2.drawImage(cardImage.getImage(), HORIZ_GAP*count, VERT_GAP*count, this);
            }
            ++count;
        }
    }
    
    
    
}
