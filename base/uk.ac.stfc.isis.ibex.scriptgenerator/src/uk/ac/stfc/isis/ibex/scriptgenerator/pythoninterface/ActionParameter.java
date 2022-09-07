package uk.ac.stfc.isis.ibex.scriptgenerator.pythoninterface;

import java.util.List;

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
	
	/**
	 *  Get whether the parameter should copy the previous row's value.
	 * 
	 * @return A boolean of whether the parameter value should be copied from the previous row
	 */
	boolean getCopyPreviousRow();
	
	/**
	 * Get the type of the action parameter. Uses the default value given in the Python Script Definition.
	 * 
	 * @return A string name of the action parameter type.
	 */
	String getType();
	
	/**
	 * Get the Enum members of the action parameter.
	 * 
	 * @return A List of Strings of the names. If the action parameter is not an Enum the list will be empty.
	 */
	List<String> getEnumMembers();
}
