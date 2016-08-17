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
		this.name = name;
		updateScript();
	}
	
	public void setTemperature(int temperature) {
		this.temperature = temperature;
		updateScript();
	}
	
	public void setWait(boolean wait) {
		this.wait = wait;
		updateScript();
	}
	
	public void updateScript() {
		String tempScript = name + " " + temperature + " " + wait;
		firePropertyChange("script", this.script, this.script = tempScript);
	}
	
	public String getScript() {
		return script;
	}

}
