package uk.ac.stfc.isis.ibex.scriptgenerator.dynamicscripting;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Optional;

import uk.ac.stfc.isis.ibex.model.ModelObject;
import uk.ac.stfc.isis.ibex.nicos.BreakLevel;
import uk.ac.stfc.isis.ibex.nicos.ExecutionInstructionType;
import uk.ac.stfc.isis.ibex.nicos.NicosErrorState;
import uk.ac.stfc.isis.ibex.nicos.NicosModel;
import uk.ac.stfc.isis.ibex.nicos.ScriptStatus;
import uk.ac.stfc.isis.ibex.nicos.messages.ExecutionInstruction;
import uk.ac.stfc.isis.ibex.nicos.messages.scriptstatus.QueuedScript;

/**
 * An adapter for dynamic scripting to use when interacting with nicos/the script server.
 */
public class DynamicScriptingNicosAdapter extends ModelObject implements PropertyChangeListener {
	
	private NicosModel nicosModel;
	private DynamicScriptName lastScriptName = new DynamicScriptName(Optional.empty());
	
	/**
	 * Create an adapter with the nicos model it depends on injected.
	 *
	 * @param nicosModel The injected nicos dependency.
	 */
	public DynamicScriptingNicosAdapter(NicosModel nicosModel) {
		this.nicosModel = nicosModel;
		this.nicosModel.addPropertyChangeListener(this);
	}
	
	/**
	 * Execute the action that has been generated with the given name and code to run.
	 *
	 * @param name The name of the script to execute.
	 * @param code The code to execute.
	 */
	public void executeAction(String name, String code) throws DynamicScriptingException {
		if (!nicosInError()) {
    		var scriptToSend = new QueuedScript();
    		scriptToSend.setName(name);
        	scriptToSend.setCode(code);
        	nicosModel.sendScript(scriptToSend);
        	if (nicosInError()) {
    			throw new DynamicScriptingException("Nicos in error, cannot play script.");
    		}
    	} else {
    		throw new DynamicScriptingException("Nicos in error, cannot play script.");
    	}
	}
	
	/**
	 * Send a stop instruction to nicos.
	 */
	public void stopExecution() {
		sendInstruction(ExecutionInstructionType.STOP, BreakLevel.IMMEDIATE);
	}
	
	/**
	 * Send a stop instruction to nicos.
	 */
	public void pauseExecution() {
		sendInstruction(ExecutionInstructionType.BREAK, BreakLevel.IMMEDIATE);
	}
	
	/**
	 * Send a resume instruction to nicos.
	 */
	public void resumeExecution() {
		sendInstruction(ExecutionInstructionType.CONTINUE, null);
	}

	private void sendInstruction(ExecutionInstructionType instruction, BreakLevel level) {
		ExecutionInstruction executionInstruction = new ExecutionInstruction(instruction, level);
		nicosModel.sendExecutionInstruction(executionInstruction);
	}

	/**
	 * Detect when a script has finished so nicos is ready for a new action.
	 * {@inheritdoc}
	 */
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		String propertyName = evt.getPropertyName();
		ScriptStatus newStatus = nicosModel.getScriptStatus();
		if (isPossibleEndOfScriptPropertyName(propertyName) && newStatus.isIdle()) {
			String scriptName = nicosModel.getScriptName();
			scriptChanged(scriptName);
		}
	}
	
	/**
	 * Indicate that the script has finished so nicos is ready for a new one by firing a property change.
	 *
	 * @param scriptName The script name of the script that has just finished.
	 */
	public void scriptChanged(String scriptName) {
		DynamicScriptName newScriptName = new DynamicScriptName(Optional.ofNullable(scriptName));
		if (!lastScriptName.equals(newScriptName)) {
			firePropertyChange(DynamicScriptingProperties.SCRIPT_FINISHED_PROPERTY, lastScriptName, lastScriptName = newScriptName);
		}
	}
	
	private Boolean isPossibleEndOfScriptPropertyName(String propertyName) {
		return propertyName.equals(DynamicScriptingProperties.SCRIPT_STATUS_PROPERTY) 
				|| propertyName.equals(DynamicScriptingProperties.CURRENTLY_EXECUTING_SCRIPT_PROPERTY)
				|| propertyName.equals(DynamicScriptingProperties.SCRIPT_NAME_PROPERTY);
	}

	private boolean nicosInError() {
		var nicosError = nicosModel.getError();
		return nicosError != NicosErrorState.NO_ERROR;
	}

}
