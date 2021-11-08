package uk.ac.stfc.isis.ibex.scriptgenerator.tests;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThrows;


import java.util.Optional;

import org.junit.Before;
import org.junit.Test;

import uk.ac.stfc.isis.ibex.scriptgenerator.NoScriptDefinitionSelectedException;
import uk.ac.stfc.isis.ibex.scriptgenerator.dynamicscripting.DynamicScriptingException;
import uk.ac.stfc.isis.ibex.scriptgenerator.dynamicscripting.DynamicScriptingModelFacade;
import uk.ac.stfc.isis.ibex.scriptgenerator.dynamicscripting.DynamicScriptingProperties;
import uk.ac.stfc.isis.ibex.scriptgenerator.generation.InvalidParamsException;
import uk.ac.stfc.isis.ibex.scriptgenerator.generation.UnsupportedLanguageException;
import uk.ac.stfc.isis.ibex.scriptgenerator.table.ScriptGeneratorAction;
import uk.ac.stfc.isis.ibex.scriptgenerator.tests.utils.ScriptGeneratorMockBuilder;

public class DynamicScriptingGeneratorFacadeTest {
	
	private ScriptGeneratorMockBuilder scriptGeneratorMockBuilder;
	private DynamicScriptingModelFacade generatorFacade;
	private Integer scriptsGeneratedCount;
	
	@Before
	public void setUp() {
		// Set up scaffolding
		scriptGeneratorMockBuilder = new ScriptGeneratorMockBuilder();
		scriptsGeneratedCount = 0;
		// Set up class under test
		generatorFacade = new DynamicScriptingModelFacade(scriptGeneratorMockBuilder.getMockScriptGeneratorModel());
		generatorFacade.addPropertyChangeListener(DynamicScriptingProperties.NICOS_SCRIPT_GENERATED_PROPERTY, evt -> {
			scriptsGeneratedCount++;
		});
	}
	
	private void assertScriptIs(Optional<Integer> id, Optional<String> name, Optional<String> code) {
		assertThat(generatorFacade.getScriptId(), is(id));
		assertThat(generatorFacade.getScriptName(), is(name));
		assertThat(generatorFacade.getScriptCode(), is(code));
	}
	
	private void assertScriptIsEmpty() {
		assertScriptIs(Optional.empty(), Optional.empty(), Optional.empty());
	}
	
	@Test
	public void test_initially_script_id_and_name_and_code_are_empty() {
		assertScriptIsEmpty();
	}
	
	@Test
	public void test_WHEN_invalid_params_on_refresh_generated_script_THEN_dynamic_script_exception() {
		// Arrange
		ScriptGeneratorAction action = scriptGeneratorMockBuilder.arrangeExceptionToThrowForAction(0, InvalidParamsException.class);
		// Assert and Act
		assertThrows(DynamicScriptingException.class, () -> {
			generatorFacade.refreshGeneratedScript(action);
		});
		assertScriptIsEmpty();
	}
	
	@Test
	public void test_WHEN_unsupported_language_on_refresh_generated_script_THEN_dynamic_script_exception() {
		// Arrange
		ScriptGeneratorAction action = scriptGeneratorMockBuilder.arrangeExceptionToThrowForAction(0, UnsupportedLanguageException.class);
		// Assert and Act
		assertThrows(DynamicScriptingException.class, () -> {
			generatorFacade.refreshGeneratedScript(action);
		});
		assertScriptIsEmpty();
	}
	
	@Test
	public void test_WHEN_no_script_def_selected_on_refresh_generated_script_THEN_dynamic_script_exception() {
		// Arrange
		ScriptGeneratorAction action = scriptGeneratorMockBuilder.arrangeExceptionToThrowForAction(0, NoScriptDefinitionSelectedException.class);
		// Assert and Act
		assertThrows(DynamicScriptingException.class, () -> {
			generatorFacade.refreshGeneratedScript(action);
		});
		assertScriptIsEmpty();
	}
	
	@Test
	public void test_WHEN_script_valid_THEN_script_generated_AND_property_changed() {
		// Arrange
		ScriptGeneratorAction action = scriptGeneratorMockBuilder.getMockScriptGeneratorAction(0).get();
		// Assert and Act
		assertThat(scriptsGeneratedCount, is(0));
		try {
			Optional<Integer> scriptId = generatorFacade.refreshGeneratedScript(action);
			assertThat(scriptId, is(Optional.of(0)));
			assertThat(scriptsGeneratedCount, is(0));
			generatorFacade.handleScriptGeneration("test");
			assertThat(scriptsGeneratedCount, is(1));
			assertScriptIs(Optional.of(0), Optional.of("Script Generator: 0"), Optional.of("test"));
		} catch (DynamicScriptingException e) {
			throw new AssertionError("Should refresh correctly");
		}
	}
	

}
