package uk.ac.stfc.isis.ibex.nicos.messages.scriptstatus;

import uk.ac.stfc.isis.ibex.model.ModelObject;

/**
 * Holder of script information returned from server.
 * 
 * THIS IS DESERIALISED FROM JSON AND SO THE CONSTRUCTOR MAY NOT BE CALLED
 */
@SuppressWarnings({ "checkstyle:membername", "unused" })
public class QueuedScript extends ModelObject {
	
	private static final String INITIAL_NAME = "My Script";
    private static final String INITIAL_CODE = "# Script\nprint(\"My Script\")";
	
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
	private String name;
	
	/**
	 * Code of the script.
	 */
	private String script;
	
	/**
	 * Constructor for a script that uses the default name and code.
	 */
	public QueuedScript() {
		setName(INITIAL_NAME);
		setCode(INITIAL_CODE);
	}
	
	/**
	 * Get the script contents.
	 * @return The script contents.
	 */
	public String getCode() {
		return script;
	}
	
	/**
	 * Set the contents of the script.
	 * @param script The contents of the script.
	 */
	public void setCode(String script) {
		firePropertyChange("code", this.script, this.script = script);
	}
	
	/**
	 * Get the name of the script.
	 * @return The name of the script.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Set the name of the script.
	 * @param name The name of the script.
	 */
	public void setName(String name) {
		firePropertyChange("name", this.name, this.name = name);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((reqid == null) ? 0 : reqid.hashCode());
		return result;
	}

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
	
	
}
