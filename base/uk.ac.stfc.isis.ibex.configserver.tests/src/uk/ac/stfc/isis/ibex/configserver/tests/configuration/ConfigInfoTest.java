
/*
 * This file is part of the ISIS IBEX application.
 * Copyright (C) 2012-2020 Science & Technology Facilities Council.
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

/**
 * 
 */
package uk.ac.stfc.isis.ibex.configserver.tests.configuration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.SortedMap;
import java.util.TreeMap;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import uk.ac.stfc.isis.ibex.configserver.Configurations;
import uk.ac.stfc.isis.ibex.configserver.Displaying;
import uk.ac.stfc.isis.ibex.configserver.configuration.ConfigInfo;
import uk.ac.stfc.isis.ibex.configserver.displaying.DisplayConfiguration;
import uk.ac.stfc.isis.ibex.epics.observing.ForwardingObservable;

@SuppressWarnings("checkstyle:methodname")
@RunWith(MockitoJUnitRunner.class)
public class ConfigInfoTest {

	private static final String NAME = "name";
	private static final String DESCRIPTION = "description";
	
	@Mock private Configurations configurations;
	@Mock private Displaying displaying;
	@Mock private ForwardingObservable<DisplayConfiguration> observableDisplayConfiguration;
	@Mock private DisplayConfiguration displayConfiguration;
	
	@Before
	public void setUp() {
		Mockito.when(configurations.display()).thenReturn(displaying);
		Mockito.when(displaying.displayCurrentConfig()).thenReturn(observableDisplayConfiguration);
		Mockito.when(observableDisplayConfiguration.getValue()).thenReturn(displayConfiguration);
		Mockito.when(displayConfiguration.name()).thenReturn("");
	}
	
    @Test
    public void GIVEN_no_items_WHEN_get_names_THEN_return_empty_list() {
        // Arrange
    	Collection<ConfigInfo> infos = new ArrayList<ConfigInfo>();

         // Act
     	Collection<String> result = ConfigInfo.names(infos);

         // Assert
     	assertEquals(Collections.emptyList(), result);
    }
	
    @Test
    public void GIVEN_one_item_WHEN_get_names_THEN_return_name() {
        // Arrange
    	Collection<ConfigInfo> infos = new ArrayList<ConfigInfo>();
		ConfigInfo elem = new ConfigInfo(NAME, false, false, "", "", "", Collections.emptyList());
		infos.add(elem);
		
     	Collection<String> expected = Arrays.asList(NAME);

         // Act
     	Collection<String> result = ConfigInfo.names(infos);

         // Assert
     	assertEquals(expected, result);
    }
    
    @Test
    public void GIVEN_multiple_items_WHEN_get_names_THEN_return_names() {
        // Arrange
    	Collection<ConfigInfo> infos = new ArrayList<ConfigInfo>();
        for (int i = 0; i < 10 ; i++) {
    		ConfigInfo elem = new ConfigInfo(NAME + i, false, false, "", "", "", Collections.emptyList());
    		infos.add(elem);
        }
        
        Collection<String> expected = new ArrayList<String>();
        for (int i = 0; i < 10; i++) {
        	expected.add(NAME + i);
        }

         // Act
     	Collection<String> result = ConfigInfo.names(infos);

         // Assert
     	assertEquals(expected, result);
    }
    
    @Test
    public void GIVEN_no_items_WHEN_get_descriptions_THEN_return_empty_list() {
        // Arrange
    	Collection<ConfigInfo> infos = new ArrayList<ConfigInfo>();

         // Act
     	Collection<String> result = ConfigInfo.descriptions(infos);

         // Assert
     	assertEquals(Collections.emptyList(), result);
    }
	
    @Test
    public void GIVEN_one_item_WHEN_get_descriptions_THEN_return_name() {
        // Arrange
    	Collection<ConfigInfo> infos = new ArrayList<ConfigInfo>();
		ConfigInfo elem = new ConfigInfo("", false, false, DESCRIPTION, "", "", Collections.emptyList());
		infos.add(elem);
		
     	Collection<String> expected = Arrays.asList(DESCRIPTION);

         // Act
     	Collection<String> result = ConfigInfo.descriptions(infos);

         // Assert
     	assertEquals(expected, result);
    }
    
    @Test
    public void GIVEN_multiple_items_WHEN_get_descriptions_THEN_return_descriptions() {
        // Arrange
    	Collection<ConfigInfo> infos = new ArrayList<ConfigInfo>();
        for (int i = 0; i < 10 ; i++) {
    		ConfigInfo elem = new ConfigInfo("", false, false, DESCRIPTION + i, "", "", Collections.emptyList());
    		infos.add(elem);
        }
        
        Collection<String> expected = new ArrayList<String>();
        for (int i = 0; i < 10; i++) {
        	expected.add(DESCRIPTION + i);
        }

         // Act
     	Collection<String> result = ConfigInfo.descriptions(infos);

         // Assert
     	assertEquals(expected, result);
    }
    
    @Test
    public void GIVEN_no_items_WHEN_get_namesAndDescriptions_THEN_return_empty_list() {
        // Arrange
    	Collection<ConfigInfo> infos = new ArrayList<ConfigInfo>();

         // Act
     	SortedMap<String, String> result = ConfigInfo.namesAndDescriptions(infos);

         // Assert
     	assertEquals(true, result.isEmpty());
    }
	
    @Test
    public void GIVEN_one_item_WHEN_get_namesAndDescriptions_THEN_return_name() {
        // Arrange
    	Collection<ConfigInfo> infos = new ArrayList<ConfigInfo>();
		ConfigInfo elem = new ConfigInfo(NAME, false, false, DESCRIPTION, "", "", Collections.emptyList());
		infos.add(elem);
		
		SortedMap<String, String> expected = new TreeMap<String, String>();
		expected.put(NAME, DESCRIPTION);

         // Act
     	SortedMap<String, String> result = ConfigInfo.namesAndDescriptions(infos);

         // Assert
     	assertEquals(expected, result);
    }
    
    @Test
    public void GIVEN_multiple_items_WHEN_get_namesAndDescriptions_THEN_return_descriptions() {
        // Arrange
    	SortedMap<String, String> expected = new TreeMap<String, String>();
    	Collection<ConfigInfo> infos = new ArrayList<ConfigInfo>();
        for (int i = 0; i < 10 ; i++) {
    		ConfigInfo elem = new ConfigInfo(NAME + i, false, false, DESCRIPTION + i, "", "", Collections.emptyList());
    		infos.add(elem);
    		expected.put(NAME + i, DESCRIPTION + i);
        }
        
         // Act
        SortedMap<String, String> result = ConfigInfo.namesAndDescriptions(infos);

         // Assert
     	assertEquals(expected, result);
    }

    @Test
    public void GIVEN_null_components_WHEN_get_infos_without_current_THEN_return_empty_collection() {
    	try (var mockedConfigurations = Mockito.mockStatic(Configurations.class)) {
    		mockedConfigurations.when(Configurations::getInstance).thenReturn(configurations);
    		
        	var contents = ConfigInfo.namesAndDescriptionsWithoutCurrent(null);
        	assertTrue(contents.isEmpty());
    	}
    }
    
    @Test
    public void GIVEN_empty_components_WHEN_get_infos_without_current_THEN_return_empty_collection() {
    	try (var mockedConfigurations = Mockito.mockStatic(Configurations.class)) {
    		mockedConfigurations.when(Configurations::getInstance).thenReturn(configurations);
    		
        	var contents = ConfigInfo.namesAndDescriptionsWithoutCurrent(Collections.emptyList());
        	assertTrue(contents.isEmpty());
    	}
    }
}
