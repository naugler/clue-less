package com.blakjack.clueless.common;

import java.awt.Color;
import java.io.IOException;
import java.io.Serializable;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import com.blakjack.clueless.client.CluelessClient;
import com.blakjack.clueless.common.CluelessMessage.Type;
import com.blakjack.clueless.gui.GameFrame;
import com.blakjack.clueless.server.CluelessServer;

public class Player implements Serializable {
    
    private static final long serialVersionUID = 1;

    private final Character character;

    private String username;
    private static final transient GameBoard board = new GameBoard();
    private Room room;

    // transient means it will not be sent over the wire. This way we can
    // use this class to send to everybody without worrying about accidentally
    // sending the player's cards
    private transient Connection connection;
    private transient List<Card> cards = new ArrayList<Card>();
    private transient CluelessServer server;
    private transient CluelessClient client;
    private transient boolean active = true;

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public enum Character {
        MUSTARD("Colonel Mustard", Color.yellow, board.getMustardStart()), 
        WHITE("Mrs. White", Color.white, board.getWhiteStart()), 
        PLUM("Professor Plum", new Color(127, 0, 255), board.getPlumStart()),
        PEACOCK("Mrs. Peacock", Color.blue, board.getPeacockStart()), 
        GREEN("Mr. Green", Color.green, board.getGreenStart()), 
        SCARLET("Miss Scarlet", Color.red, board.getScarletStart());

        private Character(String name, Color color, Room homeRoom) {
            this.charName = name;
            this.color = color;
            this.homeRoom = homeRoom;
        }

        private final Color color;
        private final String charName;
        private final Room homeRoom;

        public String getName() {
            return charName;
        }

        public Color getColor() {
            return color;
        }
        
        public Room getHomeRoom() {
            return homeRoom;
        }

        public static Character getCharacter(Color color) {
            for (Character p : Character.values()) {
                if (p.color.equals(color)) {
                    return p;
                }
            }
            return null;
        }

        public static Character getCharacter(String name) {
            for (Character p : Character.values()) {
                if (p.charName.equals(name)) {
                    return p;
                }
            }
            return null;
        }

    }

    public Player(Color color) {
        character = Character.getCharacter(color);
    }

    public Player(String name) {
        character = Character.getCharacter(name);
    }

    public Player(Player p) {
        this.username = p.getUsername();
        this.cards = p.getCards();
        this.character = p.getCharacter();
        this.room = p.getRoom();
    }

    public CluelessClient getClient() {
        return client;
    }

    public void setClient(CluelessClient client) {
        this.client = client;
    }

    public Character getCharacter() {
        return character;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String name) {
        username = name;
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public void dealCard(Card c) {
        cards.add(c);
    }

    public List<Card> getCards() {
        return cards;
    }
    
    public boolean getActive() {
        return active;
    }
    
    public void setActive(boolean active) {
        this.active = active;
    }

    public void makeSuggestion(String person, String weapon) {
        System.out.println("In UserEngine  Person = " + person + " weapon = " + weapon);
        CluelessMessage message = new CluelessMessage(Type.SUGGEST);
        message.setField("person", person);
        message.setField("weapon", weapon);
        System.out.println(message);
        sendToServer(message);
    }

    public void accuse(String person, String weapon, String room) {
        CluelessMessage message = new CluelessMessage(Type.ACCUSE);
        message.setField("person", person);
        message.setField("weapon", weapon);
        message.setField("room", room);
        sendToServer(message);
    }

    public void respondToSuggestion(Card card, CluelessMessage msg) {
        CluelessMessage message = new CluelessMessage(Type.RESP_SUGGEST);
        
        if (card != null) {
            Card c = Card.getCard(card.getName());
            message.setField("card", c);
            sendToServer(message);
        }
        
    }

    /*
     * includes UP, DOWN, LEFT, RIGHT, and SECRET
     */
    public void move(String direction) {
        System.out.println(" In USER ENGINE direction " + direction);
        // If direction is allowed
        CluelessMessage msg = new CluelessMessage(Type.MOVE);
        msg.setField("direction", direction);
        sendToServer(msg);

    }

    public void endTurn() {
        CluelessMessage msg = new CluelessMessage(Type.END_TURN);
        sendToServer(msg);
    }

    public void sendToServer(CluelessMessage msg) {
        client.send(msg);
    }

    public void sendToClient(CluelessMessage msg) {
        connection.send(msg);
    }

    public void connect(boolean startServer, int port, String address, String username, GameFrame gameFrame) throws IOException, UnknownHostException {
        if (startServer) {
            server = new CluelessServer(port);
            try {
                server.start();
            } catch (IOException ex) {
                server.stop();
                throw ex;
            }
        }
        client = new CluelessClient(username, address, port);
        client.addMessageHandler(gameFrame);
        client.addConnectionEventListener(gameFrame);
        try {
            client.start();
        } catch (UnknownHostException ex) {
            client.stop();
            throw ex;
        } catch (IOException ex) {
            client.stop();
            throw ex;
        }
    }

    public void shutdown() {
        if (client != null) {
            client.stop();
        }
        if (server != null) {
            server.stop();
        }
    }

    @Override
    public String toString() {
        String str = "Player: " + username + " Room: " + room;
        return str;
    }

}
