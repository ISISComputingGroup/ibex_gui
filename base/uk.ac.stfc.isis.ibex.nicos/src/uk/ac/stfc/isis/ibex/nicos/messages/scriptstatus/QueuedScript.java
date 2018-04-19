package uk.ac.stfc.isis.ibex.nicos.messages.scriptstatus;

import org.eclipse.swt.widgets.Event;

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

	// TODO: documentation - do we need this method to be so complicated?
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((reqid == null) ? 0 : reqid.hashCode());
		return result;
	}

	// TODO: documentation - do we need this method to be so complicated?
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		QueuedScript other = (QueuedScript) obj;
		if (reqid == null) {
			if (other.reqid != null) {
				return false;
			}
		} else if (!reqid.equals(other.reqid)) {
			return false;
		}
		return true;
	}
	
	@Override
	public String toString() {
		return reqid;
	}
	
}
