package uk.ac.stfc.isis.ibex.scriptgenerator.tests;

import org.junit.After;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.beans.PropertyChangeEvent;
import java.util.HashMap;
import java.util.Optional;

import uk.ac.stfc.isis.ibex.scriptgenerator.ScriptGeneratorProperties;
import uk.ac.stfc.isis.ibex.scriptgenerator.dynamicscripting.DynamicScriptName;
import uk.ac.stfc.isis.ibex.scriptgenerator.dynamicscripting.DynamicScriptingModelAdapter;
import uk.ac.stfc.isis.ibex.scriptgenerator.dynamicscripting.DynamicScriptingNicosAdapter;
import uk.ac.stfc.isis.ibex.scriptgenerator.dynamicscripting.DynamicScriptingStatus;
import uk.ac.stfc.isis.ibex.scriptgenerator.dynamicscripting.PlayingState;
import uk.ac.stfc.isis.ibex.scriptgenerator.table.ScriptGeneratorAction;
import uk.ac.stfc.isis.ibex.scriptgenerator.tests.utils.ScriptGeneratorMockBuilder;

public class PlayingStateTest extends DynamicScriptingStateTest {
	
	private DynamicScriptingNicosAdapter nicosAdapter;
	private DynamicScriptingModelAdapter modelAdapter;
	
	private ScriptGeneratorMockBuilder scriptGeneratorMockBuilder;
	
	@Override
	protected void setUpScaffolding() {
		scriptGeneratorMockBuilder = new ScriptGeneratorMockBuilder();
		super.setUpScaffolding();
	}
	
	@Override
	protected void setUpState() {
		nicosAdapter = new DynamicScriptingNicosAdapter(scriptGeneratorMockBuilder.getMockNicosModel());
		modelAdapter = new DynamicScriptingModelAdapter(scriptGeneratorMockBuilder.getMockScriptGeneratorModel());
		state = new PlayingState(nicosAdapter, modelAdapter, dynamicScriptIdsToAction);
		nicosAdapter.addPropertyChangeListener(state);
		modelAdapter.addPropertyChangeListener(state);
	}
	
	@After
	public void tearDown() {
		removeStateListeners();
		removeStatusSwitchCounterToState();
	}
	
	private void removeStateListeners() {
		nicosAdapter.removePropertyChangeListener(state);
		modelAdapter.removePropertyChangeListener(state);
	}
	
	@Override
	protected void setUpDynamicScriptIds() {
		dynamicScriptIdsToAction = new HashMap<Integer, ScriptGeneratorAction>();
	}
	
	protected void simulateScriptGenerated(Integer scriptId) {
		when(scriptGeneratorMockBuilder.getMockScriptGeneratorModel().getScriptFromId(1)).thenReturn(Optional.of("test"));
		PropertyChangeEvent event = new PropertyChangeEvent(
			scriptGeneratorMockBuilder.getMockScriptGeneratorModel(), 
			ScriptGeneratorProperties.GENERATED_SCRIPT_PROPERTY, 
			null, 1
		);
		modelAdapter.propertyChange(event);
	}
	
	protected void simulateScriptExecuted(Integer scriptId) {
		DynamicScriptName newName = new DynamicScriptName(Optional.of("Script Generator: " + scriptId));
		nicosAdapter.scriptChanged(newName);
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
		simulateScriptGenerated(1);
		assertTrue(state.isScriptDynamic(0));
		simulateScriptExecuted(1);
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
		int i = 0;
		do {
			currentAction = state.getCurrentlyExecutingAction();
			nextAction = state.getNextExecutingAction();
			assertThat(currentAction, is(scriptGeneratorMockBuilder.getMockScriptGeneratorAction(i)));
			assertThat(nextAction, is(scriptGeneratorMockBuilder.getMockScriptGeneratorAction(i + 1)));
			simulateScriptGenerated(i);
			assertTrue(state.isScriptDynamic(i));
			simulateScriptExecuted(i);
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
		Optional<ScriptGeneratorAction> currentAction = state.getCurrentlyExecutingAction();
		Optional<ScriptGeneratorAction> nextAction = state.getNextExecutingAction();
		assertThat(currentAction, is(scriptGeneratorMockBuilder.getMockScriptGeneratorAction(0)));
		assertThat(nextAction, is(scriptGeneratorMockBuilder.getMockScriptGeneratorAction(1)));
		// Act
		simulateScriptGenerated(1);
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
		Optional<ScriptGeneratorAction> currentAction = state.getCurrentlyExecutingAction();
		Optional<ScriptGeneratorAction> nextAction = state.getNextExecutingAction();
		assertThat(currentAction, is(scriptGeneratorMockBuilder.getMockScriptGeneratorAction(0)));
		assertThat(nextAction, is(scriptGeneratorMockBuilder.getMockScriptGeneratorAction(1)));
		// Act
		simulateScriptGenerated(1);
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
		removeStateListeners();
		removeStatusSwitchCounterToState();
		setUpState();
		// Assert
		Optional<ScriptGeneratorAction> currentAction = state.getCurrentlyExecutingAction();
		assertThat(currentAction, is(Optional.empty()));
		Optional<ScriptGeneratorAction> nextAction = state.getNextExecutingAction();
		assertThat(nextAction, is(Optional.empty()));
	}

}
