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
	private DynamicScriptName scriptName = new DynamicScriptName(Optional.empty());
	private Boolean executionStopped = false;
	private Boolean executionPaused = false;
	
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
        	executionStopped = false;
        	executionPaused = false;
    	} else {
    		throw new DynamicScriptingException("Nicos in error, cannot play script.");
    	}
	}
	
	private void sendInstruction(ExecutionInstructionType instruction) {
		ExecutionInstruction executionInstruction = new ExecutionInstruction(instruction, BreakLevel.IMMEDIATE);
		nicosModel.sendExecutionInstruction(executionInstruction);
	}
	
	public void stopExecution() {
		executionStopped = true;
		sendInstruction(ExecutionInstructionType.STOP);
	}
	
	public void pauseExecution() {
		if(!executionStopped) {
			executionPaused = true;
			sendInstruction(ExecutionInstructionType.BREAK);
		}
	}
	
	public void resumeExecution() {
		if (executionPaused) {
			executionPaused = false;
			sendInstruction(ExecutionInstructionType.CONTINUE);
		}
	}
	
	public void scriptChanged(DynamicScriptName newName) {
		scriptName.getStatus().ifPresentOrElse(name -> {
				scriptChangedGivenOldPresent(name, newName);
			}, () -> {
				scriptChangedGivenOldEmpty(newName);
			}
		);
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getPropertyName().equals(DynamicScriptingProperties.SCRIPT_STATUS_PROPERTY)) {
			Optional<String> newScriptName = Optional.ofNullable(nicosModel.getScriptName());
			DynamicScriptName newDynamicScriptName = new DynamicScriptName(newScriptName);
			scriptChanged(newDynamicScriptName);
			ScriptStatus newStatus = (ScriptStatus) evt.getNewValue();
			ScriptStatus oldStatus = (ScriptStatus) evt.getOldValue();
			if (scriptStopped(newStatus)) {
				firePropertyChange(DynamicScriptingProperties.SCRIPT_STOPPED_PROPERTY, oldStatus, newStatus);
			} else if (scriptPaused(newStatus)) {
				firePropertyChange(DynamicScriptingProperties.SCRIPT_PAUSED_PROPERTY, oldStatus, newStatus);
			} else if (scriptResumed(oldStatus, newStatus)) {
				firePropertyChange(DynamicScriptingProperties.SCRIPT_RESUMED_PROPERTY, oldStatus, newStatus);
			}
		}
	}
	
	private Boolean scriptStopped(ScriptStatus newStatus) {
		return (newStatus == ScriptStatus.IDLE || newStatus == ScriptStatus.IDLEEXC) && executionStopped;
	}
	
	private Boolean scriptPaused(ScriptStatus newStatus) {
		return newStatus == ScriptStatus.INBREAK && executionPaused;
	}
	
	private Boolean scriptResumed(ScriptStatus oldStatus, ScriptStatus newStatus) {
		return (oldStatus == ScriptStatus.INBREAK && newStatus == ScriptStatus.RUNNING) && !executionPaused;
	}
	
	private void fireScriptChange(DynamicScriptName newName) {
		firePropertyChange(DynamicScriptingProperties.SCRIPT_CHANGED_PROPERTY, scriptName, scriptName = newName);
	}
	
	private void scriptChangedGivenOldPresent(String oldScriptName, DynamicScriptName newScriptName) {
		newScriptName.getStatus().ifPresentOrElse(newName -> {
			if (!oldScriptName.equals(newName)) {
				fireScriptChange(newScriptName);
			}
		}, () -> {
			fireScriptChange(newScriptName);
		});
	}
	
	private void scriptChangedGivenOldEmpty(DynamicScriptName newScriptName) {
		newScriptName.getStatus().ifPresent(newName -> {
			fireScriptChange(newScriptName);
		});
	}
	
	private boolean nicosInError() {
		var nicosError = nicosModel.getError();
		return nicosError != NicosErrorState.NO_ERROR;
	}

}
