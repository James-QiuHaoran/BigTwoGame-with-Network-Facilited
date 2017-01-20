/**
 * This is a subclass of class Deck.
 * It is used to model a deck of cards used in a Big Two card game.
 * @author HAORAN
 * @since 2016/10/13
 * @version 1.0
 *
 */
public class BigTwoDeck extends Deck {
	
	/**
	 * Default generated serialVersionUID
	 */
	private static final long serialVersionUID = 8254481884293570270L;

	/**
	 * Constructor
	 */
	public BigTwoDeck() {
		
	}
	
	/**
	 * This is a method for initializing a deck of Big Two cards.
	 * Remove all cards from the deck, create 52 Big Two cards and
	 * add them to the deck.
	 */
	public void initialize() {
		removeAllCards();

		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 13; j++) {
				BigTwoCard card = new BigTwoCard(i, j);
				addCard(card);
			}
		}
	}
	
}