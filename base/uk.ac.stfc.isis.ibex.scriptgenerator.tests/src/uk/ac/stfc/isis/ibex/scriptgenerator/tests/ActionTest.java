package uk.ac.stfc.isis.ibex.scriptgenerator.tests;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;

import uk.ac.stfc.isis.ibex.scriptgenerator.table.ScriptGeneratorAction;

public class ActionTest {

	private ScriptGeneratorAction action;
	
	private String testParameterName = "Test value";
	
	@Before
	public void setUp() {
		var testValues = new HashMap<String, String>();
		
		testValues.put(testParameterName, "1");
		
		this.action = new ScriptGeneratorAction(testValues);
	}
	
	@Test
	public void test_GIVEN_action_WHEN_action_parameters_set_THEN_action_parameters_update() {
		var updatedValue = "2";
		action.setActionParameterValue("Test value", updatedValue);
		
		assertEquals(action.getActionParameterValue(testParameterName), updatedValue);
	}
	
}
