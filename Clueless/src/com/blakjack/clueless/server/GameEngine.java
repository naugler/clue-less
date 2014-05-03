package com.blakjack.clueless.server;

import com.blakjack.clueless.common.Card;
import com.blakjack.clueless.common.CluelessMessage;
import com.blakjack.clueless.common.GameBoard;
import com.blakjack.clueless.common.Movement;
import com.blakjack.clueless.common.Player;
import com.blakjack.clueless.common.CluelessMessage.Type;
import com.blakjack.clueless.common.Connection;
import com.blakjack.clueless.common.Connection.ConnectionEvent;
import com.blakjack.clueless.common.Player.Character;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import java.util.Date;

public class GameEngine implements Connection.MessageHandler,
        Connection.ConnectionEventListener {

    private static final int NUM_SQUARES = 25;
    // Integer is the port

    // List<UserEngine> users = new ArrayList<UserEngine>();
    List<Player> users = Collections.synchronizedList(new ArrayList<Player>());
    List<Character> availChars = new ArrayList<>();
    private static GameBoard board = new GameBoard();
    private static Deck deck = new Deck();
    // This index determines the players turn, the integer refers to the index
    // in the players list
    private static int playerTurnIndex = 0;
    // This keeps track of how many users were NOT able to refute suggestion
    private static int offsetToSuggestTo = 1;
    private static boolean refuted = false;
    // every other turn clear log
    boolean clearLog = false;

    public GameEngine() {
        // users.get(0).getPosition();
        for (Character p : Character.values()) {
            availChars.add(p);
        }

        // Set Crime - crime will be the first three cards of the deck in the
        // order, character, weapon, room
        deck.sort(); // People, weapons, rooms
        deck.shuffle(0, 5); // Character
        deck.shuffle(6, 11); // Weapon
        deck.shuffle(12, 20); // Room
        deck.swap(6, 1);
        deck.swap(12, 2);
        deck.restart();
        // Deal the crime cards so that the current card will be at the correct
        // place
        for (int i = 0; i < 3; i++) {
            deck.deal();
        }
        // Shuffle the rest of the deck
        deck.shuffle(3, 20);
    }

    private void setupGame() {
        // Deal the cards
        Card card = deck.deal();
        while (card != null) {
            for (Player p : users) {
                if (card != null) {
                    p.dealCard(card);
                    card = deck.deal();
                }
            }
        }
    }

    // TODO For now we will just give a random character - it is easiest
    // If there is time, we can allow the user to choose
    private void createPlayer(String username, Connection connection) {
        // boolean unusedFound = false;
        // Player player = null;
        // for (Character p : Character.values())
        // {
        // if (!unusedFound)
        // {
        // player = getPlayerFromCharacter(p);
        // if (player == null)
        // {
        // unusedFound = true;
        // player = new Player(p.getName());
        // }
        // }
        // }
        // UserEngine user = new UserEngine();
        Player player;

        // TODO: Possible issues with race conditions
        synchronized (availChars) {
            int rand = (int) (Math.random() * availChars.size());
            player = new Player(availChars.get(rand).getName());
            // In case there are race conditions, remove the actual Character
            // from the list without using index
            availChars.remove(player.getCharacter());
        }
        player.setUsername(username);
        player.setConnection(connection);
        player.setPosition(player.getCharacter().getHomePos());
        player.setRoom(player.getCharacter().getHomeRoom());
        // We know that the character is unique so we can do this
        // -1 is starting position

        addPlayer(player);
    }

    private int getPlayerFromCharacter(Character p) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getCharacter().equals(p)) {
                return i;
            }
        }
        return -1;
    }

    private void addPlayer(Player p) {
        // Scarlet always goes first so make sure they are put in the front of
        // the list
        if (p.getCharacter().equals(Character.SCARLET)) {
            users.add(0, p);
        } else {
            users.add(p);
        }

    }

    private void broadcast(CluelessMessage message) {
        message.setField("source", "server");
        message.setField("date", new Date(System.currentTimeMillis()));
        for (Player p : users) {
            p.getConnection().send(message);
        }
    }

    private CluelessMessage buildUpdate() {
        CluelessMessage msg = new CluelessMessage(Type.UPDATE);
        List<Player> usersCopy = new ArrayList<>();
        for (Player u : users) {
            Player p = new Player(u);
            usersCopy.add(p);
        }
        msg.setField("status", (Serializable) usersCopy);
        int turn = playerTurnIndex;
        msg.setField("turn", turn);

        return msg;
    }

    public Player getUser(Connection connection) {
        for (Player user : users) {
            if (user.getConnection().equals(connection)) {
                return user;
            }
        }
        return null;
    }

    private boolean checkUsername(String username) {
        for (Player p : users) {
            if (p.getUsername().equalsIgnoreCase(username)) {
                return true;
            }
        }
        return false;

    }

    private void login(Connection connection, CluelessMessage msg) {
        if (users.size() < 6) {
            String newUser = msg.getField("source").toString();
            boolean usernameExists = checkUsername(newUser);
            if (usernameExists) {
                CluelessMessage error = new CluelessMessage(Type.ERROR);
                error.setField("error", "Username already exists");
                connection.send(error);
                connection.close();
                return;
            }
            createPlayer(msg.getField("source").toString(), connection);
            CluelessMessage response = new CluelessMessage(Type.MESSAGE);
            response.setField("message", "Welcome " + newUser + "!");
            broadcast(response);
        } else {
            CluelessMessage error = new CluelessMessage(Type.ERROR);
            error.setField("error", "Game is full.");
            connection.send(error);
            connection.close();
            return;
        }
    }

    private void sendCards(CluelessMessage msg) {
        for (Player p : users) {
            msg.setField("cards", (Serializable) p.getCards());
            p.getConnection().send(msg);
        }
    }

    private void startFirstPlayer() {
        playerTurnIndex = 0;
        List<String> buttons = getEnabledButtons(users.get(playerTurnIndex));
        CluelessMessage message = new CluelessMessage(Type.NEXT_TURN);
        message.setField("buttons", (Serializable) buttons);
        message.setField("roomroom", users.get(playerTurnIndex).getRoom()
                .getName());
        users.get(playerTurnIndex).getConnection().send(message);
    }

    private boolean checkAccuse(CluelessMessage msg) {
        String character = (String) msg.getField("person");
        String weapon = (String) msg.getField("weapon");
        String room = (String) msg.getField("room");
        Card charact = Card.getCard(character);
        Card weap = Card.getCard(weapon);
        Card rm = Card.getCard(room);
        if (deck.getCrimeCard(0).equals(charact)
                && deck.getCrimeCard(1).equals(weap)
                && deck.getCrimeCard(2).equals(rm)) {
            return true;
        }
        return false;
    }

    @Override
    public void handle(Connection connection, CluelessMessage msg) {
        // This is a message that may be needed to pass along a different
        // message
        // Was having problems instantiating multiple messages in different case
        // values...
        CluelessMessage message;
        Type type = (Type) msg.getField("type");
        switch (type) {
        case LOGIN:
            login(connection, msg);
            break;
        case START:
            setupGame();
            // TODO(naugler) sanitize client message
            // broadcast(msg);
            message = new CluelessMessage(Type.START);
            message = CluelessMessage.copy(message, msg, "type", "source");

            // Send each user's cards to them
            sendCards(message);

            // Starts the game with the first player
            startFirstPlayer();

            break;
        case ACCUSE:
            // check if the user won
            boolean won = checkAccuse(msg);
            message = new CluelessMessage(Type.ACCUSE);
            message = CluelessMessage.copy(message, msg, "type");
            message.setField("win", won);
            message.setField("name", users.get(playerTurnIndex).getUsername());
            broadcast(message);
            break;
        case SUGGEST:
            offsetToSuggestTo = 1;
            refuted = false;
            System.out.println("IN Game Engine first time!!");
            // Get the room that the current player is in.
            Player currPlayer = users.get(playerTurnIndex);
            String roomroom = currPlayer.getRoom().getName();
            String charcter = (String) msg.getField("person");
            String weapon = (String) msg.getField("weapon");
            message = new CluelessMessage(Type.MESSAGE);
            message.setField("source", currPlayer.getUsername());
            message.setField("message", currPlayer.getUsername()
                    + " suggested \n" + charcter + " in the " + roomroom
                    + " \nwith the " + weapon);
            broadcast(message);
            message = new CluelessMessage(Type.SUGGEST);
            for (String key : msg.getFields().keySet()) {
                message.setField(key, (Serializable) msg.getField(key));

            }
            message.setField("roomroom", roomroom);

            // make sure to move the player who is the character to the room
            int index = getPlayerFromCharacter(Character.getCharacter(charcter));
            if (index != -1) {
                users.get(index).setRoom(currPlayer.getRoom());
                users.get(index).setPosition(currPlayer.getPosition());
                CluelessMessage m = new CluelessMessage(Type.MESSAGE);
                m.setField("message", users.get(index).getUsername()
                        + " was sent to the " + roomroom
                        + "\nbecause of the suggestion");
                broadcast(m);
            }

            // find out which try it is. (if a player was not able
            // refute a suggestion, send the same message using type
            // "SUGGEST" with a + 1 "try" value to try the next
            // player.

            while (offsetToSuggestTo < users.size() && refuted == false) {
                Player suggPlayer = users
                        .get((playerTurnIndex + offsetToSuggestTo)
                                % users.size());
                System.out.println("Asking suggested Player "
                        + suggPlayer.getUsername());
                System.out.println(message);
                suggPlayer.getConnection().send(message);
                synchronized (this) {
                    try {
                        this.wait();
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
            // if (offsetToSuggestTo < 6)
            // {
            // UserEngine suggPlayer = users.get((playerTurnIndex +
            // offsetToSuggestTo)%6);
            // suggPlayer.getPlayer().getConnection().send(msg);
            // }
            break;
        case RESP_SUGGEST:
            Card card = (Card) msg.getField("card");
            if (card == null) {
                message = new CluelessMessage(Type.MESSAGE);
                message.setField("source", (String) msg.getField("source"));
                message.setField(
                        "message",
                        users.get(
                                (playerTurnIndex + offsetToSuggestTo)
                                        % users.size()).getUsername()
                                + " could not refute the suggestion");
                broadcast(message);
                offsetToSuggestTo++;
            }
            // If player refuted suggestion, send current player the card
            // Send the rest of the players the results. (without the card)
            else {
                refuted = true;
                users.get(playerTurnIndex).getConnection().send(msg);
                message = new CluelessMessage(Type.MESSAGE);
                // for (String key : msg.getFields().keySet())
                // {
                // if (!key.equals("card"))
                // {
                // message.setField(key, (Serializable) msg.getField(key));
                // }
                // }
                message.setField(
                        "message",
                        users.get(
                                (playerTurnIndex + offsetToSuggestTo)
                                        % users.size()).getUsername()
                                + " refuted the suggestion");
                broadcast(message);

            }
            synchronized (this) {
                this.notifyAll();
            }
            break;
        case MOVE:

            String direction = (String) msg.getField("direction");
            System.out.println("in GAME ENGINE direction " + direction);
            Player curPlayer = users.get(playerTurnIndex);
            int currPos = curPlayer.getPosition();
            int newPos = currPos;
            // If move is allowed for current player
            // TODO: Do CHECK HERE
            switch (direction) {
            case "UP":
                // the check for if it is possible happens before we set
                // position
                newPos = currPos - 5;
                curPlayer.setRoom(curPlayer.getRoom().getUp());
                break;
            case "DOWN":
                newPos = currPos + 5;
                curPlayer.setRoom(curPlayer.getRoom().getDown());
                break;
            case "LEFT":
                newPos = currPos - 1;
                curPlayer.setRoom(curPlayer.getRoom().getLeft());
                break;
            case "RIGHT":
                newPos = currPos + 1;
                curPlayer.setRoom(curPlayer.getRoom().getRight());
                break;
            case "SECRET":
                newPos = Math.abs(currPos - 24);
                curPlayer.setRoom(curPlayer.getRoom().getShortcut());
                break;
            }
            curPlayer.setPosition(newPos);

            List<String> btns = getEnabledButtons(curPlayer);

            // Tell the player which buttons should be set now.
            message = new CluelessMessage(Type.MOVE);
            message.setField("buttons", (Serializable) btns);
            // roomroom is for the suggestionpanel so that when the player hits
            // suggest, the correct
            // room will be set.
            message.setField("roomroom", curPlayer.getRoom().getName());
            curPlayer.getConnection().send(message);

            break;
        case END_TURN:
            playerTurnIndex = (playerTurnIndex + 1) % users.size();
            if (clearLog) {
                message = new CluelessMessage(Type.CLEARLOG);
                broadcast(message);
            }
            clearLog = !clearLog;
            Player nextPlayer = users.get(playerTurnIndex);
            // tell next player that it is their turn
            // tell game frame which moves are valid
            message = new CluelessMessage(Type.NEXT_TURN);
            List<String> button = getEnabledButtons(nextPlayer);
            message.setField("buttons", (Serializable) button);

            // roomroom is here for setting the suggest panel so that the room
            // will be the room
            // the user is currently in
            message.setField("roomroom", nextPlayer.getRoom().getName());
            nextPlayer.getConnection().send(message);

        default:
            CluelessMessage error = new CluelessMessage(Type.ERROR);
            error.setField("error", "Unknown message type " + type);
            connection.send(msg);
        }
        broadcast(buildUpdate());
        /*
         * Server Received Messages: get Player and port Start Sever Join Server
         * Begin Game Movement(left, right, up, down, secret passage) Suggest
         * Respond to suggestion Accuse End Turn Quit
         */
        /*
         * Server Sent Messages Player Turn Player Moved Suggestion to player
         * Result of Suggestion Result of Accusation Result of Accusation to
         * other players Player Turn summary for log
         */

    }

    private List<String> getEnabledButtons(Player player) {
        // Player nextPlayer = users.get(playerTurnIndex);
        // tell next player that it is their turn
        // tell game frame which moves are valid
        List<String> buttons = new LinkedList<>();
        if (Movement.isDownValid(board, users, player)) {
            buttons.add("DOWN");
        }
        if (Movement.isLeftValid(board, users, player)) {
            buttons.add("LEFT");
        }
        if (Movement.isRightValid(board, users, player)) {
            buttons.add("RIGHT");
        }
        if (Movement.isUpValid(board, users, player)) {
            buttons.add("UP");
        }
        if (Movement.isShortcutValid(board, player)) {
            buttons.add("SECRET");
        }
        // TODO: need to check for suggestion
        if (Movement.isSuggestValid(board, player)) {
            buttons.add("SUGGEST");
        }
        // Once it is a persons turn they can accuse until they hit end turn
        buttons.add("ENDTURN");
        buttons.add("ACCUSE");
        return buttons;
    }

    @Override
    public void event(Connection connection, Connection.ConnectionEvent event) {
        if (event == ConnectionEvent.CLOSED) {
            Player user = null;
            for (Player u : users) {
                if (u.getConnection() == connection) {
                    user = u;
                    break;
                }
            }
            if (user != null) {
                users.remove(user);
                CluelessMessage logoff = new CluelessMessage(Type.LOGOFF);
                logoff.setField("username", user.getUsername());
                broadcast(logoff);
                broadcast(buildUpdate());
            } else {
                CluelessMessage error = new CluelessMessage(Type.ERROR);
                error.setField("error",
                        "Server error: unknown client disconnected");
                broadcast(error);
            }
        }
    }
}
