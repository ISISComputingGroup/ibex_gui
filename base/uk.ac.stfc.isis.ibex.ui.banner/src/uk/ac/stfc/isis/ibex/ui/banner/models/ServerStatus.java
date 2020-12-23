package uk.ac.stfc.isis.ibex.ui.banner.models;

public enum ServerStatus {

	/** Server is running OK. */
	OK("OK"),
	/** Server is NOT RUNNING. */
	OFF("NOT RUNNING"),
	/** Server is running PARTIALLY. */
	UNSTABLE("UNSTABLE"),
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
