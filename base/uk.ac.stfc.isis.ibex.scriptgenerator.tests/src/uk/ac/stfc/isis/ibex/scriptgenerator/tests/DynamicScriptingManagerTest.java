package uk.ac.stfc.isis.ibex.scriptgenerator.tests;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import uk.ac.stfc.isis.ibex.scriptgenerator.ScriptGeneratorSingleton;
import uk.ac.stfc.isis.ibex.scriptgenerator.dynamicscripting.DynamicScriptingManager;
import uk.ac.stfc.isis.ibex.scriptgenerator.table.ScriptGeneratorAction;

public class DynamicScriptingManagerTest {
	
	ScriptGeneratorSingleton mockScriptGeneratorModel;
	ScriptGeneratorAction mockScriptGeneratorAction;
	Optional<ScriptGeneratorAction> mockScriptGeneratorActionOptional;
	DynamicScriptingManager dynamicScriptingManager;
	
	@Before
	public void setUp() {
		// Set up mocks
		mockScriptGeneratorModel = mock(ScriptGeneratorSingleton.class);
		mockScriptGeneratorAction = mock(ScriptGeneratorAction.class);
		mockScriptGeneratorActionOptional = Optional.of(mockScriptGeneratorAction);
		when(mockScriptGeneratorModel.getAction(0)).thenReturn(mockScriptGeneratorActionOptional);
		
		// Set up class under test
		dynamicScriptingManager = new DynamicScriptingManager(mockScriptGeneratorModel);
	}
	
	@Test
	public void test_WHEN_play_script_THEN_first_action_queued() {
		// Act
		dynamicScriptingManager.playScript();
		// Assert
		assertThat(dynamicScriptingManager.currentlyExecutingAction(), is(mockScriptGeneratorActionOptional));
	}

}
