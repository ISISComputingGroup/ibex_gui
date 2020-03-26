package uk.ac.stfc.isis.ibex.scriptgenerator;

import java.util.List;
import java.util.Map;

import com.google.gson.annotations.SerializedName;

/**
 * This class is used in ParametersConverter to make it easier to convert list of actions and mapping parameters to its values 
 * in JSON.
 */
public class ScriptGeneratorActionConverter {
	@SerializedName ("action_name")
	private String actionName;
	private List<Map<String, String>> parameters;
	
	/**
	 * Initialise the private members.
	 * @param actionName name of action
	 * @param parameters list of mapped parameter to its values
	 */
	ScriptGeneratorActionConverter(String actionName, List<Map<String, String>> parameters) {
		this.actionName = actionName;
		this.parameters = parameters;
	}
	/**
	 * Set name of the action.
	 * @param actionName name of action
	 */
	public void setActionName(String actionName) {
		this.actionName = actionName;
	}
	
	/**
	 * Sets parameters.
	 * @param parameters mapped parameters to its value.
	 */
	public void setParameters(List<Map<String, String>> parameters) {
		this.parameters = parameters;
	}
	
	/**
	 * Gets List of mapped parameters to its values for e.g. temp->1, uamps -> 5.
	 * @return parameters.
	 */
	public List<Map<String, String>> getParameters() {
		return this.parameters;
	}
	
	/**
	 * Gets name of actions.
	 * @return name of action
	 */
	public String getActionName() {
		return this.actionName;
	}
}
