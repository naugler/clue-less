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
    // for Room class, if we use it
    private static final transient GameBoard board = new GameBoard();
    private transient Room room;
    private int position;

    // transient means it will not be sent over the wire. This way we can
    // use this class to send to everybody without worrying about accidentally
    // sending the player's cards
    private transient Connection connection;
    private transient List<Card> cards = new ArrayList<Card>();
    private transient CluelessServer server;
    private transient CluelessClient client;

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public enum Character {

        MUSTARD("Colonel Mustard", Color.yellow, 9, board.getLounge().getDown()), 
        WHITE("Mrs. White", Color.white, 23, board.getKitchen().getLeft()), 
        PLUM("Professor Plum", new Color(127, 0, 255), 5, board.getStudy().getDown()), // Purple
        PEACOCK("Mrs. Peacock", Color.blue, 15, board.getLibrary().getDown()), 
        GREEN("Mr. Green", Color.green, 21, board.getBallroom().getLeft()), 
        SCARLET("Miss Scarlet", Color.red, 3, board.getLounge().getLeft());

        private Character(String name, Color color, int homePos, Room homeRoom) {
            this.charName = name;
            this.color = color;
            this.homePos = homePos;
            this.homeRoom = homeRoom;
        }

        private final Color color;
        private final String charName;
        private final int homePos;
        private final Room homeRoom;

        public String getName() {
            return charName;
        }

        public Color getColor() {
            return color;
        }

        public int getHomePos() {
            return homePos;
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
        this.cards = p.getCards();
        this.character = p.getCharacter();
        this.position = p.getPosition();
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

    public int getPosition() {
        return position;
    }

    public void setPosition(int pos) {
        position = pos;
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
//        for (String key : msg.getFields().keySet()) {
//            if (!key.equals("type")) {
//                message.setField(key, (Serializable) msg.getField(key));
//            }
//        }
        message.setField("card", card);
        sendToServer(message);
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
		  //TODO: Maybe here????
//		  this.username = username;
        //TODO(naugler) validate login parameters
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

    public String toString() {
        String str = "Player: " + username + " Position: " + position;
        return str;
    }

}
