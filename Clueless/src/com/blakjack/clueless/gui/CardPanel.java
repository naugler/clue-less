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
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;
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
    
    private static final int CARD_WIDTH = 100;
    private static final int CARD_HEIGHT = 125;
    
    private int horizGap = 0;
    private int vertGap = 0;
    private Map<Card, ImageIcon> cards = new HashMap<Card, ImageIcon>();
    private ImageIcon highlightedCard;
    
    public CardPanel() {
        TitledBorder border = new TitledBorder("Clue Cards");
        setBorder(border);
        setPreferredSize(new Dimension(150,250));
        
        //just some in-place testing
        setCards(new ArrayList<Card>() {{
            add(Card.BALLROOM);
            add(Card.BILLIARD);
            add(Card.CANDLESTICK);
            add(Card.CONSERVATORY);
            add(Card.DAGGER);
            add(Card.DININGROOM);
        }});
        
        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                for (ImageIcon cardImage : cards.values()) {
                    if (cardImage != null) {
                        
                    }
                }
            }
        });
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseExited(MouseEvent e) {
                highlightedCard = null;
            }
        });
    }
    
    public void setCards(List<Card> newCards) {
        cards.clear();
        for (Card card : newCards) {
            cards.put(card, new ImageIcon(getClass().getClassLoader().getResource("card_"+card.name().toLowerCase()+".png")));
        }
        recalculateSizes();
    }
    
    private void recalculateSizes() {
        Dimension panelSize = getSize();
        horizGap = (int)(panelSize.getWidth()-CARD_WIDTH)/(cards.size()+1);
        vertGap = (int)(panelSize.getHeight()-CARD_HEIGHT)/(cards.size()+1);
    }
    
    private Rectangle getCardBounds(int index) {
        int dx1 = (index+1)*horizGap;
        int dy1 = (index+1)*vertGap;
        int dx2 = dx1+CARD_WIDTH;
        int dy2 = dy1+CARD_HEIGHT;
        return new Rectangle(dx1, dy1, dx2-dx1, dy2-dy1);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        
        //calculate card size
        recalculateSizes();
        Graphics2D g2 = (Graphics2D)g;
        int count = 1;
        for (ImageIcon cardImage : cards.values()) {
            if (cardImage != null) {
                Rectangle bounds = getCardBounds(count-1);
                int dx1 = (int)bounds.getX();
                int dy1 = (int)bounds.getY();
                int dx2 = dx1 + (int)bounds.getWidth();
                int dy2 = dy1 + (int)bounds.getHeight();
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
