
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
     * @param preferences The preference store to use for the alarm settings
     */
    public AlarmSettings(IPreferenceStore preferences) {
        this.preferences = preferences;
    }

    @Override
    public void setInstrument(InstrumentInfo instrument) {
        setAlarmURL(instrument.hostName());
    }

    /**
     * Update the current alarm URLs by replacing the oldHostName with the
     * newHostName.
     * 
     * @param hostName The name of the new instrument host
     */
    private void setAlarmURL(String hostName) {
        preferences.setValue(Preferences.RDB_URL, buildRdbUrl(hostName));
        preferences.setValue(Preferences.JMS_URL, buildJmsUrl(hostName));
    }

    /**
     * @param hostName The instrument name
     * @return The archive settings string corresponding to the given
     *         instrument.
     */
    private static String buildRdbUrl(String hostName) {
        return "jdbc:mysql://" + hostName + "/ALARM";
    }

    /**
     * @param hostName The instrument name
     * @return The URLs settings string corresponding to the given instrument.
     */
    private static String buildJmsUrl(String hostName) {
        return "failover:(tcp://" + hostName + ":61616)";
    }
}
