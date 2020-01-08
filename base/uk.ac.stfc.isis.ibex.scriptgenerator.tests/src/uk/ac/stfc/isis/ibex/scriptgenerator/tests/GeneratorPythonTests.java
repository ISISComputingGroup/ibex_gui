package uk.ac.stfc.isis.ibex.scriptgenerator.tests;

import java.util.ArrayList;
import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;

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
		when(mockPythonInterface.getValidityErrors(
				mockActionsTable.getActions(), mockConfig)
				).thenReturn(paramsValidity);
	}

	
	@Test
	public void test_WHEN_error_messages_are_empty_THEN_generator_returns_valid() {
		// Act and Assert
		assertThat(generator.areParamsValid(mockActionsTable, mockConfig), is(false));
	}

}
