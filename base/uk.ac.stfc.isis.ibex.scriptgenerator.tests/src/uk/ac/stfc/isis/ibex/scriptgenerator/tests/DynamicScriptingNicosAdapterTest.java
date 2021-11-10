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
	private StatusSwitchCounter<DynamicScriptName, Optional<String>> dynamicScriptSwitchCounter;
	private StatusSwitchCounter<ScriptStatus, ScriptStatus> statusSwitchCounter;
	
	@Before
	public void setUp() {
		dynamicScriptSwitchCounter = new StatusSwitchCounter<>();
		statusSwitchCounter = new StatusSwitchCounter<>();
		scriptGeneratorMockBuilder = new ScriptGeneratorMockBuilder();
		nicosModel = scriptGeneratorMockBuilder.getMockNicosModel();
		nicosAdapter = new DynamicScriptingNicosAdapter(nicosModel);
		nicosAdapter.addPropertyChangeListener(DynamicScriptingProperties.SCRIPT_CHANGED_PROPERTY, dynamicScriptSwitchCounter);
		nicosAdapter.addPropertyChangeListener(DynamicScriptingProperties.SCRIPT_PAUSED_PROPERTY, statusSwitchCounter);
		nicosAdapter.addPropertyChangeListener(DynamicScriptingProperties.SCRIPT_STOPPED_PROPERTY, statusSwitchCounter);
		nicosAdapter.addPropertyChangeListener(DynamicScriptingProperties.SCRIPT_RESUMED_PROPERTY, statusSwitchCounter);
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
		doScriptChange(newScriptName, propertyName, ScriptStatus.RUNNING, ScriptStatus.IDLE);
	}
	
	private void doScriptChange(String newScriptName, String propertyName, ScriptStatus oldStatus, ScriptStatus newStatus) {
		when(nicosModel.getScriptName()).thenReturn(newScriptName);
		nicosAdapter.propertyChange( 
			new PropertyChangeEvent(
				nicosModel, propertyName, oldStatus, newStatus
			)
		);
	}
	
	@Test
	public void test_WHEN_script_changes_THEN_change_notified() {
		Optional<String> oldScriptName = Optional.empty();
		String scriptName = "Script Generator: 0";
		Optional<String> newScriptName = Optional.of(scriptName);
		doScriptChange(scriptName);
		dynamicScriptSwitchCounter.assertNumberOfSwitches(oldScriptName, newScriptName, 1);
	}
	
	@Test
	public void test_WHEN_script_still_null_THEN_change_not_notified() {
		String scriptName = null;
		doScriptChange(scriptName);
		dynamicScriptSwitchCounter.assertNoSwitches();
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
		dynamicScriptSwitchCounter.assertNumberOfSwitches(oldScriptName, newScriptName, 1);
		dynamicScriptSwitchCounter.assertNumberOfSwitches(newScriptName, newerScriptName, 1);
	}
	
	@Test
	public void test_WHEN_script_changes_twice_are_the_same_THEN_change_not_notified_twice() {
		Optional<String> oldScriptName = Optional.empty();
		String scriptName = "Script Generator: 0";
		Optional<String> newScriptName = Optional.of(scriptName);
		doScriptChange(scriptName);
		doScriptChange(scriptName);
		dynamicScriptSwitchCounter.assertNumberOfSwitches(oldScriptName, newScriptName, 1);
	}
	
	@Test
	public void test_WHEN_script_changes_back_to_null_THEN_change_notified() {
		Optional<String> oldScriptName = Optional.empty();
		String scriptName = "Script Generator: 0";
		Optional<String> newScriptName = Optional.of(scriptName);
		doScriptChange(scriptName);
		doScriptChange(null);
		dynamicScriptSwitchCounter.assertNumberOfSwitches(oldScriptName, newScriptName, 1);
		dynamicScriptSwitchCounter.assertNumberOfSwitches(newScriptName, oldScriptName, 1);
	}
	
	@Test
	public void test_WHEN_non_script_change_property_fired_THEN_change_not_notified() {
		String scriptName = "Script Generator: 0";
		doScriptChange(scriptName, "test");
		dynamicScriptSwitchCounter.assertNoSwitches();
	}
	
	@Test
	public void test_WHEN_pause_AND_property_change_THEN_property_changed() {
		nicosAdapter.pauseExecution();
		doScriptChange(
			"test", DynamicScriptingProperties.SCRIPT_STATUS_PROPERTY, 
			ScriptStatus.RUNNING, ScriptStatus.INBREAK
		);
		statusSwitchCounter.assertNumberOfSwitches(ScriptStatus.RUNNING, ScriptStatus.INBREAK, 1);
	}
	
	@Test
	public void test_WHEN_pause_from_pause_AND_property_change_THEN_no_property_changed() {
		nicosAdapter.pauseExecution();
		doScriptChange(
			"test", DynamicScriptingProperties.SCRIPT_STATUS_PROPERTY, 
			ScriptStatus.RUNNING, ScriptStatus.INBREAK
		);
		nicosAdapter.pauseExecution();
		doScriptChange(
			"test", DynamicScriptingProperties.SCRIPT_STATUS_PROPERTY, 
			ScriptStatus.RUNNING, ScriptStatus.INBREAK
		);
		statusSwitchCounter.assertNumberOfSwitches(ScriptStatus.RUNNING, ScriptStatus.INBREAK, 1);
	}
	
	@Test
	public void test_WHEN_stop_AND_property_change_THEN_property_changed() {
		nicosAdapter.stopExecution();
		doScriptChange("test", DynamicScriptingProperties.SCRIPT_STATUS_PROPERTY);
		statusSwitchCounter.assertNumberOfSwitches(ScriptStatus.RUNNING, ScriptStatus.IDLE, 1);
	}
	
	@Test
	public void test_WHEN_pause_from_stopped_AND_property_change_THEN_no_property_changed() {
		nicosAdapter.stopExecution();
		doScriptChange("test", DynamicScriptingProperties.SCRIPT_STATUS_PROPERTY);
		statusSwitchCounter.assertNumberOfSwitches(ScriptStatus.RUNNING, ScriptStatus.IDLE, 1);
		nicosAdapter.pauseExecution();
		doScriptChange(
			"test", DynamicScriptingProperties.SCRIPT_STATUS_PROPERTY, 
			ScriptStatus.RUNNING, ScriptStatus.INBREAK
		);
		statusSwitchCounter.assertNumberOfSwitches(ScriptStatus.IDLE, ScriptStatus.INBREAK, 0);
	}
	
	@Test
	public void test_WHEN_resumed_from_paused_AND_property_change_THEN_property_changed() {
		nicosAdapter.pauseExecution();
		doScriptChange(
			"test", DynamicScriptingProperties.SCRIPT_STATUS_PROPERTY, 
			ScriptStatus.RUNNING, ScriptStatus.INBREAK
		);
		nicosAdapter.resumeExecution();
		doScriptChange(
			"test", DynamicScriptingProperties.SCRIPT_STATUS_PROPERTY, 
			ScriptStatus.INBREAK, ScriptStatus.RUNNING
		);
		statusSwitchCounter.assertNumberOfSwitches(ScriptStatus.INBREAK, ScriptStatus.RUNNING, 1);
	}
	
	@Test
	public void test_WHEN_resumed_from_stopped_AND_property_change_THEN_no_property_changed() {
		nicosAdapter.stopExecution();
		doScriptChange("test", DynamicScriptingProperties.SCRIPT_STATUS_PROPERTY);
		statusSwitchCounter.assertNumberOfSwitches(ScriptStatus.RUNNING, ScriptStatus.IDLE, 1);
		nicosAdapter.resumeExecution();
		doScriptChange(
			"test", DynamicScriptingProperties.SCRIPT_STATUS_PROPERTY, 
			ScriptStatus.IDLE, ScriptStatus.RUNNING
		);
		statusSwitchCounter.assertNumberOfSwitches(ScriptStatus.IDLE, ScriptStatus.RUNNING, 0);
	}


}
