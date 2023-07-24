
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

package uk.ac.stfc.isis.ibex.configserver.tests.displaying;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import uk.ac.stfc.isis.ibex.configserver.AlarmState;
import uk.ac.stfc.isis.ibex.configserver.configuration.Block;
import uk.ac.stfc.isis.ibex.configserver.displaying.DisplayBlock;
import uk.ac.stfc.isis.ibex.configserver.displaying.RuncontrolState;
import uk.ac.stfc.isis.ibex.epics.observing.ClosableObservable;
import uk.ac.stfc.isis.ibex.epics.observing.ForwardingObservable;

@SuppressWarnings({ "unchecked", "checkstyle:methodname" })
public class DisplayBlockTest {
	
	TestableIOSObservable<String> inRangeObservable;
    TestableIOSObservable<String> enabledObservable;
    TestableIOSObservable<String> valueObservable;
    TestableIOSObservable<String> descriptionObservable;
    TestableIOSObservable<AlarmState> alarmObservable;
    TestableIOSObservable<Double> lowLimitObservable;
    
	DisplayBlock displayBlock;
	Block mockedBlock;
	
	@Before
	public void setUp() {
		// Arrange
        inRangeObservable = new TestableIOSObservable<String>(mock(ClosableObservable.class));
        enabledObservable = new TestableIOSObservable<String>(mock(ClosableObservable.class));
        valueObservable = new TestableIOSObservable<String>(mock(ClosableObservable.class));
        descriptionObservable = new TestableIOSObservable<String>(mock(ClosableObservable.class));
        alarmObservable= new TestableIOSObservable<AlarmState>(mock(ClosableObservable.class));
        lowLimitObservable = new TestableIOSObservable<Double>(mock(ClosableObservable.class));
        mockedBlock = mock(Block.class);
		displayBlock = new DisplayBlock(
                mockedBlock, // block
                valueObservable, // value
                descriptionObservable, // description
                alarmObservable, // alarm
                inRangeObservable, // inRange
                lowLimitObservable,  // lowLimit
				mock(ForwardingObservable.class),  // highLimit
				mock(ForwardingObservable.class),  // suspendOnInvalid
                enabledObservable, // enabled
                "");
	}

    @Test
    public void GIVEN_pv_disconnected_THEN_blockstate_disconnected() {

        // Act
        valueObservable.setConnectionStatus(false);

        // Assert
        assertEquals(RuncontrolState.DISCONNECTED, displayBlock.getRuncontrolState());
    }

    @Test
    public void GIVEN_pv_was_disconnected_WHEN_pv_connected_THEN_blockstate_runcontrol_disabled() {

        // Arrange
        valueObservable.setConnectionStatus(false);
        enabledObservable.setValue("YES");
        inRangeObservable.setValue("YES");

        // Act
        valueObservable.setConnectionStatus(true);

        // Assert
        assertEquals(RuncontrolState.ENABLED_IN_RANGE, displayBlock.getRuncontrolState());
    }

    @Test
    public void GIVEN_pv_was_connected_WHEN_pv_disconnected_THEN_blockstate_disconnected() {

        // Arrange
        valueObservable.setConnectionStatus(true);

        // Act
        valueObservable.setConnectionStatus(false);

        // Assert
        assertEquals(RuncontrolState.DISCONNECTED, displayBlock.getRuncontrolState());
    }

	@Test
    public void GIVEN_in_range_and_disabled_THEN_runcontrol_state_is_disabled() {
        // Arrange
        valueObservable.setConnectionStatus(true);

        // Act
        enabledObservable.setValue("NO");
        inRangeObservable.setValue("YES");
		
		// Assert
        assertEquals(RuncontrolState.DISABLED, displayBlock.getRuncontrolState());
	}
	
	@Test
    public void GIVEN_in_range_and_enabled_THEN_runcontrol_state_is_enabled_in_range() {
        // Arrange
        valueObservable.setConnectionStatus(true);

        // Act
        enabledObservable.setValue("YES");
        inRangeObservable.setValue("YES");
		
		// Assert
        assertEquals(RuncontrolState.ENABLED_IN_RANGE, displayBlock.getRuncontrolState());
	}
	
	@Test
    public void GIVEN_not_in_range_and_enabled_THEN_runcontrol_state_is_enabled_out_range() {
        // Arrange
        valueObservable.setConnectionStatus(true);

		// Act
        enabledObservable.setValue("YES");
		inRangeObservable.setValue("NO");
		
		// Assert
        assertEquals(RuncontrolState.ENABLED_OUT_RANGE, displayBlock.getRuncontrolState());
	}
	
	@Test
    public void GIVEN_not_in_range_and_enabled_WHEN_set_to_in_range_THEN_runcontrol_state_is_enabled_in_range() {
        // Arrange
        valueObservable.setConnectionStatus(true);
        enabledObservable.setValue("YES");
        inRangeObservable.setValue("NO");

        // Act
		inRangeObservable.setValue("YES");
		
		// Assert
        assertEquals(RuncontrolState.ENABLED_IN_RANGE, displayBlock.getRuncontrolState());
	}

	@Test
    public void GIVEN_in_range_and_enabled_WHEN_set_to_not_in_range_THEN_runcontrol_state_is_enabled_out_range() {
        // Arrange
        valueObservable.setConnectionStatus(true);
        enabledObservable.setValue("YES");
        inRangeObservable.setValue("YES");

		// Act
		inRangeObservable.setValue("NO");
		
		// Assert
        assertEquals(RuncontrolState.ENABLED_OUT_RANGE, displayBlock.getRuncontrolState());
	}

	@Test
    public void
            GIVEN_not_in_range_and_enabled_WHEN_in_range_set_to_nonsense_THEN_runcontrol_state_is_disconnected() {
        // Arrange
        valueObservable.setConnectionStatus(true);
        enabledObservable.setValue("YES");
        inRangeObservable.setValue("NO");

		// Act
		inRangeObservable.setValue("maybe");
		
		// Assert
        assertEquals(RuncontrolState.DISCONNECTED, displayBlock.getRuncontrolState());
	}

    @Test
    public void GIVEN_pv_disconnected_WHEN_set_to_in_range_and_enabled_THEN_runcontrol_state_is_in_range() {

        // Arrange
        valueObservable.setConnectionStatus(false);

        // Act
        enabledObservable.setValue("YES");
        inRangeObservable.setValue("YES");

        // Assert
        assertEquals(RuncontrolState.ENABLED_IN_RANGE, displayBlock.getRuncontrolState());
    }

    @Test
    public void GIVEN_pv_disconnected_WHEN_set_to_not_in_range_and_enabled_THEN_runcontrol_state_is_out_of_range() {

        // Arrange
        valueObservable.setConnectionStatus(false);

        // Act
        enabledObservable.setValue("YES");
        inRangeObservable.setValue("NO");

        // Assert
        assertEquals(RuncontrolState.ENABLED_OUT_RANGE, displayBlock.getRuncontrolState());
    }

    @Test
    public void GIVEN_pv_disconnected_WHEN_set_to_in_range_and_not_enabled_THEN_runcontrol_state_is_not_enabled() {

        // Arrange
        valueObservable.setConnectionStatus(false);

        // Act
        enabledObservable.setValue("NO");
        inRangeObservable.setValue("YES");

        // Assert
        assertEquals(RuncontrolState.DISABLED, displayBlock.getRuncontrolState());
    }

    @Test
    public void GIVEN_pv_disconnected_WHEN_set_to_not_in_range_and_not_enabled_THEN_runcontrol_state_is_not_enabled() {

        // Arrange
        valueObservable.setConnectionStatus(false);

        // Act
        enabledObservable.setValue("NO");
        inRangeObservable.setValue("NO");

        // Assert
        assertEquals(RuncontrolState.DISABLED, displayBlock.getRuncontrolState());
    }
    
    @Test
    public void GIVEN_runcontrol_enabled_pv_disconnected_THEN_runcontrol_state_is_disconnected() {

        // Act
        enabledObservable.setConnectionStatus(false);

        // Assert
        assertEquals(RuncontrolState.DISCONNECTED, displayBlock.getRuncontrolState());
    }
    
    @Test
    public void GIVEN_runcontrol_in_range_pv_disconnected_THEN_runcontrol_state_is_disconnected() {

        // Act
        inRangeObservable.setConnectionStatus(false);

        // Assert
        assertEquals(RuncontrolState.DISCONNECTED, displayBlock.getRuncontrolState());
    }
    
    @Test
    public void GIVEN_no_units_WHEN_value_of_10_THEN_return_10() {
        String newValue = String.valueOf(10);
        
        // Act
        valueObservable.setValue(newValue);

        // Assert
        assertEquals(newValue, displayBlock.getValue());
    }
    
    @Test
    public void GIVEN_pv_disconnected_THEN_value_is_disconnected() {      
        // Act
        valueObservable.setConnectionStatus(false);

        // Assert
        assertEquals("disconnected", displayBlock.getValue());
    }
    
    @Test
    public void GIVEN_pv_in_error_THEN_value_is_error() {      
        // Act
        valueObservable.setError(new Exception());

        // Assert
        assertEquals("error", displayBlock.getValue());
    }
    
    @Test
    public void WHEN_low_limit_set_tovalue_THEN_lowlimit_updates() {      
        Double lowLimit = 10.0;
        // Act
        lowLimitObservable.setValue(lowLimit);

        // Assert
        assertEquals(lowLimit, displayBlock.getRunControlLowLimit());
    }
    
    @Test
    public void GIVEN_pv_connection_THEN_get_value_tooltip_text_correct() {
    	// Arrange
    	String name = "name";
    	String pvAddress = "pv_address";
    	String description = "description";
    	String value = "10";
    	
    	when(mockedBlock.getPV()).thenReturn(pvAddress);
    	when(mockedBlock.getName()).thenReturn(name);
    	
    	// Act
    	valueObservable.setConnectionStatus(true);
    	descriptionObservable.setValue(description);
    	valueObservable.setValue(value);
    	
    	// Assert
    	String expected = String.format("%s%sPV Address: %s%sValue: %s%sStatus: %s%s%s", 
        		name, System.lineSeparator(),
        		pvAddress, System.lineSeparator(),
        		value, System.lineSeparator(), 
        		"No alarm", System.lineSeparator(), 
        		description);
    	assertEquals(expected, displayBlock.getValueTooltipText());
    }
}
