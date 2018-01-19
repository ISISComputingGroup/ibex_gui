
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

package uk.ac.stfc.isis.ibex.alarm.tests;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import org.csstudio.alarm.beast.Preferences;
import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import uk.ac.stfc.isis.ibex.alarm.AlarmSettings;
import uk.ac.stfc.isis.ibex.instrument.InstrumentInfo;

/**
 * Tests for the AlarmSettings class.
 */
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
     * @param hostName The instrument name
     * @return The archive settings string corresponding to the given
     *         instrument.
     */
    private static String buildRdbUrl(String hostName) {
        return "jdbc:mysql://" + hostName + "/ALARM";
    }

    /**
     * @param hostName The instrument name
     * @return The URLs settings string corresponding to the given instrument.
     */
    private static String buildJmsUrl(String hostName) {
        return "failover:(tcp://" + hostName + ":39990)";
    }

    // These settings represent the defaults, as set in
    // uk.ac.stfc.isis.ibex.product/plugin_customization.ini

    /**
     * RDB URL for localhost.
     */
    private static final String LOCALHOST_RDB_URL = buildRdbUrl(LOCALHOST);

    /**
     * JMS URL for localhost.
     */
    private static final String LOCALHOST_JMS_URL = buildJmsUrl(LOCALHOST);

    /**
     * RDB URL for larmor.
     */
    private static final String LARMOR_RDB_URL = buildRdbUrl(NDXLARMOR);

    /**
     * JMS URL for larmor.
     */
    private static final String LARMOR_JMS_URL = buildJmsUrl(NDXLARMOR);

    /**
     * RDB URL for demo.
     */
    private static final String DEMO_RDB_URL = buildRdbUrl(NDXDEMO);

    /**
     * JMS URL for demo.
     */
    private static final String DEMO_JMS_URL = buildJmsUrl(NDXDEMO);

    /**
     * RDB URL for a custom instrument.
     */
    private static final String CUSTOM_RDB_URL = buildRdbUrl(NDWCUSTOM);

    /**
     * JMS URL for a custom instrument.
     */
    private static final String CUSTOM_JMS_URL = buildJmsUrl(NDWCUSTOM);

    /**
     * RDB URL for an instrument not conforming to standard ISIS naming
     * convention.
     */
    private static final String NON_ISIS_INST_RDB_URL = buildRdbUrl(NON_ISIS_INST_NAME);

    /**
     * JMS URL for an instrument not conforming to standard ISIS naming
     * convention.
     */
    private static final String NON_ISIS_INST_JMS_URL = buildJmsUrl(NON_ISIS_INST_NAME);

    /**
     * RDB URL for an instrument in IP address format.
     */
    private static final String IP_RDB_URL = buildRdbUrl(IP_ADDRESS);

    /**
     * JMS URL for an instrument in IP address format.
     */
    private static final String IP_JMS_URL = buildJmsUrl(IP_ADDRESS);

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
    private org.osgi.service.prefs.Preferences preferences;

    /**
     * Return value for mocked preference store jms value.
     */
    private String mockJmsUrl;

    /**
     * Return value for mocked preference store rdb value.
     */
    private String mockRdbUrl;

    /**
     * Set up procedure to run before tests.
     */
    @Before
    public void setUp() {
        // Arrange
        mockRdbUrl = DEFAULT_RDB_URL;
        mockJmsUrl = DEFAULT_JMS_URL;

        preferences = mock(org.osgi.service.prefs.Preferences.class);
        doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                mockRdbUrl = (String) invocation.getArguments()[1];
                return null;
            }
        }).when(preferences).put(eq(Preferences.RDB_URL), anyString());
        doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                mockJmsUrl = (String) invocation.getArguments()[1];
                return null;
            }
        }).when(preferences).put(eq(Preferences.JMS_URL), anyString());
        doAnswer(new Answer<String>() {
            @Override
            public String answer(InvocationOnMock invocation) throws Throwable {
                return mockJmsUrl;
            }
        }).when(preferences).get(eq(Preferences.JMS_URL), anyString());
        doAnswer(new Answer<String>() {
            @Override
            public String answer(InvocationOnMock invocation) throws Throwable {
                return mockRdbUrl;
            }
        }).when(preferences).get(eq(Preferences.RDB_URL), anyString());

        alarmSettings = new AlarmSettings(preferences);

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
        assertEquals(DEFAULT_RDB_URL, preferences.get(Preferences.RDB_URL, null));
    }

    @Test
    public void default_jms_url_is_set_correctly() {
        // Assert
        assertEquals(DEFAULT_JMS_URL, preferences.get(Preferences.JMS_URL, null));
    }

    @Test
    public void switching_from_local_host_to_NDXLARMOR_updates_rdb_url() {
        // Act
        alarmSettings.setInstrument(mockLarmor);

        // Assert
        assertEquals(LARMOR_RDB_URL, preferences.get(Preferences.RDB_URL, null));
    }

    @Test
    public void switching_from_local_host_to_NDXLARMOR_updates_jms_url() {
        // Act
        alarmSettings.setInstrument(mockLarmor);

        // Assert
        assertEquals(LARMOR_JMS_URL, preferences.get(Preferences.JMS_URL, null));
    }


    @Test
    public void switching_from_NDXLARMOR_to_NDXDEMO_updates_rdb_url() {
        // Act
        alarmSettings.setInstrument(mockLarmor);
        alarmSettings.setInstrument(mockDemo);

        // Assert
        assertEquals(DEMO_RDB_URL, preferences.get(Preferences.RDB_URL, null));
    }

    @Test
    public void switching_from_NDXLARMOR_to_NDXDEMO_updates_jms_url() {
        // Act
        alarmSettings.setInstrument(mockLarmor);
        alarmSettings.setInstrument(mockDemo);

        // Assert
        assertEquals(DEMO_JMS_URL, preferences.get(Preferences.JMS_URL, null));
    }

    @Test
    public void switching_from_NDXLARMOR_to_local_host_updates_rdb_url() {
        // Act
        alarmSettings.setInstrument(mockLarmor);
        alarmSettings.setInstrument(mockLocalHost);

        // Assert
        assertEquals(DEFAULT_RDB_URL, preferences.get(Preferences.RDB_URL, null));
    }

    @Test
    public void switching_from_NDXLARMOR_to_local_host_updates_jms_url() {
        // Act
        alarmSettings.setInstrument(mockLarmor);
        alarmSettings.setInstrument(mockLocalHost);

        // Assert
        assertEquals(DEFAULT_JMS_URL, preferences.get(Preferences.JMS_URL, null));
    }

    @Test
    public void switching_from_local_host_to_NDWCUSTOM_updates_rdb_url() {
        // Act
        alarmSettings.setInstrument(mockCustom);

        // Assert
        assertEquals(CUSTOM_RDB_URL, preferences.get(Preferences.RDB_URL, null));
    }

    @Test
    public void switching_from_local_host_to_NDWCUSTOM_updates_jms_url() {
        // Act
        alarmSettings.setInstrument(mockCustom);

        // Assert
        assertEquals(CUSTOM_JMS_URL, preferences.get(Preferences.JMS_URL, null));
    }

    @Test
    public void switching_from_NDWCUSTOM_to_NDXLARMOR_updates_rdb_url() {
        // Act
        alarmSettings.setInstrument(mockCustom);
        alarmSettings.setInstrument(mockLarmor);

        // Assert
        assertEquals(LARMOR_RDB_URL, preferences.get(Preferences.RDB_URL, null));
    }

    @Test
    public void switching_from_NDWCUSTOM_to_NDXLARMOR_updates_jms_url() {
        // Act
        alarmSettings.setInstrument(mockCustom);
        alarmSettings.setInstrument(mockLarmor);

        // Assert
        assertEquals(LARMOR_JMS_URL, preferences.get(Preferences.JMS_URL, null));
    }

    @Test
    public void switching_from_lowercase_ndxdemo_to_local_host_does_not_update_rdb_url() {
        // Act
        alarmSettings.setInstrument(mockInstrument(NDXLARMOR_LOWERCASE));
        alarmSettings.setInstrument(mockLocalHost);

        // Assert
        assertEquals(DEFAULT_RDB_URL.replace(NDXLARMOR, NDXLARMOR_LOWERCASE),
                preferences.get(Preferences.RDB_URL, null));
    }

    @Test
    public void switching_from_lowercase_ndxdemo_to_local_host_does_not_update_jms_url() {
        // Act
        alarmSettings.setInstrument(mockInstrument(NDXLARMOR_LOWERCASE));
        alarmSettings.setInstrument(mockLocalHost);
        
        // Assert
        assertEquals(DEFAULT_RDB_URL.replace(NDXLARMOR, NDXLARMOR_LOWERCASE),
                preferences.get(Preferences.RDB_URL, null));
    }

    @Test
    public void switching_from_local_host_to_non_ISIS_instrument_name_updates_jms_url() {
        // Act
        alarmSettings.setInstrument(mockInstrument(NON_ISIS_INST_NAME));

        // Assert
        assertEquals(NON_ISIS_INST_JMS_URL, preferences.get(Preferences.JMS_URL, null));
    }

    @Test
    public void switching_from_local_host_to_non_ISIS_instrument_name_updates_rdb_url() {
        // Act
        alarmSettings.setInstrument(mockInstrument(NON_ISIS_INST_NAME));

        // Assert
        assertEquals(NON_ISIS_INST_RDB_URL, preferences.get(Preferences.RDB_URL, null));
    }

    @Test
    public void switching_from_local_host_to_IP_host_name_updates_jms_url() {
        // Act
        alarmSettings.setInstrument(mockInstrument(IP_ADDRESS));

        // Assert
        assertEquals(IP_JMS_URL, preferences.get(Preferences.JMS_URL, null));
    }

    @Test
    public void switching_from_local_host_to_IP_host_name_updates_rdb_url() {
        // Act
        alarmSettings.setInstrument(mockInstrument(IP_ADDRESS));

        // Assert
        assertEquals(IP_RDB_URL, preferences.get(Preferences.RDB_URL, null));
    }

    @Test
    public void switching_from_local_host_to_local_host_causes_no_change_to_jms_url() {
        // Act
        alarmSettings.setInstrument(mockLocalHost);

        // Assert
        assertEquals(LOCALHOST_JMS_URL, preferences.get(Preferences.JMS_URL, null));
    }

    @Test
    public void switching_from_local_host_to_local_host_causes_no_change_to_rdb_url() {
        // Act
        alarmSettings.setInstrument(mockLocalHost);

        // Assert
        assertEquals(LOCALHOST_RDB_URL, preferences.get(Preferences.RDB_URL, null));
    }

    @Test
    public void switching_from_larmor_to_larmor_causes_no_change_to_jms_url() {
        // Act
        alarmSettings.setInstrument(mockLarmor);
        String expectedUrl = preferences.get(Preferences.JMS_URL, null);
        alarmSettings.setInstrument(mockLarmor);
        String actualUrl = preferences.get(Preferences.JMS_URL, null);

        // Assert
        assertEquals(expectedUrl, actualUrl);
    }

    @Test
    public void switching_from_larmor_to_larmor_causes_no_change_to_rdb_url() {
        // Act
        alarmSettings.setInstrument(mockLarmor);
        String expectedUrl = preferences.get(Preferences.RDB_URL, null);
        alarmSettings.setInstrument(mockLarmor);
        String actualUrl = preferences.get(Preferences.RDB_URL, null);

        // Assert
        assertEquals(expectedUrl, actualUrl);
    }

    @Test
    public void switching_to_instrument_called_jdbc_and_back_does_not_affect_rdb_url() {
        // Act
        String expectedUrl = preferences.get(Preferences.RDB_URL, null);
        alarmSettings.setInstrument(mockInstrument("jdbc"));
        alarmSettings.setInstrument(mockLocalHost);

        // Assert
        assertEquals(expectedUrl, preferences.get(Preferences.RDB_URL, null));
    }

    @Test
    public void switching_to_instrument_called_failover_and_back_does_not_affect_jms_url() {
        // Act
        String expectedUrl = preferences.get(Preferences.JMS_URL, null);
        alarmSettings.setInstrument(mockInstrument("failover"));
        alarmSettings.setInstrument(mockLocalHost);

        // Assert
        assertEquals(expectedUrl, preferences.get(Preferences.JMS_URL, null));
    }
}
