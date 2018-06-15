package uk.ac.stfc.isis.ibex.nicos.messages.scriptstatus;

import java.util.Objects;

import com.google.common.base.Strings;

import uk.ac.stfc.isis.ibex.model.ModelObject;
import uk.ac.stfc.isis.ibex.nicos.Nicos;

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
	 * Sends modified script to NICOS.
	 */
	public void sendNewScriptToNicos() {
		Nicos.getDefault().getModel().modifyScript(reqid, script);
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

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		return Objects.hash(name, script, reqid, user);
	}

	/**
	 * {@inheritDoc}
	 */
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
		
		return Objects.equals(reqid, ((QueuedScript) obj).reqid);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return reqid;
	}
	
}
