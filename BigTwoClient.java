import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.JOptionPane;

/**
 * BigTwoClient is used to model a Big Two card game that supports 4 players over the Internet.
 * 
 * @author HAORAN
 * @version 1.0
 * @since 2016/11/24
 *
 */
public class BigTwoClient implements CardGame, NetworkGame {
	
	private int numOfPlayers;
	private Deck deck;
	private ArrayList<CardGamePlayer> playerList;
	private ArrayList<Hand> handsOnTable;
	private int playerID; // the index of the local player
	private String playerName; // the name of the local player
	private String serverIP; // a string specifying the IP address of the game server
	private int serverPort; // an integer specifying the TCP port of the game server
	private Socket sock; // a socket connection to the game server
	private ObjectOutputStream oos; // an ObjectOutputStream for sending messages to the server
	private int currentIdx;  // current player
	private BigTwoTable table; // GUI
	private ObjectInputStream ois; // an ObjectInputStream for receiving message from the server
	
	/**
	 * An inner class that implements the Runnable interface.
	 * @author HAORAN
	 *
	 */
	class ServerHandler implements Runnable{

		@Override
		/**
		 * Implements run() from Runnable interface.
		 * Upon receiving message, call parseMessage() method from network interface.
		 */
		public void run() {
			CardGameMessage message = null;
			try {
				while ((message = (CardGameMessage) ois.readObject()) != null) {
					parseMessage(message);
					System.out.println("Receiving messages~");
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			table.repaint();
		}
	}
	
	/**
	 * A constructor for creating a Big Two client.
	 */
	public BigTwoClient() {
		playerList = new ArrayList<CardGamePlayer>();
		for (int i = 0; i < 4; i++)
			playerList.add(new CardGamePlayer());
		handsOnTable = new ArrayList<Hand>();
		table = new BigTwoTable(this);
		table.disable();
		table.repaint();
		playerName = (String) JOptionPane.showInputDialog("Please enter your name: ");
		if (playerName == null || playerName.trim().isEmpty() == true)
			playerName = "Default_name";
		makeConnection();
		table.repaint();
	}

	@Override
	/**
	 * For getting the playerID, ie. the index of the local player.
	 */
	public int getPlayerID() {
		return playerID;
	}

	@Override
	/**
	 * A method for setting the playerID.
	 * This message should be called from the parseMessage() method when a message of the type PLAYER_LIST is received from the game server.
	 */
	public void setPlayerID(int playerID) {
		this.playerID = playerID;
	}

	@Override
	/**
	 * A method for getting the name of the local player.
	 */
	public String getPlayerName() {
		return playerName;
	}

	@Override
	/**
	 * A method for setting the name of the local player.
	 */
	public void setPlayerName(String playerName) {
		playerList.get(playerID).setName(playerName);
	}

	@Override
	/**
	 * A method for getting the IP address of the game server.
	 */
	public String getServerIP() {
		return serverIP;
	}

	@Override
	/**
	 * A method for setting the IP address of the game server.
	 */
	public void setServerIP(String serverIP) {
		this.serverIP = serverIP;
	}

	@Override
	/**
	 * A method for getting the TCP port of the game server.
	 */
	public int getServerPort() {
		return serverPort;
	}

	@Override
	/**
	 * A method for setting the TCP port of the game server.
	 */
	public void setServerPort(int serverPort) {
		this.serverPort = serverPort;
	}

	@Override
	/**
	 * A method for making a socket connection with the game server.
	 */
	public void makeConnection() {
		serverIP = "147.8.252.158";
		serverPort = 2396;
		try {
			sock = new Socket(this.serverIP, this.serverPort);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// create an ObjectOutputStream for sending messages to the game server
		try {
			oos = new ObjectOutputStream(sock.getOutputStream());
			ois = new ObjectInputStream(sock.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// create a thread for receiving messages from the server
		Runnable threadJob = new ServerHandler();
		Thread myThread = new Thread(threadJob);
		myThread.start();
		
		// send a message of the type JOIN to the game server, with playerID -1 and data being a reference to a string representing the name of the local player
		sendMessage(new CardGameMessage(1, -1, this.getPlayerName()));
		
		// send a message of the type READY to the game server, with playerID and data being -1 and null.
		sendMessage(new CardGameMessage(4, -1, null));
		table.repaint();
	}

	@Override
	/**
	 * A method for parsing the messages received from the game server.
	 * This method should be called from the thread responsible for receiving messages from the game server. Based on the message type, different actions will be carried out.
	 */
	public void parseMessage(GameMessage message) {
		// parses the message based on it type
		switch (message.getType()) {
		case CardGameMessage.PLAYER_LIST:
			this.playerID = message.getPlayerID();
			table.setActivePlayer(playerID);
			for (int i = 0; i < 4; i++) {
				if (((String[])message.getData())[i] != null) {
					this.playerList.get(i).setName(((String[])message.getData())[i]);
					table.setExistence(i);
				}
			}
			this.table.repaint();
			break;
		case CardGameMessage.JOIN:
			// adds a player to the game by updating his name
			this.playerList.get(message.getPlayerID()).setName((String)message.getData());
			table.setExistence(message.getPlayerID());
			this.table.repaint();
			table.printMsg("Player " + playerList.get(message.getPlayerID()).getName() + " joined the game!\n");
			break;
		case CardGameMessage.FULL:
			// displays a message to the player that cannot join in
			playerID = -1;
			table.printMsg("The game is now full, cannot join in. Please try next time!\n");
			table.repaint();
			break;
		case CardGameMessage.QUIT:
			// removes a player by setting his name to empty string
			table.printMsg("Player " + message.getPlayerID() + " " + playerList.get(message.getPlayerID()).getName() + " left the game.\n");
			this.playerList.get(message.getPlayerID()).setName("");
			table.setNotExistence(message.getPlayerID());
			if (!this.endOfGame()) {
				table.disable(); // stop the game
				this.sendMessage(new CardGameMessage(4, -1, null));
				for (int i = 0; i < 4; i++)
					playerList.get(i).removeAllCards();
				table.repaint();
			}
			table.repaint();
			break;
		case CardGameMessage.READY:
			// displays a message to the player that the game is ready
			table.printMsg("Player " + message.getPlayerID() + " is ready now!\n");
			handsOnTable = new ArrayList<Hand>();
			table.repaint();
			break;
		case CardGameMessage.START:
			// starts a new game with the shuffled deck
			start((BigTwoDeck)message.getData());
			table.printMsg("Game is started now!\n\n");
			table.enable();
			table.repaint();
			break;
		case CardGameMessage.MOVE:
			// checks the move of a particular player
			checkMove(message.getPlayerID(), (int[])message.getData());
			table.repaint();
			break;
		case CardGameMessage.MSG:
			// broadcast the user message to a particular client
			table.printChatMsg((String)message.getData());
			break;
		default:
			table.printMsg("Wrong message type: " + message.getType());
			table.repaint();
			// invalid message
			break;
		}
		
	}

	@Override
	/**
	 * A method for sending the specified message to the game server.
	 * This method should be called whenever the client want to communicate with the game server or other clients.
	 */
	public void sendMessage(GameMessage message) {
		try {
			oos.writeObject(message);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	/**
	 * A method for getting the number of players of the game.
	 */
	public int getNumOfPlayers() {
		return numOfPlayers;
	}

	@Override
	/**
	 * A method for getting the deck of cards being used.
	 */
	public Deck getDeck() {
		return this.deck;
	}

	@Override
	/**
	 * A method for getting the list of players.
	 */
	public ArrayList<CardGamePlayer> getPlayerList() {
		return playerList;
	}

	@Override
	/**
	 * A method for getting the list of hands played on the table.
	 */
	public ArrayList<Hand> getHandsOnTable() {
		return handsOnTable;
	}

	@Override
	/**
	 * A method for getting the index of the player for the current turn.
	 */
	public int getCurrentIdx() {
		return currentIdx;
	}

	@Override
	/**
	 * A method for starting/restarting the game with a given shuffled deck of cards.
	 */
	public void start(Deck deck) {
		// remove all cards
		for (int i = 0; i < 4; i++) {
			playerList.get(i).removeAllCards();
		}
		
		this.deck = deck;
		// divide 52 cards to 4 players
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 13; j++) {
				getPlayerList().get(i).addCard(this.deck.getCard(i*13+j));
			}
		}
		
		// sort the cards 
		for (int i = 0; i < 4; i++)
			getPlayerList().get(i).getCardsInHand().sort();
		
		// identify the player who holds the 3 of Diamonds
		BigTwoCard key = new BigTwoCard(0,2);
		for (int i = 0; i < 4; i++) {
			if (playerList.get(i).getCardsInHand().contains(key)) {
				currentIdx = i;
				break;
			}
		}
		
		table.repaint();
		// set the activePlayer of the BigTwoTable instance to the playerID
		table.setActivePlayer(playerID); // only local player's cards are visible and can interact with the user
	}

	@Override
	/**
	 * A method for making a move by a player with the specified playerID using the cards specified by the list of indices.
	 * This method should be called from the BigTwoTable when the local player presses either the "Play" or "Pass" button.
	 */
	public void makeMove(int playerID, int[] cardIdx) {
		// create a CardGameMessage
		CardGameMessage msg = new CardGameMessage(6, playerID, cardIdx);
		// send the message to the server
		sendMessage(msg);
	}

	@Override
	/**
	 * A method for checking a move made by a player.
	 * This method should be called from the parseMessage() method from the NetworkGame interface when a message of the type MOVE is received from the game server.
	 */
	public void checkMove(int playerID, int[] cardIdx) {
		boolean printErrorMessage;
		if (playerID == this.getPlayerID())
			printErrorMessage = true;
		else
			printErrorMessage = false;
		
		if (cardIdx.length == 13) {
			if (printErrorMessage)
				table.printMsg("No card selected!\n\n");
		}
		else if (cardIdx == null) { // I have changed here
			// the player choose pass in this turn
			if (printErrorMessage) {
				table.printMsg("Player" + currentIdx + "'s turn: ");
				table.printMsg("{Pass}");
				if (handsOnTable.size() == 0)
					table.printMsg(" ==> Illegal Move! Since you are the first one to play, you can't choose pass!\n\n");
				else if (handsOnTable.get(handsOnTable.size()-1).getPlayer() == playerList.get(currentIdx)) 
					table.printMsg(" ==> Illegal Move!\n\n");
				else {
					table.printMsg("\n\n");
					currentIdx++;
					currentIdx = currentIdx % 4;
				}
			} else {
				if (handsOnTable.size() == 0) {
					System.out.println("Not for local player: Cannot pass");
				} else if (handsOnTable.get(handsOnTable.size()-1).getPlayer() == playerList.get(currentIdx)) {
					System.out.println("Not for local player: Cannot pass");
				} else {
					table.printMsg("Player" + currentIdx + "'s turn: ");
					table.printMsg("{Pass}");
					table.printMsg("\n\n");
					currentIdx++;
					currentIdx = currentIdx % 4;
				}
			}
		} else if (cardIdx.length > 0) {
			CardList temp;
			if (handsOnTable.size() == 0) {
				// this is the first guy who must choose a hand that contains 3 diamond
				BigTwoCard key = new BigTwoCard(0,2);
				temp = new CardList();
				for (int i = 0; i < cardIdx.length; i++) {
				    temp.addCard(getPlayerList().get(currentIdx).getCardsInHand().getCard(cardIdx[i]));
				}
				if (temp.contains(key) == false) {
					if (printErrorMessage)
						table.printMsg("Illegal Move! Since you are the first one to play, you must choose 3 Diamond!\n\n");
				}
				else if (temp.contains(key)) {
					handsOnTable.add(composeHand(getPlayerList().get(currentIdx), temp));
					if (handsOnTable.get(handsOnTable.size()-1) == null) {
						// Illegal move
						handsOnTable.remove(handsOnTable.size()-1);
						if (printErrorMessage)
							table.printMsg("Illegal Move!\n\n");
					} else {
						// Legal move
						table.printMsg("Player" + currentIdx + "'s turn: ");
						table.printMsg("{" + handsOnTable.get(handsOnTable.size()-1).getType() + "} ");
						for (int i = 0; i < handsOnTable.get(handsOnTable.size()-1).size(); i++)
							table.printMsg("[" + handsOnTable.get(handsOnTable.size()-1).getCard(i).toString() + "] ");
						table.printMsg("\n\n");
						getPlayerList().get(currentIdx).removeCards(handsOnTable.get(handsOnTable.size()-1));
						currentIdx++;
						currentIdx = currentIdx % 4;
					}
				}
			} else {
				// not the first one to play
				temp = new CardList();
				for (int i = 0; i < cardIdx.length; i++) {
				    temp.addCard(getPlayerList().get(currentIdx).getCardsInHand().getCard(cardIdx[i]));
				}
				handsOnTable.add(composeHand(getPlayerList().get(currentIdx), temp));
				if (handsOnTable.get(handsOnTable.size()-1) == null) {
					// Illegal move
					handsOnTable.remove(handsOnTable.size()-1);
					if (printErrorMessage)
						table.printMsg("Illegal Move!\n\n");
				} else {
					// Legal hand, still need to check whether it is able to beat the last hand
					boolean noNeedToCompare = false;
					if (handsOnTable.get(handsOnTable.size()-2).getPlayer() == getPlayerList().get(currentIdx))
						noNeedToCompare = true;
					if (printErrorMessage) {
						table.printMsg("Player" + currentIdx + "'s turn: ");
						table.printMsg("{" + handsOnTable.get(handsOnTable.size()-1).getType() + "} ");
						for (int i = 0; i < handsOnTable.get(handsOnTable.size()-1).size(); i++)
							table.printMsg("[" + handsOnTable.get(handsOnTable.size()-1).getCard(i).toString() + "] ");
					}
						if (!noNeedToCompare && handsOnTable.get(handsOnTable.size()-1).beats(handsOnTable.get(handsOnTable.size()-2))) {
							getPlayerList().get(currentIdx).removeCards(handsOnTable.get(handsOnTable.size()-1));
							if (!printErrorMessage) {
								table.printMsg("Player" + currentIdx + "'s turn: ");
								table.printMsg("{" + handsOnTable.get(handsOnTable.size()-1).getType() + "} ");
								for (int i = 0; i < handsOnTable.get(handsOnTable.size()-1).size(); i++)
									table.printMsg("[" + handsOnTable.get(handsOnTable.size()-1).getCard(i).toString() + "] ");
							}
							table.printMsg("\n\n");
							if (endOfGame() == true) {
								table.printMsg("Game Ended!!\n");
								table.printMsg("The Winner is: " + playerList.get(getCurrentIdx()).getName());
								table.disable();
							}
							currentIdx++;
							currentIdx = currentIdx % 4;
						} else if (noNeedToCompare) {
							if (!printErrorMessage) {
								table.printMsg("Player" + currentIdx + "'s turn: ");
								table.printMsg("{" + handsOnTable.get(handsOnTable.size()-1).getType() + "} ");
								for (int i = 0; i < handsOnTable.get(handsOnTable.size()-1).size(); i++)
									table.printMsg("[" + handsOnTable.get(handsOnTable.size()-1).getCard(i).toString() + "] ");
							}
							getPlayerList().get(currentIdx).removeCards(handsOnTable.get(handsOnTable.size()-1));
							table.printMsg("\n\n");
							if (endOfGame() == true) {
								table.printMsg("Game Ended!!\n");
								table.printMsg("The Winner is: " + playerList.get(getCurrentIdx()).getName());
								table.disable();
							}
							currentIdx++;
							currentIdx = currentIdx % 4;
						} else {
							if (printErrorMessage)
								table.printMsg("Illegal Move!\n\n");
							handsOnTable.remove(handsOnTable.size()-1);
						}
				}
			}
		}
		table.repaint();
		if (endOfGame()) {
			// print the game result
			// if click OK then get ready for the next game
			String msg = "";
			for (int i = 0; i < 4; i++) {
				if (playerList.get(i).getNumOfCards() == 0)
					msg += "Winner: Player " + i + " " + playerList.get(i).getName() + "\n";
				else
					msg += "Player " + i + " " + playerList.get(i).getName() + "have " + playerList.get(i).getNumOfCards() + " cards in hand.\n";
			}
			msg += "Do you want to start a new game?\n";
			for (int i = 0; i < 4; i++)
				playerList.get(i).removeAllCards();
			int feedback = JOptionPane.showConfirmDialog(null, msg, "Game Result", JOptionPane.YES_NO_OPTION);
			if (feedback == 0) {
				// yes
				// send a message of the type JOIN to the game server, with playerID -1 and data being a reference to a string representing the name of the local player
				sendMessage(new CardGameMessage(1, -1, this.getPlayerName()));
				// send a message of the type READY to the game server, with playerID and data being -1 and null.
				sendMessage(new CardGameMessage(4, -1, null));
				playerList.get(this.getPlayerID()).removeAllCards();
				table.repaint();
			} else {
				// no
				table.quit();
			}
		}
	}

	@Override
	/**
	 * A method for checking if the game ends.
	 */
	public boolean endOfGame() {
		for (int i = 0; i < 4; i++)
			if (this.getPlayerList().get(i).getNumOfCards() == 0)
				return true;
		return false;
	}
	
	/**
	 * A method for creating an instance of BigTwoClient
	 * @param args
	 */
	public static void main(String[] args) {
		BigTwoClient client = new BigTwoClient();
	}
	
	/**
	 * A method for returning a valid hand from the specified list of cards of the player.
	 * Return null if no valid hand can be composed from the specified list of cards
	 * @param player
	 * @param cards
	 * @return the valid hand or null if no hand is valid
	 */
	public static Hand composeHand(CardGamePlayer player, CardList cards) {
		Hand test;
		test = new Single(player, cards);
		if (test.isValid())
			return test;
		test = new Pair(player, cards);
		if (test.isValid())
			return test;
		test = new Triple(player, cards);
		if (test.isValid())
			return test;
		test = new StraightFlush(player, cards);
		if (test.isValid())
			return test;
		test = new Straight(player, cards);
		if (test.isValid())
			return test;
		test = new Flush(player, cards);
		if (test.isValid())
			return test;
		test = new FullHouse(player, cards);
		if (test.isValid())
			return test;
		test = new Quad(player, cards);
		if (test.isValid())
			return test;
		
		// none of the hands is valid
		return null;
	}

}
