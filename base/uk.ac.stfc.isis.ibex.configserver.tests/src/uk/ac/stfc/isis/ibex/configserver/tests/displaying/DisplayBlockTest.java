
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

import org.junit.Before;
import org.junit.Test;

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
	
	DisplayBlock displayBlock;
	
	@Before
	public void setUp() {
		// Arrange
        inRangeObservable = new TestableIOSObservable<>(mock(ClosableObservable.class));
        enabledObservable = new TestableIOSObservable<>(mock(ClosableObservable.class));
        valueObservable = new TestableIOSObservable<>(mock(ClosableObservable.class));
        descriptionObservable = new TestableIOSObservable<>(mock(ClosableObservable.class));
		displayBlock = new DisplayBlock(
                mock(Block.class), // block
                valueObservable, // value
                descriptionObservable, // description
                mock(ForwardingObservable.class), // alarm
                inRangeObservable, // inRange
				mock(ForwardingObservable.class),  // lowLimit
				mock(ForwardingObservable.class),  // highLimit
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

        // Act
        valueObservable.setConnectionStatus(true);

        // Assert
        assertEquals(RuncontrolState.DISABLED, displayBlock.getRuncontrolState());
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
            GIVEN_not_in_range_and_enabled_WHEN_in_range_set_to_nonsense_THEN_runcontrol_state_is_enabled_in_range() {
        // Arrange
        valueObservable.setConnectionStatus(true);
        enabledObservable.setValue("YES");
        inRangeObservable.setValue("NO");

		// Act
		inRangeObservable.setValue("maybe");
		
		// Assert
        assertEquals(RuncontrolState.ENABLED_IN_RANGE, displayBlock.getRuncontrolState());
	}

    @Test
    public void GIVEN_pv_disconnected_WHEN_set_to_in_range_and_enabled_THEN_runcontrol_state_is_disconnected() {

        // Arrange
        valueObservable.setConnectionStatus(false);

        // Act
        enabledObservable.setValue("YES");
        inRangeObservable.setValue("YES");

        // Assert
        assertEquals(RuncontrolState.DISCONNECTED, displayBlock.getRuncontrolState());
    }

    @Test
    public void GIVEN_pv_disconnected_WHEN_set_to_not_in_range_and_enabled_THEN_runcontrol_state_is_disconnected() {

        // Arrange
        valueObservable.setConnectionStatus(false);

        // Act
        enabledObservable.setValue("YES");
        inRangeObservable.setValue("NO");

        // Assert
        assertEquals(RuncontrolState.DISCONNECTED, displayBlock.getRuncontrolState());
    }

    @Test
    public void GIVEN_pv_disconnected_WHEN_set_to_in_range_and_not_enabled_THEN_runcontrol_state_is_disconnected() {

        // Arrange
        valueObservable.setConnectionStatus(false);

        // Act
        enabledObservable.setValue("NO");
        inRangeObservable.setValue("YES");

        // Assert
        assertEquals(RuncontrolState.DISCONNECTED, displayBlock.getRuncontrolState());
    }

    @Test
    public void GIVEN_pv_disconnected_WHEN_set_to_not_in_range_and_not_enabled_THEN_runcontrol_state_is_disconnected() {

        // Arrange
        valueObservable.setConnectionStatus(false);

        // Act
        enabledObservable.setValue("NO");
        inRangeObservable.setValue("NO");

        // Assert
        assertEquals(RuncontrolState.DISCONNECTED, displayBlock.getRuncontrolState());
    }
}
