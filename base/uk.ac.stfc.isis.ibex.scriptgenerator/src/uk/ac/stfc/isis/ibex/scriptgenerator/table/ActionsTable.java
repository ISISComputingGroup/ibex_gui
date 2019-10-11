package uk.ac.stfc.isis.ibex.scriptgenerator.table;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import uk.ac.stfc.isis.ibex.model.ModelObject;
import uk.ac.stfc.isis.ibex.scriptgenerator.ActionParameter;

public class ActionsTable extends ModelObject {

	private List<ActionParameter> actionParameters;
	private ArrayList<ScriptGeneratorAction> actions = new ArrayList<ScriptGeneratorAction>();
	
	public List<ScriptGeneratorAction> getActions() {
		return actions;
	}

	public ActionsTable(List<ActionParameter> actionParameter) {
		this.actionParameters = actionParameter;
	}

	public List<ActionParameter> getActionParameters() {
		return this.actionParameters;
	}

	public void addEmptyAction(List<ActionParameter> actionParameters) {
		var parametersMap = new HashMap<String, String>();
		// Make a parameter/string pair for each parameter in the action
		for (ActionParameter actionParameter: actionParameters) {
			// TODO: Add a sensible default for the action parameter
			parametersMap.put(actionParameter.getName(), actionParameter.getName()+Integer.toString(actions.size()));
		}
		
		var newAction = new ScriptGeneratorAction(parametersMap);
		
				
		firePropertyChange("actions", this.actions, this.actions.add(newAction));
	}
		
	

}
