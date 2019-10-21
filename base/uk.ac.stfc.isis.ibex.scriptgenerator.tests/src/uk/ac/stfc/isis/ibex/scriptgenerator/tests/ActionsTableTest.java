package uk.ac.stfc.isis.ibex.scriptgenerator.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import uk.ac.stfc.isis.ibex.scriptgenerator.ActionParameter;
import uk.ac.stfc.isis.ibex.scriptgenerator.table.ActionsTable;
import uk.ac.stfc.isis.ibex.scriptgenerator.table.ScriptGeneratorAction;

/**
 * 
 * This class tests the ActionsTable class, which holds the data for the script generator
 *
 */
public class ActionsTableTest {

	private ActionsTable table;
	private List<ActionParameter> actionParameters;
	
	private ScriptGeneratorAction createMockedAction(String actionId) {
		ScriptGeneratorAction action = mock(ScriptGeneratorAction.class);
				
		when(action.getActionParameterValue(anyString())).thenReturn(actionId);
		return action;
	}
	
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
		
		table.addEmptyAction(actionParameters);
		
		var numberOfActionsAfter = table.getActions().size();
		

        assertTrue(numberOfActionsAfter > numberOfActionsBefore);
	}
	
	@Test
	public void test_GIVEN_action_duplicated_WHEN_action_selected_THEN_action_is_duplicated() {
		table.addEmptyAction(actionParameters);
		
		table.duplicateAction(0);
		
		var allActions = table.getActions();
		
		assertEquals(table.getActions().size(), 2);
		
		assertEquals(allActions.get(0).getAllActionParameters(), allActions.get(1).getAllActionParameters());
	}
	
//	public void test_GIVEN_selected_action_is_not_at_bottom_of_list_WHEN_action_moved_up_THEN_action_moves_up() {
//		table.setActionParameters(actionParameters);
//	}
//	
}
