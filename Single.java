/**
 * This is a subclass of class Hand.
 * It is used to model a hand of single in a Big Two card Game.
 * @author HAORAN
 * @since 2016/10/15
 * @version 1.0
 *
 */
public class Single extends Hand {

	/**
	 * Defaulted generated serialVersionUID
	 */
	private static final long serialVersionUID = -4767109893813557115L;

	public Single(CardGamePlayer player, CardList cards) {
		super(player, cards);
		// TODO Auto-generated constructor stub
	}

	/**
	 * A method for checking if this is a valid hand.
	 */
	public boolean isValid() {
		// TODO Auto-generated method stub
		if (size() == 1)
			return true;
		else
			return false;
	}

	/**
	 * This method return the type of the hand of cards.
	 * If valid return "Single" else return null.
	 */
	String getType() {
		if (this.isValid())
			return "Single";
		else 
			return null;
	}
	
	/**
	 * This method retrieves the top card of this hand of cards.
	 * @return card
	 */
	public Card getTopCard() {
		return this.getCard(0);
	}
	
	/**
	 * This method judge whether this hand will beat that specified hand or not.
	 * @return true for yes, false otherwise.
	 */
	public boolean beats(Hand hand) {
		if (hand.getType() == "Single" && this.getTopCard().compareTo(hand.getTopCard()) > 0)
			return true;
		else
			return false;
	}

}