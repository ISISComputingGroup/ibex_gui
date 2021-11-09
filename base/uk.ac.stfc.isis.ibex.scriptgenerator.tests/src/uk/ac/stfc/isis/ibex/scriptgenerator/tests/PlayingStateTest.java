package uk.ac.stfc.isis.ibex.scriptgenerator.tests;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Optional;

import uk.ac.stfc.isis.ibex.scriptgenerator.dynamicscripting.DynamicScriptingModelFacade;
import uk.ac.stfc.isis.ibex.scriptgenerator.dynamicscripting.DynamicScriptingNicosFacade;
import uk.ac.stfc.isis.ibex.scriptgenerator.dynamicscripting.DynamicScriptingStatus;
import uk.ac.stfc.isis.ibex.scriptgenerator.dynamicscripting.PlayingState;
import uk.ac.stfc.isis.ibex.scriptgenerator.table.ScriptGeneratorAction;
import uk.ac.stfc.isis.ibex.scriptgenerator.tests.utils.ScriptGeneratorMockBuilder;

public class PlayingStateTest extends DynamicScriptingStateTest {
	
	private DynamicScriptingNicosFacade nicosFacade;
	private DynamicScriptingModelFacade scriptGeneratorFacade;
	
	private ScriptGeneratorMockBuilder scriptGeneratorMockBuilder;
	
	@Override
	protected void setUpState() {
		scriptGeneratorMockBuilder = new ScriptGeneratorMockBuilder();
		nicosFacade = new DynamicScriptingNicosFacade(scriptGeneratorMockBuilder.getMockNicosModel());
		scriptGeneratorFacade = new DynamicScriptingModelFacade(scriptGeneratorMockBuilder.getMockScriptGeneratorModel());
		state = new PlayingState(nicosFacade, scriptGeneratorFacade, dynamicScriptIdsToAction);
	}
	
	@Override
	protected void setUpDynamicScriptIds() {
		dynamicScriptIdsToAction = new HashMap<Integer, ScriptGeneratorAction>();
	}
	
	protected void simulateScriptGenerated() {
		scriptGeneratorFacade.handleScriptGeneration("test");
	}
	
	protected void simulateScriptExecuted(String scriptName) {
		nicosFacade.setScriptName("Script Generator: " + scriptId);
	}
	
	@Test
	public void test_status_is_playing() {
		DynamicScriptingStatus status = state.getStatus();
		assertThat(status, is(DynamicScriptingStatus.PLAYING));
	}
	
	@Test
	public void test_WHEN_play_THEN_state_the_same() {
		state.play();
		statusSwitchCounter.assertNoSwitches();
	}
	
	@Test
	public void test_WHEN_stop_THEN_state_is_stopped() {
		state.stop();
		statusSwitchCounter.assertNumberOfSwitches(
			DynamicScriptingStatus.PLAYING, DynamicScriptingStatus.STOPPED, 1
		);
	}
	
	@Test
	public void test_step_through_one_action() {
		// Assert
		Optional<ScriptGeneratorAction> currentAction = state.getCurrentlyExecutingAction();
		Optional<ScriptGeneratorAction> nextAction = state.getNextExecutingAction();
		assertThat(currentAction, is(scriptGeneratorMockBuilder.getMockScriptGeneratorAction(0)));
		assertThat(nextAction, is(scriptGeneratorMockBuilder.getMockScriptGeneratorAction(1)));
		// Act
		simulateScriptGenerated();
		simulateScriptExecuted(1);
		assertTrue(state.isScriptDynamic(0));
		// Assert
		currentAction = state.getCurrentlyExecutingAction();
		nextAction = state.getNextExecutingAction();
		assertThat(currentAction, is(scriptGeneratorMockBuilder.getMockScriptGeneratorAction(1)));
		assertThat(nextAction, is(scriptGeneratorMockBuilder.getMockScriptGeneratorAction(2)));
	}
	
	@Test
	public void test_stepping_through_actions() {
		Optional<ScriptGeneratorAction> currentAction;
		Optional<ScriptGeneratorAction> nextAction;
		statusSwitchCounter.assertNumberOfSwitches(
			DynamicScriptingStatus.PLAYING, DynamicScriptingStatus.STOPPED, 0
		);
		int i = 0;
		do {
			currentAction = state.getCurrentlyExecutingAction();
			nextAction = state.getNextExecutingAction();
			assertThat(currentAction, is(scriptGeneratorMockBuilder.getMockScriptGeneratorAction(i)));
			assertThat(nextAction, is(scriptGeneratorMockBuilder.getMockScriptGeneratorAction(i + 1)));
			simulateScriptGenerated();
			simulateScriptExecuted(i);
			assertTrue(state.isScriptDynamic(i));
			i++;
		} while (nextAction.isPresent());
		statusSwitchCounter.assertNumberOfSwitches(
			DynamicScriptingStatus.PLAYING, DynamicScriptingStatus.STOPPED, 1
		);
	}
	
	@Test
	public void test_WHEN_nicos_error_THEN_error_state() {
		// Arrange
		scriptGeneratorMockBuilder.arrangeNicosError();
		// Assert
		statusSwitchCounter.assertNumberOfSwitches(
			DynamicScriptingStatus.PLAYING, DynamicScriptingStatus.ERROR, 0
		);
		Optional<ScriptGeneratorAction> currentAction = state.getCurrentlyExecutingAction();
		Optional<ScriptGeneratorAction> nextAction = state.getNextExecutingAction();
		assertThat(currentAction, is(scriptGeneratorMockBuilder.getMockScriptGeneratorAction(0)));
		assertThat(nextAction, is(scriptGeneratorMockBuilder.getMockScriptGeneratorAction(1)));
		// Act
		simulateScriptGenerated();
		simulateScriptExecuted(1);
		// Assert
		statusSwitchCounter.assertNumberOfSwitches(
			DynamicScriptingStatus.PLAYING, DynamicScriptingStatus.ERROR, 1
		);
	}
	
	@Test
	public void test_WHEN_nicos_cannot_send_script_THEN_error_state() {
		// Arrange
		scriptGeneratorMockBuilder.arrangeNicosSendScriptFail();
		// Assert
		statusSwitchCounter.assertNumberOfSwitches(
			DynamicScriptingStatus.PLAYING, DynamicScriptingStatus.ERROR, 0
		);
		Optional<ScriptGeneratorAction> currentAction = state.getCurrentlyExecutingAction();
		Optional<ScriptGeneratorAction> nextAction = state.getNextExecutingAction();
		assertThat(currentAction, is(scriptGeneratorMockBuilder.getMockScriptGeneratorAction(0)));
		assertThat(nextAction, is(scriptGeneratorMockBuilder.getMockScriptGeneratorAction(1)));
		// Act
		simulateScriptGenerated();
		simulateScriptExecuted(1);
		// Assert
		statusSwitchCounter.assertNumberOfSwitches(
			DynamicScriptingStatus.PLAYING, DynamicScriptingStatus.ERROR, 1
		);
	}
	
	@Test
	public void test_WHEN_no_next_action_THEN_next_action_is_empty() {
		// Arrange
		scriptGeneratorMockBuilder.arrangeNumberOfActions(1);
		// Assert
		Optional<ScriptGeneratorAction> nextAction = state.getNextExecutingAction();
		assertThat(nextAction, is(Optional.empty()));
	}
	
	@Test
	public void test_WHEN_no_current_action_THEN_next_action_is_empty() {
		// Arrange
		scriptGeneratorMockBuilder.arrangeNumberOfActions(0);
		setUpState();
		// Assert
		Optional<ScriptGeneratorAction> currentAction = state.getCurrentlyExecutingAction();
		assertThat(currentAction, is(Optional.empty()));
		Optional<ScriptGeneratorAction> nextAction = state.getNextExecutingAction();
		assertThat(nextAction, is(Optional.empty()));
	}

}
