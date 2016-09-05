package uk.ac.stfc.isis.ibex.scriptgenerator;

import java.util.Collection;

import uk.ac.stfc.isis.ibex.model.ModelObject;

/**
 * Generates Python code based on values in a ScriptGeneratorTable row.
 */
public class PythonBuilder extends ModelObject {
	private int doSans = 1;
	private int doTrans = 1;
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

	public void setDoSans(int doSans) {
		this.doSans = doSans;
	}
	
	public void setRows(Collection<ScriptGeneratorRow> rows) {
		this.rows = rows;
		
		createScript();
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
