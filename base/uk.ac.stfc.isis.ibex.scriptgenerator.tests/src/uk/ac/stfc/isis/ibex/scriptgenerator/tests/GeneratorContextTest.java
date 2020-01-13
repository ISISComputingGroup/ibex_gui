package uk.ac.stfc.isis.ibex.scriptgenerator.tests;

import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.Before;
import org.junit.Rule;

import static org.junit.Assert.fail;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.concurrent.ExecutionException;

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
	
	@Rule
	public final ExpectedException exception = ExpectedException.none();
	
	GeneratorContext generatorContext;
	PythonInterface mockPythonInterface;
	GeneratorPython mockGeneratorPython;
	ActionsTable mockActionsTable;
	Config mockConfig;
	HashMap<Integer, String> expectedValidityErrors;
	String generatedScript = "Generated";
	
	@Before
	public void setUp() {
		// Create validity errors
		expectedValidityErrors = new HashMap<>();
		expectedValidityErrors.put(1, "invalid 1");
		
		// Create mocks
		mockActionsTable = mock(ActionsTable.class);
		mockConfig = mock(Config.class);
		
		mockPythonInterface = mock(PythonInterface.class);
		try {
			when(mockPythonInterface.getValidityErrors(mockActionsTable.getActions(), mockConfig)).thenReturn(expectedValidityErrors);
			when(mockPythonInterface.refreshAreParamsValid(mockActionsTable.getActions(), mockConfig)).thenReturn(true);
			try {
				when(mockPythonInterface.refreshGeneratedScript(mockActionsTable.getActions(), mockConfig)).thenReturn(generatedScript);
			} catch (InvalidParamsException e) {
				fail("We have mocked actions table so should not throw this exception");
			}
		} catch(ExecutionException | InterruptedException e) {
			fail("We have mocked these calls so they should not error");
		}
		
		mockGeneratorPython = mock(GeneratorPython.class);
		try {
			when(mockGeneratorPython.refreshAreParamsValid(mockActionsTable, mockConfig)).thenReturn(true);
			when(mockGeneratorPython.getValidityErrors(mockActionsTable, mockConfig)).thenReturn(expectedValidityErrors);
			
			try {
				when(mockGeneratorPython.refreshGeneratedScript(mockActionsTable, mockConfig)).thenReturn(generatedScript);
			} catch (InvalidParamsException e) {
				fail("We have mocked actions table so should not throw this exception");
			}
		} catch(InterruptedException | ExecutionException e) {
			fail("We have mocked the python interface out so should not throw these");
		}
		
		// Set up generator context
		generatorContext = new GeneratorContext(mockPythonInterface);
	}
	
	@Test
	public void test_GIVEN_params_are_valid_WHEN_context_check_params_valid_THEN_true() {
		generatorContext.putGenerator(GeneratedLanguage.PYTHON, mockGeneratorPython);
		try {
			assertThat("Params are valid and language is supported, so return true",
					generatorContext.refreshAreParamsValid(mockActionsTable, mockConfig, GeneratedLanguage.PYTHON),
					is(true));
		} catch(UnsupportedLanguageException e) {
			fail("Python is a supported language");
		} catch (InterruptedException | ExecutionException e) {
			fail("We have mocked out py4j calls so should not throw this");
		}
	}
	
	@Test
	public void test_GIVEN_params_are_invalid_WHEN_get_validity_errors_valid_THEN_errors_as_expected() {
		generatorContext.putGenerator(GeneratedLanguage.PYTHON, mockGeneratorPython);
		try {
			assertThat("Params are invalid and have outpus and language is supported, so return inserted hashmap",
					generatorContext.getValidityErrors(mockActionsTable, mockConfig, GeneratedLanguage.PYTHON),
					equalTo(expectedValidityErrors));
		} catch(UnsupportedLanguageException e) {
			fail("Python is a supported language");
		} catch (InterruptedException | ExecutionException e) {
			fail("We have mocked out py4j calls so should not throw this");
		}
	}
	
	@Test
	public void test_GIVEN_generate_script_WHEN_get_generate_script_THEN_script_as_expected() {
		generatorContext.putGenerator(GeneratedLanguage.PYTHON, mockGeneratorPython);
		try {
			assertThat("Params are invalid and have outpus and language is supported, so return inserted hashmap",
					generatorContext.refreshGeneratedScript(mockActionsTable, mockConfig, GeneratedLanguage.PYTHON),
					equalTo(generatedScript));
		} catch(UnsupportedLanguageException e) {
			fail("Python is a supported language");
		} catch (InvalidParamsException e) {
			fail("We have mocked actions table so should not throw this exception");
		} catch (InterruptedException | ExecutionException e) {
			fail("We have mocked out py4j calls so should not throw this");
		}
	}
	
	@Test
	public void test_GIVEN_using_default_generator_language_WHEN_generate_THEN_no_exception_thrown() {
		try {
			generatorContext.generate(mockActionsTable, mockConfig);
		} catch(UnsupportedLanguageException e) {
			fail("Default language should be supported");
		} catch (InvalidParamsException e) {
			fail("We have mocked actions table so should not throw this exception");
		} catch (InterruptedException | ExecutionException e) {
			fail("We have mocked out py4j calls so should not throw this");
		}
	}
	
	@Test
	public void test_GIVEN_using_default_generator_language_WHEN_check_params_validity_THEN_no_exception_thrown() {
		try {
			generatorContext.refreshAreParamsValid(mockActionsTable, mockConfig);
		} catch(UnsupportedLanguageException e) {
			fail("Default language should be supported");
		} catch (InterruptedException | ExecutionException e) {
			fail("We have mocked out py4j calls so should not throw this");
		}
	}
	
	@Test
	public void test_GIVEN_using_default_generator_language_WHEN_get_validity_errors_THEN_no_exception_thrown() {
		try {
			generatorContext.getValidityErrors(mockActionsTable, mockConfig);
		} catch(UnsupportedLanguageException e) {
			fail("Default language should be supported");
		} catch (InterruptedException | ExecutionException e) {
			fail("We have mocked out py4j calls so should not throw this");
		}
	}
	
	@Test
	public void test_GIVEN_using_unsupported_generator_language_WHEN_generate_THEN_exception_thrown() {
		try {
			generatorContext.refreshGeneratedScript(mockActionsTable, mockConfig, GeneratedLanguage.UNSUPPORTED_LANGUAGE);
			fail("Language is unsupported, should throw exception");
		} catch (InvalidParamsException e) {
			fail("We have mocked actions table so should not throw this exception");
		} catch (UnsupportedLanguageException e) {
			// success
		} catch (InterruptedException | ExecutionException e) {
			fail("We have mocked out py4j calls so should not throw this");
		}
	}
	
	@Test
	public void test_GIVEN_using_unsupported_generator_language_WHEN_check_params_validity_THEN_exception_thrown() {
		try {
			generatorContext.refreshAreParamsValid(mockActionsTable, mockConfig, GeneratedLanguage.UNSUPPORTED_LANGUAGE);
			fail("Language is unsupported, should throw exception");
		} catch (UnsupportedLanguageException e) {
			// success
		} catch (InterruptedException | ExecutionException e) {
			fail("We have mocked out py4j calls so should not throw this");
		}
	}
	
	@Test
	public void test_GIVEN_using_unsupported_generator_language_WHEN_get_validity_errors_THEN_no_exception_thrown() {
		try {
			generatorContext.getValidityErrors(mockActionsTable, mockConfig, GeneratedLanguage.UNSUPPORTED_LANGUAGE);
			fail("Language is unsupported, should throw exception");
		} catch (UnsupportedLanguageException e) {
			// success
		} catch (InterruptedException | ExecutionException e) {
			fail("We have mocked out py4j calls so should not throw this");
		}
	}

}
