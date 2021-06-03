package uk.ac.stfc.isis.ibex.ui.scriptgenerator.views;

import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;

import uk.ac.stfc.isis.ibex.nicos.Nicos;
import uk.ac.stfc.isis.ibex.nicos.NicosErrorState;
import uk.ac.stfc.isis.ibex.nicos.NicosModel;

public class ScriptGeneratorNicosViewModel {
	
	private NicosModel nicosModel = Nicos.getDefault().getModel();
	private Button queueScriptButton;
	
	public void bindQueueScriptButton(Button queueScriptButton) {
		this.queueScriptButton = queueScriptButton;
		nicosModel.addPropertyChangeListener(e -> updateButtonEnablement());
		updateButtonEnablement();
	}
	
	private void updateButtonEnablement() {
		Display.getDefault().asyncExec(() -> {
			var nicosError = nicosModel.getError();
			if (nicosError == NicosErrorState.NO_ERROR || nicosError == NicosErrorState.SCRIPT_SEND_FAIL) {
				queueScriptButton.setEnabled(true);
			} else {
				queueScriptButton.setEnabled(false);
			}
		});
	}

}
