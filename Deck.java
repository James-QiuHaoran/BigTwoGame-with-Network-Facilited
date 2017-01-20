/**
 * This class is used to represent a deck of cards in general card games.
 * 
 * @author Kenneth Wong
 */
public class Deck extends CardList {
	private static final long serialVersionUID = -3886066435694112173L;
	
	/**
	 * Creates and returns an instance of the Deck class.
	 */
	public Deck() {
		initialize();
	}

	/**
	 * Initialize the deck of cards.
	 */
	public void initialize() {
		removeAllCards();
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 13; j++) {
				Card card = new Card(i, j);
				addCard(card);
			}
		}
	}

	/**
	 * Shuffles the deck of cards.
	 */
	public void shuffle() {
		for (int i = 0; i < this.size(); i++) {
			int j = (int) (Math.random() * this.size());
			if (i != j) {
				Card card = setCard(i, getCard(j));
				setCard(j, card);
			}
		}
	}
}
