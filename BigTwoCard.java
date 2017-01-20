/**
 * This is a subclass of the Card class.
 * BigTwoCard is used to model a card used in
 * a Big Two Card game.
 * @author HAORAN
 * @since 2016/10/13
 * @version 1.0
 *
 */
public class BigTwoCard extends Card {

	/**
	 * Default generated serialVersionUID
	 */
	private static final long serialVersionUID = -6882428045893598171L;

	/**
	 * This is a constructor for building a card
	 * with the specified suit and rank.
	 * @param suit
	 * @param rank
	 */
	public BigTwoCard(int suit, int rank) {
		super(suit, rank);
	}
	
	/**
	 * This is a method for comparing this card with the specified
	 * card for order.
	 * @param card
	 * @return an integer
	 */
	public int compareTo(Card card) {
		Card thiscard;
		if (this.rank == 0)
			thiscard = new Card(this.suit, 14);
		else if (this.rank == 1)
			thiscard = new Card(this.suit, 15);
		else
			thiscard = new Card(this.suit, this.rank);
		
		Card thatcard;
		if (card.getRank() == 0)
			thatcard = new Card(card.getSuit(), 14);
		else if (card.getRank() == 1)
			thatcard = new Card(card.getSuit(), 15);
		else
			thatcard = new Card(card.getSuit(), card.getRank());

		// compare
		if (thiscard.getRank() > thatcard.getRank())
			return 1;
		else if (thiscard.getRank() == thatcard.getRank()) {
			if (thiscard.getSuit() > thatcard.getSuit())
				return 1;
			else if (thiscard.getSuit() == thatcard.getSuit())
				return 0;
			else 
				return -1;
		}
		else
			return -1;
	}

}