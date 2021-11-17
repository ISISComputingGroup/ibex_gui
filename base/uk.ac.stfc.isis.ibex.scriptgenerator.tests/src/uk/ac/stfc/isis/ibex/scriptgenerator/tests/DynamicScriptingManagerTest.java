package uk.ac.stfc.isis.ibex.scriptgenerator.tests;

import java.beans.PropertyChangeEvent;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.eclipse.ui.internal.activities.ws.TriggerPointManager;
import org.junit.AssumptionViolatedException;


import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;

import uk.ac.stfc.isis.ibex.nicos.ScriptStatus;
import uk.ac.stfc.isis.ibex.scriptgenerator.ScriptGeneratorProperties;
import uk.ac.stfc.isis.ibex.scriptgenerator.dynamicscripting.DynamicScriptName;
import uk.ac.stfc.isis.ibex.scriptgenerator.dynamicscripting.DynamicScriptingException;
import uk.ac.stfc.isis.ibex.scriptgenerator.dynamicscripting.DynamicScriptingModelAdapter;
import uk.ac.stfc.isis.ibex.scriptgenerator.dynamicscripting.DynamicScriptingManager;
import uk.ac.stfc.isis.ibex.scriptgenerator.dynamicscripting.DynamicScriptingNicosAdapter;
import uk.ac.stfc.isis.ibex.scriptgenerator.dynamicscripting.DynamicScriptingProperties;
import uk.ac.stfc.isis.ibex.scriptgenerator.dynamicscripting.DynamicScriptingState;
import uk.ac.stfc.isis.ibex.scriptgenerator.dynamicscripting.DynamicScriptingStateFactory;
import uk.ac.stfc.isis.ibex.scriptgenerator.dynamicscripting.DynamicScriptingStatus;
import uk.ac.stfc.isis.ibex.scriptgenerator.dynamicscripting.StoppedState;
import uk.ac.stfc.isis.ibex.scriptgenerator.table.ScriptGeneratorAction;
import uk.ac.stfc.isis.ibex.scriptgenerator.tests.utils.ScriptGeneratorMockBuilder;

public class DynamicScriptingManagerTest {
	
	private DynamicScriptingManager dynamicScriptingManager;
	private DynamicScriptingNicosAdapter nicosAdapter;
	private DynamicScriptingModelAdapter modelAdapter;
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
		nicosAdapter = new DynamicScriptingNicosAdapter(scriptGeneratorMockBuilder.getMockNicosModel());
		modelAdapter = new DynamicScriptingModelAdapter(scriptGeneratorMockBuilder.getMockScriptGeneratorModel());
		initialState = new StoppedState(dynamicScriptIds);
		stateFactory = new DynamicScriptingStateFactory(modelAdapter, nicosAdapter, initialState);
		dynamicScriptingManager = new DynamicScriptingManager(stateFactory);
	}
	
	private void playScript() {
		try {
			dynamicScriptingManager.playScript();
		} catch (DynamicScriptingException e) {
			throw new AssumptionViolatedException("Given correct mocking there should be no exception");
		}
	}
	
	private void triggerActionsExecuted(Integer firstActionIndex, Integer finalActionIndex) {
		int i = firstActionIndex;
		do {
			triggerActionExecuted(i);
			i++;
		} while (i <= finalActionIndex );
	}
	
	private void triggerActionExecuted(Integer scriptId) {
		simulateScriptGenerated();
		simulateScriptExecuted(scriptId);
	}
	
	private void simulateScriptGenerated() {
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
	
	private void simulateScriptStatusChange(ScriptStatus oldStatus, ScriptStatus newStatus) {
		nicosAdapter.propertyChange(
			new PropertyChangeEvent(
				scriptGeneratorMockBuilder.getMockNicosModel(),
				DynamicScriptingProperties.SCRIPT_STATUS_PROPERTY, 
				oldStatus, newStatus
			)
		);
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
	public void test_WHEN_play_script_from_stopped_THEN_first_action_queued() {
		// Act
		playScript();
		// Assert
		assertThat(dynamicScriptingManager.getCurrentlyExecutingAction(), is(scriptGeneratorMockBuilder.getMockScriptGeneratorAction(0)));
	}
	
	@Test
	public void test_WHEN_play_script_from_stopped_THEN_actions_executed() {
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
	public void test_WHEN_play_script_from_stopped_AND_stopped_THEN_actions_executed_AND_stopped() {
		// Arrange
		List<ScriptGeneratorAction> actions = scriptGeneratorMockBuilder.getMockScriptGeneratorActions();
		// Act
		playScript();
		triggerActionsExecuted(0, actions.size() / 2);
		dynamicScriptingManager.stopScript();
		simulateScriptStatusChange(ScriptStatus.RUNNING, ScriptStatus.IDLE);
		// Assert
		assertThat(dynamicScriptingManager.getCurrentlyExecutingAction(), is(Optional.empty()));
		assertThat(dynamicScriptingManager.getDynamicScriptingStatus(), is(DynamicScriptingStatus.STOPPED));
	}
	
	@Test
	public void test_WHEN_play_script_from_stopped_AND_stopped_AND_resumed_THEN_actions_executed_AND_stopped_AND_actions_executed() {
		// Arrange
		List<ScriptGeneratorAction> actions = scriptGeneratorMockBuilder.getMockScriptGeneratorActions();
		// Act
		playScript();
		triggerActionsExecuted(0, actions.size() / 2);
		dynamicScriptingManager.stopScript();
		simulateScriptStatusChange(ScriptStatus.RUNNING, ScriptStatus.IDLE);
		// Act and Assert
		playScript();
		int i = 0;
		Optional<ScriptGeneratorAction> currentlyExecutingAction;
		do {
			currentlyExecutingAction = dynamicScriptingManager.getCurrentlyExecutingAction();
			assertThat(currentlyExecutingAction, is(scriptGeneratorMockBuilder.getMockScriptGeneratorAction(i)));
			triggerActionExecuted(i);
			i++;
		} while (i < actions.size() - 1);
		// Assert
		assertThat(dynamicScriptingManager.getDynamicScriptingStatus(), is(DynamicScriptingStatus.PLAYING));
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
	public void test_WHEN_play_script_from_stopped_AND_invalid_params_THEN_dynamic_scripting_in_pause_state() {
		// Arrange
		scriptGeneratorMockBuilder.arrangeRefreshScriptThrows(scriptGeneratorMockBuilder.getinvalidParamsException());
		// Act
		playScript();
		triggerActionExecuted(1);
		// Assert
		assertDynamicScriptingInErrorState();
	}
	
	@Test
	public void test_WHEN_play_script_from_stopped_AND_language_not_supported_THEN_dynamic_scripting_in_error_state() {
		// Arrange
		scriptGeneratorMockBuilder.arrangeRefreshScriptThrows(scriptGeneratorMockBuilder.getUnsupportedLanguageException());
		// Act
		playScript();
		triggerActionExecuted(1);
		// Assert
		assertDynamicScriptingInErrorState();
		
	}
	
	@Test
	public void test_WHEN_play_script_from_stopped_AND_no_script_definition_selected_THEN_dynamic_scripting_in_error_state() {
		// Arrange
		scriptGeneratorMockBuilder.arrangeRefreshScriptThrows(scriptGeneratorMockBuilder.getNoScriptDefSelectedException());
		// Act
		playScript();
		triggerActionExecuted(1);
		// Assert
		assertDynamicScriptingInErrorState();
	}
	
	@Test
	public void test_WHEN_play_script_from_stopped_AND_action_invalid_THEN_dynamic_scripting_in_pause_state() {
		// Arrange
		ScriptGeneratorAction action = scriptGeneratorMockBuilder.getMockScriptGeneratorAction(0).get();
		action.setInvalid("Invalid");
		// Act
		playScript();
		// Assert
		assertThat(dynamicScriptingManager.getCurrentlyExecutingAction(), is(scriptGeneratorMockBuilder.getMockScriptGeneratorAction(0)));
		assertThat(dynamicScriptingManager.getDynamicScriptingStatus(), is(DynamicScriptingStatus.PAUSED));
	}
	
	@Test
	public void test_WHEN_play_script_from_stopped_AND_action_invalid_THEN_dynamic_scripting_in_pause_state_WHEN_correct_validity_AND_resume_THEN_resumed() {
		// Arrange
		ScriptGeneratorAction action = scriptGeneratorMockBuilder.getMockScriptGeneratorAction(0).get();
		action.setInvalid("Invalid");
		// Act
		playScript();
		// Assert
		assertThat(dynamicScriptingManager.getCurrentlyExecutingAction(), is(scriptGeneratorMockBuilder.getMockScriptGeneratorAction(0)));
		assertThat(dynamicScriptingManager.getDynamicScriptingStatus(), is(DynamicScriptingStatus.PAUSED));
		// Act
		action.setValid();
		playScript();
		// Assert
		assertThat(dynamicScriptingManager.getCurrentlyExecutingAction(), is(scriptGeneratorMockBuilder.getMockScriptGeneratorAction(0)));
		assertThat(dynamicScriptingManager.getDynamicScriptingStatus(), is(DynamicScriptingStatus.PLAYING));
	}
	
	@Test
	public void test_WHEN_pause_script_from_stopped_THEN_stopped() {
		// Act
		dynamicScriptingManager.pauseScript();
		// Assert
		assertThat(dynamicScriptingManager.getDynamicScriptingStatus(), is(DynamicScriptingStatus.STOPPED));
	}
	
	@Test
	public void test_WHEN_play_script_AND_pause_script_from_stopped_THEN_actions_executed() {
		// Arrange
		List<ScriptGeneratorAction> actions = scriptGeneratorMockBuilder.getMockScriptGeneratorActions();
		// Act and Assert
		playScript();
		int i = 0;
		Optional<ScriptGeneratorAction> currentlyExecutingAction = dynamicScriptingManager.getCurrentlyExecutingAction();
		do {
			assertThat(dynamicScriptingManager.getCurrentlyExecutingAction(), is(scriptGeneratorMockBuilder.getMockScriptGeneratorAction(i)));
			dynamicScriptingManager.pauseScript();
			simulateScriptStatusChange(ScriptStatus.RUNNING, ScriptStatus.INBREAK);
			assertThat(dynamicScriptingManager.getCurrentlyExecutingAction(), is(scriptGeneratorMockBuilder.getMockScriptGeneratorAction(i)));
			assertThat(dynamicScriptingManager.getDynamicScriptingStatus(), is(DynamicScriptingStatus.PAUSED));
			playScript();
			simulateScriptStatusChange(ScriptStatus.INBREAK, ScriptStatus.RUNNING);
			assertThat(dynamicScriptingManager.getCurrentlyExecutingAction(), is(scriptGeneratorMockBuilder.getMockScriptGeneratorAction(i)));
			assertThat(dynamicScriptingManager.getDynamicScriptingStatus(), is(DynamicScriptingStatus.PLAYING));
			triggerActionExecuted(i);
			currentlyExecutingAction = dynamicScriptingManager.getCurrentlyExecutingAction();
			i++;
		} while (currentlyExecutingAction.isPresent());
		assertThat(i, is(actions.size()));
	}
	
	@Test
	public void test_WHEN_play_script_AND_pause_script_AND_stop_script_from_playing_THEN_actions_executed_AND_stopped() {
		// Arrange
		Integer numOfActions = 10;
		scriptGeneratorMockBuilder.arrangeNumberOfActions(10);
		List<ScriptGeneratorAction> actions = scriptGeneratorMockBuilder.getMockScriptGeneratorActions();
		// Act
		playScript();
		int i = 0;
		do {
			dynamicScriptingManager.pauseScript();
			simulateScriptStatusChange(ScriptStatus.RUNNING, ScriptStatus.INBREAK);
			playScript();
			simulateScriptStatusChange(ScriptStatus.INBREAK, ScriptStatus.RUNNING);
			triggerActionExecuted(i);
			i++;
		} while (i < numOfActions / 2);
		dynamicScriptingManager.stopScript();
		simulateScriptStatusChange(ScriptStatus.RUNNING, ScriptStatus.IDLE);
		// Assert
		assertThat(dynamicScriptingManager.getCurrentlyExecutingAction(), is(Optional.empty()));
		assertThat(dynamicScriptingManager.getDynamicScriptingStatus(), is(DynamicScriptingStatus.STOPPED));
		assertThat(i, is(actions.size() / 2));
		
	}
	
	@Test
	public void test_WHEN_play_script_AND_pause_script_AND_stop_script_from_paused_THEN_actions_executed_AND_stopped() {
		Integer numOfActions = 10;
		scriptGeneratorMockBuilder.arrangeNumberOfActions(10);
		// Act
		playScript();
		triggerActionsExecuted(0, numOfActions / 2);
		dynamicScriptingManager.pauseScript();
		simulateScriptStatusChange(ScriptStatus.RUNNING, ScriptStatus.INBREAK);
		dynamicScriptingManager.stopScript();
		simulateScriptStatusChange(ScriptStatus.RUNNING, ScriptStatus.IDLE);
		// Assert
		assertThat(dynamicScriptingManager.getCurrentlyExecutingAction(), is(Optional.empty()));
		assertThat(dynamicScriptingManager.getDynamicScriptingStatus(), is(DynamicScriptingStatus.STOPPED));
	}

}
