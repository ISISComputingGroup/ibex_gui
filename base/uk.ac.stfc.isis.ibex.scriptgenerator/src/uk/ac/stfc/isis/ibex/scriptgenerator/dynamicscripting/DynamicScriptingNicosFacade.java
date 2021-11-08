package uk.ac.stfc.isis.ibex.scriptgenerator.dynamicscripting;

import java.util.Optional;

import uk.ac.stfc.isis.ibex.model.ModelObject;
import uk.ac.stfc.isis.ibex.nicos.NicosErrorState;
import uk.ac.stfc.isis.ibex.nicos.NicosModel;
import uk.ac.stfc.isis.ibex.nicos.messages.scriptstatus.QueuedScript;

public class DynamicScriptingNicosFacade extends ModelObject {
	
	private NicosModel nicosModel;
	private Optional<String> scriptName = Optional.empty();
	
	
	public DynamicScriptingNicosFacade(NicosModel nicosModel) {
		this.nicosModel = nicosModel;
		this.nicosModel.addPropertyChangeListener(DynamicScriptingProperties.CURRENTLY_EXECUTING_SCRIPT, evt -> {
			setScriptName(this.nicosModel.getScriptName());
		});
		scriptIdle();
	}
	
	public void executeAction(String name, String code) throws DynamicScriptingException {
		if (!nicosInError()) {
    		var scriptToSend = new QueuedScript();
    		scriptToSend.setName(name);
        	scriptToSend.setCode(code);
        	nicosModel.sendScript(scriptToSend);
        	if (nicosInError()) {
        		scriptError();
    			throw new DynamicScriptingException("Nicos in error, cannot play script.");
    		}
        	scriptRunning();
    	} else {
    		scriptError();
    		throw new DynamicScriptingException("Nicos in error, cannot play script.");
    	}
	}
	
	private boolean nicosInError() {
		var nicosError = nicosModel.getError();
		return nicosError != NicosErrorState.NO_ERROR;
	}
	
	private void scriptRunning() {
		firePropertyChange(DynamicScriptingProperties.SCRIPT_CHANGED_PROPERTY, false, true);
	}
	
	private void scriptError() {
		firePropertyChange(DynamicScriptingProperties.SCRIPT_CHANGED_PROPERTY, true, false);
	}
	
	private void scriptIdle() {
		firePropertyChange(DynamicScriptingProperties.SCRIPT_CHANGED_PROPERTY, true, false);
	}
	
	public void setScriptName(String newName) {
		if (scriptName.isPresent()) {
			var oldName = scriptName.get();
			if (oldName != newName) {
				firePropertyChange(DynamicScriptingProperties.SCRIPT_CHANGED_PROPERTY, false, true);
			}
			this.scriptName = Optional.of(newName);
		} else {
			this.scriptName = Optional.of(newName);
		}
	}

}
