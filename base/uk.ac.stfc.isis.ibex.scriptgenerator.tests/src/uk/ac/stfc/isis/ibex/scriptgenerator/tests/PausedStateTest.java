package uk.ac.stfc.isis.ibex.scriptgenerator.tests;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.beans.PropertyChangeEvent;
import java.util.HashMap;
import java.util.Optional;

import org.junit.Test;

import uk.ac.stfc.isis.ibex.nicos.ScriptStatus;
import uk.ac.stfc.isis.ibex.scriptgenerator.dynamicscripting.DynamicScriptingModelAdapter;
import uk.ac.stfc.isis.ibex.scriptgenerator.dynamicscripting.DynamicScriptingNicosAdapter;
import uk.ac.stfc.isis.ibex.scriptgenerator.dynamicscripting.DynamicScriptingProperties;
import uk.ac.stfc.isis.ibex.scriptgenerator.dynamicscripting.DynamicScriptingStatus;
import uk.ac.stfc.isis.ibex.scriptgenerator.dynamicscripting.PausedState;
import uk.ac.stfc.isis.ibex.scriptgenerator.table.ActionDynamicScriptingStatus;
import uk.ac.stfc.isis.ibex.scriptgenerator.table.ScriptGeneratorAction;

public class PausedStateTest extends DynamicScriptingStateTest {
	
	private DynamicScriptingNicosAdapter nicosAdapter;
	private DynamicScriptingModelAdapter modelAdapter;
	private Optional<ScriptGeneratorAction> currentAction;
	private Optional<ScriptGeneratorAction> nextAction;
	private Boolean pauseComplete;
	
	@Override
	protected void setUpState() {
		state = new PausedState(nicosAdapter, modelAdapter, currentAction, dynamicScriptIdsToAction, pauseComplete);
	}
	
	@Override
	protected void setUpScaffolding() {
		super.setUpScaffolding();
		nicosAdapter = mock(DynamicScriptingNicosAdapter.class);
		modelAdapter = mock(DynamicScriptingModelAdapter.class);
		currentAction = Optional.of(new ScriptGeneratorAction(new HashMap<>()));
		currentAction.get().setPausedBeforeExecution();
		nextAction = Optional.of(new ScriptGeneratorAction(new HashMap<>()));
		pauseComplete = false;
	}
	
	private void simulateScriptFinished() {
		state.propertyChange(
			new PropertyChangeEvent(
				nicosAdapter, DynamicScriptingProperties.SCRIPT_FINISHED_PROPERTY,
				null, null
			)
		);
	}
	
	@Test
	public void test_status_correct() {
		assertThat(state.getStatus(), is(DynamicScriptingStatus.PAUSED));
	}
	
	@Test
	public void test_WHEN_stop_AND_script_finishes_THEN_state_is_stopped() {
		// Arrange
		currentAction = Optional.of(new ScriptGeneratorAction(new HashMap<>()));
		currentAction.get().setPausedDuringExecution();
		reloadState();
		// Act
		state.stop();
		simulateScriptFinished();
		// Assert
		statusSwitchCounter.assertNumberOfSwitches(
			DynamicScriptingStatus.PAUSED, DynamicScriptingStatus.STOPPED, 1
		);
		assertThat(currentAction.get().getDynamicScriptingStatus(), is(ActionDynamicScriptingStatus.NO_STATUS));
	}
	
	@Test
	public void test_GIVEN_current_action_paused_during_execution_WHEN_stop_AND_script_not_finished_THEN_no_state_change() {
		// Arrange
		currentAction = Optional.of(new ScriptGeneratorAction(new HashMap<>()));
		currentAction.get().setPausedDuringExecution();
		reloadState();
		// Act
		state.stop();
		// Assert
		statusSwitchCounter.assertNoSwitches();
		assertThat(currentAction.get().wasPausedDuringExecution(), is(true));
	}
	
	@Test
	public void test_GIVEN_current_action_empty_WHEN_stop_AND_script_does_not_finish_THEN_stop() {
		// Arrange
		currentAction = Optional.empty();
		reloadState();
		// Act
		state.stop();
		// Assert
		statusSwitchCounter.assertNumberOfSwitches(
			DynamicScriptingStatus.PAUSED, DynamicScriptingStatus.STOPPED, 1
		);
	}
	
	@Test
	public void test_GIVEN_current_action_paused_before_execution_WHEN_stop_THEN_state_change() {
		// Arrange
		currentAction = Optional.of(new ScriptGeneratorAction(new HashMap<>()));
		currentAction.get().setPausedBeforeExecution();
		reloadState();
		// Act
		state.stop();
		// Assert
		statusSwitchCounter.assertNumberOfSwitches(
			DynamicScriptingStatus.PAUSED, DynamicScriptingStatus.STOPPED, 1
		);
		assertThat(currentAction.get().getDynamicScriptingStatus(), is(ActionDynamicScriptingStatus.NO_STATUS));
	}
	
	@Test
	public void test_GIVEN_next_action_WHEN_no_stop_AND_script_finishes_THEN_action_changes_AND_new_pause() {
		// Arrange
		when(modelAdapter.getActionAfter(currentAction.get())).thenReturn(nextAction);
		// Act
		simulateScriptFinished();
		// Assert
		statusSwitchCounter.assertNumberOfSwitches(
			DynamicScriptingStatus.PAUSED, DynamicScriptingStatus.PAUSED, 1
		);
		assertThat(currentAction.get().getDynamicScriptingStatus(), is(ActionDynamicScriptingStatus.NO_STATUS));
		assertThat(state.getCurrentlyExecutingAction(), is(nextAction));
		assertThat(nextAction.get().getDynamicScriptingStatus(), is(ActionDynamicScriptingStatus.PAUSED_BEFORE_EXECUTION));
	}
	
	@Test
	public void test_GIVEN_no_next_action_WHEN_no_stop_AND_script_finishes_THEN_stopped() {
		// Arrange
		when(modelAdapter.getActionAfter(currentAction.get())).thenReturn(Optional.empty());
		// Act
		simulateScriptFinished();
		// Assert
		statusSwitchCounter.assertNumberOfSwitches(
			DynamicScriptingStatus.PAUSED, DynamicScriptingStatus.STOPPED, 1
		);
		assertThat(currentAction.get().getDynamicScriptingStatus(), is(ActionDynamicScriptingStatus.NO_STATUS));
		assertThat(state.getCurrentlyExecutingAction(), is(currentAction));
	}
	
	@Test
	public void test_GIVEN_no_current_action_WHEN_no_stop_AND_script_finishes_THEN_stopped() {
		// Arrange
		currentAction = Optional.empty();
		reloadState();
		// Act
		simulateScriptFinished();
		// Assert
		statusSwitchCounter.assertNumberOfSwitches(
			DynamicScriptingStatus.PAUSED, DynamicScriptingStatus.STOPPED, 1
		);
		assertThat(state.getCurrentlyExecutingAction(), is(currentAction));
	}
	
	@Test
	public void test_GIVEN_pause_complete_WHEN_play_THEN_state_switches_to_playing() {
		// Arrange
		pauseComplete = true;
		removeStatusSwitchCounterToState();
		setUpState();
		attachStatusSwitchCounterToState();
		// Act
		state.play();
		// Assert
		statusSwitchCounter.assertNumberOfSwitches(
			DynamicScriptingStatus.PAUSED, DynamicScriptingStatus.PLAYING, 1
		);
	}
	
	@Test
	public void test_GIVEN_pause_not_complete_WHEN_play_THEN_state_switches_to_playing_after_script_paused_by_nicos() {
		// Arrange
		pauseComplete = false;
		removeStatusSwitchCounterToState();
		setUpState();
		attachStatusSwitchCounterToState();
		// Act
		state.play();
		// Assert
		statusSwitchCounter.assertNumberOfSwitches(
			DynamicScriptingStatus.PAUSED, DynamicScriptingStatus.PLAYING, 0
		);
		// Act
		state.propertyChange(
			new PropertyChangeEvent(
				nicosAdapter, DynamicScriptingProperties.SCRIPT_PAUSED_PROPERTY, 
				null, ScriptStatus.INBREAK
			)
		);
		// Assert
		statusSwitchCounter.assertNumberOfSwitches(
			DynamicScriptingStatus.PAUSED, DynamicScriptingStatus.PLAYING, 1
		);
	}

}
