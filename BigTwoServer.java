/**
 * This class is used to model a Big Two card game server.
 * @author Kenneth Wong
 *
 */
public class BigTwoServer extends CardGameServer {
	/**
	 * Creates and returns an instance of the BigTwoServer class.
	 */
	public BigTwoServer() {
		super("Big Two Server", 4);
	}
	
	/**
	 * Creates and returns an instance of the BigTwoDeck class.
	 * @return an instance of the BigTwoDeck class
	 */
	public Deck createDeck() {
		return new BigTwoDeck(); 
	}
	
	/**
	 * main() method for starting the server.
	 * 
	 * @param args
	 *            the port to be used by the server. The default port 5000 will
	 *            be used if no arguments has been supplied
	 */
	public static void main(String[] args) {
		BigTwoServer server = new BigTwoServer();
		if (args.length > 0) {
			server.start(Integer.parseInt(args[0]));
		} else {
			server.start(2396);
		}
	} // main
}
