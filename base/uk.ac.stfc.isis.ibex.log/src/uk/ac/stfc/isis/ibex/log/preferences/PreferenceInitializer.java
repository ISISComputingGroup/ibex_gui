/*
 * Copyright (C) 2013-2014 Research Councils UK (STFC)
 *
 * This file is part of the Instrument Control Project at ISIS.
 *
 * This code and information are provided "as is" without warranty of any 
 * kind, either expressed or implied, including but not limited to the
 * implied warranties of merchantability and/or fitness for a particular 
 * purpose.
 */
package uk.ac.stfc.isis.ibex.log.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

import uk.ac.stfc.isis.ibex.log.Log;

/**
 * Class used to initialize default preference values.
 */
public class PreferenceInitializer extends AbstractPreferenceInitializer {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer#initializeDefaultPreferences()
	 */
	public void initializeDefaultPreferences() {
		IPreferenceStore store = Log.getDefault().getPreferenceStore();
		
		store.setDefault(PreferenceConstants.P_JMS_ADDRESS, PreferenceConstants.DEFAULT_JMS_ADDRESS);
		store.setDefault(PreferenceConstants.P_JMS_PORT, PreferenceConstants.DEFAULT_JMS_PORT);
		store.setDefault(PreferenceConstants.P_JMS_TOPIC, PreferenceConstants.DEFAULT_JMS_TOPIC);
		store.setDefault(PreferenceConstants.P_SQL_ADDRESS, PreferenceConstants.DEFAULT_SQL_ADDRESS);
		store.setDefault(PreferenceConstants.P_SQL_PORT, PreferenceConstants.DEFAULT_SQL_PORT);
		store.setDefault(PreferenceConstants.P_SQL_USERNAME, PreferenceConstants.DEFAULT_SQL_USERNAME);
		store.setDefault(PreferenceConstants.P_SQL_PASSWORD, PreferenceConstants.DEFAULT_SQL_PASSWORD);
		store.setDefault(PreferenceConstants.P_SQL_SCHEMA, PreferenceConstants.DEFAULT_SQL_SCHEMA);
		store.setDefault(PreferenceConstants.P_MINOR_MESSAGE, PreferenceConstants.DEFAULT_MINOR_MESSAGE);
	}

}
