package com.blakjack.clueless.common;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class Player {
	
//	private int position = -1;
	
	private Character character;
	
	private String username;
	private Connection connection;
	private int position;
	private List<Card> cards = new ArrayList<Card>();
	
//	private Connection connection;
	
//	private ArrayList<Card> cards = new ArrayList<Card>();
	
	public enum Character {
		MUSTARD("Colonel Mustard", Color.yellow),
		WHITE("Mrs. White", Color.white),
		PLUM("Professor Plum", new Color(255,0,255)), //Purple 
		PEACOCK("Mrs. Peacock", Color.blue),
		GREEN("Mr. Green", Color.green),
		SCARLET("Miss Scarlet", Color.red);
		
		private Character(String name, Color color)
		{
			this.name = name;
			this.color = color;
		}
		private Color color;
		private String name;
		public String getName(){return name;}
		public Color getColor(){return color;}
		public static Character getCharacter(Color color)
		{
			for (Character p : Character.values())
			{
				if (p.color.equals( color))
				{
					return p;
				}
			}
			return null;
		}
		public static Character getCharacter(String name)
		{
			for (Character p : Character.values())
			{
				if (p.name == name)
				{
					return p;
				}
			}
			return null;
		}
			
	}
	public Player( Color color )
	{
		character = Character.getCharacter(color);
	}
	
	public Player( String name )
	{
		character = Character.getCharacter(name);
	}
	
	public Character getCharacter()
	{
		return character;
	}
	
	public String getUsername()
	{
		return username;
	}
	
	public void setUsername(String name)
	{
		username = name;
	}
	
	public void accuse()
	{
		
	}
	
	public void suggest()
	{
		
	}

	// to respond to suggestion
	public Card chooseCard()
	{
		return null;
	}
	
	public void move()
	{
		
	}
	
	public void endTurn()
	{
		
	}
	
	public void login()
	{
		
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
	
}
