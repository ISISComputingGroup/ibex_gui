package uk.ac.stfc.isis.ibex.nicos.messages.scriptstatus;

/**
 * Holder of script information returned from server.
 * 
 * THIS IS DESERIALISED FROM JSON AND SO THE CONSTRUCTOR MAY NOT BE CALLED
 */
@SuppressWarnings({ "checkstyle:membername", "unused" })
public class QueuedScript {
	/**
	 * User who submitted the script.
	 */
	public String user;
	
	/**
	 * Request ID - unique script identifier.
	 */
	public String reqid;
	
	/**
	 * Name of the script.
	 */
	public String name;
	
	/**
	 * Code of the script.
	 */
	public String script;
}
