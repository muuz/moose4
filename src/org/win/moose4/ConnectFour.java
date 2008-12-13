package org.win.moose4;

import java.io.IOException;

/**
 * The main class which provides a means for starting the connect four
 * application in server or client mode
 */
public class ConnectFour
{
	/**
	 * Print the usage message on STDERR
	 */
	private static void printUsage()
	{
		System.err.println("USAGE: java ConnectFour " +
			"<server|client|human> " +
			"<ip_of_server> <port_on_server>"
		);
	}

	/**
	 * Begin the connect four application
	 */
	public static void main(String argv[])
	{
		/* Display usage and quit */
		if( argv.length == 0 ||
			argv[0].equalsIgnoreCase("-h") ||
			argv[0].equals("--help")
		) {
			printUsage();
		} else if( argv[0].equalsIgnoreCase("Server")) {
			try {
				new ConnectFourServer(
					Constants.SVR_PORT
				).runServer();
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
						"ERROR: Port must be a number"
					);
					printUsage();
				} catch( IOException ioe ) {
					System.err.println("ERROR: Server " +
						"'"+argv[1]+":"+argv[2]+"'" +
						" couldn't be contacted"
					);
					printUsage();
				}
			}
		} else if( argv[0].equalsIgnoreCase("Human")) {
			if( argv.length != 3 ) {
				System.err.println("ERROR: Please provide " +
					"an HOST and a PORT"
				);
				printUsage();
			} else {
				try {
					new ConnectFourGUI(
						Constants.WHITE,
						argv[1],
						Integer.parseInt(argv[2])
					);
				} catch( NumberFormatException nfe ) {
					System.err.println(
						"ERROR: Port must be a number"
					);
					printUsage();
				} catch( IOException ioe ) {
					System.err.println("ERROR: Server " +
						"'"+argv[1]+":"+argv[2]+"'" +
						" couldn't be contacted"
					);
				}
			}
		} else {
			System.err.println("Unrecognized mode: " + argv[0]);
			printUsage();
		}
	}
}
