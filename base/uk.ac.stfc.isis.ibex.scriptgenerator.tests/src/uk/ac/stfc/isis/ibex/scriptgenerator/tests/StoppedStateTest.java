package uk.ac.stfc.isis.ibex.scriptgenerator.tests;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import uk.ac.stfc.isis.ibex.scriptgenerator.dynamicscripting.DynamicScriptingStatus;
import uk.ac.stfc.isis.ibex.scriptgenerator.dynamicscripting.StoppedState;

public class StoppedStateTest extends DynamicScriptingStateTest {
	
	@Before
	public void setUp() {
		setUpScaffolding();
		state = new StoppedState(dynamicScriptIdsToAction);
		attachStatusSwitchCounterToState();
	}
	
	@Test
	public void test_status_is_stopped() {
		DynamicScriptingStatus status = state.getStatus();
		assertThat(status, is(DynamicScriptingStatus.STOPPED));
	}
	
	@Test
	public void test_WHEN_play_THEN_state_is_playing() {
		state.play();
		statusSwitchCounter.assertNumberOfSwitches(
			DynamicScriptingStatus.STOPPED, DynamicScriptingStatus.PLAYING, 1
		);
	}
	
	@Test
	public void test_WHEN_stop_THEN_state_the_same() {
		state.stop();
		statusSwitchCounter.assertNoSwitches();
	}
	
	@Test
	public void test_actions_empty() {
		assertActionsEmpty();
	}
	
	@Test
	public void test_GIVEN_dynamic_script_id_WHEN_check_id_THEN_is_dynamic() {
		assertTrue(state.isScriptDynamic(dynamicScriptId));
	}
	
	@Test
	public void test_GIVEN_incorrect_dynamic_script_id_WHEN_check_id_THEN_is_dynamic() {
		assertFalse(state.isScriptDynamic(3));
	}
	
}
