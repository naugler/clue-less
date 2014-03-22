/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.blakjack.clueless;

import com.blakjack.clueless.Connection.MessageHandler;
import com.blakjack.clueless.client.CluelessClient;
import com.blakjack.clueless.server.CluelessServer;
import java.awt.BorderLayout;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;

/**
 * The main application frame for Clue-Less
 * 
 * @author nauglrj1
 */
public class GameFrame extends JFrame implements MessageHandler {
    
    private CluelessServer server;
    private CluelessClient client;
    
    private final JTextArea log = new JTextArea();
    
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
    }
    
    private void initializeComponents() {
        setSize(600, 400);
        setLayout(new BorderLayout());
        Menu fileMenu = new Menu("File");
        Menu helpMenu = new Menu("Help");
        
        MenuItem newGame = new MenuItem("New Game");
        newGame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                connect(true);
            }
        });
        fileMenu.add(newGame);
        
        MenuItem joinGame = new MenuItem("Join Game");
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
        
        log("Welcome to Clue-Less!");
        log.setEditable(false);
        add(log, BorderLayout.WEST);
    }
    
    private void connect(boolean startServer) {
        LoginPanel loginFrame = new LoginPanel(startServer);
        String title = startServer ? "New Game" : "Join Game";
        int retval = JOptionPane.showConfirmDialog(this, 
                loginFrame,
                title,
                JOptionPane.OK_CANCEL_OPTION);
        if (retval == JOptionPane.OK_OPTION) {
            //TODO(naugler) validate login configuration
            if (startServer) {
                server = new CluelessServer(loginFrame.getPort());
                server.start();
            }
            client = new CluelessClient(loginFrame.getUsername(), loginFrame.getAddress(), loginFrame.getPort());
            client.addMessageHandler(this);
            client.start();
        }
        
        waitInLobby(startServer);
    }
    
    private void waitInLobby(boolean startServer) {
        LobbyPanel lobby = new LobbyPanel(startServer);
    }
    
    @Override
    public void handle(Connection connection, CluelessMessage msg) {
        CluelessMessage.Type type = (CluelessMessage.Type)msg.getField("type");
        switch(type) {
            case ERROR:
                JOptionPane.showMessageDialog(this, msg.getField("error"), "Error", JOptionPane.ERROR_MESSAGE);
                log(msg.getField("error").toString());
                break;
            case LOGIN:
                log("Welcome user "+msg.getField("username")+"!");
                break;
            case LOGOFF:
                log("User "+msg.getField("username")+" has left.");
                break;
            case MESSAGE:
                log(msg.getField("source")+": "+msg.getField("message"));
                break;
            case UPDATE:
                handleUpdate(msg);
                break;
            default:
                log("Unknown message: "+msg);
        }
    }
    
    /**
     * This updates the GUI to reflect the current state of the game.
     * @param msg 
     */
    private void handleUpdate(CluelessMessage msg) {
        
    }
    
    public void log(String message) {
        log.append(message+"\n");
    }
    
    private void shutdown() {
        if (JOptionPane.showConfirmDialog(this, "Are you sure you would like to exit?") == JOptionPane.OK_OPTION) {
            if (client != null) {
                client.stop();
            }
            if (server != null) {
                server.stop();
            }
        }
    }
    
}
