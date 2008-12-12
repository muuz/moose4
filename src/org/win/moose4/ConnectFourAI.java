package org.win.moose4;

import java.util.Vector;
import java.util.Random;

/**
 * Originally, the brain behind an AI player. Its function is blurred
 * with ConnectFourBoard.
 */
public class ConnectFourAI
{
	/**
	 * The board view that is maintained
	 */
	private ConnectFourBoard _board;

	/**
	 * The colour which this AI is playing for 
	 */
	private byte _colour;

	/**
	 * The current list of all moves made
	 */
	private Vector<Message> _allMoves;

	/**
	 * Construct a new ConnectFourAI with the given parameters
	 * @param board The starting board
	 * @param colour The colour this AI should play for
	 * @throws InvalidInputException If the colour specified is Constants.OPEN 
	 */
	public ConnectFourAI(ConnectFourBoard board, byte colour)
	throws InvalidInputException
	{
		if( colour != Constants.BLACK && colour != Constants.WHITE ) {
			throw new InvalidInputException("Colour OPEN should "+
				"not be selected"
			);
		}
		_board = board;
		_colour = colour;
		_allMoves = new Vector<Message>(49);
	}

	/**
	 * Generate a random move and place it on the local board
	 * @return The Message object containing the move
	 */
	public Message randFirstMove()
	{
		Vector<Message> legalMoves = _board.getLegalMoves();
		Message move = legalMoves.get(new Random().nextInt(
			legalMoves.size())
		);
		_allMoves.add(move);
		_board.makeMove(move, _colour);
		return move;
	}

	/**
	 * Do some magic to generate the next move that the AI should make
	 * @return The Message containing the next move
	 */
	public Message getNextMove()
	{
		Message move = _board.bestMove(_colour);
		if( move.mType != 1 ) {
			_board.makeMove(move, _colour);
			_allMoves.add(move);
		}
		return move;
	}

	/**
	 * Place an opponents move on the board
	 * @param move The move read from the opponent
	 */
	public void makeOppMove(Message move)
	{
		_allMoves.add(move);
		_board.makeMove(move, (_colour == Constants.BLACK) ?
			Constants.WHITE : Constants.BLACK
		);
	}

	/**
	 * Print the current board configuration to STDOUT
	 */
	public void printBoard()
	{
		_board.printBoard();
	}

	/**
	 * Print the list of moves made to STDOUT
	 */
	public void printMoves()
	{
		for( int i=0; i<_allMoves.size(); ++i ) {
			Message msg = _allMoves.get(i);
			System.out.println( "Move " + (i+1) + "; " + 
				((i%2 == 1) ? "Black" : "White") + 
				" @ " + msg.row + "-" + msg.col
			);
		}
	}

	/**
	 * Validate a given move for the current board configuration
	 * @param move The move to validate
	 * @return true if move is valid, false otherwise
	 */
	public boolean validateMove(Message move)
	{
		Message cur;
		Vector<Message> legalMoves = _board.getLegalMoves();
		for( int i=0; i<legalMoves.size(); ++i ) {
			cur = legalMoves.get(i);
			if( cur.row == move.row && cur.col == move.col ) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Check if the last move made is a winning move
	 * @return The colour of the winner or 0 
	 */
	public byte lastMoveWins()
	{
		if( _allMoves.size() >= 7 ) {
			Message lastMove = _allMoves.lastElement();
			return _board.moveWins(lastMove.row, lastMove.col);
		} else {
			return Constants.OPEN;
		}
	}

	/**
	 * Getter provided for the GUI
	 * @return The 2-D array of bytes which is the core of the board
	 * representation
	 */
	public byte[][] getBoard()
	{
		return _board.getBoard();
	}
}
