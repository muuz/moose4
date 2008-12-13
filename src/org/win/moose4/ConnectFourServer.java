package org.win.moose4;

import java.net.Socket;
import java.net.ServerSocket;
import java.io.IOException;

/**
 * The server for connect four game
 */
public class ConnectFourServer
{
	/**
	 * The server socket for the application
	 */
	private ServerSocket _ss;

	/**
	 * Construct a new ConnectFourServer on the given port
	 * @param port The port to listen on
	 * @throws IOException If an I/O issue occurs while binding the port 
	 */
	public ConnectFourServer(int port)
	throws IOException
	{
		_ss = new ServerSocket(port);
	}

	/**
	 * Begin listening on the prescribed port
	 * @throws IOException If an I/O issue occurs while listening on the
	 * server socket
	 */ 
	public void runServer()
	throws IOException
	{
		Socket s;
		while(true) {
			System.out.print("Waiting for connection...");
			s = _ss.accept();
			System.out.println("DONE");
			new ConnectFourPlayer(
				s, Constants.BLACK
			).playServerGame();

			s.close();
		}
	}
}
