package uk.ac.stfc.isis.ibex.scriptgenerator.tests;

import static org.junit.Assert.assertThrows;

import static org.mockito.Mockito.when;

import java.beans.PropertyChangeEvent;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;

import uk.ac.stfc.isis.ibex.nicos.NicosModel;
import uk.ac.stfc.isis.ibex.scriptgenerator.dynamicscripting.DynamicScriptName;
import uk.ac.stfc.isis.ibex.scriptgenerator.dynamicscripting.DynamicScriptingException;
import uk.ac.stfc.isis.ibex.scriptgenerator.dynamicscripting.DynamicScriptingNicosFacade;
import uk.ac.stfc.isis.ibex.scriptgenerator.dynamicscripting.DynamicScriptingProperties;
import uk.ac.stfc.isis.ibex.scriptgenerator.tests.utils.ScriptGeneratorMockBuilder;
import uk.ac.stfc.isis.ibex.scriptgenerator.tests.utils.StatusSwitchCounter;

public class DynamicScriptingNicosFacadeTest {
	
	private ScriptGeneratorMockBuilder scriptGeneratorMockBuilder;
	private NicosModel nicosModel;
	private DynamicScriptingNicosFacade nicosFacade;
	private StatusSwitchCounter<DynamicScriptName, Optional<String>> statusSwitchCounter;
	
	@Before
	public void setUp() {
		statusSwitchCounter = new StatusSwitchCounter<>();
		scriptGeneratorMockBuilder = new ScriptGeneratorMockBuilder();
		nicosModel = scriptGeneratorMockBuilder.getMockNicosModel();
		nicosFacade = new DynamicScriptingNicosFacade(nicosModel);
		nicosFacade.addPropertyChangeListener(DynamicScriptingProperties.SCRIPT_CHANGED_PROPERTY, statusSwitchCounter);
	}
	
	@Test
	public void test_WHEN_no_error_THEN_no_exception() {
		try {
			nicosFacade.executeAction("test", "test");
		} catch(DynamicScriptingException e) {
			throw new AssertionError("Should correctly run");
		}
	}
	
	@Test
	public void test_WHEN_nicos_error_THEN_exception() {
		scriptGeneratorMockBuilder.arrangeNicosError();
		assertThrows(DynamicScriptingException.class, () -> {
			nicosFacade.executeAction("test", "test");
	    });	
	}
	
	@Test
	public void test_WHEN_nicos_cannot_send_script_THEN_exception_AND_property_change() {
		scriptGeneratorMockBuilder.arrangeNicosSendScriptFail();
		assertThrows(DynamicScriptingException.class, () -> {
			nicosFacade.executeAction("test", "test");
	    });
	}
	
	private void doScriptChange(String oldScriptName, String newScriptName) {
		when(nicosModel.getScriptName()).thenReturn(newScriptName);
		nicosFacade.propertyChange( 
			new PropertyChangeEvent(
				nicosModel, DynamicScriptingProperties.SCRIPT_STATUS_PROPERTY,
				newScriptName, newScriptName
			)
		);
	}
	
	@Test
	public void test_WHEN_script_changes_THEN_change_notified() {
		Optional<String> oldScriptName = Optional.empty();
		String scriptName = "Script Generator: 0";
		Optional<String> newScriptName = Optional.of(scriptName);
		doScriptChange(null, scriptName);
		statusSwitchCounter.assertNumberOfSwitches(oldScriptName, newScriptName, 1);
	}
	
	@Test
	public void test_WHEN_script_still_null_THEN_change_not_notified() {
		String scriptName = null;
		doScriptChange(null, scriptName);
		statusSwitchCounter.assertNoSwitches();
	}
	
	@Test
	public void test_WHEN_script_changes_twice_THEN_change_notified() {
		Optional<String> oldScriptName = Optional.empty();
		String scriptName = "Script Generator: 0";
		Optional<String> newScriptName = Optional.of(scriptName);
		doScriptChange(null, scriptName);
		String scriptName1 = "Script Generator: 1";
		Optional<String> newerScriptName = Optional.of(scriptName1);
		doScriptChange(scriptName, scriptName1);
		statusSwitchCounter.assertNumberOfSwitches(oldScriptName, newScriptName, 1);
		statusSwitchCounter.assertNumberOfSwitches(newScriptName, newerScriptName, 1);
	}
	
	@Test
	public void test_WHEN_script_changes_twice_are_the_same_THEN_change_not_notified_twice() {
		Optional<String> oldScriptName = Optional.empty();
		String scriptName = "Script Generator: 0";
		Optional<String> newScriptName = Optional.of(scriptName);
		doScriptChange(null, scriptName);
		doScriptChange(scriptName, scriptName);
		statusSwitchCounter.assertNumberOfSwitches(oldScriptName, newScriptName, 1);
	}
	
	@Test
	public void test_WHEN_script_changes_back_to_null_THEN_change_notified() {
		Optional<String> oldScriptName = Optional.empty();
		String scriptName = "Script Generator: 0";
		Optional<String> newScriptName = Optional.of(scriptName);
		doScriptChange(null, scriptName);
		doScriptChange(scriptName, null);
		statusSwitchCounter.assertNumberOfSwitches(oldScriptName, newScriptName, 1);
		statusSwitchCounter.assertNumberOfSwitches(newScriptName, oldScriptName, 1);
	}


}
