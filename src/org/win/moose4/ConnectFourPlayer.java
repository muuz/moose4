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
		oppMove();
		myMove();

		// Play the game
		while( oppMove() && myMove() ); 
	}

	/**
	 * Play the client modified version of a connect four game
	 */
	public void playClientGame()
	{
		myMove();
		oppMove();

		// Play the game
		while( myMove() && oppMove() ); 
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
	 * @return true if the game continues, false otherwise
	 */
	private boolean oppMove()
	{
		Message move = _comm.readMove();
		
		// Regular move message
		if( move.mType == 0 ) {
			if( _ai.validateMove(move) ) {
				_ai.makeOppMove(move);
				
				
			} else {
				claimIllegal("Proposed move is illegal: " +
					move.row+"-"+move.col
				);
				return false;
			}

			if( _ai.lastMoveWins() != Constants.OPEN ) {
				claimIllegal("Last move submitted ("+move.row+
					"-"+move.col+") wins and was not "+
					"marked as such."
				);
				return false;
			}
		//Opponent claims an illegal move was made
		} else if ( move.mType == 1 ) {
			System.out.println("Illegal claim!");

			_ai.printBoard();
			_ai.printMoves();

			return false;
		//Opponent claims to be making a winning move
		} else if ( move.mType == 2 ) {
			_ai.printBoard();

			_ai.printMoves();
			System.out.println("Claimed winning move: " +
				move.row + "-" + move.col
			);
			
			return false;
		//Opponent has sent an unrecognized message type
		} else {
			claimIllegal("Proposed message type '" + move.mType +
				"' is not recognized"
			);
			return false;
		}

		return true;
	}

	/**
	 * Calculate and perform our next move
	 * @return true if the game should continue, false otherwise
	 */
	private boolean myMove()
	{
		boolean rtn = true;
		Message msg = _ai.getNextMove();
		_comm.writeMove(msg);
		if( msg.mType == 1 ) {
			System.out.println("I call shenanagans!");
			_ai.printBoard();
			_ai.printMoves();
			rtn = false;
		} else if( msg.mType == 2 ) {
			System.out.println("I claim victory!");
			_ai.printBoard();
			_ai.printMoves();
			rtn = false;
		}

		return rtn;
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
	}
}
