package com.blakjack.clueless.gui;

import com.blakjack.clueless.common.Player;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import java.util.HashMap;
import java.util.Map;
import javax.swing.ImageIcon;

public class ButtonPad extends JPanel {
    
    private JButton suggestBtn;
    private JButton accuseBtn;
    private JButton shortcutBtn;
    private JButton endBtn;
    private JButton upBtn;
    private JButton downBtn;
    private JButton leftBtn;
    private JButton rightBtn;
    private final Map<String, JButton> buttons = new HashMap<String, JButton>();
    private final Player player;
    
    public ButtonPad(Player client) {
        super();
        player = client;
        initComponents();
        setAllEnabled(false);
    }

    public void setBtnEnabled(String button, boolean enable) {
        buttons.get(button).setEnabled(enable);
    }

    public void setAllEnabled(boolean enable) {
        accuseBtn.setEnabled(enable);
        downBtn.setEnabled(enable);
        endBtn.setEnabled(enable);
        leftBtn.setEnabled(enable);
        rightBtn.setEnabled(enable);
        shortcutBtn.setEnabled(enable);
        suggestBtn.setEnabled(enable);
        upBtn.setEnabled(enable);
    }

    private void initComponents() {
        setLayout(new GridLayout(3, 3));

        //SUGGEST//
        suggestBtn = new JButton();
        suggestBtn.setToolTipText("Make a suggestion");
        suggestBtn.setIcon(new ImageIcon(getClass().getClassLoader().getResource("suggestBtn.png")));
        suggestBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SuggestionPanel suggestionPanel = new SuggestionPanel(player.getPosition());
                String title = "Make Suggestion";
                int retval = JOptionPane.showConfirmDialog(ButtonPad.this,
                        suggestionPanel,
                        title,
                        JOptionPane.OK_CANCEL_OPTION);
                if (retval == JOptionPane.OK_OPTION) {
                    System.out.println("Button pressed by this player");
                    player.makeSuggestion(suggestionPanel.getPerson().getName(), 
                            suggestionPanel.getWeapon().getName());
                }
            }
        });
        buttons.put("SUGGEST", suggestBtn);
        
        //ACCUSE//
        accuseBtn = new JButton();
        accuseBtn.setToolTipText("Make an accusation");
        accuseBtn.setIcon(new ImageIcon(getClass().getClassLoader().getResource("accuseBtn.png")));
        accuseBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SuggestionPanel suggestionPanel = new SuggestionPanel();
                String title = "Make Accusation";
                int retval = JOptionPane.showConfirmDialog(ButtonPad.this,
                        suggestionPanel,
                        title,
                        JOptionPane.OK_CANCEL_OPTION);
                if (retval == JOptionPane.OK_OPTION) {
                    System.out.println("Button pressed by this player");
                    player.accuse(suggestionPanel.getPerson().getName(), 
                            suggestionPanel.getWeapon().getName(), 
                            suggestionPanel.getRoom().getName());
                }
            }
        });
        buttons.put("ACCUSE", accuseBtn);
        
        //SHORTCUT//
        shortcutBtn = new JButton();
        shortcutBtn.setToolTipText("Take the shortcut!");
        shortcutBtn.setIcon(new ImageIcon(getClass().getClassLoader().getResource("shortcutBtn.png")));
        shortcutBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                player.move("SECRET");
            }
        });
        buttons.put("SECRET", shortcutBtn);
        
        //ENDTURN//
        endBtn = new JButton();
        endBtn.setToolTipText("End the turn");
        endBtn.setIcon(new ImageIcon(getClass().getClassLoader().getResource("endBtn.png")));
        endBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                player.endTurn();
            }
        });
        buttons.put("ENDTURN", endBtn);
        
        //UP//
        upBtn = new JButton();
        upBtn.setToolTipText("Move up");
        upBtn.setIcon(new ImageIcon(getClass().getClassLoader().getResource("upBtn.png")));
        upBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                player.move("UP");
            }
        });
        buttons.put("UP", upBtn);
        
        //DOWN//
        downBtn = new JButton();
        downBtn.setToolTipText("Move down");
        downBtn.setIcon(new ImageIcon(getClass().getClassLoader().getResource("downBtn.png")));
        downBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                player.move("DOWN");
            }
        });
        buttons.put("DOWN", downBtn);
        
        //LEFT//
        leftBtn = new JButton();
        leftBtn.setToolTipText("Move left");
        leftBtn.setIcon(new ImageIcon(getClass().getClassLoader().getResource("leftBtn.png")));
        leftBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                player.move("LEFT");
            }
        });
        buttons.put("LEFT", leftBtn);
        
        //DOWN//
        rightBtn = new JButton();
        rightBtn.setToolTipText("Move right");
        rightBtn.setIcon(new ImageIcon(getClass().getClassLoader().getResource("rightBtn.png")));
        rightBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                player.move("RIGHT");
            }
        });
        buttons.put("RIGHT", rightBtn);
        
        add(suggestBtn);
        add(upBtn);
        add(shortcutBtn);
        add(leftBtn);
        add(new JPanel());
        add(rightBtn);
        add(accuseBtn);
        add(downBtn);
        add(endBtn);
    }
}
