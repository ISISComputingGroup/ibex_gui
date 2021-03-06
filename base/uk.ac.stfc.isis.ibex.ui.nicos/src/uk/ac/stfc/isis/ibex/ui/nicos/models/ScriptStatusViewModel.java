package uk.ac.stfc.isis.ibex.ui.nicos.models;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
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

    private static final Color HIGHLIGHT = Display.getDefault().getSystemColor(SWT.COLOR_GREEN);
    private static final Color HIGHLIGHT_PAUSED = Display.getDefault().getSystemColor(SWT.COLOR_GRAY);

    private static final String PAUSE_TEXT = "Pause Script Execution";
    private static final String RESUME_TEXT = "Resume Script Execution";

    private static final ExecutionInstruction STOP_INSTRUCTION =
            new ExecutionInstruction(ExecutionInstructionType.STOP, BreakLevel.NOW);
    private static final ExecutionInstruction PAUSE_INSTRUCTION =
            new ExecutionInstruction(ExecutionInstructionType.BREAK, BreakLevel.AFTER_LINE);
    private static final ExecutionInstruction RESUME_INSTRUCTION =
            new ExecutionInstruction(ExecutionInstructionType.CONTINUE, null);

	private static final String NOT_EXECUTING = "Execution finished";
	private static final String LINE_NUMBER_FORMAT = "Executing line %d";

    private boolean enableButtons = false;
    private Color highlightColour = HIGHLIGHT;

    private ScriptStatus status = ScriptStatus.IDLE;
    private String lineNumberStr = "";
    private String scriptNameStr = "";
    private String combinedScriptInfoStr = "";
    private String toggleButtonText = "";
    private String statusReadback = "";
    private Image toggleButtonIcon = PAUSE_ICON;

    private NicosModel model;

	/**
	 * Constructor.
	 * 
	 * @param model the NicosModel to observe
	 */
	public ScriptStatusViewModel(final NicosModel model) {
        this.model = model;
		
		model.addPropertyChangeListener("lineNumber", e ->
				setCombinedScriptInfo(model.getLineNumber(), model.getScriptStatus(), model.getScriptName()));
		
		model.addPropertyChangeListener("scriptStatus", e ->
				setCombinedScriptInfo(model.getLineNumber(), model.getScriptStatus(), model.getScriptName()));
		
		model.addPropertyChangeListener("scriptName", e ->
				setCombinedScriptInfo(model.getLineNumber(), model.getScriptStatus(), model.getScriptName()));
	}

	
	/**
	 * Combine the script information with current line number, executing status and name into a single string.
	 * 
	 * @param lineNumber 
	 * 				The line number, or -1 for a script that is not executing
	 * @param status
	 * 				The run state of the script server.
	 * @param scriptName 
	 * 				The name of the running script.
	 * 
	 */
	private void setCombinedScriptInfo(int lineNumber, ScriptStatus status, String scriptName) {
		setLineNumber(lineNumber);
		setScriptStatus(status);
		setScriptName(scriptName);
		
		String combinedScriptInfo = lineNumberStr + " (" + status.getDesc() + ") : " + scriptNameStr;
		
		firePropertyChange("combinedScriptInfo", combinedScriptInfoStr, combinedScriptInfoStr = combinedScriptInfo);
	}
	
	/**
	 * A formatted string with combined script information with current line number, executing status and name
	 * that will be displayed on the GUI.
	 * 
	 * @return String
	 * 				The combined script information with current line number, executing status and name.
	 */
	public String getCombinedScriptInfo() {
		return combinedScriptInfoStr;
	}
	
	/**
	 * Sets the line number of the currently executing script.
	 * 
	 * @param lineNumber the line number, or -1 for a script that is not executing
	 */
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
	 * A formatted string representation of the line number to display on the user interface.
	 * 
	 * @return a formatted string representation of the line number to display on the user interface
	 */
	public String getLineNumber() {
		return lineNumberStr;
	}
	
	/**
	 * Sets the name of the currently executing script.
	 * 
	 * @param scriptName 
	 * 				The name of the running script.
	 */
	private void setScriptName(String scriptName) {
		firePropertyChange("scriptName", scriptNameStr, scriptNameStr = scriptName);
	}
    
	/**
	 * A formatted string representation of the current running script name to display on the user interface.
	 * 
	 * @return a formatted string representation of the current script name to display on the user interface
	 */
	public String getScriptName() {
		return scriptNameStr;
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
        setHighlightColour(HIGHLIGHT);
        switch (status) {
            case IDLEEXC:
            case IDLE:
            case STOPPING:
                setToggleButtonTextAndIcon(PAUSE_TEXT, PAUSE_ICON);
                setEnableButtons(false);
                break;
            case RUNNING:
                setToggleButtonTextAndIcon(PAUSE_TEXT, PAUSE_ICON);
                break;
            case INBREAK:
                setToggleButtonTextAndIcon(RESUME_TEXT, RESUME_ICON);
                setHighlightColour(HIGHLIGHT_PAUSED);
                break;
            case INVALID:
            default:
                break;
        }
    
    }

    /**
     * @return The icon on the toggle pause button
     */
    public Image getToggleButtonIcon() {
        return toggleButtonIcon;
    }
    
    private void setToggleButtonTextAndIcon(String text, Image icon) {
        firePropertyChange("toggleButtonText", toggleButtonText, toggleButtonText = text);
        firePropertyChange("toggleButtonIcon", toggleButtonIcon, toggleButtonIcon = icon);
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
    
    /**
     * Whether or not the script run control buttons should be enabled.
     * 
     * @param enable whether they are enabled
     */
    private void setEnableButtons(boolean enable) {
        firePropertyChange("enableButtons", enableButtons, enableButtons = enable);
    }

    /**
     * Whether or not the script run control buttons should be enabled.
     * 
     * @return whether they are enabled
     */
    public boolean getEnableButtons() {
        return enableButtons;
    }
    
    /**
     * The status readback of the script
     * 
     * @param status
     */
    private void setStatusReadback(ScriptStatus status) {
        String displayString = "Queue status: " + status.getDesc();
        firePropertyChange("statusReadback", statusReadback, statusReadback = displayString);

    }

    /**
     * @return The text to display indicating the current execution status
     */
    public String getStatusReadback() {
        return statusReadback;
    }

    private void setHighlightColour(Color highlightColour) {
        firePropertyChange("highlightColour", this.highlightColour, this.highlightColour = highlightColour);
    }

    /**
     * The colour in which to highlight the currently executed line.
     * 
     * @return the colour
     */
    public Color getHighlightColour() {
        return highlightColour;
    }
}
