package uk.ac.stfc.isis.ibex.scriptgenerator.tests;

import java.util.ArrayList;
import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import uk.ac.stfc.isis.ibex.scriptgenerator.ScriptGeneratorSingleton;
import uk.ac.stfc.isis.ibex.scriptgenerator.pythoninterface.ConfigLoader;
import uk.ac.stfc.isis.ibex.scriptgenerator.pythoninterface.PythonInterface;
import uk.ac.stfc.isis.ibex.scriptgenerator.table.ActionsTable;

public class ScriptGeneratorSingletonTest {
	
	ScriptGeneratorSingleton model;
	HashMap<Integer, String> validityErrors;
	ActionsTable mockActionsTable;
	PythonInterface mockPythonInterface;
	ConfigLoader mockConfigLoader;
	
	@Before
	public void setUp() {
		mockPythonInterface = mock(PythonInterface.class);
		mockConfigLoader = mock(ConfigLoader.class);
		mockActionsTable = mock(ActionsTable.class);
		model = new ScriptGeneratorSingleton(mockPythonInterface, mockConfigLoader, mockActionsTable);
	}
	
	@Test
	public void test_GIVEN_invalidity_errors_from_table_WHEN_get_first_n_lines_THEN_contents_as_expected() {
		// Arrange
		var mockErrorsList = new ArrayList<String>();
		mockErrorsList.add("Row: 1, Reason: invalid 1");
		mockErrorsList.add("Row: 2, Reason: invalid 2");
		when(mockActionsTable.getInvalidityErrorLines()).thenReturn(mockErrorsList);
		
		// Act
		String invalidityErrors = model.getFirstNLinesOfInvalidityErrors(5);
		
		// Assert
		assertThat("Errors should reflect those returned by actions table",
				invalidityErrors, equalTo("- Row: 1, Reason: invalid 1\n- Row: 2, Reason: invalid 2"));
	}
	
	@Test
	public void test_GIVEN_more_than_n_lines_WHEN_get_first_n_lines_THEN_limited_and_user_notified() {
		// Arrange
		var mockErrorsList = new ArrayList<String>();
		mockErrorsList.add("Row: 1, Reason: invalid 1");
		mockErrorsList.add("Row: 2, Reason: invalid 2");
		mockErrorsList.add("Row: 3, Reason: invalid 3");
		mockErrorsList.add("Row: 4, Reason: invalid 4");
		when(mockActionsTable.getInvalidityErrorLines()).thenReturn(mockErrorsList);
		
		
		// Act
		int maxNumOfLines = 2;
		String invalidityErrors = model.getFirstNLinesOfInvalidityErrors(maxNumOfLines);
		
		// Assert
		assertThat("Errors should reflect those put in actions table",
				invalidityErrors, equalTo("- Row: 1, Reason: invalid 1\n- Row: 2, Reason: invalid 2\n\n ... plus 2 suppressed errors."
						+ " To see an error for a specific row hover over it."));
		assertThat("Should be limited to 2 lines and two other new lines for notification",
				invalidityErrors.split("\n").length, is(maxNumOfLines+2));
	}
	
	@Test
	public void test_GIVEN_less_than_n_lines_WHEN_get_first_n_lines_THEN_not_limited_user_not_notified() {
		// Arrange
		var mockErrorsList = new ArrayList<String>();
		mockErrorsList.add("Row: 1, Reason: invalid 1");
		mockErrorsList.add("Row: 2, Reason: invalid 2");
		when(mockActionsTable.getInvalidityErrorLines()).thenReturn(mockErrorsList);
		
		// Act
		int maxNumOfLines = 10;
		String invalidityErrors = model.getFirstNLinesOfInvalidityErrors(maxNumOfLines);
		
		// Assert
		assertThat("Errors should reflect those put in actions table",
				invalidityErrors, equalTo("- Row: 1, Reason: invalid 1\n- Row: 2, Reason: invalid 2"));
		assertThat("Should not be limited",
				invalidityErrors.split("\n").length, is(2));
	}

}
