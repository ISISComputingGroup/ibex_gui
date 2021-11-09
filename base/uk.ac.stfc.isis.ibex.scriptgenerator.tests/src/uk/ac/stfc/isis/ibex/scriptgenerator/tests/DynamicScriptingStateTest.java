package uk.ac.stfc.isis.ibex.scriptgenerator.tests;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.util.HashMap;
import java.util.Optional;

import org.junit.Before;

import uk.ac.stfc.isis.ibex.scriptgenerator.dynamicscripting.DynamicScriptingProperties;
import uk.ac.stfc.isis.ibex.scriptgenerator.dynamicscripting.DynamicScriptingState;
import uk.ac.stfc.isis.ibex.scriptgenerator.dynamicscripting.DynamicScriptingStatus;
import uk.ac.stfc.isis.ibex.scriptgenerator.table.ScriptGeneratorAction;
import uk.ac.stfc.isis.ibex.scriptgenerator.tests.utils.StatusSwitchCounter;

public abstract class DynamicScriptingStateTest {
	
	protected DynamicScriptingState state;
	
	protected HashMap<Integer, ScriptGeneratorAction> dynamicScriptIdsToAction;
	protected StatusSwitchCounter<DynamicScriptingStatus, DynamicScriptingStatus> statusSwitchCounter;
	protected Integer dynamicScriptId;
	protected ScriptGeneratorAction action;
	
	@Before
	public void setUp() {
		setUpScaffolding();
		setUpState();
		attachStatusSwitchCounterToState();
	}
	
	protected void setUpScaffolding() {
		setUpDynamicScriptIds();
		statusSwitchCounter = new StatusSwitchCounter<>();
	}
	
	protected void setUpDynamicScriptIds() {
		dynamicScriptIdsToAction = new HashMap<Integer, ScriptGeneratorAction>();
		dynamicScriptId = 1;
		action = new ScriptGeneratorAction(new HashMap<>());
		dynamicScriptIdsToAction.put(dynamicScriptId, action);
	}
	
	protected abstract void setUpState();
	
	protected void attachStatusSwitchCounterToState() {
		state.addPropertyChangeListener(DynamicScriptingProperties.STATE_CHANGE_PROPERTY, statusSwitchCounter);
	}
	
	protected void removeStatusSwitchCounterToState() {
		state.removePropertyChangeListener(DynamicScriptingProperties.STATE_CHANGE_PROPERTY, statusSwitchCounter);
	}
	
	protected void assertActionsEmpty() {
		assertThat(state.getCurrentlyExecutingAction(), is(Optional.empty()));
		assertThat(state.getNextExecutingAction(), is(Optional.empty()));
	}
	
	

}
