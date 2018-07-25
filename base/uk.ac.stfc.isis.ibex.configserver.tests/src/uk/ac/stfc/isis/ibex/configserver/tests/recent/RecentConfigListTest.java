package uk.ac.stfc.isis.ibex.configserver.tests.recent;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import uk.ac.stfc.isis.ibex.configserver.configuration.ConfigInfo;
import uk.ac.stfc.isis.ibex.configserver.recent.RecentConfigList;


public class RecentConfigListTest {

    private static final int MRU_LENGTH = 6;
    private static final String name = "NAME";
    private static final String TIMESTAMP = "2018-15-13 12:45:0";
    RecentConfigList recentConfigList = new RecentConfigList();

    @Before
    public void setUp() {
        recentConfigList.clear();
    }

    @After
    public void tearDown() {
        recentConfigList.clear();
    }

    @Test
    public void GIVEN_name_WHEN_add_THEN_recent_config_list_contains_name() {
        List<String> names = new ArrayList<String>();
        names.add(name);
        List<String> expecteds = new ArrayList<String>();
        expecteds.add(name);
        recentConfigList.add(name);
        List<String> actuals = recentConfigList.get();
        assertEquals(expecteds, actuals);
    }

    @Test
    public void GIVEN_name_already_in_list_WHEN_add_to_list_containing_several_names_THEN_name_at_top_of_list() {
        for(int i = 0; i < MRU_LENGTH; i++) {
            recentConfigList.add(name +i);
        }
        String expected = name + 5;
        recentConfigList.add(expected);
        List<String> actuals = recentConfigList.get();
        assertEquals(expected, actuals.get(0));
    }

    @Test
    public void GIVEN_name_already_in_list_WHEN_add_to_list_containing_several_names_THEN_name_only_exists_once_in_list() {
        for(int i = 0; i < MRU_LENGTH; i++) {
            recentConfigList.add(name + i);
        }
        String expected = name + 5;
        recentConfigList.add(expected);
        List<String> actuals = recentConfigList.get();
        for (int i = 0; i < MRU_LENGTH; i++){
            if (i == 0 ) {
                assertEquals(expected, actuals.get(i));
            } else {
                assertNotEquals(expected, actuals.get(i));
            }
        }
    }

    @Test
    public void GIVEN_name_already_in_list_WHEN_remove_name_THEN_name_is_not_in_list_and_list_not_empty() {
        for(int i = 0; i < MRU_LENGTH; i++) {
            recentConfigList.add(name + i);
        }
        String toBeRemoved = name + 5;
        recentConfigList.remove(toBeRemoved);
        List<String> actuals = recentConfigList.get();
        assertFalse(actuals.contains(toBeRemoved));
        assertFalse(actuals.isEmpty());
    }

    @Test
    public void GIVEN_list_WHEN_clear_THEN_list_is_empty() {
        for(int i = 0; i < MRU_LENGTH; i++) {
            recentConfigList.add(name + i);
        }
        recentConfigList.clear();
        List<String> actuals = recentConfigList.get();
        assertTrue(actuals.isEmpty());
    }

    @Test
    public void GIVEN_name_already_first_in_list_WHEN_add_to_list_containing_several_names_THEN_name_at_top_of_list() {
        for(int i = 0; i < MRU_LENGTH; i++) {
            recentConfigList.add(name + i);
        }
        String expected = name + 0;
        recentConfigList.add(expected);
        List<String> actuals = recentConfigList.get();
        assertEquals(expected, actuals.get(0));
    }

    @Test
    public void GIVEN_list_of_6_names_WHEN_add_name_not_already_in_list_THEN_still_6_names() {
        for(int i = 0; i < MRU_LENGTH; i++) {
            recentConfigList.add(name + i);
        }
        String expected = name;
        recentConfigList.add(expected);
        List<String> actuals = recentConfigList.get();
        assertEquals(MRU_LENGTH, actuals.size());
    }

    @Test
    public void GIVEN_list_of_6_names_WHEN_add_name_not_already_in_list_THEN_name_is_at_top_of_list() {
        for(int i = 0; i < MRU_LENGTH; i++) {
            recentConfigList.add(name + i);
        }
        String expected = name;
        recentConfigList.add(expected);
        List<String> actuals = recentConfigList.get();
        assertEquals(expected, actuals.get(0));
    }

    @Test
    public void GIVEN_list_of_configs_in_server_and_some_config_names_WHEN_convert_name_to_config_info_THEN_get_correct_configs() {
        Collection<ConfigInfo> configs = new ArrayList<ConfigInfo>();
        List<String> configNames = new ArrayList<String>();
        for (int i = 0; i < 10 ; i++){
            ConfigInfo config = mock(ConfigInfo.class);
            when(config.name()).thenReturn(name + i);
            configs.add(config);
            if ((i % 2) == 0) {
                configNames.add(name + i);
            }
        }
        
        int i = 0;
        for (ConfigInfo conf : recentConfigList.configNamesToConfigInfos(configNames, configs)){
            String actual = conf.name();
            String expected = configNames.get(i);
            assertEquals(expected, actual);
            i++;
        }
        i = 0;
    }

    @Test
    public void GIVEN_a_config_in_server_WHEN_get_lat_time_modified_THEN_get_correct_timestamp() {
        ConfigInfo config = mock(ConfigInfo.class);
        Collection<String> timestamps = new ArrayList<String>();
        String expected = "";
        for (int i = 0; i<10; i++){
            timestamps.add(TIMESTAMP + i);
            if (i == 9) {
                expected = TIMESTAMP + i;
            }
        }
        when(config.getHistory()).thenReturn(timestamps);
        String actual = recentConfigList.getLastModified(config);
        assertEquals(expected, actual);
    }

}
