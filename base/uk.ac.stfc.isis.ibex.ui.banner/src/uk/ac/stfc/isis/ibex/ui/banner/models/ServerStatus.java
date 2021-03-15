package uk.ac.stfc.isis.ibex.ui.banner.models;

/**
 * Represents the state of (a part of) the IBEX server.
 */
public enum ServerStatus {

	/** Server is UP. */
	UP("UP"),
	/** Server is DOWN. */
	DOWN("DOWN"),
	/** Server is PARTIALLY running. */
	PARTIAL("PARTIAL"),
	/** Server status is UNKNOWN. */
	UNKNOWN("UNKNOWN");

	private final String name;

	/**
	 * Constructor.
	 * 
	 * @param name name of the language
	 */
	ServerStatus(String name) {
		this.name = name;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return name;
	}
}
