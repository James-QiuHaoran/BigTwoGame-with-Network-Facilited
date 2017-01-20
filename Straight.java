import java.util.ArrayList;
/**
 * This is a subclass of Hand.
 * Straight is a hand of five cards with consecutive ranks.
 * @author HAORAN
 * @since 2016/10/15
 * @version 1.0
 *
 */
public class Straight extends Hand {

	/**
	 * Default generated serialVersionUID
	 */
	private static final long serialVersionUID = 2739990177258982901L;

	public Straight(CardGamePlayer player, CardList cards) {
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
			ArrayList<Integer> list = new ArrayList<Integer> ();
			int max = -1;
			for (int i = 0; i < size(); i++) {
				if (getCard(i).getRank() == 0)
					list.add(13);
				else if (getCard(i).getRank() == 1)
					list.add(14);
				else
					list.add(getCard(i).getRank());
				if (list.get(list.size()-1) >= max)
					max = list.get(list.size()-1);
			}
			if (!(list.contains(max) && list.contains(max-1) && list.contains(max-2) && list.contains(max-3) && list.contains(max-4)))
				return false;
		}
		return true;
	}

	/**
	 * This is a method for retrieving the type of this hand of cards.
	 */
	public String getType() {
		return "Straight";
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
	    if (hand.getType() == "Straight" && hand.getTopCard().compareTo(this.getTopCard()) < 0)
			return true;
		else
			return false;
	}

}