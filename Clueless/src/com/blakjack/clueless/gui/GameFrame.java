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
import javax.swing.ImageIcon;

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
    private final GameBoardPanel gameBoard = new GameBoardPanel();
    private final PlayersPanel playersPanel = new PlayersPanel();
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
        
        setIconImage(new ImageIcon(getClass().getClassLoader().getResource("suggestBtn.png")).getImage());
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
        about.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new AboutDialog(GameFrame.this).setVisible(true);
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
        leftPanel.add(log, BorderLayout.CENTER);
        leftPanel.add(buttonPad, BorderLayout.SOUTH);
//        leftPanel.add(cardPanel, BorderLayout.SOUTH);
        add(leftPanel, BorderLayout.WEST);
        
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(playersPanel, BorderLayout.NORTH);
        centerPanel.add(gameBoard, BorderLayout.CENTER);
        add(centerPanel, BorderLayout.CENTER);
        
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.add(new EvidenceLocker(), BorderLayout.NORTH);
        rightPanel.add(cardPanel, BorderLayout.CENTER);
        add(rightPanel, BorderLayout.EAST);
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
        LobbyDialog lobby = new LobbyDialog(this, startServer, player);
        player.getClient().addMessageHandler(lobby);

        CluelessMessage login = new CluelessMessage(CluelessMessage.Type.LOGIN);
        player.getClient().send(login);

        lobby.setModal(true);
        lobby.setVisible(true);
        player.getClient().removeMessageHandler(lobby);
    }

    @Override
    public void handle(Connection connection, CluelessMessage msg) {
        CluelessMessage.Type type = (CluelessMessage.Type) msg.getField("type");
        List<Player> gameStatus = (List<Player>) msg.getField("status");
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
            	List<Card> cards = (List<Card>)msg.getField("cards");
            	cardPanel.setCards(cards);
            	cardPanel.repaint();
            	for (Card c : cards)
            	{
            		player.dealCard(c);
            	}
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
            		JOptionPane.showMessageDialog(this, "SOMEONE Won the game!!!!!");// Show appropriate message
            	}
            	else
            	{
            		JOptionPane.showMessageDialog(this, "Someone lost the game!!!");
            		// Show appropriate message
            	}
            	// Do something to exit
            	break;
            // Note: Do opposite since message is coming from the server 
            // So when suggest comes in, we need to respond to the suggestion
            // If the response comes in, we need to tell original user.
            case SUGGEST:
            	// Show suggestion message to user.
                List<Card> cardsInHand = player.getCards();
                List<Card> cardsToShow = new ArrayList<Card>();
                String suggestedPerson = (String)msg.getField("person");
                String suggestedWeapon = (String)msg.getField("weapon");
                String suggestedRoom = (String) msg.getField("roomroom");

                for (Card card : cardsInHand) {
                    if (card.getName().equalsIgnoreCase(suggestedPerson) ||
                        card.getName().equalsIgnoreCase(suggestedWeapon) ||
                        card.getName().equalsIgnoreCase(suggestedRoom)) {
                        cardsToShow.add(card);
                    }
                }
                Card response = null;
                if (cardsToShow.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "You were asked to refute suggestion but do not have any cards.");
                    CluelessMessage message = new CluelessMessage(com.blakjack.clueless.common.CluelessMessage.Type.RESP_SUGGEST);
                    message = CluelessMessage.copy(message, msg, "type");
//                	msg.setField("type", com.blakjack.clueless.common.CluelessMessage.Type.RESP_SUGGEST);
                    player.getClient().send(message);
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
            	Card c = (Card) msg.getField("card");
                if (c != null) {
                        JOptionPane.showMessageDialog(
                                this,
                                "Someone Showed you the card "
                                    + c.getName()
                                    + ". Once you hit OK, you will not be able to view the card anymore.");
                        buttonPad.setAllEnabled(false);
                        buttonPad.setBtnEnabled("ACCUSE", true);
                        buttonPad.setBtnEnabled("ENDTURN", true);
                }
            	break;
            case MOVE:
            	Object object = msg.getField("buttons");
            	List<String> butons = null;
            	buttonPad.setAllEnabled(false);
            	if (object instanceof List)
            	{
                    butons = (List<String>) object;
            	}
            	for (String button : butons)
            	{
                    if (!button.equals("UP") && !button.equals("DOWN") && !button.equals("LEFT") && !button.equals("RIGHT") && !button.equals("SECRET"))
                    {
                            buttonPad.setBtnEnabled(button, true);
                    }
                    else
                    {
                            buttonPad.setBtnEnabled(button, false);
                    }
            	}
            	buttonPad.setRoomForSug((String ) msg.getField("roomroom"));
            	
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
            	if (obj instanceof List) {
                    buttons = (List<String>) obj;
            	}
            	for (String button : buttons) {
                    buttonPad.setBtnEnabled(button, true);
            	}
            	buttonPad.setRoomForSug((String ) msg.getField("roomroom"));
            	
            	break;
            case CLEARLOG:
                log.setText("");
                break;
            default:
                log("Unknown message: " + msg);
        }
        if (gameStatus != null)
        {
            gameBoard.setPlayerPositions(gameStatus);
            playersPanel.setPlayers(gameStatus, (int)msg.getField("turn"));
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
