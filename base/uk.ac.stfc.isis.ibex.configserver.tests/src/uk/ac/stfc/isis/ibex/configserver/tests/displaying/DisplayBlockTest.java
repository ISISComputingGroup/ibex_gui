
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
import uk.ac.stfc.isis.ibex.configserver.displaying.BlockState;
import uk.ac.stfc.isis.ibex.configserver.displaying.DisplayBlock;
import uk.ac.stfc.isis.ibex.epics.observing.ForwardingObservable;
import uk.ac.stfc.isis.ibex.epics.observing.Observable;

@SuppressWarnings({ "unchecked", "checkstyle:methodname" })
public class DisplayBlockTest {
	
	TestableIOSObservable<String> inRangeObservable;
    TestableIOSObservable<String> enabledObservable;
    TestableIOSObservable<String> disconnectedObservable;
	
	DisplayBlock displayBlock;
	
	@Before
	public void setUp() {
		// Arrange
		inRangeObservable = new TestableIOSObservable<>(mock(Observable.class));
        enabledObservable = new TestableIOSObservable<>(mock(Observable.class));
		
		displayBlock = new DisplayBlock(
                mock(Block.class), // block
				mock(ForwardingObservable.class),  // value
				mock(ForwardingObservable.class),  // description
                inRangeObservable, // inRange
				mock(ForwardingObservable.class),  // lowLimit
				mock(ForwardingObservable.class),  // highLimit
                enabledObservable, // enabled
				"");		
	}
	
	@Test
    public void if_in_range_and_disabled_unset_then_state_is_disabled() {
        // Act
        enabledObservable.setValue("NO");
        inRangeObservable.setValue("YES");
		
		// Assert
        assertEquals(BlockState.RUNCONTROL_DISABLED, displayBlock.blockState());
	}
	
	@Test
    public void if_in_range_while_enabled_then_state_is_in_range() {
        // Act
        enabledObservable.setValue("YES");
        inRangeObservable.setValue("YES");
		
		// Assert
        assertEquals(BlockState.RUNCONTROL_ENABLED_IN_RANGE, displayBlock.blockState());
	}
	
	@Test
    public void if_not_in_range_while_enabled_then_out_range() {
		// Act
        enabledObservable.setValue("YES");
		inRangeObservable.setValue("NO");
		
		// Assert
        assertEquals(BlockState.RUNCONTROL_ENABLED_OUT_RANGE, displayBlock.blockState());
	}
	
	@Test
    public void if_in_range_to_false_then_true_sets_in_range() {
        // Arrange
        enabledObservable.setValue("YES");
        inRangeObservable.setValue("NO");

        // Act
		inRangeObservable.setValue("YES");
		
		// Assert
        assertEquals(BlockState.RUNCONTROL_ENABLED_IN_RANGE, displayBlock.blockState());
	}

	@Test
    public void setting_in_range_to_true_then_false_sets_out_range() {
        // Arrange
        enabledObservable.setValue("YES");
        inRangeObservable.setValue("YES");

		// Act
		inRangeObservable.setValue("NO");
		
		// Assert
        assertEquals(BlockState.RUNCONTROL_ENABLED_OUT_RANGE, displayBlock.blockState());
	}

	@Test
    public void setting_in_range_to_false_then_nonsense_sets_in_range() {
        // Setting InRange to a nonsense value should default to true.

        // Arrange
        enabledObservable.setValue("YES");
        inRangeObservable.setValue("NO");

		// Act
		inRangeObservable.setValue("maybe");
		
		// Assert
        assertEquals(BlockState.RUNCONTROL_ENABLED_IN_RANGE, displayBlock.blockState());
	}

}
