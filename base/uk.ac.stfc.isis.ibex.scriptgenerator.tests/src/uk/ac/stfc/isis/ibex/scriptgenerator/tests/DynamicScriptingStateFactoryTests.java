package uk.ac.stfc.isis.ibex.scriptgenerator.tests;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;

import uk.ac.stfc.isis.ibex.scriptgenerator.dynamicscripting.DynamicScript;
import uk.ac.stfc.isis.ibex.scriptgenerator.dynamicscripting.DynamicScriptingException;
import uk.ac.stfc.isis.ibex.scriptgenerator.dynamicscripting.DynamicScriptingModelAdapter;
import uk.ac.stfc.isis.ibex.scriptgenerator.dynamicscripting.DynamicScriptingNicosAdapter;
import uk.ac.stfc.isis.ibex.scriptgenerator.dynamicscripting.DynamicScriptingState;
import uk.ac.stfc.isis.ibex.scriptgenerator.dynamicscripting.DynamicScriptingStateFactory;
import uk.ac.stfc.isis.ibex.scriptgenerator.dynamicscripting.DynamicScriptingStatus;
import uk.ac.stfc.isis.ibex.scriptgenerator.dynamicscripting.ErrorState;
import uk.ac.stfc.isis.ibex.scriptgenerator.dynamicscripting.PlayingState;
import uk.ac.stfc.isis.ibex.scriptgenerator.dynamicscripting.StoppedState;
import uk.ac.stfc.isis.ibex.scriptgenerator.table.ScriptGeneratorAction;

public class DynamicScriptingStateFactoryTests {
	
	private DynamicScriptingStateFactory factory;
	private DynamicScriptingNicosAdapter nicosAdapter;
	private DynamicScriptingModelAdapter modelAdapter;
	private DynamicScriptingState state;
	
	@Before
	public void setUp() throws DynamicScriptingException {
		nicosAdapter = mock(DynamicScriptingNicosAdapter.class);
		modelAdapter = mock(DynamicScriptingModelAdapter.class);
		Integer scriptId = 1;
		DynamicScript script = new DynamicScript(scriptId);
		ScriptGeneratorAction action = new ScriptGeneratorAction(new HashMap<>());
		when(modelAdapter.getDynamicScript()).thenReturn(Optional.of(script));
		when(modelAdapter.getFirstAction()).thenReturn(Optional.of(action));
		when(modelAdapter.refreshGeneratedScript(action)).thenReturn(Optional.of(scriptId));
		state = mock(StoppedState.class);
		factory = new DynamicScriptingStateFactory(modelAdapter, nicosAdapter, state);
	}
	
	@Test
	public void test_GIVEN_initial_state_THEN_state_set_by_constructor() {
		assertThat(factory.getCurrentState(), is(state));
	}
	
	@Test
	public void test_WHEN_change_state_to_playing_THEN_state_changed() {
		DynamicScriptingState newState = factory.changeState(DynamicScriptingStatus.PLAYING);
		DynamicScriptingState newStateFromGet = factory.getCurrentState();
		assertThat(newState, is(not(state)));
		assertThat(newStateFromGet, is(not(state)));
		assertTrue(newState instanceof PlayingState);
		assertTrue(newStateFromGet instanceof PlayingState);
	}
	
	@Test
	public void test_WHEN_change_state_to_stopped_THEN_state_changed() {
		DynamicScriptingState newState = factory.changeState(DynamicScriptingStatus.STOPPED);
		DynamicScriptingState newStateFromGet = factory.getCurrentState();
		assertThat(newState, is(not(state)));
		assertThat(newStateFromGet, is(not(state)));
		assertTrue(newState instanceof StoppedState);
		assertTrue(newStateFromGet instanceof StoppedState);
	}
	
	@Test
	public void test_WHEN_change_state_to_error_THEN_state_changed() {
		DynamicScriptingState newState = factory.changeState(DynamicScriptingStatus.ERROR);
		DynamicScriptingState newStateFromGet = factory.getCurrentState();
		assertThat(newState, is(not(state)));
		assertThat(newStateFromGet, is(not(state)));
		assertTrue(newState instanceof ErrorState);
		assertTrue(newStateFromGet instanceof ErrorState);
	}

}
