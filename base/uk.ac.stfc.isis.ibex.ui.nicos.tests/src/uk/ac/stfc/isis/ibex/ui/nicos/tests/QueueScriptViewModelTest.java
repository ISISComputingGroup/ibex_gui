package uk.ac.stfc.isis.ibex.ui.nicos.tests;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;

import uk.ac.stfc.isis.ibex.nicos.NicosModel;
import uk.ac.stfc.isis.ibex.nicos.messages.scriptstatus.QueuedScript;
import uk.ac.stfc.isis.ibex.ui.nicos.models.QueueScriptViewModel;

import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

public class QueueScriptViewModelTest {
	
	private NicosModel model;
	
	private QueuedScript script1 = createMockedScript("ID1", "Script 1", "Code 1");
	private QueuedScript script2 = createMockedScript("ID2", "Script 2", "Code 2");
	private QueuedScript script3 = createMockedScript("ID3", "Script 3", "Code 3");
	
	@Captor
    ArgumentCaptor<List> setQueueCaptor = ArgumentCaptor.forClass(List.class);
	
	private QueuedScript createMockedScript(String id, String name, String code) {
		QueuedScript script = mock(QueuedScript.class);
		when(script.getCode()).thenReturn(code);
		when(script.getName()).thenReturn(name);
		script.reqid = id;
		return script;
	}

	@Before
	public void setUp() {
		model = mock(NicosModel.class);
		
		final List<QueuedScript> scripts = Arrays.asList(script1, script2, script3);
		
		when(model.getQueuedScripts()).thenReturn(scripts);
	}
	

	@Test
	public void test_GIVEN_selected_script_is_not_at_top_of_queue_WHEN_move_script_up_THEN_script_is_moved_up() {
		// Arrange
		QueueScriptViewModel vm = new QueueScriptViewModel(model);
		vm.setSelectedScript(script2);
		
		// Act
		vm.moveScript(true);
		
		// Assert
		verify(model).sendReorderedQueue(setQueueCaptor.capture());
        List<String> expected = Arrays.asList(script2.reqid, script1.reqid, script3.reqid);
        List<String> actual = setQueueCaptor.getValue();
        assertEquals(expected, actual);
	}
	
	@Test
	public void test_GIVEN_selected_script_is_not_at_bottom_of_queue_WHEN_move_script_down_THEN_script_is_moved_down() {
		// Arrange
		QueueScriptViewModel vm = new QueueScriptViewModel(model);
		vm.setSelectedScript(script2);
		
		// Act
		vm.moveScript(false);
		
		// Assert
		verify(model).sendReorderedQueue(setQueueCaptor.capture());
        List<String> expected = Arrays.asList(script1.reqid, script3.reqid, script2.reqid);
        List<String> actual = setQueueCaptor.getValue();
        assertEquals(expected, actual);
	}
	
	@Test
	public void test_GIVEN_selected_script_is_at_top_of_queue_WHEN_move_script_up_THEN_script_is_not_moved() {
		// Arrange
		QueueScriptViewModel vm = new QueueScriptViewModel(model);
		vm.setSelectedScript(script1);
		
		// Act
		vm.moveScript(true);
		
		// Assert
		verify(model, times(0)).sendReorderedQueue(Mockito.anyList());
	}
	
	@Test
	public void test_GIVEN_selected_script_is_at_bottom_of_queue_WHEN_move_script_down_THEN_script_is_not_moved() {
		// Arrange
		QueueScriptViewModel vm = new QueueScriptViewModel(model);
		vm.setSelectedScript(script3);
		
		// Act
		vm.moveScript(false);
		
		// Assert
		verify(model, times(0)).sendReorderedQueue(Mockito.anyList());
	}
	
	@Test
	public void test_GIVEN_top_script_in_queue_is_selected_THEN_up_button_not_enabled() {
		// Arrange
		QueueScriptViewModel vm = new QueueScriptViewModel(model);
		vm.setSelectedScript(script1);
		
		// Act / Assert
		assertFalse(vm.getUpButtonEnabled());
	}
	
	@Test
	public void test_GIVEN_bottom_script_in_queue_is_selected_THEN_down_button_not_enabled() {
		// Arrange
		QueueScriptViewModel vm = new QueueScriptViewModel(model);
		vm.setSelectedScript(script3);
		
		// Act / Assert
		assertFalse(vm.getDownButtonEnabled());
	}
	
	@Test
	public void test_GIVEN_script_in_queue_is_selected_that_is_not_at_the_top_THEN_up_button_enabled() {
		// Arrange
		QueueScriptViewModel vm = new QueueScriptViewModel(model);
		vm.setSelectedScript(script2);
		
		// Act / Assert
		assertTrue(vm.getUpButtonEnabled());
	}
	
	@Test
	public void test_GIVEN_script_in_queue_is_selected_that_is_not_at_the_bottom_THEN_down_button_enabled() {
		// Arrange
		QueueScriptViewModel vm = new QueueScriptViewModel(model);
		vm.setSelectedScript(script2);
		
		// Act / Assert
		assertTrue(vm.getDownButtonEnabled());
	}
	
	@Test
	public void test_GIVEN_no_script_selected_THEN_up_button_not_enabled() {
		// Arrange
		QueueScriptViewModel vm = new QueueScriptViewModel(model);
		vm.setSelectedScript(null);
		
		// Act / Assert
		assertFalse(vm.getUpButtonEnabled());
	}
	
	@Test
	public void test_GIVEN_no_script_selected_THEN_down_button_not_enabled() {
		// Arrange
		QueueScriptViewModel vm = new QueueScriptViewModel(model);
		vm.setSelectedScript(null);
		
		// Act / Assert
		assertFalse(vm.getDownButtonEnabled());
	}
	
	@Test
	public void test_GIVEN_no_script_selected_THEN_dequeue_button_not_enabled() {
		// Arrange
		QueueScriptViewModel vm = new QueueScriptViewModel(model);
		vm.setSelectedScript(null);
		
		// Act / Assert
		assertFalse(vm.getDequeueButtonEnabled());
	}
	
	@Test
	public void test_GIVEN_script_is_selected_THEN_dequeue_button_is_enabled() {
		// Arrange
		QueueScriptViewModel vm = new QueueScriptViewModel(model);
		vm.setSelectedScript(script1);
		
		// Act / Assert
		assertTrue(vm.getDequeueButtonEnabled());
	}

}
