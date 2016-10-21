
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
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import org.csstudio.alarm.beast.Preferences;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceStore;
import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import uk.ac.stfc.isis.ibex.instrument.InstrumentInfo;
import uk.ac.stfc.isis.ibex.ui.alarm.AlarmSettings;

@SuppressWarnings("checkstyle:methodname")
public class AlarmSettingsTest {

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
    private static String BuildRdbUrl(String inst_name) {
        return "jdbc:mysql://" + inst_name + "/ALARM";
    }

    /**
     * @param inst_name The instrument name
     * @return The URLs settings string corresponding to the given instrument.
     */
    private static String BuildJmsUrl(String inst_name) {
        return "failover:(tcp://" + inst_name + ":61616)";
    }

    // These settings represent the defaults, as set in
    // uk.ac.stfc.isis.ibex.product/plugin_customization.ini

    /**
     * RDB URL for localhost.
     */
    private static final String LOCALHOST_RDB_URL = BuildRdbUrl(LOCALHOST);

    /**
     * JMS URL for localhost.
     */
    private static final String LOCALHOST_JMS_URL = BuildJmsUrl(LOCALHOST);

    /**
     * RDB URL for larmor.
     */
    private static final String LARMOR_RDB_URL = BuildRdbUrl(NDXLARMOR);

    /**
     * JMS URL for larmor.
     */
    private static final String LARMOR_JMS_URL = BuildJmsUrl(NDXLARMOR);

    /**
     * RDB URL for demo.
     */
    private static final String DEMO_RDB_URL = BuildRdbUrl(NDXDEMO);

    /**
     * JMS URL for demo.
     */
    private static final String DEMO_JMS_URL = BuildJmsUrl(NDXDEMO);

    /**
     * RDB URL for a custom instrument.
     */
    private static final String CUSTOM_RDB_URL = BuildRdbUrl(NDWCUSTOM);

    /**
     * JMS URL for a custom instrument.
     */
    private static final String CUSTOM_JMS_URL = BuildJmsUrl(NDWCUSTOM);

    /**
     * RDB URL for an instrument not conforming to standard ISIS naming
     * convention.
     */
    private static final String NON_ISIS_INST_RDB_URL = BuildRdbUrl(NON_ISIS_INST_NAME);

    /**
     * JMS URL for an instrument not conforming to standard ISIS naming
     * convention.
     */
    private static final String NON_ISIS_INST_JMS_URL = BuildJmsUrl(NON_ISIS_INST_NAME);

    /**
     * RDB URL for an instrument in IP address format.
     */
    private static final String IP_RDB_URL = BuildRdbUrl(IP_ADDRESS);

    /**
     * JMS URL for an instrument in IP address format.
     */
    private static final String IP_JMS_URL = BuildJmsUrl(IP_ADDRESS);

    /**
     * RDB URL for the default instrument (currently localhost).
     */
    private static final String DEFAULT_RDB_URL = LOCALHOST_RDB_URL;

    /**
     * JMS URL for the default instrument (currently localhost).
     */
    private static final String DEFAULT_JMS_URL = LOCALHOST_JMS_URL;
    
    /**
     * Log plotter settings instance to use for tests.
     */
    private AlarmSettings alarmSettings;

    /**
     * Mock instrument with host name localhost.
     */
    private InstrumentInfo mockLocalHost;

    /**
     * Mock instrument with host name larmor.
     */
    private InstrumentInfo mockLarmor;
    /**
     * Mock instrument with host name demo.
     */
    private InstrumentInfo mockDemo;

    /**
     * Mock instrument with custom instrument host name.
     */
    private InstrumentInfo mockCustom;

    /**
     * Preference store.
     */
    private IPreferenceStore preferenceStore;

    /**
     * Return value for mocked preference store jms value.
     */
    private String mock_jms_url;

    /**
     * Return value for mocked preference store rdb value.
     */
    private String mock_rdb_url;

    /**
     * Set up procedure to run before tests.
     */
    @Before
    public void setUp() {
        // Arrange
        mock_rdb_url = DEFAULT_RDB_URL;
        mock_jms_url = DEFAULT_JMS_URL;

        preferenceStore = mock(PreferenceStore.class);
        doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                mock_rdb_url = (String) invocation.getArguments()[1];
                return null;
            }
        }).when(preferenceStore).setValue(eq(Preferences.RDB_URL), anyString());
        doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                mock_jms_url = (String) invocation.getArguments()[1];
                return null;
            }
        }).when(preferenceStore).setValue(eq(Preferences.JMS_URL), anyString());
        doAnswer(new Answer<String>() {
            @Override
            public String answer(InvocationOnMock invocation) throws Throwable {
                return mock_jms_url;
            }
        }).when(preferenceStore).getString(eq(Preferences.JMS_URL));
        doAnswer(new Answer<String>() {
            @Override
            public String answer(InvocationOnMock invocation) throws Throwable {
                return mock_rdb_url;
            }
        }).when(preferenceStore).getString(eq(Preferences.RDB_URL));

        alarmSettings = new AlarmSettings(preferenceStore);

        mockLocalHost = mockInstrument(LOCALHOST);
        mockLarmor = mockInstrument(NDXLARMOR);
        mockDemo = mockInstrument(NDXDEMO);
        mockCustom = mockInstrument(NDWCUSTOM);
    }

    /**
     * Generate a mock instrument based on a given host name.
     * 
     * @param hostName Host name of mock instrument
     * @return An instrumentInfo object with host name same as input parameter
     */
    private InstrumentInfo mockInstrument(String hostName) {
        InstrumentInfo returnedInstrument = mock(InstrumentInfo.class);
        when(returnedInstrument.hostName()).thenReturn(hostName);
        return returnedInstrument;
    }

    @Test
    public void default_rdb_url_is_set_correctly() {
        // Assert
        assertEquals(DEFAULT_RDB_URL, preferenceStore.getString(Preferences.RDB_URL));
    }

    @Test
    public void default_jms_url_is_set_correctly() {
        // Assert
        assertEquals(DEFAULT_JMS_URL, preferenceStore.getString(Preferences.JMS_URL));
    }

    @Test
    public void switching_from_local_host_to_NDXLARMOR_updates_rdb_url() {
        // Act
        alarmSettings.setInstrument(mockLarmor, mockLocalHost);

        // Assert
        assertEquals(LARMOR_RDB_URL, preferenceStore.getString(Preferences.RDB_URL));
    }

    @Test
    public void switching_from_local_host_to_NDXLARMOR_updates_jms_url() {
        // Act
        alarmSettings.setInstrument(mockLarmor, mockLocalHost);

        // Assert
        assertEquals(LARMOR_JMS_URL, preferenceStore.getString(Preferences.JMS_URL));
    }


    @Test
    public void switching_from_NDXLARMOR_to_NDXDEMO_updates_rdb_url() {
        // Act
        alarmSettings.setInstrument(mockLarmor, mockLocalHost);
        alarmSettings.setInstrument(mockDemo, mockLarmor);

        // Assert
        assertEquals(DEMO_RDB_URL, preferenceStore.getString(Preferences.RDB_URL));
    }

    @Test
    public void switching_from_NDXLARMOR_to_NDXDEMO_updates_jms_url() {
        // Act
        alarmSettings.setInstrument(mockLarmor, mockLocalHost);
        alarmSettings.setInstrument(mockDemo, mockLarmor);

        // Assert
        assertEquals(DEMO_JMS_URL, preferenceStore.getString(Preferences.JMS_URL));
    }

    @Test
    public void switching_from_NDXLARMOR_to_local_host_updates_rdb_url() {
        // Act
        alarmSettings.setInstrument(mockLarmor, mockLocalHost);
        alarmSettings.setInstrument(mockLocalHost, mockLarmor);

        // Assert
        assertEquals(DEFAULT_RDB_URL, preferenceStore.getString(Preferences.RDB_URL));
    }

    @Test
    public void switching_from_NDXLARMOR_to_local_host_updates_jms_url() {
        // Act
        alarmSettings.setInstrument(mockLarmor, mockLocalHost);
        alarmSettings.setInstrument(mockLocalHost, mockLarmor);

        // Assert
        assertEquals(DEFAULT_JMS_URL, preferenceStore.getString(Preferences.JMS_URL));
    }

    @Test
    public void switching_from_local_host_to_NDWCUSTOM_updates_rdb_url() {
        // Act
        alarmSettings.setInstrument(mockCustom, mockLocalHost);

        // Assert
        assertEquals(CUSTOM_RDB_URL, preferenceStore.getString(Preferences.RDB_URL));
    }

    @Test
    public void switching_from_local_host_to_NDWCUSTOM_updates_jms_url() {
        // Act
        alarmSettings.setInstrument(mockCustom, mockLocalHost);

        // Assert
        assertEquals(CUSTOM_JMS_URL, preferenceStore.getString(Preferences.JMS_URL));
    }

    @Test
    public void switching_from_NDWCUSTOM_to_NDXLARMOR_updates_rdb_url() {
        // Act
        alarmSettings.setInstrument(mockCustom, mockLocalHost);
        alarmSettings.setInstrument(mockLarmor, mockCustom);

        // Assert
        assertEquals(LARMOR_RDB_URL, preferenceStore.getString(Preferences.RDB_URL));
    }

    @Test
    public void switching_from_NDWCUSTOM_to_NDXLARMOR_updates_jms_url() {
        // Act
        alarmSettings.setInstrument(mockCustom, mockLocalHost);
        alarmSettings.setInstrument(mockLarmor, mockCustom);

        // Assert
        assertEquals(LARMOR_JMS_URL, preferenceStore.getString(Preferences.JMS_URL));
    }

    @Test
    public void switching_from_lowercase_ndxdemo_to_local_host_does_not_update_rdb_url() {
        // Act
        alarmSettings.setInstrument(mockInstrument(NDXLARMOR_LOWERCASE), mockLocalHost);
        alarmSettings.setInstrument(mockLocalHost, mockInstrument(NDXLARMOR_LOWERCASE));

        // Assert
        assertEquals(DEFAULT_RDB_URL.replace(NDXLARMOR, NDXLARMOR_LOWERCASE),
                preferenceStore.getString(Preferences.RDB_URL));
    }

    @Test
    public void switching_from_lowercase_ndxdemo_to_local_host_does_not_update_jms_url() {
        // Act
        alarmSettings.setInstrument(mockInstrument(NDXLARMOR_LOWERCASE), mockLocalHost);
        alarmSettings.setInstrument(mockLocalHost, mockInstrument(NDXLARMOR_LOWERCASE));

        // Assert
        assertEquals(DEFAULT_RDB_URL.replace(NDXLARMOR, NDXLARMOR_LOWERCASE),
                preferenceStore.getString(Preferences.RDB_URL));
    }

    @Test
    public void switching_from_local_host_to_non_ISIS_instrument_name_updates_jms_url() {
        // Act
        alarmSettings.setInstrument(mockInstrument(NON_ISIS_INST_NAME), mockLocalHost);

        // Assert
        assertEquals(NON_ISIS_INST_JMS_URL, preferenceStore.getString(Preferences.JMS_URL));
    }

    @Test
    public void switching_from_local_host_to_non_ISIS_instrument_name_updates_rdb_url() {
        // Act
        alarmSettings.setInstrument(mockInstrument(NON_ISIS_INST_NAME), mockLocalHost);

        // Assert
        assertEquals(NON_ISIS_INST_RDB_URL, preferenceStore.getString(Preferences.RDB_URL));
    }

    @Test
    public void switching_from_local_host_to_IP_host_name_updates_jms_url() {
        // Act
        alarmSettings.setInstrument(mockInstrument(IP_ADDRESS), mockLocalHost);

        // Assert
        assertEquals(IP_JMS_URL, preferenceStore.getString(Preferences.JMS_URL));
    }

    @Test
    public void switching_from_local_host_to_IP_host_name_updates_rdb_url() {
        // Act
        alarmSettings.setInstrument(mockInstrument(IP_ADDRESS), mockLocalHost);

        // Assert
        assertEquals(IP_RDB_URL, preferenceStore.getString(Preferences.RDB_URL));
    }

    @Test
    public void switching_from_local_host_to_local_host_causes_no_change_to_jms_url() {
        // Act
        alarmSettings.setInstrument(mockLocalHost, mockLocalHost);

        // Assert
        assertEquals(LOCALHOST_JMS_URL, preferenceStore.getString(Preferences.JMS_URL));
    }

    @Test
    public void switching_from_local_host_to_local_host_causes_no_change_to_rdb_url() {
        // Act
        alarmSettings.setInstrument(mockLocalHost, mockLocalHost);

        // Assert
        assertEquals(LOCALHOST_RDB_URL, preferenceStore.getString(Preferences.RDB_URL));
    }

    @Test
    public void switching_from_larmor_to_larmor_causes_no_change_to_jms_url() {
        // Act
        alarmSettings.setInstrument(mockLarmor, mockLocalHost);
        String expectedUrls = preferenceStore.getString(Preferences.JMS_URL);
        alarmSettings.setInstrument(mockLarmor, mockLarmor);
        String actualUrls = preferenceStore.getString(Preferences.JMS_URL);

        // Assert
        assertEquals(actualUrls, expectedUrls);
    }

    @Test
    public void switching_from_larmor_to_larmor_causes_no_change_to_rdb_url() {
        // Act
        alarmSettings.setInstrument(mockLarmor, mockLocalHost);
        String expectedArchives = preferenceStore.getString(Preferences.RDB_URL);
        alarmSettings.setInstrument(mockLarmor, mockLarmor);
        String actualArchives = preferenceStore.getString(Preferences.RDB_URL);

        // Assert
        assertEquals(actualArchives, expectedArchives);
    }

    @Test
    public void switching_from_localhost_to_larmor_with_demo_as_old_instrument_updates_jms_url() {

        // Works this way for localhost because the values on exit are not
        // stored in the standard
        // preference store so value is always localhost on startup. See ticket
        // #1109

        // Act
        alarmSettings.setInstrument(mockLarmor, mockDemo);

        // Assert
        assertEquals(LARMOR_JMS_URL, preferenceStore.getString(Preferences.JMS_URL));
    }

    @Test
    public void switching_from_localhost_to_larmor_with_demo_as_old_instrument_does_not_change_rdb_url() {

        // Works this way for localhost because the values on exit are not
        // stored in the standard
        // preference store so value is always localhost on startup. See ticket
        // #1109

        // Act
        alarmSettings.setInstrument(mockLarmor, mockDemo);

        // Assert
        assertEquals(LARMOR_RDB_URL, preferenceStore.getString(Preferences.RDB_URL));
    }

    @Test(expected = RuntimeException.class)
    public void switching_from_custom_to_larmor_with_demo_as_old_instrument_raises_RuntimeException() {
        // Act
        alarmSettings.setInstrument(mockCustom, mockLocalHost);
        alarmSettings.setInstrument(mockLarmor, mockDemo);
    }

    @Test(expected = RuntimeException.class)
    public void switching_from_custom_to_larmor_with_demo_as_old_instrument_does_not_change_jms_url() {
        // Act
        alarmSettings.setInstrument(mockCustom, mockLocalHost);
        alarmSettings.setInstrument(mockLarmor, mockDemo);

        // Assert
        assertEquals(CUSTOM_JMS_URL, preferenceStore.getString(Preferences.JMS_URL));
    }

    @Test(expected = RuntimeException.class)
    public void switching_from_custom_to_larmor_with_demo_as_old_instrument_does_not_change_rdb_url() {
        // Act
        alarmSettings.setInstrument(mockCustom, mockLocalHost);
        alarmSettings.setInstrument(mockLarmor, mockDemo);

        // Assert
        assertEquals(CUSTOM_RDB_URL, preferenceStore.getString(Preferences.RDB_URL));
    }
}
