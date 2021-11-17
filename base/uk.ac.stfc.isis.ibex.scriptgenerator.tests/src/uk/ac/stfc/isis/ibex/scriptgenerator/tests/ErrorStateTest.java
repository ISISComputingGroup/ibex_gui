package uk.ac.stfc.isis.ibex.scriptgenerator.tests;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.junit.Test;

import uk.ac.stfc.isis.ibex.scriptgenerator.dynamicscripting.DynamicScriptingStatus;
import uk.ac.stfc.isis.ibex.scriptgenerator.dynamicscripting.ErrorState;

public class ErrorStateTest extends DynamicScriptingStateTest {
	
	@Override
	public void setUpState() {
		state = new ErrorState(dynamicScriptIdsToAction);
	}
	
	@Test
	public void test_status_is_error_initially() {
		assertThat(state.getStatus(), is(DynamicScriptingStatus.ERROR));
	}
	
	@Test
	public void test_WHEN_play_THEN_status_is_error() {
		state.play();
		statusSwitchCounter.assertNoSwitches();
	}
	
	@Test
	public void test_WHEN_stop_THEN_status_is_error() {
		state.stop();
		statusSwitchCounter.assertNoSwitches();
	}
	
	@Test
	public void test_actions_empty() {
		assertCurrentAndNextActionsEmpty();
	}
	

}
