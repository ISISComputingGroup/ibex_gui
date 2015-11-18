
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

package uk.ac.stfc.isis.ibex.ui.alarm;

import java.util.regex.Pattern;

import org.csstudio.alarm.beast.Preferences;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.preferences.ScopedPreferenceStore;

import uk.ac.stfc.isis.ibex.instrument.InstrumentInfo;
import uk.ac.stfc.isis.ibex.instrument.InstrumentInfoReceiver;
import uk.ac.stfc.isis.ibex.instrument.internal.LocalHostInstrumentInfo;

public class AlarmSettings implements InstrumentInfoReceiver {

	private static final String PREF_QUALIFIER_ID = org.csstudio.alarm.beast.Activator.ID;
	
    private static final IPreferenceStore PREFERENCES =
            new ScopedPreferenceStore(InstanceScope.INSTANCE, PREF_QUALIFIER_ID);

	@Override
	public void setInstrument(InstrumentInfo instrument) {
        setAlarmURL(instrument.hostName());
	}

    private void setAlarmURL(String hostName) {
        PREFERENCES.setValue(Preferences.RDB_URL, updateHostName(hostName, getAlarmURL()));
        PREFERENCES.setValue(Preferences.JMS_URL, updateHostName(hostName, getJMSURL()));
	}

    private static String updateHostName(String hostName, String preference) {
        if (containsRegex(preference, InstrumentInfo.validInstrumentRegex())) {
            preference = preference.replaceAll(InstrumentInfo.validInstrumentRegex(), hostName);
        } else if (containsRegex(preference, LocalHostInstrumentInfo.validLocalInstrumentRegex())) {
            preference = preference.replaceAll(LocalHostInstrumentInfo.validLocalInstrumentRegex(), hostName);
        } else {
            throw new RuntimeException(
                    "Invalid preference string, does not contain a matching host pattern:" + preference);
        }

        return preference;
    }

    private static boolean containsRegex(String string, String regex) {
        return Pattern.compile(regex).matcher(string).find();
    }

    private static String getAlarmURL() {
        return PREFERENCES.getString(Preferences.RDB_URL);
    }

    private static String getJMSURL() {
        return PREFERENCES.getString(Preferences.JMS_URL);
    }
}
