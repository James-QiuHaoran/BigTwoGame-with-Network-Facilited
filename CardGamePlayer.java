/**
 * This class is used to represent a player in general card games.
 * 
 * @author Kenneth Wong
 */
public class CardGamePlayer {
	private static int playerId = 0;
	private String name = "";
	private CardList cardsInHand = new CardList();

	/**
	 * Creates and returns an instance of the Player class.
	 */
	public CardGamePlayer() {
		this.name = "Player " + playerId;
		playerId++;
	}

	/**
	 * Creates and returns an instance of the Player class.
	 * 
	 * @param name
	 *            the name of the player
	 */
	public CardGamePlayer(String name) {
		this.name = name;
	}

	/**
	 * Returns the name of this player.
	 * 
	 * @return the name of this player
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Sets the name of this player.
	 * 
	 * @param name
	 *            the name of this player
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Adds the specified card to this player.
	 * 
	 * @param card
	 *            the specified card to be added to this player
	 */
	public void addCard(Card card) {
		if (card != null) {
			cardsInHand.addCard(card);
		}
	}

	/**
	 * Removes the list of cards from this player, if they are held by this
	 * player.
	 * 
	 * @param cards
	 *            the list of cards to be removed from this player
	 */
	public void removeCards(CardList cards) {
		for (int i = 0; i < cards.size(); i++) {
			cardsInHand.removeCard(cards.getCard(i));
		}
	}

	/**
	 * Removes all cards from this player.
	 */
	public void removeAllCards() {
		cardsInHand = new CardList();
	}

	/**
	 * Returns the number of cards held by this player.
	 * 
	 * @return the number of cards held by this player
	 */
	public int getNumOfCards() {
		return cardsInHand.size();
	}

	/**
	 * Sorts the list of cards held by this player.
	 */
	public void sortCardsInHand() {
		cardsInHand.sort();
	}

	/**
	 * Returns the list of cards held by this player.
	 * 
	 * @return the list of cards held by this player
	 */
	public CardList getCardsInHand() {
		return cardsInHand;
	}

	/**
	 * Returns the list of cards played by this player.
	 * 
	 * @param cardIdx
	 *            the list of the indices of the cards
	 * @return the list of cards played by this player, or null if the list of
	 *         cards is empty
	 */
	public CardList play(int[] cardIdx) {
		if (cardIdx == null) {
			return null;
		}

		CardList cards = new CardList();
		for (int idx : cardIdx) {
			if (idx >= 0 && idx < cardsInHand.size()) {
				cards.addCard(cardsInHand.getCard(idx));
			}
		}

		if (cards.isEmpty()) {
			return null;
		} else {
			return cards;
		}
	}
}
