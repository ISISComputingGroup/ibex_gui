package uk.ac.stfc.isis.ibex.log;

import org.eclipse.jface.preference.IPreferenceStore;

import uk.ac.stfc.isis.ibex.log.message.LogMessage;
import uk.ac.stfc.isis.ibex.log.preferences.PreferenceConstants;
import uk.ac.stfc.isis.ibex.model.ModelObject;

public class LogCounter extends ModelObject implements ILogMessageConsumer {
	
	private static final String MAJOR = "MAJOR";
	private static final String MINOR = "MINOR";
	
	private static IPreferenceStore preferenceStore = Log.getDefault().getPreferenceStore();

	private MessageCounter counter = new MessageCounter();
	private boolean running = true;

	public void start()	{
		running = true;
	}
	
	public void stop() {
		running = false;
	}
	
	public long getCount() {
		return counter.countsForSeverity(MAJOR) + optionalMinorCount();
	}
	
	public void resetCount() {
		long grandTotal = counter.totalCount();	
		counter = new MessageCounter();
		fireCountChanged(grandTotal);
	}
	
	/**
	 * Count the messages as they come in, if they have the correct severity.
	 */
	@Override
	public void newMessage(LogMessage logMessage) {	
		if (!running) {
			return;
		}
		
		long totalToDate = counter.totalCount();	
		counter.countMessage(logMessage);
		fireCountChanged(totalToDate);
	}

	private void fireCountChanged(long previousTotal) {
		firePropertyChange("count", previousTotal, counter.totalCount());
	}
	
	private long optionalMinorCount() {
		return includeMinorSeverityCount() ?  counter.countsForSeverity(MINOR) : 0;
	}
	
	private boolean includeMinorSeverityCount() {
		return preferenceStore.getBoolean(PreferenceConstants.P_MINOR_MESSAGE);
	}

	@Override
	public void clearMessages() {
		this.resetCount();		
	}
}
