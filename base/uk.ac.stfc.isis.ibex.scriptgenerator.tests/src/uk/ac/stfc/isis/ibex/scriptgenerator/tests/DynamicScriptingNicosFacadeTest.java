package uk.ac.stfc.isis.ibex.scriptgenerator.tests;

import static org.junit.Assert.assertThrows;

import org.junit.Before;
import org.junit.Test;
import org.junit.Test.None;

import uk.ac.stfc.isis.ibex.nicos.ScriptStatus;
import uk.ac.stfc.isis.ibex.scriptgenerator.dynamicscripting.DynamicScriptingException;
import uk.ac.stfc.isis.ibex.scriptgenerator.dynamicscripting.DynamicScriptingNicosFacade;
import uk.ac.stfc.isis.ibex.scriptgenerator.dynamicscripting.DynamicScriptingProperties;
import uk.ac.stfc.isis.ibex.scriptgenerator.tests.utils.ScriptGeneratorMockBuilder;
import uk.ac.stfc.isis.ibex.scriptgenerator.tests.utils.StatusSwitchCounter;

public class DynamicScriptingNicosFacadeTest {
	
	private ScriptGeneratorMockBuilder scriptGeneratorMockBuilder;
	private StatusSwitchCounter statusSwitchCounter;
	private DynamicScriptingNicosFacade nicosFacade;
	
	@Before
	public void setUp() {
		// Set up mocks
		scriptGeneratorMockBuilder = new ScriptGeneratorMockBuilder();
		statusSwitchCounter = new StatusSwitchCounter();
		// Set up class under test
		nicosFacade = new DynamicScriptingNicosFacade(scriptGeneratorMockBuilder.getMockNicosModel());
		nicosFacade.addPropertyChangeListener(DynamicScriptingProperties.SCRIPT_STATUS_PROPERTY, statusSwitchCounter);
	}
	
	@Test(expected = None.class)
	public void test_WHEN_no_error_THEN_script_running() {
		try {
			nicosFacade.executeAction("test", "test");
			statusSwitchCounter.assertNumberOfSwitches(ScriptStatus.IDLE, ScriptStatus.RUNNING, 1);
			statusSwitchCounter.assertNumberOfSwitches(ScriptStatus.IDLE, ScriptStatus.IDLEEXC, 0);
		} catch(DynamicScriptingException e) {
			throw new AssertionError("Should correctly run");
		}
	}
	
	@Test
	public void test_WHEN_nicos_error_THEN_exception_AND_property_change() {
		// Arrange
		scriptGeneratorMockBuilder.arrangeNicosError();
		// Act
		assertThrows(DynamicScriptingException.class, () -> {
			try {
				nicosFacade.executeAction("test", "test");
			} catch (DynamicScriptingException e) {
				throw e;
			} finally {
				// Assert
				statusSwitchCounter.assertNumberOfSwitches(ScriptStatus.IDLE, ScriptStatus.RUNNING, 0);
				statusSwitchCounter.assertNumberOfSwitches(ScriptStatus.IDLE, ScriptStatus.IDLEEXC, 1);
			}
	    });	
	}
	
	@Test
	public void test_WHEN_nicos_cannot_send_script_THEN_exception_AND_property_change() {
		// Arrange
		scriptGeneratorMockBuilder.arrangeNicosSendScriptFail();
		// Act
		assertThrows(DynamicScriptingException.class, () -> {
			try {
				nicosFacade.executeAction("test", "test");
			} catch (DynamicScriptingException e) {
				throw e;
			} finally {
				// Assert
				statusSwitchCounter.assertNumberOfSwitches(ScriptStatus.IDLE, ScriptStatus.RUNNING, 0);
				statusSwitchCounter.assertNumberOfSwitches(ScriptStatus.IDLE, ScriptStatus.IDLEEXC, 1);
			}
	    });
	}

}
