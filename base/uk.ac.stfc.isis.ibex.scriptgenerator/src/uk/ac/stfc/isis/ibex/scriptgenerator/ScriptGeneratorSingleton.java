package uk.ac.stfc.isis.ibex.scriptgenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import uk.ac.stfc.isis.ibex.model.ModelObject;
import uk.ac.stfc.isis.ibex.scriptgenerator.pythoninterface.ConfigLoader;
import uk.ac.stfc.isis.ibex.scriptgenerator.table.ActionsTable;

public class ScriptGeneratorSingleton extends ModelObject {
	private Integer iteratedNumber = 0;
	private String iteratedNumberString;
	
	private ActionsTable scriptGeneratorTable = new ActionsTable(new ArrayList<ActionParameter>());
	
	public ScriptGeneratorSingleton() {
		reloadConfig();
	}
	
	/**
	 * Reloads the configuration
	 * 
	 * @return Integer of the updated model value
	 */
	public void reloadConfig() {
		ConfigLoader config = new ConfigLoader();
		setActionParameters(config.getActionParameters().collect(Collectors.toCollection(ArrayList::new)));
	}
	
	public void setActionParameters(ArrayList<ActionParameter> actionParameters) {
		var newParameters = new ArrayList<ActionParameter>();
		for (ActionParameter actionParameter:actionParameters) {
			newParameters.add(actionParameter);
		}
		scriptGeneratorTable.setActionParameters(newParameters);
	}
	
	public ActionsTable getScriptGeneratorTable() {
		return scriptGeneratorTable;
	}
	
	
	public void addEmptyAction() {
		scriptGeneratorTable.addEmptyAction(scriptGeneratorTable.getActionParameters());
	}
	
	public void deleteAction(int index) {
		scriptGeneratorTable.deleteAction(index);
	}
	
	public void duplicateAction(int index) {
		scriptGeneratorTable.duplicateAction(index);
	}
	
	public void moveActionUp(int index) {
		scriptGeneratorTable.moveAction(index, index-1);
	}

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
