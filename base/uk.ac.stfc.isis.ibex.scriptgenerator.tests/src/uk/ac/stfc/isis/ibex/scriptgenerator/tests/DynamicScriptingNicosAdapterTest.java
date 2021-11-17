package uk.ac.stfc.isis.ibex.scriptgenerator.tests;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThrows;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.reset;

import org.mockito.ArgumentCaptor;

import java.beans.PropertyChangeEvent;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;

import uk.ac.stfc.isis.ibex.nicos.ExecutionInstructionType;
import uk.ac.stfc.isis.ibex.nicos.NicosModel;
import uk.ac.stfc.isis.ibex.nicos.ScriptStatus;
import uk.ac.stfc.isis.ibex.nicos.messages.ExecutionInstruction;
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
	
	@Before
	public void setUp() {
		dynamicScriptSwitchCounter = new StatusSwitchCounter<>();
		scriptGeneratorMockBuilder = new ScriptGeneratorMockBuilder();
		nicosModel = scriptGeneratorMockBuilder.getMockNicosModel();
		nicosAdapter = new DynamicScriptingNicosAdapter(nicosModel);
		nicosAdapter.addPropertyChangeListener(DynamicScriptingProperties.SCRIPT_FINISHED_PROPERTY, dynamicScriptSwitchCounter);
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
	
	private void assertInstructionTypeSent(ExecutionInstructionType type, Integer numberOfInstructions) {
		ArgumentCaptor<ExecutionInstruction> captor = ArgumentCaptor.forClass(ExecutionInstruction.class);
		verify(nicosModel, times(numberOfInstructions)).sendExecutionInstruction(captor.capture());
		ExecutionInstruction executedInstruction = captor.getValue();
		assertThat(executedInstruction.toString(), is(type.getCommand()));
		reset(nicosModel);
	}
	
	@Test
	public void test_WHEN_pause_THEN_instruction_sent() {
		for (int i = 1; i < 5; i++) {
			nicosAdapter.pauseExecution();
			assertInstructionTypeSent(ExecutionInstructionType.BREAK, 1);
		}
	}
	
	@Test
	public void test_WHEN_resumed_from_THEN_instruction_sent() {
		for (int i = 1; i < 5; i++) {
			nicosAdapter.resumeExecution();
			assertInstructionTypeSent(ExecutionInstructionType.CONTINUE, 1);
		}
	}
	
	@Test
	public void test_WHEN_stop_THEN_instruction_sent() {
		for (int i = 1; i < 5; i++) {
			nicosAdapter.stopExecution();
			assertInstructionTypeSent(ExecutionInstructionType.STOP, 1);
		}
	}
	
	@Test
	public void test_WHEN_stop_AND_resume_AND_pause_THEN_instructions_sent() {
		nicosAdapter.stopExecution();
		assertInstructionTypeSent(ExecutionInstructionType.STOP, 1);
		nicosAdapter.resumeExecution();
		assertInstructionTypeSent(ExecutionInstructionType.CONTINUE, 1);
		nicosAdapter.pauseExecution();
		assertInstructionTypeSent(ExecutionInstructionType.BREAK, 1);
	}
	
	@Test
	public void test_WHEN_stop_AND_pause_AND_resume_THEN_instructions_sent() {
		nicosAdapter.stopExecution();
		assertInstructionTypeSent(ExecutionInstructionType.STOP, 1);
		nicosAdapter.pauseExecution();
		assertInstructionTypeSent(ExecutionInstructionType.BREAK, 1);
		nicosAdapter.resumeExecution();
		assertInstructionTypeSent(ExecutionInstructionType.CONTINUE, 1);
	}
	
	@Test
	public void test_WHEN_resume_AND_pause_AND_stop_THEN_instructions_sent() {
		nicosAdapter.resumeExecution();
		assertInstructionTypeSent(ExecutionInstructionType.CONTINUE, 1);
		nicosAdapter.pauseExecution();
		assertInstructionTypeSent(ExecutionInstructionType.BREAK, 1);
		nicosAdapter.stopExecution();
		assertInstructionTypeSent(ExecutionInstructionType.STOP, 1);
	}
	
	@Test
	public void test_WHEN_resume_AND_stop_AND_pause_THEN_instructions_sent() {
		nicosAdapter.resumeExecution();
		assertInstructionTypeSent(ExecutionInstructionType.CONTINUE, 1);
		nicosAdapter.stopExecution();
		assertInstructionTypeSent(ExecutionInstructionType.STOP, 1);
		nicosAdapter.pauseExecution();
		assertInstructionTypeSent(ExecutionInstructionType.BREAK, 1);
	}
	
	@Test
	public void test_WHEN_pause_AND_stop_AND_resume_THEN_instructions_sent() {
		nicosAdapter.pauseExecution();
		assertInstructionTypeSent(ExecutionInstructionType.BREAK, 1);
		nicosAdapter.stopExecution();
		assertInstructionTypeSent(ExecutionInstructionType.STOP, 1);
		nicosAdapter.resumeExecution();
		assertInstructionTypeSent(ExecutionInstructionType.CONTINUE, 1);
	}
	
	@Test
	public void test_WHEN_pause_AND_resume_AND_stop_THEN_instructions_sent() {
		nicosAdapter.pauseExecution();
		assertInstructionTypeSent(ExecutionInstructionType.BREAK, 1);
		nicosAdapter.resumeExecution();
		assertInstructionTypeSent(ExecutionInstructionType.CONTINUE, 1);
		nicosAdapter.stopExecution();
		assertInstructionTypeSent(ExecutionInstructionType.STOP, 1);
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
	public void test_WHEN_nicos_paused_THEN_no_property_changed() {
		doScriptChange(
			"test", DynamicScriptingProperties.SCRIPT_STATUS_PROPERTY, 
			ScriptStatus.RUNNING, ScriptStatus.INBREAK
		);
		dynamicScriptSwitchCounter.assertNoSwitches();
	}
	
	@Test
	public void test_WHEN_nicos_starts_running_THEN_no_property_change() {
		doScriptChange(
			"test", DynamicScriptingProperties.SCRIPT_STATUS_PROPERTY, 
			ScriptStatus.IDLE, ScriptStatus.RUNNING
		);
		dynamicScriptSwitchCounter.assertNoSwitches();
	}


}
