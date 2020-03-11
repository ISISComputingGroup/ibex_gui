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
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.core.databinding.observable.map.WritableMap;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.TypedListener;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import uk.ac.stfc.isis.ibex.ui.configserver.CheckboxLabelProvider;
import uk.ac.stfc.isis.ibex.ui.configserver.CheckboxLabelProvider.CheckboxSelectionAdapter;

/**
 *
 */
public class CheckboxLabelProviderTest {
    
    private CheckboxLabelProvider<String> labelProvider;
    
    private CheckboxLabelProvider<String> mockCheckboxLabelProvider;
    
    private Map<String, AtomicBoolean> unmodifiableMapView;
    
    private final String[] testModels = {"block", "ioc", "synoptic"};
    
    private WritableMap<String, String> testMap;
    
    private Button checkBox;
    
    @Mock
    private Realm mockRealm;
    
    @Before
    public void prepareForTest() {
        MockitoAnnotations.initMocks(this);
        when(mockRealm.isCurrent()).thenReturn(true);
        
        testMap = new WritableMap<>(mockRealm);
        
        labelProvider = new CheckboxLabelProvider<String>(testMap) {
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
        };
        
        checkBox = mock(Button.class);
        
        mockCheckboxLabelProvider = new CheckboxLabelProvider<>(testMap) {
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
        
        unmodifiableMapView = labelProvider.getUnmodifiableUpdateFlagsMap();
    }
    
    private ViewerCell getMockedViewerCell(String element) {
        ViewerCell cell = mock(ViewerCell.class);
        when(cell.getElement()).thenReturn(element);
        return cell;
    }
    
    @Test
    public void GIVEN_empty_table_WHEN_adding_models_THEN_checkbox_listeners_added() {
        when(checkBox.getListeners(SWT.Selection)).thenReturn(new Listener[0]);
        
        assertEquals(checkBox.getListeners(SWT.Selection).length, 0);
              
        testMap.put(testModels[0], "a");
        testMap.put(testModels[1], "a");
        testMap.put(testModels[2], "a");
        
        mockCheckboxLabelProvider.update(getMockedViewerCell(testModels[0]));
        mockCheckboxLabelProvider.update(getMockedViewerCell(testModels[1]));
        mockCheckboxLabelProvider.update(getMockedViewerCell(testModels[2]));
        
        ArgumentCaptor<SelectionListener> captor = ArgumentCaptor.forClass(SelectionListener.class);
        verify(checkBox, times(3)).addSelectionListener(captor.capture());
        
        List<SelectionListener> capturedListeners = captor.getAllValues();
        for(SelectionListener listener: capturedListeners) {
            assertEquals(listener.getClass(), CheckboxSelectionAdapter.class);
        }
    }
    
    @Test
    public void GIVEN_nonempty_table_WHEN_table_removes_element_then_is_sorted_THEN_no_listeners_added_removed_elements_checkbox() {
        when(checkBox.getListeners(SWT.Selection)).thenReturn(new Listener[0]);
        assertEquals(checkBox.getListeners(SWT.Selection).length, 0);
        
        testMap.put(testModels[0], "a");
        testMap.put(testModels[1], "a");
        testMap.put(testModels[2], "a");
        
        mockCheckboxLabelProvider.update(getMockedViewerCell(testModels[0]));
        mockCheckboxLabelProvider.update(getMockedViewerCell(testModels[1]));
        mockCheckboxLabelProvider.update(getMockedViewerCell(testModels[2]));
        
        verify(checkBox, times(3)).addSelectionListener(any());
        
        testMap.remove(testModels[2]);
        
        mockCheckboxLabelProvider.resetCheckBoxListenerUpdateFlags();
        mockCheckboxLabelProvider.update(getMockedViewerCell(testModels[0]));
        mockCheckboxLabelProvider.update(getMockedViewerCell(testModels[1]));
        mockCheckboxLabelProvider.update(getMockedViewerCell(testModels[2]));
        
        verify(checkBox, times(5)).addSelectionListener(any());
    }
    
    @Test
    public void GIVEN_table_WHEN_checkboxes_updated_then_table_sorted_THEN_checkbox_listeners_readded() {
        when(checkBox.getListeners(SWT.Selection)).thenReturn(new Listener[0]);
        assertEquals(checkBox.getListeners(SWT.Selection).length, 0);
        
        testMap.put(testModels[0], "a");
        testMap.put(testModels[1], "a");
        testMap.put(testModels[2], "a");
        
        mockCheckboxLabelProvider.update(getMockedViewerCell(testModels[0]));
        mockCheckboxLabelProvider.update(getMockedViewerCell(testModels[1]));
        mockCheckboxLabelProvider.update(getMockedViewerCell(testModels[2]));
        
        verify(checkBox, times(3)).addSelectionListener(any());
        
        mockCheckboxLabelProvider.update(getMockedViewerCell(testModels[0]));
        mockCheckboxLabelProvider.update(getMockedViewerCell(testModels[1]));
        mockCheckboxLabelProvider.update(getMockedViewerCell(testModels[2]));
        
        verify(checkBox, times(3)).addSelectionListener(any());
        
        mockCheckboxLabelProvider.resetCheckBoxListenerUpdateFlags();
        mockCheckboxLabelProvider.update(getMockedViewerCell(testModels[0]));
        mockCheckboxLabelProvider.update(getMockedViewerCell(testModels[1]));
        mockCheckboxLabelProvider.update(getMockedViewerCell(testModels[2]));
        
        verify(checkBox, times(6)).addSelectionListener(any());
    }
    
    @Test
    public void GIVEN_checkbox_with_selection_adapters_WHEN_clear_checkbox_selection_listeners_THEN_selection_adapters_removed() {
        Button mockCheckBox = mock(Button.class);
        
        TypedListener[] checkBoxListeners = new TypedListener[2];
        checkBoxListeners[0] = new TypedListener(labelProvider.new CheckboxSelectionAdapter(mockCheckBox, testModels[0]));
        checkBoxListeners[1] = new TypedListener(labelProvider.new CheckboxSelectionAdapter(mockCheckBox, testModels[1]));
        
        when(mockCheckBox.getListeners(SWT.Selection)).thenReturn(checkBoxListeners);
        
        CheckboxLabelProvider.clearCheckBoxSelectListeners(mockCheckBox);
        
        verify(mockCheckBox, times(1)).removeSelectionListener((SelectionListener) checkBoxListeners[0].getEventListener());
        verify(mockCheckBox, times(1)).removeSelectionListener((SelectionListener) checkBoxListeners[1].getEventListener());
    }
    
    @Test
    public void GIVEN_checkbox_with_non_selection_listeners_WHEN_clear_checkbox_selection_listeners_THEN_no_listener_removed() {
        Button mockCheckBox = mock(Button.class);
        
        TypedListener[] checkBoxListeners = new TypedListener[2];
        checkBoxListeners[0] = new TypedListener(mock(SelectionListener.class));
        checkBoxListeners[1] = new TypedListener(mock(SelectionListener.class));
        
        when(mockCheckBox.getListeners(SWT.Selection)).thenReturn(checkBoxListeners);
        
        CheckboxLabelProvider.clearCheckBoxSelectListeners(mockCheckBox);
        
        verify(mockCheckBox, never()).removeSelectionListener(any());
    }
    
    @Test
    public void GIVEN_checkbox_and_model_WHEN_should_not_update_THEN_label_provider_listeners_not_changed() {
        Button mockCheckBox = mock(Button.class);
        
        labelProvider.resetCheckBoxListeners(false, mockCheckBox, testModels[0]);
        
        verify(mockCheckBox, never()).addSelectionListener(any());
    }
    
    @Test
    public void GIVEN_checkbox_and_model_WHEN_should_update_THEN_label_provider_listeners_added() {
        Button mockCheckBox = mock(Button.class);
        when(mockCheckBox.getListeners(SWT.Selection)).thenReturn(new Listener[0]);
        
        labelProvider.resetCheckBoxListeners(true, mockCheckBox, testModels[0]);
        
        ArgumentCaptor<SelectionListener> captor = ArgumentCaptor.forClass(SelectionListener.class);
        verify(mockCheckBox, times(1)).addSelectionListener(captor.capture());
        
        assertEquals(captor.getValue().getClass(), CheckboxSelectionAdapter.class);
    }
}
