package uk.ac.stfc.isis.ibex.ui.nicos.models;

import java.util.List;

import uk.ac.stfc.isis.ibex.model.ModelObject;
import uk.ac.stfc.isis.ibex.nicos.NicosModel;
import uk.ac.stfc.isis.ibex.nicos.messages.NicosLogEntry;

/**
 * View model for the content of the NICOS output log.
 */
public class OutputLogViewModel extends ModelObject {

    private String log = "";

	/**
     * Constructor.
     * 
     * @param model
     *            the NicosModel to observe
     */
	public OutputLogViewModel(final NicosModel model) {
        model.addPropertyChangeListener("logEntries", e -> appendLog(model.getLogEntries()));
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
     * @return The entries from the NICOS log parsed into a single string.
     */
    public String getLog() {
        return log;
	}
}
