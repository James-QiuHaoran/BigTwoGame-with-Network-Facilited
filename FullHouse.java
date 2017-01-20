import java.util.ArrayList;
/**
 * This is a subclass of Hand.
 * It models a hand of FullHouse which contains two cards with the same rank and another three with a different rank.
 * @author HAORAN
 * @since 2016/10/15
 * @version 1.0
 *
 */
public class FullHouse extends Hand {

	/**
	 * Default generated serialVersionUID
	 */
	private static final long serialVersionUID = 1185706523090495950L;

	public FullHouse(CardGamePlayer player, CardList cards) {
		super(player, cards);
		// TODO Auto-generated constructor stub
	}

	/**
	 * A method checking whether this is valid or not.
	 */
	public boolean isValid() {
		ArrayList<Integer> list = new ArrayList<Integer>();
		if (size() != 5)
			return false;
		else {
			for (int i = 0; i < 5; i++) {
				if (!list.contains(getCard(i).getRank()))
					list.add(getCard(i).getRank());
			}
			if (list.size() != 2)
				return false;
			else {
				int num1 = 0;
				int num2 = 0;
				for (int i = 0; i < 5; i++) {
					if (list.get(0) == getCard(i).getRank())
						num1++;
					else if (list.get(1) == getCard(i).getRank())
						num2++;
				}
				if (!(num1 == 2 || num2 == 2))
					return false;
			}
		}
		return true;
	}

	/**
	 * This is a method for retrieving the type of this hand of cards.
	 */
	public String getType() {
		return "FullHouse";
	}
	
	/**
	 * This method retrieves the top card of this hand of cards.
	 * @return card
	 */
	public Card getTopCard() {
		Card topCard;
		//topCard = this.getCard(0);
		ArrayList<Integer> list = new ArrayList<Integer>();
		ArrayList<Card> list1 = new ArrayList<Card>();
		ArrayList<Card> list2 = new ArrayList<Card>();
		for (int i = 0; i < 5; i++) {
			if (!list.contains(getCard(i).getRank()))
				list.add(getCard(i).getRank());
		}
		for (int i = 0; i < 5; i++) {
			if (list.get(0) == getCard(i).getRank())
				list1.add(getCard(i));
			else
				list2.add(getCard(i));
		}
		
		if (list1.size() == 3) {
			topCard = list1.get(0);
			for (int i = 1; i < 3; i++) {
				if (list1.get(i).compareTo(list1.get(0)) > 0)
					topCard = list1.get(i);
			}
		}
		else {
			topCard = list2.get(0);
			for (int i = 1; i < 3; i++) {
				if (list2.get(i).compareTo(list2.get(0)) > 0)
					topCard = list2.get(i);
			}
		}
		return topCard;
	}
	
	/**
	 * This method judge whether this hand will beat that specified hand or not.
	 * @return true for yes, false otherwise.
	 */
	public boolean beats(Hand hand) {
		if (hand.getType() == "Straight" || hand.getType() == "Flush")
			return true;
		else if (hand.getType() == "FullHouse" && hand.getTopCard().compareTo(this.getTopCard()) < 0)
			return true;
		else
			return false;
	}

}