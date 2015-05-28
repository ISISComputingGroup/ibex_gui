package uk.ac.stfc.isis.ibex.ui.alarm;

import org.csstudio.alarm.beast.Preferences;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.preferences.ScopedPreferenceStore;

import uk.ac.stfc.isis.ibex.instrument.InstrumentInfo;
import uk.ac.stfc.isis.ibex.instrument.InstrumentInfoReceiver;

public class AlarmSettings implements InstrumentInfoReceiver {

	private static final String PREF_QUALIFIER_ID = org.csstudio.alarm.beast.Activator.ID;
	
    private static final IPreferenceStore PREFERENCES =
            new ScopedPreferenceStore(InstanceScope.INSTANCE, PREF_QUALIFIER_ID);

	private static final String RDB_URL =  PREFERENCES.getString(Preferences.RDB_URL);

	@Override
	public void setInstrument(InstrumentInfo instrument) {
		setDatabaseURL(instrument.hostName());
	}

	private void setDatabaseURL(String hostName) {
		PREFERENCES.setValue(Preferences.RDB_URL, RDB_URL.replaceAll("localhost", hostName));
	}
}
