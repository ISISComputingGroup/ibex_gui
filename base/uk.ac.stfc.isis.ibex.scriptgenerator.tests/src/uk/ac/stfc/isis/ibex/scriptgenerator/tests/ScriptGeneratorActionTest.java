package uk.ac.stfc.isis.ibex.scriptgenerator.tests;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.CoreMatchers.equalTo;

import org.junit.Before;
import org.junit.Test;

import uk.ac.stfc.isis.ibex.scriptgenerator.JavaActionParameter;
import uk.ac.stfc.isis.ibex.scriptgenerator.table.ScriptGeneratorAction;

public class ScriptGeneratorActionTest {

	ScriptGeneratorAction action;
	HashMap<JavaActionParameter, String> values;
	
	@Before
	public void setUp() {
		var actionParam1 = new JavaActionParameter("actionParam1", "actionParam1DefaultVal", false, false, null);
		var actionParam2 = new JavaActionParameter("actionParam2", "actionParam2DefaultVal", false, false, null);
		var actionParam3 = new JavaActionParameter("actionParam3", "actionParam3DefaultVal", false, false, null);
		
		values = new HashMap<>();
		values.put(actionParam1, actionParam1.getDefaultValue());
		values.put(actionParam2, actionParam2.getDefaultValue());
		values.put(actionParam3, actionParam3.getDefaultValue());
		
		action = new ScriptGeneratorAction(values);
	}
	
	@Test
	public void test_WHEN_create_new_action_with_old_action_THEN_action_param_values_the_same() {
		// Act
		ScriptGeneratorAction newAction = new ScriptGeneratorAction(action);
		
		// Assert
		for(Entry<JavaActionParameter, String> entry : values.entrySet()) {
			assertThat("Entries in new action should be the same", entry.getValue(), 
					equalTo(newAction.getActionParameterValue(entry.getKey())));
		}
		assertThat("Should be the same number of action parameters",
				newAction.getActionParameterValueMap().size(), equalTo(values.size()));
	}
	
	@Test
	public void test_WHEN_getting_action_params_THEN_are_expected_strings() {
		// Act
		Map<JavaActionParameter, String> actionParamsAsStrings = action.getActionParameterValueMap();
		
		// Assert
		HashMap<JavaActionParameter, String> expectedParams = new HashMap<>();
		expectedParams.put(new JavaActionParameter("actionParam1", "actionParam1DefaultVal", false, false, null), "actionParam1DefaultVal");
		expectedParams.put(new JavaActionParameter("actionParam2", "actionParam2DefaultVal", false, false, null), "actionParam2DefaultVal");
		expectedParams.put(new JavaActionParameter("actionParam3", "actionParam3DefaultVal", false, false, null), "actionParam3DefaultVal");
		
		assertThat("Should convert action params to their name",
				actionParamsAsStrings, equalTo(expectedParams));
		
	}
	
	@Test
	public void test_WHEN_created_THEN_is_valid() {
		// Assert
		assertThat("Should be valid at beginning", action.isValid(), is(true));
		assertThat("Should return null when valid", action.getInvalidityReason(), is(Optional.empty()));
	}
	
	@Test
	public void test_WHEN_set_invalid_THEN_is_not_valid_AND_reason_is_as_expected() {
		// Act
		action.setInvalid("invalid");
		
		// Assert
		assertThat("Should be invalid after set", action.isValid(), is(false));
		assertThat("Should be invalid after set", action.getInvalidityReason().get(), equalTo("invalid"));
	}
	
	@Test
	public void test_WHEN_set_invalid_then_set_valid_THEN_is_valid() {
		// Act
		action.setInvalid("invalid");
		action.setValid();
		
		// Assert
		assertThat("Should be valid at beginning", action.isValid(), is(true));
		assertThat("Should return null when valid", action.getInvalidityReason(), is(Optional.empty()));
	}
	
	@Test
	public void test_WHEN_created_THEN_default_value_is_value() {
		for (JavaActionParameter actionParameter : values.keySet()) {
			// Assert
			assertThat("Should initially use the default value",
					action.getActionParameterValue(actionParameter),
					is(actionParameter.getDefaultValue())
			);
		}
	}
	
	@Test
	public void test_WHEN_value_changed_THEN_no_longer_default_value() {
		// Arrange
		String newValue = "different";
		for (JavaActionParameter actionParameter : values.keySet()) {
			// Act
			action.setActionParameterValue(actionParameter, newValue);
			// Assert
			assertThat("Should no longer use default value",
					action.getActionParameterValue(actionParameter),
					is(not(actionParameter.getDefaultValue()))
			);
			assertThat("Should use new value",
					action.getActionParameterValue(actionParameter),
					is(newValue)
			);
		}
	}
}
