package uk.ac.stfc.isis.ibex.ui.nicos.models;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.swt.graphics.Image;
import org.eclipse.wb.swt.ResourceManager;

import uk.ac.stfc.isis.ibex.model.ModelObject;
import uk.ac.stfc.isis.ibex.nicos.BreakLevel;
import uk.ac.stfc.isis.ibex.nicos.ExecutionInstructionType;
import uk.ac.stfc.isis.ibex.nicos.NicosModel;
import uk.ac.stfc.isis.ibex.nicos.ScriptStatus;
import uk.ac.stfc.isis.ibex.nicos.messages.ExecutionInstruction;

/**
 * View model for the status of the currently executing script.
 */
public class ScriptStatusViewModel extends ModelObject {

    private static final Image PAUSE_ICON =
            ResourceManager.getPluginImage("uk.ac.stfc.isis.ibex.ui.dae", "icons/pause.png");
    private static final Image RESUME_ICON =
            ResourceManager.getPluginImage("uk.ac.stfc.isis.ibex.ui.dae", "icons/resume.png");

    private static final String PAUSE_TEXT = "Pause Script Execution";
    private static final String RESUME_TEXT = "Resume Script Execution";

    private static final ExecutionInstruction STOP_INSTRUCTION =
            new ExecutionInstruction(ExecutionInstructionType.STOP, BreakLevel.NOW);
    private static final ExecutionInstruction PAUSE_INSTRUCTION =
            new ExecutionInstruction(ExecutionInstructionType.BREAK, BreakLevel.AFTER_LINE);
    private static final ExecutionInstruction RESUME_INSTRUCTION =
            new ExecutionInstruction(ExecutionInstructionType.CONTINUE, null);

	private static final String NOT_EXECUTING = "Execution finished.";
	private static final String LINE_NUMBER_FORMAT = "Executing line %d.";

    private boolean enableButtons = false;

    private ScriptStatus status = ScriptStatus.IDLE;
    private String lineNumberStr = "";
    private String toggleButtonText = "";
    private String statusReadback = "";
    private Image toggleButtonIcon = PAUSE_ICON;

    private NicosModel model;

	/**
	 * Constructor.
	 * @param model the NicosModel to observe
	 */
	public ScriptStatusViewModel(final NicosModel model) {
        this.model = model;
		
		model.addPropertyChangeListener("lineNumber", new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				setLineNumber(model.getLineNumber());
			}
		});

        model.addPropertyChangeListener("scriptStatus", new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent arg0) {
                setScriptStatus(model.getScriptStatus());
            }
        });
	}
	
	private void setLineNumber(int lineNumber) {
		String line;
		
		if (lineNumber > 0) {
			line = String.format(LINE_NUMBER_FORMAT, lineNumber);
		} else {
			line = NOT_EXECUTING;
		}
		
		firePropertyChange("lineNumber", lineNumberStr, lineNumberStr = line);
	}
	
    /**
     * Modify the view model to reflect the run status of the script server.
     * 
     * @param status
     *            The run status of the script server.
     */
    public void setScriptStatus(ScriptStatus status) {
        setEnableButtons(true);
        this.status = status;
        setStatusReadback(status);
        switch (status) {
            case IDLEEXC:
            case IDLE:
            case STOPPING:
                setToggleButtonIcon(PAUSE_ICON);
                setToggleButtonText(PAUSE_TEXT);
                setEnableButtons(false);
                break;
            case RUNNING:
                setToggleButtonIcon(PAUSE_ICON);
                setToggleButtonText(PAUSE_TEXT);
                break;
            case INBREAK:
                setToggleButtonIcon(RESUME_ICON);
                setToggleButtonText(RESUME_TEXT);
                break;
            case INVALID:
            default:
                break;
        }
    
    }

	/**
	 * A formatted string representation of the line number to display on the user interface.
	 * @return a formatted string representation of the line number to display on the user interface
	 */
	public String getLineNumber() {
		return lineNumberStr;
	}

    private void setToggleButtonIcon(Image icon) {
        firePropertyChange("toggleButtonIcon", toggleButtonIcon, toggleButtonIcon = icon);
    }

    /**
     * @return The icon on the toggle pause button
     */
    public Image getToggleButtonIcon() {
        return toggleButtonIcon;
    }

    private void setToggleButtonText(String text) {
        firePropertyChange("toggleButtonText", toggleButtonText, toggleButtonText = text);
    }

    /**
     * @return The text on the toggle pause button
     */
    public String getToggleButtonText() {
        return toggleButtonText;
    }

    /**
     * Send a message to the NICOS server telling it to stop the current script
     * execution.
     */
    public void stopExecution() {
        model.sendExecutionInstruction(STOP_INSTRUCTION);
    }

    /**
     * Send a message to the NICOS server telling it to pause the current script
     * after the current line.
     */
    public void toggleExecution() {
        if (status == ScriptStatus.INBREAK) {
            model.sendExecutionInstruction(RESUME_INSTRUCTION);
        } else if (status == ScriptStatus.RUNNING) {
            model.sendExecutionInstruction(PAUSE_INSTRUCTION);
        }
    }

    private void setEnableButtons(boolean enable) {
        firePropertyChange("enableButtons", enableButtons, enableButtons = enable);
    }

    /**
     * @return Whether the script run control buttons should be enabled.
     */
    public boolean getEnableButtons() {
        return enableButtons;
    }

    private void setStatusReadback(ScriptStatus status) {
        String displayString = "Status is: " + status.getDesc();
        firePropertyChange("statusReadback", statusReadback, statusReadback = displayString);

    }

    /**
     * @return The text to display indicating the current execution status
     */
    public String getStatusReadback() {
        return statusReadback;
    }
}
