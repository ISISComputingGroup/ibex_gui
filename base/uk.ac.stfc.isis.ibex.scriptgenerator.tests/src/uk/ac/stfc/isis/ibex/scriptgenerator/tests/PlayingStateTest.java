package uk.ac.stfc.isis.ibex.scriptgenerator.tests;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.Optional;

import static org.hamcrest.CoreMatchers.not;

import uk.ac.stfc.isis.ibex.nicos.ScriptStatus;
import uk.ac.stfc.isis.ibex.scriptgenerator.dynamicscripting.DynamicScriptingGeneratorFacade;
import uk.ac.stfc.isis.ibex.scriptgenerator.dynamicscripting.DynamicScriptingNicosFacade;
import uk.ac.stfc.isis.ibex.scriptgenerator.dynamicscripting.DynamicScriptingProperties;
import uk.ac.stfc.isis.ibex.scriptgenerator.dynamicscripting.DynamicScriptingState;
import uk.ac.stfc.isis.ibex.scriptgenerator.dynamicscripting.DynamicScriptingStatus;
import uk.ac.stfc.isis.ibex.scriptgenerator.dynamicscripting.PlayingState;
import uk.ac.stfc.isis.ibex.scriptgenerator.table.ScriptGeneratorAction;
import uk.ac.stfc.isis.ibex.scriptgenerator.tests.utils.ScriptGeneratorMockBuilder;

public class PlayingStateTest {
	
	private PlayingState state;
	private DynamicScriptingNicosFacade nicosFacade;
	private DynamicScriptingGeneratorFacade generatorFacade;
	private Integer numberOfSwitchesFromPlayingToStoppedState;
	private Integer numberOfSwitchesFromPlayingToIdleExc;
	private Optional<Integer> scriptId;
	
	private ScriptGeneratorMockBuilder scriptGeneratorMockBuilder;
	
	@Before
	public void setUp() {
		// Set up mocks
		scriptGeneratorMockBuilder = new ScriptGeneratorMockBuilder();
		numberOfSwitchesFromPlayingToStoppedState = 0;
		numberOfSwitchesFromPlayingToIdleExc = 0;
		scriptId = Optional.empty();
		// Set up class under test
		setUpClassUnderTest();
	}
	
	private void setUpClassUnderTest() {
		nicosFacade = new DynamicScriptingNicosFacade(scriptGeneratorMockBuilder.getMockNicosModel());
		generatorFacade = new DynamicScriptingGeneratorFacade(scriptGeneratorMockBuilder.getMockScriptGeneratorModel());
		state = new PlayingState(
				scriptGeneratorMockBuilder.getMockScriptGeneratorModel(), 
				scriptGeneratorMockBuilder.getMockNicosModel(), 
				nicosFacade, generatorFacade
		);
		state.addPropertyChangeListener(DynamicScriptingProperties.STATE_CHANGE_PROPERTY, event -> {
			DynamicScriptingState oldState = (DynamicScriptingState) event.getOldValue();
			DynamicScriptingState newState = (DynamicScriptingState) event.getNewValue();
			if (oldState.getStatus() == DynamicScriptingStatus.PLAYING && newState.getStatus() == DynamicScriptingStatus.STOPPED) {
				numberOfSwitchesFromPlayingToStoppedState++;
			}
			if (oldState.getStatus() == DynamicScriptingStatus.PLAYING && newState.getStatus() == DynamicScriptingStatus.STOPPED) {
				numberOfSwitchesFromPlayingToIdleExc++;
			}
		});
		state.addPropertyChangeListener(DynamicScriptingProperties.NEW_SCRIPT_ID_PROPERTY, evt -> {
			scriptId = (Optional<Integer>) evt.getNewValue();
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
		assertThat(scriptId, is(Optional.empty()));
		// Act
		generatorFacade.handleScriptGeneration("test");
		nicosFacade.setScriptStatus(ScriptStatus.IDLE);
		assertThat(scriptId, is(Optional.of(0)));
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
		assertThat(numberOfSwitchesFromPlayingToStoppedState, is(0));
		assertThat(scriptId, is(Optional.empty()));
		int i = 0;
		do {
			currentAction = state.getCurrentlyExecutingAction();
			nextAction = state.getNextExecutingAction();
			assertThat(currentAction, is(scriptGeneratorMockBuilder.getMockScriptGeneratorAction(i)));
			assertThat(nextAction, is(scriptGeneratorMockBuilder.getMockScriptGeneratorAction(i + 1)));
			generatorFacade.handleScriptGeneration("test");
			nicosFacade.setScriptStatus(ScriptStatus.IDLE);
			assertThat(scriptId, is(Optional.of(i)));
			i++;
		} while (nextAction.isPresent());
		assertThat(numberOfSwitchesFromPlayingToStoppedState, is(1));
	}
	
	@Test
	public void test_WHEN_nicos_error_THEN_error_state() {
		// Arrange
		scriptGeneratorMockBuilder.arrangeNicosError();
		// Assert
		assertThat(numberOfSwitchesFromPlayingToIdleExc, is(0));
		assertThat(scriptId, is(Optional.empty()));
		Optional<ScriptGeneratorAction> currentAction = state.getCurrentlyExecutingAction();
		Optional<ScriptGeneratorAction> nextAction = state.getNextExecutingAction();
		assertThat(currentAction, is(scriptGeneratorMockBuilder.getMockScriptGeneratorAction(0)));
		assertThat(nextAction, is(scriptGeneratorMockBuilder.getMockScriptGeneratorAction(1)));
		assertThat(scriptId, is(Optional.empty()));
		// Act
		nicosFacade.setScriptStatus(ScriptStatus.IDLE);
		// Assert
		assertThat(scriptId, is(Optional.empty()));
		assertThat(numberOfSwitchesFromPlayingToStoppedState, is(0));
	}
	
	@Test
	public void test_WHEN_nicos_cannot_send_script_THEN_error_state() {
		// Arrange
		scriptGeneratorMockBuilder.arrangeNicosSendScriptFail();
		// Assert
		assertThat(numberOfSwitchesFromPlayingToIdleExc, is(0));
		assertThat(scriptId, is(Optional.empty()));
		Optional<ScriptGeneratorAction> currentAction = state.getCurrentlyExecutingAction();
		Optional<ScriptGeneratorAction> nextAction = state.getNextExecutingAction();
		assertThat(currentAction, is(scriptGeneratorMockBuilder.getMockScriptGeneratorAction(0)));
		assertThat(nextAction, is(scriptGeneratorMockBuilder.getMockScriptGeneratorAction(1)));
		assertThat(scriptId, is(Optional.empty()));
		// Act
		nicosFacade.setScriptStatus(ScriptStatus.IDLE);
		// Assert
		assertThat(scriptId, is(Optional.empty()));
		assertThat(numberOfSwitchesFromPlayingToStoppedState, is(0));
	}
	
	@Test
	public void test_WHEN_no_next_action_THEN_next_action_is_empty() {
		// Arrange
		scriptGeneratorMockBuilder.arrangeNumberOfActions(1);
		assertThat(scriptId, is(Optional.empty()));
		// Assert
		Optional<ScriptGeneratorAction> nextAction = state.getNextExecutingAction();
		assertThat(nextAction, is(Optional.empty()));
		assertThat(scriptId, is(Optional.empty()));
	}
	
	@Test
	public void test_WHEN_no_current_action_THEN_next_action_is_empty() {
		// Arrange
		scriptGeneratorMockBuilder.arrangeNumberOfActions(0);
		setUpClassUnderTest();
		assertThat(scriptId, is(Optional.empty()));
		// Assert
		Optional<ScriptGeneratorAction> currentAction = state.getCurrentlyExecutingAction();
		assertThat(currentAction, is(Optional.empty()));
		Optional<ScriptGeneratorAction> nextAction = state.getNextExecutingAction();
		assertThat(nextAction, is(Optional.empty()));
		assertThat(scriptId, is(Optional.empty()));
	}

}
