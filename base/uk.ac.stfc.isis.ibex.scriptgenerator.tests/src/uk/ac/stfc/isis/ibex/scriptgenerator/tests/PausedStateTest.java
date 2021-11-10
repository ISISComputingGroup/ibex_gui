package uk.ac.stfc.isis.ibex.scriptgenerator.tests;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.beans.PropertyChangeEvent;
import java.util.HashMap;
import java.util.Optional;

import org.junit.Test;

import uk.ac.stfc.isis.ibex.nicos.ScriptStatus;
import uk.ac.stfc.isis.ibex.scriptgenerator.dynamicscripting.DynamicScriptingNicosAdapter;
import uk.ac.stfc.isis.ibex.scriptgenerator.dynamicscripting.DynamicScriptingProperties;
import uk.ac.stfc.isis.ibex.scriptgenerator.dynamicscripting.DynamicScriptingStatus;
import uk.ac.stfc.isis.ibex.scriptgenerator.dynamicscripting.PausedState;
import uk.ac.stfc.isis.ibex.scriptgenerator.table.ScriptGeneratorAction;
import uk.ac.stfc.isis.ibex.scriptgenerator.tests.utils.ScriptGeneratorMockBuilder;

public class PausedStateTest extends DynamicScriptingStateTest {
	
	private DynamicScriptingNicosAdapter nicosAdapter;
	private ScriptGeneratorAction currentAction;
	
	private ScriptGeneratorMockBuilder scriptGeneratorMockBuilder;

	@Override
	protected void setUpState() {
		currentAction = new ScriptGeneratorAction(new HashMap<>());
		state = new PausedState(nicosAdapter, Optional.ofNullable(currentAction), dynamicScriptIdsToAction);
		nicosAdapter.addPropertyChangeListener(state);
	}
	
	@Override
	protected void setUpScaffolding() {
		scriptGeneratorMockBuilder = new ScriptGeneratorMockBuilder();
		super.setUpScaffolding();
		nicosAdapter = new DynamicScriptingNicosAdapter(scriptGeneratorMockBuilder.getMockNicosModel());
	}
	
	@Test
	public void test_status_correct() {
		assertThat(state.getStatus(), is(DynamicScriptingStatus.PAUSED));
	}
	
	@Test
	public void test_WHEN_paused_THEN_action_executing() {
		assertTrue(currentAction.isExecuting());
	}
	
	@Test
	public void test_WHEN_stop_THEN_state_is_stopped() {
		state.stop();
		nicosAdapter.propertyChange(
			new PropertyChangeEvent(
				scriptGeneratorMockBuilder.getMockNicosModel(), DynamicScriptingProperties.SCRIPT_STATUS_PROPERTY,
				ScriptStatus.INBREAK, ScriptStatus.IDLE
			)
		);
		statusSwitchCounter.assertNumberOfSwitches(
			DynamicScriptingStatus.PAUSED, DynamicScriptingStatus.STOPPED, 1
		);
		assertFalse(action.isExecuting());
	}
	
	@Test
	public void test_WHEN_play_THEN_state_is_resumed() {
		state.play();
		nicosAdapter.propertyChange(
			new PropertyChangeEvent(
				scriptGeneratorMockBuilder.getMockNicosModel(), DynamicScriptingProperties.SCRIPT_STATUS_PROPERTY,
				ScriptStatus.INBREAK, ScriptStatus.RUNNING
			)
		);
		statusSwitchCounter.assertNumberOfSwitches(
			DynamicScriptingStatus.PAUSED, DynamicScriptingStatus.PLAYING, 1
		);
		assertTrue(currentAction.isExecuting());
	}

}
