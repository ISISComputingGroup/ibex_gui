
/*
* This file is part of the ISIS IBEX application.
* Copyright (C) 2012-2021 Science & Technology Facilities Council.
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

package uk.ac.stfc.isis.ibex.e4.ui.perspectiveswitcher;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.preferences.ConfigurationScope;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.IPreferencesService;
import org.osgi.service.prefs.BackingStoreException;

import uk.ac.stfc.isis.ibex.logger.IsisLog;
import uk.ac.stfc.isis.ibex.logger.LoggerUtils;
import org.apache.logging.log4j.Logger;

/**
 * Supplies the details for the IBEX preference page.
 */
public class PerspectivePreferenceSupplier {
    
    private static final Logger LOG = IsisLog.getLogger(PerspectivePreferenceSupplier.class);
    
    private final IPreferencesService preferenceService;
    
    private final IEclipsePreferences writablePreferences =
            ConfigurationScope.INSTANCE.getNode("uk.ac.stfc.isis.ibex.preferences");
    
    /**
     * Instantiate a new preference supplier.
     */
    public PerspectivePreferenceSupplier() {
        this(Platform.getPreferencesService());
    }
    
    /**
     * Instantiate a new preference supplier based on a specific preferenceService.
     * @param preferenceService the preference service to use
     */
    public PerspectivePreferenceSupplier(IPreferencesService preferenceService) {
        this.preferenceService = preferenceService;
    }

    /**
     * The overall preferences name.
     */
    private static final String PREFERENCE_NODE = "uk.ac.stfc.isis.ibex.preferences";
    
    /**
     * Defines which perspectives to hide.
     */
    private static final String PERSPECTIVES_TO_HIDE = "perspectives_not_shown";
    
    /**
     * Defines which perspectives to hide by default.
     */
    private static final String DEFAULT_PERSPECTIVES_TO_HIDE = "";

    /**
     * Defines whether to use the local perspective settings or those from the server.
     */
    private static final String USE_LOCAL_PERSPECTIVES = "use_local_perspectives";
    
    /**
     * Gets a string from the IBEX preference store.
     * 
     * @return the preferences string, or the default if it was not present.
     */
    private String getString(String name, String def) {
        return preferenceService.getString(PREFERENCE_NODE, name, def, null);
    }
    
    /**
     * Gets a boolean from the IBEX preference store.
     * 
     * @return the preferences boolean, or the default if it was not present.
     */
    private boolean getBoolean(String name, boolean def) {
        return preferenceService.getBoolean(PREFERENCE_NODE, name, def, null);
    }
    
    /**
     * Gets a list of perspective IDs which should not be shown.
     * 
     * @return a list of perspective IDs which should not be shown (may be empty, but never null).
     */
    public List<String> perspectivesToHide() {
        String preferencesString = getString(PERSPECTIVES_TO_HIDE, DEFAULT_PERSPECTIVES_TO_HIDE);
        if (preferencesString == null || preferencesString.isEmpty()) {
            return new ArrayList<>();
        }
        return Arrays.asList(preferencesString.split(",")).stream().map(String::trim).collect(Collectors.toList());
    }
    
    /**
     * Set which perspectives are hidden to the user for this client.
     * 
     * @param preferences a list of perspective IDs which should not be shown
     */
    public void setPerspectivesToHide(List<String> preferences) {
        writablePreferences.put(PERSPECTIVES_TO_HIDE, preferences.stream().collect(Collectors.joining(",")));
        try {
            writablePreferences.flush();
        } catch (BackingStoreException e) {
            LoggerUtils.logErrorWithStackTrace(LOG, "Unable to set perspectives to hide", e);
        }
    }
    
    /**
     * Adds a listener to be notified when hidden perspectives change.
     * @param listener the listener
     */
    public void addHiddenPerspectivesListener(Consumer<List<String>> listener) {
        writablePreferences.addPreferenceChangeListener(event -> {
            if (event.getKey().equals(PERSPECTIVES_TO_HIDE)) {
                listener.accept(perspectivesToHide());
            }
        });
    }
    
    /**
     * Whether the local perspectives should be used.
     * @return true if using local perspectives; false otherwise
     */
    public Boolean getUseLocalPerspectives() {
        writablePreferences.addPreferenceChangeListener(event -> {
            System.out.println(event);
        });
        return getBoolean(USE_LOCAL_PERSPECTIVES, false);
    }
    
    /**
     * Set whether the local perspectives should be used.
     * @param useLocal if using local perspectives; false otherwise
     */
    public void setUseLocalPerspectives(Boolean useLocal) {
        writablePreferences.put(USE_LOCAL_PERSPECTIVES, useLocal.toString());
        try {
            writablePreferences.flush();
        } catch (BackingStoreException e) {
            LoggerUtils.logErrorWithStackTrace(LOG, "Unable to set to use local perspective settings", e);
        }
    }
    
    /**
     * Adds a listener to be notified when the preference to use local perspectives changes.
     * @param listener the listener
     */
    public void addUseLocalPerspectivesListener(Consumer<Boolean> listener) {
        writablePreferences.addPreferenceChangeListener(event -> {
            if (event.getKey().equals(USE_LOCAL_PERSPECTIVES)) {
                listener.accept(getUseLocalPerspectives());
            }
        });
    }    
}
