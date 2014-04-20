package com.blakjack.clueless.gui;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.blakjack.clueless.client.UserEngine;

public class ButtonPad extends JPanel {
    
    private class Button extends JButton {
        
        private final JButton button = new JButton();
        private Button(ActionListener buttonListener) {
            button.addActionListener(buttonListener);
        }
        public JButton getButton() {
            return button;
        }
    }
    
//    private final ActionListener buttonListener = new ActionListener() {
//        @Override
//        public void actionPerformed(ActionEvent e) {
////            if () {
//                SuggestionPanel suggestionPanel = new SuggestionPanel(false);
//                String title = false ? "Make Accusation" : "Make Suggestion";
//                int retval = JOptionPane.showConfirmDialog(ButtonPad.this,
//                        suggestionPanel,
//                        title,
//                        JOptionPane.OK_CANCEL_OPTION);
//                if (retval == JOptionPane.OK_OPTION)
//                {
//                	System.out.println("Button pressed by this player");
//                	userEngine.makeSuggestion(suggestionPanel.getPerson().getName(), suggestionPanel.getWeapon().getName());
//                }
////            }
//        }
//    };
    
    private final Button UP = new Button(new ActionListener() {
    	@Override
    	public void actionPerformed(ActionEvent e) {
    		userEngine.move("UP");
    	}
    });
    private UserEngine userEngine;
    private final Button DOWN = new Button(new ActionListener() {
    	@Override
    	public void actionPerformed(ActionEvent e) {
    		userEngine.move("DOWN");
    	}
    });
    private final Button RIGHT = new Button(new ActionListener() {
    	@Override
    	public void actionPerformed(ActionEvent e) {
    		userEngine.move("RIGHT");
    	}
    });
    private final Button LEFT = new Button(new ActionListener() {
    	@Override
    	public void actionPerformed(ActionEvent e) {
    		userEngine.move("LEFT");
    	}
    });
    private final Button ACCUSE = new Button(new ActionListener() {
    	@Override
    	public void actionPerformed(ActionEvent e) {
    		SuggestionPanel suggestionPanel = new SuggestionPanel();
            String title = "Make Accusation";
            int retval = JOptionPane.showConfirmDialog(ButtonPad.this,
                    suggestionPanel,
                    title,
                    JOptionPane.OK_CANCEL_OPTION);
            if (retval == JOptionPane.OK_OPTION)
            {
            	System.out.println("Button pressed by this player");
            	userEngine.accuse(suggestionPanel.getPerson().getName(), suggestionPanel.getWeapon().getName(), suggestionPanel.getRoom().getName());
            }
    	}
    });
    private final Button SUGGEST = new Button(new ActionListener() {
    	@Override
    	public void actionPerformed(ActionEvent e) {
    		SuggestionPanel suggestionPanel = new SuggestionPanel(userEngine.getPlayer().getPosition());
            String title = "Make Suggestion";
            int retval = JOptionPane.showConfirmDialog(ButtonPad.this,
                    suggestionPanel,
                    title,
                    JOptionPane.OK_CANCEL_OPTION);
            if (retval == JOptionPane.OK_OPTION)
            {
            	System.out.println("Button pressed by this player");
            	userEngine.makeSuggestion(suggestionPanel.getPerson().getName(), suggestionPanel.getWeapon().getName());
            }
    	}
    });
    private final Button SECRET = new Button(new ActionListener() {
    	@Override
    	public void actionPerformed(ActionEvent e) {
    		userEngine.move("SECRET");
    	}
    });
    private final Button ENDTURN = new Button(new ActionListener() {
    	@Override
    	public void actionPerformed(ActionEvent e) {
    		userEngine.endTurn();
    	}
    });
	
    public ButtonPad(UserEngine client) {
        super();
        userEngine = client;
        initComponents();
        enableAll(false);
    }
    
    public void enableButton(String button, boolean enable)
    {
    	Button b = null;
    	switch (button)
    	{
    	case "UP":
    		b = UP;
    		break;
    	case "DOWN":
    		b = DOWN;
    		break;
    	case "LEFT":
    		b = LEFT;
    		break;
    	case "RIGHT":
    		b = RIGHT;
    		break;
    	case "SECRET":
    		b = SECRET;
    		break;
    	case "ENDTURN":
    		b = ENDTURN;
    		break;
    	case "ACCUSE":
    		b = ACCUSE;
    		break;
    	case "SUGGEST":
    		b = SUGGEST;
    		break;
    	default:
    		System.out.println("ERROR: That button does not exist!!!!");
    	}
    	if (b != null)
    	{
    		b.getButton().setEnabled(enable);
    	}
    }
    
    public void enableAll(boolean enable)
    {
    	ACCUSE.getButton().setEnabled(enable);
    	DOWN.getButton().setEnabled(enable);
    	ENDTURN.getButton().setEnabled(enable);
    	LEFT.getButton().setEnabled(enable);
    	RIGHT.getButton().setEnabled(enable);
    	SECRET.getButton().setEnabled(enable);
    	SUGGEST.getButton().setEnabled(enable);
    	UP.getButton().setEnabled(enable);
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
