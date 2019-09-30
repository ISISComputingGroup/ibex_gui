package uk.ac.stfc.isis.ibex.e4.ui.perspectiveswitcher.controls;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.wb.swt.SWTResourceManager;

import uk.ac.stfc.isis.ibex.nicos.NicosModel;
import uk.ac.stfc.isis.ibex.nicos.ScriptStatus;

/**
 * ViewModel to interact with the Script Server button and change its states
 *
 */
public final class ScriptServerButtonViewModel extends PerspectiveButtonViewModel {
	
	private static final Color SCRIPT_COLOR = SWTResourceManager.getColor(150, 250, 170);
	private final FlashingButton flash;
	private ScriptStatus status = ScriptStatus.IDLE;
	private String Label;
	// Spacing to separate from the label
	private String PlayButton = "     \u25B6";
	private String PauseButton= "     \u23F8";
	
	/**
	 * 
	 * @param model 
	 * 			the model that ScriptServerButton listens to 
	 * @param buttonLabel
	 * 			the label to display on the button
	 */
	public ScriptServerButtonViewModel(NicosModel model, String buttonLabel) {
		super(buttonLabel);
		// TODO Auto-generated constructor stub
		flash = new FlashingButton(this, SCRIPT_COLOR);
		flash.setDefaultColour(DEFOCUSSED);
		Label = buttonLabel;
		
		model.addPropertyChangeListener("scriptStatus", arg0->{
			status = model.getScriptStatus();
			setButtonColor();
			updateFlashing();
		});
		
	}
	
	/**
	 * set Script server button colour for different states of script
	 */
	protected void setButtonColor() {
		if (status == ScriptStatus.RUNNING) {
			setColor(SCRIPT_COLOR);
		}
		
	}
	
	/**
	 * Flash when it is required
	 */
	protected void updateFlashing() {
		if (status == ScriptStatus.RUNNING) {
			flash.start();
			setText(Label + PlayButton);
		} else if (status == ScriptStatus.INBREAK) {
			flash.stop();
			setText(Label + PauseButton);
		} else {
			flash.stop();
			setText(Label);
		}
	}
	
}