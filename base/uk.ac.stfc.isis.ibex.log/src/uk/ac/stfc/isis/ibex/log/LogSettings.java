package uk.ac.stfc.isis.ibex.log;

import org.eclipse.jface.preference.IPreferenceStore;

import uk.ac.stfc.isis.ibex.instrument.InstrumentInfo;
import uk.ac.stfc.isis.ibex.instrument.InstrumentInfoReceiver;
import uk.ac.stfc.isis.ibex.log.preferences.PreferenceConstants;

public class LogSettings implements InstrumentInfoReceiver {

	private static final Log LOG = Log.getDefault();
	private static final IPreferenceStore PREFERENCES = Log.getDefault().getPreferenceStore();

	@Override
	public void setInstrument(InstrumentInfo instrument) {
		PREFERENCES.setValue(PreferenceConstants.P_JMS_ADDRESS, instrument.hostName());
		PREFERENCES.setValue(PreferenceConstants.P_SQL_ADDRESS, instrument.hostName());	
		
		LOG.clearMessages();
	}
}
