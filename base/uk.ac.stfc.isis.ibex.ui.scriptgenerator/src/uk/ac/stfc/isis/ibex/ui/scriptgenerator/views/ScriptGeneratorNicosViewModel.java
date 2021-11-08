package uk.ac.stfc.isis.ibex.ui.scriptgenerator.views;

import java.util.HashSet;

import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;

import uk.ac.stfc.isis.ibex.nicos.Nicos;
import uk.ac.stfc.isis.ibex.nicos.NicosErrorState;
import uk.ac.stfc.isis.ibex.nicos.NicosModel;
import uk.ac.stfc.isis.ibex.scriptgenerator.ScriptGeneratorSingleton;
import uk.ac.stfc.isis.ibex.scriptgenerator.dynamicscripting.DynamicScriptingModelFacade;
import uk.ac.stfc.isis.ibex.scriptgenerator.dynamicscripting.DynamicScriptingException;
import uk.ac.stfc.isis.ibex.scriptgenerator.dynamicscripting.DynamicScriptingManager;
import uk.ac.stfc.isis.ibex.scriptgenerator.dynamicscripting.DynamicScriptingNicosFacade;
import uk.ac.stfc.isis.ibex.scriptgenerator.dynamicscripting.StoppedState;

/**
 * A ViewModel to control the View in relation to elements from the nicos model.
 */
public class ScriptGeneratorNicosViewModel {
	
	private NicosModel nicosModel = Nicos.getDefault().getModel();
	private Button queueScriptButton;
	private DynamicScriptingManager dynamicScriptingManager;
	
	public ScriptGeneratorNicosViewModel(ScriptGeneratorSingleton scriptGeneratorModel) {
		var nicosFacade = new DynamicScriptingNicosFacade(nicosModel);
		var generatorFacade = new DynamicScriptingModelFacade(scriptGeneratorModel);
		var dynamicScriptingState = new StoppedState(nicosFacade, generatorFacade, new HashSet<Integer>());
		dynamicScriptingManager = new DynamicScriptingManager(dynamicScriptingState);
		scriptGeneratorModel.setDynamicScriptingManager(dynamicScriptingManager);
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
     */
    public void playScript() {
    	try {
			dynamicScriptingManager.playScript();
		} catch (DynamicScriptingException e) {
			MessageDialog.openError(Display.getDefault().getActiveShell(), "Dynamic scripting error", "Dynamic scripting error");
		}
    }

}
