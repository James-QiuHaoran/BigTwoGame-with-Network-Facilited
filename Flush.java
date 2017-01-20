/**
 * This is a subclass of class Hand.
 * It models a hand of Flush which have four cards with the same suit.
 * @author HAORAN
 * @since 2016/10/15
 * @version 1.0
 *
 */
public class Flush extends Hand {

	/**
	 * Default generated serialVersionUID
	 */
	private static final long serialVersionUID = -7213542241275104442L;

	public Flush(CardGamePlayer player, CardList cards) {
		super(player, cards);
		// TODO Auto-generated constructor stub
	}

	/**
	 * A method checking whether this is valid or not.
	 */
	public boolean isValid() {
		if (size() != 5)
			return false;
		else {
			for (int i = 0; i < 4; i++)
				if (getCard(i).getSuit() != getCard(4).getSuit())
					return false;
		}
		return true;
	}

	/**
	 * This is a method for retrieving the type of this hand of cards.
	 */
	public String getType() {
		return "Flush";
	}
	
	/**
	 * This method retrieves the top card of this hand of cards.
	 * @return card
	 */
	public Card getTopCard() {
		Card topCard;
		topCard = this.getCard(0);
		
		for (int i = 1; i < 5; i++) {
			if (this.getCard(i).compareTo(topCard) > 0)
				topCard = this.getCard(i);
		}
		return topCard;
	}
	
	/**
	 * This method judge whether this hand will beat that specified hand or not.
	 * @return true for yes, false otherwise.
	 */
	public boolean beats(Hand hand) {
		if (hand.getType() == "Straight")
			return true;
		else if (hand.getType() == "Flush" && hand.getTopCard().compareTo(this.getTopCard()) < 0)
			return true;
		else
			return false;
	}

}