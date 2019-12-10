package uk.ac.stfc.isis.ibex.scriptgenerator.tests;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.fail;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.HashMap;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;

import uk.ac.stfc.isis.ibex.scriptgenerator.generation.GeneratedLanguage;
import uk.ac.stfc.isis.ibex.scriptgenerator.generation.GeneratorContext;
import uk.ac.stfc.isis.ibex.scriptgenerator.generation.GeneratorPython;
import uk.ac.stfc.isis.ibex.scriptgenerator.generation.InvalidParamsException;
import uk.ac.stfc.isis.ibex.scriptgenerator.generation.UnsupportedLanguageException;
import uk.ac.stfc.isis.ibex.scriptgenerator.pythoninterface.Config;
import uk.ac.stfc.isis.ibex.scriptgenerator.pythoninterface.PythonInterface;
import uk.ac.stfc.isis.ibex.scriptgenerator.table.ActionsTable;


public class GeneratorContextTest {
	
	GeneratorContext generatorContext;
	PythonInterface mockPythonInterface;
	GeneratorPython mockGeneratorPython;
	ActionsTable mockActionsTable;
	Config mockConfig;
	HashMap<Integer, String> expectedValidityErrors;
	String generatedScript;
	
	@Before
	public void setUp() {
		// Create mocks
		mockPythonInterface = mock(PythonInterface.class);
		mockGeneratorPython = mock(GeneratorPython.class);
		mockActionsTable = mock(ActionsTable.class);
		mockConfig = mock(Config.class);
		when(mockGeneratorPython.areParamsValid(mockActionsTable, mockConfig)).thenReturn(true);
		
		// Create validity errors
		expectedValidityErrors = new HashMap<>();
		expectedValidityErrors.put(1, "invalid 1");
		when(mockGeneratorPython.getValidityErrors(mockActionsTable, mockConfig)).thenReturn(expectedValidityErrors);
		
		// Mock generation
		generatedScript = "Generated";
		try {
			when(mockGeneratorPython.generate(mockActionsTable, mockConfig)).thenReturn(generatedScript);
		} catch (InvalidParamsException e) {
			fail("We have mocked actions table so should not throw this exception");
		}
		
		// Set up generator context
		generatorContext = new GeneratorContext(mockPythonInterface);
		generatorContext.putGenerator(GeneratedLanguage.PYTHON, mockGeneratorPython);
	}
	
	@Test
	public void test_GIVEN_params_are_valid_WHEN_context_check_params_valid_THEN_true() {
		try {
			assertThat("Params are valid and language is supported, so return true",
					generatorContext.areParamsValid(mockActionsTable, mockConfig, GeneratedLanguage.PYTHON),
					is(true));
		} catch(UnsupportedLanguageException e) {
			fail("Python is a supported language");
		}
	}
	
	@Test
	public void test_GIVEN_params_are_invalid_WHEN_get_validity_errors_valid_THEN_errors_as_expected() {
		try {
			assertThat("Params are invalid and have outpus and language is supported, so return inserted hashmap",
					generatorContext.getValidityErrors(mockActionsTable, mockConfig, GeneratedLanguage.PYTHON),
					equalTo(expectedValidityErrors));
		} catch(UnsupportedLanguageException e) {
			fail("Python is a supported language");
		}
	}
	
	@Test
	public void test_GIVEN_generate_script_WHEN_get_generate_script_THEN_script_as_expected() {
		try {
			assertThat("Params are invalid and have outpus and language is supported, so return inserted hashmap",
					generatorContext.generate(mockActionsTable, mockConfig, GeneratedLanguage.PYTHON),
					equalTo(generatedScript));
		} catch(UnsupportedLanguageException e) {
			fail("Python is a supported language");
		} catch (InvalidParamsException e) {
			fail("We have mocked actions table so should not throw this exception");
		}
	}
	

}
