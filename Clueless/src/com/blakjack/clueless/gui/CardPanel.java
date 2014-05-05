/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.blakjack.clueless.gui;

import com.blakjack.clueless.common.Card;
import java.awt.Cursor;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

/**
 * This class shows the user's Clue-Less cards
 * 
 * @author nauglrj1
 */
public class CardPanel extends JPanel {
    
    private static final int CARD_WIDTH = 100;
    private static final int CARD_HEIGHT = 125;
    
    Map<Card, ImageIcon> cards = new HashMap<Card, ImageIcon>();
    
    public CardPanel() {
        TitledBorder border = new TitledBorder("Clue Cards");
        setBorder(border);
        setPreferredSize(new Dimension(150,250));
        
        setToolTipText("Click for more detail!");
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JOptionPane.showMessageDialog(CardPanel.this,
                        new CardDetailPanel(cards),
                        "Your Hand",
                        JOptionPane.OK_CANCEL_OPTION);
            }
            
        });
    }
    
    public void setCards(List<Card> newCards) {
        cards.clear();
        for (Card card : newCards) {
            cards.put(card, new ImageIcon(getClass().getClassLoader().getResource("card_"+card.name().toLowerCase()+".png")));
        }
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        
        //calculate card size
        Dimension panelSize = getSize();
        int horizGap = (int)(panelSize.getWidth()-CARD_WIDTH)/(cards.size()+1);
        int vertGap = (int)(panelSize.getHeight()-CARD_HEIGHT)/(cards.size()+1);
        Graphics2D g2 = (Graphics2D)g;
        int count = 1;
        for (ImageIcon cardImage : cards.values()) {
            if (cardImage != null) {
                int dx1 = count*horizGap;
                int dy1 = count*vertGap;
                int dx2 = dx1+CARD_WIDTH;
                int dy2 = dy1+CARD_HEIGHT;
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
