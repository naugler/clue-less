package com.blakjack.clueless.server;

import com.blakjack.clueless.common.Card;

public class Deck {
	private final int NUM_CARDS = 21;
	private Card[] cards;
	private int curCard = 0;
	
	public Deck()
	{
		cards = Card.values();
	}
	
	/**
	 * Shuffle cards between pos1 and pos2 inclusive.  This is useful when needing to 
	 * shuffle each of the types separately for choosing the actual crime 
	 * stuff (one weapon, one room, one person) 
	 * pos2 should be greater than pos1
	 */
	public void shuffle(int pos1, int pos2) 
	{
		
		int i, j;
		if (pos1 >= 0 && pos1 < NUM_CARDS && pos2 >= 0 && pos2 < NUM_CARDS)
		{
			for (i = pos2; i >= pos1; i--) {
				j = (int) ((i - pos1) * Math.random()) + pos1; // in the deck

				/*
				 * --------------------------------- Swap these randomly picked
				 * cards ---------------------------------
				 */
				swap(i,j);
			}
		}
		else
			System.out.println("ERROR: Cannot shuffle cards that do not exist");

	}
	
	public void sort()
	{
		cards = Card.values();
	}
	
	public void swap(int i, int j)
	{
		if ((i < NUM_CARDS && i >= 0) && (j <NUM_CARDS && j >= 0))
		{
			Card tmp = cards[i];
			cards[i] = cards[j];
			cards[j] = tmp;
		}
	}
	
	public Card deal()
	{
		if (curCard < NUM_CARDS)
		{
			return cards[curCard++];
		}
		return null;
	}
	
	public void restart()
	{
		curCard = 0;
	}
	/*
	 * this is the only method here specific for our application
	 */
	public Card getCrimeCard(int index)
	{
		
		if (index >=0 && index <4)
		{
			return cards[index];
		}
		return null;
	}

}
