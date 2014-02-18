package game;

import java.awt.Color;
import java.util.ArrayList;

public class Player {
	
	private int position = -1;
	
	private Person person;
	
	private ArrayList<Card> cards = new ArrayList<Card>();
	
	private boolean myTurn = false;
	
	private enum Person {
		MUSTARD("Colonel Mustard", Color.yellow),
		WHITE("Mrs. White", Color.white),
		PLUM("Professor Plum", new Color(255,0,255)), //Purple 
		PEACOCK("Mrs. Peacock", Color.blue),
		GREEN("Mr. Green", Color.green),
		SCARLET("Miss Scarlet", Color.red);
		
		private Person(String name, Color color)
		{
			this.name = name;
			this.color = color;
		}
		private Color color;
		private String name;
		public String getName(){return name;}
		public Color getColor(){return color;}
		public static Person getPerson(Color color)
		{
			for (Person p : Person.values())
			{
				if (p.color.equals( color))
				{
					return p;
				}
			}
			return null;
		}
		public static Person getPerson(String name)
		{
			for (Person p : Person.values())
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
		person = Person.getPerson(color);
	}
	
	public Player( String name )
	{
		person = Person.getPerson(name);
	}
	
	public Person getPerson()
	{
		return person;
	}
	
	public void dealCard(Card c)
	{
		cards.add(c);
	}
	
	public ArrayList<Card> getCards()
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
	
	public void setTurn(boolean myTurn)
	{
		this.myTurn = myTurn;
	}
	
	public boolean getTurn()
	{
		return myTurn;
	}
	
}
