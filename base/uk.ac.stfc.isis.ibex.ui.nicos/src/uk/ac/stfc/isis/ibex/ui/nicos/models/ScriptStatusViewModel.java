package uk.ac.stfc.isis.ibex.ui.nicos.models;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import uk.ac.stfc.isis.ibex.model.ModelObject;
import uk.ac.stfc.isis.ibex.nicos.NicosModel;

/**
 * View model for the status of the currently executing script.
 */
public class ScriptStatusViewModel extends ModelObject {

	private static final String NOT_EXECUTING = "Execution finished.";
	private static final String LINE_NUMBER_FORMAT = "Executing line %d.";
	private String lineNumberStr = "";

	/**
	 * Constructor.
	 * @param model the NicosModel to observe
	 */
	public ScriptStatusViewModel(final NicosModel model) {
		
		model.addPropertyChangeListener("lineNumber", new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				setLineNumber(model.getLineNumber());
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
	 * A formatted string representation of the line number to display on the user interface.
	 * @return a formatted string representation of the line number to display on the user interface
	 */
	public String getLineNumber() {
		return lineNumberStr;
	}
}
