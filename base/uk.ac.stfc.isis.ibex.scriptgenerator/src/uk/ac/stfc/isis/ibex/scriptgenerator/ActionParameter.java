package uk.ac.stfc.isis.ibex.scriptgenerator;

/**
 * Data type for an action parameter.
 * 
 */
public class ActionParameter {

	private String name;
	
	/**
	 * This class holds the name about an action parameter.
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

	
	/**
	 * Check to see if the ActionParameter's attributes are equal to provided object
	 * @param The object to compare against
	 */
	@Override
	public boolean equals(Object o) {
		if (this.getClass() != o.getClass()) return false;	
		
		ActionParameter actionParameter = (ActionParameter) o;
		return actionParameter.getName() == this.getName();
		
	}

}
