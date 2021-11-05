package uk.ac.stfc.isis.ibex.scriptgenerator.tests;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;

import uk.ac.stfc.isis.ibex.scriptgenerator.dynamicscripting.DynamicScriptingNicosFacade;
import uk.ac.stfc.isis.ibex.scriptgenerator.dynamicscripting.DynamicScriptingState;
import uk.ac.stfc.isis.ibex.scriptgenerator.dynamicscripting.DynamicScriptingStatus;
import uk.ac.stfc.isis.ibex.scriptgenerator.dynamicscripting.StoppedState;
import uk.ac.stfc.isis.ibex.scriptgenerator.table.ScriptGeneratorAction;
import uk.ac.stfc.isis.ibex.scriptgenerator.tests.utils.ScriptGeneratorMockBuilder;

public class StoppedStateTest {
	
	private StoppedState state;
	private DynamicScriptingNicosFacade nicosFacade;
	
	private ScriptGeneratorMockBuilder scriptGeneratorMockBuilder;
	
	@Before
	public void setUp() {
		// Set up mocks
		scriptGeneratorMockBuilder = new ScriptGeneratorMockBuilder();
		// Set up class under test
		nicosFacade = new DynamicScriptingNicosFacade(scriptGeneratorMockBuilder.getMockNicosModel());
		state = new StoppedState(
				scriptGeneratorMockBuilder.getMockScriptGeneratorModel(), 
				scriptGeneratorMockBuilder.getMockNicosModel(),
				nicosFacade
		);
	}
	
	@Test
	public void test_status_is_stopped() {
		// Act
		DynamicScriptingStatus status = state.getStatus();
		// Assert
		assertThat(status, is(DynamicScriptingStatus.STOPPED));
	}
	
	@Test
	public void test_WHEN_play_THEN_state_is_playing() {
		// Act
		DynamicScriptingState newState = state.play();
		// Assert
		assertThat(newState, is(not(state)));
		assertThat(newState.getStatus(), is(DynamicScriptingStatus.PLAYING));
	}
	
	@Test
	public void test_WHEN_stop_THEN_state_the_same() {
		// Act
		DynamicScriptingState newState = state.stop();
		// Assert
		assertThat(newState, is(state));
	}
	
	@Test
	public void test_no_current_action() {
		// Act
		Optional<ScriptGeneratorAction> actionOptional = state.getCurrentlyExecutingAction();
		// Assert
		assertTrue(actionOptional.isEmpty());
	}
	
	@Test
	public void test_no_next_action() {
		// Act
		Optional<ScriptGeneratorAction> actionOptional = state.getNextExecutingAction();
		// Assert
		assertTrue(actionOptional.isEmpty());
	}
	
}
