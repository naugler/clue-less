package com.blakjack.clueless.gui;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JPanel;

public class ButtonPad extends JPanel {
    
    private class Button extends JButton {
        
        private final JButton button = new JButton();
        private Button() {
            button.addActionListener(buttonListener);
        }
        public JButton getButton() {
            return button;
        }
    }
    
    private final ActionListener buttonListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("BURTON CLURKED");
        }
    };
    
    private final Button UP = new Button();
    private final Button DOWN = new Button();
    private final Button RIGHT = new Button();
    private final Button LEFT = new Button();
    private final Button ACCUSE = new Button();
    private final Button SUGGEST = new Button();
    private final Button SECRET = new Button();
    private final Button ENDTURN = new Button();
	
    public ButtonPad() {
        super();
        initComponents();
    }
        
    private void initComponents() {
        setLayout(new GridLayout(3,3));
        
        add(SUGGEST.getButton());
        add(UP.getButton());
        add(SECRET.getButton());
        add(LEFT.getButton());
        add(new JPanel());
        add(RIGHT.getButton());
        add(ACCUSE.getButton());
        add(DOWN.getButton());
        add(ENDTURN.getButton());
    }
	
//	public void setButtonEnabled(Button button, boolean enabled){
//		Array.get(button).setEnabled(enabled);		
//	}

//	public boolean isButtonEnabled(Button button) {
//		return Array.get(button).enabled
//	}
	
}
