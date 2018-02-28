package uk.ac.stfc.isis.ibex.ui.nicos.models;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

import uk.ac.stfc.isis.ibex.model.ModelObject;
import uk.ac.stfc.isis.ibex.nicos.NicosModel;
import uk.ac.stfc.isis.ibex.nicos.messages.NicosLogEntry;

/**
 * View model for the content of the output log.
 */
public class OutputLogViewModel extends ModelObject {

    private String log = "";

	/**
	 * Constructor.
	 * @param model the NicosModel to observe
	 */
	public OutputLogViewModel(final NicosModel model) {
		
        model.addPropertyChangeListener("logEntries", new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
                appendLog(model.getLogEntries());
			}
		});
	}
	
    private void appendLog(List<NicosLogEntry> log) {
        String newMessages = "";
        for (NicosLogEntry entry : log) {
            newMessages += entry.toString();
        }
        setLog(this.log.concat(newMessages));
	}

    private void setLog(String log) {
        firePropertyChange("log", this.log, this.log = log);
    }
	
	/**
	 * A formatted string representation of the line number to display on the user interface.
	 * @return a formatted string representation of the line number to display on the user interface
	 */
    public String getLog() {
        return log;
	}
}
