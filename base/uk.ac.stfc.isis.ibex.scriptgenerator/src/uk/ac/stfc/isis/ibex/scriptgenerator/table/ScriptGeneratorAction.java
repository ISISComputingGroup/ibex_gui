package uk.ac.stfc.isis.ibex.scriptgenerator.table;

import java.util.HashMap;
import java.util.Map;


import uk.ac.stfc.isis.ibex.model.ModelObject;

public class ScriptGeneratorAction extends ModelObject {

	private HashMap<String, String > actionParameterValues;

	public ScriptGeneratorAction(HashMap<String, String> values) {
		this.actionParameterValues = values;
	}
	
	public ScriptGeneratorAction(ScriptGeneratorAction actionToCopy) {
		this.actionParameterValues = new HashMap<String, String>();
		// Add all Parameter/value pairs to the hash map
		for (Map.Entry<String, String> entry: actionToCopy.getAllActionParameters().entrySet()) {
			setActionParameterValue(entry.getKey(), entry.getValue());
		}
	}
	
	public void setActionParameterValue(String key, String value) {
		String oldValue = actionParameterValues.get(key); 
		actionParameterValues.put(key, value);
		firePropertyChange(key, oldValue, value);
	}
	
	public String getActionParameterValue(String parameterName) {
		return actionParameterValues.get(parameterName);		
	}
	
	public HashMap<String, String> getAllActionParameters(){
		return actionParameterValues;
		// is it appropriate to get the action parameters map?
	}
	
}
