package uk.ac.stfc.isis.ibex.scriptgenerator.table;

import java.util.HashMap;

import uk.ac.stfc.isis.ibex.model.ModelObject;

public class ScriptGeneratorAction extends ModelObject {

	private String data;
	private HashMap<String, String > values;

	public ScriptGeneratorAction(HashMap<String, String> values) {
		this.values = values;
	}
	
	public void setValue(String key, String value) {
		values.replace(key, value);
	}
	
	public String getData(String parameterName) {
		return values.get(parameterName);
		
	}
	
}
