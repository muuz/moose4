package org.win.moose4;

import java.util.Vector;
import java.util.Random;

/**
 * Class which represents a connect-4 board
 */
public class ConnectFourBoard
{
	/**
	 * The number of rows and columns in the board
	 */
	private int _rows, _cols;

	/**
	 * The core board representation
	 */
	private byte _board[][];

	/**
	 * The children of the current board configuration
	 */
	private ConnectFourBoard _children[];

	/**
	 * The open row for each column
	 */
	private int _currentRow[];

	/**
	 * Construct a new default 7x7 Connect-4 board
	 * @throws InvalidInputException Shouldn't happen...
	 */
	public ConnectFourBoard()
	throws InvalidInputException
	{
		this(7, 7);
	}

	/**
	 * Construct a new Connect-4 board with the given parameters
	 * @param rows The number of rows on the board
	 * @param cols The number of columns on the board
	 * @throws InvalidInputException If the number of rows or columns 
	 * specified is invalid
	 */
	public ConnectFourBoard(int rows, int cols)
	throws InvalidInputException
	{
		if( _rows < 0 || _cols < 0 ) {
			throw new InvalidInputException(
				"Row and Column entries must be positive"
			);
		}

		_rows = rows;
		_cols = cols;

		_board = new byte[rows][cols];
		_children = new ConnectFourBoard[cols];
		_currentRow = new int[_cols];

		//initialize the board
		for( int i=0; i<rows; ++i ) {
			for( int j=0; j<cols; ++j ) {
				_board[i][j] = Constants.OPEN;
			}
		}

		// Initialize available moves list
		for( int i=0; i<cols; ++i ) {
			_currentRow[i] = rows-1;
		}
	}

	/**
	 * Get a list of the currently available legal moves
	 * @return The vector list of the legal moves
	 */
	public Vector<Message> getLegalMoves()
	{
		Vector<Message> legalMoves = new Vector<Message>(7);

		for( int c=0; c<_cols; ++c ) {
			if( _currentRow[c] >= 0 ) {
				legalMoves.add(new Message(_currentRow[c],
					c, 0)
				);
			}
		}

		return legalMoves;
	}


	/**
	 * Generate a child of the current board for the given index and colour
	 * @param col The column of the move to make the child board for
	 * @param colour The colour of the move being make
	 * @return The newly generated child board or null
	 */
	private ConnectFourBoard generateChild(int col, byte colour)
	{
		for( int r=_rows-1; r>=0; --r ) {
			if( _board[r][col] == Constants.OPEN ) {
				makeMove(new Message(r, col, 0), colour);
				return this;
			}
		}

		return null;
	}

	/**
	 * Test if the current board has any children
	 * @return true if the board has no children, false otherwise
	 */
	private boolean hasNoChildren()
	{
		for( int i=0; i<_cols; ++i ) {
			if( _currentRow[i] >= 0 ) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Draw a given move for a given colour on this board
	 * @param move The move to make
	 * @param colour The colour of the move
	 */
	public void makeMove(Message move, byte colour)
	{
		_board[move.row][move.col] = colour;
		_currentRow[move.col]--;
	}

	/**
	 * Remove a given move from the board
	 */
	public void unrollChild(int col)
	{
		for( int r=0; r<_rows; ++r ) {
			if( _board[r][col] != Constants.OPEN ) {
				_board[r][col] = Constants.OPEN;
				_currentRow[col]++;
				break;
			}
		}
	}

	/**
	 * Print the current board configuration to STDOUT
	 */
	public void printBoard()
	{
		for( int i=0; i<_board.length; ++i ) {
			for( int j=0; j<_board[i].length; ++j ) {
				System.out.print( ((_board[i][j] == 
					Constants.BLACK)?"B": ((_board[i][j] ==
						Constants.WHITE)?"W":"O"
						) 
					)+ " "
				);
			}
			System.out.println();
		}
	}

	/**
	 * Check to see if a move was a winning move
	 * NOTE: Main idea taken from DwarfConnect4
	 *
	 * @param row The row of the last move
	 * @param col The column of the last move
	 * @return The winning colour if there is a winner, Slot.OPEN otherwise
	 */
	public byte moveWins(int row, int col)
	{
		byte moveColour = _board[row][col];
		int connected=1;

		/* CHECK HORIZONTAL */
		//cells to the left
		for( int i = col+1; i<_cols; ++i ) {
			if( _board[row][i] == moveColour ) {
				connected++;
			} else {
				break;
			}
		}

		//cells to the right
		for( int i = col-1; i >= 0; --i ) {
			if( _board[row][i] == moveColour ) {
				connected++;
			} else {
				break;
			}
		}

		if( connected >= 4 ) {
			return moveColour;
		}

		/* CHECK VERTICAL */
		connected = 1;

		//cells above
		for( int i = row-1; i >= 0; --i ) {
			if( _board[i][col] == moveColour ) {
				connected++;
			} else {
				break;
			}
		}

		//cells below
		for( int i = row+1; i < _rows; ++i ) {
			if( _board[i][col] == moveColour ) {
				connected++;
			} else {
				break;
			}
		}

		if( connected >= 4 ) {
			return moveColour;
		}

		/*CHECK ANGLES*/
		connected = 1;

		//SouthWest to NorthEast
		for( int r=row-1, c=col+1; r>=0 && c<_cols; --r, ++c ) {
			if( _board[r][c] == moveColour ) {
				connected++;
			} else {
				break;
			}
		}

		for( int r=row+1, c=col-1; r<_rows && c>=0; ++r, --c ) {
			if( _board[r][c] == moveColour ) {
				connected++;
			} else {
				break;
			}
		}

		if( connected >= 4 ) {
			return moveColour;
		}

	
		//NorthWest to SouthEast
		connected = 1;

		for( int r=row+1, c=col+1; r<_rows && c<_cols; ++r, ++c ) {
			if( _board[r][c] == moveColour ) {
				connected++;
			} else {
				break;
			}
		}

		for( int r=row-1, c=col-1; r>=0 && c>=0; --r, --c ) {
			if( _board[r][c] == moveColour ) {
				connected++;
			} else {
				break;
			}
		}

		if( connected >= 4 ) {
			return moveColour;
		}

		// No win
		return Constants.OPEN;
	}

	/**
	 * Test to see if a given move wins without affecting the current
	 * board configuration
	 * @param row The row of the move under investigation
	 * @param col The column of the move under investigation
	 * @param colour The colour of the move under investigation
	 * @return The colour of the winner or Constants.OPEN
	 */
	private byte moveWinsChecker(int row, int col, byte colour)
	{
		_board[row][col] = colour;
		byte toRtn = moveWins(row, col);
		_board[row][col] = Constants.OPEN;

		return toRtn;
	}

	/**
	 * Calculate the value of this board
	 * @param colour The player considering this board
	 * NOTE: colour is used to differentiate between boards with a 
	 * connect-4 for both players. If it's MAX's turn, return max in that
	 * case, and vice versa for MIN's turn
	 * @return The heuristic value of the board
	 */
	public int heuristic(byte colour)
	{
		int whiteScores[] = new int[7], blackScores[] = new int[7];
		for( int i=0; i<whiteScores.length; ++i ) {
			whiteScores[i] = blackScores[i] = 0;
		}
		int found=0;

		countVertical(whiteScores, blackScores);
		countHorizontal(whiteScores, blackScores);
		countDiagonal(whiteScores, blackScores);

		//weighting
		int total = 0;

		//ones with one side
		total += whiteScores[0];
		total -= blackScores[0];

		//ones with two sides
		total += whiteScores[1]*2;
		total -= blackScores[1]*2;
		
		//twos with one open side
		total += whiteScores[2] * 5;
		total -= blackScores[2] * 5;

		//twos with two or more open sides
		total += whiteScores[3] * (
			(colour == Constants.WHITE) ? 10 : 20
		);
		total -= blackScores[3] * (
			(colour == Constants.BLACK) ? 10 : 20
		);

		//threes with one open side
		total += whiteScores[4] * (
			(colour == Constants.WHITE) ? 30 : 40
		);
		total -= blackScores[4] * (
			(colour == Constants.BLACK) ? 30 : 40
		);

		//threes with two or more open sides
		total += whiteScores[5] * (
			(colour == Constants.WHITE) ? 90 : 200
		);
		total -= blackScores[5] * (
			(colour == Constants.BLACK) ? 90 : 200
		);

		if( whiteScores[6] >= 1 && blackScores[6] >= 1 ) {
			return (colour == Constants.WHITE ) ? Integer.MAX_VALUE:
				Integer.MIN_VALUE;
		} else if( whiteScores[6] >= 1 ) {
			total = Integer.MAX_VALUE;
		} else if( blackScores[6] >= 1 ) {
			total = Integer.MIN_VALUE;
		}

		return total;
	}

	/**
	 * Count the scores up for a given colour in the vertical direction
	 * @param colour The colour under investigation
	 * @param scores The array of score counts
	 */
	private void countVerticalByColour(byte colour, int scores[])
	{
		int consec = 0, blanks = 0, inRow=0;
		boolean openleft=false, openright=false;
		for( int c=0; c<_cols; ++c ) {
			for( int r=_rows-1; r>=0; --r ) {
				if( _board[r][c] == colour) {
					consec++;
					if( consec == 4 ) {
						scores[6]++;
					}
					inRow++;
				} else if( _board[r][c] == Constants.OPEN ) {
					blanks++;
					consec = 0;
					if( inRow == 0 ) {
						openleft = true;
					} else if( openleft ) {
						openright = true;
					} else {
						openleft = true;
					}
				} else { 
					countScores(inRow, blanks, openleft,
						openright, scores);
					consec=blanks=inRow=0;
					openleft = openright = false;
				}
			}

			countScores(inRow, blanks, openleft, openright, 
				scores
			);
			consec = blanks = inRow = 0;
			openleft = openright = false;
		}
	}

	/**
	 * Count up scores based on the given parameters
	 * @param inRow The count of elements in a row
	 * @param blanks The number of blanks in the row
	 * @param openleft true if there is an opening to the left of the 
	 * elements
	 * @param openright true if there is an opening to the right of the
	 * elements
	 * @param scores The array to count scores in
	 */
	private void countScores(int inRow, int blanks, boolean openleft, 
		boolean openright, int scores[])
	{
		if( inRow >= 3 && blanks >= 1 ) {
			if( openleft && openright ) {
				scores[5]++;
			} else {
				scores[4]++;
			}
		} else if( inRow == 2 && blanks >= 2 ) {
			if( openleft && openright ) {
				scores[3]++;
			} else {
				scores[2]++;
			}
		} else if( inRow == 1 && blanks >= 3 ) {
			if( openleft && openright ) {
				scores[1]++;
			} else {
				scores[0]++;
			}
		}
	}

	/**
	 * Count the scores up for a given colour in the horizontal direction
	 * @param colour The colour under investigation
	 * @param scores The array to count scores in
	 */
	private void countHorizontalByColour(byte colour, int scores[])
	{
		int consec = 0, blanks = 0, inRow = 0;
		boolean openleft=false, openright=false;
		for( int r=0; r<_rows; ++r ) { 
			for( int c=0; c<_cols; ++c ) {
				if( _board[r][c] == colour ) {
					consec++;
					if( consec == 4 ) {
						scores[6]++;
					}
					inRow++;
				} else if( _board[r][c] == Constants.OPEN ) {
					blanks++;
					consec=0;
					if( inRow == 0 ) {
						openleft = true;
					} else if( openleft ) {
						openright = true;
					} else {
						openleft = true;
					}
				} else {
					countScores(inRow, blanks, openleft,
						openright, scores);
					consec = blanks = inRow = 0;
					openright = openleft = false;
				}
			}

			countScores(inRow, blanks, openleft, openright, 
				scores
			);

			consec = blanks = inRow = 0;
			openleft = openright = false;
		}
	}

	/**
	 * Count the scores up for a given colour in the diagonal directions
	 * @param colour The colour under investigation
	 * @param scores The array to count scores in
	 */
	private void countDiagonalByColour(byte colour, int scores[])
	{
		int rStart=3, cStart=0, consec=0, blanks=0, inRow=0;
		boolean openleft=false, openright=false;

		//NorthWest to SouthEast
		while( rStart+cStart <= 4 ) {
			for( int r=rStart, c=cStart; r<_rows && c<_cols;
				++r, ++c) {
				if( _board[r][c] == colour ) {
					consec++;
					if( consec == 4 ) {
						scores[6]++;
					}
					inRow++;
				} else if( _board[r][c] == Constants.OPEN ) {
					blanks++;
					consec=0;
					if( inRow == 0 ) {
						openleft = true;
					} else if( openleft ) {
						openright = true;
					} else {
						openleft = true;
					}
				} else {
					countScores(inRow, blanks, openleft,
						openright, scores);
					consec = blanks = inRow = 0;
					openleft = openright = false;
				} 
			}

			countScores(inRow, blanks, openleft, openright, scores);

			rStart--;
			if( rStart < 0 ) {
				rStart = 0;
				cStart++;
			}

			consec = blanks = inRow = 0;
			openleft = openright = false;
		}

		// NorthEast to SouthWest
		rStart=0; cStart=3;
		while( rStart+cStart <= 9 ) {
			consec = blanks = inRow = 0;
			openleft = openright = false;
			for( int r=rStart, c=cStart; r<_rows && c>=0;
				--c, ++r ) {
				if( _board[r][c] == colour ) {
					consec++;
					inRow++;
					if( consec == 4 ) {
						scores[6]++;
					}
				} else if( _board[r][c] == Constants.OPEN ) {
					blanks++;
					consec = 0;
					if( inRow == 0 ) {
						openleft = true;
					} else if( openleft ) {
						openright = true;
					} else {
						openleft = true;
					}
				} else {
					countScores(inRow, blanks, openleft,
						openright, scores);
					consec = blanks = inRow = 0;
					openleft = openright = false;
				}
			}

			countScores(inRow, blanks, openleft, openright, scores);

			cStart++;
			if( cStart >= _cols ) {
				cStart = _cols-1;
				rStart++;
			}
		}
	}

	/**
	 * Count all the vertical scores
	 * @param whiteScores The array to count the white scores in
	 * @param blackScores The array to count the black scores in
	 */
	private void countVertical(int whiteScores[], int blackScores[])
	{
		countVerticalByColour(Constants.WHITE, whiteScores);
		countVerticalByColour(Constants.BLACK, blackScores);
	}

	/**
	 * Count all the horizontal scores
	 * @param whiteScores The array to count the white scores in
	 * @param blackScores The array to count the black scores in
	 */
	private void countHorizontal(int whiteScores[], int blackScores[])
	{
		countHorizontalByColour(Constants.WHITE, whiteScores);
		countHorizontalByColour(Constants.BLACK, blackScores);
	}

	/**
	 * Count all the diagonal scores
	 * @param whiteScores The array to count the white scores in
	 * @param blackScores The array to count the black scores in
	 */
	private void countDiagonal(int whiteScores[], int blackScores[])
	{
		countDiagonalByColour(Constants.WHITE, whiteScores);
		countDiagonalByColour(Constants.BLACK, blackScores);
	}

	/**
	 * Calculate the minimax value for the current board
	 * @param colour The colour to maximize for
	 * @param maxDepth The maximum allowable depth
	 * @param curDepth The depth that we currently are at
	 * @param max The max value (used for alpha-beta pruning)
	 * @param min The min value (used for alpha-beta pruning)
	 * @return The minimax value calculated
	 */
	public int minimax(byte colour, int maxDepth, int curDepth, int max, 
		int min)
	{
		byte oppColour = (colour == Constants.BLACK) ? Constants.WHITE : 
			Constants.BLACK; 

		if( maxDepth == curDepth || hasNoChildren() ) {
			return heuristic(oppColour);
		}

		//Maximizing for opponent
		int toRtn=(oppColour == Constants.BLACK) ? Integer.MAX_VALUE : 
			Integer.MIN_VALUE; 
		int curHeur;
		for( int i=0; i<_children.length; ++i ) {
			if( (_children[i] = generateChild(i, oppColour)) != 
				null ) {
				byte win = moveWins(_currentRow[i]+1, i);
				if( win != Constants.OPEN ) {
					curHeur = (win == Constants.BLACK) ? 
						Integer.MIN_VALUE : 
						Integer.MAX_VALUE;
				} else {
					curHeur = _children[i].minimax(
						oppColour, maxDepth, curDepth+1,
						max, min
					);
				}

				//Include depth in heuristic
				if( oppColour == Constants.BLACK ) {
					curHeur+=curDepth;
				} else {
					curHeur-=curDepth;
				}
				
				//Alpha-Beta pruning
				if( (oppColour == Constants.WHITE &&
					curHeur >= min) ||
					(oppColour == Constants.BLACK &&
					curHeur <= max)
				) {
					unrollChild(i);
					return curHeur;
				} 


				if(
					(oppColour == Constants.WHITE &&
						curHeur > toRtn
					)
					||
					(oppColour == Constants.BLACK &&
						curHeur < toRtn
					)
				) {
					toRtn = curHeur;
				}

				if( oppColour == Constants.WHITE &&
					curHeur > max
				) {
					max = curHeur;
				} else if( oppColour == Constants.BLACK &&
					curHeur < min
				) {
					min = curHeur;
				}

				unrollChild(i);
			}
		}

		return toRtn;
	}

	public int negascout(byte colour, int depth, int alpha, int beta)
	{
		byte oppColour = (colour == Constants.BLACK) ? Constants.WHITE :
		                        Constants.BLACK;

		if( depth == 0 || hasNoChildren() ) {
			return heuristic(oppColour);
		}

		int a, b;

		b = beta;
		for( int i=0; i<_children.length; ++i ) {
			_children[i] = generateChild(i, oppColour);
			if( _children[i] != null ) {
				a = _children[i].negascout(oppColour, depth-1, -b, -alpha);

				if( a>alpha ) {
					alpha = a;
				} 
			
				if( alpha >= beta ) {
					unrollChild(i);
					return alpha;
				}

				if( alpha >= b ) {
					_children[i] = generateChild(i, oppColour);
					alpha = -_children[i].negascout(oppColour,
						depth-1, -beta, -alpha
					);
					if( alpha >= beta ) {
						unrollChild(i);
						return alpha;
					}
				}

				b = alpha + 1;
				unrollChild(i);
			}
		}

		return alpha;
	}

	/**
	 * Calculate the best move for the given colour from the current
	 * board configuration
	 * @param colour The colour to find a move for
	 * @return The message to send to the opponent
	 */
	public Message bestMove(byte colour)
	{
		boolean isFirst = true;
		int bestHeur = 100000, idx=-1, nextValue;
		byte win = Constants.OPEN;
		long time = System.currentTimeMillis();

		// For each child of the current board layout
		for( int i=0; i<_children.length; ++i ) {
			_children[i] = generateChild(i, colour);
			if( _children[i] != null ) {
				win = moveWins(_currentRow[i]+1, i);
				if( isFirst ) {
					if( win == Constants.BLACK ) {
						bestHeur = Integer.MIN_VALUE;
					} else if( win == Constants.WHITE ) {
						bestHeur = Integer.MAX_VALUE;
					} else {
						bestHeur = _children[i].minimax(
							colour, Constants.DEPTH,
							0, Integer.MIN_VALUE,
							Integer.MAX_VALUE
						);
					}
					idx = i;
					isFirst = false;
				} else {
					if( win == Constants.BLACK ) {
						nextValue = Integer.MIN_VALUE;
					} else if( win == Constants.WHITE ) {
						nextValue = Integer.MAX_VALUE;
					} else {
						nextValue= _children[i].minimax(
							colour, Constants.DEPTH,
							0, Integer.MIN_VALUE,
							Integer.MAX_VALUE
						);
					}
					if(
						(colour == Constants.WHITE &&
							nextValue > bestHeur
						) || (
						colour == Constants.BLACK &&
							nextValue < bestHeur 
						)
					) {
						bestHeur = nextValue;
						idx = i;
					}
				}
				unrollChild(i);
			}
		}
		//If you're cornered, pick a random legal move
		if( bestHeur == Integer.MAX_VALUE && 
			colour == Constants.BLACK ||
			bestHeur==Integer.MIN_VALUE && colour==Constants.WHITE
		) {
			System.out.println("Doing random move");
			Vector<Message> safeMoves = getLegalMoves();
			return safeMoves.get(new Random().nextInt(
				safeMoves.size())
			);
		}

		if( idx == -1 ) {
			System.out.println("TIE?");
			System.out.println("Move time: " + 
				(System.currentTimeMillis()-time)
			);
			return new Message(0,0,1);
		} else {
			win = moveWinsChecker(_currentRow[idx], idx,
				colour);
			return new Message(_currentRow[idx], idx,
				((win != Constants.OPEN) ? 2 : 0)
			);
		}
	}

	/**
	 * Getter for GUI purposes
	 * @return The 2-D array of bytes which makes up the core board
	 * representation
	 */
	public byte[][] getBoard()
	{
		return _board;
	}

	public static void main(String argv[])
	throws Exception
	{
		ConnectFourBoard board = new ConnectFourBoard();
		board.bestMove(Constants.WHITE);
	}
}
