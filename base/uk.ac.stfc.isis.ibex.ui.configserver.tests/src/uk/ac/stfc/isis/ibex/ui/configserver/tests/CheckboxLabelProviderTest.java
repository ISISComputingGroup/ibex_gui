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


import java.util.LinkedList;

import org.eclipse.core.databinding.observable.map.IObservableMap;
import org.eclipse.core.databinding.observable.map.ObservableMap;
import org.eclipse.jface.viewers.TableViewer;
import org.junit.Before;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import uk.ac.stfc.isis.ibex.ui.configserver.CheckboxLabelProvider;
import uk.ac.stfc.isis.ibex.ui.tables.DataboundTable;

/**
 *
 */
public class CheckboxLabelProviderTest {
    
    private CheckboxLabelProvider<String> labelProvider;
    
    private LinkedList<String> modelList;
    
    @Mock
    private DataboundTable<String> table;
    
    //private HashMap<K, V>
    
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        IObservableMap mockProperties = Mockito.mock(ObservableMap.class);
        
        modelList.add("block");
        modelList.add("ioc");
        modelList.add("macro");
        
        Mockito.when(table.viewer()).thenReturn(Mockito.mock(TableViewer.class));
        Mockito.when(table.viewer().getInput()).thenReturn(modelList);
        
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
    }
}
