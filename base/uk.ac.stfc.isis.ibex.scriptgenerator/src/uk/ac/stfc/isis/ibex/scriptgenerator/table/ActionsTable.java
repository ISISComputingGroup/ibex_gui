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
	private List<ScriptGeneratorAction> actions = Collections.emptyList();

	public List<ScriptGeneratorAction> getActions() {
		return actions;
	}

	public ActionsTable(List<ActionParameter> actionParameter) {
		this.actionParameters = actionParameter;
		
	}

	public List<ActionParameter> getActionParameters() {
		return this.actionParameters;
	}
	
	public void set_actions() {
		var actions = new ArrayList<ScriptGeneratorAction>(); 
		var map = new HashMap<String, String>();
		map.put("column name", "Value");
		map.put("code", "anothervalue");
		actions.add(new ScriptGeneratorAction(map));
		actions.add(new ScriptGeneratorAction(map));
		
		firePropertyChange("actions", this.actions, this.actions=actions);
		
	}

}
