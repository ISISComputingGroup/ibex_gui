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
package uk.ac.stfc.isis.ibex.ui.synoptic.editor.target.selector.tests;

import static org.junit.Assert.*;
import static org.mockito.Matchers.anyString;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Widget;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import uk.ac.stfc.isis.ibex.devicescreens.components.ComponentType;
import uk.ac.stfc.isis.ibex.opis.desc.OpiDescription;
import uk.ac.stfc.isis.ibex.synoptic.model.desc.ComponentDescription;
import uk.ac.stfc.isis.ibex.synoptic.model.desc.TargetDescription;
import uk.ac.stfc.isis.ibex.ui.synoptic.editor.model.SynopticViewModel;
import uk.ac.stfc.isis.ibex.ui.synoptic.editor.target.selector.TargetSelectorViewModel;

/**
 * Tests for the target selector view model.
 */
public class TargetSelectorViewModelTests {
    
    @Captor ArgumentCaptor<PropertyChangeListener> propertyChangeCaptor;
    
    private Event source;
    private ComponentDescription validComponentDesc;

    private OpiDescription validOpiDesc;

    private SynopticViewModel synopticModel;

    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);
        
        source = new Event();
        source.widget = Mockito.mock(Widget.class);
        
        TargetDescription validTarget = Mockito.mock(TargetDescription.class);
        Mockito.when(validTarget.name()).thenReturn("Target name");
        
        validComponentDesc = Mockito.mock(ComponentDescription.class);
        Mockito.when(validComponentDesc.target()).thenReturn(validTarget);
        Mockito.when(validComponentDesc.type()).thenReturn(ComponentType.CHOPPER);
        Mockito.doNothing().when(validComponentDesc).setName(Mockito.anyString());
        
        validOpiDesc = Mockito.mock(OpiDescription.class);
        Mockito.when(validOpiDesc.getDescription()).thenReturn("OPI description");
        
        synopticModel = Mockito.mock(SynopticViewModel.class);
        
        Mockito.doNothing().when(synopticModel).addPropertyChangeListener(anyString(), propertyChangeCaptor.capture());
        Mockito.when(synopticModel.getOpi(Mockito.anyString())).thenReturn(validOpiDesc);
    }
    
    @Test
    public void GIVEN_single_selected_component_is_not_null_WHEN_asking_if_panel_should_be_enabled_THEN_true() {
        // Arrange
        Mockito.when(synopticModel.getSingleSelectedComp()).thenReturn(validComponentDesc);
        TargetSelectorViewModel model = new TargetSelectorViewModel(synopticModel);
        
        // Act
        propertyChangeCaptor.getValue().propertyChange(new PropertyChangeEvent(source, "selectedComponents", true, false));
        
        // Assert
        assertTrue(model.isEnabled());       
    }
    
    @Test
    public void GIVEN_single_selected_component_is_null_WHEN_asking_if_panel_should_be_enabled_THEN_false() {
        // Arrange
        Mockito.when(synopticModel.getSingleSelectedComp()).thenReturn(null);
        TargetSelectorViewModel model = new TargetSelectorViewModel(synopticModel);
        
        // Act
        propertyChangeCaptor.getValue().propertyChange(new PropertyChangeEvent(source, "selectedComponents", true, false));
        
        // Assert
        assertFalse(model.isEnabled());       
    }

}
