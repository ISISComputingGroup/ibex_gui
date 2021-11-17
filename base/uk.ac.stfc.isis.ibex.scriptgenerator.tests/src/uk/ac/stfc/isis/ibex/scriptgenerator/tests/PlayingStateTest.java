package uk.ac.stfc.isis.ibex.scriptgenerator.tests;

import org.junit.After;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.mock;

import java.beans.PropertyChangeEvent;
import java.util.HashMap;
import java.util.Optional;

import uk.ac.stfc.isis.ibex.nicos.ScriptStatus;
import uk.ac.stfc.isis.ibex.scriptgenerator.ScriptGeneratorProperties;
import uk.ac.stfc.isis.ibex.scriptgenerator.dynamicscripting.DynamicScript;
import uk.ac.stfc.isis.ibex.scriptgenerator.dynamicscripting.DynamicScriptingException;
import uk.ac.stfc.isis.ibex.scriptgenerator.dynamicscripting.DynamicScriptingModelAdapter;
import uk.ac.stfc.isis.ibex.scriptgenerator.dynamicscripting.DynamicScriptingNicosAdapter;
import uk.ac.stfc.isis.ibex.scriptgenerator.dynamicscripting.DynamicScriptingProperties;
import uk.ac.stfc.isis.ibex.scriptgenerator.dynamicscripting.DynamicScriptingStatus;
import uk.ac.stfc.isis.ibex.scriptgenerator.dynamicscripting.PlayingState;
import uk.ac.stfc.isis.ibex.scriptgenerator.generation.InvalidParamsException;
import uk.ac.stfc.isis.ibex.scriptgenerator.table.ActionDynamicScriptingStatus;
import uk.ac.stfc.isis.ibex.scriptgenerator.table.ScriptGeneratorAction;
import uk.ac.stfc.isis.ibex.scriptgenerator.tests.utils.ScriptGeneratorMockBuilder;

public class PlayingStateTest extends DynamicScriptingStateTest {
	
	private DynamicScriptingNicosAdapter nicosAdapter;
	private DynamicScriptingModelAdapter modelAdapter;
	private Optional<ScriptGeneratorAction> firstAction;
	private DynamicScript script;
	
	private ScriptGeneratorMockBuilder scriptGeneratorMockBuilder;
	
	@Override
	protected void setUpScaffolding() {
		firstAction = Optional.empty();
		scriptGeneratorMockBuilder = new ScriptGeneratorMockBuilder();
		super.setUpScaffolding();
		nicosAdapter = new DynamicScriptingNicosAdapter(scriptGeneratorMockBuilder.getMockNicosModel());
		modelAdapter = new DynamicScriptingModelAdapter(scriptGeneratorMockBuilder.getMockScriptGeneratorModel());
	}
	
	@Override
	protected void setUpState() {
		state = new PlayingState(nicosAdapter, modelAdapter, firstAction, dynamicScriptIdsToAction);
		attachStateListeners();
	}
	
	@After
	public void tearDown() {
		removeStateListeners();
		removeStatusSwitchCounterToState();
	}
	
	private void attachStateListeners() {
		nicosAdapter.addPropertyChangeListener(state);
		modelAdapter.addPropertyChangeListener(state);
	}
	
	private void removeStateListeners() {
		nicosAdapter.removePropertyChangeListener(state);
		modelAdapter.removePropertyChangeListener(state);
	}
	
	private void reloadState() {
		removeStateListeners();
		removeStatusSwitchCounterToState();
		setUpState();
		attachStatusSwitchCounterToState();
	}
	
	@Override
	protected void setUpDynamicScriptIds() {
		dynamicScriptIdsToAction = new HashMap<Integer, ScriptGeneratorAction>();
	}
	
	private void simulateScriptGenerated(Integer scriptId) {
		when(scriptGeneratorMockBuilder.getMockScriptGeneratorModel().getScriptFromId(1)).thenReturn(Optional.of("test"));
		PropertyChangeEvent event = new PropertyChangeEvent(
			scriptGeneratorMockBuilder.getMockScriptGeneratorModel(), 
			ScriptGeneratorProperties.GENERATED_SCRIPT_PROPERTY, 
			null, 1
		);
		modelAdapter.propertyChange(event);
	}
	
	private void simulateScriptExecuted(Integer scriptId) {
		nicosAdapter.scriptChanged("Script Generator: " + scriptId);
	}
	
	private void simulateGenerationAndExecutionOfScript(Integer scriptId) {
		simulateScriptGenerated(scriptId);
		simulateScriptExecuted(scriptId);
	}
	
	private void simulateGenerationAndExecutionOfScriptWithDynamicScriptIdAssert(Integer scriptId) {
		simulateScriptGenerated(scriptId);
		assertTrue(state.isScriptDynamic(scriptId));
		simulateScriptExecuted(scriptId);
	}
	
	private void assertCurrentAndNextActionsAreThoseFromTheGivenIndices(Integer currentActionIndex, Integer nextActionIndex) {
		Optional<ScriptGeneratorAction> currentAction = state.getCurrentlyExecutingAction();
		Optional<ScriptGeneratorAction> nextAction = state.getNextExecutingAction();
		assertThat(currentAction, is(scriptGeneratorMockBuilder.getMockScriptGeneratorAction(currentActionIndex)));
		assertThat(nextAction, is(scriptGeneratorMockBuilder.getMockScriptGeneratorAction(nextActionIndex)));
	}
	
	private void createModelAdapterAsMock(Integer scriptId) throws DynamicScriptingException {
		modelAdapter = mock(DynamicScriptingModelAdapter.class);
		ScriptGeneratorAction action = new ScriptGeneratorAction(new HashMap<>());
		when(modelAdapter.getDynamicScript()).thenReturn(Optional.empty());
		when(modelAdapter.getFirstAction()).thenReturn(Optional.of(action));
		when(modelAdapter.refreshGeneratedScript(action)).thenReturn(Optional.of(scriptId));
	}
	
	private void createModelAdapterAsMockWithDynamicScript(Integer scriptId) throws DynamicScriptingException {
		createModelAdapterAsMock(scriptId);
		script = new DynamicScript(scriptId);
		when(modelAdapter.getDynamicScript()).thenReturn(Optional.of(script));
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
		// Arrange
		firstAction = scriptGeneratorMockBuilder.getMockScriptGeneratorAction(1);
		reloadState();
		// Act
		state.stop();
		nicosAdapter.propertyChange(
			new PropertyChangeEvent(
				scriptGeneratorMockBuilder.getMockNicosModel(), DynamicScriptingProperties.SCRIPT_STATUS_PROPERTY,
				null, ScriptStatus.IDLE
			)
		);
		// Assert
		assertThat(
			state.getCurrentlyExecutingAction().get().getDynamicScriptingStatus(), 
			is(ActionDynamicScriptingStatus.NO_STATUS)
		);
		statusSwitchCounter.assertNumberOfSwitches(
			DynamicScriptingStatus.PLAYING, DynamicScriptingStatus.STOPPED, 1
		);
	}
	
	@Test
	public void test_step_through_one_action() {
		// Assert
		state.start();
		assertCurrentAndNextActionsAreThoseFromTheGivenIndices(0, 1);
		// Act
		simulateGenerationAndExecutionOfScript(1);
		// Assert
		assertCurrentAndNextActionsAreThoseFromTheGivenIndices(1, 2);
	}
	
	@Test
	public void test_stepping_through_actions() {
		state.start();
		int i = 0;
		do {
			assertCurrentAndNextActionsAreThoseFromTheGivenIndices(i, i + 1);
			simulateGenerationAndExecutionOfScriptWithDynamicScriptIdAssert(i);
			i++;
		} while (state.getCurrentlyExecutingAction().isPresent());
		statusSwitchCounter.assertNumberOfSwitches(
			DynamicScriptingStatus.PLAYING, DynamicScriptingStatus.STOPPED, 1
		);
	}
	
	@Test
	public void test_WHEN_nicos_error_THEN_error_state() {
		// Arrange
		scriptGeneratorMockBuilder.arrangeNicosError();
		state.start();
		assertCurrentAndNextActionsAreThoseFromTheGivenIndices(0, 1);
		// Act
		simulateGenerationAndExecutionOfScript(1);
		// Assert
		statusSwitchCounter.assertNumberOfSwitches(
			DynamicScriptingStatus.PLAYING, DynamicScriptingStatus.ERROR, 1
		);
	}
	
	@Test
	public void test_WHEN_nicos_cannot_send_script_THEN_error_state() {
		// Arrange
		scriptGeneratorMockBuilder.arrangeNicosSendScriptFail();
		state.start();
		assertCurrentAndNextActionsAreThoseFromTheGivenIndices(0, 1);
		// Act
		simulateGenerationAndExecutionOfScript(1);
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
		assertNextActionEmpty();
	}
	
	@Test
	public void test_WHEN_no_current_action_THEN_next_action_is_empty() {
		// Arrange
		scriptGeneratorMockBuilder.arrangeNumberOfActions(0);
		reloadState();
		// Assert
		assertCurrentAndNextActionsEmpty();
	}
	
	@Test
	public void test_GIVEN_dynamic_scripting_exception_WHEN_generating_THEN_error_state() {
		// Arrange
		scriptGeneratorMockBuilder.arrangeRefreshScriptThrows(new InvalidParamsException("test"));
		// Act
		state.start();
		state.propertyChange(new PropertyChangeEvent(
			modelAdapter, DynamicScriptingProperties.SCRIPT_FINISHED_PROPERTY, 
			Optional.empty(), Optional.of(new DynamicScript(1))
		));
		// Assert
		statusSwitchCounter.assertNumberOfSwitches(
			DynamicScriptingStatus.PLAYING, DynamicScriptingStatus.ERROR, 1
		);
	}
	
	@Test
	public void test_GIVEN_no_dynamic_script_code_WHEN_execute_script_THEN_error_state() throws DynamicScriptingException {
		// Arrange
		createModelAdapterAsMockWithDynamicScript(1);
		reloadState();
		// Act
		state.propertyChange(new PropertyChangeEvent(
			modelAdapter, DynamicScriptingProperties.NICOS_SCRIPT_GENERATED_PROPERTY, 
			Optional.empty(), Optional.of(script)
		));
		// Assert
		statusSwitchCounter.assertNumberOfSwitches(
			DynamicScriptingStatus.PLAYING, DynamicScriptingStatus.ERROR, 1
		);
	}
	
	@Test
	public void test_GIVEN_no_dynamic_script_WHEN_execute_script_THEN_error_state() throws DynamicScriptingException {
		// Arrange
		createModelAdapterAsMock(1);
		reloadState();
		// Act
		state.propertyChange(new PropertyChangeEvent(
			modelAdapter, DynamicScriptingProperties.NICOS_SCRIPT_GENERATED_PROPERTY, 
			Optional.empty(), Optional.of(new DynamicScript(1))
		));
		// Assert
		statusSwitchCounter.assertNumberOfSwitches(
			DynamicScriptingStatus.PLAYING, DynamicScriptingStatus.ERROR, 1
		);
	}
	
	@Test
	public void test_WHEN_paused_THEN_paused() {
		// Arrange
		firstAction = scriptGeneratorMockBuilder.getMockScriptGeneratorAction(1);
		reloadState();
		// Act
		state.pause();
		nicosAdapter.propertyChange(
			new PropertyChangeEvent(
				scriptGeneratorMockBuilder.getMockNicosModel(), DynamicScriptingProperties.SCRIPT_STATUS_PROPERTY,
				null, ScriptStatus.INBREAK
			)
		);
		// Assert
		assertThat(
			state.getCurrentlyExecutingAction().get().getDynamicScriptingStatus(), 
			is(ActionDynamicScriptingStatus.PAUSED_DURING_EXECUTION)
		);
		statusSwitchCounter.assertNumberOfSwitches(
			DynamicScriptingStatus.PLAYING, DynamicScriptingStatus.STOPPED, 0
		);
		statusSwitchCounter.assertNumberOfSwitches(
			DynamicScriptingStatus.PLAYING, DynamicScriptingStatus.PAUSED, 1
		);
	}
	
	@Test
	public void test_GIVEN_action_executing_WHEN_resume_script_THEN_script_resumed() {
		// Arrange
		firstAction = scriptGeneratorMockBuilder.getMockScriptGeneratorAction(1);
		firstAction.get().setExecuting();
		dynamicScriptIdsToAction = new HashMap<Integer, ScriptGeneratorAction>();
		dynamicScriptIdsToAction.put(1, firstAction.get());
		reloadState();
		// Assert
		assertCurrentAndNextActionsAreThoseFromTheGivenIndices(1, 2);
		// Act
		assertTrue(state.isScriptDynamic(1));
		simulateScriptExecuted(1);
		// Assert
		assertCurrentAndNextActionsAreThoseFromTheGivenIndices(2, 3);
	}
	
	@Test
	public void test_GIVEN_action_not_executing_WHEN_play_script_THEN_script_played() {
		// Arrange
		firstAction = scriptGeneratorMockBuilder.getMockScriptGeneratorAction(1);
		firstAction.get().clearDynamicScriptingStatus();
		reloadState();
		// Assert
		assertCurrentAndNextActionsAreThoseFromTheGivenIndices(1, 2);
		// Act
		state.start();
		simulateGenerationAndExecutionOfScriptWithDynamicScriptIdAssert(1);
		// Assert
		assertCurrentAndNextActionsAreThoseFromTheGivenIndices(2, 3);
	}

}
