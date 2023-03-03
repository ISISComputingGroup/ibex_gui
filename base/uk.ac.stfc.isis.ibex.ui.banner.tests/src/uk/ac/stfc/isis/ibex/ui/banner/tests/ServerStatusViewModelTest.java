package uk.ac.stfc.isis.ibex.ui.banner.tests;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.junit.Before;
import org.junit.Test;

import uk.ac.stfc.isis.ibex.epics.observing.ForwardingObservable;
import uk.ac.stfc.isis.ibex.instrument.status.ServerStatusVariables;
import uk.ac.stfc.isis.ibex.ui.banner.models.ServerStatusViewModel;
import uk.ac.stfc.isis.ibex.ui.banner.models.ServerStatus;


public class ServerStatusViewModelTest {

    private ServerStatusViewModel viewModel;
    private ServerStatusVariables pvs;


    @SuppressWarnings("unchecked")
	@Before
    public void setUp() {
        pvs = mock(ServerStatusVariables.class);
        when(pvs.getRuncontrolPV()).thenReturn(mock(ForwardingObservable.class));
        when(pvs.getBlockServerPV()).thenReturn(mock(ForwardingObservable.class));
        when(pvs.getBlockGatewayPV()).thenReturn(mock(ForwardingObservable.class));
        when(pvs.getIsisDaePV()).thenReturn(mock(ForwardingObservable.class));
        when(pvs.getInstetcPV()).thenReturn(mock(ForwardingObservable.class));
        when(pvs.getDatabaseServerPV()).thenReturn(mock(ForwardingObservable.class));
        when(pvs.getProcservControlPV()).thenReturn(mock(ForwardingObservable.class));
        when(pvs.getAlarmServerPV()).thenReturn(mock(ForwardingObservable.class));
        viewModel = new ServerStatusViewModel(pvs);
    }
    
    private void setAll(ServerStatus statusToSet) {
    	viewModel.setAlarmServerStatus(statusToSet);
    	viewModel.setBlockGatewayStatus(statusToSet);
    	viewModel.setBlockServerStatus(statusToSet);
    	viewModel.setDbServerStatus(statusToSet);
    	viewModel.setInstetcStatus(statusToSet);
    	viewModel.setIsisDaeStatus(statusToSet);
    	viewModel.setPsControlStatus(statusToSet);
    	viewModel.setRunControlStatus(statusToSet);
    }

    @Test
    public void GIVEN_no_initial_value_WHEN_reading_overall_status_THEN_status_is_NOT_RUNNING() {
        // Arrange
        ServerStatus expected = ServerStatus.DOWN;

        // Act
        ServerStatus actual = viewModel.getOverallStatus();

        // Assert
        assertEquals(expected, actual);
    }

    @Test
    public void GIVEN_some_variables_off_some_unknown_WHEN_reading_overall_status_THEN_status_is_NOT_RUNNING() {
        // Arrange
        ServerStatus expected = ServerStatus.DOWN;
    	setAll(ServerStatus.DOWN);
    	viewModel.setAlarmServerStatus(ServerStatus.UNKNOWN);

        // Act
        ServerStatus actual = viewModel.getOverallStatus();

        // Assert
        assertEquals(expected, actual);
    }
    
    @Test
    public void GIVEN_all_variables_running_WHEN_reading_overall_status_THEN_status_is_RUNNING() {
        // Arrange
    	setAll(ServerStatus.UP);
        ServerStatus expected = ServerStatus.UP;

        // Act
        ServerStatus actual = viewModel.getOverallStatus();

        // Assert
        assertEquals(expected, actual);
    }

    @Test
    public void GIVEN_some_variables_running_WHEN_reading_overall_status_THEN_status_is_PARTIAL() {
        // Arrange
    	viewModel.setBlockGatewayStatus(ServerStatus.UP);
    	viewModel.setBlockServerStatus(ServerStatus.UP);

        ServerStatus expected = ServerStatus.PARTIAL;

        // Act
        ServerStatus actual = viewModel.getOverallStatus();

        // Assert
        assertEquals(expected, actual);
    }
    
    @Test 
    public void GIVEN_individual_variables_WHEN_changing_and_reading_individual_status_THEN_individual_status_is_correct() {
    	for (ServerStatus statusToSet: ServerStatus.values()) {
    		setAll(statusToSet);
	    	assertEquals(statusToSet, viewModel.getAlarmServerStatus());
	    	assertEquals(statusToSet, viewModel.getBlockGatewayStatus());
	    	assertEquals(statusToSet, viewModel.getBlockServerStatus());
	    	assertEquals(statusToSet, viewModel.getDbServerStatus());
	    	assertEquals(statusToSet, viewModel.getInstetcStatus());
	    	assertEquals(statusToSet, viewModel.getIsisDaeStatus());
	    	assertEquals(statusToSet, viewModel.getPsControlStatus());
	    	assertEquals(statusToSet, viewModel.getRunControlStatus());
    	}
    	
    	
    }
}
