
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

package uk.ac.stfc.isis.ibex.databases.preferences;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.IScopeContext;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.jface.preference.IPreferenceStore;

import uk.ac.stfc.isis.ibex.databases.Databases;

/**
 * Supplies the details for the IBEX database preference page.
 */
public class PreferenceSupplier extends AbstractPreferenceInitializer {
    /** The preference setting for the SQL address. */
    public static final String SQL_ADDRESS = "sqlAddress";
    
    /** The default setting for the SQL address. */
    public static final String DEFAULT_SQL_ADDRESS = "localhost";
    
    /** The preference setting for the SQL port. */
    public static final String SQL_PORT = "sqlPort";

    /** The default setting for the SQL port. */
    public static final String DEFAULT_SQL_PORT = "3306";
    
    /** The scope for the preference settings. */
	public static final IScopeContext SCOPE_CONTEXT = InstanceScope.INSTANCE;

    /**
     * The overall preferences name.
     */
    public static final String PREFERENCE_NODE = "uk.ac.stfc.isis.ibex.databases";
	
    /**
     * @return the database preferences.
     */
	public static IEclipsePreferences getPreferences() {
        return SCOPE_CONTEXT.getNode(PREFERENCE_NODE);
    }
	
    /**
     * @return the preference setting for the SQL address.
     */
	public static String sqlAddress() {
		return getPreferences().get(SQL_ADDRESS, DEFAULT_SQL_ADDRESS);
	}
	
    /**
     * @return the preference setting for the SQL port.
     */
	public static String sqlPort() {
		return getPreferences().get(SQL_PORT, DEFAULT_SQL_PORT);
	}
	
	@Override
	public void initializeDefaultPreferences() {
        IPreferenceStore store = Databases.getDefault().getPreferenceStore();
        Map<String, String> initializationEntries = PreferenceSupplier.getInitializationEntries();
        for (Map.Entry<String, String> entry : initializationEntries.entrySet()) {
            store.setDefault(entry.getKey(), entry.getValue());
        }
    }

    /**
     * Gets the initial settings for the preferences.
     * 
     * @return the initial settings
     */
	public static Map<String, String> getInitializationEntries() {
        Map<String, String> entries = new HashMap<String, String>();
        entries.put(SQL_ADDRESS, DEFAULT_SQL_ADDRESS);
        entries.put(SQL_PORT, DEFAULT_SQL_PORT);
    		
        return entries;
	}
}
