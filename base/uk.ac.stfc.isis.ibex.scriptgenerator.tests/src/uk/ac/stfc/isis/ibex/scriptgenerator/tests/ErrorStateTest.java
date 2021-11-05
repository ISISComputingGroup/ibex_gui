package uk.ac.stfc.isis.ibex.scriptgenerator.tests;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;

import uk.ac.stfc.isis.ibex.scriptgenerator.dynamicscripting.DynamicScriptingGeneratorFacade;
import uk.ac.stfc.isis.ibex.scriptgenerator.dynamicscripting.DynamicScriptingNicosFacade;
import uk.ac.stfc.isis.ibex.scriptgenerator.dynamicscripting.DynamicScriptingState;
import uk.ac.stfc.isis.ibex.scriptgenerator.dynamicscripting.DynamicScriptingStatus;
import uk.ac.stfc.isis.ibex.scriptgenerator.dynamicscripting.ErrorState;
import uk.ac.stfc.isis.ibex.scriptgenerator.tests.utils.ScriptGeneratorMockBuilder;

public class ErrorStateTest {
	
	private ErrorState state;
	private DynamicScriptingNicosFacade nicosFacade;
	private DynamicScriptingGeneratorFacade generatorFacade;
	
	private ScriptGeneratorMockBuilder scriptGeneratorMockBuilder;
	
	@Before
	public void setUp() {
		scriptGeneratorMockBuilder = new ScriptGeneratorMockBuilder();
		nicosFacade = new DynamicScriptingNicosFacade(scriptGeneratorMockBuilder.getMockNicosModel());
		generatorFacade = new DynamicScriptingGeneratorFacade(scriptGeneratorMockBuilder.getMockScriptGeneratorModel());
		state = new ErrorState(
			scriptGeneratorMockBuilder.getMockScriptGeneratorModel(), 
			scriptGeneratorMockBuilder.getMockNicosModel(),
			nicosFacade, generatorFacade
		);
	}
	
	public void assertActionsEmpty() {
		assertThat(state.getCurrentlyExecutingAction(), is(Optional.empty()));
		assertThat(state.getNextExecutingAction(), is(Optional.empty()));
	}
	
	@Test
	public void test_status_is_error_initially() {
		assertThat(state.getStatus(), is(DynamicScriptingStatus.ERROR));
	}
	
	@Test
	public void test_WHEN_play_THEN_status_is_error() {
		DynamicScriptingState newState = state.play();
		assertThat(newState.getStatus(), is(DynamicScriptingStatus.ERROR));
		assertActionsEmpty();
	}
	
	@Test
	public void test_WHEN_stop_THEN_status_is_error() {
		DynamicScriptingState newState = state.stop();
		assertThat(newState.getStatus(), is(DynamicScriptingStatus.ERROR));
		assertActionsEmpty();
	}
	
	@Test
	public void test_actions_empty() {
		assertActionsEmpty();
	}
	

}
