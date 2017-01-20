public interface NetworkGame {
	/**
	 * Returns the playerID (index) of the local player.
	 * 
	 * @return the playerID (index) of the local player
	 */
	public int getPlayerID();

	/**
	 * Sets the playerID (index) of the local player.
	 * 
	 * @param playerID
	 *            the playerID (index) of the local player.
	 */
	public void setPlayerID(int playerID);

	/**
	 * Returns the name of the local player.
	 * 
	 * @return the name of the local player
	 */
	public String getPlayerName();

	/**
	 * Sets the name of the local player.
	 * 
	 * @param playerName
	 *            the name of the local player
	 */
	public void setPlayerName(String playerName);

	/**
	 * Returns the IP address of the server.
	 * 
	 * @return the IP address of the server
	 */
	public String getServerIP();

	/**
	 * Sets the IP address of the server.
	 * 
	 * @param serverIP
	 *            the IP address of the server
	 */
	public void setServerIP(String serverIP);

	/**
	 * Returns the TCP port of the server.
	 * 
	 * @return the TCP port of the server
	 */
	public int getServerPort();

	/**
	 * Sets the TCP port of the server
	 * 
	 * @param serverPort
	 *            the TCP port of the server
	 */
	public void setServerPort(int serverPort);

	/**
	 * Makes a network connection to the server.
	 */
	public void makeConnection();

	/**
	 * Parses the specified message received from the server.
	 * 
	 * @param message
	 *            the specified message received from the server
	 */
	public void parseMessage(GameMessage message);

	/**
	 * Sends the specified message to the server.
	 * 
	 * @param message
	 *            the specified message to be sent the server
	 */
	public void sendMessage(GameMessage message);
}