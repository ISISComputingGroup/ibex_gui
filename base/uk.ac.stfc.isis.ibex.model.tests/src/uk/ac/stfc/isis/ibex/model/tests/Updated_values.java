package uk.ac.stfc.isis.ibex.model.tests;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import uk.ac.stfc.isis.ibex.model.SettableUpdatedValue;
import uk.ac.stfc.isis.ibex.model.UpdatedValues;

public class Updated_values {

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
