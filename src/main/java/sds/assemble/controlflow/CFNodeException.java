package sds.assemble.controlflow;

/**
 * This class is for exception of
 * {@link CFNode <code>CFNode</code>}.
 * @author inagaki
 */
public class CFNodeException extends RuntimeException {
	/**
	 * constructor.
	 * @param message messsage for notification
	 */
	public CFNodeException(String message) {
		super(message);
	}
}