package uk.ac.stfc.isis.ibex.ui.ioccontrol.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import uk.ac.stfc.isis.ibex.configserver.IocControl;
import uk.ac.stfc.isis.ibex.configserver.IocState;
import uk.ac.stfc.isis.ibex.model.SetCommand;
import uk.ac.stfc.isis.ibex.model.SettableUpdatedValue;
import uk.ac.stfc.isis.ibex.ui.ioccontrol.IocControlViewModel;

public class IocControlViewModelTest {
	
	@Mock
	private IocState ioc;
	@Mock
	private IocControl control;
	@Mock
	private SetCommand<Collection<String>> command;
	
	private SettableUpdatedValue<Collection<IocState>> iocsUpdated;
	private IocControlViewModel viewModel;
	
	
	@SuppressWarnings("unchecked")
	@Before
    public void setUp() {
		ioc = Mockito.mock(IocState.class);
		control = Mockito.mock(IocControl.class);
		command = (SetCommand<Collection<String>>) Mockito.mock(SetCommand.class);
		
		iocsUpdated = new SettableUpdatedValue<Collection<IocState>>();
		iocsUpdated.setValue(Arrays.asList(ioc));
		
		Mockito.when(control.iocs()).thenReturn(iocsUpdated).thenReturn(iocsUpdated);
		Mockito.when(control.startIoc()).thenReturn(command);
		Mockito.when(control.stopIoc()).thenReturn(command);
		Mockito.when(control.restartIoc()).thenReturn(command);
		Mockito.when(ioc.getDescription()).thenReturn("Motors");
		Mockito.when(ioc.getIsRunning()).thenReturn(true);
		Mockito.when(ioc.getInCurrentConfig()).thenReturn(true);
        viewModel = new IocControlViewModel(control);
    }
	
	@Test
	public void WHEN_no_ioc_THEN_all_commands_disabled() {
		viewModel.setIoc(null);
		
		assertFalse(viewModel.getStartEnabled());
		assertFalse(viewModel.getStopEnabled());
		assertFalse(viewModel.getRestartEnabled());
	}
	
	@Test
	public void GIVEN_ioc_running_WHEN_ioc_selected_THEN_commands_states_set_correctly() {
		Mockito.when(ioc.getIsRunning()).thenReturn(true);
		Mockito.when(command.getCanSend()).thenReturn(true).thenReturn(true).thenReturn(true);
		viewModel.setIoc(ioc);
		
		assertFalse(viewModel.getStartEnabled());
		assertTrue(viewModel.getStopEnabled());
		assertTrue(viewModel.getRestartEnabled());
	}
	
	@Test
	public void GIVEN_ioc_stopped_WHEN_ioc_selected_THEN_commands_states_set_correctly() {
		Mockito.when(ioc.getIsRunning()).thenReturn(false);
		Mockito.when(command.getCanSend()).thenReturn(true).thenReturn(true).thenReturn(true);
		viewModel.setIoc(ioc);
		
		assertTrue(viewModel.getStartEnabled());
		assertFalse(viewModel.getStopEnabled());
		assertFalse(viewModel.getRestartEnabled());
	}
	
	@Test
	public void WHEN_description_selected_THEN_correct_item_selected() {
		viewModel.setSelected("desc", null);
		var selected = viewModel.getSelected();
		assertEquals(selected.description().get(), "desc");
		assertTrue(selected.ioc().isEmpty());
	}
	
	@Test
	public void WHEN_ioc_selected_THEN_correct_item_selected() {
		viewModel.setSelected("desc", "ioc");
		var selected = viewModel.getSelected();
		assertEquals(selected.description().get(), "desc");
		assertEquals(selected.ioc().get(), "ioc");
	}
	
	@Test
	public void WHEN_description_on_top_THEN_correct_item_on_top() {
		viewModel.setTop("desc", null);
		var selected = viewModel.getTop();
		assertEquals(selected.description().get(), "desc");
		assertTrue(selected.ioc().isEmpty());
	}
	
	@Test
	public void WHEN_ioc_on_top_THEN_correct_item_on_top() {
		viewModel.setTop("desc", "ioc");
		var selected = viewModel.getTop();
		assertEquals(selected.description().get(), "desc");
		assertEquals(selected.ioc().get(), "ioc");
	}
	
	@Test
	public void WHEN_descriptions_expanded_THEN_correct_items_expanded() {
		// "Running" and "In Config" are expanded initially.
		assertEquals(viewModel.getExpanded().size(), 2);
		viewModel.addExpanded("Motors");
		assertEquals(viewModel.getExpanded().size(), 3);
		viewModel.removeExpanded("Motors");
		assertEquals(viewModel.getExpanded().size(), 2);
	}
}
