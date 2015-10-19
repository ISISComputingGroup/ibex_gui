
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

public class PreferenceSupplier extends AbstractPreferenceInitializer {
    
    public static final String SQL_ADDRESS = "sqlAddress";
    public static final String Default_SQL_ADDRESS = "localhost";
    
    public static final String SQL_PORT = "sqlPort";
    public static final String Default_SQL_PORT = "3306";
    
	public static final IScopeContext SCOPE_CONTEXT = InstanceScope.INSTANCE;
    public static final String PREFERENCE_NODE = "uk.ac.stfc.isis.ibex.databases";
	
	public static IEclipsePreferences getPreferences() {
        return SCOPE_CONTEXT.getNode(PREFERENCE_NODE);
    }
	
	public static String SQLAddress() {
		return getPreferences().get(SQL_ADDRESS, Default_SQL_ADDRESS);
	}
	
	public static String SQLPort() {
		return getPreferences().get(SQL_PORT, Default_SQL_PORT);
	}
	
	@Override
	public void initializeDefaultPreferences() {
        IPreferenceStore store = Databases.getDefault().getPreferenceStore();
        Map<String, String> initializationEntries = PreferenceSupplier.getInitializationEntries();
        for (Map.Entry<String, String> entry : initializationEntries.entrySet()) {
            store.setDefault(entry.getKey(), entry.getValue());
        }
    }

	public static Map<String, String> getInitializationEntries() {
        Map<String, String> entries = new HashMap<String, String>();
        entries.put(SQL_ADDRESS, Default_SQL_ADDRESS);
        entries.put(SQL_PORT, Default_SQL_PORT);
    		
        return entries;
	}
}
