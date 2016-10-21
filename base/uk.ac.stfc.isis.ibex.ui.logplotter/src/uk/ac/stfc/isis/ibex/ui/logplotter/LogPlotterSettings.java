
/*
* This file is part of the ISIS IBEX application.
* Copyright (C) 2012-2015 Science & Technology Facilities Council.
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

package uk.ac.stfc.isis.ibex.ui.logplotter;

import org.csstudio.trends.databrowser2.Activator;
import org.csstudio.trends.databrowser2.preferences.Preferences;
import org.eclipse.jface.preference.IPreferenceStore;

import uk.ac.stfc.isis.ibex.instrument.InstrumentInfo;
import uk.ac.stfc.isis.ibex.instrument.InstrumentInfoReceiver;

/**
 * This class is responsible for changing the settings for the DataBrowser when
 * the instrument is changed, or first set. The archives and url settings get
 * changed.
 */
public class LogPlotterSettings implements InstrumentInfoReceiver {

    /**
     * Current log plotter settings preference store.
     **/
    private IPreferenceStore preferences;
	
    /**
     * Default constructor.
     */
	public LogPlotterSettings() {
        this(Activator.getDefault().getPreferenceStore());
	}
	
    /**
     * Construct with initial preferences.
     * 
     * @param preferences Initial preferences
     */
	public LogPlotterSettings(IPreferenceStore preferences) {
	    this.preferences = preferences;
	}
	
	// Connect the databrowser to the archive engine.
	@Override
    public void setInstrument(InstrumentInfo newInstrument, InstrumentInfo oldInstrument) {
        setURLs(newInstrument.hostName(), oldInstrument.hostName());
        setArchives(newInstrument.hostName(), oldInstrument.hostName());
	}

    /**
     * Update the URL preferences. Finds instances of oldHostName and replaces
     * with newHostName.
     * 
     * @param newHostName The new host instrument name
     * @param oldHostName The previous host instrument name
     */
    private void setURLs(String newHostName, String oldHostName) {
        preferences.setValue(Preferences.URLS, updateHostName(newHostName, oldHostName, getDatabaseUrls()));
	}

    /**
     * Update the archive preferences. Finds instances of oldHostName and
     * replaces with newHostName.
     * 
     * @param newHostName The new host instrument name
     * @param oldHostName The previous host instrument name
     */
    private void setArchives(String newHostName, String oldHostName) {
        preferences.setValue(Preferences.ARCHIVES, updateHostName(newHostName, oldHostName, getArchives()));
	}

    /**
     * Updates a particular preference. Finds instances of oldHostName and
     * replaces with newHostName.
     * 
     * @param newHostName The new host instrument name
     * @param oldHostName The previous host instrument name
     * @param preference The preference to update
     * @return The update preference string
     */
    private static String updateHostName(String newHostName, String oldHostName, String preference) {
        String newPreference = preference.replace(oldHostName, newHostName);
        if (newPreference == preference) {
            throw new RuntimeException(
                    "Unable to update preference: " + preference + ". Does not contain old host name: " + oldHostName);
        } else {
            preference = newPreference;
        }
        return preference;
	}

    /**
     * @return The current URLs preferences
     */
    private String getDatabaseUrls() {
        return preferences.getString(Preferences.URLS);
    }

    /**
     * @return The current archive preferences
     */
    private String getArchives() {
        return preferences.getString(Preferences.ARCHIVES);
    }
}
