package uk.ac.stfc.isis.ibex.scriptgenerator.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
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

}
