package uk.ac.stfc.isis.ibex.e4.ui.perspectiveswitcher.controls;

import org.eclipse.swt.graphics.Color;
import org.eclipse.wb.swt.SWTResourceManager;

import uk.ac.stfc.isis.ibex.nicos.NicosModel;
import uk.ac.stfc.isis.ibex.nicos.ScriptStatus;

/**
 * ViewModel to interact with the Script Server button and change its states
 *
 */
public final class ScriptServerButtonViewModel extends PerspectiveButtonViewModel {
	
	private static final Color SCRIPT_BUTTON_COLOR = SWTResourceManager.getColor(150, 250, 170);
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
		flash = new FlashingButton(this, SCRIPT_BUTTON_COLOR);
		Label = buttonLabel;
		
		model.addPropertyChangeListener("scriptStatus", ignored->{
			status = model.getScriptStatus();
			setButtonColor();
			updateFlashing();
		});
		
	}
	
	/**
	 * set Script server button colour for different states of script
	 */
	protected void setButtonColor() {
		if (status == ScriptStatus.RUNNING && (!inFocus)) {
			setColor(SCRIPT_BUTTON_COLOR);
		} else if (active) {
			setColor(ACTIVE);
		}
	}
	
	/**
	 * Flash when it is required, Flashing only used when Script is paused
	 */
	protected void updateFlashing() {
		String label = Label;
		if (status == ScriptStatus.RUNNING) {
			label = label + PlayButton;
		} else if (status == ScriptStatus.INBREAK) {
			flash.start();
			label = label + PauseButton;
		} else {
			flash.stop();
			// once flashing is stopped set color for either activated or deactivated button
			setButtonColor();
		}
		setText(label);
	}
	
	/**
	 * set if focus is in the current button or not, 
	 * the way this button is represented(colors) also depends on focus 
	 * @param inFocus if focus is in the current button or not i.e. Sript Server button
	 */
	@Override
	public void setFocus(boolean inFocus) {
        super.setFocus(inFocus);
        if (flash != null) {
            if (inFocus) {
                flash.stop();
            } else {
                updateFlashing();
            }
        }
        setButtonColor();
    }
}
