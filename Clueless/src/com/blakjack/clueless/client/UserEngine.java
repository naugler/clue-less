package com.blakjack.clueless.client;

import java.util.ArrayList;
import java.util.List;

import com.blakjack.clueless.common.Card;
import com.blakjack.clueless.common.Connection;
import com.blakjack.clueless.common.Player;

public class UserEngine {
	private Player player;
	private Connection connection;
	private int position;
	private List<Card> cards = new ArrayList<Card>();
	
//	TODO Not sure if I want this here or in player...
//	private String username;
	
	// TODO Here is where the logic will be called for determining what a user can do.
	
	
	public Player getPlayer()
	{
		return player;
	}
	
	public void setPlayer(Player p)
	{
		this.player = p;
	}
	
	public Connection getConnection()
	{
		return connection;
	}
	
	public void setConnection(Connection connection)
	{
		this.connection = connection;
	}
	
	public void dealCard(Card c)
	{
		cards.add(c);
	}
	
	public List<Card> getCards()
	{
		return cards;
	}
	
	public int getPosition()
	{
		return position;
	}
	
	public void setPosition(int pos)
	{
		position = pos;
	}
	
//	public String getUsername()
//	{
//		return username;
//	}
//	
//	public void setUsername(String name)
//	{
//		username = name;
//	}
	
	
	
}
