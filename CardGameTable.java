/**
 * An interface for a general card game table (GUI)
 * 
 * @author Kenneth Wong
 * 
 */
public interface CardGameTable {
	/**
	 * Sets the index of the active player (i.e., the current player).
	 * 
	 * @param activePlayer
	 *            an int value representing the index of the active player
	 */
	public void setActivePlayer(int activePlayer);

	/**
	 * Returns an array of indices of the cards selected.
	 * 
	 * @return an array of indices of the cards selected
	 */
	public int[] getSelected();

	/**
	 * Resets the list of selected cards to an empty list.
	 */
	public void resetSelected();

	/**
	 * Repaints the GUI.
	 */
	public void repaint();

	/**
	 * Prints the specified string to the message area of the card game table.
	 * 
	 * @param msg
	 *            the string to be printed to the message area of the card game
	 *            table
	 */
	public void printMsg(String msg);

	/**
	 * Clears the message area of the card game table.
	 */
	public void clearMsgArea();

	/**
	 * Resets the GUI.
	 */
	public void reset();

	/**
	 * Enables user interactions.
	 */
	public void enable();

	/**
	 * Disables user interactions.
	 */
	public void disable();
}
