package uk.ac.stfc.isis.ibex.scriptgenerator;

import java.util.ArrayList;
import java.util.List;

import uk.ac.stfc.isis.ibex.model.ModelObject;
import uk.ac.stfc.isis.ibex.scriptgenerator.table.ActionsTable;

/**
 * Acts as a permanent reference to the ActionsTable
 *
 */
public class ScriptGeneratorSingleton extends ModelObject {
	private Integer iteratedNumber = 0;
	private String iteratedNumberString;
	
	private ActionsTable scriptGeneratorTable = new ActionsTable(new ArrayList<ActionParameter>());

	/**
	 *  Adds the new action parameters to the empty ActionsTable
	 * @param actionParameters
	 */
	public void setActionParameters(ArrayList<ActionParameter> actionParameters) {
		// Setting new action parameters drops the whole table
		//this.scriptGeneratorTable = new ActionsTable(new ArrayList<ActionParameter>());
		var newParameters = new ArrayList<ActionParameter>();
		for (ActionParameter actionParameter:actionParameters) {
			newParameters.add(actionParameter);
		}
		scriptGeneratorTable.setActionParameters(newParameters);
	}
	
	/**
	 * @return ScriptGeneratorTable
	 */
	public ActionsTable getScriptGeneratorTable() {		
		return scriptGeneratorTable;
	}
	
	/**
	 * Adds a new action (row) to the ActionsTable, with default parameter values
	 */
	public void addEmptyAction() {
		scriptGeneratorTable.addEmptyAction(scriptGeneratorTable.getActionParameters());
	}
	
	/**
	 * Removes action at position index from ActionsTable
	 * @param index
	 */
	public void deleteAction(int index) {
		
		scriptGeneratorTable.deleteAction(index);
	}
	
	/**
	 * Duplicates action at position index in ActionsTable
	 * @param index
	 */
	public void duplicateAction(int index) {
		scriptGeneratorTable.duplicateAction(index);
	}
	
	/**
	 * Moves action one row up in table
	 * @param index
	 */
	public void moveActionUp(int index) {
		scriptGeneratorTable.moveAction(index, index-1);
	}

	/**
	 * Moves action one row down in table
	 * @param index
	 */
	public void moveActionDown(int index) {
		scriptGeneratorTable.moveAction(index, index+1);
	}
	
	/**
	 * Adds one to the toy model's value.
	 * Updates string representation
	 * 
	 * @return Integer of the updated model value
	 */
	public int iterateNumber() {

		iteratedNumber++;
		this.scriptGeneratorTable.getActions().get(0).setActionParameterValue("code", iteratedNumber.toString());
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
