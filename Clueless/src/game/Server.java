package game;

import java.util.ArrayList;

public class Server {
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
}
