package uk.ac.stfc.isis.ibex.scriptgenerator.table;

import java.util.HashMap;
import java.util.Map;


import uk.ac.stfc.isis.ibex.model.ModelObject;

/**
 * Class defines one action or 'step' in the script
 *
 */
public class ScriptGeneratorAction extends ModelObject {

	private HashMap<String, String > actionParameterValues;

	/**
	 * Default constructor sets each parameter/value pair using input argument
	 */
	public ScriptGeneratorAction(HashMap<String, String> values) {
		this.actionParameterValues = values;
	}
	
	/**
	 * Constructor sets the parameter/value pairs to copy an existing ScriptGeneratorAction
	 * @param actionToCopy
	 */
	public ScriptGeneratorAction(ScriptGeneratorAction actionToCopy) {
		this.actionParameterValues = new HashMap<String, String>();
		// Add all Parameter/value pairs to the hash map
		for (Map.Entry<String, String> entry: actionToCopy.getAllActionParameters().entrySet()) {
			setActionParameterValue(entry.getKey(), entry.getValue());
		}
	}
	
	/**
	 * Sets an action parameter value
	 * @param key
	 * 			The name of the parameter to set
	 * @param value
	 * 			The new value to set the parameter to
	 */
	public void setActionParameterValue(String key, String value) {
		String oldValue = actionParameterValues.get(key); 
		actionParameterValues.put(key, value);
		firePropertyChange(key, oldValue, value);
	}
	
	/**
	 * Gets an action parameter value
	 * @param parameterName
	 * 			The name of the parameter to get
	 * @return
	 * 			The value of the parameter
	 */
	public String getActionParameterValue(String parameterName) {
		return actionParameterValues.get(parameterName);		
	}
	
	/**
	 * Get all parameter, value pairs
	 * @return
	 * 			HashMap of parameter, value pairs
	 */
	public HashMap<String, String> getAllActionParameters(){
		return actionParameterValues;
	}
	
}
