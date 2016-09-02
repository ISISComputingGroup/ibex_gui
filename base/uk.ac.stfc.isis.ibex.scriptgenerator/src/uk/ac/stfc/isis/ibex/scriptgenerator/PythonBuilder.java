package uk.ac.stfc.isis.ibex.scriptgenerator;

import java.util.Collection;

import uk.ac.stfc.isis.ibex.model.ModelObject;

public class PythonBuilder extends ModelObject {
	private int position;
	private int trans;
	private int transWait;
	private int sans;
	private int sansWait;
	private int period;
	private String sampleName; 
	private int thickness; 
	private Collection<ScriptGeneratorRow> rows;
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
	
	public void setSans(int sans) {
		this.sans = sans;
		updateScript();
	}
	
	public void setSansWait(int sansWait) {
		this.sansWait = sansWait;
		updateScript();
	}
	
	public void setPeriod(int period) {
		this.period = period;
		updateScript();
	}
	
	public void setSampleName(String sampleName) {
		this.sampleName = sampleName;
		updateScript();
	}
	
	public void setThickness(int thickness) {
		this.thickness = thickness;
		updateScript();
	}
	
	public void setRows(Collection<ScriptGeneratorRow> rows) {
		this.rows = rows;
		
		createScript();
	}
	
	public void updateScript() {
		String tempScript = position + " " + trans + " " + transWait;
		firePropertyChange("script", this.script, this.script = tempScript);
	}
	
	public String getScript() {
		return script;
	}
	
	public void createScript() {
		script = new String();
		
		for (ScriptGeneratorRow row : rows) {
			script += "position = " + row.position;
		}
	}
}
