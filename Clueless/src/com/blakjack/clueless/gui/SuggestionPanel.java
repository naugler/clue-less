/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.blakjack.clueless.gui;

import com.blakjack.clueless.common.Card;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * A simple dialog for solution suggestion
 * 
 * @author nauglrj1
 */
public class SuggestionPanel extends JPanel {

    private final JComboBox personComboBox = new JComboBox(new Object[]
    {Card.GREEN, Card.MUSTARD, Card.PEACOCK, Card.PLUM, Card.SCARLET, Card.WHITE});
    private final JComboBox weaponComboBox = new JComboBox(new Object[]
    {Card.CANDLESTICK, Card.DAGGER, Card.PIPE, Card.REVOLVER, Card.ROPE, Card.WRENCH});
    private final JComboBox roomComboBox = new JComboBox(new Object[]
    {Card.BALLROOM, Card.BILLIARD, Card.CONSERVATORY, Card.DININGROOM, Card.HALL, Card.KITCHEN, Card.LIBRARY, Card.LOUNGE, Card.STUDY});
    
    public SuggestionPanel() {
        initComponents();
    }
    /**
     * This constructor is for the suggest box where the room is given and cannot be changed
     * @param position
     */
    public SuggestionPanel(int position)
    {
    	initComponents();
    	// TODO: change the 0 to correspond to the position of the player
    	roomComboBox.setSelectedIndex(0);
    	roomComboBox.setEnabled(false);
    }
    
    private void initComponents() {
        setLayout(new GridBagLayout());
        
        GridBagConstraints c = new GridBagConstraints();
        c.anchor = GridBagConstraints.WEST;
        add(new JLabel("Suspect: "), c);
        c.gridy = 1;
        add(new JLabel("Weapon: "), c);
        c.gridy = 2;
        add(new JLabel("Room: "), c);
        
        c.gridx = 1;
        c.gridy = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 2;
        add(personComboBox, c);
        c.gridy = 1;
        add(weaponComboBox, c);
        c.gridy = 2;
        add(roomComboBox, c);
    }
    
    public Card getPerson() {
        return (Card)personComboBox.getSelectedItem();
    }
    
    public Card getWeapon() {
        return (Card)weaponComboBox.getSelectedItem();
    }
    
    public Card getRoom() {
        return (Card)roomComboBox.getSelectedItem();
    }
    
}

