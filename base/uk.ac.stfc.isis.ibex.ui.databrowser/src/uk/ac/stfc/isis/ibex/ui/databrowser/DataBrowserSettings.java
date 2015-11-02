
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

package uk.ac.stfc.isis.ibex.ui.databrowser;

import org.csstudio.trends.databrowser2.Activator;
import org.csstudio.trends.databrowser2.preferences.Preferences;
import org.eclipse.jface.preference.IPreferenceStore;

import uk.ac.stfc.isis.ibex.instrument.InstrumentInfo;
import uk.ac.stfc.isis.ibex.instrument.InstrumentInfoReceiver;
import uk.ac.stfc.isis.ibex.instrument.internal.LocalHostInstrumentInfo;

/**
 * This class is responsible for changing the settings for the DataBrowser when
 * the instrument is changed, or first set. The archives and url settings get
 * changed.
 */
public class DataBrowserSettings implements InstrumentInfoReceiver {

    private IPreferenceStore preferences;
	
	public DataBrowserSettings() {
        this(Activator.getDefault().getPreferenceStore());
	}
	
	public DataBrowserSettings(IPreferenceStore preferences) {
	    this.preferences = preferences;
	}
	
	// Connect the databrowser to the archive engine.
	@Override
	public void setInstrument(InstrumentInfo instrument) {	
		setURLs(instrument.hostName());
		setArchives(instrument.hostName());
	}

	private void setURLs(String hostName) {
        preferences.setValue(Preferences.URLS, updateHostName(hostName, getDatabaseUrls()));
	}
	
	private void setArchives(String hostName) {
        preferences.setValue(Preferences.ARCHIVES, updateHostName(hostName, getArchives()));
	}
	
    /**
     * This class assumes machine names begin with NDX, e.g. NDXLARMOR, or are
     * localhost.
     * 
     * @param hostName
     *            The host name of the new machine.
     * @param preference
     *            The preference string.
     * @return The preference string with the host name replace.
     * @throws Exception
     */
    private static String updateHostName(String hostName, String preference) {
        if (preference.matches(InstrumentInfo.validInstrumentRegex())) {
            preference = preference.replaceAll("NDX[A-Z]+", hostName);
        } else if (preference.matches(LocalHostInstrumentInfo.validLocalInstrumentRegex())) {
            preference = preference.replaceAll("localhost", hostName);
        } else {
            throw new RuntimeException("Invalid preference string, does not contain a matching host pattern");
        }

        return preference;
	}

    private String getDatabaseUrls() {
        return preferences.getString(Preferences.URLS);
    }

    private String getArchives() {
        return preferences.getString(Preferences.ARCHIVES);
    }
}
