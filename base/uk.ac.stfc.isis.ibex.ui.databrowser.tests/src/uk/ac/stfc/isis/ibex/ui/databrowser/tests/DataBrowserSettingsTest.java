
/**
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

package uk.ac.stfc.isis.ibex.ui.databrowser.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.csstudio.trends.databrowser2.preferences.Preferences;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceStore;
import org.junit.Before;
import org.junit.Test;

import uk.ac.stfc.isis.ibex.instrument.InstrumentInfo;
import uk.ac.stfc.isis.ibex.ui.databrowser.DataBrowserSettings;

public class DataBrowserSettingsTest {

    // These settings represent the defaults, as set in
    // uk.ac.stfc.isis.ibex.product/plugin_customization.ini
    private final static String DEFAULT_ARCHIVE_SETTINGS = "RDB|1|jdbc\\:mysql\\://localhost/archive*RDB|2|jdbc\\:mysql\\://130.246.39.152/archive";
    private final static String DEFAULT_URLS_SETTINGS = "jdbc\\:mysql\\://localhost/archive*jdbc\\:mysql\\://130.246.39.152/archive";
    private final static String LARMOR_ARCHIVE_SETTINGS = "RDB|1|jdbc\\:mysql\\://NDXLARMOR/archive*RDB|2|jdbc\\:mysql\\://130.246.39.152/archive";
    private final static String LARMOR_URLS_SETTINGS = "jdbc\\:mysql\\://NDXLARMOR/archive*jdbc\\:mysql\\://130.246.39.152/archive";
    private final static String DEMO_ARCHIVE_SETTINGS = "RDB|1|jdbc\\:mysql\\://NDXDEMO/archive*RDB|2|jdbc\\:mysql\\://130.246.39.152/archive";
    private final static String DEMO_URLS_SETTINGS = "jdbc\\:mysql\\://NDXDEMO/archive*jdbc\\:mysql\\://130.246.39.152/archive";

    private final static String LOCALHOST = "localhost";
    private final static String NDXLARMOR = "NDXLARMOR";
    private final static String NDXDEMO = "NDXDEMO";
    private final static String NDXLARMOR_LOWERCASE = "NDXlarmor";
    private final static String NOT_A_HOST_NAME = "JDBC";
    private final static String IP_ADDRESS = "123.123.123.123";

    private IPreferenceStore preferenceStore;
    
    private DataBrowserSettings dataBrowserSettings;

    private InstrumentInfo mockLocalHost;
    private InstrumentInfo mockLarmor;
    private InstrumentInfo mockDemo;

    @Before
    public void setUp() {
        // Arrange
        preferenceStore = new PreferenceStore();
        
        preferenceStore.setValue(Preferences.ARCHIVES, DEFAULT_ARCHIVE_SETTINGS);
        preferenceStore.setValue(Preferences.URLS, DEFAULT_URLS_SETTINGS);
        
        dataBrowserSettings = new DataBrowserSettings(preferenceStore);

        mockLocalHost = mockInstrument(LOCALHOST);
        mockLarmor = mockInstrument(NDXLARMOR);
        mockDemo = mockInstrument(NDXDEMO);
    }

    private InstrumentInfo mockInstrument(String hostName) {
        InstrumentInfo returnedInstrument = mock(InstrumentInfo.class);
        when(returnedInstrument.hostName()).thenReturn(hostName);
        return returnedInstrument;
    }

    @Test
    public void default_archives_setting_is_set_correctly() {
        // Assert
        assertEquals(DEFAULT_ARCHIVE_SETTINGS, preferenceStore.getString(Preferences.ARCHIVES));
    }

    @Test
    public void default_urls_setting_is_set_correctly() {
        // Assert
        assertEquals(DEFAULT_URLS_SETTINGS, preferenceStore.getString(Preferences.URLS));
    }

    @Test
    public void swtiching_from_local_host_to_NDXLARMOR_updates_archives_settings() {
        // Act
        dataBrowserSettings.setInstrument(mockLarmor);

        // Assert
        assertEquals(LARMOR_ARCHIVE_SETTINGS, preferenceStore.getString(Preferences.ARCHIVES));
    }

    @Test
    public void swtiching_from_local_host_to_NDXLARMOR_updates_urls_settings() {
        // Act
        dataBrowserSettings.setInstrument(mockLarmor);

        // Assert
        assertEquals(LARMOR_URLS_SETTINGS, preferenceStore.getString(Preferences.URLS));
    }

    @Test
    public void swtiching_from_NDXLARMOR_to_NDXDEMO_updates_archives_settings() {
        // Act
        dataBrowserSettings.setInstrument(mockLarmor);
        dataBrowserSettings.setInstrument(mockDemo);

        // Assert
        assertEquals(DEMO_ARCHIVE_SETTINGS, preferenceStore.getString(Preferences.ARCHIVES));
    }

    @Test
    public void swtiching_from_NDXLARMOR_to_NDXDEMO_updates_urls_settings() {
        // Act
        dataBrowserSettings.setInstrument(mockLarmor);
        dataBrowserSettings.setInstrument(mockDemo);

        // Assert
        assertEquals(DEMO_URLS_SETTINGS, preferenceStore.getString(Preferences.URLS));
    }

    @Test
    public void swtiching_from_NDXLARMOR_to_local_host_updates_archives_settings() {
        // Act
        dataBrowserSettings.setInstrument(mockLarmor);
        dataBrowserSettings.setInstrument(mockLocalHost);

        // Assert
        assertEquals(DEFAULT_ARCHIVE_SETTINGS, preferenceStore.getString(Preferences.ARCHIVES));
    }

    @Test
    public void swtiching_from_NDXLARMOR_to_local_host_updates_urls_settings() {
        // Act
        dataBrowserSettings.setInstrument(mockLarmor);
        dataBrowserSettings.setInstrument(mockLocalHost);

        // Assert
        assertEquals(DEFAULT_URLS_SETTINGS, preferenceStore.getString(Preferences.URLS));
    }

    @Test
    public void switching_from_lowercase_ndxdemo_to_local_host_does_not_update_archives_settings() {
        // Act
        dataBrowserSettings.setInstrument(mockInstrument(NDXLARMOR_LOWERCASE));
        dataBrowserSettings.setInstrument(mockLocalHost);

        // Assert
        assertEquals(DEFAULT_ARCHIVE_SETTINGS.replace(NDXLARMOR, NDXLARMOR_LOWERCASE),
                preferenceStore.getString(Preferences.ARCHIVES));
    }

    @Test
    public void switching_from_lowercase_ndxdemo_to_local_host_does_not_update_urls_settings() {
        // Act
        dataBrowserSettings.setInstrument(mockInstrument(NDXLARMOR_LOWERCASE));
        dataBrowserSettings.setInstrument(mockLocalHost);

        // Assert
        assertEquals(DEFAULT_ARCHIVE_SETTINGS.replace(NDXLARMOR, NDXLARMOR_LOWERCASE),
                preferenceStore.getString(Preferences.ARCHIVES));
    }

    @Test(expected = RuntimeException.class)
    public void switching_from_invalid_host_name_to_local_host_throws_runtime_error() {
        // Act
        dataBrowserSettings.setInstrument(mockInstrument(NOT_A_HOST_NAME));
        dataBrowserSettings.setInstrument(mockLocalHost);

        // Assert
        assertFalse(DEFAULT_ARCHIVE_SETTINGS.equals(preferenceStore.getString(Preferences.ARCHIVES)));
    }

    @Test(expected = RuntimeException.class)
    public void switching_from_IP_host_name_to_local_host_throws_runtime_error() {
        // Act
        dataBrowserSettings.setInstrument(mockInstrument(IP_ADDRESS));
        dataBrowserSettings.setInstrument(mockLocalHost);

        // Assert
        assertFalse(DEFAULT_ARCHIVE_SETTINGS.equals(preferenceStore.getString(Preferences.ARCHIVES)));
    }
}
