package uk.ac.stfc.isis.ibex.scriptgenerator.tests;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.AssumptionViolatedException;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import uk.ac.stfc.isis.ibex.scriptgenerator.NoScriptDefinitionSelectedException;
import uk.ac.stfc.isis.ibex.scriptgenerator.ScriptGeneratorSingleton;
import uk.ac.stfc.isis.ibex.scriptgenerator.dynamicscripting.DynamicScriptingException;
import uk.ac.stfc.isis.ibex.scriptgenerator.dynamicscripting.DynamicScriptingManager;
import uk.ac.stfc.isis.ibex.scriptgenerator.generation.InvalidParamsException;
import uk.ac.stfc.isis.ibex.scriptgenerator.generation.UnsupportedLanguageException;
import uk.ac.stfc.isis.ibex.scriptgenerator.table.ScriptGeneratorAction;
import uk.ac.stfc.isis.ibex.scriptgenerator.tests.utils.ScriptGeneratorMockBuilder;

public class DynamicScriptingManagerTest {
	
	private DynamicScriptingManager dynamicScriptingManager;
	
	private ScriptGeneratorMockBuilder scriptGeneratorMockBuilder;
	
	@Before
	public void setUp() {
		// Set up mocks
		scriptGeneratorMockBuilder = new ScriptGeneratorMockBuilder();
		// Set up class under test
		dynamicScriptingManager = new DynamicScriptingManager(scriptGeneratorMockBuilder.getMockScriptGeneratorModel());
	}
	
	private void playScript() {
		try {
			dynamicScriptingManager.playScript();
		} catch (DynamicScriptingException e) {
			throw new AssumptionViolatedException("Given correct mocking there should be no exception");
		}
	}
	
	private void assertDynamicScriptingInErrorState() {
		assertThat(dynamicScriptingManager.getCurrentlyExecutingAction(), is(Optional.empty()));
	}
	
	@Test
	public void test_WHEN_play_script_THEN_first_action_queued() {
		// Arrange
		scriptGeneratorMockBuilder.arrangeCorrectScriptId();
		// Act
		playScript();
		// Assert
		assertThat(dynamicScriptingManager.getCurrentlyExecutingAction(), is(scriptGeneratorMockBuilder.getMockScriptGeneratorActionOptional()));
	}
	
	@Test
	public void test_WHEN_play_script_THEN_dynamic_scripting_manager_knows_it_is_a_dynamic_script() {
		// Arrange
		Integer scriptId = scriptGeneratorMockBuilder.arrangeCorrectScriptId();
		// Act
		playScript();
		// Assert
		assertThat(dynamicScriptingManager.isScriptDynamic(scriptId), is(true));
	}
	
	@Test
	public void test_WHEN_play_script_AND_invalid_params_THEN_dynamic_scripting_in_error_state() {
		// Arrange
		scriptGeneratorMockBuilder.arrangeRefreshScriptThrows(scriptGeneratorMockBuilder.getinvalidParamsException());
		// Act
		playScript();
		// Assert
		assertDynamicScriptingInErrorState();
	}
	
	@Test
	public void test_WHEN_play_script_AND_language_not_supported_THEN_dynamic_scripting_in_error_state() {
		// Arrange
		scriptGeneratorMockBuilder.arrangeRefreshScriptThrows(scriptGeneratorMockBuilder.getUnsupportedLanguageException());
		// Act
		playScript();
		// Assert
		assertDynamicScriptingInErrorState();
	}
	
	@Test
	public void test_WHEN_play_script_AND_no_script_definition_selected_THEN_dynamic_scripting_in_error_state() {
		// Arrange
		scriptGeneratorMockBuilder.arrangeRefreshScriptThrows(scriptGeneratorMockBuilder.getNoScriptDefSelectedException());
		// Act
		playScript();
		// Assert
		assertDynamicScriptingInErrorState();
	}

}
