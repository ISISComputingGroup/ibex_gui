package uk.ac.stfc.isis.ibex.ui.scriptgenerator.views;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;

import uk.ac.stfc.isis.ibex.nicos.Nicos;
import uk.ac.stfc.isis.ibex.nicos.NicosErrorState;
import uk.ac.stfc.isis.ibex.nicos.NicosModel;
import uk.ac.stfc.isis.ibex.scriptgenerator.ScriptGeneratorSingleton;
import uk.ac.stfc.isis.ibex.scriptgenerator.dynamicscripting.DynamicScriptingModelAdapter;
import uk.ac.stfc.isis.ibex.scriptgenerator.dynamicscripting.DynamicScriptingException;
import uk.ac.stfc.isis.ibex.scriptgenerator.dynamicscripting.DynamicScriptingManager;
import uk.ac.stfc.isis.ibex.scriptgenerator.dynamicscripting.DynamicScriptingNicosAdapter;
import uk.ac.stfc.isis.ibex.scriptgenerator.dynamicscripting.DynamicScriptingProperties;
import uk.ac.stfc.isis.ibex.scriptgenerator.dynamicscripting.DynamicScriptingStateFactory;
import uk.ac.stfc.isis.ibex.scriptgenerator.dynamicscripting.DynamicScriptingStatus;
import uk.ac.stfc.isis.ibex.scriptgenerator.dynamicscripting.StoppedState;
import uk.ac.stfc.isis.ibex.scriptgenerator.table.ScriptGeneratorAction;

/**
 * A ViewModel to control the View in relation to elements from the nicos model.
 */
public class ScriptGeneratorNicosViewModel implements PropertyChangeListener {
	
	private NicosModel nicosModel = Nicos.getDefault().getModel();
	private DynamicScriptingManager dynamicScriptingManager;
	private Button runButton;
	private Button pauseButton;
	private Button stopButton;
	private static final Color GREEN = Display.getCurrent().getSystemColor(SWT.COLOR_GREEN);
	private static final Color DEFAULT_COLOR = null;
	private static final String RUN_BUTTON_TEXT = "Run";
	private static final String RESUME_BUTTON_TEXT = "Resume";
	
	/**
	 *  Constructor to create a view model using a scriptgeneratorsingleton as the model.
	 * @param scriptGeneratorModel - The script generator singleton to be used as the model.
	 */
	public ScriptGeneratorNicosViewModel(ScriptGeneratorSingleton scriptGeneratorModel) {
		var nicosAdapter = new DynamicScriptingNicosAdapter(nicosModel);
		nicosModel.addPropertyChangeListener(nicosAdapter);
		var modelAdapter = new DynamicScriptingModelAdapter(scriptGeneratorModel);
		scriptGeneratorModel.addPropertyChangeListener(modelAdapter);
		var dynamicScriptingState = new StoppedState(new HashMap<Integer, ScriptGeneratorAction>());
		var dynamicScriptingStateFactory = new DynamicScriptingStateFactory(modelAdapter, nicosAdapter, dynamicScriptingState);
		dynamicScriptingManager = new DynamicScriptingManager(dynamicScriptingStateFactory);
		scriptGeneratorModel.setDynamicScriptingManager(dynamicScriptingManager);
		dynamicScriptingManager.addPropertyChangeListener(this);
	}
		
	/**
	 * Bind the run/resume, pause and stop buttons,
	 *  so we can change the display depending on the state of dynamic scripting.
	 * 
	 * @param runButton The run/resume button to bind.
	 * @param pauseButton The pause button to bind.
	 * @param stopButton The stop button to bind.
	 */
	public void bindControls(Button runButton, Button pauseButton, Button stopButton) {
		bindRunButton(runButton);
		bindPauseButton(pauseButton);
		bindStopButton(stopButton);
		formatButtonsBasedOnStatus(dynamicScriptingManager.getDynamicScriptingStatus());
		nicosModel.addUiThreadPropertyChangeListener(e -> updateButtonEnablement());
	}
	
	/**
	 * Change the state of the run/resume, pause and stop buttons based on the status of the dynamic scripting.
	 */
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		var propName = evt.getPropertyName();
		if (propName.equals(DynamicScriptingProperties.STATE_CHANGE_PROPERTY) || propName.equals("error")) {
			updateButtonEnablement();
		}
	}
	
	
	private void bindRunButton(Button runButton) {
		this.runButton = runButton;
		runButton.addListener(SWT.Selection, e -> {
        	playScript();
        });
	}
	
	private void bindPauseButton(Button pauseButton) {
		this.pauseButton = pauseButton;
		pauseButton.addListener(SWT.Selection, e -> {
        	pauseScript();
        });
	}
	
	private void bindStopButton(Button stopButton) {
		this.stopButton = stopButton;
		stopButton.addListener(SWT.Selection, e -> {
        	stopScript();
        });
	}

	private void playScript() {
    	try {
			dynamicScriptingManager.playScript();
		} catch (DynamicScriptingException e) {
			MessageDialog.openError(Display.getDefault().getActiveShell(), "Dynamic scripting error", "Dynamic scripting error");
		}
    }
    
	private void stopScript() {
		dynamicScriptingManager.stopScript();
    }
    
	private void pauseScript() {
		dynamicScriptingManager.pauseScript();
    }
	
	private boolean nicosInError() {
		var nicosError = nicosModel.getError();
		return nicosError != NicosErrorState.NO_ERROR && nicosError != NicosErrorState.SCRIPT_SEND_FAIL;
	}
	
	private void updateButtonEnablement() {
		formatButtonsBasedOnStatus(dynamicScriptingManager.getDynamicScriptingStatus());
	}
	
	private void formatButtonsBasedOnStatus(DynamicScriptingStatus status) {
		switch (status) {
			case PLAYING:
				formatButtonsWhenPlaying();
				break;
			case PAUSED:
				formatButtonsWhenPaused();
				break;
			case STOPPED:
				formatButtonsWhenStopped();
				break;
			case ERROR:
				formatButtonsWhenError();
				break;
			default: // do nothing;
				break;
		}
	}
	
	private void formatButtonsWhenError() {
		runButton.setBackground(DEFAULT_COLOR);
		pauseButton.setBackground(DEFAULT_COLOR);
		runButton.setEnabled(false);
		pauseButton.setEnabled(false);
		stopButton.setEnabled(false);
		runButton.setText(RUN_BUTTON_TEXT);
		runButton.setImage(ExecutingStatusDisplay.EXECUTING_IMAGE);
	}

	private void formatButtonsWhenStopped() {
		runButton.setBackground(DEFAULT_COLOR);
		pauseButton.setBackground(DEFAULT_COLOR);
		runButton.setEnabled(!nicosInError());
		pauseButton.setEnabled(false);
		stopButton.setEnabled(false);
		runButton.setText(RUN_BUTTON_TEXT);
		runButton.setImage(ExecutingStatusDisplay.EXECUTING_IMAGE);
	}

	private void formatButtonsWhenPaused() {
		runButton.setBackground(DEFAULT_COLOR);
		pauseButton.setBackground(GREEN);
		runButton.setEnabled(!nicosInError());
		pauseButton.setEnabled(false);
		stopButton.setEnabled(!nicosInError());
		runButton.setText(RESUME_BUTTON_TEXT);
		runButton.setImage(ExecutingStatusDisplay.PAUSED_DURING_IMAGE);
	}

	private void formatButtonsWhenPlaying() {	
		runButton.setBackground(GREEN);
		pauseButton.setBackground(DEFAULT_COLOR);
		runButton.setEnabled(!nicosInError());
		pauseButton.setEnabled(!nicosInError());
		stopButton.setEnabled(!nicosInError());
		runButton.setText(RUN_BUTTON_TEXT);
		runButton.setImage(ExecutingStatusDisplay.EXECUTING_IMAGE);
	}

}
