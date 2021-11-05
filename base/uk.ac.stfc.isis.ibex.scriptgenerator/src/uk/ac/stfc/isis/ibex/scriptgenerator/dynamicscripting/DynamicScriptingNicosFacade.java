package uk.ac.stfc.isis.ibex.scriptgenerator.dynamicscripting;

import uk.ac.stfc.isis.ibex.model.ModelObject;
import uk.ac.stfc.isis.ibex.nicos.NicosErrorState;
import uk.ac.stfc.isis.ibex.nicos.NicosModel;
import uk.ac.stfc.isis.ibex.nicos.ScriptStatus;
import uk.ac.stfc.isis.ibex.nicos.messages.scriptstatus.QueuedScript;
import uk.ac.stfc.isis.ibex.scriptgenerator.table.ScriptGeneratorAction;

public class DynamicScriptingNicosFacade extends ModelObject {
	
	private NicosModel nicosModel;
	private ScriptStatus scriptStatus = ScriptStatus.IDLE;
	
	public DynamicScriptingNicosFacade(NicosModel nicosModel) {
		this.nicosModel = nicosModel;
	}
	
	public void executeAction(ScriptGeneratorAction action) throws DynamicScriptingException {
		if (!nicosInError()) {
			String name = "Script Generator: " + action;
			String code = "print(" + action + ")";
    		var scriptToSend = new QueuedScript();
    		scriptToSend.setName(name);
        	scriptToSend.setCode(code);
        	nicosModel.sendScript(scriptToSend);
        	setScriptStatus(ScriptStatus.RUNNING);
    	} else {
    		throw new DynamicScriptingException("Nicos in error, cannot play script.");
    	}
	}
	
	private boolean nicosInError() {
		var nicosError = nicosModel.getError();
		return nicosError != NicosErrorState.NO_ERROR && nicosError != NicosErrorState.SCRIPT_SEND_FAIL;
	}
	
	public void setScriptStatus(ScriptStatus scriptStatus) {
        firePropertyChange("scriptStatus", this.scriptStatus, this.scriptStatus = scriptStatus);
    }

}
