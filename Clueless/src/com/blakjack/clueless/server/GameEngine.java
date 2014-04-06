package com.blakjack.clueless.server;

import com.blakjack.clueless.client.UserEngine;
import com.blakjack.clueless.common.Card;
import com.blakjack.clueless.common.CluelessMessage;
import com.blakjack.clueless.common.Player;
import com.blakjack.clueless.common.CluelessMessage.Type;
import com.blakjack.clueless.common.Connection;
import com.blakjack.clueless.common.Connection.ConnectionEvent;
import com.blakjack.clueless.common.Player.Character;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.blakjack.clueless.common.SquareTile;







import java.util.Date;

public class GameEngine implements Connection.MessageHandler, Connection.ConnectionEventListener {
    
	private static final int NUM_SQUARES = 25;
//	Integer is the port
	
	List<UserEngine> users = new ArrayList<UserEngine>();
	List<Character> availChars = new ArrayList<Character>();
	private static SquareTile[] gameboard = new SquareTile[NUM_SQUARES];
	private static Deck deck = new Deck();
//	This index determines the players turn, the integer refers to the index in the players list
	private static int playerTurnIndex = 0;
//	This keeps track of how many users were NOT able to refute suggestion
    private static int offsetToSuggestTo = 1;
    private static boolean refuted = false;
	public GameEngine() 
	{
//		users.get(0).getPosition();
		for (Character p : Character.values())
		{
			availChars.add(p);
		}
		
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
			for(UserEngine u : users)
			{
				if (card != null)
				{
					u.getPlayer().dealCard(card);
					card = deck.deal();
				}
			}
		}		
	}
	
//	TODO For now we will just give a random character - it is easiest
//	If there is time, we can allow the user to choose
	private void createPlayer(String username, Connection connection)
	{
//		boolean unusedFound = false;
//		Player player = null;
//		for (Character p : Character.values())
//		{
//			if (!unusedFound)
//			{
//				player = getPlayerFromCharacter(p);
//				if (player == null)
//				{
//					unusedFound = true;
//					player = new Player(p.getName());
//				}
//			}
//		}
		UserEngine user = new UserEngine();
		Player player;
		
//		TODO: Possible issues with race conditions
		synchronized (availChars) {
			int rand = (int) (Math.random() * availChars.size());
			player = new Player(availChars.get(rand).getName());
	//		In case there are race conditions, remove the actual Character from the list without using index
			availChars.remove(player.getCharacter());
		}
		player.setUsername(username);
		user.setPlayer(player);
		user.getPlayer().setConnection(connection);
		
		addPlayer(user);
		
	}
	
	private Player getPlayerFromCharacter(Character p)
	{
		for (UserEngine user : users)
		{
			if (user.getPlayer().getCharacter().equals(p))
			{
				return user.getPlayer();
			}
		}
		return null;
	}
	
	private void addPlayer(UserEngine u)
	{
//        Scarlet always goes first so make sure they are put in the front of the list 
		if (u.getPlayer().getCharacter().equals(Character.SCARLET) )
		{
			users.add(0, u);
		}
		else
		{
			users.add(u);
		}
		
	}
	

        private void broadcast(CluelessMessage message) {
            message.setField("source", "server");
            message.setField("date", new Date(System.currentTimeMillis()));
            for (UserEngine u : users) {
                u.getPlayer().getConnection().send(message);
            }
        }
        
        private CluelessMessage buildUpdate() {
            CluelessMessage msg = new CluelessMessage(Type.UPDATE);
            int turn = playerTurnIndex;
            int count = 0;
            for (UserEngine u : users) {
                msg.setField("player"+count+"username", u.getPlayer().getUsername());
                msg.setField("player"+count+"character", u.getPlayer().getCharacter().getName());
                msg.setField("player"+count+"position", u.getPlayer().getPosition());
                ++count;
            }
            msg.setField("turn", turn);
            
            return msg;
        }
        
    public UserEngine getUser(Connection connection) {
        for (UserEngine user : users) {
            if (user.getPlayer().getConnection().equals(connection)) {
                return user;
            }
        }
        return null;
    }

    @Override
    public void handle(Connection connection, CluelessMessage msg) {
            Type type = (Type)msg.getField("type");
            switch(type) {
                case LOGIN:
                    if (users.size() < 6) {
                        String newUser = msg.getField("source").toString();
                        for (UserEngine u : users) {
                            if (u.getPlayer().getUsername().equalsIgnoreCase(newUser)) {
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
                case START:
                    //TODO(naugler) sanitize client message
                    broadcast(msg);
                    break;
                case SUGGEST:
                	offsetToSuggestTo = 1;
                	refuted = false;
                	// Get the room that the current player is in.
                	UserEngine currPlayer = users.get(playerTurnIndex);
                	String room = gameboard[currPlayer.getPlayer().getPosition()].getRoom();
	            	msg.setField("room", room);
                	
                	// find out which try it is. (if a player was not able 
                	// refute a suggestion, send the same message using type
                	// "SUGGEST" with a + 1 "try" value to try the next 
                	// player.
	            	
                	while (offsetToSuggestTo < 6 && refuted == false)
                	{
                		UserEngine suggPlayer = users.get((playerTurnIndex + offsetToSuggestTo)%6); 
		            	suggPlayer.getPlayer().getConnection().send(msg);
		            	synchronized(this)
		            	{
		            		try {
								this.wait();
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
		            	}
                	}
//                	if (offsetToSuggestTo < 6)
//                	{
//		            	UserEngine suggPlayer = users.get((playerTurnIndex + offsetToSuggestTo)%6); 
//		            	suggPlayer.getPlayer().getConnection().send(msg);
//                	}
                	break;
                case RESP_SUGGEST:
                	String card = (String) msg.getField("card");
                	if (card == null)
                	{
                		offsetToSuggestTo++;
                	}
                	// If player refuted suggestion, send current player the card
                	// Send the rest of the players the results. (without the card)
                	else
                	{
                		refuted = true;
                		users.get(playerTurnIndex).getPlayer().getConnection().send(msg);
                		CluelessMessage message = new CluelessMessage(Type.RESP_SUGGEST);
                		for (String key : msg.getFields().keySet())
                		{
                			if (key != "card")
                			{
                				message.setField(key, (Serializable) msg.getField(key));
                			}
                		}
                		broadcast(message);
                		
                	}
                	synchronized(this)
                	{
                		this.notifyAll();
                	}
                	break;
                    //loop through players (we will have to mark the message?)
                    //get player
//                	//tell them about it!
//                	suggestee.getConnection().send(msg);
                    
//                    for (UserEngine u : users) {
                        //don't know yet how we know which user should get the suggestion
//                        u.suggest(Card.WHITE, Card.WHITE, Card.PLUM);
//                    }
//                	UserEngine1		UserEngine2 		UserEngine3
//                	 - CLIENT		 - CLIENT		 - CLIENT
//                	 - SERVER
//                	 	GAMEENGINE
//	                	 	USER*
//	                	 		getValidmoves()
//	                	 		
//	                	 		PLAYER
//	                	 			CARDS
//	                	 			Character
//	                	 		CONNECTION
//                	
//                	Game engine
//               			list userEngines
//               	
//                	////
//                	UserEngine (user engine???)
//                		Connection
//                		Player
//                		
//                	UserEngine game = connections.get(suggestedPlayer)
//                    break;
                default:
                    CluelessMessage error = new CluelessMessage(Type.ERROR);
                    error.setField("error", "Unknown message type "+type);
                    connection.send(msg);
            }
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
            UserEngine user = null;
            for (UserEngine u : users) {
                if (u.getPlayer().getConnection() == connection) {
                    user = u;
                    break;
                }
            }
            if (user != null) {
                users.remove(user);
                CluelessMessage logoff = new CluelessMessage(Type.LOGOFF);
                logoff.setField("username", user.getPlayer().getUsername());
                broadcast(logoff);
                broadcast(buildUpdate());
            } else {
                CluelessMessage error = new CluelessMessage(Type.ERROR);
                error.setField("error", "Server error: unknown client disconnected");
                broadcast(error);
            }
        }
    }
}
