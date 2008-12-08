import java.io.IOException;

/**
 * The main class which provides a means for starting the connect four
 * application in server or client mode
 */
public class ConnectFour
{
	/**
	 * The port that the server runs on
	 */
	private final static int SRV_PORT = 44521;

	/**
	 * Print the usage message on STDERR
	 */
	private static void printUsage()
	{
		System.err.println("USAGE: java ConnectFour <server|client> " +
			"<ip_of_server> <port_on_server>"
		);
	}

	/**
	 * Begin the connect four application
	 */
	public static void main(String argv[])
	{
		if( argv.length == 0 ) {
			printUsage();
		} else if( argv[0].equalsIgnoreCase("Server")) {
			try {
				new ConnectFourServer(SRV_PORT).runServer();
			} catch ( IOException ioe ) {
				System.err.println(ioe.getMessage());
				System.err.println("ERROR: I/O issue has "+
					"occured on the server-side."
				);
				System.exit(1);
			}
		} else if( argv[0].equalsIgnoreCase("Client")) {
			if( argv.length != 3 ) {
				printUsage();
			} else {
				try {
					new ConnectFourClient(argv[1], 
						Integer.parseInt(argv[2])
					).runClient();
				} catch( NumberFormatException nfe ) {
					System.err.println(
						"Port must be a number");
					printUsage();
				} catch( IOException ioe ) {
					ioe.printStackTrace();
				}
			}
		} else if( argv[0].equalsIgnoreCase("Human")) {
			if( argv.length != 3 ) {
				printUsage();
			} else {
				try {
					new ConnectFourHumanClient(argv[1],
						Integer.parseInt(argv[2])
					).runClient();
				} catch( NumberFormatException nfe ) {
					System.err.println(
						"Port must be a number"
					);
					printUsage();
				} catch( IOException ioe ) {
					ioe.printStackTrace();
				}
			}
		}
	}
}
