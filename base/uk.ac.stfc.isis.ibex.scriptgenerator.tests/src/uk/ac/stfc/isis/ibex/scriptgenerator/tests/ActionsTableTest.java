package uk.ac.stfc.isis.ibex.scriptgenerator.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;

import uk.ac.stfc.isis.ibex.scriptgenerator.JavaActionParameter;
import uk.ac.stfc.isis.ibex.scriptgenerator.table.ActionsTable;

/**
 * 
 * This class tests the ActionsTable class, which holds the data for the script generator.
 *
 */
public class ActionsTableTest {

	private ActionsTable table;
	private List<JavaActionParameter> actionParameters;
	private JavaActionParameter testParameter;
	
	@Before
	public void setUp() {		
		testParameter = new JavaActionParameter("Test Parameter", "test value", false);
		
		actionParameters = new ArrayList<JavaActionParameter>();
		actionParameters.add(testParameter);
		
		table = new ActionsTable(actionParameters);
	}
	
	private void addEmptyActions(int numOfActions) {
		for(int i = 0; i < numOfActions; i++) {
			table.addEmptyAction();
		}
	}
		
	@Test
	public void test_GIVEN_empty_table_WHEN_action_added_THEN_action_appears_in_table() {
		var numberOfActionsBefore = table.getActions().size();
		
		table.addEmptyAction();
		
		var numberOfActionsAfter = table.getActions().size();
		

        assertTrue(numberOfActionsAfter > numberOfActionsBefore);
        assertEquals(numberOfActionsAfter, 1);
	}
	
	@Test
	public void test_GIVEN_action_duplicated_WHEN_action_selected_THEN_action_is_duplicated() {
		table.addEmptyAction();
		
		table.duplicateAction(table.getActions(), 1);
		
		var allActions = table.getActions();
		
		assertEquals(table.getActions().size(), 2);
		
		assertEquals(allActions.get(0).getActionParameterValueMap(), allActions.get(1).getActionParameterValueMap());
	}
	
	@Test
	public void test_GIVEN_actions_duplicated_WHEN_multiple_actions_selected_THEN_actions_are_duplicated() {
		addEmptyActions(2);
		
		table.duplicateAction(table.getActions(), 2);
		
		var allActions = table.getActions();
		
		assertEquals(table.getActions().size(), 4);
		
		assertEquals(allActions.get(0).getActionParameterValueMap(), allActions.get(2).getActionParameterValueMap());
	}
	
	@Test
	public void test_GIVEN_action_in_table_WHEN_action_deleted_THEN_action_is_removed() {
		table.addEmptyAction();

		table.deleteAction(table.getActions());
		
		assertEquals(table.getActions().size(), 0);
		
	}
	
	@Test
	public void test_GIVEN_actions_in_table_WHEN_all_actions_deleted_THEN_actions_are_removed() {
		addEmptyActions(3);

		table.deleteAction(table.getActions());
		
		assertEquals(table.getActions().size(), 0);
		
	}
	
	@Test
	public void test_GIVEN_action_in_table_WHEN_action_moved_THEN_action_moves_in_table() {
		// Add two actions
		addEmptyActions(2);
		
		var secondAction = table.getActions().get(1);
		
		table.moveActionUp(Arrays.asList(secondAction));
		
		assertEquals(table.getActions().indexOf(secondAction), 0);
	}
	
	@Test
	public void test_GIVEN_actions_in_table_WHEN_multiple_action_moved_THEN_actions_move_in_table() {
		// Add six actions
		addEmptyActions(6);

		var firstAction = table.getActions().get(0);
		var secondAction = table.getActions().get(1);
		var thirdAction = table.getActions().get(2);
		var fourthAction = table.getActions().get(3);
		
		table.moveActionUp(Arrays.asList(secondAction, thirdAction, fourthAction));
		
		assertEquals(0, table.getActions().indexOf(secondAction));
		assertEquals(1, table.getActions().indexOf(thirdAction));
		assertEquals(2, table.getActions().indexOf(fourthAction));
		
		table.moveActionDown(Arrays.asList(firstAction, secondAction, thirdAction));
		
		assertEquals(1, table.getActions().indexOf(secondAction));
		assertEquals(2, table.getActions().indexOf(thirdAction));
		assertEquals(0, table.getActions().indexOf(fourthAction));
	}
	
	@Test
	public void test_GIVEN_action_at_top_of_table_WHEN_action_moved_up_THEN_action_does_not_move() {
		// Add two actions
		addEmptyActions(2);
		
		var firstAction = table.getActions().get(0);
		
		table.moveActionUp(Arrays.asList(firstAction));
		
		assertEquals(0, table.getActions().indexOf(firstAction));
	}
	
	@Test
	public void test_GIVEN_one_of_multiple_selected_actions_at_top_of_table_WHEN_action_moved_up_THEN_action_does_not_move() {
		// Add three actions
		addEmptyActions(3);
		
		var firstAction = table.getActions().get(0);
		var secondAction = table.getActions().get(1);
		
		table.moveActionUp(Arrays.asList(firstAction, secondAction));
		
		assertEquals(0, table.getActions().indexOf(firstAction));
		assertEquals(1, table.getActions().indexOf(secondAction));
	}
	
	@Test
	public void test_GIVEN_action_at_bottom_of_table_WHEN_action_moved_down_THEN_action_does_not_move() {
		// Add two actions
		addEmptyActions(2);
		
		var secondAction = table.getActions().get(1);
		
		table.moveActionDown(Arrays.asList(secondAction));
		
		assertEquals(1, table.getActions().indexOf(secondAction));
	}
	
	@Test
	public void test_GIVEN_one_of_multiple_selected_actions_at_bottom_of_table_WHEN_action_moved_down_THEN_action_does_not_move() {
		// Add three actions
		addEmptyActions(3);
		
		var secondAction = table.getActions().get(1);
		var thirdAction = table.getActions().get(2);
		
		table.moveActionDown(Arrays.asList(secondAction, thirdAction));
		
		assertEquals(1, table.getActions().indexOf(secondAction));
		assertEquals(2, table.getActions().indexOf(thirdAction));
	}
	
	@Test
	public void test_WHEN_actions_invalid_THEN_errors_returned_are_correct() {
		// Arrange
		addEmptyActions(4);
		table.getActions().get(1).setInvalid("invalid 1");
		table.getActions().get(3).setInvalid("invalid 2");
		
		// Act
		ArrayList<String> actualValidityErrors = table.getInvalidityErrorLines();
		
		// Assert
		String[] arrayExpectedValidityErrors = {"Action Errors:", "Row: 2, Reason: ", "invalid 1", "Row: 4, Reason: ", "invalid 2"};
		List<String> expectedValidityErrors = Arrays.asList(arrayExpectedValidityErrors);
		assertThat("We expect validity errors to match those set into actions",
				expectedValidityErrors, equalTo(actualValidityErrors));
	}
	
	@Test
	public void test_GIVEN_validity_errors_WHEN_first_map_THEN_error_is_for_globals() {
		// Arrange
		addEmptyActions(2);
		Map<Integer, String> validityErrorsGlobal = new HashMap<Integer, String>();
		Map<Integer, String> validityErrorsParam = Collections.<Integer, String>emptyMap();
		validityErrorsGlobal.put(0, "invalid 0");
		List<Map<Integer, String>> validityErrors = new ArrayList<Map<Integer, String>>();
		validityErrors.add(validityErrorsGlobal);
		validityErrors.add(validityErrorsParam);
		// Act
		table.setValidityErrors(validityErrors);
		ArrayList<String> actualValidityErrors = table.getInvalidityErrorLines();
		
		// Assert
				String[] arrayExpectedValidityErrors = {"Global Parameter Errors: ","Global Parameter: 1, Reason: ","invalid 0"};
				List<String> expectedValidityErrors = Arrays.asList(arrayExpectedValidityErrors);
				assertThat("We expect validity errors to match those set into actions",
						expectedValidityErrors, equalTo(actualValidityErrors));
	}
	
	@Test
	public void test_WHEN_setting_actions_as_invalid_THEN_correct_actions_are_invalid() {
		// Arrange
		addEmptyActions(5);
		Map<Integer, String> validityErrorsGlobal = new HashMap<Integer, String>();
		Map<Integer, String> validityErrorsParam = new HashMap<Integer, String>();
		List<Map<Integer, String>> validityErrors = new ArrayList<Map<Integer, String>>();
		validityErrors.add(validityErrorsGlobal);
		validityErrors.add(validityErrorsParam);
		
		validityErrors.get(0).put(0, "invalid 0");
		validityErrors.get(1).put(1, "invalid 1");
		validityErrors.get(1).put(4, "invalid 2");
		validityErrors.get(1).put(3, "invalid 3");
		
		// Act
		table.setValidityErrors(validityErrors);
		
		// Assert
		assertThat("We set 0 in global params to invalid, so global parameter error should be set",
				table.getGlobalValidityErrors().get(0), equalTo("invalid 0"));
		assertThat("We set 1 in params to invalid so should not be valid", 
				table.getActions().get(1).isValid(), is(false));
		assertThat("We set 1 to invalid so should give same invalidity error string",
				table.getActions().get(1).getInvalidityReason().get(), equalTo("invalid 1"));
		assertThat("We did not set 2 to invalid so should be valid",
				table.getActions().get(2).isValid(), is(true));
		assertThat("As 2 is valid should return null",
				table.getActions().get(2).getInvalidityReason(), is(Optional.empty()));
		assertThat("We set 3 to invalid so should not be valid",
				table.getActions().get(3).isValid(), is(false));
		assertThat("We set 3 to invalid so should give same invalidity error string",
				table.getActions().get(3).getInvalidityReason().get(), equalTo("invalid 3"));
		assertThat("We set 4 to invalid so should not be valid",
				table.getActions().get(4).isValid(), is(false));
		assertThat("We set 4 to invalid so should give same invalidity error string",
				table.getActions().get(4).getInvalidityReason().get(), equalTo("invalid 2"));
	}
	
	@Test
	public void test_GIVEN_multiple_actions_THEN_multiple_actions_are_added() {
		List<Map<JavaActionParameter, String>> list = new ArrayList<Map<JavaActionParameter, String>>();
		// create multiple actions
		Map<JavaActionParameter, String> exampleOne = new HashMap<JavaActionParameter, String>();
		Map<JavaActionParameter, String> exampleTwo = new HashMap<JavaActionParameter, String>();
		
		list.add(exampleOne);
		list.add(exampleTwo);
		
		table.addMultipleActions(list);
		
		assertEquals(table.getActions().size(), 2);
		
	}
	
	private void assertActionsHaveValue(Map<Integer, String> indexValueMap) {
		for (Integer index : indexValueMap.keySet()) {
			assertThat("Action should be inserted into the correct position",
					table.getActions().get(index).getActionParameterValue(testParameter), is(indexValueMap.get(index)));
		}
	}
	
	@Test
	public void test_GIVEN_multiple_actions_WHEN_insert_action_THEN_action_inserted_correctly() {
		int numOfActions = 6;
		addEmptyActions(6);
		for(int i = 0; i < numOfActions; i++) {
			table.getActions().get(i).setActionParameterValue(testParameter, "not default value");
		}
		table.insertEmptyAction(3);
		
		assertThat("We inserted an action so there should now be another in the table",
				table.getActions().size(), is(numOfActions + 1));
		assertActionsHaveValue(Map.of(3, testParameter.getDefaultValue()));
	}
	
	@Test
	public void test_GIVEN_multiple_actions_WHEN_insert_multiple_actions_THEN_action_inserted_correctly() {
		int numOfActions = 6;
		addEmptyActions(6);
		for(int i = 0; i < numOfActions; i++) {
			table.getActions().get(i).setActionParameterValue(testParameter, "not default value");
		}
		table.insertEmptyAction(3);
		table.insertEmptyAction(3);
		table.insertEmptyAction(6);
		
		assertThat("We inserted an action so there should now be another in the table",
				table.getActions().size(), is(numOfActions + 3));
		assertThat("Action should be inserted into the third position",
				table.getActions().get(3).getActionParameterValue(testParameter), is(testParameter.getDefaultValue()));
		assertActionsHaveValue(
				Map.of(
						3, testParameter.getDefaultValue(),
						4, testParameter.getDefaultValue(),
						6, testParameter.getDefaultValue()
				)
		);
	}
	
	@Test
	public void test_GIVEN_multiple_actions_WHEN_insert_action_at_beginning_THEN_action_inserted_correctly() {
		int numOfActions = 6;
		addEmptyActions(6);
		for(int i = 0; i < numOfActions; i++) {
			table.getActions().get(i).setActionParameterValue(testParameter, "not default value");
		}
		table.insertEmptyAction(-2);
		
		assertThat("We inserted an action so there should now be another in the table",
				table.getActions().size(), is(numOfActions + 1));
		assertActionsHaveValue(Map.of(0, testParameter.getDefaultValue()));
	}
	
	@Test
	public void test_GIVEN_multiple_actions_WHEN_insert_action_at_end_THEN_action_inserted_correctly() {
		int numOfActions = 6;
		addEmptyActions(6);
		for(int i = 0; i < numOfActions; i++) {
			table.getActions().get(i).setActionParameterValue(testParameter, "not default value");
		}
		table.insertEmptyAction(numOfActions + 2);
		
		assertThat("We inserted an action so there should now be another in the table",
				table.getActions().size(), is(numOfActions + 1));
		assertActionsHaveValue(Map.of(numOfActions, testParameter.getDefaultValue()));
	}

}
