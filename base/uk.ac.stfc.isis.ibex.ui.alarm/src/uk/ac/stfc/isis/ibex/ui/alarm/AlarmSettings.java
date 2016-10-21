
/*
 * This file is part of the ISIS IBEX application. Copyright (C) 2012-2015
 * Science & Technology Facilities Council. All rights reserved.
 *
 * This program is distributed in the hope that it will be useful. This program
 * and the accompanying materials are made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution. EXCEPT AS
 * EXPRESSLY SET FORTH IN THE ECLIPSE PUBLIC LICENSE V1.0, THE PROGRAM AND
 * ACCOMPANYING MATERIALS ARE PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES
 * OR CONDITIONS OF ANY KIND. See the Eclipse Public License v1.0 for more
 * details.
 *
 * You should have received a copy of the Eclipse Public License v1.0 along with
 * this program; if not, you can obtain a copy from
 * https://www.eclipse.org/org/documents/epl-v10.php or
 * http://opensource.org/licenses/eclipse-1.0.php
 */

package uk.ac.stfc.isis.ibex.ui.alarm;

import org.csstudio.alarm.beast.Preferences;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.preferences.ScopedPreferenceStore;

import uk.ac.stfc.isis.ibex.instrument.InstrumentInfo;
import uk.ac.stfc.isis.ibex.instrument.InstrumentInfoReceiver;

/**
 * This class is responsible for changing the alarm settings for the DataBrowser
 * when the instrument is changed, or first set. Several url settings get
 * changed.
 */
public class AlarmSettings implements InstrumentInfoReceiver {

    /**
     * The plugin activator ID.
     */
    private static final String PREF_QUALIFIER_ID = org.csstudio.alarm.beast.Activator.ID;

    /**
     * The current preferences store.
     */
    private IPreferenceStore preferences;

    /**
     * Default constructor.
     */
    public AlarmSettings() {
        this(new ScopedPreferenceStore(InstanceScope.INSTANCE, PREF_QUALIFIER_ID));
    }

    /**
     * Create an AlarmSettings object with a specific preference store.
     * 
     * @param scopedPreferenceStore
     */
    public AlarmSettings(IPreferenceStore preferences) {
        this.preferences = preferences;
    }

    @Override
    public void setInstrument(InstrumentInfo newInstrument, InstrumentInfo oldInstrument) {
        setAlarmURL(newInstrument.hostName(), oldInstrument.hostName());
    }

    /**
     * Update the current alarm URLs by replacing the oldHostName with the
     * newHostName.
     * 
     * @param newHostName The name of the new instrument host
     * @param oldHostName The name of the previous instrument host
     */
    private void setAlarmURL(String newHostName, String oldHostName) {
        preferences.setValue(Preferences.RDB_URL, updateHostName(newHostName, oldHostName, getAlarmURL()));
        preferences.setValue(Preferences.JMS_URL, updateHostName(newHostName, oldHostName, getJMSURL()));
    }

    /**
     * Update the host name in a specific URL by replacing the old host name
     * with the new one.
     * 
     * @param newHostName The name of the new instrument host
     * @param oldHostName The name of the previous instrument host
     * @param preference The preference to be updated
     * @return The updated preference string
     */
    private static String updateHostName(String newHostName, String oldHostName, String preference) {
        // Extra replace of localhost is needed because preference not restored
        // on startup, so defaults to localhost. See ticket #1109
        String newPreference = preference.replace(oldHostName, newHostName).replace("localhost", newHostName);
        if (newPreference == preference) {
            throw new RuntimeException(
                    "Unable to update preference: " + preference + ". Does not contain old host name: " + oldHostName);
        } else {
            preference = newPreference;
        }
        return preference;
    }

    /**
     * @return The current alarm URL
     */
    private String getAlarmURL() {
        return preferences.getString(Preferences.RDB_URL);
    }

    /**
     * @return The current JMS URL
     */
    private String getJMSURL() {
        return preferences.getString(Preferences.JMS_URL);
    }
}
