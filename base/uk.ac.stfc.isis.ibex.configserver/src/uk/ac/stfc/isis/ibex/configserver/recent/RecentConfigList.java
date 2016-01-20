
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
package uk.ac.stfc.isis.ibex.configserver.recent;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.preferences.ConfigurationScope;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.prefs.Preferences;

/**
 * Class to manage the list of recently used configurations
 * @author sjb99183
 *
 */
public class RecentConfigList {
	private List<String> recent;
	private static final int MRU_LENGTH = 4;
	private static final String MRU_PREFS = "config-mru-slot-";
	private static final String PLUGIN_ID = "uk.ac.stfc.isis.ibex.configserver";
	
	/**
	 * Constructor - loads the saved list from preferences
	 */
	public RecentConfigList() {
		load();
	}
	
	/**
	 *  Add an item
	 * @param item the configuration item to add to the list
	 */
	public void add(String item) {
		// Is it already in the list?
		boolean first = true;
		for (String current : recent) {
			if (current.equals(item)) {
				if (!first) {
					// Move to first in the list
					recent.remove(item);
					recent.add(0, item);
					save();
				}
				// Done
				return;
			}
			first = false;
		}
		
		// Not in the list, so add it, removing the oldest item if required
		recent.add(0, item);
		while (recent.size() > MRU_LENGTH) {
			recent.remove(MRU_LENGTH);
		}
		save();
	}
	
	/**
	 * Returns the list of recently used configuration names
	 * @return
	 */
	public List<String> get() {
		return recent;
	}

	// Save the list
	private void save() {
		IEclipsePreferences prefs = ConfigurationScope.INSTANCE.getNode(PLUGIN_ID);
		Preferences mruPrefs = prefs.node(MRU_PREFS);
		for (int i = 0; i < recent.size(); i++) {
			mruPrefs.put(((Integer) i).toString(), recent.get(i));
		}
		try {
			mruPrefs.flush();
		} catch (BackingStoreException e) {
			e.printStackTrace();
		}
	}
	
	// Load the list
	private void load() {
		recent = new ArrayList<String>();
		IEclipsePreferences prefs = ConfigurationScope.INSTANCE.getNode(PLUGIN_ID);
		Preferences mruPrefs = prefs.node(MRU_PREFS);
		try {
			String[] names = mruPrefs.keys();
			for (int i = 0; i < names.length && i < MRU_LENGTH; i++) {
				recent.add(mruPrefs.get(names[i], ""));
			}
		} catch (BackingStoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
