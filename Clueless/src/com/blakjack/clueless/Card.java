package com.blakjack.clueless;

public enum Card {
	MUSTARD("Colonel Mustard", "person"),
	WHITE("Mrs. White", "person"),
	PLUM("Professor Plum", "person"),
	PEACOCK("Mrs. Peacock", "person"),
	GREEN("Mr. Green", "person"),
	SCARLET("Miss Scarlet", "person"),
	DAGGER("Dagger","weapon"),
	ROPE("Rope", "weapon"),
	PIPE("Lead Pipe", "weapon"),
	CANDLESTICK("Candlestick", "weapon"),
	REVOLVER("Revolver", "weapon"),
	WRENCH("Wrench", "weapon"),
	KITCHEN("Kitchen", "room"),
	BALLROOM("Ballroom", "room"),
	DININGROOM("Dining Room", "room"),
	LOUNGE("Lounge", "room"),
	HALL("Hall", "room"),
	CONSERVATORY("Conservatory", "room"),
	BILLIARD("Billiard Room", "room"),
	LIBRARY("Library", "room"),
	STUDY("Study", "room");
	
	private Card(String name, String type)
	{
		this.name = name;
		this.type = type;
	}
	private String type;
	private String name;
	public String getName(){return name;}
	public String getType(){return type;}
}
