package uk.ac.stfc.isis.ibex.preferences;

import java.util.Map;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

public class PreferenceInitializer extends AbstractPreferenceInitializer {

	@Override
	public void initializeDefaultPreferences() {
        IPreferenceStore store = Preferences.getDefault().getPreferenceStore();
        Map<String, String> initializationEntries = PreferenceSupplier.getInitializationEntries();
        for (Map.Entry<String, String> entry : initializationEntries.entrySet()) {
            store.setDefault(entry.getKey(), entry.getValue());
        }		
	}
}
