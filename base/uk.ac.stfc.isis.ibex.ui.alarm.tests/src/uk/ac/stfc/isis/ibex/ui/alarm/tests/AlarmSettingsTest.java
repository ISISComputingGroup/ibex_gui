
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

package uk.ac.stfc.isis.ibex.ui.alarm.tests;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import org.csstudio.trends.databrowser2.preferences.Preferences;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceStore;
import org.junit.Before;
import org.junit.Test;

import uk.ac.stfc.isis.ibex.instrument.InstrumentInfo;
import uk.ac.stfc.isis.ibex.ui.logplotter.LogPlotterSettings;

@SuppressWarnings("checkstyle:methodname")
public class LogPlotterSettingsTest {

    /**
     * Localhost instrument name.
     */
    private static final String LOCALHOST = "localhost";

    /**
     * Larmor instrument name.
     */
    private static final String NDXLARMOR = "NDXLARMOR";

    /**
     * Demo instrument name.
     */
    private static final String NDXDEMO = "NDXDEMO";

    /**
     * Larmor instrument name with larmor in lower case.
     */
    private static final String NDXLARMOR_LOWERCASE = "NDXlarmor";

    /**
     * Custom instrument name.
     */
    private static final String NDWCUSTOM = "NDWCUSTOM";

    /**
     * Generic IP address.
     */
    private static final String IP_ADDRESS = "123.123.123.123";

    /**
     * Instrument name not conforming to standard ISIS naming convention.
     */
    private static final String NON_ISIS_INST_NAME = "JOES_PC";

    /**
     * @param inst_name The instrument name
     * @return The archive settings string corresponding to the given
     *         instrument.
     */
    private static String BuildArchiveSettings(String inst_name) {
        return "RDB|1|jdbc\\:mysql\\://" + inst_name + "/archive*RDB|2|jdbc\\:mysql\\://130.246.39.152/archive";
    }

    /**
     * @param inst_name The instrument name
     * @return The URLs settings string corresponding to the given instrument.
     */
    private static String BuildUrlsSettings(String inst_name) {
        return "jdbc\\:mysql\\://" + inst_name + "/archive*jdbc\\:mysql\\://130.246.39.152/archive";
    }

    // These settings represent the defaults, as set in
    // uk.ac.stfc.isis.ibex.product/plugin_customization.ini

    /**
     * Archive settings for localhost.
     */
    private static final String LOCALHOST_ARCHIVE_SETTINGS = BuildArchiveSettings(LOCALHOST);

    /**
     * URL settings for localhost.
     */
    private static final String LOCALHOST_URLS_SETTINGS = BuildUrlsSettings(LOCALHOST);

    /**
     * Archive settings for larmor.
     */
    private static final String LARMOR_ARCHIVE_SETTINGS = BuildArchiveSettings(NDXLARMOR);

    /**
     * URL settings for larmor.
     */
    private static final String LARMOR_URLS_SETTINGS = BuildUrlsSettings(NDXLARMOR);

    /**
     * Archive settings for demo.
     */
    private static final String DEMO_ARCHIVE_SETTINGS = BuildArchiveSettings(NDXDEMO);

    /**
     * URL settings for demo.
     */
    private static final String DEMO_URLS_SETTINGS = BuildUrlsSettings(NDXDEMO);

    /**
     * Archive settings for a custom instrument.
     */
    private static final String CUSTOM_ARCHIVE_SETTINGS = BuildArchiveSettings(NDWCUSTOM);

    /**
     * URL settings for a custom instrument.
     */
    private static final String CUSTOM_URLS_SETTINGS = BuildUrlsSettings(NDWCUSTOM);

    /**
     * Archive settings for an instrument not conforming to standard ISIS naming
     * convention.
     */
    private static final String NON_ISIS_INST_ARCHIVE_SETTINGS = BuildArchiveSettings(NON_ISIS_INST_NAME);

    /**
     * URL settings for an instrument not conforming to standard ISIS naming
     * convention.
     */
    private static final String NON_ISIS_INST_URLS_SETTINGS = BuildUrlsSettings(NON_ISIS_INST_NAME);

    /**
     * Archive settings for an instrument in IP address format.
     */
    private static final String IP_ARCHIVE_SETTINGS = BuildArchiveSettings(IP_ADDRESS);

    /**
     * URL settings for an instrument in IP address format.
     */
    private static final String IP_URLS_SETTINGS = BuildUrlsSettings(IP_ADDRESS);

    /**
     * Archive settings for the default instrument (currently localhost).
     */
    private static final String DEFAULT_ARCHIVE_SETTINGS = LOCALHOST_ARCHIVE_SETTINGS;

    /**
     * URL settings for the default instrument (currently localhost).
     */
    private static final String DEFAULT_URLS_SETTINGS = LOCALHOST_URLS_SETTINGS;

    private IPreferenceStore preferenceStore;
    
    private LogPlotterSettings logPlotterSettings;

    private InstrumentInfo mockLocalHost;
    private InstrumentInfo mockLarmor;
    private InstrumentInfo mockDemo;
    private InstrumentInfo mockCustom;

    @Before
    public void setUp() {
        // Arrange
        preferenceStore = new PreferenceStore();
        
        preferenceStore.setValue(Preferences.ARCHIVES, DEFAULT_ARCHIVE_SETTINGS);
        preferenceStore.setValue(Preferences.URLS, DEFAULT_URLS_SETTINGS);
        
        logPlotterSettings = new LogPlotterSettings(preferenceStore);

        mockLocalHost = mockInstrument(LOCALHOST);
        mockLarmor = mockInstrument(NDXLARMOR);
        mockDemo = mockInstrument(NDXDEMO);
        mockCustom = mockInstrument(NDWCUSTOM);
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
    public void switching_from_local_host_to_NDXLARMOR_updates_archives_settings() {
        // Act
        logPlotterSettings.setInstrument(mockLarmor, mockLocalHost);

        // Assert
        assertEquals(LARMOR_ARCHIVE_SETTINGS, preferenceStore.getString(Preferences.ARCHIVES));
    }

    @Test
    public void switching_from_local_host_to_NDXLARMOR_updates_urls_settings() {
        // Act
        logPlotterSettings.setInstrument(mockLarmor, mockLocalHost);

        // Assert
        assertEquals(LARMOR_URLS_SETTINGS, preferenceStore.getString(Preferences.URLS));
    }


    @Test
    public void switching_from_NDXLARMOR_to_NDXDEMO_updates_archives_settings() {
        // Act
        logPlotterSettings.setInstrument(mockLarmor, mockLocalHost);
        logPlotterSettings.setInstrument(mockDemo, mockLarmor);

        // Assert
        assertEquals(DEMO_ARCHIVE_SETTINGS, preferenceStore.getString(Preferences.ARCHIVES));
    }

    @Test
    public void switching_from_NDXLARMOR_to_NDXDEMO_updates_urls_settings() {
        // Act
        logPlotterSettings.setInstrument(mockLarmor, mockLocalHost);
        logPlotterSettings.setInstrument(mockDemo, mockLarmor);

        // Assert
        assertEquals(DEMO_URLS_SETTINGS, preferenceStore.getString(Preferences.URLS));
    }

    @Test
    public void switching_from_NDXLARMOR_to_local_host_updates_archives_settings() {
        // Act
        logPlotterSettings.setInstrument(mockLarmor, mockLocalHost);
        logPlotterSettings.setInstrument(mockLocalHost, mockLarmor);

        // Assert
        assertEquals(DEFAULT_ARCHIVE_SETTINGS, preferenceStore.getString(Preferences.ARCHIVES));
    }

    @Test
    public void switching_from_NDXLARMOR_to_local_host_updates_urls_settings() {
        // Act
        logPlotterSettings.setInstrument(mockLarmor, mockLocalHost);
        logPlotterSettings.setInstrument(mockLocalHost, mockLarmor);

        // Assert
        assertEquals(DEFAULT_URLS_SETTINGS, preferenceStore.getString(Preferences.URLS));
    }

    @Test
    public void switching_from_local_host_to_NDWCUSTOM_updates_archives_settings() {
        // Act
        logPlotterSettings.setInstrument(mockCustom, mockLocalHost);

        // Assert
        assertEquals(CUSTOM_ARCHIVE_SETTINGS, preferenceStore.getString(Preferences.ARCHIVES));
    }

    @Test
    public void switching_from_local_host_to_NDWCUSTOM_updates_urls_settings() {
        // Act
        logPlotterSettings.setInstrument(mockCustom, mockLocalHost);

        // Assert
        assertEquals(CUSTOM_URLS_SETTINGS, preferenceStore.getString(Preferences.URLS));
    }

    @Test
    public void switching_from_NDWCUSTOM_to_NDXLARMOR_updates_archives_settings() {
        // Act
        logPlotterSettings.setInstrument(mockCustom, mockLocalHost);
        logPlotterSettings.setInstrument(mockLarmor, mockCustom);

        // Assert
        assertEquals(LARMOR_ARCHIVE_SETTINGS, preferenceStore.getString(Preferences.ARCHIVES));
    }

    @Test
    public void switching_from_NDWCUSTOM_to_NDXLARMOR_updates_urls_settings() {
        // Act
        logPlotterSettings.setInstrument(mockCustom, mockLocalHost);
        logPlotterSettings.setInstrument(mockLarmor, mockCustom);

        // Assert
        assertEquals(LARMOR_URLS_SETTINGS, preferenceStore.getString(Preferences.URLS));
    }

    @Test
    public void switching_from_lowercase_ndxdemo_to_local_host_does_not_update_archives_settings() {
        // Act
        logPlotterSettings.setInstrument(mockInstrument(NDXLARMOR_LOWERCASE), mockLocalHost);
        logPlotterSettings.setInstrument(mockLocalHost, mockInstrument(NDXLARMOR_LOWERCASE));

        // Assert
        assertEquals(DEFAULT_ARCHIVE_SETTINGS.replace(NDXLARMOR, NDXLARMOR_LOWERCASE),
                preferenceStore.getString(Preferences.ARCHIVES));
    }

    @Test
    public void switching_from_lowercase_ndxdemo_to_local_host_does_not_update_urls_settings() {
        // Act
        logPlotterSettings.setInstrument(mockInstrument(NDXLARMOR_LOWERCASE), mockLocalHost);
        logPlotterSettings.setInstrument(mockLocalHost, mockInstrument(NDXLARMOR_LOWERCASE));

        // Assert
        assertEquals(DEFAULT_ARCHIVE_SETTINGS.replace(NDXLARMOR, NDXLARMOR_LOWERCASE),
                preferenceStore.getString(Preferences.ARCHIVES));
    }

    @Test
    public void switching_from_local_host_to_non_ISIS_instrument_name_updates_urls_settings() {
        // Act
        logPlotterSettings.setInstrument(mockInstrument(NON_ISIS_INST_NAME), mockLocalHost);

        // Assert
        assertEquals(NON_ISIS_INST_URLS_SETTINGS, preferenceStore.getString(Preferences.URLS));
    }

    @Test
    public void switching_from_local_host_to_non_ISIS_instrument_name_updates_archives_settings() {
        // Act
        logPlotterSettings.setInstrument(mockInstrument(NON_ISIS_INST_NAME), mockLocalHost);

        // Assert
        assertEquals(NON_ISIS_INST_ARCHIVE_SETTINGS, preferenceStore.getString(Preferences.ARCHIVES));
    }

    @Test
    public void switching_from_local_host_to_IP_host_name_updates_urls_settings() {
        // Act
        logPlotterSettings.setInstrument(mockInstrument(IP_ADDRESS), mockLocalHost);

        // Assert
        assertEquals(IP_URLS_SETTINGS, preferenceStore.getString(Preferences.URLS));
    }

    @Test
    public void switching_from_local_host_to_IP_host_name_updates_archives_settings() {
        // Act
        logPlotterSettings.setInstrument(mockInstrument(IP_ADDRESS), mockLocalHost);

        // Assert
        assertEquals(IP_ARCHIVE_SETTINGS, preferenceStore.getString(Preferences.ARCHIVES));
    }

    @Test
    public void switching_from_local_host_to_local_host_causes_no_change_to_urls_settings() {
        // Act
        logPlotterSettings.setInstrument(mockLocalHost, mockLocalHost);

        // Assert
        assertEquals(LOCALHOST_URLS_SETTINGS, preferenceStore.getString(Preferences.URLS));
    }

    @Test
    public void switching_from_local_host_to_local_host_causes_no_change_to_archives_settings() {
        // Act
        logPlotterSettings.setInstrument(mockLocalHost, mockLocalHost);

        // Assert
        assertEquals(LOCALHOST_ARCHIVE_SETTINGS, preferenceStore.getString(Preferences.ARCHIVES));
    }

    @Test
    public void switching_from_larmor_to_larmor_causes_no_change_to_urls_settings() {
        // Act
        logPlotterSettings.setInstrument(mockLarmor, mockLocalHost);
        String expectedUrls = preferenceStore.getString(Preferences.URLS);
        logPlotterSettings.setInstrument(mockLarmor, mockLarmor);
        String actualUrls = preferenceStore.getString(Preferences.URLS);

        // Assert
        assertEquals(actualUrls, expectedUrls);
    }

    @Test
    public void switching_from_larmor_to_larmor_causes_no_change_to_archives_settings() {
        // Act
        logPlotterSettings.setInstrument(mockLarmor, mockLocalHost);
        String expectedArchives = preferenceStore.getString(Preferences.ARCHIVES);
        logPlotterSettings.setInstrument(mockLarmor, mockLarmor);
        String actualArchives = preferenceStore.getString(Preferences.ARCHIVES);

        // Assert
        assertEquals(actualArchives, expectedArchives);
    }

    @Test(expected = RuntimeException.class)
    public void switching_from_localhost_to_larmor_with_demo_as_old_instrument_raises_RuntimeException() {
        // Act
        logPlotterSettings.setInstrument(mockLarmor, mockDemo);
    }

    @Test(expected = RuntimeException.class)
    public void switching_from_localhost_to_larmor_with_demo_as_old_instrument_does_not_change_urls_settings() {
        // Act
        logPlotterSettings.setInstrument(mockLarmor, mockDemo);

        // Assert
        assertEquals(LOCALHOST_URLS_SETTINGS, preferenceStore.getString(Preferences.URLS));
    }

    @Test(expected = RuntimeException.class)
    public void switching_from_localhost_to_larmor_with_demo_as_old_instrument_does_not_change_archives_settings() {
        // Act
        logPlotterSettings.setInstrument(mockLarmor, mockDemo);

        // Assert
        assertEquals(LOCALHOST_ARCHIVE_SETTINGS, preferenceStore.getString(Preferences.ARCHIVES));
    }
}
