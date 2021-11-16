package uk.ac.stfc.isis.ibex.scriptgenerator.tests;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;

import java.beans.PropertyChangeEvent;
import java.util.HashMap;
import java.util.Optional;

import org.junit.Test;

import uk.ac.stfc.isis.ibex.scriptgenerator.dynamicscripting.DynamicScriptingNicosAdapter;
import uk.ac.stfc.isis.ibex.scriptgenerator.dynamicscripting.DynamicScriptingProperties;
import uk.ac.stfc.isis.ibex.scriptgenerator.dynamicscripting.DynamicScriptingStatus;
import uk.ac.stfc.isis.ibex.scriptgenerator.dynamicscripting.PausedState;
import uk.ac.stfc.isis.ibex.scriptgenerator.table.ActionDynamicScriptingStatus;
import uk.ac.stfc.isis.ibex.scriptgenerator.table.ScriptGeneratorAction;

public class PausedStateTest extends DynamicScriptingStateTest {
	
	private DynamicScriptingNicosAdapter nicosAdapter;
	private ScriptGeneratorAction currentAction;
	
	@Override
	protected void setUpState() {
		currentAction = new ScriptGeneratorAction(new HashMap<>());
		state = new PausedState(nicosAdapter, Optional.ofNullable(currentAction), dynamicScriptIdsToAction);
	}
	
	@Override
	protected void setUpScaffolding() {
		super.setUpScaffolding();
		nicosAdapter = mock(DynamicScriptingNicosAdapter.class);
	}
	
	private void simulateScriptFinished() {
		state.propertyChange(
			new PropertyChangeEvent(
				nicosAdapter, DynamicScriptingProperties.SCRIPT_FINISHED_PROPERTY,
				null, null
			)
		);
	}
	
	@Test
	public void test_status_correct() {
		assertThat(state.getStatus(), is(DynamicScriptingStatus.PAUSED));
	}
	
	@Test
	public void test_WHEN_stop_THEN_state_is_stopped() {
		state.stop();
		simulateScriptFinished();
		statusSwitchCounter.assertNumberOfSwitches(
			DynamicScriptingStatus.PAUSED, DynamicScriptingStatus.STOPPED, 1
		);
		assertThat(action.getDynamicScriptingStatus(), is(ActionDynamicScriptingStatus.NO_STATUS));
	}
	
	@Test
	public void test_WHEN_play_THEN_state_is_resumed() {
		state.play();
		statusSwitchCounter.assertNumberOfSwitches(
			DynamicScriptingStatus.PAUSED, DynamicScriptingStatus.PLAYING, 1
		);
	}

}
