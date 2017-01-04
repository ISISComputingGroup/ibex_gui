
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
 * the instrument is changed, or first set. The archives and URL settings get
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
	
    /**
     * Connect the databrowser to the archive engine for the new instrument.
     * 
     * @param instrument instrument being switched to
     */
	@Override
    public void setInstrument(InstrumentInfo instrument) {
        setURLs(instrument.hostName());
        setArchives(instrument.hostName());
	}

    @Override
    public void preSetInstrument(InstrumentInfo instrument) {
        // nothing extra to do
    }

    @Override
    public void postSetInstrument(InstrumentInfo instrument) {
        // nothing extra to do
    }

    /**
     * Update the URL preferences. Finds instances of oldHostName and replaces
     * with newHostName.
     * 
     * @param hostName The new host instrument name
     */
    private void setURLs(String hostName) {
        preferences.setValue(Preferences.URLS, buildDatabaseUrl(hostName));
	}

    /**
     * Update the archive preferences. Finds instances of oldHostName and
     * replaces with newHostName.
     * 
     * @param hostName The new host instrument name
     */
    private void setArchives(String hostName) {
        preferences.setValue(Preferences.ARCHIVES, buildArchivesUrl(hostName));
	}

    /**
     * @param hostName The instrument name
     * @return The database URL corresponding to the given instrument.
     */
    private static String buildDatabaseUrl(String hostName) {
        return "jdbc:mysql://" + hostName + "/archive*jdbc:mysql://130.246.39.152/archive";
    }

    /**
     * @param hostName The instrument name
     * @return The archives URL corresponding to the given instrument.
     */
    private static String buildArchivesUrl(String hostName) {
        return "RDB|1|jdbc:mysql://" + hostName + "/archive*RDB|2|jdbc:mysql://130.246.39.152/archive";
    }

}
