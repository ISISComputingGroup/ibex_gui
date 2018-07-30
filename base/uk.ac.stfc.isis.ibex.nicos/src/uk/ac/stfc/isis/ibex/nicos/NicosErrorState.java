/**
 * 
 */
package uk.ac.stfc.isis.ibex.nicos;

/**
 * Errors that can occur when talking to the NICOS backend.
 */
public enum NicosErrorState {
	
	/**
	 * No error.
	 */
	NO_ERROR("No error"),
	
	/**
	 * Error for when a script could not be sent.
	 */
	SCRIPT_SEND_FAIL("Failed to send script"),

    /**
     * Error for when a login fails.
     */
    FAILED_LOGIN("Failed to login"),

    /**
     * Error for when the protocol received from the server is unrecognised.
     */
    INVALID_PROTOCOL("NICOS protocol is invalid"),

    /**
     * Error for when the serialiser received from the server is unrecognised.
     */
    INVALID_SERIALISER("NICOS serialiser is invalid"),

    /**
     * Error for when the connection to nicos throws an exception.
     */
    CONNECTION_FAILED("The connection to NICOS failed"),
    
    /**
     * Error for when a response was expected but none was received.
     */
    NO_RESPONSE("Server did not respond to request.");
	
	private final String message;

	/**
	 * Make a new nicos error state with a specific user-facing message.
	 * @param message the message to show to the user
	 */
	NicosErrorState(String message) {
		this.message = message;
	}
	
	/**
	 * Gets the error message from this type of error.
	 */
	@Override
	public String toString() {
		return message;
	}
}
