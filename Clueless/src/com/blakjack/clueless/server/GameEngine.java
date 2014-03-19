package com.blakjack.clueless.server;

import com.blakjack.clueless.Card;
import com.blakjack.clueless.CluelessMessage;
import com.blakjack.clueless.Connection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.blakjack.clueless.Player;
import com.blakjack.clueless.SquareTile;
import com.blakjack.clueless.Player.Person;

public class GameEngine implements Connection.MessageHandler {
    
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
	
	private void joinGame(Player p)
	{
		if (p.getPerson().equals(Person.SCARLET) )
		{
			players.add(0, p);
		}
		else
		{
			players.add(p);
		}
		
	}

    @Override
    public void handle(Object msg) {
        CluelessMessage message = (CluelessMessage)msg;
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
}
