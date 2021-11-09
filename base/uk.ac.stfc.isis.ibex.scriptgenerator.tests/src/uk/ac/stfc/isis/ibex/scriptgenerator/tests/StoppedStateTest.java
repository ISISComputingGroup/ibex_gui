package uk.ac.stfc.isis.ibex.scriptgenerator.tests;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;

import uk.ac.stfc.isis.ibex.scriptgenerator.dynamicscripting.DynamicScriptingProperties;
import uk.ac.stfc.isis.ibex.scriptgenerator.dynamicscripting.DynamicScriptingStatus;
import uk.ac.stfc.isis.ibex.scriptgenerator.dynamicscripting.StoppedState;
import uk.ac.stfc.isis.ibex.scriptgenerator.table.ScriptGeneratorAction;
import uk.ac.stfc.isis.ibex.scriptgenerator.tests.utils.StatusSwitchCounter;

public class StoppedStateTest {
	
	private StoppedState state;
	
	private HashMap<Integer, ScriptGeneratorAction> dynamicScriptIdsToAction;
	private StatusSwitchCounter<DynamicScriptingStatus, DynamicScriptingStatus> statusSwitchCounter;
	private Integer dynamicScriptId;
	private ScriptGeneratorAction action;
	
	@Before
	public void setUp() {
		// Set up scaffolding
		dynamicScriptIdsToAction = new HashMap<Integer, ScriptGeneratorAction>();
		dynamicScriptId = 1;
		action = new ScriptGeneratorAction(new HashMap<>());
		dynamicScriptIdsToAction.put(dynamicScriptId, action);
		statusSwitchCounter = new StatusSwitchCounter<>();
		// Set up class under test
		state = new StoppedState(dynamicScriptIdsToAction);
		state.addPropertyChangeListener(DynamicScriptingProperties.STATE_CHANGE_PROPERTY, statusSwitchCounter);
		
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
		state.play();
		// Assert
		statusSwitchCounter.assertNumberOfSwitches(
			DynamicScriptingStatus.STOPPED, DynamicScriptingStatus.PLAYING, 1
		);
	}
	
	@Test
	public void test_WHEN_stop_THEN_state_the_same() {
		// Act
		state.stop();
		// Assert
		statusSwitchCounter.assertNoSwitches();
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
	
	@Test
	public void test_GIVEN_dynamic_script_id_WHEN_check_id_THEN_is_dynamic() {
		// Act and Assert
		assertTrue(state.isScriptDynamic(dynamicScriptId));
	}
	
	@Test
	public void test_GIVEN_incorrect_dynamic_script_id_WHEN_check_id_THEN_is_dynamic() {
		// Act and Assert
		assertFalse(state.isScriptDynamic(3));
	}
	
}
