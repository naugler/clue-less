/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.blakjack.clueless.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author nauglrj1
 */
public class AboutDialog extends JDialog {
    
    private final ImageIcon logoLarge;

    public AboutDialog(JFrame parent) {
        super(parent, "About Clue-Less", true);
        setLocationRelativeTo(parent);
        
        logoLarge = new ImageIcon(getClass().getClassLoader().getResource("logoLarge.png"));
        JLabel logoLabel = new JLabel();
        logoLabel.setIcon(logoLarge);

        Box b = Box.createVerticalBox();
        b.add(Box.createGlue());
        b.add(new JLabel("Clue-Less was produced by a small team of developers,"));
        b.add(new JLabel("that were certainly not being forced or coerced"));
        b.add(new JLabel("at the time of its authorship."));
        b.add(new JLabel("Servitude leads to prosperity."));
        b.add(logoLabel);
        b.add(Box.createGlue());
        getContentPane().add(b, "Center");

        JPanel p2 = new JPanel();
        JButton ok = new JButton("Ok");
        p2.add(ok);
        getContentPane().add(p2, "South");

        ok.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                setVisible(false);
            }
        });

        pack();
    }
}
