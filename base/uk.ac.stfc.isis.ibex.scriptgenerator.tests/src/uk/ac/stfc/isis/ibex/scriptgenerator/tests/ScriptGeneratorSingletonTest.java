package uk.ac.stfc.isis.ibex.scriptgenerator.tests;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;
import org.junit.After;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.junit.Assert.fail;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import uk.ac.stfc.isis.ibex.scriptgenerator.ScriptGeneratorSingleton;
import uk.ac.stfc.isis.ibex.scriptgenerator.generation.InvalidParamsException;
import uk.ac.stfc.isis.ibex.scriptgenerator.pythoninterface.Config;
import uk.ac.stfc.isis.ibex.scriptgenerator.pythoninterface.ConfigLoader;
import uk.ac.stfc.isis.ibex.scriptgenerator.pythoninterface.PythonInterface;
import uk.ac.stfc.isis.ibex.scriptgenerator.table.ActionsTable;

public class ScriptGeneratorSingletonTest {
	
	ScriptGeneratorSingleton model;
	HashMap<Integer, String> validityErrors;
	ActionsTable mockActionsTable;
	PythonInterface mockPythonInterface;
	ConfigLoader mockConfigLoader;
	
	ScriptGeneratorSingleton mockModel;
	String[] scriptLines;
	String timestamp;
	Config mockConfig;
	String configName;
	String filepathPrefix;
	
	@Before
	public void setUp() {
		mockPythonInterface = mock(PythonInterface.class);
		mockConfigLoader = mock(ConfigLoader.class);
		mockActionsTable = mock(ActionsTable.class);
		model = new ScriptGeneratorSingleton(mockPythonInterface, mockConfigLoader, mockActionsTable);
		
		// Mock out the model to test some methods and make real calls to others
		mockModel = mock(ScriptGeneratorSingleton.class);
		scriptLines = new String[] {"test", "test2"};
		timestamp = "timestamp";
		mockConfig = mock(Config.class);
		configName = "config";
		filepathPrefix = (System.getProperty("user.dir") + "/test_scripts/").replaceAll("\\\\", "/");
		
		// Mock out some methods to test real calls to others
		try {
			when(mockModel.generateScript()).thenReturn(scriptLines);
			when(mockModel.getTimestamp()).thenReturn(timestamp);
			when(mockModel.getConfig()).thenReturn(mockConfig);
			when(mockConfig.getName()).thenReturn(configName);
			when(mockModel.generate()).thenCallRealMethod();
			when(mockModel.generateTo(filepathPrefix)).thenCallRealMethod();
		} catch (InvalidParamsException|IOException e) {
			fail("Won't fail as setting mock");
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
				fail("Failed to deleter folder " + test_scripts_folder.getPath());
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
				invalidityErrors, equalTo("Row: 1, Reason: invalid 1\nRow: 2, Reason: invalid 2\n"));
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
				invalidityErrors, equalTo("Row: 1, Reason: invalid 1\nRow: 2, Reason: invalid 2\n\nLimited to "
						+ maxNumOfLines + 
						" lines. To see an error for a specific row hover over it."));
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
				invalidityErrors, equalTo("Row: 1, Reason: invalid 1\nRow: 2, Reason: invalid 2\n"));
		assertThat("Should not be limited",
				invalidityErrors.split("\n").length, is(2));
	}
	
	@Test
	public void test_GIVEN_new_file_WHEN_generate_THEN_filename_without_version() {
		// Arrange in setUp
		try {
			String expectedFilepath = String.format("%s%s-%s.py", filepathPrefix, configName, timestamp);
			// Act
			String filepath = mockModel.generateTo(filepathPrefix);
			// Assert
			assertThat("Should generate filepath with the config name and a timestamp",
					filepath, equalTo(expectedFilepath));
		} catch(InvalidParamsException e) {
			fail("Mocked out generateScript so should not throw this");
		} catch(IOException e) {
			fail("Should not fail to write to file");
		}
	}
	
	@Test
	public void test_GIVEN_new_file_WHEN_generate_THEN_file_contents_as_expected() {
		// Arrange in setUp
		try {
			// Act
			String filepath = mockModel.generateTo(filepathPrefix);
			// Assert
			try(BufferedReader reader = new BufferedReader(new FileReader(filepath))){
				int linenum = 0;
				String line;
				while((line = reader.readLine()) != null) {
					if(linenum > scriptLines.length) {
						fail("Should be the same number of script lines as written lines");
					}
					assertThat("Lines should match the generated script",
							line, equalTo(scriptLines[linenum]));
					linenum += 1;
				}
			} catch(IOException e) {
				fail("Should not fail to read from file");
			}
		} catch(InvalidParamsException e) {
			fail("Mocked out generateScript so should not throw this");
		} catch(IOException e) {
			fail("Should not fail to write to file");
		}
	}
	
	@Test
	public void test_GIVEN_new_and_old_file_WHEN_generate_THEN_filename_with_version() {
		// Arrange in setUp
		try {
			String expectedOldFilepath = String.format("%s%s-%s.py", filepathPrefix, configName, timestamp);
			String expectedNewFilepath = String.format("%s%s-%s(1).py", filepathPrefix, configName, timestamp);
			String expectedNewNewFilepath = String.format("%s%s-%s(2).py", filepathPrefix, configName, timestamp);
			// Act
			String oldFilepath = mockModel.generateTo(filepathPrefix);
			String newFilepath = mockModel.generateTo(filepathPrefix);
			String newNewFilepath = mockModel.generateTo(filepathPrefix);
			// Assert
			assertThat("Should generate filepath with the config name and a timestamp",
					oldFilepath, equalTo(expectedOldFilepath));
			assertThat("Should generate filepath with the config name, a timestamp and 0 a version",
					newFilepath, equalTo(expectedNewFilepath));
			assertThat("Should generate filepath with the config name, a timestamp and 1 as a version",
					newNewFilepath, equalTo(expectedNewNewFilepath));
		} catch(InvalidParamsException e) {
			fail("Mocked out generateScript so should not throw this");
		} catch(IOException e) {
			fail("Should not fail to write to file");
		}
	}

}
