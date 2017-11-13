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
import java.util.Arrays;
import java.util.Collections;

import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Widget;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import uk.ac.stfc.isis.ibex.devicescreens.components.ComponentType;
import uk.ac.stfc.isis.ibex.opis.desc.MacroInfo;
import uk.ac.stfc.isis.ibex.opis.desc.OpiDescription;
import uk.ac.stfc.isis.ibex.synoptic.model.desc.ComponentDescription;
import uk.ac.stfc.isis.ibex.synoptic.model.desc.Property;
import uk.ac.stfc.isis.ibex.synoptic.model.desc.TargetDescription;
import uk.ac.stfc.isis.ibex.ui.synoptic.editor.model.SynopticViewModel;
import uk.ac.stfc.isis.ibex.ui.synoptic.editor.target.selector.TargetSelectorViewModel;

/**
 * Tests for the target selector view model.
 */
public class TargetSelectorViewModelTests {
    
    @Captor 
    ArgumentCaptor<PropertyChangeListener> propertyChangeCaptor;
    
    private Event source;
    private ComponentDescription chopperComponentDesc;
    private OpiDescription chopperOpiDesc;
    private SynopticViewModel synopticModel;
    private OpiDescription eurothermOpiDesc;
    private OpiDescription noneOpiDesc;
    private ComponentDescription noneComponentDesc;
    private static final String MK3_CHOPPER = "Mk3 Chopper";
    private static final String NONE = "NONE";
    private static final String EUROTHERM = "Eurotherm";
    
    private OpiDescription opiDescriptionMock(ComponentType type, String desc){
        OpiDescription mock = Mockito.mock(OpiDescription.class);
        Mockito.when(mock.getType()).thenReturn(type.name());
        Mockito.when(mock.getDescription()).thenReturn(desc);
        Mockito.when(mock.getMacros()).thenReturn(Collections.<MacroInfo>emptyList());
        return mock;
    }
    
    private ComponentDescription componentDescriptionMock(TargetDescription target, ComponentType type) {
        ComponentDescription mock = Mockito.mock(ComponentDescription.class);
        Mockito.when(mock.target()).thenReturn(target);
        Mockito.when(mock.type()).thenReturn(ComponentType.CHOPPER);
        Mockito.doNothing().when(mock).setName(Mockito.anyString());
        return mock;
    }
    
    private TargetDescription targetDescriptionMock(String name) {
        TargetDescription mock = Mockito.mock(TargetDescription.class);
        Mockito.when(mock.name()).thenReturn(name);
        Mockito.when(mock.getProperties()).thenReturn(Collections.<Property>emptyList());
        return mock;
    }

    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);
        
        source = new Event();
        source.widget = Mockito.mock(Widget.class);
        
        chopperComponentDesc = componentDescriptionMock(targetDescriptionMock(MK3_CHOPPER), ComponentType.CHOPPER);
        noneComponentDesc = componentDescriptionMock(targetDescriptionMock(NONE), ComponentType.UNKNOWN);
        
        chopperOpiDesc = opiDescriptionMock(ComponentType.CHOPPER, MK3_CHOPPER + " description");
        eurothermOpiDesc = opiDescriptionMock(ComponentType.EUROTHERM, EUROTHERM + " description");
        noneOpiDesc = opiDescriptionMock(ComponentType.UNKNOWN, "");
        
        synopticModel = Mockito.mock(SynopticViewModel.class);
        
        Mockito.doNothing().when(synopticModel).addPropertyChangeListener(anyString(), propertyChangeCaptor.capture());
        Mockito.when(synopticModel.getOpi(MK3_CHOPPER)).thenReturn(chopperOpiDesc);
        Mockito.when(synopticModel.getOpi(EUROTHERM)).thenReturn(eurothermOpiDesc);
        Mockito.when(synopticModel.getOpi(NONE)).thenReturn(noneOpiDesc);
    }
    
    @Test
    public void GIVEN_single_selected_component_is_not_null_WHEN_asking_if_panel_should_be_enabled_THEN_true() {
        // Arrange
        Mockito.when(synopticModel.getSingleSelectedComp()).thenReturn(chopperComponentDesc);
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
    
    @Test
    public void GIVEN_a_selected_component_with_a_default_icon_WHEN_opi_is_set_THEN_icon_is_replaced() {
        // Arrange
        Mockito.when(synopticModel.getSingleSelectedComp()).thenReturn(chopperComponentDesc);
        TargetSelectorViewModel model = new TargetSelectorViewModel(synopticModel);
        
        // Act
        model.setOpi(EUROTHERM);
        
        // Assert
        assertEquals(model.getIconSelectionIndex(), Arrays.asList(model.componentTypesArray).indexOf(ComponentType.EUROTHERM.name()));
    }
    
    @Test
    public void GIVEN_a_components_name_is_set_WHEN_retrieving_the_name_THEN_it_is_set_correctly() {
        // Arrange
        Mockito.when(synopticModel.getSingleSelectedComp()).thenReturn(chopperComponentDesc);
        TargetSelectorViewModel model = new TargetSelectorViewModel(synopticModel);
        
        // Act
        final String name = "My name 123";
        model.setName(name);
        
        // Assert
        assertEquals(name, model.getName());
    }
    
    @Test
    public void GIVEN_a_component_icon_is_set_WHEN_retrieving_the_icon_THEN_it_is_set_to_the_icon_that_was_just_passed_in() {
        // Arrange
        Mockito.when(synopticModel.getSingleSelectedComp()).thenReturn(chopperComponentDesc);
        TargetSelectorViewModel model = new TargetSelectorViewModel(synopticModel);
        
        // Act
        final int iconSelectionIndex = 15;
        model.setIconSelectionIndex(iconSelectionIndex);
        
        // Assert
        assertEquals(iconSelectionIndex, model.getIconSelectionIndex());
    }
    
    @Test
    public void GIVEN_an_opi_is_set_WHEN_retrieving_opi_THEN_opi_is_set_to_the_opi_that_was_just_passed_in() {
        // Arrange
        Mockito.when(synopticModel.getSingleSelectedComp()).thenReturn(noneComponentDesc);
        TargetSelectorViewModel model = new TargetSelectorViewModel(synopticModel);
        assertEquals(model.getOpi(), NONE);
        
        // Act
        model.setOpi(MK3_CHOPPER);
        
        // Assert
        assertEquals(model.getOpi(), MK3_CHOPPER);
    }
    
    @Test
    public void GIVEN_a_component_which_was_unknown_WHEN_opi_is_set_THEN_icon_is_also_updated() {
        // Arrange
        Mockito.when(synopticModel.getSingleSelectedComp()).thenReturn(noneComponentDesc);
        TargetSelectorViewModel model = new TargetSelectorViewModel(synopticModel);
        assertEquals(model.getIconSelectionIndex(), Arrays.asList(model.componentTypesArray).indexOf(ComponentType.UNKNOWN.name()));
        
        // Act
        model.setOpi(MK3_CHOPPER);
        
        // Assert
        assertEquals(model.getIconSelectionIndex(), Arrays.asList(model.componentTypesArray).indexOf(ComponentType.CHOPPER.name()));
    }
    
    @Test
    public void GIVEN_an_opi_is_set_WHEN_getting_desctiption_THEN_description_is_correct() {
        // Arrange
        Mockito.when(synopticModel.getSingleSelectedComp()).thenReturn(chopperComponentDesc);
        TargetSelectorViewModel model = new TargetSelectorViewModel(synopticModel);
        propertyChangeCaptor.getValue().propertyChange(new PropertyChangeEvent(source, "selectedComponents", true, false));
        assertEquals(model.getDescription(), MK3_CHOPPER + " description");
        
        // Act
        model.setOpi(EUROTHERM);
        
        // Assert
        assertEquals(model.getDescription(), EUROTHERM + " description");
    }

}
