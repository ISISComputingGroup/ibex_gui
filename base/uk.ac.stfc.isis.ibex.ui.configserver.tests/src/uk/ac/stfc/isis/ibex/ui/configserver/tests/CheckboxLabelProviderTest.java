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

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.core.databinding.observable.map.ObservableMap;
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
import uk.ac.stfc.isis.ibex.ui.tables.DataboundTable;

/**
 *
 */
public class CheckboxLabelProviderTest {
    
    private CheckboxLabelProvider<String> labelProvider;
    
    private Map<String, AtomicBoolean> unmodifiableMapView;
    
    private final String[] testModels = {"block", "ioc"};
    
    private HashMap<String, String> testContentsMap;
    
    private ObservableMap<String, String> testMap;
    
    @Mock
    private DataboundTable<String> table;
    
    @Mock
    private Realm mockRealm;
    
    public CheckboxLabelProviderTest() {
        MockitoAnnotations.initMocks(this);
        
        testContentsMap = new HashMap<>();
        testMap = new ObservableMap<>(mockRealm, testContentsMap);
        
        labelProvider = new CheckboxLabelProvider<String>(testMap, table) {
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
        
        unmodifiableMapView = labelProvider.getUnmodifiableUpdateFlagsMap();
    }
    
    @Before
    public void resetUpdateFlagMap() {
        mockRealm = mock(Realm.class);
        testContentsMap = new HashMap<>();
        testMap = new ObservableMap<>(mockRealm, testContentsMap);
    }
    
    @Test
    public void GIVEN_empty_update_flags_map_WHEN_adding_new_models_THEN_all_new_models_added() {
        for(String s: testModels) {
            assertEquals(false, unmodifiableMapView.containsKey(s));
        }
              
        testContentsMap.put(testModels[0], "a");
        testContentsMap.put(testModels[1], "a");
        
        for(String s: testModels) {
            assertEquals(true, unmodifiableMapView.containsKey(s));
            assertEquals(true, unmodifiableMapView.get(s).get());
        }
    }
    
    @Test
    public void GIVEN_update_flags_map_WHEN_table_now_empty_THEN_remove_old_map_entries() {
        testContentsMap.put(testModels[0], "a");
        testContentsMap.put(testModels[1], "a");
        
        for(String s: testModels) {
            assertEquals(true, unmodifiableMapView.containsKey(s));
        }
        
        testContentsMap.clear();
        
        assertEquals(false, unmodifiableMapView.containsKey(testModels[0]));
        assertEquals(false, unmodifiableMapView.containsKey(testModels[1]));
    }
    
    @Test
    public void GIVEN_update_flags_map_WHEN_reset_update_flags_THEN_all_flags_true() {
        testContentsMap.put(testModels[0], "a");
        testContentsMap.put(testModels[1], "a");
        
        for(String model: testModels) {
            unmodifiableMapView.get(model).set(false);
            assertEquals(false, unmodifiableMapView.get(model).get());
        }
        
        labelProvider.resetCheckBoxListenerUpdateFlags();
        
        for(String model: unmodifiableMapView.keySet()) {
            assertEquals(true, unmodifiableMapView.get(model).get());
        }
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
