package uk.ac.stfc.isis.ibex.scriptgenerator.tests;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.After;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import uk.ac.stfc.isis.ibex.scriptgenerator.NoScriptDefinitionSelectedException;
import uk.ac.stfc.isis.ibex.scriptgenerator.ScriptGeneratorSingleton;
import uk.ac.stfc.isis.ibex.scriptgenerator.pythoninterface.ScriptDefinitionWrapper;
import uk.ac.stfc.isis.ibex.scriptgenerator.pythoninterface.ScriptDefinitionLoader;
import uk.ac.stfc.isis.ibex.scriptgenerator.pythoninterface.ActionParameter;
import uk.ac.stfc.isis.ibex.scriptgenerator.pythoninterface.PythonInterface;
import uk.ac.stfc.isis.ibex.scriptgenerator.table.ActionsTable;

public class ScriptGeneratorSingletonTest {

	ScriptGeneratorSingleton model;
	HashMap<Integer, String> validityErrors;
	ActionsTable mockActionsTable;
	PythonInterface mockPythonInterface;
	ScriptDefinitionLoader mockConfigLoader;
	
	ScriptGeneratorSingleton mockModel;
	String scriptLines;
	String jsonLine;
	ScriptDefinitionWrapper mockConfig;
	String configName;
	String filepathPrefix;
	
	@Before
	public void setUp() {
		mockPythonInterface = mock(PythonInterface.class);
		mockConfigLoader = mock(ScriptDefinitionLoader.class);
		mockActionsTable = mock(ActionsTable.class);
		
		// Mock out the model to test some methods and make real calls to others
		mockModel = mock(ScriptGeneratorSingleton.class);
		scriptLines = "test\ntest2";
		jsonLine = "{\"test:me\"}";
		mockConfig = mock(ScriptDefinitionWrapper.class);
		configName = "config";
		List<ActionParameter> list = Collections.emptyList();
		when(mockConfig.getGlobalParameters()).thenReturn(list);
		when(mockConfigLoader.getScriptDefinition()).thenReturn(mockConfig);
		model = new ScriptGeneratorSingleton(mockPythonInterface, mockConfigLoader, mockActionsTable);
		
		filepathPrefix = (System.getProperty("user.dir") + "\\test_script_gen_handler_scripts\\");
		if(!(new File(filepathPrefix).mkdir())) {
			fail("We need to create this directory to write files to it");
		}
		
		// Mock out some methods to test real calls to others
		when(mockModel.getScriptDefinition()).thenReturn(Optional.of(mockConfig));
		when(mockConfig.getName()).thenReturn(configName);
		
		
		mockConfig.getGlobalParameters();
		try {
			when(mockModel.generateScriptFileName()).thenCallRealMethod();
		} catch (NoScriptDefinitionSelectedException e) {
			fail("We have mocked out getConfig() so should always return a config");
		}
	}
	
	@After
	public void tearDown() {
		File test_scripts_folder = new File(filepathPrefix);
		if(test_scripts_folder.exists()) {
			for (String entry : test_scripts_folder.list()) {
				if(!new File(test_scripts_folder.getPath(), entry).delete()) {
					fail("Failed to delete file " + entry);
				}
			}
			if(!test_scripts_folder.delete()) {
				fail("Failed to delete folder " + test_scripts_folder.getPath());
			}
		}
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
	
	@Test
	public void test_WHEN_generate_THEN_filename_with_timestamp() {
		// Arrange
		String expectedFilepathRegex = String.format("^%s-[0-9].*$", configName);

		try {
			// Act
			String filepath = mockModel.generateScriptFileName();
			
			// Assert
			assertTrue("Should generate filepath with the config name and a timestamp", filepath.matches(expectedFilepathRegex));
		} catch(NoScriptDefinitionSelectedException e) {
			fail("We have mocked out getConfig() so should always return a config");
		}

	}
	
}
