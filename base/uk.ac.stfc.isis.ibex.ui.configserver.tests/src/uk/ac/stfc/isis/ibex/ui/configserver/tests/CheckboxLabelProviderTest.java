 /*
 * This file is part of the ISIS IBEX application.
 * Copyright (C) 2012-2016 Science & Technology Facilities Council.
 * All rights reserved.
 *
 * This program is distributed in the hope that it will be useful.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution.
 * EXCEPT AS EXPRESSLY SET FORTH IN THE ECLIPSE PUBLIC LICENSE V1.0, THE PROGRAM 
 * AND ACCOMPANYING MATERIALS ARE PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES 
 * OR CONDITIONS OF ANY KIND.  See the Eclipse Public License v1.0 for more details.
 *
 * You should have received a copy of the Eclipse Public License v1.0
 * along with this program; if not, you can obtain a copy from
 * https://www.eclipse.org/org/documents/epl-v10.php or 
 * http://opensource.org/licenses/eclipse-1.0.php
 */

/**
 * 
 */
package uk.ac.stfc.isis.ibex.ui.configserver.tests;


import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.util.EventListener;
import java.util.List;
import java.util.stream.Stream;

import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.core.databinding.observable.map.WritableMap;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import uk.ac.stfc.isis.ibex.ui.widgets.CheckboxLabelProvider;
import uk.ac.stfc.isis.ibex.ui.widgets.CheckboxLabelProvider.CheckboxSelectionAdapter;

/**
 *
 */
public class CheckboxLabelProviderTest {
	
	public static class mockSelectionListener implements Listener {
		private EventListener eventListener;
		@Override
		public void handleEvent(Event event) {
			return;
			
		}    
		
		public mockSelectionListener (EventListener listener) {
			this.eventListener = listener;
		}
		
		public EventListener getEventListener() {
			return this.eventListener;
		}

	}
    private CheckboxLabelProvider<String> modifiedCheckboxLabelProvider;
    
    private final String[] testModels = {"block", "ioc", "synoptic"};
    
    private WritableMap<String, String> tableContents;
    
    private Button checkBox;
    
    private Realm mockRealm;
    
    @Before
    public void prepareForTest() {
    	mockRealm = mock(Realm.class);
        when(mockRealm.isCurrent()).thenReturn(true);
        
        tableContents = new WritableMap<>(mockRealm);

        checkBox = mock(Button.class);
        when(checkBox.getListeners(SWT.Selection)).thenReturn(new Listener[0]);
        
        modifiedCheckboxLabelProvider = new CheckboxLabelProvider<>(tableContents) {
            @Override
            protected boolean checked(String model) {
                return true;
            }

            @Override
            protected void setChecked(String model, boolean checked) {
                
            }

            @Override
            protected boolean isEditable(String model) {
                return true;
            }
            
            @Override
            protected Button getControl(ViewerCell cell, int style) {
                return checkBox;
            }
        };
    }
    
    private ViewerCell getMockedViewerCell(String element) {
        ViewerCell cell = mock(ViewerCell.class);
        when(cell.getElement()).thenReturn(element);
        return cell;
    }
    
    @Test
    public void GIVEN_empty_table_WHEN_adding_models_THEN_checkbox_listeners_added() {
        assertEquals(checkBox.getListeners(SWT.Selection).length, 0);
              
        tableContents.put(testModels[0], "a");
        tableContents.put(testModels[1], "a");
        tableContents.put(testModels[2], "a");
        
        //update the check box for the given model
        modifiedCheckboxLabelProvider.update(getMockedViewerCell(testModels[0]));
        modifiedCheckboxLabelProvider.update(getMockedViewerCell(testModels[1]));
        modifiedCheckboxLabelProvider.update(getMockedViewerCell(testModels[2]));
        
        ArgumentCaptor<SelectionListener> captor = ArgumentCaptor.forClass(SelectionListener.class);
        verify(checkBox, times(3)).addSelectionListener(captor.capture());
        
        List<SelectionListener> capturedListeners = captor.getAllValues();
        for(SelectionListener listener: capturedListeners) {
            assertEquals(listener.getClass(), CheckboxSelectionAdapter.class);
        }
    }
    
    @Test
    public void GIVEN_nonempty_table_WHEN_table_removes_element_then_is_sorted_THEN_no_listeners_added_for_removed_elements_checkbox() {
        assertEquals(checkBox.getListeners(SWT.Selection).length, 0);
        
        tableContents.put(testModels[0], "a");
        tableContents.put(testModels[1], "a");
        tableContents.put(testModels[2], "a");
        
        //update the check box for the given model
        modifiedCheckboxLabelProvider.update(getMockedViewerCell(testModels[0]));
        modifiedCheckboxLabelProvider.update(getMockedViewerCell(testModels[1]));
        modifiedCheckboxLabelProvider.update(getMockedViewerCell(testModels[2]));
        
        verify(checkBox, times(3)).addSelectionListener(any());
        
        tableContents.remove(testModels[2]);
        
        /*the reset method is called after sorting the table, so this simulates sorting.*/
        modifiedCheckboxLabelProvider.resetCheckBoxListenerUpdateFlags();
        modifiedCheckboxLabelProvider.update(getMockedViewerCell(testModels[0]));
        modifiedCheckboxLabelProvider.update(getMockedViewerCell(testModels[1]));
        modifiedCheckboxLabelProvider.update(getMockedViewerCell(testModels[2]));
        
        ArgumentCaptor<SelectionListener> captor = ArgumentCaptor.forClass(SelectionListener.class);
        // 5 calls as 3 from first adding of models but only 2 now one has been removed
        verify(checkBox, times(5)).addSelectionListener(captor.capture()); 
        
        List<SelectionListener> capturedListeners = captor.getAllValues();
        for(SelectionListener listener: capturedListeners) {
            assertEquals(listener.getClass(), CheckboxSelectionAdapter.class);
        }
    }
    
    @Test
    public void GIVEN_nonempty_table_WHEN_checkbox_already_updated_then_update_called_THEN_no_new_listeners_added() {
        assertEquals(checkBox.getListeners(SWT.Selection).length, 0);
        
        tableContents.put(testModels[0], "a");
        tableContents.put(testModels[1], "a");
        tableContents.put(testModels[2], "a");
        
        //update the check box for the given model
        modifiedCheckboxLabelProvider.update(getMockedViewerCell(testModels[0]));
        modifiedCheckboxLabelProvider.update(getMockedViewerCell(testModels[1]));
        modifiedCheckboxLabelProvider.update(getMockedViewerCell(testModels[2]));
        
        verify(checkBox, times(3)).addSelectionListener(any());
        
        modifiedCheckboxLabelProvider.update(getMockedViewerCell(testModels[0]));
        modifiedCheckboxLabelProvider.update(getMockedViewerCell(testModels[1]));
        modifiedCheckboxLabelProvider.update(getMockedViewerCell(testModels[2]));
        
        /*after the update method calls, the check box update flags are set to false,
         * so the subsequent update calls are not supposed to add any listeners*/
        verify(checkBox, times(3)).addSelectionListener(any());
    }
    
    @Test
    public void GIVEN_table_WHEN_checkboxes_updated_then_table_sorted_THEN_checkbox_listeners_readded() {
        assertEquals(checkBox.getListeners(SWT.Selection).length, 0);
        
        tableContents.put(testModels[0], "a");
        tableContents.put(testModels[1], "a");
        tableContents.put(testModels[2], "a");
        
        //update the check box for the given model
        modifiedCheckboxLabelProvider.update(getMockedViewerCell(testModels[0]));
        modifiedCheckboxLabelProvider.update(getMockedViewerCell(testModels[1]));
        modifiedCheckboxLabelProvider.update(getMockedViewerCell(testModels[2]));
        
        verify(checkBox, times(3)).addSelectionListener(any());
        
        /*the reset method is called after sorting the table, so this simulates sorting.
         * After sorting, the models correspond to other check boxes, the label provider
         * update flags need to be set to true so old listeners are removed.*/
        modifiedCheckboxLabelProvider.resetCheckBoxListenerUpdateFlags();
        modifiedCheckboxLabelProvider.update(getMockedViewerCell(testModels[0]));
        modifiedCheckboxLabelProvider.update(getMockedViewerCell(testModels[1]));
        modifiedCheckboxLabelProvider.update(getMockedViewerCell(testModels[2]));
        
        ArgumentCaptor<SelectionListener> captor = ArgumentCaptor.forClass(SelectionListener.class);
        verify(checkBox, times(6)).addSelectionListener(captor.capture());
        
        List<SelectionListener> capturedListeners = captor.getAllValues();
        for(SelectionListener listener: capturedListeners) {
            assertEquals(listener.getClass(), CheckboxSelectionAdapter.class);
        }
    }
    
    @Test
    public void GIVEN_checkbox_with_selection_adapters_WHEN_clear_checkbox_selection_listeners_THEN_selection_adapters_removed() {
        Button mockCheckBox = mock(Button.class);
        
        var checkBoxListeners = new CheckboxSelectionAdapter[2];
        checkBoxListeners[0] = modifiedCheckboxLabelProvider.new CheckboxSelectionAdapter(mockCheckBox, testModels[0]);
        checkBoxListeners[1] = modifiedCheckboxLabelProvider.new CheckboxSelectionAdapter(mockCheckBox, testModels[1]);
        
        when(mockCheckBox.getTypedListeners(SWT.Selection, CheckboxSelectionAdapter.class)).thenReturn(Stream.of(checkBoxListeners));
        
        CheckboxLabelProvider.clearCheckBoxSelectListeners(mockCheckBox);
        
        verify(mockCheckBox, times(1)).removeSelectionListener((SelectionListener) checkBoxListeners[0]);
        verify(mockCheckBox, times(1)).removeSelectionListener((SelectionListener) checkBoxListeners[1]);
    }
    
    @Test
    public void GIVEN_checkbox_with_non_selection_listeners_WHEN_clear_checkbox_selection_listeners_THEN_no_listener_removed() {
        Button mockCheckBox = mock(Button.class);
        
        Listener[] checkBoxListeners = new mockSelectionListener[2];
        checkBoxListeners[0] = new mockSelectionListener(mock(SelectionListener.class));
        checkBoxListeners[1] = new mockSelectionListener(mock(SelectionListener.class));
        
        when(mockCheckBox.getListeners(SWT.Selection)).thenReturn(checkBoxListeners);
        
        CheckboxLabelProvider.clearCheckBoxSelectListeners(mockCheckBox);
        
        verify(mockCheckBox, never()).removeSelectionListener(any());
    }
}
