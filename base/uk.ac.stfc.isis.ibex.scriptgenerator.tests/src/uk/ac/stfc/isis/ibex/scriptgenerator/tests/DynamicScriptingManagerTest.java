package uk.ac.stfc.isis.ibex.scriptgenerator.tests;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.AssumptionViolatedException;


import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import uk.ac.stfc.isis.ibex.scriptgenerator.dynamicscripting.DynamicScriptName;
import uk.ac.stfc.isis.ibex.scriptgenerator.dynamicscripting.DynamicScriptingException;
import uk.ac.stfc.isis.ibex.scriptgenerator.dynamicscripting.DynamicScriptingModelFacade;
import uk.ac.stfc.isis.ibex.scriptgenerator.dynamicscripting.DynamicScriptingManager;
import uk.ac.stfc.isis.ibex.scriptgenerator.dynamicscripting.DynamicScriptingNicosFacade;
import uk.ac.stfc.isis.ibex.scriptgenerator.dynamicscripting.DynamicScriptingState;
import uk.ac.stfc.isis.ibex.scriptgenerator.dynamicscripting.DynamicScriptingStateFactory;
import uk.ac.stfc.isis.ibex.scriptgenerator.dynamicscripting.DynamicScriptingStatus;
import uk.ac.stfc.isis.ibex.scriptgenerator.dynamicscripting.StoppedState;
import uk.ac.stfc.isis.ibex.scriptgenerator.table.ScriptGeneratorAction;
import uk.ac.stfc.isis.ibex.scriptgenerator.tests.utils.ScriptGeneratorMockBuilder;

public class DynamicScriptingManagerTest {
	
	private DynamicScriptingManager dynamicScriptingManager;
	private DynamicScriptingNicosFacade nicosFacade;
	private DynamicScriptingModelFacade modelFacade;
	private DynamicScriptingState initialState;
	private HashMap<Integer, ScriptGeneratorAction> dynamicScriptIds;
	private DynamicScriptingStateFactory stateFactory;
	
	private ScriptGeneratorMockBuilder scriptGeneratorMockBuilder;
	
	@Before
	public void setUp() {
		// Set up mocks
		scriptGeneratorMockBuilder = new ScriptGeneratorMockBuilder();
		// Set up class under test
		dynamicScriptIds = new HashMap<>();
		nicosFacade = new DynamicScriptingNicosFacade(scriptGeneratorMockBuilder.getMockNicosModel());
		modelFacade = new DynamicScriptingModelFacade(scriptGeneratorMockBuilder.getMockScriptGeneratorModel());
		initialState = new StoppedState(dynamicScriptIds);
		stateFactory = new DynamicScriptingStateFactory(modelFacade, nicosFacade, initialState);
		dynamicScriptingManager = new DynamicScriptingManager(stateFactory);
	}
	
	private void playScript() {
		try {
			dynamicScriptingManager.playScript();
		} catch (DynamicScriptingException e) {
			throw new AssumptionViolatedException("Given correct mocking there should be no exception");
		}
	}
	
	private void triggerActionExecuted(Integer scriptId) {
		simulateScriptGenerated();
		simulateScriptExecuted(scriptId);
	}
	
	private void simulateScriptGenerated() {
		modelFacade.handleScriptGeneration("test");
	}
	
	private void simulateScriptExecuted(Integer scriptId) {
		DynamicScriptName newName = new DynamicScriptName(Optional.of("Script Generator: " + scriptId));
		nicosFacade.scriptChanged(newName);
	}
	
	private void assertDynamicScriptingInErrorState() {
		assertThat(dynamicScriptingManager.getCurrentlyExecutingAction(), is(Optional.empty()));
		assertThat(dynamicScriptingManager.getDynamicScriptingStatus(), is(DynamicScriptingStatus.ERROR));
	}
	
	@Test
	public void test_WHEN_initially_set_up_THEN_in_stopped_state() {
		assertThat(dynamicScriptingManager.getDynamicScriptingStatus(), is(DynamicScriptingStatus.STOPPED));
	}
	
	@Test
	public void test_WHEN_initially_set_up_AND_stopped_THEN_in_stopped_state() {
		dynamicScriptingManager.stopScript();
		assertThat(dynamicScriptingManager.getDynamicScriptingStatus(), is(DynamicScriptingStatus.STOPPED));
	}
	
	@Test
	public void test_WHEN_play_script_THEN_first_action_queued() {
		// Act
		playScript();
		// Assert
		assertThat(dynamicScriptingManager.getCurrentlyExecutingAction(), is(scriptGeneratorMockBuilder.getMockScriptGeneratorAction(0)));
	}
	
	@Test
	public void test_WHEN_play_script_THEN_actions_executed() {
		List<ScriptGeneratorAction> actions = scriptGeneratorMockBuilder.getMockScriptGeneratorActions();
		// Act
		playScript();
		// Assert
		int i = 0;
		Optional<ScriptGeneratorAction> currentlyExecutingAction = dynamicScriptingManager.getCurrentlyExecutingAction();
		do {
			assertThat(currentlyExecutingAction, is(scriptGeneratorMockBuilder.getMockScriptGeneratorAction(i)));
			triggerActionExecuted(i);
			currentlyExecutingAction = dynamicScriptingManager.getCurrentlyExecutingAction();
			i++;
		} while (currentlyExecutingAction.isPresent());
		assertThat(i, is(actions.size()));
	}
	
	@Test
	public void test_WHEN_play_script_THEN_dynamic_scripting_manager_knows_it_is_a_dynamic_script() {
		// Arrange
		List<Integer> scriptIds = scriptGeneratorMockBuilder.arrangeCorrectScriptId();
		// Act
		playScript();
		triggerActionExecuted(1);
		// Assert
		assertThat(dynamicScriptingManager.isScriptDynamic(scriptIds.get(0)), is(true));
	}
	
	@Test
	public void test_WHEN_play_script_AND_invalid_params_THEN_dynamic_scripting_in_error_state() {
		// Arrange
		scriptGeneratorMockBuilder.arrangeRefreshScriptThrows(scriptGeneratorMockBuilder.getinvalidParamsException());
		// Act
		playScript();
		triggerActionExecuted(1);
		// Assert
		assertDynamicScriptingInErrorState();
	}
	
	@Test
	public void test_WHEN_play_script_AND_language_not_supported_THEN_dynamic_scripting_in_error_state() {
		// Arrange
		scriptGeneratorMockBuilder.arrangeRefreshScriptThrows(scriptGeneratorMockBuilder.getUnsupportedLanguageException());
		// Act
		playScript();
		triggerActionExecuted(1);
		// Assert
		assertDynamicScriptingInErrorState();
		
	}
	
	@Test
	public void test_WHEN_play_script_AND_no_script_definition_selected_THEN_dynamic_scripting_in_error_state() {
		// Arrange
		scriptGeneratorMockBuilder.arrangeRefreshScriptThrows(scriptGeneratorMockBuilder.getNoScriptDefSelectedException());
		// Act
		playScript();
		triggerActionExecuted(1);
		// Assert
		assertDynamicScriptingInErrorState();
	}

}
