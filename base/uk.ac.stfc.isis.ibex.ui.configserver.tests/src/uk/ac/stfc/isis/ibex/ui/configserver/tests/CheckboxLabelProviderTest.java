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

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import org.eclipse.core.databinding.observable.map.IObservableMap;
import org.eclipse.core.databinding.observable.map.ObservableMap;
import org.eclipse.jface.viewers.TableViewer;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import uk.ac.stfc.isis.ibex.ui.configserver.CheckboxLabelProvider;
import uk.ac.stfc.isis.ibex.ui.tables.DataboundTable;

/**
 *
 */
public class CheckboxLabelProviderTest {
    
    private CheckboxLabelProvider<String> labelProvider;
    
    private final LinkedList<String> tableModelList = new LinkedList<>();
    
    private Map<String, AtomicBoolean> unmodifiableMapView;
    
    //rivate Set<String> modelTestSet = new HashSet<>();
    
    @Mock
    private DataboundTable<String> table;
    
    //private HashMap<K, V>
    
    public CheckboxLabelProviderTest() {
        MockitoAnnotations.initMocks(this);
        IObservableMap mockProperties = mock(ObservableMap.class);
        
        when(table.viewer()).thenReturn(mock(TableViewer.class));
        when(table.viewer().getInput()).thenReturn(tableModelList);
        
        labelProvider = new CheckboxLabelProvider<String>(mockProperties, table) {
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
    
    @SuppressWarnings("unchecked")
    @Before
    public void resetUpdateFlagMap() {
        tableModelList.clear();
        labelProvider.updateCheckboxListenerUpdateFlags();
    }
    
    @SuppressWarnings("unchecked")
    @Test
    public void GIVEN_empty_update_flags_map_WHEN_adding_new_models_THEN_all_new_models_added() {
        tableModelList.add("model");
        tableModelList.add("ioc");
        
        for(String s: tableModelList) {
            assertEquals(false, unmodifiableMapView.containsKey(s));
        }
        
        labelProvider.addNewModelsToUpdateFlagsMap(new HashSet<>((List<String>) table.viewer().getInput()));
        
        for(String s: tableModelList) {
            assertEquals(true, unmodifiableMapView.containsKey(s));
            assertEquals(true, unmodifiableMapView.get(s).get());
        }
    }
    
    @SuppressWarnings("unchecked")
    @Test
    public void GIVEN_complete_update_flags_map_WHEN_adding_models_THEN_map_unchanged() {
        tableModelList.add("block");
        tableModelList.add("ioc");
        
        labelProvider.addNewModelsToUpdateFlagsMap(new HashSet<>((List<String>) table.viewer().getInput()));
        
        for(String s: tableModelList) {
            assertEquals(true, unmodifiableMapView.containsKey(s));
        }
        
        int mapSize = unmodifiableMapView.size();
        
        labelProvider.addNewModelsToUpdateFlagsMap(new HashSet<>((List<String>) table.viewer().getInput()));
        
        for(String s: tableModelList) {
            assertEquals(true, unmodifiableMapView.containsKey(s));
        }
        assertEquals(mapSize, unmodifiableMapView.size());
    }
    
    @SuppressWarnings("unchecked")
    @Test
    public void GIVEN_update_flags_map_WHEN_table_now_empty_THEN_remove_old_map_entries() {
        tableModelList.add("block");
        tableModelList.add("ioc");
        
        labelProvider.addNewModelsToUpdateFlagsMap(new HashSet<>((List<String>) table.viewer().getInput()));
        
        for(String s: tableModelList) {
            assertEquals(true, unmodifiableMapView.containsKey(s));
        }
        
        tableModelList.clear();
        labelProvider.removeModelsNoLongerInTable(new HashSet<>((List<String>) table.viewer().getInput()));
        
        assertEquals(false, unmodifiableMapView.containsKey("block"));
        assertEquals(false, unmodifiableMapView.containsKey("ioc"));
    }
}
