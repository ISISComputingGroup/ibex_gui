package uk.ac.stfc.isis.ibex.scriptgenerator.tests;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.junit.Test;

import uk.ac.stfc.isis.ibex.scriptgenerator.dynamicscripting.DynamicScriptingStatus;
import uk.ac.stfc.isis.ibex.scriptgenerator.dynamicscripting.PausedState;

public class PausedStateTest extends DynamicScriptingStateTest {

	@Override
	protected void setUpState() {
		state = new PausedState(dynamicScriptIdsToAction);
	}
	
	@Test
	public void test_status_correct() {
		assertThat(state.getStatus(), is(DynamicScriptingStatus.PAUSED));
	}

}
