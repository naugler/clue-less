/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.blakjack.clueless.gui;

import com.blakjack.clueless.client.UserEngine;
import com.blakjack.clueless.common.CluelessMessage;
import com.blakjack.clueless.common.Connection;
import com.blakjack.clueless.common.Connection.MessageHandler;
import com.blakjack.clueless.common.Connection.ConnectionEvent;
import com.blakjack.clueless.common.Connection.ConnectionEventListener;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.UnknownHostException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;

/**
 * The main application frame for Clue-Less
 *
 * @author nauglrj1
 */
public class GameFrame extends JFrame implements MessageHandler, ConnectionEventListener {

    private final JTextArea log = new JTextArea();
    
    private final CardPanel cardPanel = new CardPanel();
    private final MenuItem newGame = new MenuItem("New Game");
    private final MenuItem joinGame = new MenuItem("Join Game");
    private final UserEngine userEngine = new UserEngine();
    private final ButtonPad buttonPad = new ButtonPad(userEngine);
//    /**
//     * @param args the command line arguments
//     */
    public static void main(String[] args) {
        System.out.println("Starting Clue-Less...");
        GameFrame gameFrame = new GameFrame();
        gameFrame.setVisible(true);
    }

    public GameFrame() {
        super("Clue-Less");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        initializeComponents();
        pack();
    }

    private void initializeComponents() {
        setLayout(new BorderLayout());
        Menu fileMenu = new Menu("File");
        Menu helpMenu = new Menu("Help");

        newGame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                connect(true);
            }
        });
        fileMenu.add(newGame);

        joinGame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                connect(false);
                //get options.
//                userEngine.suggest();
            }
        });
        fileMenu.add(joinGame);

        MenuItem exitGame = new MenuItem("Exit");
        exitGame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                shutdown();
            }
        });
        fileMenu.add(exitGame);

        MenuItem about = new MenuItem("About");
        exitGame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //TODO(naugler)
            }
        });
        helpMenu.add(about);

        MenuBar bar = new MenuBar();
        bar.add(fileMenu);
        bar.add(helpMenu);
        this.setMenuBar(bar);

        JPanel leftPanel = new JPanel(new BorderLayout());
        log("Welcome to Clue-Less!");
        log.setEditable(false);
        log.setPreferredSize(new Dimension(100, 200));
        leftPanel.add(log, BorderLayout.NORTH);
        leftPanel.add(buttonPad, BorderLayout.CENTER);
        leftPanel.add(cardPanel, BorderLayout.SOUTH);
        add(leftPanel, BorderLayout.WEST);

        add(new GameBoardPanel(), BorderLayout.CENTER);

        add(new EvidenceLocker(), BorderLayout.EAST);
    }

    private void connect(boolean startServer) {
        LoginPanel loginPanel = new LoginPanel(startServer);
        String title = startServer ? "New Game" : "Join Game";
        int retval = JOptionPane.showConfirmDialog(this,
                loginPanel,
                title,
                JOptionPane.OK_CANCEL_OPTION);
        try {
            userEngine.connect(startServer, retval == JOptionPane.OK_OPTION, loginPanel.getPort(), loginPanel.getAddress(), loginPanel.getUsername(), this);
        } catch (UnknownHostException ex) {
            JOptionPane.showMessageDialog(this, "Unknown host: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return;
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Socket error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        waitInLobby(startServer);

    }

    private void waitInLobby(boolean startServer) {
        LobbyDialog lobby = new LobbyDialog(startServer, userEngine);
        userEngine.getClient().addMessageHandler(lobby);

        CluelessMessage login = new CluelessMessage(CluelessMessage.Type.LOGIN);
        userEngine.getClient().send(login);

        lobby.setModal(true);
        lobby.setVisible(true);
        userEngine.getClient().removeMessageHandler(lobby);
    }

    @Override
    public void handle(Connection connection, CluelessMessage msg) {
        CluelessMessage.Type type = (CluelessMessage.Type) msg.getField("type");
        switch (type) {
            case ERROR:
                JOptionPane.showMessageDialog(this, msg.getField("error"), "Error", JOptionPane.ERROR_MESSAGE);
                log(msg.getField("error").toString());
                break;
            case LOGIN:
                log("Welcome user " + msg.getField("source") + "!");
                break;
            case LOGOFF:
                log("User " + msg.getField("username") + " has left.");
                break;
            case MESSAGE:
                //TODO(naugler) messaging?
                log(msg.getField("source") + ": " + msg.getField("message"));
                break;
            case UPDATE:
                handleUpdate(msg);
                break;
            // Note: Do opposite since message is coming from the server 
            // So when suggest comes in, we need to respond to the suggestion
            // If the response comes in, we need to tell original user.
            case ACCUSE:
            	boolean won = (boolean) msg.getField("win");
            	if (won)
            	{
            		// Show appropriate message
            	}
            	else
            	{
            		// Show appropriate message
            	}
            case SUGGEST:
            	System.out.println("You are being asked to respond to suggestion");
            	System.out.println(msg);
            	// Show suggestion message to user.  
                
//            	// This method will be called in the "OK" of the suggestion
//            	userEngine.respondToSuggestion(card, msg);
                break;
            case RESP_SUGGEST:
            	// Show resulting card if card exists and this user made suggestion

//            	//THe suggest method will be called on the suggest button
//            	userEngine.makeSuggestion(person, weapon);
            	break;
            case MOVE:
            	String player = (String) msg.getField("player");
            	int pos = (int) msg.getField("position");
            	System.out.println("Player " + player + " new position " + pos + "if up should be -6" );
            	// move the piece to the correct place in the graphics
            	// alert player in log box
            	break;
            case END_TURN:
            	// turn off all buttons
            	break;
            case NEXT_TURN:
            	// turn on appropriate buttons
            	break;
            default:
                log("Unknown message: " + msg);
        }
    }

    /**
     * This updates the GUI to reflect the current state of the game.
     *
     * @param msg
     */
    private void handleUpdate(CluelessMessage msg) {
        //what kind of message was?
        //movement - move a piece
        //change game board
        //update game pad
        //suggestions? - show suggestion popup

    }

    public void log(String message) {
        log.append(message + "\n");
    }

    private void shutdown() {
        if (JOptionPane.showConfirmDialog(this, "Are you sure you would like to exit?") == JOptionPane.OK_OPTION) {
            userEngine.shutdown();
        }
    }

    @Override
    public void event(Connection connection, Connection.ConnectionEvent event) {
        if (event == ConnectionEvent.OPEN) {
            newGame.setEnabled(false);
            joinGame.setEnabled(false);
        } else {
            newGame.setEnabled(true);
            joinGame.setEnabled(true);
        }
    }

}
