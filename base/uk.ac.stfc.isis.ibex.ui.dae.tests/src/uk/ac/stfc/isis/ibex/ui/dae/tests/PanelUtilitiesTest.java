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

import uk.ac.stfc.isis.ibex.ui.dae.DaeViewModel;
import uk.ac.stfc.isis.ibex.ui.dae.experimentsetup.PanelUtilities;

public class PanelUtilitiesTest {
    
    private Display DISPLAY;
    private DaeViewModel viewModel;
    private PanelUtilities utils;
    private Color yellow;
    private Color white;
    private Color unchanged;
    
    
    
    @Before
    public void setUp() {
        // Arrange
        DISPLAY = mock(Display.class);
        viewModel = mock(DaeViewModel.class);
        yellow = new Color(DISPLAY, 255, 255, 0);
        white = new Color(DISPLAY, 255, 255, 255);
        unchanged = new Color(DISPLAY, 240, 240, 240);
        when(DISPLAY.getSystemColor(SWT.COLOR_YELLOW)).thenReturn(yellow);
        when(DISPLAY.getSystemColor(SWT.COLOR_WHITE)).thenReturn(white);
        utils = new PanelUtilities(viewModel, DISPLAY);
    }
    
    @Test
    public void GIVEN_true_WHEN_get_colour_THEN_return_colour_yellow() {
        Color expected = yellow;
        Color actual = utils.getColour(true);
        assertEquals(expected, actual);
    }
    
    @Test
    public void GIVEN_false_WHEN_get_colour_THEN_return_colour_denoting_unchanged_widgets() {
        Color expected = unchanged;
        Color actual = utils.getColour(false);
        assertEquals(expected, actual);
    }
    
    @Test
    public void WHEN_get_white_THEN_return_colour_white() {
        Color expected = white;
        Color actual = utils.getWhite();
        assertEquals(expected, actual);
    }
    
    @Test
    public void GIVEN_chante_WHEN_set_is_changed_to_dae_viewModel_THEN_get_is_changed_is_true() {
        Mockito.when(viewModel.getIsChanged()).thenCallRealMethod();
        Mockito.doCallRealMethod().when(viewModel).setIsChanged(Mockito.any(boolean.class));
        boolean expected = true;
        utils.setIsChanged(true);
        boolean actual = viewModel.getIsChanged();
        assertEquals(expected, actual);
    }
    
    @Test
    public void GIVEN_not_null_cell_and_its_not_empty_value_that_has_changed_WHEN_try_to_change_colour_THEN_colour_changed() {
        /*Here we won't actually be looking at the colour but at the setIsChanged function in the dae viewModel as that is called when 
         * the colour is changed. Using Mockito.doCallRealMethod on either setBackground or getBackground then calling these functions 
         * causes an InvocationTargetException error.
         */
        utils.setRecordedCellValue("123");
        
        ViewerCell mockCell = mock(ViewerCell.class, Mockito.RETURNS_DEEP_STUBS);
        Mockito.doNothing().when(mockCell).setBackground(Mockito.any(Color.class));
        when(mockCell.getText()).thenReturn("not 123");
        
        Mockito.when(viewModel.getIsChanged()).thenCallRealMethod();
        Mockito.doCallRealMethod().when(viewModel).setIsChanged(Mockito.any(boolean.class));
        viewModel.setIsChanged(false);
        
        utils.setRecordedCell(mockCell);
        utils.tryToChangeBackgroundOfCell();
        
        boolean expected = true;
        boolean actual = viewModel.getIsChanged();
        assertEquals(expected, actual);
    }
    
    @Test
    public void GIVEN_null_cell_and_its_not_empty_value_that_has_changed_WHEN_try_to_change_colour_THEN_colour_not_changed() {
        /*Here we won't actually be looking at the colour but at the setIsChanged function in the dae viewModel as that is called when 
         * the colour is changed. Using Mockito.doCallRealMethod on either setBackground or getBackground then calling these functions 
         * causes an InvocationTargetException error.
         */
        utils.setRecordedCellValue("123");
        
        ViewerCell mockCell = null;
        
        Mockito.when(viewModel.getIsChanged()).thenCallRealMethod();
        Mockito.doCallRealMethod().when(viewModel).setIsChanged(Mockito.any(boolean.class));
        viewModel.setIsChanged(false);
        
        utils.setRecordedCell(mockCell);
        utils.tryToChangeBackgroundOfCell();
        
        boolean expected = false;
        boolean actual = viewModel.getIsChanged();
        assertEquals(expected, actual);
    }
    
    @Test
    public void GIVEN_not_null_cell_and_its_empty_value_that_has_changed_WHEN_try_to_change_colour_THEN_colour_not_changed() {
        /*Here we won't actually be looking at the colour but at the setIsChanged function in the dae viewModel as that is called when 
         * the colour is changed. Using Mockito.doCallRealMethod on either setBackground or getBackground then calling these functions 
         * causes an InvocationTargetException error.
         */
        utils.setRecordedCellValue("");
        
        ViewerCell mockCell = mock(ViewerCell.class, Mockito.RETURNS_DEEP_STUBS);
        Mockito.doNothing().when(mockCell).setBackground(Mockito.any(Color.class));
        when(mockCell.getText()).thenReturn("not  ");
        
        Mockito.when(viewModel.getIsChanged()).thenCallRealMethod();
        Mockito.doCallRealMethod().when(viewModel).setIsChanged(Mockito.any(boolean.class));
        viewModel.setIsChanged(false);
        
        utils.setRecordedCell(mockCell);
        utils.tryToChangeBackgroundOfCell();
        
        boolean expected = false;
        boolean actual = viewModel.getIsChanged();
        assertEquals(expected, actual);
    }
    
    @Test
    public void GIVEN_not_null_cell_and_its_not_empty_value_that_has_not_changed_WHEN_try_to_change_colour_THEN_colour_not_changed() {
        /*Here we won't actually be looking at the colour but at the setIsChanged function in the dae viewModel as that is called when 
         * the colour is changed. Using Mockito.doCallRealMethod on either setBackground or getBackground then calling these functions 
         * causes an InvocationTargetException error.
         */
        utils.setRecordedCellValue("123");
        
        ViewerCell mockCell = mock(ViewerCell.class, Mockito.RETURNS_DEEP_STUBS);
        Mockito.doNothing().when(mockCell).setBackground(Mockito.any(Color.class));
        when(mockCell.getText()).thenReturn("123");
        
        Mockito.when(viewModel.getIsChanged()).thenCallRealMethod();
        Mockito.doCallRealMethod().when(viewModel).setIsChanged(Mockito.any(boolean.class));
        viewModel.setIsChanged(false);
        
        utils.setRecordedCell(mockCell);
        utils.tryToChangeBackgroundOfCell();
        
        boolean expected = false;
        boolean actual = viewModel.getIsChanged();
        assertEquals(expected, actual);
    }
    
}
