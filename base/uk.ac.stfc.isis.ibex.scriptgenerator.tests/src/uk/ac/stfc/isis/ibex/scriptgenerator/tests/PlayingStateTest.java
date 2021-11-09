package uk.ac.stfc.isis.ibex.scriptgenerator.tests;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.hamcrest.CoreMatchers.not;

import uk.ac.stfc.isis.ibex.nicos.ScriptStatus;
import uk.ac.stfc.isis.ibex.scriptgenerator.dynamicscripting.DynamicScriptingModelFacade;
import uk.ac.stfc.isis.ibex.scriptgenerator.dynamicscripting.DynamicScriptingNicosFacade;
import uk.ac.stfc.isis.ibex.scriptgenerator.dynamicscripting.DynamicScriptingProperties;
import uk.ac.stfc.isis.ibex.scriptgenerator.dynamicscripting.DynamicScriptingState;
import uk.ac.stfc.isis.ibex.scriptgenerator.dynamicscripting.DynamicScriptingStatus;
import uk.ac.stfc.isis.ibex.scriptgenerator.dynamicscripting.PlayingState;
import uk.ac.stfc.isis.ibex.scriptgenerator.table.ScriptGeneratorAction;
import uk.ac.stfc.isis.ibex.scriptgenerator.tests.utils.ScriptGeneratorMockBuilder;
import uk.ac.stfc.isis.ibex.scriptgenerator.tests.utils.StatusSwitchCounter;

public class PlayingStateTest extends DynamicScriptingStateTest {
	
	private DynamicScriptingNicosFacade nicosFacade;
	private DynamicScriptingModelFacade scriptGeneratorFacade;
	
	private ScriptGeneratorMockBuilder scriptGeneratorMockBuilder;
	
	protected void setUpState() {
		nicosFacade = new DynamicScriptingNicosFacade(scriptGeneratorMockBuilder.getMockNicosModel());
		scriptGeneratorFacade = new DynamicScriptingModelFacade(scriptGeneratorMockBuilder.getMockScriptGeneratorModel());
		state = new PlayingState(nicosFacade, scriptGeneratorFacade, dynamicScriptIdsToAction);
		state.addPropertyChangeListener(DynamicScriptingProperties.STATE_CHANGE_PROPERTY, statusSwitchCounter);
		state.addPropertyChangeListener(DynamicScriptingProperties.NEW_SCRIPT_ID_PROPERTY, evt -> {
			Optional<Integer> scriptId = (Optional<Integer>) evt.getNewValue();
			scriptId.ifPresent(Id -> { scriptIds.add(Id); });
		});
	}
	
	@Test
	public void test_status_is_playing() {
		// Act
		DynamicScriptingStatus status = state.getStatus();
		// Assert
		assertThat(status, is(DynamicScriptingStatus.PLAYING));
	}
	
	@Test
	public void test_WHEN_play_THEN_state_the_same() {
		// Act
		DynamicScriptingState newState = state.play();
		// Assert
		assertThat(newState, is(state));
	}
	
	@Test
	public void test_WHEN_stop_THEN_state_is_stopped() {
		// Act
		DynamicScriptingState newState = state.stop();
		// Assert
		assertThat(newState, is(not(state)));
		assertThat(newState.getStatus(), is(DynamicScriptingStatus.STOPPED));
	}
	
	@Test
	public void test_step_through_one_action() {
		// Assert
		Optional<ScriptGeneratorAction> currentAction = state.getCurrentlyExecutingAction();
		Optional<ScriptGeneratorAction> nextAction = state.getNextExecutingAction();
		assertThat(currentAction, is(scriptGeneratorMockBuilder.getMockScriptGeneratorAction(0)));
		assertThat(nextAction, is(scriptGeneratorMockBuilder.getMockScriptGeneratorAction(1)));
		assertTrue(scriptIds.isEmpty());
		// Act
		scriptGeneratorFacade.handleScriptGeneration("test");
		nicosFacade.setScriptStatus(ScriptStatus.IDLE);
		assertTrue(scriptIds.contains(0));
		assertThat(scriptIds.size(), is(1));
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
		assertTrue(scriptIds.isEmpty());
		int i = 0;
		do {
			currentAction = state.getCurrentlyExecutingAction();
			nextAction = state.getNextExecutingAction();
			assertThat(currentAction, is(scriptGeneratorMockBuilder.getMockScriptGeneratorAction(i)));
			assertThat(nextAction, is(scriptGeneratorMockBuilder.getMockScriptGeneratorAction(i + 1)));
			scriptGeneratorFacade.handleScriptGeneration("test");
			nicosFacade.setScriptStatus(ScriptStatus.IDLE);
			assertTrue(scriptIds.contains(i));
			assertThat(scriptIds.size(), is(i + 1));
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
		assertTrue(scriptIds.isEmpty());
		Optional<ScriptGeneratorAction> currentAction = state.getCurrentlyExecutingAction();
		Optional<ScriptGeneratorAction> nextAction = state.getNextExecutingAction();
		assertThat(currentAction, is(scriptGeneratorMockBuilder.getMockScriptGeneratorAction(0)));
		assertThat(nextAction, is(scriptGeneratorMockBuilder.getMockScriptGeneratorAction(1)));
		assertTrue(scriptIds.isEmpty());
		// Act
		scriptGeneratorFacade.handleScriptGeneration("test");
		nicosFacade.setScriptStatus(ScriptStatus.IDLE);
		// Assert
		assertTrue(scriptIds.isEmpty());
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
		assertTrue(scriptIds.isEmpty());
		Optional<ScriptGeneratorAction> currentAction = state.getCurrentlyExecutingAction();
		Optional<ScriptGeneratorAction> nextAction = state.getNextExecutingAction();
		assertThat(currentAction, is(scriptGeneratorMockBuilder.getMockScriptGeneratorAction(0)));
		assertThat(nextAction, is(scriptGeneratorMockBuilder.getMockScriptGeneratorAction(1)));
		assertTrue(scriptIds.isEmpty());
		// Act
		scriptGeneratorFacade.handleScriptGeneration("test");
		nicosFacade.setScriptStatus(ScriptStatus.IDLE);
		// Assert
		assertTrue(scriptIds.isEmpty());
		statusSwitchCounter.assertNumberOfSwitches(
			DynamicScriptingStatus.PLAYING, DynamicScriptingStatus.ERROR, 1
		);
	}
	
	@Test
	public void test_WHEN_no_next_action_THEN_next_action_is_empty() {
		// Arrange
		scriptGeneratorMockBuilder.arrangeNumberOfActions(1);
		assertTrue(scriptIds.isEmpty());
		// Assert
		Optional<ScriptGeneratorAction> nextAction = state.getNextExecutingAction();
		assertThat(nextAction, is(Optional.empty()));
		assertTrue(scriptIds.isEmpty());
	}
	
	@Test
	public void test_WHEN_no_current_action_THEN_next_action_is_empty() {
		// Arrange
		scriptGeneratorMockBuilder.arrangeNumberOfActions(0);
		setUpState();
		assertTrue(scriptIds.isEmpty());
		// Assert
		Optional<ScriptGeneratorAction> currentAction = state.getCurrentlyExecutingAction();
		assertThat(currentAction, is(Optional.empty()));
		Optional<ScriptGeneratorAction> nextAction = state.getNextExecutingAction();
		assertThat(nextAction, is(Optional.empty()));
		assertTrue(scriptIds.isEmpty());
	}

}
