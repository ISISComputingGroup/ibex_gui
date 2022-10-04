package uk.ac.stfc.isis.ibex.scriptgenerator.table;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import uk.ac.stfc.isis.ibex.model.ModelObject;
import uk.ac.stfc.isis.ibex.scriptgenerator.JavaActionParameter;
import uk.ac.stfc.isis.ibex.scriptgenerator.ScriptGeneratorProperties;

/**
 * Class defines one action or 'step' in the script.
 *
 */
public class ScriptGeneratorAction extends ModelObject {

    /**
     * Contains a mapping from the action's parameters and the action's values for those parameters.
     */
    private Map<JavaActionParameter, String> actionParameterValues;

    /**
     * Contains the reason for the action being invalid. Empty optional if valid.
     */
    private Optional<String> invalidityReason = Optional.empty();

    /**
     * Contains the estimated time to complete the action. Empty optional if the action is invalid.
     */
    private Optional<Number> estimatedTime = Optional.empty();
    
    /**
     * Contains the custom estimates of an action. Empty optional if the action is invalid.
     */
    private Optional<List<Number>> customEstimates = Optional.empty();
    
    private ActionDynamicScriptingStatus dynamicScriptingStatus = ActionDynamicScriptingStatus.NO_STATUS;

    /**
     * Default constructor sets each parameter/value pair using input argument.
     * @param paremetersMap
     * 			The user-set value (string) for the specified ActionParameter.
     */
    public ScriptGeneratorAction(Map<JavaActionParameter, String> paremetersMap) {
    	this.actionParameterValues = paremetersMap;
    }

    /**
     * Constructor sets the parameter/value pairs to copy an existing ScriptGeneratorAction.
     * @param actionToCopy
     * 			The action to copy.
     */
    public ScriptGeneratorAction(ScriptGeneratorAction actionToCopy) {
    	this.actionParameterValues = new HashMap<JavaActionParameter, String>();
    	// Add all Parameter/value pairs to the hash map
    	for (Map.Entry<JavaActionParameter, String> entry: actionToCopy.getActionParameterValueMap().entrySet()) {
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
    public void setActionParameterValue(JavaActionParameter actionParameter, String value) {
    	String oldValue = actionParameterValues.get(actionParameter); 
    	actionParameterValues.put(actionParameter, value);
    	firePropertyChange(actionParameter.getName(), oldValue, value);
    	firePropertyChange(ScriptGeneratorProperties.VALUE_PROPERTY, oldValue, value);
    }

    /**
     * Gets an action parameter value.
     * @param parameter
     * 			The parameter to get.
     * @return 
     * 			The value of the parameter.
     */
    public String getActionParameterValue(JavaActionParameter parameter) {
    	return actionParameterValues.get(parameter);
    }

    /**
     * Get all parameter, value pairs.
     * @return
     * 			Map of parameter, value pairs.
     */
    public Map<JavaActionParameter, String> getActionParameterValueMap() {
    	return actionParameterValues;
    }

    /**
     * Get all parameter, value pairs. With the parameter as a string
     * @return
     * 			Map of parameter, value pairs.
     */
    public Map<String, String> getActionParameterValueMapAsStrings() {
    	Map<String, String> actionParametersAsString = new HashMap<>();

    	for (Map.Entry<JavaActionParameter, String> actionParam : actionParameterValues.entrySet()) {
    		actionParametersAsString.put(actionParam.getKey().getName(), actionParam.getValue());
    	}
    	return actionParametersAsString;
    }

    /**
     * Set this action as valid.
     */
    public void setValid() {
    	firePropertyChange(ScriptGeneratorProperties.VALIDITY_PROPERTY, isValid(), true);
		invalidityReason = Optional.empty();
    }

    /**
     * Set this action as invalid with a reason.
     * 
     * @param reason The reason for this being invalid.
     */
    public void setInvalid(String reason) {
    	firePropertyChange(ScriptGeneratorProperties.VALIDITY_PROPERTY, isValid(), false);
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

    /**
     * Set the estimated time that this step will run for in seconds.
     * @param newEstimatedTime estimated time
     */
    public void setEstimatedTime(Optional<Number> newEstimatedTime) {
    	firePropertyChange(ScriptGeneratorProperties.TIME_ESTIMATE_PROPERTY, estimatedTime, newEstimatedTime);
    	estimatedTime = newEstimatedTime;
    }
    
    /**
     * Set the custom estimate value.
     * @param newEstimates custom estimates numbers
     */
    public void setCustomEstimate(Optional<List<Number>> newEstimates) {
    	firePropertyChange(ScriptGeneratorProperties.CUSTOM_ESTIMATE_PROPERTY, customEstimates, newEstimates);
    	customEstimates = newEstimates;
    }

    /**
     * Get the estimated time in seconds.
     * @return estimated time in seconds
     */
    public Optional<Number> getEstimatedTime() {
    	return estimatedTime;
    }
    
    /**
     * Get the estimated custom numbers.
     * @return custom estimates numbers
     */
    public Optional<List<Number>> getEstimatedCustom() {
    	return customEstimates;
    }
    
    /**
     * Sets this script into "executing" state.
     */
    public void setExecuting() {
    	firePropertyChange(ScriptGeneratorProperties.VALUE_PROPERTY, dynamicScriptingStatus, dynamicScriptingStatus = ActionDynamicScriptingStatus.EXECUTING);
    }   
    
    /**
     * Sets this script into "paused before execution" state.
     */
    public void setPausedBeforeExecution() {
    	firePropertyChange(ScriptGeneratorProperties.VALUE_PROPERTY, dynamicScriptingStatus, dynamicScriptingStatus = ActionDynamicScriptingStatus.PAUSED_BEFORE_EXECUTION);
    }
    
    /**
     * Set this script into "paused during execution" state.
     */
    public void setPausedDuringExecution() {
    	firePropertyChange(ScriptGeneratorProperties.VALUE_PROPERTY, dynamicScriptingStatus, dynamicScriptingStatus = ActionDynamicScriptingStatus.PAUSED_DURING_EXECUTION);
    }
    
    /**
     * Clears a previously set dynamic scripting state.
     */
    public void clearDynamicScriptingStatus() {
    	firePropertyChange(ScriptGeneratorProperties.VALUE_PROPERTY, dynamicScriptingStatus, dynamicScriptingStatus = ActionDynamicScriptingStatus.NO_STATUS);
    }
    
    /**
     * Gets the current dynamic scripting state of this script.
     * @return the state
     */
    public ActionDynamicScriptingStatus getDynamicScriptingStatus() {
		return dynamicScriptingStatus;
	}
    
    /**
     * Gets whether this script is paused during execution.
     * @return true if paused during execution; false otherwise
     */
    public Boolean wasPausedDuringExecution() {
    	return dynamicScriptingStatus.equals(ActionDynamicScriptingStatus.PAUSED_DURING_EXECUTION);
    }
    
}	
