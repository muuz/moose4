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

	public boolean myMove(Message m)
	{
		boolean rtn = true;
		if( m != null ) {
			System.out.println("Check for win @: "+m.row+"-"+m.col);
			_board.makeMove(m, Constants.WHITE);
			if( _board.moveWins(m.row, m.col) != Constants.OPEN ) {
				m.mType = 2;
				rtn = false;
			}

			_comm.writeMove(m);
		} else {
			rtn = false;
		}

		return rtn;
	}

	public Message oppMove()
	{
		Message move = _comm.readMove();
		if( move.mType != 1 ) {
			_board.makeMove(move, Constants.BLACK);
		}

		return move;
	}
}
