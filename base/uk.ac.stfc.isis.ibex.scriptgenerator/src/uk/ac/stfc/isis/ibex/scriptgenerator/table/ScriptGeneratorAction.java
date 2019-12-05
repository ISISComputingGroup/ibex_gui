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

	private HashMap<ActionParameter, String> actionParameterValues;
	private boolean valid = true;
	private String invalidityReason = null;

	/**
	 * Default constructor sets each parameter/value pair using input argument.
	 * @param values
	 * 			The user-set value (string) for the specified ActionParameter.
	 */
	public ScriptGeneratorAction(HashMap<ActionParameter, String> values) {
		this.actionParameterValues = values;
	}
	
	/**
	 * Constructor sets the parameter/value pairs to copy an existing ScriptGeneratorAction.
	 * @param actionToCopy
	 * 			The action to copy.
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
	public HashMap<ActionParameter, String> getAllActionParameters() {
		return actionParameterValues;
	}

	/**
	 * Get all parameter value pairs with parameters as strings.
	 * 
	 * @return HashMap of parameter (string), value pairs.
	 */
	public HashMap<String, String> getAllActionParametersAsStrings() {
		HashMap<String, String> actionParamStringValues = new HashMap<>();
		for (Map.Entry<ActionParameter, String> entry : actionParameterValues.entrySet()) {
			actionParamStringValues.put(entry.getKey().getName(), entry.getValue());
		}
		return actionParamStringValues;
	}
	
	/**
	 * Set this action as valid.
	 */
	public void setValid() {
		valid = true;
		invalidityReason = null;
	}
	
	/**
	 * Set this action as invalid with a reason.
	 * 
	 * @param reason The reason for this being invalid.
	 */
	public void setInvalid(String reason) {
		valid = false;
		invalidityReason = reason;
	}
	
	/**
	 * True if the action is valid, false if not.
	 * 
	 * @return True if the action is valid, false if not.
	 */
	public boolean isValid() {
		return valid;
	}
	
	/**
	 * Get the current reason for invalidity (may be null if valid).
	 * 
	 * @return String of reason if invalid. Null if valid.
	 */
	public String getInvalidityReason() {
		return invalidityReason;
	}
	
}
