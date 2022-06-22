package uk.ac.stfc.isis.ibex.ui.scriptgenerator.views;

import java.util.Optional;

import org.eclipse.swt.graphics.Image;
import org.eclipse.wb.swt.ResourceManager;

import uk.ac.stfc.isis.ibex.scriptgenerator.table.ActionDynamicScriptingStatus;
import uk.ac.stfc.isis.ibex.scriptgenerator.table.ScriptGeneratorAction;

/**
 * Class to handle displaying images for execution status.
 *
 */
public final class ExecutingStatusDisplay {
	private ExecutingStatusDisplay() {
	}
	/**
	 * Image to use to show that a script is executing.
	 */
    public static final Image EXECUTING_IMAGE = ResourceManager.getPluginImage("uk.ac.stfc.isis.ibex.ui.scriptgenerator", "icons/play.png");
    /**
     * Image to use to show pausing.
     */
    public static final Image PAUSED_BEFORE_IMAGE = ResourceManager.getPluginImage("uk.ac.stfc.isis.ibex.ui.scriptgenerator", "icons/pause.png");
    /**
     * Image to use to show resuming.
     */
    public static final Image PAUSED_DURING_IMAGE = ResourceManager.getPluginImage("uk.ac.stfc.isis.ibex.ui.scriptgenerator", "icons/resume.png");
    /**
     * Null image for not executing.
     */
    public static final Image NOT_EXECUTING_IMAGE = null;
	
    /**
     * @param status The scripting status.
     * @return The mark to display whether executing or not.
     */
	public static Image getImage(ActionDynamicScriptingStatus status) {
		switch (status) {
			case EXECUTING:
				return EXECUTING_IMAGE;
			case PAUSED_BEFORE_EXECUTION:
				return PAUSED_BEFORE_IMAGE;
			case PAUSED_DURING_EXECUTION:
				return PAUSED_DURING_IMAGE;
			default:
				return NOT_EXECUTING_IMAGE;
		}
	}
	
	/**
	 * Get the ValidityDisplay from it's displayed text.
	 * 
	 * @param optionalImage an image to generate a Validity display element from.
	 * @return A ValidityDisplay element generated from the given string.
	 */
	public static ActionDynamicScriptingStatus fromImage(Optional<Image> optionalImage) {
		if (optionalImage.isPresent()) {
			Image image = optionalImage.get();
			if (image.equals(EXECUTING_IMAGE)) {
				return ActionDynamicScriptingStatus.EXECUTING;
			} else if (image.equals(PAUSED_BEFORE_IMAGE)) {
				return ActionDynamicScriptingStatus.PAUSED_BEFORE_EXECUTION;
			} else if (image.equals(PAUSED_DURING_IMAGE)) {
				return ActionDynamicScriptingStatus.PAUSED_DURING_EXECUTION;
			} else {
				return ActionDynamicScriptingStatus.NO_STATUS;
			}
		} else {
			return ActionDynamicScriptingStatus.NO_STATUS;
		}
		
	}
	
	/**
	 * Whether the actions executing status equals this enum.
	 * 
	 * @param action The action to check the executing status of.
	 * @param status The status to check against.
	 * @return True if the action and this enum have the same executing status or false if not.
	 */
	public static boolean equalsAction(ActionDynamicScriptingStatus status, ScriptGeneratorAction action) {
		return status.equals(action.getDynamicScriptingStatus());
	}

}
