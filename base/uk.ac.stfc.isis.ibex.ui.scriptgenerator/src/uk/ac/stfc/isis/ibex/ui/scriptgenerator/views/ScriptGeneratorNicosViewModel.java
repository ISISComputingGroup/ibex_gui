package uk.ac.stfc.isis.ibex.ui.scriptgenerator.views;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;

import uk.ac.stfc.isis.ibex.nicos.Nicos;
import uk.ac.stfc.isis.ibex.nicos.NicosErrorState;
import uk.ac.stfc.isis.ibex.nicos.NicosModel;
import uk.ac.stfc.isis.ibex.nicos.messages.scriptstatus.QueuedScript;
import uk.ac.stfc.isis.ibex.scriptgenerator.ScriptGeneratorSingleton;

/**
 * A ViewModel to control the View in relation to elements from the nicos model.
 */
public class ScriptGeneratorNicosViewModel {
	
	private NicosModel nicosModel = Nicos.getDefault().getModel();
	private Button queueScriptButton;
	private ScriptGeneratorSingleton scriptGeneratorModel;
	
	public ScriptGeneratorNicosViewModel(ScriptGeneratorSingleton scriptGeneratorModel) {
		this.scriptGeneratorModel = scriptGeneratorModel;
	}
	
	/**
	 * Bind the given button to a property that enables and disables it.
	 * 
	 * @param queueScriptButton The button to enable/disable.
	 */
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
     * 
     * @param name The name of the script to queue.
     * @param code The code for the script to send.
     */
    public void queueScript(String name, String code) {
    	if (!nicosInError()) {
    		var scriptToSend = new QueuedScript();
    		scriptToSend.setName(name);
        	scriptToSend.setCode(code);
        	nicosModel.sendScript(scriptToSend);
    	} else {
    		var display = Display.getDefault();
    		display.asyncExec(() -> {
    			MessageDialog.openWarning(display.getActiveShell(), "Error", "Failed to queue script");
    		});
    	}
    	
    }

}
