/**
 * This is a subclass of superclass Hand.
 * Pair is two cards with the same rank.
 * @author HAORAN
 * @since 2016/10/15
 * @version 1.0
 *
 */
public class Pair extends Hand {

	/**
	 * Default generated serialVersionUID
	 */
	private static final long serialVersionUID = 8389486019879008947L;

	public Pair(CardGamePlayer player, CardList cards) {
		super(player, cards);
		// TODO Auto-generated constructor stub
	}

	/**
	 * A method for checking if this is a valid hand.
	 */
	public boolean isValid() {
		if (this.size() == 2 && this.getCard(0).rank == this.getCard(1).rank)
			return true;
		else
			return false;
	}

	/**
	 * This is a method for retrieving the type of this hand of cards.
	 */
	public String getType() {
		return "Pair";
	}
	
	/**
	 * This method retrieves the top card of this hand of cards.
	 * @return card
	 */
	public Card getTopCard() {
		Card topCard;
		topCard = this.getCard(0).getSuit() > this.getCard(1).getSuit() ? this.getCard(0) : this.getCard(1);
		return topCard;
	}
	
	/**
	 * This method judge whether this hand will beat that specified hand or not.
	 * @return true for yes, false otherwise.
	 */
	public boolean beats(Hand hand) {
		if (hand.getType() == "Pair" && hand.getTopCard().compareTo(this.getTopCard()) < 0)
			return true;
		else 
			return false;
	}

}
