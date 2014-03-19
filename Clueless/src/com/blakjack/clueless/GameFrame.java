/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.blakjack.clueless;

import com.blakjack.clueless.client.CluelessClient;
import com.blakjack.clueless.server.CluelessServer;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * The main application frame for Clue-Less
 * 
 * @author nauglrj1
 */
public class GameFrame extends JFrame {
    
    private CluelessServer server;
    private CluelessClient client;
    
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
        this.setSize(600, 400);
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
            client = new CluelessClient(loginFrame.getAddress(), loginFrame.getPort());
            client.start(loginFrame.getUsername());
        }
        
        waitInLobby();
    }
    
    private void waitInLobby() {
        LobbyPanel lobby = new LobbyPanel();
    }
    
    private void shutdown() {
        //TODO(naugler) show confirmation dialog?
        if (client != null) {
            client.stop();
        }
        if (server != null) {
            server.stop();
        }
    }
    
}
