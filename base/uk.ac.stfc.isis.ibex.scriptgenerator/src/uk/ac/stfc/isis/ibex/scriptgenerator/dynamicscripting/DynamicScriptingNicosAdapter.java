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

public class DynamicScriptingNicosAdapter extends ModelObject implements PropertyChangeListener {
	
	private NicosModel nicosModel;
	private DynamicScriptName lastScriptName = new DynamicScriptName(Optional.empty());
	
	public DynamicScriptingNicosAdapter(NicosModel nicosModel) {
		this.nicosModel = nicosModel;
		this.nicosModel.addPropertyChangeListener(this);
	}
	
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
	
	private void sendInstruction(ExecutionInstructionType instruction, BreakLevel level) {
		ExecutionInstruction executionInstruction = new ExecutionInstruction(instruction, level);
		nicosModel.sendExecutionInstruction(executionInstruction);
	}
	
	public void stopExecution() {
		sendInstruction(ExecutionInstructionType.STOP, BreakLevel.IMMEDIATE);
	}
	
	public void pauseExecution() {
		sendInstruction(ExecutionInstructionType.BREAK, BreakLevel.IMMEDIATE);
	}
	
	public void resumeExecution() {
		sendInstruction(ExecutionInstructionType.CONTINUE, null);
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getPropertyName().equals(DynamicScriptingProperties.SCRIPT_STATUS_PROPERTY)) {
			ScriptStatus newStatus = (ScriptStatus) evt.getNewValue();
			ScriptStatus oldStatus = (ScriptStatus) evt.getOldValue();
			if (scriptFinished(newStatus)) {
				String scriptName = nicosModel.getScriptName();
				DynamicScriptName newScriptName = new DynamicScriptName(Optional.ofNullable(scriptName));
				if (!lastScriptName.equals(newScriptName)) {
					System.out.println(lastScriptName);
					System.out.println(newScriptName);
					firePropertyChange(DynamicScriptingProperties.SCRIPT_FINISHED_PROPERTY, lastScriptName, lastScriptName = newScriptName);
				}
			}
		}
	}
	
	private Boolean scriptFinished(ScriptStatus newStatus) {
		return newStatus.isIdle();
	}
	
	private boolean nicosInError() {
		var nicosError = nicosModel.getError();
		return nicosError != NicosErrorState.NO_ERROR;
	}

}
