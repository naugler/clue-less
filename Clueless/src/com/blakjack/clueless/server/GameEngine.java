package com.blakjack.clueless.server;

import com.blakjack.clueless.common.Card;
import com.blakjack.clueless.common.CluelessMessage;
import com.blakjack.clueless.common.CluelessMessage.Type;
import com.blakjack.clueless.common.Connection;
import com.blakjack.clueless.common.Connection.ConnectionEvent;

import java.util.ArrayList;
import java.util.List;

import com.blakjack.clueless.common.Player;
import com.blakjack.clueless.common.SquareTile;
import com.blakjack.clueless.common.Player.Person;
import java.util.Date;

public class GameEngine implements Connection.MessageHandler, Connection.ConnectionEventListener {
    
	private static final int NUM_SQUARES = 25;
//	Integer is the port
	List<Player> players = new ArrayList<Player>();
	private static SquareTile[] gameboard = new SquareTile[NUM_SQUARES];
	private static Deck deck = new Deck();
        
	private static void initialize() 
	{
		for (int i = 0; i < NUM_SQUARES; i++)
		{
			gameboard[i] = new SquareTile(i);
		}
//		Set Crime - crime will be the first three cards of the deck in the order, character, weapon, room
		deck.sort(); // People, weapons, rooms
		deck.shuffle(0, 5); // Character
		deck.shuffle(6, 11); // Weapon
		deck.shuffle(12, 20);  // Room
		deck.swap(6, 1);
		deck.swap(12, 2);
		// Deal the crime cards so that the current card will be at the correct place
		for (int i = 0; i < 3; i++)
		{
			deck.deal();
		}
//		Shuffle the rest of the deck
		deck.shuffle(3, 20);
	}
	
	private void beginGame()
	{
		// Deal the cards
		Card card = deck.deal();
		while (card != null)
		{
			for(Player p : players)
			{
				if (card != null)
				{
					p.dealCard(card);
					card = deck.deal();
				}
			}
		}		
	}
	
	private void createPlayer(String username, Connection connection)
	{
		boolean unusedFound = false;
		Player player = null;
		for (Person p : Person.values())
		{
			if (!unusedFound)
			{
				player = getPlayerFromPerson(p);
				if (player == null)
				{
					unusedFound = true;
					player = new Player(p.getName());
				}
			}
		}
		player.setUsername(username);
		player.setConnection(connection);
		addPlayer(player);
	}
	
	private void addPlayer(Player p)
	{
            //what?
		if (p.getPerson().equals(Person.SCARLET) )
		{
			players.add(0, p);
		}
		else
		{
			players.add(p);
		}
		
	}
	private Player getPlayerFromPerson(Person p)
	{
		for (Player player : players)
		{
			if (player.getPerson().equals(p))
			{
				return player;
			}
		}
		return null;
	}

        private void broadcast(CluelessMessage message) {
            message.setField("source", "server");
            message.setField("date", new Date(System.currentTimeMillis()));
            for (Player p : players) {
                p.getConnection().send(message);
            }
        }
        
        private CluelessMessage buildUpdate() {
            CluelessMessage msg = new CluelessMessage(Type.UPDATE);
            
            int turn = -1;
            int count = 0;
            for (Player p : players) {
                msg.setField("player"+count+"username", p.getUsername());
                msg.setField("player"+count+"character", p.getPerson().getName());
                msg.setField("player"+count+"position", p.getPosition());
                if (p.getTurn()) {
                    turn = count;
                }
                ++count;
            }
            msg.setField("turn", turn);
            
            return msg;
        }

    @Override
    public void handle(Connection connection, CluelessMessage msg) {
            Type type = (Type)msg.getField("type");
            switch(type) {
                case LOGIN:
                    if (players.size() < 6) {
                        String newUser = msg.getField("source").toString();
                        for (Player p : players) {
                            if (p.getUsername().equalsIgnoreCase(newUser)) {
                                CluelessMessage error = new CluelessMessage(Type.ERROR);
                                error.setField("error", "Username already exists");
                                connection.send(error);
                                connection.close();
                                return;
                            }
                        }
                        createPlayer(msg.getField("source").toString(), connection);
                        CluelessMessage response = new CluelessMessage(Type.MESSAGE);
                        response.setField("message", "Welcome "+newUser+"!");
                        broadcast(response);
                        broadcast(buildUpdate());
                    } else {
                        CluelessMessage error = new CluelessMessage(Type.ERROR);
                        error.setField("error", "Game is full.");
                        connection.send(error);
                        connection.close();
                        return;
                    }
                    break;
                default:
                    CluelessMessage error = new CluelessMessage(Type.ERROR);
                    error.setField("error", "Unknown message type "+type);
                    connection.send(msg);
            }
//        String type = (String) message.getField("TYPE");
//        switch (type.toUpperCase())
//        {
//        case "SET USERNAME":
//          if (type.equalsIgnoreCase("SET USERNAME"))
//          {
//        	  if (players.size() < 6)
//        	  {
//        		  String username = (String) message.getField("USERNAME");
//        		  int port = (int) message.getField("PORT");
//        		  createPlayer(username, port);
//        	  }
//        	  else
//        	  {
//        		  System.out.println("There are already 6 players: no more can join");
//        	  }
//          }
//          break;
//        }
/*   
 * Server Received Messages:
 *  get Player and port
 * 	Start Sever
 * 	Join Server
 *  Begin Game
 *  Movement(left, right, up, down, secret passage)
 *  Suggest
 *  Respond to suggestion
 *  Accuse
 *  End Turn
 *  Quit	
 */
 /* 
  * Server Sent Messages
  * Player Turn
  * Player Moved
  * Suggestion to player
  * Result of Suggestion
  * Result of Accusation
  * Result of Accusation to other players
  * Player Turn summary for log
  * 
  */
         
        //do something
//        String type = message.getField("TYPE");
//        if (type.equalsIgnoreCase("STARTGAME")) {
            //set player turns, cards, solution whatever,
            //notify first player that their turn has started.
//        }
    }    

    @Override
    public void event(Connection connection, Connection.ConnectionEvent event) {
        if (event == ConnectionEvent.CLOSED) {
            Player player = null;
            for (Player p : players) {
                if (p.getConnection() == connection) {
                    player = p;
                    break;
                }
            }
            if (player != null) {
                players.remove(player);
                CluelessMessage logoff = new CluelessMessage(Type.LOGOFF);
                logoff.setField("username", player.getUsername());
                broadcast(logoff);
            } else {
                CluelessMessage error = new CluelessMessage(Type.ERROR);
                error.setField("error", "Server error: unknown client disconnected");
                broadcast(error);
            }
        }
    }
}
