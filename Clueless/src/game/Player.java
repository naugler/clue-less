package game;

import java.awt.Color;

public class Player {
	
	int position = -1;
	
	Person person;
	
	Card[] cards;
	
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
			
	}
}
