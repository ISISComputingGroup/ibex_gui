package uk.ac.stfc.isis.ibex.scriptgenerator.tests;

import static org.junit.Assert.assertThrows;

import static org.mockito.Mockito.when;

import java.beans.PropertyChangeEvent;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;

import uk.ac.stfc.isis.ibex.nicos.NicosModel;
import uk.ac.stfc.isis.ibex.nicos.ScriptStatus;
import uk.ac.stfc.isis.ibex.scriptgenerator.dynamicscripting.DynamicScriptName;
import uk.ac.stfc.isis.ibex.scriptgenerator.dynamicscripting.DynamicScriptingException;
import uk.ac.stfc.isis.ibex.scriptgenerator.dynamicscripting.DynamicScriptingNicosAdapter;
import uk.ac.stfc.isis.ibex.scriptgenerator.dynamicscripting.DynamicScriptingProperties;
import uk.ac.stfc.isis.ibex.scriptgenerator.tests.utils.ScriptGeneratorMockBuilder;
import uk.ac.stfc.isis.ibex.scriptgenerator.tests.utils.StatusSwitchCounter;

public class DynamicScriptingNicosAdapterTest {
	
	private ScriptGeneratorMockBuilder scriptGeneratorMockBuilder;
	private NicosModel nicosModel;
	private DynamicScriptingNicosAdapter nicosAdapter;
	private StatusSwitchCounter<DynamicScriptName, Optional<String>> statusSwitchCounter;
	
	@Before
	public void setUp() {
		statusSwitchCounter = new StatusSwitchCounter<>();
		scriptGeneratorMockBuilder = new ScriptGeneratorMockBuilder();
		nicosModel = scriptGeneratorMockBuilder.getMockNicosModel();
		nicosAdapter = new DynamicScriptingNicosAdapter(nicosModel);
		nicosAdapter.addPropertyChangeListener(DynamicScriptingProperties.SCRIPT_CHANGED_PROPERTY, statusSwitchCounter);
	}
	
	@Test
	public void test_WHEN_no_error_THEN_no_exception() {
		try {
			nicosAdapter.executeAction("test", "test");
		} catch(DynamicScriptingException e) {
			throw new AssertionError("Should correctly run");
		}
	}
	
	@Test
	public void test_WHEN_nicos_error_THEN_exception() {
		scriptGeneratorMockBuilder.arrangeNicosError();
		assertThrows(DynamicScriptingException.class, () -> {
			nicosAdapter.executeAction("test", "test");
	    });	
	}
	
	@Test
	public void test_WHEN_nicos_cannot_send_script_THEN_exception_AND_property_change() {
		scriptGeneratorMockBuilder.arrangeNicosSendScriptFail();
		assertThrows(DynamicScriptingException.class, () -> {
			nicosAdapter.executeAction("test", "test");
	    });
	}
	
	private void doScriptChange(String newScriptName) {
		doScriptChange(newScriptName, DynamicScriptingProperties.SCRIPT_STATUS_PROPERTY);
	}
	
	private void doScriptChange(String newScriptName, String propertyName) {
		when(nicosModel.getScriptName()).thenReturn(newScriptName);
		nicosAdapter.propertyChange( 
			new PropertyChangeEvent(
				nicosModel, propertyName,
				ScriptStatus.RUNNING, ScriptStatus.IDLE
			)
		);
	}
	
	@Test
	public void test_WHEN_script_changes_THEN_change_notified() {
		Optional<String> oldScriptName = Optional.empty();
		String scriptName = "Script Generator: 0";
		Optional<String> newScriptName = Optional.of(scriptName);
		doScriptChange(scriptName);
		statusSwitchCounter.assertNumberOfSwitches(oldScriptName, newScriptName, 1);
	}
	
	@Test
	public void test_WHEN_script_still_null_THEN_change_not_notified() {
		String scriptName = null;
		doScriptChange(scriptName);
		statusSwitchCounter.assertNoSwitches();
	}
	
	@Test
	public void test_WHEN_script_changes_twice_THEN_change_notified() {
		Optional<String> oldScriptName = Optional.empty();
		String scriptName = "Script Generator: 0";
		Optional<String> newScriptName = Optional.of(scriptName);
		doScriptChange(scriptName);
		String scriptName1 = "Script Generator: 1";
		Optional<String> newerScriptName = Optional.of(scriptName1);
		doScriptChange(scriptName1);
		statusSwitchCounter.assertNumberOfSwitches(oldScriptName, newScriptName, 1);
		statusSwitchCounter.assertNumberOfSwitches(newScriptName, newerScriptName, 1);
	}
	
	@Test
	public void test_WHEN_script_changes_twice_are_the_same_THEN_change_not_notified_twice() {
		Optional<String> oldScriptName = Optional.empty();
		String scriptName = "Script Generator: 0";
		Optional<String> newScriptName = Optional.of(scriptName);
		doScriptChange(scriptName);
		doScriptChange(scriptName);
		statusSwitchCounter.assertNumberOfSwitches(oldScriptName, newScriptName, 1);
	}
	
	@Test
	public void test_WHEN_script_changes_back_to_null_THEN_change_notified() {
		Optional<String> oldScriptName = Optional.empty();
		String scriptName = "Script Generator: 0";
		Optional<String> newScriptName = Optional.of(scriptName);
		doScriptChange(scriptName);
		doScriptChange(null);
		statusSwitchCounter.assertNumberOfSwitches(oldScriptName, newScriptName, 1);
		statusSwitchCounter.assertNumberOfSwitches(newScriptName, oldScriptName, 1);
	}
	
	@Test
	public void test_WHEN_non_script_change_property_fired_THEN_change_not_notified() {
		String scriptName = "Script Generator: 0";
		doScriptChange(scriptName, "test");
		statusSwitchCounter.assertNoSwitches();
	}


}
