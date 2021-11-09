package uk.ac.stfc.isis.ibex.scriptgenerator.tests;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThrows;

import java.util.HashMap;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;

import uk.ac.stfc.isis.ibex.scriptgenerator.NoScriptDefinitionSelectedException;
import uk.ac.stfc.isis.ibex.scriptgenerator.dynamicscripting.DynamicScript;
import uk.ac.stfc.isis.ibex.scriptgenerator.dynamicscripting.DynamicScriptingException;
import uk.ac.stfc.isis.ibex.scriptgenerator.dynamicscripting.DynamicScriptingModelAdapter;
import uk.ac.stfc.isis.ibex.scriptgenerator.dynamicscripting.DynamicScriptingProperties;
import uk.ac.stfc.isis.ibex.scriptgenerator.generation.InvalidParamsException;
import uk.ac.stfc.isis.ibex.scriptgenerator.generation.UnsupportedLanguageException;
import uk.ac.stfc.isis.ibex.scriptgenerator.table.ScriptGeneratorAction;
import uk.ac.stfc.isis.ibex.scriptgenerator.tests.utils.ScriptGeneratorMockBuilder;

public class DynamicScriptingModelAdapterTest {
	
	private ScriptGeneratorMockBuilder scriptGeneratorMockBuilder;
	private DynamicScriptingModelAdapter modelAdapter;
	private Integer scriptsGeneratedCount;
	
	@Before
	public void setUp() {
		// Set up scaffolding
		scriptGeneratorMockBuilder = new ScriptGeneratorMockBuilder();
		scriptsGeneratedCount = 0;
		// Set up class under test
		modelAdapter = new DynamicScriptingModelAdapter(scriptGeneratorMockBuilder.getMockScriptGeneratorModel());
		modelAdapter.addPropertyChangeListener(DynamicScriptingProperties.NICOS_SCRIPT_GENERATED_PROPERTY, evt -> {
			scriptsGeneratedCount++;
		});
	}
	
	private void assertScriptIs(Integer id, String name, Optional<String> code) {
		Optional<DynamicScript> scriptOptional = modelAdapter.getDynamicScript();
		if (scriptOptional.isEmpty()) {
			throw new AssertionError("Script not present to test");
		}
		DynamicScript script = scriptOptional.get();
		assertThat(script.getId(), is(id));
		assertThat(script.getName(), is(name));
		assertThat(script.getCode(), is(code));
	}
	
	private void assertScriptIsEmpty() {
		Optional<DynamicScript> script = modelAdapter.getDynamicScript();
		assertThat(script, is(Optional.empty()));
	}
	
	@Test
	public void test_initially_script_id_and_name_and_code_are_empty() {
		assertScriptIsEmpty();
	}
	
	@Test
	public void test_WHEN_get_first_action_THEN_is_first_action() {
		Optional<ScriptGeneratorAction> action = modelAdapter.getFirstAction();
		assertThat(action, is(not(Optional.empty())));
		assertThat(action, is(scriptGeneratorMockBuilder.getMockScriptGeneratorAction(0)));
	}
	
	@Test
	public void test_WHEN_get_second_action_THEN_is_second_action() {
		ScriptGeneratorAction firstAction = modelAdapter.getFirstAction().get();
		Optional<ScriptGeneratorAction> secondAction = modelAdapter.getActionAfter(firstAction);
		assertThat(secondAction, is(not(Optional.empty())));
		assertThat(secondAction, is(scriptGeneratorMockBuilder.getMockScriptGeneratorAction(1)));
	}
	
	@Test
	public void test_GIVEN_action_that_does_not_exist_WHEN_get_action_after_THEN_empty() {
		ScriptGeneratorAction action = new ScriptGeneratorAction(new HashMap<>());
		Optional<ScriptGeneratorAction> nextAction = modelAdapter.getActionAfter(action);
		assertThat(nextAction, is(Optional.empty()));
	}
	
	@Test
	public void test_WHEN_invalid_params_on_refresh_generated_script_THEN_dynamic_script_exception() {
		// Arrange
		ScriptGeneratorAction action = scriptGeneratorMockBuilder.arrangeExceptionToThrowForAction(0, InvalidParamsException.class);
		// Assert and Act
		assertThrows(DynamicScriptingException.class, () -> {
			modelAdapter.refreshGeneratedScript(action);
		});
		assertScriptIsEmpty();
	}
	
	@Test
	public void test_WHEN_unsupported_language_on_refresh_generated_script_THEN_dynamic_script_exception() {
		// Arrange
		ScriptGeneratorAction action = scriptGeneratorMockBuilder.arrangeExceptionToThrowForAction(0, UnsupportedLanguageException.class);
		// Assert and Act
		assertThrows(DynamicScriptingException.class, () -> {
			modelAdapter.refreshGeneratedScript(action);
		});
		assertScriptIsEmpty();
	}
	
	@Test
	public void test_WHEN_no_script_def_selected_on_refresh_generated_script_THEN_dynamic_script_exception() {
		// Arrange
		ScriptGeneratorAction action = scriptGeneratorMockBuilder.arrangeExceptionToThrowForAction(0, NoScriptDefinitionSelectedException.class);
		// Assert and Act
		assertThrows(DynamicScriptingException.class, () -> {
			modelAdapter.refreshGeneratedScript(action);
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
			Optional<Integer> scriptId = modelAdapter.refreshGeneratedScript(action);
			assertThat(scriptId, is(Optional.of(0)));
			assertThat(scriptsGeneratedCount, is(0));
			modelAdapter.handleScriptGeneration("test");
			assertThat(scriptsGeneratedCount, is(1));
			assertScriptIs(0, "Script Generator: 0", Optional.of("test" + "\nrunscript()"));
		} catch (DynamicScriptingException e) {
			throw new AssertionError("Should refresh correctly");
		}
	}
	

}
