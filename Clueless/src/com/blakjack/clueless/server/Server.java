package com.blakjack.clueless.server;

import com.blakjack.clueless.CluelessMessage;
import com.blakjack.clueless.Connection;
import java.util.ArrayList;

import com.blakjack.clueless.Player;
import com.blakjack.clueless.SquareTile;

public class Server implements Connection.MessageHandler {
    
	private static final int NUM_SQUARES = 25;
	ArrayList<Player> players = new ArrayList<Player>();
	private static SquareTile[] gameboard = new SquareTile[NUM_SQUARES];
	private static Deck deck = new Deck();
        
	public static void main(String[] args) 
	{
		for (int i = 0; i < NUM_SQUARES; i++)
		{
			gameboard[i] = new SquareTile(i);
		}
		//TODO: fill Players
	}

    @Override
    public void handle(Object msg) {
        CluelessMessage message = (CluelessMessage)msg;
        //do something
//        String type = message.getField("TYPE");
//        if (type.equalsIgnoreCase("STARTGAME")) {
            //set player turns, cards, solution whatever,
            //notify first player that their turn has started.
//        }
    }
}
