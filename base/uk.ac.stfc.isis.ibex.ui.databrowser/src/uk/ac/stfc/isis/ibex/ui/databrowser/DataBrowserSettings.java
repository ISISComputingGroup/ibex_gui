package uk.ac.stfc.isis.ibex.ui.databrowser;

import org.csstudio.trends.databrowser2.Activator;
import org.csstudio.trends.databrowser2.preferences.Preferences;
import org.eclipse.jface.preference.IPreferenceStore;

import uk.ac.stfc.isis.ibex.instrument.InstrumentInfo;
import uk.ac.stfc.isis.ibex.instrument.InstrumentInfoReceiver;

public class DataBrowserSettings implements InstrumentInfoReceiver {

	private final static IPreferenceStore PREFERENCES = Activator.getDefault().getPreferenceStore();
	private final static String DATABASE_URLS = PREFERENCES.getString(Preferences.URLS);
	private final static String ARCHIVES = PREFERENCES.getString(Preferences.ARCHIVES);	
	
	// Connect the databrowser to the archive engine.
	@Override
	public void setInstrument(InstrumentInfo instrument) {	
		setURLs(instrument.hostName());
		setArchives(instrument.hostName());
	}

	private void setURLs(String hostName) {
		PREFERENCES.setValue(Preferences.URLS, updateHostName(hostName, DATABASE_URLS));
	}
	
	private void setArchives(String hostName) {
		PREFERENCES.setValue(Preferences.ARCHIVES, updateHostName(hostName, ARCHIVES));	
	}
	
	private static String updateHostName(String hostName, String preference) {
		return preference.replaceAll("localhost", hostName);
	}
}
