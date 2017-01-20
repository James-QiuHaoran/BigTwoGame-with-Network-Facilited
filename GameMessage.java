import java.io.Serializable;

/**
 * This class is used to model the message for a network game.
 * @author Kenneth Wong
 *
 */
public class GameMessage implements Serializable {
	private static final long serialVersionUID = -9138385504565085818L;
	private int type;
	private int playerID;
	private Object data;
	
	/**
	 * Creates and returns an instance of the GameMessage class.
	 * @param type the type of this message
	 * @param playerID the playerID of this message
	 * @param data the data of this message
	 */
	public GameMessage(int type, int playerID, Object data) {
		this.type = type;
		this.playerID = playerID;
		this.data = data;
	}
	
	/**
	 * Returns the type of this message.
	 * @return the type of this message
	 */
	public int getType() {
		return this.type;
	}
	
	/**
	 * Sets the type of this message.
	 * @param type the type of this message
	 */
	public void setType(int type) {
		this.type = type;
	}
	
	/**
	 * Returns the playerID of this message.
	 * @return the playerID of this message
	 */
	public int getPlayerID() {
		return this.playerID;
	}
	
	/**
	 * Sets the playerID of this message.
	 * @param playerID the playerID of this message
	 */
	public void setPlayerID(int playerID) {
		this.playerID = playerID;
	}
	
	/**
	 * Returns the data of this message.
	 * @return the data of this message
	 */
	public Object getData() {
		return this.data;
	}
	
	/**
	 * Sets the data of this message.
	 * @param data the data of this message
	 */
	public void setData(Object data) {
		this.data = data;
	}
}
