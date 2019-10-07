package uk.ac.stfc.isis.ibex.scriptgenerator.table;

import java.util.HashMap;

import uk.ac.stfc.isis.ibex.model.ModelObject;

public class ScriptGeneratorAction extends ModelObject {

	private HashMap<String, String > actionParameterValues;

	public ScriptGeneratorAction(HashMap<String, String> values) {
		this.actionParameterValues = values;
	}
	
	public void setActionParameterValue(String key, String value) {
		String oldValue = actionParameterValues.get(key); 
		actionParameterValues.replace(key, value);
		firePropertyChange(key, oldValue, value);
	}
	
	public String getActionParameterValue(String parameterName) {
		return actionParameterValues.get(parameterName);		
	}
	
}
