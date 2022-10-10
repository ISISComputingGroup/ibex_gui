//CHECKSTYLE:OFF

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

package uk.ac.stfc.isis.ibex.ui.experimentdetails.tests;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import uk.ac.stfc.isis.ibex.epics.observing.ClosableObservable;
import uk.ac.stfc.isis.ibex.epics.observing.ForwardingObservable;
import uk.ac.stfc.isis.ibex.epics.writing.Writable;
import uk.ac.stfc.isis.ibex.experimentdetails.ObservableExperimentDetailsModel;
import uk.ac.stfc.isis.ibex.ui.experimentdetails.ExperimentDetailsViewModel;

/**
 * Unit tests for the DeviceScreensDescriptionViewModel class.
 */
public class ExperimentDetailsViewModelTest {

    private ExperimentDetailsViewModel viewModel;
    private ObservableExperimentDetailsModel model;

    @SuppressWarnings("unchecked")
	@Before
    public void setUp() {
        model = mock(ObservableExperimentDetailsModel.class);
        
        when(model.rbNumber()).thenReturn(mock(ClosableObservable.class));
        when(model.rbNumberSetter()).thenReturn(mock(Writable.class));
        when(model.displayTitle()).thenReturn(mock(ForwardingObservable.class));
        when(model.displayTitleSetter()).thenReturn(mock(Writable.class));
        
        viewModel = new ExperimentDetailsViewModel(model);
    }

    @Test
    public void WHEN_view_model_constructed_THEN_listener_added_for_user_details() {
        verify(model).addPropertyChangeListener(eq("userDetails"), any());
    }
    
    @Test
    public void WHEN_user_details_changes_THEN_user_details_warning_visible_fired() {
        PropertyChangeListener mockListener = mock(PropertyChangeListener.class);
        viewModel.addPropertyChangeListener("userDetailsWarningVisible", mockListener);
        
        viewModel.setUserDetailsWarningVisible();
        
        verify(mockListener).propertyChange(any());
    }
    
    @Test
    public void GIVEN_user_details_empty_WHEN_user_details_changes_THEN_warning_visible_is_true() {
        when(model.isUserDetailsEmpty()).thenReturn(true);
        
        PropertyChangeListener mockListener = mock(PropertyChangeListener.class);
        viewModel.addPropertyChangeListener("userDetailsWarningVisible", mockListener);
        
        viewModel.setUserDetailsWarningVisible();
        
        ArgumentCaptor<PropertyChangeEvent> captor = ArgumentCaptor.forClass(PropertyChangeEvent.class);        
        verify(mockListener).propertyChange(captor.capture());
        assertEquals(captor.getValue().getOldValue(), false);
        assertEquals(captor.getValue().getNewValue(), true);
    }
    
    @Test
    public void GIVEN_user_details_not_empty_WHEN_user_details_changes_THEN_warning_visible_is_false() {
        when(model.isUserDetailsEmpty()).thenReturn(false);
        
        PropertyChangeListener mockListener = mock(PropertyChangeListener.class);
        viewModel.addPropertyChangeListener("userDetailsWarningVisible", mockListener);
        
        viewModel.setUserDetailsWarningVisible();
        
        ArgumentCaptor<PropertyChangeEvent> captor = ArgumentCaptor.forClass(PropertyChangeEvent.class);        
        verify(mockListener).propertyChange(captor.capture());
        assertEquals(captor.getValue().getOldValue(), true);
        assertEquals(captor.getValue().getNewValue(), false);
    }
}
