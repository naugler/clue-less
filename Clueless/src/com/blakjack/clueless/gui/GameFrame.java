/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.blakjack.clueless.gui;

import com.blakjack.clueless.common.Card;
import com.blakjack.clueless.common.CluelessMessage;
import com.blakjack.clueless.common.Connection;
import com.blakjack.clueless.common.Connection.MessageHandler;
import com.blakjack.clueless.common.Connection.ConnectionEvent;
import com.blakjack.clueless.common.Connection.ConnectionEventListener;
import com.blakjack.clueless.common.Player;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

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
//    private final UserEngine userEngine = new UserEngine();
    private final Player player = new Player("");
    private final ButtonPad buttonPad = new ButtonPad(player);
//    private static GameBoardStatus gameStatus = new GameBoardStatus();
    private final GameBoardPanel gameBoard = new GameBoardPanel();;
    /**
     * @param args the command line arguments
     */
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
        setMaximumSize(getSize());
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
        add(gameBoard, BorderLayout.CENTER);

        add(new EvidenceLocker(), BorderLayout.EAST);
    }

    private void connect(boolean startServer) {
        LoginPanel loginPanel = new LoginPanel(startServer);
        String title = startServer ? "New Game" : "Join Game";
        int retval = JOptionPane.showConfirmDialog(this,
                loginPanel,
                title,
                JOptionPane.OK_CANCEL_OPTION);
        if (retval == JOptionPane.OK_OPTION) {
            try {
                player.connect(startServer, loginPanel.getPort(), loginPanel.getAddress(), loginPanel.getUsername(), this);
            } catch (UnknownHostException ex) {
                JOptionPane.showMessageDialog(this, "Unknown host: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                return;
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Socket error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            waitInLobby(startServer);
        }

    }

    private void waitInLobby(boolean startServer) {
        LobbyDialog lobby = new LobbyDialog(startServer, player);
        player.getClient().addMessageHandler(lobby);

        CluelessMessage login = new CluelessMessage(CluelessMessage.Type.LOGIN);
        player.getClient().send(login);

        lobby.setModal(true);
        lobby.setVisible(true);
        player.getClient().removeMessageHandler(lobby);
    }

    @Override
    public void handle(Connection connection, CluelessMessage msg) {
        System.out.println("GAMEFRAME HANDLE: "+msg);
        CluelessMessage.Type type = (CluelessMessage.Type) msg.getField("type");
        List<Player> temp = (List<Player>) msg.getField("status");
        List<Player> gameStatus = null;
        if (temp != null)
        {
        	System.out.println(temp);
        	gameStatus = temp;
        }
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
            case START:
            	buttonPad.setAllEnabled(false);
            	break;
            case MESSAGE:
                //TODO(naugler) messaging?
                log(msg.getField("source") + ": " + msg.getField("message"));
                break;
            case UPDATE:
                handleUpdate(msg);
                break;
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
            // Note: Do opposite since message is coming from the server 
            // So when suggest comes in, we need to respond to the suggestion
            // If the response comes in, we need to tell original user.
            case SUGGEST:
            	// Show suggestion message to user.
                List<Card> cardsInHand = player.getCards();
                List<Card> cardsToShow = new ArrayList<Card>();
                String suggestedPerson = (String)msg.getField("person");
                String suggestedWeapon = (String)msg.getField("weapon");
                String suggestedRoom = "?"; //todo(naugler) how do i get the suggested room?
                for (Card card : cardsInHand) {
                    if (card.getName().equalsIgnoreCase(suggestedPerson) ||
                            card.getName().equalsIgnoreCase(suggestedWeapon) ||
                            card.getName().equalsIgnoreCase(suggestedRoom)) {
                        cardsToShow.add(card);
                    }
                }
                Card response = null;
                if (cardsToShow.isEmpty()) {
                    //todo(naugler)show dialog?
                } else {
                    response = (Card)JOptionPane.showInputDialog(this, 
                            "How will you refute the suggestion?",
                            "Input Required",
                            JOptionPane.QUESTION_MESSAGE,
                            null,
                            cardsToShow.toArray(),
                            cardsToShow.get(0));
                }
            	player.respondToSuggestion(response, msg);
                break;
            case RESP_SUGGEST:
            	// Show resulting card if card exists and this user made suggestion

//            	//THe suggest method will be called on the suggest button
//            	userEngine.makeSuggestion(person, weapon);
            	break;
            case MOVE:
//            	String player = (String) msg.getField("player");
//            	int pos = (int) msg.getField("position");
//            	System.out.println("Player " + player + " new position " + pos + "if up should be -6" );
            	// move the piece to the correct place in the graphics
            	// alert player in log box
            	break;
            case END_TURN:
            	buttonPad.setAllEnabled(false);
//            	userEngine.getClient().send(msg);
            	break;
            case NEXT_TURN:
            	// turn on appropriate buttons
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        JOptionPane.showMessageDialog(GameFrame.this, "It is now your turn!");
                    }
                });
            	Object obj = msg.getField("buttons");
            	List<String> buttons = null;
            	if (obj instanceof List)
            	{
            		buttons = (List<String>) obj;
            	}
            	for (String button : buttons)
            	{
            		buttonPad.setBtnEnabled(button, true);
            	}
            	
            	break;
            default:
                log("Unknown message: " + msg);
        }
        if (gameStatus != null)
        {
        	gameBoard.setPlayerPositions(gameStatus);
        }
        gameBoard.repaint();
        
    }

    /**
     * This updates the GUI to reflect the current state of the game.
     *
     * @param msg
     */
    private void handleUpdate(CluelessMessage msg) {
//    	gameStatus = (GameBoardStatus) msg.getField("status");
    }

    public void log(String message) {
        log.append(message + "\n");
    }

    private void shutdown() {
        if (JOptionPane.showConfirmDialog(this, "Are you sure you would like to exit?") == JOptionPane.OK_OPTION) {
            player.shutdown();
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
