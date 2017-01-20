/**
 * This class is used to model the message for a network card game.
 * 
 * @author Kenneth Wong
 *
 */
public class CardGameMessage extends GameMessage {
	private static final long serialVersionUID = -4847411748052026276L;
	/**
	 * Sent by the server to a client when a connection is established. In this
	 * message, playerID specifies the playerID of the local player, and data is
	 * a reference to a regular array of strings specifying the names of the
	 * players
	 */
	public static final int PLAYER_LIST = 0;
	/**
	 * Sent by a client to the server when a connection is established. In this
	 * message, playerID specifies the playerID of the player, and data is
	 * simply null (not being used).
	 */
	public static final int JOIN = 1;
	/**
	 * Sent by the server to a client after a connection is established but the
	 * server is not able to serve this client because it is full. In this
	 * message, playerID is -1 (not being used) and data is simply null (not
	 * being used).
	 */
	public static final int FULL = 2;
	/**
	 * Broadcast by a server when a client loses connection to the server. In this message,
	 * playerID specifies the player who loses the connection to the server, and data is a string
	 * representation of the IP address and TCP port of this player.
	 */
	public static final int QUIT = 3;
	/**
	 * Sent by a client to the server to indicate it is ready for a new game. The server will also
	 * broadcast this message upon receiving it. In this message, playerID specifies the player who
	 * becomes ready for a new game (for the message broadcast by the server) or -1 (for the message
	 * sent by a client), and data is simply null (not being used).
	 */
	public static final int READY = 4;
	/**
	 * Broadcast by the server when all clients are ready for a new game. In this message, playerID is 
	 * -1 (no being used), and data is a reference to a Deck object (a shuffled deck for the new game).
	 */
	public static final int START = 5;
	/**
	 * Sent by a client when the local player makes a move. The server will broadcast this message upon
	 * receiving it. In this message, playerID specifies the player who makes the move, and data is a
	 * reference to an array of int specifying the indices of the cards being played.
	 */
	public static final int MOVE = 6;
	/**
	 * Sent by a client to the server when the local player press [ENTER] in the chat input field.
	 * The server will first add the name, IP address and TCP port of the player into the chat
	 * message, and then broadcast the message. In this message, playerID specifies the player who
	 * sent this chat message, and data is a reference to a string containing a formated chat message.
	 */
	public static final int MSG = 7;

	/**
	 * Creates and returns an instance of CardGameMessage.
	 * 
	 * @param type
	 *            the message type of this message
	 * @param playerID
	 *            the playerID of this message
	 * @param data
	 *            the data of this message
	 */
	public CardGameMessage(int type, int playerID, Object data) {
		super(type, playerID, data);
	}
}
