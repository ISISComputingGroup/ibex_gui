package uk.ac.stfc.isis.ibex.scriptgenerator.tests;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;

import uk.ac.stfc.isis.ibex.scriptgenerator.JavaActionParameter;
import uk.ac.stfc.isis.ibex.scriptgenerator.table.ScriptGeneratorAction;

public class ActionTest {

	private ScriptGeneratorAction action;
	
	private JavaActionParameter parameterOneName = new JavaActionParameter("parameter one", "value one", false, false, null);
	private JavaActionParameter parameterTwoName = new JavaActionParameter("parameter two", "value two", false, false, null);
	
	@Before
	public void setUp() {
		var testValues = new HashMap<JavaActionParameter, String>();
		
		testValues.put(parameterOneName, "1");
		testValues.put(parameterTwoName, "12.3");
		
		this.action = new ScriptGeneratorAction(testValues);
	}
	
	@Test
	public void test_GIVEN_action_WHEN_action_parameters_set_THEN_action_parameters_update() {
		var updatedValue = "2";
		action.setActionParameterValue(parameterOneName, updatedValue);
		
		assertEquals(action.getActionParameterValue(parameterOneName), updatedValue);
	}
	
	@Test
	public void test_GIVEN_action_as_constructor_argument_THEN_action_parameter_values_are_duplicated() {
		var valueToTest = "new value";	
		
		action.setActionParameterValue(parameterTwoName, valueToTest);
				
		var duplicatedAction = new ScriptGeneratorAction(action);
		
		assertEquals(duplicatedAction.getActionParameterValueMap(), action.getActionParameterValueMap());
	}
	
}
