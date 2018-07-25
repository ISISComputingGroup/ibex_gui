
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
import java.util.Collection;
import java.util.List;

import org.eclipse.core.runtime.preferences.ConfigurationScope;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.prefs.Preferences;

import uk.ac.stfc.isis.ibex.configserver.configuration.ConfigInfo;


/**
 * Class to manage the list of recently used configurations.
 *
 */
public class RecentConfigList {
    /** The configuration server object. */
	private List<String> recent;
	private static final int MRU_LENGTH = 6;
	private static final String MRU_PREFS = "config-mru-slot-";
	private static final String PLUGIN_ID = "uk.ac.stfc.isis.ibex.configserver";

	/**
     * Constructor - loads the saved list from preferences.
     */
	public RecentConfigList() {
		load();
	}

	/**
     * Add an item.
     * 
     * @param item the configuration item to add to the list
     */
	public void add(String item) {
		// Is it already in the list?
		boolean first = true;
		for (String current : recent) {
			if (current.equals(item)) {
				if (!first) {
					// Move to first in the list
					recent.remove(current);
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
	 * Allows to convert a list of configuration names to a collection of information on the configurations.
	 * 
	 * @param configNames
	 *                 The list of configuration names.
	 * @param configsInServer
	 *                 The collection of information on the configurations in the server.
	 * @return
	 *                 The collection of information on the configurations passed to the method.
	 */
	public Collection<ConfigInfo> configNamesToConfigInfos(List<String> configNames, Collection<ConfigInfo> configsInServer) {
        Collection<ConfigInfo> recentConfigs = new ArrayList<ConfigInfo>();
        for (String configName : configNames) {
            for (ConfigInfo config : configsInServer) {
                if (config.name().equals(configName)) {
                    recentConfigs.add(config);
                }
            }
        }
	    return recentConfigs;
	}

	/**
	 * Gets the history of the passed configuration.
	 * 
	 * @param config
	 *             The configuration whose history we want to know about.
	 * @return
	 *             The history.
	 */
	private Collection<String> getHistory(ConfigInfo config) {
	    return config.getHistory();
	}

	/**
	 * Gets the date at which the passed configuration was last modified. This is the last date in it's history.
	 * 
	 * @param config
	 *             The configuration which we want to know about.
	 * @return
	 *             The time stamp of when the configuration was last modified.
	 */
	public String getLastModified(ConfigInfo config) {
	    Collection<String> history = getHistory(config);
	    String timestamp = "";
	    if (!history.isEmpty()) {
    	    for (String date : history) {
    	            timestamp = date;
    	    }
	    }
	    return timestamp;
	}

	/**
     * Clears the list of recently used configuration names and time stamps of when they were last loaded.
     */
    public void clear() {
        recent.clear();
        save();
    }

    /**
     * Returns the list of recently used configuration names without the current configuration.
     * 
     * @param configsInServer
     *                 The collection of information on the configurations in the server.
     * @return
     *          The list of recently used configuration names without the current configuration.
     */
    public List<String> getWithoutCurrent(Collection<ConfigInfo> configsInServer) {
        List<String> recentWithoutCurrent = (List<String>) ConfigInfo.namesWithoutCurrent(configNamesToConfigInfos(recent, configsInServer));
        return recentWithoutCurrent;
    }

    /**
     * Returns the list of time stamps of the recently used configuration without that of the current configuration.
     * 
     * @param configsInServer
     *                 The collection of information on the configurations in the server.
     * @return
     *          The list of time stamps of the recently used configuration without that of the current configuration.
     */
    public List<String> getLastModifiedTimestampsWithoutCurrent(Collection<ConfigInfo> configsInServer) {
        List<String> timestamps = new ArrayList<String>();
        Collection<ConfigInfo> configsWithoutCurrent = configNamesToConfigInfos(getWithoutCurrent(configsInServer), configsInServer);
        for (ConfigInfo config : configsWithoutCurrent) {
            timestamps.add(getLastModified(config));
        }
        return timestamps;
    }

	/**
	 * Returns the list of recently used configuration names.
	 * 
     * @return 
     *          The list of recently used configuration names.
     */
	public List<String> get() {
	    return recent;
	}

	/**
     * Returns the list of time stamps of the recently used configuration.
     * 
     * @param configsInServer
     *                 The collection of information on the configurations in the server.
     * @return
     *          The list of time stamps of the recently used configuration.
     */
    public List<String> getLastModifiedTimestamps(Collection<ConfigInfo> configsInServer) {
        List<String> timestamps = new ArrayList<String>();
        Collection<ConfigInfo> configs = configNamesToConfigInfos(recent, configsInServer);
        for (ConfigInfo config : configs) {
            timestamps.add(getLastModified(config));
        }
        return timestamps;
    }

	/**
     * Removes an item from list of recently used configuration names and time stamps of when they were last loaded.
     * 
     * @param item 
     *              The item to remove.
     */
    public void remove(String item) {
        String toBeDeleted = "";
        for (String current : recent) {
            if (current.equals(item)) {
                toBeDeleted = current;
            }
        }
        if (!toBeDeleted.isEmpty()) {
            recent.remove(toBeDeleted);
        }
        save();
    }

    /**
     * Save the list.
     */
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

    /**
     * Load the list.
     */
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
			e.printStackTrace();
		}
	}
}
