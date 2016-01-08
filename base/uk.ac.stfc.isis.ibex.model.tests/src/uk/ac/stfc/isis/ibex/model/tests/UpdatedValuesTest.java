
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

package uk.ac.stfc.isis.ibex.model.tests;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import uk.ac.stfc.isis.ibex.model.SettableUpdatedValue;
import uk.ac.stfc.isis.ibex.model.UpdatedValues;

public class UpdatedValuesTest {

	private SettableUpdatedValue<String> first;
	private SettableUpdatedValue<String> second;
	
	private boolean wasRun;	
	private Runnable allSetAction = new Runnable() {
		@Override
		public void run() {
			wasRun = true;
		}
	};
	
	@Before
	public void setup() {
		first = new SettableUpdatedValue<>();
		second = new SettableUpdatedValue<>();
		
		UpdatedValues.awaitValues(first, second).onAllSet(allSetAction);
	}
	
	@Test
	public void action_performed_when_just_a_single_value_is_set() {
		UpdatedValues.awaitValues(first).onAllSet(allSetAction);
		
		assertFalse(wasRun);
		first.setValue("1");
		assertTrue(wasRun);
	}
	
	@Test
	public void action_performed_when_all_values_are_set() {
		assertFalse(wasRun);
		first.setValue("1");
		assertFalse(wasRun);
		second.setValue("2");
		assertTrue(wasRun);
	}

	@Test
	public void action_performed_when_all_values_are_set_in_reverse_order() {
		assertFalse(wasRun);
		second.setValue("2");
		assertFalse(wasRun);
		first.setValue("1");
		assertTrue(wasRun);
	}
	
	@Test
	public void if_all_values_are_already_set_then_action_is_performed_immediately() {
		first.setValue("1");
		second.setValue("2");
		
		wasRun = false;
		UpdatedValues.awaitValues(first, second).onAllSet(allSetAction);
		
		assertTrue(wasRun);
	}
	
	@Test
	public void if_all_values_are_not_already_set_then_action_is_not_called_immediately() {
		first.setValue("1");
		
		wasRun = false;
		UpdatedValues.awaitValues(first, second).onAllSet(allSetAction);
		
		assertFalse(wasRun);
	}
}
