/**
 * A general-purpose exception thrown when input provided to a method is invalid
 */
public class InvalidInputException extends Exception
{
	/**
	 * Construct a new InvalidInputException with the default message
	 */
	public InvalidInputException()
	{
		super();
	}

	/**
	 * Construct a new InvalidInputException with the given message
	 * @param msg The message to provide
	 */
	public InvalidInputException(String msg)
	{
		super(msg);
	}
}
