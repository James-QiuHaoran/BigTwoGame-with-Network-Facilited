/**
 * This is a subclass of Hand.
 * Triple is defined as three cards with the same rank.
 * @author HAORAN
 * @since 2016/10/15
 * @version 1.0
 *
 */
public class Triple extends Hand {

	/**
	 * Default generated serialVersionUID
	 */
	private static final long serialVersionUID = 1342742035651056741L;

	public Triple(CardGamePlayer player, CardList cards) {
		super(player, cards);
		// TODO Auto-generated constructor stub
	}

	/**
	 * A method for checking if this is a valid hand.
	 */
	public boolean isValid() {
		if (this.size() == 3 && this.getCard(0).rank == this.getCard(1).rank && getCard(0).rank == getCard(2).rank)
			return true;
		else
			return false;
	}

	/**
	 * This is a method for retrieving the type of this hand of cards.
	 */
	public String getType() {
		return "Triple";
	}
	
	/**
	 * This method retrieves the top card of this hand of cards.
	 * @return card
	 */
	public Card getTopCard() {
		Card topCard;
		int max = this.getCard(0).getSuit();
		topCard = this.getCard(0);
		for (int i = 1; i < 3; i++) {
			if (this.getCard(i).getSuit() >= max) {
				max = this.getCard(i).getSuit();
				topCard = this.getCard(i);
			}
		}
		return topCard;
	}
	
	/**
	 * This method judge whether this hand will beat that specified hand or not.
	 * @return true for yes, false otherwise.
	 */
	public boolean beats(Hand hand) {
		if (hand.getType() == "Triple" && hand.getTopCard().compareTo(this.getTopCard()) < 0)
			return true;
		else
			return false;
	}

}