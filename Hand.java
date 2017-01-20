/**
 * This class is a subclass of CardList class.
 * It is used to model a hand of cards.
 * @author HAORAN
 * @since 2016/10/13
 * @version 1.0
 *
 */
public abstract class Hand extends CardList {

	/**
	 * Default generated serialVersionUID
	 */
	private static final long serialVersionUID = -3244172656591331634L;
	private CardGamePlayer player;
	
	/**
	 * This is a constructor setting the player and the cards.
	 * @param player
	 * @param cards
	 */
	public Hand(CardGamePlayer player, CardList cards) {
		// TODO Auto-generated constructor stub
		this.player = player;
		
		// set the card with the parameter CardList
		for (int i = 0; i < cards.size(); i++) {
			this.addCard(cards.getCard(i));
		}
		
		this.sort();
	}
	
	/**
	 * Getter method for private instance variable player.
	 * @return
	 */
	public CardGamePlayer getPlayer() {
		return player;
	}
	
	/**
	 * This method retrieved the top card of this hand.
	 * @return topCard
	 */
	public Card getTopCard() {
		int suit = 0, rank = 0;
		Card topCard = new Card(suit, rank);
		
		// single, pair and triple don't need to override this method
		topCard = getCard(0);
		for (int i = 1; i < size(); i++) {
			if (getCard(i).compareTo(topCard) > 0)
				topCard = getCard(i);
		}
		return topCard;
	}
	
	/**
	 * This method checks if this hand beats a specified hand.
	 * Single, Pair and Triple don't need to override this method.
	 * @param hand
	 * @return true or false
	 */
	public boolean beats(Hand hand) {
		boolean beats = false;
		if (this.getTopCard().compareTo(hand.getTopCard()) > 0)
			beats = true;
		return beats;
	}
	
	abstract boolean isValid();
	abstract String getType();

}