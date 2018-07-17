
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.core.runtime.preferences.ConfigurationScope;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.prefs.Preferences;
/**
 * Class to manage the list of recently used configurations.
 *
 */
public class RecentConfigList {
    private static final String NUMBER_REGEX = "(\\d+)";
    private static final String SEPARATOR = "                                      Last loaded on: ";
	private List<String> recent;
	private List<String> recentNames = new ArrayList<String>();
	private List<String> recentTimestamps = new ArrayList<String>();
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
			if (getName(current).equals(getName(item))) {
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
     * Clears the list of recently used configuration names and time stamps of when they were last loaded.
     */
    public void clear() {
        recent.clear();
        save();
    }

	/**
	 * Returns the list of recently used configuration names and time stamps of when they were last loaded.
	 * 
     * @return 
     *          The list of recently used configuration names and time stamps of when they were last loaded.
     */
	public List<String> get() {
	    return recent;
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
            if (getName(current).equals(getName(item))) {
                toBeDeleted = current;
            }
        }
        if (!toBeDeleted.isEmpty()) {
            recent.remove(toBeDeleted);
        }
        save();
    }

	/**
	 * Returns the list of recently used configuration names.
	 * 
     * @return 
     *          The list of recently used configuration names.
     */
    public List<String> getNames() {
        recentNames.clear();
        for (String item : get()) {
            recentNames.add(getName(item));
        }
        return recentNames;
    }

	/**
	 * Returns the list of time stamps at which recently used configurations were last loaded.
	 * 
     * @return 
     *          The list of time stamps at which recently used configurations were last loaded.
     */
    public List<String> getTimestamps() {
        recentTimestamps.clear();
        for (String item : get()) {
            recentTimestamps.add(getTimestamp(item));
        }
        return recentTimestamps;
    }
    
    /**
     * Creates a matcher for the time stamp pattern. This allows to find the time stamp in a string with names and time stamps.
     * 
     * @param item 
     *              The original string containing a name and a time stamp.
     * @return
     *              The matcher matched to the time stamp pattern.
     */
    private Matcher getMatcher(String item, boolean first) {
        Pattern pattern;
        if (first) {
            String pat = SEPARATOR + NUMBER_REGEX + "-" + NUMBER_REGEX + "-" + NUMBER_REGEX + " "
                    + NUMBER_REGEX + ":" + NUMBER_REGEX + ":" + NUMBER_REGEX;
            pattern = Pattern.compile("(" + pat + ")");
        } else {
            String pat = NUMBER_REGEX + "-" + NUMBER_REGEX + "-" + NUMBER_REGEX + " "
                    + NUMBER_REGEX + ":" + NUMBER_REGEX + ":" + NUMBER_REGEX;
            pattern = Pattern.compile("(" + pat + ")");
        }
        return pattern.matcher(item);
    }
	
	/**
     * Returns the name minus the time stamp at which recently used configurations were last loaded.
     * 
     * @param item
     *            The original string containing a name and a time stamp.
     * @return 
     *            The name without the time stamp at which the item was last loaded.
     */
    public String getName(String item) {
        Matcher match = getMatcher(item, true);
        String name = item;
        if (match.find()) {
            name = item.replace(match.group(1), "");
        }
        return name;
    }
    
    /**
     * Returns the time stamp at which recently used configurations were last loaded.
     * 
     * @param item
     *            The original string containing a name and a time stamp.
     * @return 
     *            The time stamp at which the item was last loaded (trimmed to remove the space separating the name and time stamp).
     */
    public String getTimestamp(String item) {
        Matcher match = getMatcher(item, true);
        Matcher secondMatch = getMatcher(item, false);
        return findPattern(secondMatch, findPattern(match, item));
    }
    
    /**
     * A method used to find a pattern in a string.
     * @param match
     *              The matcher used to find the pattern.
     * @param item
     *              The original string.
     * @return
     *              The pattern to be returned.
     */
    private String findPattern(Matcher match, String item) {
        String pattern = item;
        if (match.find()) {
            pattern = match.group(1).trim();
        }
        return pattern;
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
