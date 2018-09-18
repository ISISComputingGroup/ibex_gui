package uk.ac.stfc.isis.ibex.ui.dae.tests;

import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;

import uk.ac.stfc.isis.ibex.ui.dae.experimentsetup.DaeExperimentSetupTableViewer;
import uk.ac.stfc.isis.ibex.ui.dae.experimentsetup.ExperimentSetup;
import uk.ac.stfc.isis.ibex.ui.dae.experimentsetup.ExperimentSetupViewModel;
import uk.ac.stfc.isis.ibex.ui.dae.experimentsetup.PanelViewModel;

public class PanelViewModelTest {
    
    private Display DISPLAY;
    private ExperimentSetupViewModel viewModel;
    private PanelViewModel panelViewModel;
    private Color yellow;
    private Color white;
    private Color unchanged;
    private ExperimentSetup experimentSetup;
    private DaeExperimentSetupTableViewer tableViewer;
    
    
    
    @Before
    public void setUp() {
        // Arrange
        DISPLAY = mock(Display.class);
        
        yellow = new Color(DISPLAY, 255, 255, 0);
        white = new Color(DISPLAY, 255, 255, 255);
        unchanged = new Color(DISPLAY, 240, 240, 240);
        
        when(DISPLAY.getSystemColor(SWT.COLOR_YELLOW)).thenReturn(yellow);
        when(DISPLAY.getSystemColor(SWT.COLOR_WHITE)).thenReturn(white);
        
        viewModel = mock(ExperimentSetupViewModel.class);
        
        experimentSetup = mock(ExperimentSetup.class);
        Mockito.doNothing().when(experimentSetup).setSendChangeBtnEnableState(Mockito.any(boolean.class));
        
        panelViewModel = new PanelViewModel(experimentSetup, DISPLAY, viewModel);
        
        tableViewer = mock(DaeExperimentSetupTableViewer.class);
        Mockito.doCallRealMethod().when(tableViewer).setRecordedCellValue(Mockito.any(String.class));
        Mockito.doCallRealMethod().when(tableViewer).setRecordedCell(Mockito.any(ViewerCell.class));
        Mockito.doCallRealMethod().when(tableViewer).tryToChangeBackgroundOfCell();
        Mockito.doCallRealMethod().when(tableViewer).setExperimentSetupViewModel(Mockito.any(ExperimentSetupViewModel.class));
        Mockito.doCallRealMethod().when(tableViewer).setName(Mockito.any(String.class));
        Mockito.doCallRealMethod().when(tableViewer).setPanelViewModel(Mockito.any(PanelViewModel.class));
        Mockito.doNothing().when(tableViewer).ifTableValuesDifferentFromCachedValuesThenChangeLabels();
        Mockito.doCallRealMethod().when(tableViewer).createInitialCachedValue();
        tableViewer.setExperimentSetupViewModel(viewModel);
        tableViewer.setPanelViewModel(panelViewModel);
    }
    
    @Test
    public void WHEN_get_changed_colour_THEN_return_colour_yellow() {
        //Given
        Color expected = yellow;
        
        //When
        Color actual = panelViewModel.getColour("changedColour");
        
        //Then
        assertEquals(expected, actual);
    }
    
    @Test
    public void WHEN_get_unchanged_colour_THEN_return_colour_denoting_unchanged_widgets() {
        //Given
        Color expected = unchanged;
        
        //When
        Color actual = panelViewModel.getColour("unchangedColour");
        
        //Then
        assertEquals(expected, actual);
    }
    
    @Test
    public void WHEN_get_white_THEN_return_colour_white() {
        //Given
        Color expected = white;
        
        //When
        Color actual = panelViewModel.getColour("white");
        
        //Then
        assertEquals(expected, actual);
    }
    
    @Test
    public void GIVEN_change_WHEN_set_change_to_dae_viewModel_THEN_get_change_contains_true() {
        //Given
        Mockito.when(viewModel.getIsChanged()).thenCallRealMethod();
        Mockito.doCallRealMethod().when(viewModel).setIsChanged(Mockito.any(boolean.class));
        boolean expected = true;
        
        //When
        panelViewModel.setIsChanged("name", true);
        boolean actual = viewModel.getIsChanged();
        
        //Then
        assertEquals(expected, actual);
    }
    
    /*In the next 5 tests we won't actually be looking at the colour but at the setIsChanged function in the dae viewModel as that is called when 
     * the colour is changed. Using Mockito.doCallRealMethod on either setBackground or getBackground then calling these functions 
     * causes an InvocationTargetException error.
     */
    
    @Test
    public void GIVEN_not_null_cell_and_its_not_empty_value_that_has_changed_WHEN_try_to_change_colour_THEN_colour_changed() {
        //Given
        ArrayList<String> cachedValues = new ArrayList<String>();
        cachedValues.add("was not 123");
        when(viewModel.getItemFromTableCachedValues("name")).thenReturn(cachedValues);
        
        
        ViewerCell mockCell = mock(ViewerCell.class, Mockito.RETURNS_DEEP_STUBS);
        Mockito.doNothing().when(mockCell).setBackground(Mockito.any(Color.class));
        when(mockCell.getText()).thenReturn("not 123");
        when(mockCell.getVisualIndex()).thenReturn(0);
        
        Mockito.when(viewModel.getIsChanged()).thenCallRealMethod();
        Mockito.doCallRealMethod().when(viewModel).setIsChanged(Mockito.any(boolean.class));
        viewModel.setIsChanged(false);
        boolean expected = true;
        
        //When
        tableViewer.setName("name");
        tableViewer.setRecordedCellValue("123");
        tableViewer.createInitialCachedValue();
        tableViewer.setRecordedRowIndexCoefficient(0);
        tableViewer.setRecordedCell(mockCell);
        tableViewer.tryToChangeBackgroundOfCell();
        
        boolean actual = viewModel.getIsChanged();
        
        //Then
        assertEquals(expected, actual);
    }
    
    @Test
    public void GIVEN_null_cell_and_its_not_empty_value_that_has_changed_WHEN_try_to_change_colour_THEN_colour_not_changed() {
        //Given
        ArrayList<String> cachedValues = new ArrayList<String>();
        cachedValues.add("was not 123");
        when(viewModel.getItemFromTableCachedValues("name")).thenReturn(cachedValues);
        
        tableViewer.setRecordedCellValue("123");
        
        ViewerCell mockCell = null;
        
        Mockito.when(viewModel.getIsChanged()).thenCallRealMethod();
        Mockito.doCallRealMethod().when(viewModel).setIsChanged(Mockito.any(boolean.class));
        viewModel.setIsChanged(false);
        boolean expected = false;
        
        //When
        tableViewer.setName("name");
        tableViewer.createInitialCachedValue();
        tableViewer.setRecordedRowIndexCoefficient(0);
        tableViewer.setRecordedCell(mockCell);
        tableViewer.tryToChangeBackgroundOfCell();
        boolean actual = viewModel.getIsChanged();
        
        //Then
        assertEquals(expected, actual);
    }
    
    @Test
    public void GIVEN_not_null_cell_and_its_empty_value_that_has_changed_WHEN_try_to_change_colour_THEN_colour_not_changed() {
        //Given
        ArrayList<String> cachedValues = new ArrayList<String>();
        cachedValues.add("was not 123");
        when(viewModel.getItemFromTableCachedValues("name")).thenReturn(cachedValues);
        
        tableViewer.setRecordedCellValue("");
        
        ViewerCell mockCell = mock(ViewerCell.class, Mockito.RETURNS_DEEP_STUBS);
        Mockito.doNothing().when(mockCell).setBackground(Mockito.any(Color.class));
        when(mockCell.getText()).thenReturn("not  ");
        when(mockCell.getVisualIndex()).thenReturn(0);
        
        Mockito.when(viewModel.getIsChanged()).thenCallRealMethod();
        Mockito.doCallRealMethod().when(viewModel).setIsChanged(Mockito.any(boolean.class));
        viewModel.setIsChanged(false);
        boolean expected = false;
        
        
        //When
        tableViewer.setName("name");
        tableViewer.createInitialCachedValue();
        tableViewer.setRecordedRowIndexCoefficient(0);
        tableViewer.setRecordedCell(mockCell);
        tableViewer.tryToChangeBackgroundOfCell();
        boolean actual = viewModel.getIsChanged();
        
        //Then
        assertEquals(expected, actual);
    }
    
    @Test
    public void GIVEN_not_null_cell_and_its_not_empty_value_that_has_not_changed_WHEN_try_to_change_colour_THEN_colour_not_changed() {
        //Given
        ArrayList<String> cachedValues = new ArrayList<String>();
        cachedValues.add("was not 123");
        when(viewModel.getItemFromTableCachedValues("name")).thenReturn(cachedValues);
        
        tableViewer.setRecordedCellValue("123");
        
        ViewerCell mockCell = mock(ViewerCell.class, Mockito.RETURNS_DEEP_STUBS);
        Mockito.doNothing().when(mockCell).setBackground(Mockito.any(Color.class));
        when(mockCell.getText()).thenReturn("123");
        when(mockCell.getVisualIndex()).thenReturn(0);
        
        Mockito.when(viewModel.getIsChanged()).thenCallRealMethod();
        Mockito.doCallRealMethod().when(viewModel).setIsChanged(Mockito.any(boolean.class));
        viewModel.setIsChanged(false);
        boolean expected = false;
        
        //When
        tableViewer.setName("name");
        tableViewer.createInitialCachedValue();
        tableViewer.setRecordedRowIndexCoefficient(0);
        tableViewer.setRecordedCell(mockCell);
        tableViewer.tryToChangeBackgroundOfCell();
        boolean actual = viewModel.getIsChanged();
        
        //Then
        assertEquals(expected, actual);
    }
    
    @Test
    public void GIVEN_not_null_cell_and_its_not_empty_value_that_has_changed_but_was_applied_WHEN_try_to_change_colour_THEN_colour_not_changed() {
        //Given
        ArrayList<String> cachedValues = new ArrayList<String>();
        cachedValues.add("123");
        when(viewModel.getItemFromTableCachedValues("name")).thenReturn(cachedValues);
        
        tableViewer.setRecordedCellValue("not 123");
        
        ViewerCell mockCell = mock(ViewerCell.class, Mockito.RETURNS_DEEP_STUBS);
        Mockito.doNothing().when(mockCell).setBackground(Mockito.any(Color.class));
        when(mockCell.getText()).thenReturn("123");
        when(mockCell.getVisualIndex()).thenReturn(0);
        
        Mockito.when(viewModel.getIsChanged()).thenCallRealMethod();
        Mockito.doCallRealMethod().when(viewModel).setIsChanged(Mockito.any(boolean.class));
        viewModel.setIsChanged(false);
        boolean expected = false;
        
        //When
        tableViewer.setName("name");
        tableViewer.createInitialCachedValue();
        tableViewer.setRecordedRowIndexCoefficient(0);
        tableViewer.setRecordedCell(mockCell);
        tableViewer.tryToChangeBackgroundOfCell();
        boolean actual = viewModel.getIsChanged();
        
        //Then
        assertEquals(expected, actual);
    }
    
}
