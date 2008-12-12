package org.win.moose4;

import java.io.IOException;
import java.net.Socket;

/**
 * A connect-4 human client interface
 */
public class ConnectFourHumanClient
extends ConnectFourClient
{
	private ConnectFourBoard _board;
	private ConnectFourCommunicator _comm;
	/**
	 * Construct a new connect-4 client with the given IP and port of the
	 * server
	 * @param ip The IP Address
	 * @param port The port
	 * @throws IOException if an I/O issue occurs
	 */
	public ConnectFourHumanClient(String ip, int port)
	throws IOException
	{
		super(ip, port);
		try {
			_board = new ConnectFourBoard();
			_comm = new ConnectFourCommunicator(_s);
		} catch( InvalidInputException iie ) {
			System.err.println(iie.getMessage());
			System.err.println("ERROR: An exception has been " +
				"thrown when it shouldn't have been."
			);
			System.exit(1);
		} catch( IOException ioe ) {
			System.err.println(ioe.getMessage());
			System.err.println("Error: An I/O issue has occured "+
				"while connecting to the server."
			);
			System.exit(1);
		}
	}

	/**
	 * Start the client process
	 * @throws IOException if things go wrong
	 */
	public void runClient()
	throws IOException
	{
		boolean done = false;
		while( !done ) {
			myMove();
			if( !oppMove() ) {
				break;
			}
		}
	}

	public void myMove()
	{
		//GET MOVE FROM GUI
		//_board.makeMove(move, Constants.WHITE);
		//_comm.writeMove(someMove);
		//UPDATE GUI
	}

	public boolean oppMove()
	{
		boolean rtn = true;
		Message move = _comm.readMove();
		_board.makeMove(move, Constants.BLACK);
		//UPDATE GUI
		if( move.mType == 1 ) {
			//Server claims illegal
			//Probably means you win
			rtn = false;
		} else if ( move.mType == 2 ) {
			//Server wins
			rtn = false;
		}

		return rtn;
	}
}
