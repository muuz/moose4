import java.net.Socket;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.IOException;

/**
 * An abstaction of the communicator used by both client and server
 * to communicate on the object streams
 */
public class ConnectFourCommunicator
{
	/**
	 * The input stream
	 */
	private ObjectInputStream _ois;

	/**
	 * The output stream
	 */
	private ObjectOutputStream _oos;

	/**
	 * Construct a new ConnectFourCommunicator on the given socket
	 * @param s The socket to communicate on
	 * @throws IOException If there is an I/O issue opening the streams
	 */
	public ConnectFourCommunicator(Socket s)
	throws IOException
	{
		_oos = new ObjectOutputStream(s.getOutputStream());
		_ois = new ObjectInputStream(s.getInputStream());
	}

	/**
	 * Read a move from the input stream
	 * @return The message object read from the stream
	 */
	public Message readMove()
	{
		try {
			System.out.print("Awaiting move...");
			Message m = (Message)_ois.readObject();
			System.out.println("DONE");
			return m;
		} catch( ClassCastException cce ) {
			System.err.println(cce.getMessage());
			System.err.println("ERROR: Object received could not "+
				"be cast as a Message."
			);
			System.exit(1);
		} catch( ClassNotFoundException cnfe ) {
			System.err.println(cnfe.getMessage());
			System.err.println("ERROR: Class of the serialized "+
				"object (should be Message) could not be found"+
				"while attempting to read a Message"
			);
			System.exit(1);
		} catch( IOException ioe ) {
			System.err.println(ioe.getMessage());
			System.err.println("ERROR: An I/O issue has occured " +
				"while reading a Message"
			);
			System.exit(1);
		}

		System.err.println("WARNING: Execution has reached a thought "+
			"to be unreachable section");
		return null;
	}

	/**
	 * Write the given move to the output stream
	 * @param move The move to write
	 */
	public void writeMove(Message move)
	{
		try {
			_oos.writeObject(move);
			_oos.flush();
		} catch( IOException ioe ) {
			System.err.println(ioe.getMessage());
			System.err.println("ERROR: An I/O issue has occured "+
				"while writing a Message"
			);
			System.exit(1);
		}
	}
}
