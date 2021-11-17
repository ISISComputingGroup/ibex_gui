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
import uk.ac.stfc.isis.ibex.scriptgenerator.dynamicscripting.PausedState;
import uk.ac.stfc.isis.ibex.scriptgenerator.dynamicscripting.PlayingState;
import uk.ac.stfc.isis.ibex.scriptgenerator.dynamicscripting.StoppedState;
import uk.ac.stfc.isis.ibex.scriptgenerator.table.ScriptGeneratorAction;

public class DynamicScriptingStateFactoryTests {
	
	private DynamicScriptingStateFactory factory;
	private DynamicScriptingNicosAdapter nicosAdapter;
	private DynamicScriptingModelAdapter modelAdapter;
	private DynamicScriptingState state;
	private ScriptGeneratorAction action;
	private Integer scriptId;
	private DynamicScript script;
	
	@Before
	public void setUp() throws DynamicScriptingException {
		nicosAdapter = mock(DynamicScriptingNicosAdapter.class);
		modelAdapter = mock(DynamicScriptingModelAdapter.class);
		scriptId = 1;
		script = new DynamicScript(scriptId);
		action = new ScriptGeneratorAction(new HashMap<>());
		when(modelAdapter.getDynamicScript()).thenReturn(Optional.of(script));
		when(modelAdapter.getFirstAction()).thenReturn(Optional.of(action));
		when(modelAdapter.refreshGeneratedScript(action)).thenReturn(Optional.of(scriptId));
	}
	
	public void setUpFactoryWithState(DynamicScriptingState state) {
		this.state = state;
		factory = new DynamicScriptingStateFactory(modelAdapter, nicosAdapter, this.state);
	}
	
	private void assertStatesAreNewAndAreInstanceOf(DynamicScriptingState newState, DynamicScriptingState otherNewState, DynamicScriptingStatus expectedStatus) {
		assertThat(newState, is(not(state)));
		assertThat(otherNewState, is(not(state)));
		assertThat(newState.getStatus(), is(expectedStatus));
		assertThat(otherNewState.getStatus(), is(expectedStatus));
	}
	
	@Test
	public void test_GIVEN_initial_state_THEN_state_set_by_constructor() {
		state = mock(StoppedState.class);
		when(state.getCurrentlyExecutingAction()).thenReturn(Optional.empty());
		setUpFactoryWithState(state);
		assertThat(factory.getCurrentState(), is(state));
	}
	
	@Test
	public void test_WHEN_change_state_from_stopped_to_playing_THEN_state_changed() {
		state = mock(StoppedState.class);
		when(state.getCurrentlyExecutingAction()).thenReturn(Optional.empty());
		setUpFactoryWithState(state);
		DynamicScriptingState newState = factory.changeState(DynamicScriptingStatus.PLAYING);
		DynamicScriptingState newStateFromGet = factory.getCurrentState();
		assertStatesAreNewAndAreInstanceOf(newState, newStateFromGet, DynamicScriptingStatus.PLAYING);
	}
	
	@Test
	public void test_WHEN_change_state_from_stopped_to_stopped_THEN_state_changed() {
		state = mock(StoppedState.class);
		when(state.getCurrentlyExecutingAction()).thenReturn(Optional.empty());
		setUpFactoryWithState(state);
		DynamicScriptingState newState = factory.changeState(DynamicScriptingStatus.STOPPED);
		DynamicScriptingState newStateFromGet = factory.getCurrentState();
		assertStatesAreNewAndAreInstanceOf(newState, newStateFromGet, DynamicScriptingStatus.STOPPED);
	}
	
	@Test
	public void test_WHEN_change_state_from_stopped_to_error_THEN_state_changed() {
		state = mock(StoppedState.class);
		when(state.getCurrentlyExecutingAction()).thenReturn(Optional.empty());
		setUpFactoryWithState(state);
		DynamicScriptingState newState = factory.changeState(DynamicScriptingStatus.ERROR);
		DynamicScriptingState newStateFromGet = factory.getCurrentState();
		assertStatesAreNewAndAreInstanceOf(newState, newStateFromGet, DynamicScriptingStatus.ERROR);
	}
	
	@Test
	public void test_WHEN_change_state_from_stopped_to_paused_THEN_state_changed() {
		state = mock(StoppedState.class);
		when(state.getCurrentlyExecutingAction()).thenReturn(Optional.empty());
		setUpFactoryWithState(state);
		DynamicScriptingState newState = factory.changeState(DynamicScriptingStatus.PAUSED);
		DynamicScriptingState newStateFromGet = factory.getCurrentState();
		assertStatesAreNewAndAreInstanceOf(newState, newStateFromGet, DynamicScriptingStatus.PAUSED);
	}
	
	@Test
	public void test_WHEN_change_state_from_playing_to_playing_THEN_state_changed() {
		state = mock(PlayingState.class);
		when(state.getCurrentlyExecutingAction()).thenReturn(Optional.of(action));
		setUpFactoryWithState(state);
		DynamicScriptingState newState = factory.changeState(DynamicScriptingStatus.PLAYING);
		DynamicScriptingState newStateFromGet = factory.getCurrentState();
		assertStatesAreNewAndAreInstanceOf(newState, newStateFromGet, DynamicScriptingStatus.PLAYING);
		assertThat(newState.getCurrentlyExecutingAction().get(), is(action));
	}
	
	@Test
	public void test_WHEN_change_state_from_playing_to_stopped_THEN_state_changed() {
		state = mock(PlayingState.class);
		when(state.getCurrentlyExecutingAction()).thenReturn(Optional.of(action));
		setUpFactoryWithState(state);
		DynamicScriptingState newState = factory.changeState(DynamicScriptingStatus.STOPPED);
		DynamicScriptingState newStateFromGet = factory.getCurrentState();
		assertStatesAreNewAndAreInstanceOf(newState, newStateFromGet, DynamicScriptingStatus.STOPPED);
	}
	
	@Test
	public void test_WHEN_change_state_from_playing_to_error_THEN_state_changed() {
		state = mock(PlayingState.class);
		when(state.getCurrentlyExecutingAction()).thenReturn(Optional.of(action));
		setUpFactoryWithState(state);
		DynamicScriptingState newState = factory.changeState(DynamicScriptingStatus.ERROR);
		DynamicScriptingState newStateFromGet = factory.getCurrentState();
		assertStatesAreNewAndAreInstanceOf(newState, newStateFromGet, DynamicScriptingStatus.ERROR);
	}
	
	@Test
	public void test_WHEN_change_state_from_playing_to_paused_THEN_state_changed() {
		state = mock(PlayingState.class);
		when(state.getCurrentlyExecutingAction()).thenReturn(Optional.of(action));
		setUpFactoryWithState(state);
		DynamicScriptingState newState = factory.changeState(DynamicScriptingStatus.PAUSED);
		DynamicScriptingState newStateFromGet = factory.getCurrentState();
		assertStatesAreNewAndAreInstanceOf(newState, newStateFromGet, DynamicScriptingStatus.PAUSED);
		assertThat(newState.getCurrentlyExecutingAction().get(), is(action));
	}
	
	@Test
	public void test_WHEN_change_state_from_paused_to_playing_THEN_state_changed() {
		state = mock(PausedState.class);
		when(state.getCurrentlyExecutingAction()).thenReturn(Optional.of(action));
		setUpFactoryWithState(state);
		DynamicScriptingState newState = factory.changeState(DynamicScriptingStatus.PLAYING);
		DynamicScriptingState newStateFromGet = factory.getCurrentState();
		assertStatesAreNewAndAreInstanceOf(newState, newStateFromGet, DynamicScriptingStatus.PLAYING);
		assertThat(newState.getCurrentlyExecutingAction().get(), is(action));
	}
	
	@Test
	public void test_WHEN_change_state_from_paused_to_stopped_THEN_state_changed() {
		state = mock(PausedState.class);
		when(state.getCurrentlyExecutingAction()).thenReturn(Optional.of(action));
		setUpFactoryWithState(state);
		DynamicScriptingState newState = factory.changeState(DynamicScriptingStatus.STOPPED);
		DynamicScriptingState newStateFromGet = factory.getCurrentState();
		assertStatesAreNewAndAreInstanceOf(newState, newStateFromGet, DynamicScriptingStatus.STOPPED);
	}
	
	@Test
	public void test_WHEN_change_state_from_paused_to_error_THEN_state_changed() {
		state = mock(PausedState.class);
		when(state.getCurrentlyExecutingAction()).thenReturn(Optional.of(action));
		setUpFactoryWithState(state);
		DynamicScriptingState newState = factory.changeState(DynamicScriptingStatus.ERROR);
		DynamicScriptingState newStateFromGet = factory.getCurrentState();
		assertStatesAreNewAndAreInstanceOf(newState, newStateFromGet, DynamicScriptingStatus.ERROR);
	}
	
	@Test
	public void test_WHEN_change_state_from_paused_to_paused_THEN_state_changed() {
		state = mock(PausedState.class);
		when(state.getCurrentlyExecutingAction()).thenReturn(Optional.of(action));
		setUpFactoryWithState(state);
		DynamicScriptingState newState = factory.changeState(DynamicScriptingStatus.PAUSED);
		DynamicScriptingState newStateFromGet = factory.getCurrentState();
		assertStatesAreNewAndAreInstanceOf(newState, newStateFromGet, DynamicScriptingStatus.PAUSED);
		assertThat(newState.getCurrentlyExecutingAction().get(), is(action));
	}

}
