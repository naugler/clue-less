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
import java.util.Collection;
import java.util.Map;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

/**
 *
 * @author nauglrj1
 */
public class CardDetailPanel extends JPanel {
    
    private static final int MARGIN = 5;
    private static final int CARD_WIDTH = 175;
    private static final int CARD_HEIGHT = 250;
    
    private final Collection<ImageIcon> images;
    
    CardDetailPanel(Map<Card, ImageIcon> cards) {
        images = cards.values();
        Dimension size = new Dimension((CARD_WIDTH+MARGIN)*cards.size()+MARGIN, CARD_HEIGHT+2*MARGIN);
        setPreferredSize(size);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); //To change body of generated methods, choose Tools | Templates.
        
        Graphics2D g2 = (Graphics2D)g;
        int count = 0;
        for (ImageIcon cardImage : images) {
            if (cardImage != null) {
                int dx1 = count*(CARD_WIDTH+MARGIN);
                int dy1 = MARGIN;
                int dx2 = dx1+CARD_WIDTH;
                int dy2 = CARD_HEIGHT+MARGIN;
                int sx1 = 0;
                int sy1 = 0;
                int sx2 = cardImage.getIconWidth();
                int sy2 = cardImage.getIconHeight();
                g2.drawImage(cardImage.getImage(), 
                        dx1, dy1, dx2, dy2,
                        sx1, sy1, sx2, sy2, this);
            }
            ++count;
        }
    }
    
}
