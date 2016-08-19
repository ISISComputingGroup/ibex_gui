package uk.ac.stfc.isis.ibex.scriptgenerator;

import uk.ac.stfc.isis.ibex.model.ModelObject;

public class PythonBuilder extends ModelObject {
	private int position;
	private int trans;
	private int transWait;
	private String script;
	
	public PythonBuilder() {
	}
	
//	private void generateHeader() {
//		
//	}
//	
//	private void setSamplePar(String name, String value) {
//		String s = String.format("    set_sample_par(%s, %s)", name, value);
//	}

	public void setPosition(int position) {
		this.position = position;
		updateScript();
	}
	
	public void setTrans(int trans) {
		this.trans = trans;
		updateScript();
	}
	
	public void setTransWait(int transWait) {
		this.transWait = transWait;
		updateScript();
	}
	
	public void updateScript() {
		String tempScript = position + " " + trans + " " + transWait;
		firePropertyChange("script", this.script, this.script = tempScript);
	}
	
	public String getScript() {
		return script;
	}

}
