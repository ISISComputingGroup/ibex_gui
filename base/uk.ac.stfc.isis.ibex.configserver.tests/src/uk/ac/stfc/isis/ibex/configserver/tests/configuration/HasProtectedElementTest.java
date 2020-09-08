
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import org.junit.Test;

import uk.ac.stfc.isis.ibex.configserver.configuration.ConfigInfo;

@SuppressWarnings("checkstyle:methodname")
public class HasProtectedElementTest {
	
    @Test
    public void WHEN_no_elements_THEN_return_false() {
        // Arrange
    	Collection<ConfigInfo> infos = new ArrayList<ConfigInfo>();

        // Act
    	boolean result = ConfigInfo.hasProtectedElement(infos);

        // Assert
    	assertEquals(false, result);
    }
	
    @Test
    public void WHEN_no_protected_elements_THEN_return_false() {
        // Arrange
    	Collection<ConfigInfo> infos = new ArrayList<ConfigInfo>();
        for (int i = 0; i < 10 ; i++) {
        	ConfigInfo elem = new ConfigInfo("", false, "", "", "", Collections.emptyList());
            infos.add(elem);
        }

        // Act
    	boolean result = ConfigInfo.hasProtectedElement(infos);

        // Assert
    	assertEquals(false, result);
    }
    
    @Test
    public void WHEN_one_element_and_not_protected_THEN_return_true() {
        // Arrange
    	Collection<ConfigInfo> infos = new ArrayList<ConfigInfo>();
    	ConfigInfo elem = new ConfigInfo("", false, "", "", "", Collections.emptyList());
        infos.add(elem);

        // Act
    	boolean result = ConfigInfo.hasProtectedElement(infos);

        // Assert
    	assertEquals(false, result);
    }
    
    @Test
    public void WHEN_one_element_and_protected_THEN_return_true() {
        // Arrange
    	Collection<ConfigInfo> infos = new ArrayList<ConfigInfo>();
    	ConfigInfo elem = new ConfigInfo("", true, "", "", "", Collections.emptyList());
        infos.add(elem);

        // Act
    	boolean result = ConfigInfo.hasProtectedElement(infos);

        // Assert
    	assertEquals(true, result);
    }
    
    @Test
    public void WHEN_one_protected_element_THEN_return_true() {
        // Arrange
    	Collection<ConfigInfo> infos = new ArrayList<ConfigInfo>();
        for (int i = 0; i < 9 ; i++) {
        	ConfigInfo elem = new ConfigInfo("", false, "", "", "", Collections.emptyList());
            infos.add(elem);
        }
    	ConfigInfo elem = new ConfigInfo("", true, "", "", "", Collections.emptyList());
        infos.add(elem);

        // Act
    	boolean result = ConfigInfo.hasProtectedElement(infos);

        // Assert
    	assertEquals(true, result);
    }
    
    @Test
    public void WHEN_multiple_protected_elements_THEN_return_true() {
        // Arrange
    	Collection<ConfigInfo> infos = new ArrayList<ConfigInfo>();
        for (int i = 0; i < 10 ; i++) {
        	if (i%2==0) { 
        		ConfigInfo elem = new ConfigInfo("", false, "", "", "", Collections.emptyList());
        		infos.add(elem);
        	} else { 
        		ConfigInfo elem = new ConfigInfo("", true, "", "", "", Collections.emptyList());
        		infos.add(elem);
        	}
        }

         // Act
     	boolean result = ConfigInfo.hasProtectedElement(infos);

         // Assert
     	assertEquals(true, result);
    }
    
    @Test
    public void WHEN_all_protected_elements_THEN_return_true() {
        // Arrange
    	Collection<ConfigInfo> infos = new ArrayList<ConfigInfo>();
        for (int i = 0; i < 10 ; i++) {
    		ConfigInfo elem = new ConfigInfo("", true, "", "", "", Collections.emptyList());
    		infos.add(elem);
        }

         // Act
     	boolean result = ConfigInfo.hasProtectedElement(infos);

         // Assert
     	assertEquals(true, result);
    }
    
    @Test
    public void WHEN_null_collection_THEN_return_false() {
        // Arrange
    	Collection<ConfigInfo> infos = null;

        // Act
    	boolean result = ConfigInfo.hasProtectedElement(infos);

        // Assert
    	assertEquals(false, result);
    }
}
