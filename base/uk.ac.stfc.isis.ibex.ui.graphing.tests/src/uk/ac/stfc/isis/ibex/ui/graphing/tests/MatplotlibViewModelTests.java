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

package uk.ac.stfc.isis.ibex.ui.graphing.tests;

import static org.junit.Assert.assertEquals;

import java.beans.PropertyChangeListener;
import java.io.ByteArrayInputStream;
import java.util.Optional;
import java.util.concurrent.ScheduledExecutorService;

import org.eclipse.swt.graphics.ImageData;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import uk.ac.stfc.isis.ibex.ui.graphing.websocketview.MatplotlibButtonState;
import uk.ac.stfc.isis.ibex.ui.graphing.websocketview.MatplotlibButtonType;
import uk.ac.stfc.isis.ibex.ui.graphing.websocketview.MatplotlibCursorPosition;
import uk.ac.stfc.isis.ibex.ui.graphing.websocketview.MatplotlibFigureViewModel;
import uk.ac.stfc.isis.ibex.ui.graphing.websocketview.MatplotlibNavigationType;
import uk.ac.stfc.isis.ibex.ui.graphing.websocketview.MatplotlibPressType;
import uk.ac.stfc.isis.ibex.ui.graphing.websocketview.MatplotlibWebsocketModel;

/**
 * Unit tests for the DeviceScreensDescriptionViewModel class.
 */
@RunWith(MockitoJUnitRunner.class)
public class MatplotlibViewModelTests {
	
	private MatplotlibWebsocketModel model;
	private MatplotlibFigureViewModel viewModel;
	private ScheduledExecutorService workerThread;
	
	@Captor private ArgumentCaptor<Runnable> runnableCaptor;

    @Before
    public void setUp() {
    	model = Mockito.mock(MatplotlibWebsocketModel.class);
    	workerThread = Mockito.mock(ScheduledExecutorService.class);
    	viewModel = new MatplotlibFigureViewModel("test", 1, model, workerThread);
    }

    @Test
    public void WHEN_server_connected_THEN_plotname_reflects_model() {
    	Mockito.when(model.getPlotName()).thenReturn("test_plot_name");
    	Mockito.when(model.isConnected()).thenReturn(true);
    	viewModel.onConnectionStatus(true);
    	assertEquals(viewModel.getPlotName().getValue(), "test_plot_name");
    }

    @Test
    public void WHEN_server_disconnected_THEN_plotname_prefixed_with_disconnected() {
    	Mockito.when(model.getPlotName()).thenReturn("test_plot_name");
    	Mockito.when(model.isConnected()).thenReturn(false);
    	viewModel.onConnectionStatus(false);
    	assertEquals(viewModel.getPlotName().getValue(), "[Disconnected] test_plot_name");
    }
    
    @Test
    public void WHEN_server_connected_and_pan_set_THEN_button_states_are_updated() {
    	Mockito.when(model.getNavMode()).thenReturn(MatplotlibNavigationType.PAN);
    	Mockito.when(model.isConnected()).thenReturn(true);
    	viewModel.updateNavMode();
    	assertEquals(viewModel.getPanButtonState().getValue(), MatplotlibButtonState.ENABLED_ACTIVE);
    	assertEquals(viewModel.getZoomButtonState().getValue(), MatplotlibButtonState.ENABLED_INACTIVE);
    }
    
    @Test
    public void WHEN_server_connected_and_zoom_set_THEN_button_states_are_updated() {
    	Mockito.when(model.getNavMode()).thenReturn(MatplotlibNavigationType.ZOOM);
    	Mockito.when(model.isConnected()).thenReturn(true);
    	viewModel.updateNavMode();
    	assertEquals(viewModel.getPanButtonState().getValue(), MatplotlibButtonState.ENABLED_INACTIVE);
    	assertEquals(viewModel.getZoomButtonState().getValue(), MatplotlibButtonState.ENABLED_ACTIVE);
    }

    @Test
    public void WHEN_server_connected_and_no_zoom_or_pan_set_THEN_button_states_are_updated() {
    	Mockito.when(model.getNavMode()).thenReturn(MatplotlibNavigationType.NONE);
    	Mockito.when(model.isConnected()).thenReturn(true);
    	viewModel.updateNavMode();
    	assertEquals(viewModel.getPanButtonState().getValue(), MatplotlibButtonState.ENABLED_INACTIVE);
    	assertEquals(viewModel.getZoomButtonState().getValue(), MatplotlibButtonState.ENABLED_INACTIVE);
    }
    
    @Test
    public void WHEN_server_disconnected_THEN_nav_button_states_are_updated() {
    	Mockito.when(model.isConnected()).thenReturn(false);
    	viewModel.updateNavMode();
    	assertEquals(viewModel.getPanButtonState().getValue(), MatplotlibButtonState.DISABLED);
    	assertEquals(viewModel.getZoomButtonState().getValue(), MatplotlibButtonState.DISABLED);
    }
    
    @Test
    public void WHEN_server_connected_and_back_state_set_THEN_button_state_is_correct() {
    	Mockito.when(model.getBackState()).thenReturn(true);
    	Mockito.when(model.isConnected()).thenReturn(true);
    	viewModel.updateBackState();
    	assertEquals(viewModel.getBackButtonState().getValue(), MatplotlibButtonState.ENABLED_INACTIVE);
    }
    
    @Test
    public void WHEN_server_disconnected_and_back_state_set_THEN_button_state_is_correct() {
    	Mockito.when(model.isConnected()).thenReturn(false);
    	viewModel.updateBackState();
    	assertEquals(viewModel.getBackButtonState().getValue(), MatplotlibButtonState.DISABLED);
    }
    
    @Test
    public void WHEN_server_connected_and_forward_state_set_THEN_button_state_is_correct() {
    	Mockito.when(model.getForwardState()).thenReturn(true);
    	Mockito.when(model.isConnected()).thenReturn(true);
    	viewModel.updateForwardState();
    	assertEquals(viewModel.getForwardButtonState().getValue(), MatplotlibButtonState.ENABLED_INACTIVE);
    }
    
    @Test
    public void WHEN_server_disconnected_and_forward_state_set_THEN_button_state_is_correct() {
    	Mockito.when(model.isConnected()).thenReturn(false);
    	viewModel.updateForwardState();
    	assertEquals(viewModel.getForwardButtonState().getValue(), MatplotlibButtonState.DISABLED);
    }
    
    @Test
    public void WHEN_canvas_resized_THEN_resize_request_sent_to_model() {
    	Mockito.verify(workerThread, Mockito.times(4)).scheduleWithFixedDelay(runnableCaptor.capture(), Mockito.anyLong(), Mockito.anyLong(), Mockito.any());
    	viewModel.canvasResized(11, 22);
    	
    	// index 0 = redraw, 1 = canvas size, 2 = cursor position, 3 = force refresh
    	Runnable updateCanvasSizeIfRequired = runnableCaptor.getAllValues().get(1);
    	
    	// index 0 = redraw, 1 = canvas size, 2 = cursor position, 3 = force refresh
    	updateCanvasSizeIfRequired.run();
    	Mockito.verify(model, Mockito.times(1)).canvasResized(11, 22);
    	
    	// If the runnable runs again but the canvas hasn't been resized again, an extra
    	// request should not be sent to the model
    	updateCanvasSizeIfRequired.run();
    	Mockito.verify(model, Mockito.times(1)).canvasResized(11, 22);
    	
    	// If the runnable runs again and the canvas has been resized, but to the same values as before,
    	// then an extra request should not be sent to the model
    	viewModel.canvasResized(11, 22);
    	updateCanvasSizeIfRequired.run();
    	Mockito.verify(model, Mockito.times(1)).canvasResized(11, 22);
    }

    @Test
    public void viewmodel_periodically_forces_plot_refresh() {
    	Mockito.verify(workerThread, Mockito.times(4)).scheduleWithFixedDelay(runnableCaptor.capture(), Mockito.anyLong(), Mockito.anyLong(), Mockito.any());
    	
    	// index 0 = redraw, 1 = canvas size, 2 = cursor position, 3 = force refresh
    	runnableCaptor.getAllValues().get(3).run();
    	Mockito.verify(model, Mockito.times(1)).forceServerRefresh();
    }
    
    @Test
    public void WHEN_redraw_required_THEN_image_updated() {
    	Mockito.verify(workerThread, Mockito.times(4)).scheduleWithFixedDelay(runnableCaptor.capture(), Mockito.anyLong(), Mockito.anyLong(), Mockito.any());
    	
    	Mockito.when(model.getImageData()).thenReturn(
    			Optional.of(new ImageData(new ByteArrayInputStream(MatplotlibModelTests.FAKE_IMAGE_BYTES))));
    	
    	var mockListener = Mockito.mock(PropertyChangeListener.class);
    	viewModel.getImage().addPropertyChangeListener(mockListener);
    	viewModel.imageUpdated();
    	
    	// index 0 = redraw, 1 = canvas size, 2 = cursor position, 3 = force refresh
    	runnableCaptor.getAllValues().get(0).run();
    	Mockito.verify(model, Mockito.times(1)).getImageData();
    	// Assert that our listener gets a property change event for the new image.
    	Mockito.verify(mockListener, Mockito.times(1)).propertyChange(Mockito.any());
    }
    
    @Test
    public void WHEN_cursor_position_change_required_THEN_cursor_change_sent_to_model() {
    	Mockito.verify(workerThread, Mockito.times(4)).scheduleWithFixedDelay(runnableCaptor.capture(), Mockito.anyLong(), Mockito.anyLong(), Mockito.any());
    	
    	var newCursorPos = new MatplotlibCursorPosition(1, 2, true);
    	viewModel.setCursorPosition(newCursorPos);
    	
    	// index 0 = redraw, 1 = canvas size, 2 = cursor position, 3 = force refresh
    	runnableCaptor.getAllValues().get(2).run();
    	Mockito.verify(model, Mockito.times(1)).cursorPositionChanged(newCursorPos);
    }
    
    @Test
    public void WHEN_mouse_event_occurs_THEN_mouse_event_sent_to_model() {
    	Mockito.verify(workerThread, Mockito.times(4)).scheduleWithFixedDelay(runnableCaptor.capture(), Mockito.anyLong(), Mockito.anyLong(), Mockito.any());
    	
    	var cursorPos = new MatplotlibCursorPosition(1, 2, true);
    	var newPressType = MatplotlibPressType.BUTTON_PRESS;
    	viewModel.notifyButtonPressed(cursorPos, newPressType);
    	
    	Mockito.verify(model, Mockito.times(1)).notifyButtonPress(cursorPos, newPressType);
    }
    
    @Test
    public void WHEN_toolbar_nav_button_pressed_THEN_button_pressed_sent_to_model() {
    	Mockito.verify(workerThread, Mockito.times(4)).scheduleWithFixedDelay(runnableCaptor.capture(), Mockito.anyLong(), Mockito.anyLong(), Mockito.any());
    	
    	var navType = MatplotlibButtonType.HOME;
    	viewModel.navigatePlot(navType);
    	
    	Mockito.verify(model, Mockito.times(1)).navigatePlot(navType);
    }
    
    @Test
    public void WHEN_server_disconnected_THEN_all_nav_buttons_disabled() {
    	Mockito.when(model.isConnected()).thenReturn(false);
    	viewModel.onConnectionStatus(false);
    	
    	assertEquals(viewModel.getHomeButtonState().getValue(), MatplotlibButtonState.DISABLED);
    	assertEquals(viewModel.getBackButtonState().getValue(), MatplotlibButtonState.DISABLED);
    	assertEquals(viewModel.getForwardButtonState().getValue(), MatplotlibButtonState.DISABLED);
    	assertEquals(viewModel.getPanButtonState().getValue(), MatplotlibButtonState.DISABLED);
    	assertEquals(viewModel.getZoomButtonState().getValue(), MatplotlibButtonState.DISABLED);
    }
    
}
