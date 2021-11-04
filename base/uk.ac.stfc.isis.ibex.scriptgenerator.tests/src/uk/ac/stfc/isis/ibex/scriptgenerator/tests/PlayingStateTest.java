package uk.ac.stfc.isis.ibex.scriptgenerator.tests;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.Optional;

import static org.hamcrest.CoreMatchers.not;

import uk.ac.stfc.isis.ibex.nicos.ScriptStatus;
import uk.ac.stfc.isis.ibex.scriptgenerator.dynamicscripting.DynamicScriptingState;
import uk.ac.stfc.isis.ibex.scriptgenerator.dynamicscripting.DynamicScriptingStatus;
import uk.ac.stfc.isis.ibex.scriptgenerator.dynamicscripting.PlayingState;
import uk.ac.stfc.isis.ibex.scriptgenerator.table.ScriptGeneratorAction;
import uk.ac.stfc.isis.ibex.scriptgenerator.tests.utils.ScriptGeneratorMockBuilder;

public class PlayingStateTest {
	
	private PlayingState state;
	
	private ScriptGeneratorMockBuilder scriptGeneratorMockBuilder;
	
	@Before
	public void setUp() {
		// Set up mocks
		scriptGeneratorMockBuilder = new ScriptGeneratorMockBuilder();
		// Set up class under test
		state = new PlayingState(scriptGeneratorMockBuilder.getMockScriptGeneratorModel(), scriptGeneratorMockBuilder.getMockNicosModel());
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
	public void test_stepping_through_actions() {
		var mockNicosModel = scriptGeneratorMockBuilder.getMockNicosModel();
		// Act
		Optional<ScriptGeneratorAction> currentAction = state.getCurrentlyExecutingAction();
		Optional<ScriptGeneratorAction> nextAction = state.getNextExecutingAction();
		// Assert
		assertThat(currentAction, is(scriptGeneratorMockBuilder.getMockScriptGeneratorAction(0)));
		assertThat(nextAction, is(scriptGeneratorMockBuilder.getMockScriptGeneratorAction(1)));
		// Act
		mockNicosModel.getScriptStatus();
		mockNicosModel.runTestUpdate(ScriptStatus.IDLE);
		// Act
		currentAction = state.getCurrentlyExecutingAction();
		nextAction = state.getNextExecutingAction();
		// Assert
		assertThat(currentAction, is(scriptGeneratorMockBuilder.getMockScriptGeneratorAction(1)));
		assertThat(nextAction, is(scriptGeneratorMockBuilder.getMockScriptGeneratorAction(2)));
	}

}
