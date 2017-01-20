import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;

/**
 * This class is used to model a card game server.
 * 
 * @author Kenneth Wong
 *
 */
public class CardGameServer {
	// The name of this card game server
	private String serverName;
	// The maximum number of players in a card game
	private final int maxNumOfPlayers;
	// Array for holding sockets of the clients
	private Socket[] clientSockets;
	// Array for holding ObjectOutputStreams of the clients
	private ObjectOutputStream[] clientOutputStreams;
	// Array for holding player names of the clients
	private String[] clientNames;
	// Array for holding ready states of the clients
	private boolean[] clientReadyStates;
	// number of current players
	private int numOfPlayers = 0;
	// the main frame of the server
	private JFrame frame = null;
	// text area for displaying server states
	private JTextArea textArea = null;
	// a boolean indicating if the server is up
	private boolean serverUp = false;

	/**
	 * Creates and returns an instance of the BigTwoServer class.
	 * 
	 * @param serverName
	 *            the name of this card game server
	 * @param maxNumOfPlayers
	 *            the maximum number of players in a card game
	 */
	public CardGameServer(String serverName, int maxNumOfPlayers) {
		this.serverName = serverName;
		this.maxNumOfPlayers = maxNumOfPlayers;

		// creates arrays for holding client sockets, output streams, player
		// names, and ready states
		clientSockets = new Socket[maxNumOfPlayers];
		clientOutputStreams = new ObjectOutputStream[maxNumOfPlayers];
		clientNames = new String[maxNumOfPlayers];
		clientReadyStates = new boolean[maxNumOfPlayers];

		buildGUI();
	}

	/**
	 * Builds the GUI for the server
	 */
	private void buildGUI() {
		// build a GUI
		frame = new JFrame(serverName);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		textArea = new JTextArea(20, 40);
		textArea.setEditable(false);
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		JScrollPane scroller = new JScrollPane(textArea);
		scroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		scroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		frame.add(scroller);

		// build the menu bar
		JMenuItem clearMenuItem = new JMenuItem("Clear console");
		clearMenuItem.addActionListener(new ClearMenuItemListener());
		JMenuItem quitMenuItem = new JMenuItem("Quit");
		quitMenuItem.addActionListener(new QuitMenuItemListener());
		JMenu menu = new JMenu("Option");
		menu.add(clearMenuItem);
		menu.add(quitMenuItem);
		JMenuBar menuBar = new JMenuBar();
		menuBar.add(menu);
		frame.setJMenuBar(menuBar);

		frame.pack();
		frame.setVisible(true);
	} // buildGUI

	/**
	 * Starts the server at the specified port
	 * 
	 * @param port
	 *            the specified port at which the server starts
	 */
	public void start(int port) {
		// start the server
		try {
			// creates a ServerScoket
			ServerSocket serverSocket = new ServerSocket(port);
			println("Starts up the server at localhost:"
					+ serverSocket.getLocalPort());
			serverUp = true;
			while (serverUp) {
				// waits for clients to connect
				Socket clientSocket = serverSocket.accept();
				addConnection(clientSocket);
			} // while
			serverSocket.close();
		} catch (Exception ex) {
			println("Error in starting up the server at localhost:" + port);
			ex.printStackTrace();
		}
	} // start

	/**
	 * Parses the incoming message from the specified client.
	 * 
	 * @param clientSocket
	 *            the socket connection to the specified client
	 * @param message
	 *            the message received from the specified client
	 */
	private synchronized void parseMessage(Socket clientSocket,
			CardGameMessage message) {
		// updates the playerID
		for (int i = 0; i < maxNumOfPlayers; i++) {
			if (clientSockets[i] == clientSocket) {
				message.setPlayerID(i);
				break;
			}
		}

		// parses the message based on it type
		switch (message.getType()) {
		case CardGameMessage.JOIN:
			// adds a player to the game
			addPlayer(clientSocket, (String) message.getData());
			break;
		case CardGameMessage.READY:
			// marks the specified player as ready for a new game
			setReadyState(clientSocket);
			break;
		case CardGameMessage.MOVE:
			println("Broadcasts a \"MOVE\" message from "
					+ clientSocket.getRemoteSocketAddress());
			// broadcast the MOVE message to all clients
			broadcastMessage(message);
			break;
		case CardGameMessage.MSG:
			println("Broadcasts a user message from "
					+ clientSocket.getRemoteSocketAddress());
			// broadcast the user message to all clients
			broadcastUserMessage(clientSocket, (String) message.getData());
			break;
		default:
			println("Wrong message type: " + message.getType());
			// invalid message
			break;
		}
	} // parseMessage

	/**
	 * Broadcasts the specified message to all clients.
	 * 
	 * @param message
	 *            the specified message to be broadcast to all clients
	 */
	private synchronized void broadcastMessage(CardGameMessage message) {
		if (numOfPlayers > 0) {
			for (int i = 0; i < maxNumOfPlayers; i++) {
				if (clientSockets[i] != null && clientOutputStreams[i] != null) {
					try {
						clientOutputStreams[i].writeObject(message);
					} catch (Exception ex) {
						println("Error in broadcasting a message to the client at "
								+ clientSockets[i].getRemoteSocketAddress());
						ex.printStackTrace();
					}
				}
			}
		}
	} // broadcastMessage

	/**
	 * Adds a new socket connection to the server
	 * 
	 * @param clientSocket
	 *            the socket connection to be added to the server
	 */
	private synchronized void addConnection(Socket clientSocket) {
		// adds this connection to the server if the server is not full
		if (numOfPlayers < maxNumOfPlayers) {
			// locates the first empty slot for the new connection
			for (int i = 0; i < maxNumOfPlayers; i++) {
				if (clientSockets[i] == null) {
					try {
						// creates an ObjectOutputStream for this client socket
						ObjectOutputStream oostream = new ObjectOutputStream(
								clientSocket.getOutputStream());

						clientSockets[i] = clientSocket;
						clientOutputStreams[i] = oostream;
						clientNames[i] = null;
						clientReadyStates[i] = false;
						numOfPlayers++;
						println("Establishes a connection with a client at "
								+ clientSocket.getRemoteSocketAddress());

						// creates a thread for receiving messages from this
						// client
						Thread t = new Thread(new ClientHandler(clientSocket));
						t.start();

						// sends the player list to the new player
						oostream.writeObject(new CardGameMessage(
								CardGameMessage.PLAYER_LIST, i, clientNames));
					} catch (Exception ex) {
						println("Error in establishing a connection with a client at "
								+ clientSocket.getRemoteSocketAddress());
						ex.printStackTrace();
					}
					break;
				}
			} // for
		} else {
			// Max. no. of players reached
			println("Server is full: cannot establish a connection with a client at "
					+ clientSocket.getRemoteSocketAddress());

			// creates a thread for sending a FULL message to this client, waits
			// for 1000 milliseconds and closes the socket
			Thread t = new Thread(new ClientHandler2(clientSocket));
			t.start();
		}

	} // addConnection

	/**
	 * Removes a connection from the server (possibly due to connection loss).
	 * 
	 * @param clientSocket
	 *            the socket connection being removed from the server
	 */
	private synchronized void removeConnection(Socket clientSocket) {
		if (numOfPlayers > 0) {
			// locates the client socket in the array
			for (int i = 0; i < maxNumOfPlayers; i++) {
				if (clientSockets[i] == clientSocket) {
					String name = clientNames[i];

					clientSockets[i] = null;
					clientOutputStreams[i] = null;
					clientNames[i] = null;
					clientReadyStates[i] = false;
					numOfPlayers--;

					println(name + " (" + clientSocket.getRemoteSocketAddress()
							+ ") leaves the game.");

					String remoteAddress = clientSocket
							.getRemoteSocketAddress().toString();

					// broadcasts a message about the leaving of this player
					broadcastMessage(new CardGameMessage(CardGameMessage.QUIT,
							i, remoteAddress));
					break;
				}
			}
		}
	} // removeConnection

	/**
	 * adds a player to the game.
	 * 
	 * @param clientSocket
	 *            the socket connection to the player who is joining the game
	 * @param name
	 *            the name of the player who is joining the game
	 */
	private synchronized void addPlayer(Socket clientSocket, String name) {
		if (numOfPlayers > 0) {
			// locates the client socket in the array
			for (int i = 0; i < maxNumOfPlayers; i++) {
				if (clientSockets[i] == clientSocket) {
					// updates the name of the new player
					clientNames[i] = name;

					println(name + " (" + clientSocket.getRemoteSocketAddress()
							+ ") joins the game.");

					// broadcasts a message about this player joining the game
					broadcastMessage(new CardGameMessage(CardGameMessage.JOIN,
							i, name));
					break;
				}
			}
		}
	} // addPlayer

	/**
	 * Marks the specified player as ready for a new game.
	 * 
	 * @param clientSocket
	 *            the socket connection to the player who becomes ready for a
	 *            new game
	 */
	private synchronized void setReadyState(Socket clientSocket) {
		if (numOfPlayers > 0) {
			// locates the client socket in the array
			for (int i = 0; i < maxNumOfPlayers; i++) {
				if (clientSockets[i] == clientSocket) {
					clientReadyStates[i] = true;
					println(clientNames[i] + " ("
							+ clientSocket.getRemoteSocketAddress()
							+ " ) is ready for the next game.");
					broadcastMessage(new CardGameMessage(CardGameMessage.READY,
							i, null));
					break;
				}
			}
		}

		// checks if all players are ready
		if (numOfPlayers == maxNumOfPlayers) {
			for (int i = 0; i < maxNumOfPlayers; i++) {
				if (clientReadyStates[i] == false) {
					// returns if any of the players is not ready
					return;
				}
			}

			// resets the ready states of all the players for the next game
			for (int i = 0; i < maxNumOfPlayers; i++) {
				clientReadyStates[i] = false;
			}

			// creates a new deck, shuffles the deck, and starts a new game
			Deck deck = createDeck();
			deck.shuffle();
			println("All players are ready. Game starts.");
			broadcastMessage(new CardGameMessage(CardGameMessage.START, -1,
					deck));
		}
	}

	/**
	 * Creates and returns an instance of the Deck class. Overrides this method
	 * if a different type of deck is needed.
	 * 
	 * @return an instance of the Deck class
	 */
	public Deck createDeck() {
		return new Deck();
	}

	private synchronized void broadcastUserMessage(Socket clientSocket,
			String msg) {
		if (numOfPlayers > 0) {
			// locates the client in the array
			for (int i = 0; i < maxNumOfPlayers; i++) {
				if (clientSockets[i] == clientSocket) {
					String longMsg = clientNames[i] + " ("
							+ clientSocket.getRemoteSocketAddress() + "): "
							+ msg;
					broadcastMessage(new CardGameMessage(CardGameMessage.MSG,
							i, longMsg));
					break;
				}
			}
		}
	}

	/**
	 * Prints the specified message to the text area.
	 * 
	 * @param msg
	 */
	private void println(String msg) {
		textArea.append(msg + "\n");
		textArea.setCaretPosition(textArea.getDocument().getLength());
	}

	/**
	 * This inner class is used for receiving incoming messages from a client
	 * 
	 * @author Kenneth Wong
	 *
	 */
	private class ClientHandler implements Runnable {
		private Socket clientSocket; // socket connection to the client
		private ObjectInputStream oistream; // ObjectInputStream of the client

		/**
		 * Creates and returns an instance of the ClientHandler class.
		 * 
		 * @param clientSocket
		 *            the socket connection to the client
		 */
		public ClientHandler(Socket clientSocket) {
			this.clientSocket = clientSocket;
			try {
				// creates an ObjectInputStream and chains it to the InputStream
				// of the client socket
				oistream = new ObjectInputStream(clientSocket.getInputStream());
			} catch (Exception ex) {
				println("Error in creating an ObjectInputStream for the client at "
						+ clientSocket.getRemoteSocketAddress());
				ex.printStackTrace();
			}
		} // constructor

		// implementation of method from the Runnable interface
		public void run() {
			CardGameMessage message;
			try {
				// waits for messages from the client
				while ((message = (CardGameMessage) oistream.readObject()) != null) {
					println("Message received from "
							+ clientSocket.getRemoteSocketAddress());
					parseMessage(clientSocket, message);
				} // close while
			} catch (Exception ex) {
				println("Error in receiving messages from the client at "
						+ clientSocket.getRemoteSocketAddress());
				ex.printStackTrace();
				// possible connection loss, removes the connection
				removeConnection(clientSocket);
			}
		} // run
	} // ClientHandler

	private class ClientHandler2 implements Runnable {
		private Socket clientSocket; // socket connection to the client

		/**
		 * Creates and returns an instance of the ClientHandler2 class.
		 * 
		 * @param clientSocket
		 *            the socket connection to the client
		 */
		public ClientHandler2(Socket clientSocket) {
			this.clientSocket = clientSocket;
		} // constructor

		// implementation of method from the Runnable interface
		public void run() {
			try {
				// creates an ObjectOutputStream and chains it to the
				// OutputStream
				// of the client socket
				ObjectOutputStream oostream = new ObjectOutputStream(
						clientSocket.getOutputStream());
				// sends a FULL message to the client
				oostream.writeObject(new CardGameMessage(CardGameMessage.FULL,
						-1, null));
				oostream.flush();
			} catch (Exception ex) {
				println("Error in sending a FULL message to the client at "
						+ clientSocket.getRemoteSocketAddress());
				ex.printStackTrace();
			}

			// sleeps for 1000 milliseconds before closing the socket
			try {
				Thread.sleep(1000);
			} catch (Exception ex) {
				println("Error in sleeping before closing the client socket at "
						+ clientSocket.getRemoteSocketAddress());
				ex.printStackTrace();
			}

			// closes the socket
			try {
				clientSocket.close();
			} catch (Exception ex) {
				println("Error in closing the client socket at "
						+ clientSocket.getRemoteSocketAddress());
				ex.printStackTrace();
			}
		} // run
	} // ClientHandler2

	/**
	 * This inner class is used for handling the Clear menu
	 * 
	 * @author Kenneth Wong
	 *
	 */
	private class ClearMenuItemListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			textArea.setText("");
		}
	} // ClearMenuItemListener

	/**
	 * This inner class is used for handling the Quit menu
	 * 
	 * @author Kenneth Wong
	 *
	 */
	private class QuitMenuItemListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			frame.dispose();
			System.exit(0);
		}
	} // QuitMenuItemListener
}
