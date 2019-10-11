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
	//private ArrayList<ScriptGeneratorAction> actions = new ArrayList<ScriptGeneratorAction>();
	
	public List<ScriptGeneratorAction> getActions() {
		return actions;
	}

	public ActionsTable(List<ActionParameter> actionParameter) {
		this.actionParameters = actionParameter;
		
	}

	public List<ActionParameter> getActionParameters() {
		return this.actionParameters;
	}
	
	public void setActions() {
		var actions = new ArrayList<ScriptGeneratorAction>(); 
		var map = new HashMap<String, String>();
		var map2 = new HashMap<String, String>();
		var map3 = new HashMap<String, String>();
		map.put("column name", "Value1");
		map.put("code", "anothervalue1");
		actions.add(new ScriptGeneratorAction(map));
		map2.put("column name", "Value2");
		map2.put("code", "anothervalue2");
		actions.add(new ScriptGeneratorAction(map2));
		map3.put("column name", "Value3");
		map3.put("code", "anothervalue3");
		actions.add(new ScriptGeneratorAction(map3));
		
		firePropertyChange("actions", this.actions, this.actions=actions);
		
	}

	public void addEmptyAction(List<ActionParameter> actionParameters) {
		var map = new HashMap<String, String>();
		var actions = new ArrayList<ScriptGeneratorAction>(); 
		// Make a parameter/string pair for each parameter in the action
		for (ActionParameter actionParameter: actionParameters) {
			map.put(actionParameter.getName(), "empty");
		}
		
		var newAction = new ScriptGeneratorAction(map);
		// Add the new, empty action to the list
		//actions.add(new ScriptGeneratorAction(map));
		actions.add(newAction);
		
		//this.actions.addAll(actions);
				
		firePropertyChange("actions", this.actions, this.actions.addAll(actions));
	}
		
	

}
