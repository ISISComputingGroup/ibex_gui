package uk.ac.stfc.isis.ibex.ui.dae.tests;

import org.eclipse.core.databinding.DataBindingContext;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;

import uk.ac.stfc.isis.ibex.ui.dae.detectordiagnostics.DetectorDiagnosticsViewModel;

public class DetectorDiagnosticsViewModelTest {
    private DataBindingContext DATA_BIND;
    private DetectorDiagnosticsViewModel viewModel;
    
    @Before
    public void setUp() {
        DATA_BIND = mock(DataBindingContext.class);
        
        viewModel = new DetectorDiagnosticsViewModel(DATA_BIND);
    }
    
    @Test
    public void WHEN_update_spectra_count_called_before_update_count_rates_THEN_no_exception_thrown() {
    	viewModel.updateSpectraCount(0);
    }

}