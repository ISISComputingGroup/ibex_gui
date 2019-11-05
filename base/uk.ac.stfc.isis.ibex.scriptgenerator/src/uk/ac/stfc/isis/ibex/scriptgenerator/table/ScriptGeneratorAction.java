package uk.ac.stfc.isis.ibex.scriptgenerator.table;

import java.util.HashMap;
import java.util.Map;


import uk.ac.stfc.isis.ibex.model.ModelObject;
import uk.ac.stfc.isis.ibex.scriptgenerator.ActionParameter;

/**
 * Class defines one action or 'step' in the script.
 *
 */
public class ScriptGeneratorAction extends ModelObject {

	private HashMap<ActionParameter, String > actionParameterValues;

	/**
	 * Default constructor sets each parameter/value pair using input argument.
	 */
	public ScriptGeneratorAction(HashMap<ActionParameter, String> values) {
		this.actionParameterValues = values;
	}
	
	/**
	 * Constructor sets the parameter/value pairs to copy an existing ScriptGeneratorAction.
	 * @param actionToCopy
	 */
	public ScriptGeneratorAction(ScriptGeneratorAction actionToCopy) {
		this.actionParameterValues = new HashMap<ActionParameter, String>();
		// Add all Parameter/value pairs to the hash map
		for (Map.Entry<ActionParameter, String> entry: actionToCopy.getAllActionParameters().entrySet()) {
			setActionParameterValue(entry.getKey(), entry.getValue());
		}
	}
	
	/**
	 * Sets an action parameter value.
	 * @param actionParameter
	 * 			The name of the parameter to set.
	 * @param value
	 * 			The new value to set the parameter to.
	 */
	public void setActionParameterValue(ActionParameter actionParameter, String value) {
		String oldValue = actionParameterValues.get(actionParameter); 
		actionParameterValues.put(actionParameter, value);
		firePropertyChange(actionParameter.getName(), oldValue, value);
	}
	
	/**
	 * Gets an action parameter value.
	 * @param parameterName
	 * 			The name of the parameter to get.
	 * @return
	 * 			The value of the parameter.
	 */
	public String getActionParameterValue(ActionParameter parameter) {
		return actionParameterValues.get(parameter);
	}
	
	/**
	 * Get all parameter, value pairs.
	 * @return
	 * 			HashMap of parameter, value pairs.
	 */
	public HashMap<ActionParameter, String> getAllActionParameters(){
		return actionParameterValues;
	}
	
}
