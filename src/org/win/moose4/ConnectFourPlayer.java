package org.win.moose4;

import java.net.Socket;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.util.Vector;

/**
 * An abstraction of a player in the connect four game
 * Can play a server or client game
 */
public class ConnectFourPlayer 
{
	/**
	 * The brain behind the players moves
	 */
	private ConnectFourAI _ai;

	/**
	 * The communication interface
	 */
	private ConnectFourCommunicator _comm;

	/**
	 * Construct a new ConnectFourPlayer with the given parameters
	 * @param s The socket to communicate on
	 * @param colour The colour to play for
	 */
	public ConnectFourPlayer(Socket s, byte colour)
	{
		try {
			_ai = new ConnectFourAI(new ConnectFourBoard(), colour);
			_comm = new ConnectFourCommunicator(s);

						
		} catch( InvalidInputException iie ) {
			System.err.println(iie.getMessage());
			System.err.println("ERROR: An exception has been " +
				"thrown when it shouldn't have been."
			);
			System.exit(1);
		} catch( IOException ioe ) {
			System.err.println(ioe.getMessage());
			System.err.println("ERROR: An I/O issue has occured "+
				"while connecting to the server."
			);
			System.exit(1);
		}
	}	

	/**
	 * Play the server modified version of a connect four game
	 */
	public void playServerGame()
	{
		boolean done = false;
		
		oppMove();
		myFirstMove();

		while( !done ) {
			oppMove();
			myMove();
		}
	}

	/**
	 * Play the client modified version of a connect four game
	 */
	public void playClientGame()
	{
		boolean done = false;
		
		myFirstMove();
		oppMove();

		while( !done ) {
			myMove();
			oppMove();
		}
	}

	/**
	 * Perform a random first move to begin the game
	 */
	private void myFirstMove()
	{
		_comm.writeMove(_ai.randFirstMove());
	}

	/**
	 * Read an opponents move from the wire and process it
	 */
	private void oppMove()
	{
		Message move = _comm.readMove();
		
		if( move.mType == 0 ) {
			if( _ai.validateMove(move) ) {
				_ai.makeOppMove(move);
				
				
			} else {
				claimIllegal("Proposed move is illegal: " +
					move.row+"-"+move.col
				);
				System.exit(0);
			}

			if( _ai.lastMoveWins() != Constants.OPEN ) {
				claimIllegal("Last move submitted ("+move.row+
					"-"+move.col+") wins and was not "+
					"marked as such."
				);
				
				System.exit(0);
			}
		} else if ( move.mType == 1 ) {
			System.out.println("Illegal claim!");

			_ai.printBoard();
			_ai.printMoves();

			System.exit(0);
		} else if ( move.mType == 2 ) {
			_ai.printBoard();

			_ai.printMoves();
			System.out.println("Claimed winning move: " +	move.row + "-" + move.col);
			
			Runtime x = Runtime.getRuntime();
			System.out.println("Memory used: " + (x.totalMemory() - x.freeMemory()));
			
			System.exit(0);
		} else {
			claimIllegal("Proposed type is illegal: "+move.mType);
			System.exit(0);
		}
	}

	/**
	 * Calculate and perform our next move
	 */
	private void myMove()
	{
		Message msg = _ai.getNextMove();
		_comm.writeMove(msg);
		if( msg.mType == 1 ) {
			_ai.printBoard();
			_ai.printMoves();
			System.exit(0);
		} else if( msg.mType == 2 ) {
			_ai.printBoard();
			_ai.printMoves();
			System.exit(0);
		}
	}

	/**
	 * Claim that an illegal move has happened
	 * @param msg The message to display
	 */
	private void claimIllegal(String msg)
	{
		System.out.println(msg);
		_comm.writeMove(new Message(0, 0, 1));
		_ai.printBoard();
		_ai.printMoves();

		Runtime x = Runtime.getRuntime();
		System.out.println("Memory used: " + (x.totalMemory() - x.freeMemory()));
	}
}
