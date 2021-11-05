package uk.ac.stfc.isis.ibex.scriptgenerator.tests;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.not;

import uk.ac.stfc.isis.ibex.nicos.ScriptStatus;
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
	private Integer numberOfSwitchesFromPlayingToStoppedState;
	
	private ScriptGeneratorMockBuilder scriptGeneratorMockBuilder;
	
	@Before
	public void setUp() {
		// Set up mocks
		scriptGeneratorMockBuilder = new ScriptGeneratorMockBuilder();
		numberOfSwitchesFromPlayingToStoppedState = 0;
		// Set up class under test
		nicosFacade = new DynamicScriptingNicosFacade(scriptGeneratorMockBuilder.getMockNicosModel());
		state = new PlayingState(
				scriptGeneratorMockBuilder.getMockScriptGeneratorModel(), 
				scriptGeneratorMockBuilder.getMockNicosModel(), 
				nicosFacade
		);
		state.addPropertyChangeListener(DynamicScriptingProperties.STATE_CHANGE_PROPERTY, event -> {
			DynamicScriptingState oldState = (DynamicScriptingState) event.getOldValue();
			DynamicScriptingState newState = (DynamicScriptingState) event.getNewValue();
			if (oldState.getStatus() == DynamicScriptingStatus.PLAYING && newState.getStatus() == DynamicScriptingStatus.STOPPED) {
				numberOfSwitchesFromPlayingToStoppedState++;
			}
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
	
//	@Test
//	public void test_WHEN_pause_THEN_state_is_paused() {
//		// Act
//		DynamicScriptingState newState = state.pause();
//		// Assert
//		assertThat(newState, is(not(state)));
//	}
	
	@Test
	public void test_step_through_one_action() {
		// Assert
		Optional<ScriptGeneratorAction> currentAction = state.getCurrentlyExecutingAction();
		Optional<ScriptGeneratorAction> nextAction = state.getNextExecutingAction();
		assertThat(currentAction, is(scriptGeneratorMockBuilder.getMockScriptGeneratorAction(0)));
		assertThat(nextAction, is(scriptGeneratorMockBuilder.getMockScriptGeneratorAction(1)));
		// Act
		nicosFacade.setScriptStatus(ScriptStatus.IDLE);
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
		int i = 0;
		do {
			currentAction = state.getCurrentlyExecutingAction();
			nextAction = state.getNextExecutingAction();
			assertThat(currentAction, is(scriptGeneratorMockBuilder.getMockScriptGeneratorAction(i)));
			assertThat(nextAction, is(scriptGeneratorMockBuilder.getMockScriptGeneratorAction(i + 1)));
			nicosFacade.setScriptStatus(ScriptStatus.IDLE);
			i++;
		} while (nextAction.isPresent());
		assertThat(numberOfSwitchesFromPlayingToStoppedState, is(1));
	}

}
