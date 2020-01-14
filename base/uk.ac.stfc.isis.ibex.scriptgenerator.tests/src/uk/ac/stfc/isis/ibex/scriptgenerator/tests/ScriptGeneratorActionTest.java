package uk.ac.stfc.isis.ibex.scriptgenerator.tests;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.CoreMatchers.equalTo;

import org.junit.Before;
import org.junit.Test;

import uk.ac.stfc.isis.ibex.scriptgenerator.ActionParameter;
import uk.ac.stfc.isis.ibex.scriptgenerator.table.ScriptGeneratorAction;

public class ScriptGeneratorActionTest {

	ScriptGeneratorAction action;
	HashMap<ActionParameter, String> values;
	
	@Before
	public void setUp() {
		values = new HashMap<>();
		values.put(new ActionParameter("actionParam1"), "actionParam1Val");
		values.put(new ActionParameter("actionParam2"), "actionParam2Val");
		values.put(new ActionParameter("actionParam3"), "actionParam3Val");
		
		action = new ScriptGeneratorAction(values);
	}
	
	@Test
	public void test_WHEN_create_new_action_with_old_action_THEN_action_param_values_the_same() {
		// Act
		ScriptGeneratorAction newAction = new ScriptGeneratorAction(action);
		
		// Assert
		for(Entry<ActionParameter, String> entry : values.entrySet()) {
			assertThat("Entries in new action should be the same", entry.getValue(), 
					equalTo(newAction.getActionParameterValue(entry.getKey())));
		}
		assertThat("Should be the same number of action parameters",
				newAction.getAllActionParameters().size(), equalTo(values.size()));
	}
	
	@Test
	public void test_WHEN_getting_action_params_as_strings_THEN_are_expected_strings() {
		// Act
		Map<String, String> actionParamsAsStrings = action.getAllActionParametersAsStrings();
		
		// Assert
		HashMap<String, String> expectedParams = new HashMap<>();
		expectedParams.put("actionParam1", "actionParam1Val");
		expectedParams.put("actionParam2", "actionParam2Val");
		expectedParams.put("actionParam3", "actionParam3Val");
		
		assertThat("Should convert action params to their name",
				actionParamsAsStrings, equalTo(expectedParams));
		
	}
	
	@Test
	public void test_WHEN_created_THEN_is_valid() {
		// Assert
		assertThat("Should be valid at beginning", action.isValid(), is(true));
		assertThat("Should return null when valid", action.getInvalidityReason(), nullValue());
	}
	
	@Test
	public void test_WHEN_set_invalid_THEN_is_not_valid_AND_reason_is_as_expected() {
		// Act
		action.setInvalid("invalid");
		
		// Assert
		assertThat("Should be invalid after set", action.isValid(), is(false));
		assertThat("Should be invalid after set", action.getInvalidityReason(), equalTo("invalid"));
	}
	
	@Test
	public void test_WHEN_set_invalid_then_set_valid_THEN_is_valid() {
		// Act
		action.setInvalid("invalid");
		action.setValid();
		
		// Assert
		assertThat("Should be valid at beginning", action.isValid(), is(true));
		assertThat("Should return null when valid", action.getInvalidityReason(), nullValue());
	}
}