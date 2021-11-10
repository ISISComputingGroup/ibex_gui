package uk.ac.stfc.isis.ibex.ui.scriptgenerator.views;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;

import uk.ac.stfc.isis.ibex.nicos.Nicos;
import uk.ac.stfc.isis.ibex.nicos.NicosErrorState;
import uk.ac.stfc.isis.ibex.nicos.NicosModel;
import uk.ac.stfc.isis.ibex.scriptgenerator.ScriptGeneratorSingleton;
import uk.ac.stfc.isis.ibex.scriptgenerator.dynamicscripting.DynamicScriptingModelAdapter;
import uk.ac.stfc.isis.ibex.scriptgenerator.dynamicscripting.DynamicScriptingException;
import uk.ac.stfc.isis.ibex.scriptgenerator.dynamicscripting.DynamicScriptingManager;
import uk.ac.stfc.isis.ibex.scriptgenerator.dynamicscripting.DynamicScriptingNicosAdapter;
import uk.ac.stfc.isis.ibex.scriptgenerator.dynamicscripting.DynamicScriptingStateFactory;
import uk.ac.stfc.isis.ibex.scriptgenerator.dynamicscripting.StoppedState;
import uk.ac.stfc.isis.ibex.scriptgenerator.table.ScriptGeneratorAction;

/**
 * A ViewModel to control the View in relation to elements from the nicos model.
 */
public class ScriptGeneratorNicosViewModel {
	
	private NicosModel nicosModel = Nicos.getDefault().getModel();
	private List<Button> queueScriptButtons = new ArrayList<>();
	private DynamicScriptingManager dynamicScriptingManager;
	
	public ScriptGeneratorNicosViewModel(ScriptGeneratorSingleton scriptGeneratorModel) {
		var nicosAdapter = new DynamicScriptingNicosAdapter(nicosModel);
		nicosModel.addPropertyChangeListener(nicosAdapter);
		var modelAdapter = new DynamicScriptingModelAdapter(scriptGeneratorModel);
		scriptGeneratorModel.addPropertyChangeListener(modelAdapter);
		var dynamicScriptingState = new StoppedState(new HashMap<Integer, ScriptGeneratorAction>());
		var dynamicScriptingStateFactory = new DynamicScriptingStateFactory(modelAdapter, nicosAdapter, dynamicScriptingState);
		dynamicScriptingManager = new DynamicScriptingManager(dynamicScriptingStateFactory);
		scriptGeneratorModel.setDynamicScriptingManager(dynamicScriptingManager);
	}
	
	/**
	 * Bind the given button to a property that enables and disables it.
	 * 
	 * @param queueScriptButton The button to enable/disable.
	 */
	public void bindQueueScriptButton(Button queueScriptButton) {
		this.queueScriptButtons.add(queueScriptButton);
		nicosModel.addPropertyChangeListener(e -> updateButtonEnablement());
		updateButtonEnablement();
	}
	
	private boolean nicosInError() {
		var nicosError = nicosModel.getError();
		return nicosError != NicosErrorState.NO_ERROR && nicosError != NicosErrorState.SCRIPT_SEND_FAIL;
	}
	
	private void updateButtonEnablement() {
		Display.getDefault().asyncExec(() -> {
			for (Button button : queueScriptButtons) {
				button.setEnabled(!nicosInError());
			}
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
    
    public void stopScript() {
		dynamicScriptingManager.stopScript();
    }
    
    public void pauseScript() {
		dynamicScriptingManager.pauseScript();
    }

}
