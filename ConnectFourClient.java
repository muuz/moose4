import java.io.IOException;
import java.net.Socket;

/**
 * A connect-4 client representation
 */
public class ConnectFourClient
{
	/**
	 * The socket to connect with
	 */
	private Socket _s;

	/**
	 * Construct a new connect-4 client with the given IP address of
	 * the server and the port on which the application is running
	 * @param ip The server ip
	 * @param port The server port
	 * @throws IOException If an I/O issue has occured while connnecting
	 * to the server 
	 */
	public ConnectFourClient(String ip, int port)
	throws IOException
	{
		System.out.print("Connecting...");
		_s = new Socket(ip, port);
		System.out.println("DONE");
	}

	/**
	 * Start the client game playing process
	 * @throws IOException If an I/O issue occurs while playing the game
	 */
	public void runClient()
	throws IOException
	{
		new ConnectFourPlayer(_s, Constants.WHITE).playClientGame();
		_s.close();
	}
}
