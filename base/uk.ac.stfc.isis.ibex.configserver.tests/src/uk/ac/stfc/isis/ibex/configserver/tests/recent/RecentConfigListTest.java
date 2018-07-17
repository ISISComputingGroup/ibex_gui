package uk.ac.stfc.isis.ibex.configserver.tests.recent;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import uk.ac.stfc.isis.ibex.configserver.recent.RecentConfigList;


public class RecentConfigListTest {

    private static final int MRU_LENGTH = 6;
    String name = "NAME";
    String timestamp = "2018-15-13 12:45:0";

    @Test
    public void GIVEN_name_WHEN_add_THEN_recent_config_list_contains_name(){
        RecentConfigList recentConfigList = new RecentConfigList();
        recentConfigList.clear();
        List<String> names = new ArrayList<String>();
        names.add(name);
        List<String> expecteds = new ArrayList<String>();
        expecteds.add(name);
        recentConfigList.add(name);
        List<String> actuals = recentConfigList.get();
        assertEquals(expecteds, actuals);
    }

    @Test
    public void GIVEN_name_already_in_list_WHEN_add_to_list_containing_several_names_THEN_name_at_top_of_list(){
        RecentConfigList recentConfigList = new RecentConfigList();
        recentConfigList.clear();
        for(int i = 0; i < MRU_LENGTH; i++){
            recentConfigList.add(name +i);
        }
        String expected = name + 5;
        recentConfigList.add(expected);
        List<String> actuals = recentConfigList.get();
        assertEquals(expected, actuals.get(0));
    }
    
    @Test
    public void GIVEN_name_already_in_list_WHEN_add_to_list_containing_several_names_THEN_name_only_exists_once_in_list(){
        RecentConfigList recentConfigList = new RecentConfigList();
        recentConfigList.clear();
        for(int i = 0; i < MRU_LENGTH; i++){
            recentConfigList.add(name +i);
        }
        String expected = name + 5;
        recentConfigList.add(expected);
        List<String> actuals = recentConfigList.get();
        for (int i = 0; i < MRU_LENGTH; i++){
            if (i ==0 ) {
                assertEquals(expected, actuals.get(i));
            } else {
                assertNotEquals(expected, actuals.get(i));
            }
        }
    }
    
    @Test
    public void GIVEN_string_containing_name_and_timestamp_already_in_list_WHEN_add_to_list_containing_several_strings_THEN_string_at_top_of_list(){
        RecentConfigList recentConfigList = new RecentConfigList();
        recentConfigList.clear();
        for(int i = 0; i < MRU_LENGTH; i++){
            recentConfigList.add(name + 5 +" " + timestamp + 5);
        }
        String expected =name + 5 +" " + timestamp + 5;
        recentConfigList.add(expected);
        List<String> actuals = recentConfigList.get();
        assertEquals(expected, actuals.get(0));
    }
    
    @Test
    public void GIVEN_string_containing_name_and_timestamp_already_in_list_WHEN_add_to_list_containing_several_strings_THEN_string_only_exists_once_in_list(){
        RecentConfigList recentConfigList = new RecentConfigList();
        recentConfigList.clear();
        for(int i = 0; i < MRU_LENGTH; i++){
            recentConfigList.add(name + i +" " + timestamp + i);
        }
        String expected = name + 5 +" " + timestamp + 5;
        recentConfigList.add(expected);
        List<String> actuals = recentConfigList.get();
        for (int i = 0; i < MRU_LENGTH; i++){
            if (i ==0 ) {
                assertEquals(expected, actuals.get(i));
            } else {
                assertNotEquals(expected, actuals.get(i));
            }
        }
    }

    @Test
    public void GIVEN_name_already_first_in_list_WHEN_add_to_list_containing_several_names_THEN_name_at_top_of_list(){
        RecentConfigList recentConfigList = new RecentConfigList();
        recentConfigList.clear();
        for(int i = 0; i < MRU_LENGTH; i++){
            recentConfigList.add(name + i);
        }
        String expected = name + 0;
        recentConfigList.add(expected);
        List<String> actuals = recentConfigList.get();
        assertEquals(expected, actuals.get(0));
    }

    @Test
    public void GIVEN_list_of_6_names_WHEN_add_name_not_already_in_list_THEN_still_6_names(){
        RecentConfigList recentConfigList = new RecentConfigList();
        recentConfigList.clear();
        for(int i = 0; i < MRU_LENGTH; i++){
            recentConfigList.add(name + i);
        }
        String expected = name;
        recentConfigList.add(expected);
        List<String> actuals = recentConfigList.get();
        assertEquals(MRU_LENGTH, actuals.size());
    }
    
    @Test
    public void GIVEN_list_of_6_strings_containing_name_and_timestamp_WHEN_add_string_not_already_in_list_THEN_still_6_strings(){
        RecentConfigList recentConfigList = new RecentConfigList();
        recentConfigList.clear();
        for(int i = 0; i < MRU_LENGTH; i++){
            recentConfigList.add(name + i + " " + timestamp + i);
        }
        String expected = name + " " + timestamp;
        recentConfigList.add(expected);
        List<String> actuals = recentConfigList.get();
        assertEquals(MRU_LENGTH, actuals.size());
    }

    @Test
    public void GIVEN_list_of_6_names_WHEN_add_name_not_already_in_list_THEN_name_is_at_top_of_list(){
        RecentConfigList recentConfigList = new RecentConfigList();
        recentConfigList.clear();
        for(int i = 0; i < MRU_LENGTH; i++){
            recentConfigList.add(name + i);
        }
        String expected = name;
        recentConfigList.add(expected);
        List<String> actuals = recentConfigList.get();
        assertEquals(expected, actuals.get(0));
    }
    
    @Test
    public void GIVEN_list_of_6_strings_containing_name_and_timestamp_WHEN_add_string_not_already_in_list_THEN_string_is_at_top_of_list(){
        RecentConfigList recentConfigList = new RecentConfigList();
        recentConfigList.clear();
        for(int i = 0; i < MRU_LENGTH; i++){
            recentConfigList.add(name + i + " " + timestamp + i);
        }
        String expected = name + " " + timestamp;
        recentConfigList.add(expected);
        List<String> actuals = recentConfigList.get();
        assertEquals(expected, actuals.get(0));
    }

    @Test
    public void GIVEN_list_containing_strings_with_names_and_timestamp_WHEN_getName_THEN_only_have_name(){
        RecentConfigList recentConfigList = new RecentConfigList();
        recentConfigList.clear();
        List<String> expected = new ArrayList<String>();
        for(int i = 0; i < MRU_LENGTH ; i++){
            String item = name + i + " " + timestamp + i;
            recentConfigList.add(item);
            expected.add(0, name+i);
        }
        
        List<String> actuals = recentConfigList.getNames();
        assertEquals(expected, actuals);
    }

    @Test
    public void GIVEN_list_containing_strings_with_names_and_timestamp_WHEN_getTimestamp_THEN_only_have_timestamp(){
        RecentConfigList recentConfigList = new RecentConfigList();
        recentConfigList.clear();
        List<String> expected = new ArrayList<String>();
        for(int i = 0; i < MRU_LENGTH ; i++){
            String item = name + i + " " + timestamp + i;
            recentConfigList.add(item);
            expected.add(0, timestamp+i);
        }
        
        List<String> actuals = recentConfigList.getTimestamps();
        assertEquals(expected, actuals);
    }
}
