package uk.ac.stfc.isis.ibex.scriptgenerator.table;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import uk.ac.stfc.isis.ibex.model.ModelObject;
import uk.ac.stfc.isis.ibex.scriptgenerator.ActionParameter;

/**
 * Class defines one action or 'step' in the script.
 *
 */
public class ScriptGeneratorAction extends ModelObject {

	/**
	 * Contains a mapping from the action's parameters and the action's values for those parameters.
	 */
	private Map<ActionParameter, String> actionParameterValues;
	
	/**
	 * Contains the reason for the action being invalid. Empty optional if valid.
	 */
	private Optional<String> invalidityReason = Optional.empty();
	
	/**
	 * The property to fire a change of if the action becomes valid or invalid.
	 */
	private static final String VALIDITY_PROPERTY = "validity";
	
	/**
	 * The property to fire if the actions values change.
	 */
	private static final String VALUE_PROPERTY = "value";

	/**
	 * Default constructor sets each parameter/value pair using input argument.
	 * @param paremetersMap
	 * 			The user-set value (string) for the specified ActionParameter.
	 */
	public ScriptGeneratorAction(Map<ActionParameter, String> paremetersMap) {
		this.actionParameterValues = paremetersMap;
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
		firePropertyChange(VALUE_PROPERTY, oldValue, value);
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
	 * 			Map of parameter, value pairs.
	 */
	public Map<ActionParameter, String> getAllActionParameters() {
		return actionParameterValues;
	}
	
	/**
	 * Get all parameter, value pairs. With the parameter as a string
	 * @return
	 * 			Map of parameter, value pairs.
	 */
	public Map<String, String> getAllActionParametersAsString() {
		Map<String, String> actionParametersAsString = new HashMap<>();
		for(Map.Entry<ActionParameter, String> actionParam : actionParameterValues.entrySet()) {
			actionParametersAsString.put(actionParam.getKey().getName(), actionParam.getValue());
		}
		return actionParametersAsString;
	}
	
	/**
	 * Set this action as valid.
	 */
	public void setValid() {
		firePropertyChange(VALIDITY_PROPERTY, isValid(), true);
		invalidityReason = Optional.empty();
	}
	
	/**
	 * Set this action as invalid with a reason.
	 * 
	 * @param reason The reason for this being invalid.
	 */
	public void setInvalid(String reason) {
		firePropertyChange(VALIDITY_PROPERTY, isValid(), false);
		invalidityReason = Optional.of(reason);
	}
	
	/**
	 * True if the action is valid, false if not.
	 * 
	 * @return True if the action is valid, false if not.
	 */
	public boolean isValid() {
		return invalidityReason.isEmpty();
	}
	
	/**
	 * Get the current reason for invalidity (may be empty option if valid).
	 * 
	 * @return Optional string of reason if invalid. Null if valid.
	 */
	public Optional<String> getInvalidityReason() {
		return invalidityReason;
	}
	
}
