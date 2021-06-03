package uk.ac.stfc.isis.ibex.ui.scriptgenerator.views;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;

import uk.ac.stfc.isis.ibex.nicos.Nicos;
import uk.ac.stfc.isis.ibex.nicos.NicosErrorState;
import uk.ac.stfc.isis.ibex.nicos.NicosModel;
import uk.ac.stfc.isis.ibex.nicos.messages.scriptstatus.QueuedScript;

public class ScriptGeneratorNicosViewModel {
	
	private NicosModel nicosModel = Nicos.getDefault().getModel();
	private Button queueScriptButton;
	
	public void bindQueueScriptButton(Button queueScriptButton) {
		this.queueScriptButton = queueScriptButton;
		nicosModel.addPropertyChangeListener(e -> updateButtonEnablement());
		updateButtonEnablement();
	}
	
	private boolean nicosInError() {
		var nicosError = nicosModel.getError();
		return nicosError != NicosErrorState.NO_ERROR && nicosError != NicosErrorState.SCRIPT_SEND_FAIL;
	}
	
	private void updateButtonEnablement() {
		Display.getDefault().asyncExec(() -> {
			queueScriptButton.setEnabled(!nicosInError());
		});
	}
	
	/**
     * Queue the current script generator contents as a script in nicos.
     */
    public void queueScript() {
    	if (!nicosInError()) {
    		var scriptToSend = new QueuedScript();
        	scriptToSend.setCode("# James Script\nprint(\"James Script\")");
        	nicosModel.sendScript(scriptToSend);
    	} else {
    		var display = Display.getDefault();
    		display.asyncExec(() -> {
    			MessageDialog.openWarning(display.getActiveShell(), "Error", "Failed to queue script");
    		});
    	}
    	
    }

}
