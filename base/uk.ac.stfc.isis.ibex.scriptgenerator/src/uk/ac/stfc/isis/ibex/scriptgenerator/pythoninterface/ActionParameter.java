package uk.ac.stfc.isis.ibex.scriptgenerator.pythoninterface;

/**
 * An action parameter with a name and default value.
 */
public interface ActionParameter {
	
	/**
	 * Get the name of the action parameter.
	 * 
	 * @return A string name of the action parameter.
	 */
	String getName();
	
	/**
	 * Get the default value for the action parameter.
	 * 
	 * @return The default value for the action parameter
	 */
	String getDefaultValue();

}
