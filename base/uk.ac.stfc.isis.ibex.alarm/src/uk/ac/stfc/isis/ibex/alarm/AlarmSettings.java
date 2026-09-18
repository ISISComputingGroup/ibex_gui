
/*
 * This file is part of the ISIS IBEX application.
 * Copyright (C) 2012-2016 Science & Technology Facilities Council.
 * All rights reserved.
 *
 * This program is distributed in the hope that it will be useful.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution.
 * EXCEPT AS EXPRESSLY SET FORTH IN THE ECLIPSE PUBLIC LICENSE V1.0, THE PROGRAM 
 * AND ACCOMPANYING MATERIALS ARE PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES 
 * OR CONDITIONS OF ANY KIND.  See the Eclipse Public License v1.0 for more details.
 *
 * You should have received a copy of the Eclipse Public License v1.0
 * along with this program; if not, you can obtain a copy from
 * https://www.eclipse.org/org/documents/epl-v10.php or 
 * http://opensource.org/licenses/eclipse-1.0.php
 */

/**
 * 
 */
package uk.ac.stfc.isis.ibex.alarm;

import org.eclipse.core.runtime.preferences.InstanceScope;
import org.osgi.service.prefs.Preferences;

import uk.ac.stfc.isis.ibex.activemq.ActiveMQ;
import uk.ac.stfc.isis.ibex.instrument.InstrumentInfo;

/**
 * Class to manage the alarm settings for CSS Beast Alarm.
 */
public class AlarmSettings {

    /**
     * The plugin activator ID for beast.
     */
    private static final String PREF_QUALIFIER_ID = org.csstudio.alarm.beast.Activator.ID;
    private static final String PREF_QUALIFIER_HIST_ID = org.csstudio.alarm.beast.msghist.Activator.ID;

    private Preferences preferences, preferencesHist;


    /**
     * Instantiates a new alarm settings with default preferences store.
     */
    public AlarmSettings() {
        this(InstanceScope.INSTANCE.getNode(PREF_QUALIFIER_ID), InstanceScope.INSTANCE.getNode(PREF_QUALIFIER_HIST_ID));
    }


    /**
     * Instantiates a new alarm settings with desired preferences.
     *
     * @param preferences The preferences to use for normal alarms
     * @param preferencesHist The preferences to use for alarm history
     */
    public AlarmSettings(Preferences preferences, Preferences preferencesHist) {
        this.preferences = preferences;
        this.preferencesHist = preferencesHist;
    }

    /**
     * Update the alarm settings to reflect the new instrument.
     * 
     * @param instrument the instrument to use
     */
    public void setInstrument(InstrumentInfo instrument) {
        String hostName = instrument.hostName();
        preferences.put(org.csstudio.alarm.beast.Preferences.RDB_URL, buildRdbUrl(hostName, "ALARM"));
        preferences.put(org.csstudio.alarm.beast.Preferences.JMS_URL, buildJmsUrl(hostName));
        preferencesHist.put(org.csstudio.alarm.beast.msghist.Preferences.RDB_URL, buildRdbUrl(hostName, "log"));
    }

    /**
     * @param hostName The instrument name
     * @table Database table name
     * @return The archive settings string corresponding to the given
     *         instrument.
     */
    private static String buildRdbUrl(String hostName, String table) {
        return "jdbc:mysql://" + hostName + "/" + table + "?useSSL=false&useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=Europe/London&autoReconnect=true&allowPublicKeyRetrieval=true";
    }

    /**
     * @param hostName The instrument name
     * @return The URLs settings string corresponding to the given instrument.
     */
    private static String buildJmsUrl(String hostName) {
        return "failover:(tcp://" + hostName + ":" + ActiveMQ.JMS_PORT + ")";
    }
}
