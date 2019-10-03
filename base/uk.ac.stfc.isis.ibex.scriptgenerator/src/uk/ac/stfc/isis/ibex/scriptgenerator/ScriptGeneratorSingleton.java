package uk.ac.stfc.isis.ibex.scriptgenerator;

import java.util.ArrayList;
import java.util.List;

import uk.ac.stfc.isis.ibex.model.ModelObject;
import uk.ac.stfc.isis.ibex.scriptgenerator.table.ScriptGeneratorTable;

public class ScriptGeneratorSingleton extends ModelObject {
	private Integer iteratedNumber = 0;
	private String iteratedNumberString;
	
	private ScriptGeneratorTable scriptGeneratorTable;
	
	public ScriptGeneratorSingleton() {
		List<ColumnDescription> columnDescriptions = new ArrayList<ColumnDescription>();
		
		columnDescriptions.add(new ColumnDescription("ColumnName"));
		columnDescriptions.add(new ColumnDescription("COde is cool"));
		
		scriptGeneratorTable = new ScriptGeneratorTable(columnDescriptions);
		
	}
	
	public ScriptGeneratorTable getScriptGeneratorTable() {
		return scriptGeneratorTable;
	}
	
	
	/**
	 * Adds one to the toy model's value.
	 * Updates string representation
	 * 
	 * @return Integer of the updated model value
	 */
	public int iterateNumber() {

		
		iteratedNumber++;
		setIteratedNumber(iteratedNumber.toString());
		return iteratedNumber;
	}
	
	/**
	 * Returns the model's value as a string.
	 * 
	 * @return String representation of the model's value
	 */
	public String getIteratedNumber() {

		return iteratedNumberString;
	}
	
	/**
	 * Updates the string representation of the model's value.
	 * 
	 * @param newValue
	 *         String representation of the toy model's current value
	 */
	protected synchronized void setIteratedNumber(String newValue) {

		firePropertyChange("iteratedNumber", this.iteratedNumberString, this.iteratedNumberString = newValue);
	}
}
