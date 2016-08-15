package uk.ac.stfc.isis.ibex.scriptgenerator;

import uk.ac.stfc.isis.ibex.model.ModelObject;

public class PythonBuilder extends ModelObject {
	private String name;
	private int temperature;
	private boolean wait;
	private String script;
	
	public PythonBuilder() {
	}

	public void setName(String name) {
		//updateScript()
	}
	
	public void setTemperature(int temperature) {
		//updateScript()
	}
	
	public void setWait(boolean wait) {
		//updateScript()
	}
	
	public void updateScript() {
		script = name + " " + temperature + " " + wait;
		firePropertyChange("script", this.script, this.script = script);
	}
	
	public String getScript() {
		return script;
	}

}
