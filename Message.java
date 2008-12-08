public class Message implements java.io.Serializable{
	int row;
	int col;
	int mType;

	public Message(int r, int c, int m){
		row = r;		// Zero-based row number of the move, from 0 to 6
		col = c;		// Zero-based column number of the move, from 0 to 6
		mType = m;		// Message type, from 0 to 2
	}

	public int getRow(){
		return row;
	}

	public int getColumn(){
		return col;
	}

	public int getMessageType(){
		return mType;
	}
}