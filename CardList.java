import java.io.Serializable;
import java.util.ArrayList;

/**
 * This class is used to represent a list of cards.
 * 
 * @author Kenneth Wong
 */
public class CardList implements Serializable {
	private static final long serialVersionUID = -3711761437629470849L;
	private ArrayList<Card> cards = new ArrayList<Card>();

	/**
	 * Appends the specified card to the end of this list.
	 * 
	 * @param card
	 *            the card to be appended to this list
	 */
	public void addCard(Card card) {
		if (card != null) {
			cards.add(card);
		}
	}

	/**
	 * Returns the card at the specified position in this list.
	 * 
	 * @param i
	 *            the index of the card to returned
	 * @return the card at the specified position in this list, or null if the
	 *         index is invalid
	 */
	public Card getCard(int i) {
		if (i >= 0 && i < cards.size()) {
			return cards.get(i);
		} else {
			return null;
		}
	}

	/**
	 * Removes the card at the specified position in this list. Shifts any
	 * subsequent cards to the left (subtracts one from their indices).
	 * 
	 * @param i
	 *            the index of the card to be removed
	 * @return the card that is removed from the list, or null if the index is
	 *         invalid
	 */
	public Card removeCard(int i) {
		if (i >= 0 && i < cards.size()) {
			return cards.remove(i);
		} else {
			return null;
		}
	}

	/**
	 * Removes the first occurrence of the specified card from this list, if it
	 * is present. If the list does not contain the card, it remains unchanged.
	 * Returns true if this list contained the specified card (or equivalently,
	 * if this list changed as a result of the call).
	 * 
	 * @param card
	 *            the card to be removed from this list, if presents
	 * @return true if this list contained the specified card; otherwise false
	 */
	public boolean removeCard(Card card) {
		return cards.remove(card);
	}

	/**
	 * Removes all cards from this list.
	 */
	public void removeAllCards() {
		cards = new ArrayList<Card>();
	}

	/**
	 * Replaces the card at the specified position in this list with the
	 * specified card.
	 * 
	 * @param i
	 *            the index of the card to be replaced
	 * @param card
	 *            the card to be stored at the specified position
	 * @return the card previously stored at the specified position, or null if
	 *         the index is invalid
	 */
	public Card setCard(int i, Card card) {
		if (i >= 0 && i < cards.size()) {
			return cards.set(i, card);
		} else {
			return null;
		}
	}

	/**
	 * Returns true if this list contains the specified card.
	 * 
	 * @param card
	 *            the card whose presence in this list is to be tested
	 * @return true if this list contains the specified card; otherwise false
	 */
	public boolean contains(Card card) {
		return cards.contains(card);
	}

	/**
	 * Returns true if this list contains no cards.
	 * 
	 * @return true if this list contains no cards; otherwise false
	 */
	public boolean isEmpty() {
		return cards.isEmpty();
	}

	/**
	 * Sorts this list according to the order of the cards.
	 */
	public void sort() {
		cards.sort(null);
	}

	/**
	 * Returns the number of cards in this list.
	 * 
	 * @return the number of cards in this list
	 */
	public int size() {
		return cards.size();
	}

	/**
	 * Prints the cards in this list to the console. Equivalent to calling
	 * print(true, false);
	 */
	public void print() {
		print(true, false);
	}

	/**
	 * Prints the cards in this list to the console.
	 * 
	 * @param printFront
	 *            a boolean value specifying whether to print the face (true) or
	 *            the black (false) of the cards
	 * @param printIndex
	 *            a boolean value specifying whether to print the index in front
	 *            of each card
	 */
	public void print(boolean printFront, boolean printIndex) {
		if (cards.size() > 0) {
			for (int i = 0; i < cards.size(); i++) {
				String string = "";
				if (printIndex) {
					string = i + " ";
				}
				if (printFront) {
					string = string + "[" + cards.get(i) + "]";
				} else {
					string = string + "[  ]";
				}
				if (i % 13 != 0) {
					string = " " + string;
				}
				System.out.print(string);
				if (i % 13 == 12 || i == cards.size() - 1) {
					System.out.println("");
				}
			}
		} else {
			System.out.println("[Empty]");
		}
	}

	/**
	 * Returns a string representation of the cards in the list
	 * 
	 * @return a string representation of the cards in the list
	 */
	public String toString() {
		String string = "";
		if (cards.size() > 0) {
			for (int i = 0; i < cards.size(); i++) {
				string = string + "[" + cards.get(i) + "]";
				if (i != cards.size() - 1) {
					string = string + " ";
				}
			}
		} else {
			string = "[Empty]";
		}

		return string;
	}
}
