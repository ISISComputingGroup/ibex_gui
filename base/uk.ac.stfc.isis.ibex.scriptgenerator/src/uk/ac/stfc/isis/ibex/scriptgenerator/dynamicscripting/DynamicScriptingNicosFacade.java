package uk.ac.stfc.isis.ibex.scriptgenerator.dynamicscripting;

import uk.ac.stfc.isis.ibex.model.ModelObject;
import uk.ac.stfc.isis.ibex.nicos.NicosErrorState;
import uk.ac.stfc.isis.ibex.nicos.NicosModel;
import uk.ac.stfc.isis.ibex.nicos.ScriptStatus;
import uk.ac.stfc.isis.ibex.nicos.messages.scriptstatus.QueuedScript;

public class DynamicScriptingNicosFacade extends ModelObject {
	
	private NicosModel nicosModel;
	private ScriptStatus scriptStatus = ScriptStatus.IDLE;
	
	public DynamicScriptingNicosFacade(NicosModel nicosModel) {
		this.nicosModel = nicosModel;
		setScriptStatus(this.nicosModel.getScriptStatus());
	}
	
	public void executeAction(String name, String code) throws DynamicScriptingException {
		if (!nicosInError()) {
    		var scriptToSend = new QueuedScript();
    		scriptToSend.setName(name);
        	scriptToSend.setCode(code);
        	nicosModel.sendScript(scriptToSend);
        	if (nicosInError()) {
        		setScriptStatus(ScriptStatus.IDLEEXC);
    			throw new DynamicScriptingException("Nicos in error, cannot play script.");
    		}
        	setScriptStatus(ScriptStatus.RUNNING);
    	} else {
    		setScriptStatus(ScriptStatus.IDLEEXC);
    		throw new DynamicScriptingException("Nicos in error, cannot play script.");
    	}
	}
	
	private boolean nicosInError() {
		var nicosError = nicosModel.getError();
		return nicosError != NicosErrorState.NO_ERROR;
	}
	
	public void setScriptStatus(ScriptStatus scriptStatus) {
        firePropertyChange("scriptStatus", this.scriptStatus, this.scriptStatus = scriptStatus);
    }
	
	public ScriptStatus getScriptStatus() {
		return scriptStatus;
	}

}
