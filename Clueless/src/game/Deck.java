package game;

public class Deck {
	private final int NUM_CARDS = 21;
	private Card[] cards = new Card[NUM_CARDS];
	private int curCard = 0;
	
	public Deck()
	{
		for (int i = 0; i < 21; i++)
		{
			cards = Card.values();
		}
	}
	
	public void shuffle() 
	{
		int i, j;

		for (i = 0; i < NUM_CARDS; i++) {
			j = (int) (NUM_CARDS * Math.random()); // in the deck

			/*
			 * --------------------------------- Swap these randomly picked
			 * cards ---------------------------------
			 */
			Card tmp = cards[i];
			cards[i] = cards[j];
			cards[j] = tmp;
		}
		curCard = 0;

	}
	
	public Card deal()
	{
		if (curCard < NUM_CARDS)
		{
			return cards[curCard++];
		}
		return null;
	}

}
