package uk.ac.stfc.isis.ibex.scriptgenerator;

/**
 * Data type for an action parameter.
 * 
 */
public class ActionParameter {

	private String name;
	
	/**
	 * This class holds the name (and other information) about an action parameter.
	 * @param name The name of the action parameter (column header)
	 */
	public ActionParameter(String name) {
		this.name = name;
	}

	/**
	 * @return The name of this parameter.
	 */
	public String getName() {
		return name;
	}


}
