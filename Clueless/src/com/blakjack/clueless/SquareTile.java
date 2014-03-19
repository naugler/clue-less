package com.blakjack.clueless;

public class SquareTile {
	Player occupyingPlayer = null;
	RoomType room;
	private enum RoomType {HALLWAY, ROOM, CORNER, BLANK}
	
	public SquareTile(int position)
	{
		switch (position)
		{
		case 0: case 4: case 20: case 24:
			room = RoomType.CORNER;
			break;
		case 2: case 10: case 12: case 14: case 22:
			room = RoomType.ROOM;
			break;
		case 1: case 3: case 5: case 7: case 9: case 11: case 13: case 15: case 17: case 19: case 21: case 23:
			room = RoomType.HALLWAY;
			break;
		default: 
			room = RoomType.BLANK;
		}
	}

}
