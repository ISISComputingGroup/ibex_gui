package uk.ac.stfc.isis.ibex.scriptgenerator.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.nullValue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import uk.ac.stfc.isis.ibex.scriptgenerator.ActionParameter;
import uk.ac.stfc.isis.ibex.scriptgenerator.table.ActionsTable;

/**
 * 
 * This class tests the ActionsTable class, which holds the data for the script generator.
 *
 */
public class ActionsTableTest {

	private ActionsTable table;
	private List<ActionParameter> actionParameters;
	
	@Before
	public void setUp() {
		table = new ActionsTable(new ArrayList<ActionParameter>());
		
		var testParameter = new ActionParameter("Test Parameter");
		
		actionParameters = new ArrayList<ActionParameter>();
		actionParameters.add(testParameter);
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
		
		table.duplicateAction(0);
		
		var allActions = table.getActions();
		
		assertEquals(table.getActions().size(), 2);
		
		assertEquals(allActions.get(0).getAllActionParameters(), allActions.get(1).getAllActionParameters());
	}
	
	@Test
	public void test_GIVEN_action_in_table_WHEN_action_deleted_THEN_action_is_removed() {
		table.addEmptyAction();

		table.deleteAction(0);
		
		assertEquals(table.getActions().size(), 0);
		
	}
	
	@Test
	public void test_GIVEN_action_in_table_WHEN_action_moved_THEN_action_moves_in_table() {
		// Add two actions
		table.addEmptyAction();
		table.addEmptyAction();
		
		var secondAction = table.getActions().get(1);
		
		table.moveAction(1, 0);
		
		assertEquals(table.getActions().indexOf(secondAction), 0);
	}
	
	@Test
	public void test_GIVEN_action_at_top_of_table_WHEN_action_moved_up_THEN_action_does_not_move() {
		// Add two actions
		table.addEmptyAction();
		table.addEmptyAction();
		
		var firstAction = table.getActions().get(0);
		
		table.moveAction(0, -1);
		
		assertEquals(table.getActions().indexOf(firstAction), 0);
	}
	
	@Test
	public void test_GIVEN_action_at_bottom_of_table_WHEN_action_moved_down_THEN_action_does_not_move() {
		// Add two actions
		table.addEmptyAction();
		table.addEmptyAction();
		
		var secondAction = table.getActions().get(1);
		
		table.moveAction(1, 2);
		
		assertEquals(table.getActions().indexOf(secondAction), 1);
	}
	
	@Test
	public void test_WHEN_actions_invalid_THEN_errors_returned_are_correct() {
		// Arrange
		table.addEmptyAction();
		table.addEmptyAction();
		table.addEmptyAction();
		table.addEmptyAction();
		table.getActions().get(1).setInvalid("invalid 1");
		table.getActions().get(3).setInvalid("invalid 2");
		
		// Act
		ArrayList<String> actualValidityErrors = table.getInvalidityErrorLines();
		
		// Assert
		String[] arrayExpectedValidityErrors = {"Row: 2, Reason: invalid 1\n", "Row: 4, Reason: invalid 2\n"};
		List<String> expectedValidityErrors = Arrays.asList(arrayExpectedValidityErrors);
		assertThat("We expect validity errors to match those set into actions", actualValidityErrors, equalTo(expectedValidityErrors));
	}
	
	@Test
	public void test_WHEN_setting_actions_as_invalid_THEN_correct_actions_are_invalid() {
		// Arrange
		table.addEmptyAction();
		table.addEmptyAction();
		table.addEmptyAction();
		table.addEmptyAction();
		HashMap<Integer, String> validityErrors = new HashMap<Integer, String>();
		validityErrors.put(0, "invalid 1");
		validityErrors.put(3, "invalid 2");
		validityErrors.put(2, "invalid 3");
		
		// Act
		table.setValidityErrors(validityErrors);
		
		// Assert
		assertThat(table.getActions().get(0).isValid(), is(false));
		assertThat(table.getActions().get(0).getInvalidityReason(), equalTo("invalid 1"));
		assertThat(table.getActions().get(1).isValid(), is(true));
		assertThat(table.getActions().get(1).getInvalidityReason(), nullValue());
		assertThat(table.getActions().get(2).isValid(), is(false));
		assertThat(table.getActions().get(2).getInvalidityReason(), equalTo("invalid 3"));
		assertThat(table.getActions().get(3).isValid(), is(false));
		assertThat(table.getActions().get(3).getInvalidityReason(), equalTo("invalid 2"));
	}

}
