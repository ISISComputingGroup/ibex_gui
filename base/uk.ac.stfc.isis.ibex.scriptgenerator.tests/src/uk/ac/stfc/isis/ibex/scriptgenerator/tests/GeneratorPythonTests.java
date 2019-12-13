package uk.ac.stfc.isis.ibex.scriptgenerator.tests;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import uk.ac.stfc.isis.ibex.scriptgenerator.generation.GeneratorPython;
import uk.ac.stfc.isis.ibex.scriptgenerator.pythoninterface.Config;
import uk.ac.stfc.isis.ibex.scriptgenerator.pythoninterface.PythonInterface;
import uk.ac.stfc.isis.ibex.scriptgenerator.table.ActionsTable;
import uk.ac.stfc.isis.ibex.scriptgenerator.table.ScriptGeneratorAction;

public class GeneratorPythonTests {
	
	GeneratorPython generator;
	PythonInterface mockPythonInterface;
	ActionsTable mockActionsTable;
	HashMap<String, String> actionAsMapString;
	int numberOfActions;
	Config mockConfig;
	HashMap<Integer, String> paramsValidity;
		
	@Before
	public void setUp() {
		// Set up actions table
		mockActionsTable = mock(ActionsTable.class);
		numberOfActions = 4;
		actionAsMapString = new HashMap<String, String>();
		actionAsMapString.put("actionParam1", "actionParam1Val");
		actionAsMapString.put("actionParam2", "actionParam2Val");
		actionAsMapString.put("actionParam3", "actionParam3Val");
		var actions = new ArrayList<ScriptGeneratorAction>();
		for (int i = 0; i < numberOfActions; i++) {
			var action = mock(ScriptGeneratorAction.class);
			when(action.getAllActionParametersAsStrings()).thenReturn(actionAsMapString);
			actions.add(action);
		}
		when(mockActionsTable.getActions()).thenReturn(actions);
		
		// Set up python interface and generator
		mockPythonInterface = mock(PythonInterface.class);
		paramsValidity = new HashMap<>();
		paramsValidity.put(1, "invalid 1");
		paramsValidity.put(2, "invalid 2");
		mockConfig = mock(Config.class);
		
		// Set up generator and finish mocking python interface
		generator =  new GeneratorPython(mockPythonInterface);
		when(mockPythonInterface.areParamsValid(
				generator.convertActionsTableToPythonRepr(mockActionsTable), mockConfig)
				).thenReturn(paramsValidity);
	}
	
	@Test
	public void test_GIVEN_actions_table_WHEN_converting_to_python_repr_THEN_contents_as_expected() {
		// Act
		List<Map<String, String>> pythonReprActionsTable = generator.convertActionsTableToPythonRepr(mockActionsTable);
		
		// Assert
		assertThat("Should be four actions in table",
				pythonReprActionsTable.size(), is(4));
		for(Map<String, String> action : pythonReprActionsTable) {
			assertThat("Should be equal to contents of actions",
					action, equalTo(actionAsMapString));
		}
	}
	
	@Test
	public void test_WHEN_error_messages_are_empty_THEN_generator_returns_valid() {
		// Act and Assert
		assertThat(generator.areParamsValid(mockActionsTable, mockConfig), is(false));
	}
	
	@Test
	public void test_WHEN_error_messages_not_empty_THEN_generator_returns_invalid() {
		// Arrange
		when(mockPythonInterface.areParamsValid(
				generator.convertActionsTableToPythonRepr(mockActionsTable), mockConfig)
				).thenReturn(new HashMap<>());
		
		// Act and Assert
		assertThat(generator.areParamsValid(mockActionsTable, mockConfig), is(true));
	}

}
