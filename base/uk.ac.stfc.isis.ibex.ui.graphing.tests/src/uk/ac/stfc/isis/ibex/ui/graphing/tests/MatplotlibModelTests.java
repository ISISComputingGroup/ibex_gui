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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.concurrent.ScheduledExecutorService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import uk.ac.stfc.isis.ibex.ui.graphing.websocketview.MatplotlibButtonType;
import uk.ac.stfc.isis.ibex.ui.graphing.websocketview.MatplotlibCursorPosition;
import uk.ac.stfc.isis.ibex.ui.graphing.websocketview.MatplotlibFigureViewModel;
import uk.ac.stfc.isis.ibex.ui.graphing.websocketview.MatplotlibNavigationType;
import uk.ac.stfc.isis.ibex.ui.graphing.websocketview.MatplotlibPressType;
import uk.ac.stfc.isis.ibex.ui.graphing.websocketview.MatplotlibWebsocketEndpoint;
import uk.ac.stfc.isis.ibex.ui.graphing.websocketview.MatplotlibWebsocketModel;

/**
 * Unit tests for the DeviceScreensDescriptionViewModel class.
 */
@RunWith(MockitoJUnitRunner.class)
public class MatplotlibModelTests {
	
	private MatplotlibWebsocketModel model;
	private MatplotlibFigureViewModel viewModel;
	private MatplotlibWebsocketEndpoint websocket;
	private ScheduledExecutorService workerThread;
	/**
	 * This is a base-64 encoded 1x1 pixel pure white image. We need this to be a valid image to that
	 * the ImageData constructor does not throw errors.
	 */
	private static final String BASE64_1x1px_IMAGE = "iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAQAAAC1HAwCAAAAC0lEQVR42mP8/x8AAwMCAO+ip1sAAAAASUVORK5CYII=";
	public static final byte[] FAKE_IMAGE_BYTES = Base64.getDecoder().decode(BASE64_1x1px_IMAGE);
	
	@Captor private ArgumentCaptor<Runnable> runnableCaptor;

    @Before
    public void setUp() {
    	viewModel = Mockito.mock(MatplotlibFigureViewModel.class);
    	websocket = Mockito.mock(MatplotlibWebsocketEndpoint.class);
    	workerThread = Mockito.mock(ScheduledExecutorService.class);
    	model = new MatplotlibWebsocketModel(viewModel, "ws://test:65535", 1, websocket, workerThread);
    }

    @Test
    public void WHEN_model_constructed_THEN_can_get_url() {
    	Mockito.when(websocket.toString()).thenReturn("test_server_name");
        assertEquals(model.getServerName(), "test_server_name");
    }
    
    @Test
    public void WHEN_full_image_received_THEN_image_sent_to_viewmodel() {
    	model.setIsDiffImage(false);
    	model.setImageData(new ByteArrayInputStream(FAKE_IMAGE_BYTES));
    	Mockito.verify(viewModel, Mockito.times(1)).imageUpdated();
    }
    
    @Test
    public void WHEN_diff_image_received_THEN_image_not_sent_to_viewmodel() {
    	model.setIsDiffImage(true);
    	model.setImageData(new ByteArrayInputStream(FAKE_IMAGE_BYTES));
    	Mockito.verify(viewModel, Mockito.times(0)).imageUpdated();
    }
    
    @Test
    public void WHEN_image_received_THEN_image_present() {
    	model.setIsDiffImage(false);
    	model.setImageData(new ByteArrayInputStream(FAKE_IMAGE_BYTES));
    	assertEquals(model.getImageData().isPresent(), true);
    }
    
    @Test
    public void WHEN_server_connected_THEN_image_cleared() {
    	model.setIsDiffImage(false);
    	model.setImageData(new ByteArrayInputStream(FAKE_IMAGE_BYTES));
    	model.onConnectionClose();
    	assertEquals(model.getImageData().isPresent(), false);
    }
    
    @Test
    public void WHEN_refresh_requested_THEN_refresh_queued() {
    	model.forceServerRefresh();;
    	Mockito.verify(workerThread, Mockito.times(1)).submit(runnableCaptor.capture());
    	
    	runnableCaptor.getValue().run();
    	Mockito.verify(websocket, Mockito.times(1)).forceServerRefresh();
    }
    
    @Test
    public void WHEN_resize_requested_THEN_resize_queued() {
    	model.canvasResized(20, 50);
    	Mockito.verify(workerThread, Mockito.times(1)).submit(runnableCaptor.capture());
    	
    	runnableCaptor.getValue().run();
    	Mockito.verify(websocket, Mockito.times(1)).canvasResized(20, 50);
    	
    }
    
    @Test
    public void WHEN_cursor_position_changed_THEN_websocket_notified() {
    	var newCursorPosition = new MatplotlibCursorPosition(1, 2, true);
    	model.cursorPositionChanged(newCursorPosition);
    	Mockito.verify(workerThread, Mockito.times(1)).submit(runnableCaptor.capture());
    	
    	runnableCaptor.getValue().run();
    	Mockito.verify(websocket, Mockito.times(1)).cursorPositionChanged(newCursorPosition);
    	
    }

    @Test
    public void WHEN_button_click_event_THEN_websocket_notified() {
    	var newCursorPosition = new MatplotlibCursorPosition(1, 2, true);
    	var newPressType = MatplotlibPressType.BUTTON_PRESS;
    	
    	model.notifyButtonPress(newCursorPosition, newPressType);
    	Mockito.verify(workerThread, Mockito.times(1)).submit(runnableCaptor.capture());
    	
    	runnableCaptor.getValue().run();
    	Mockito.verify(websocket, Mockito.times(1)).notifyButtonPress(newCursorPosition, newPressType);
    }
    
    @Test
    public void WHEN_toolbar_nav_button_pressed_THEN_websocket_notified() {
    	var newNavType = MatplotlibButtonType.HOME;
    	
    	model.navigatePlot(newNavType);
    	Mockito.verify(workerThread, Mockito.times(1)).submit(runnableCaptor.capture());
    	
    	runnableCaptor.getValue().run();
    	Mockito.verify(websocket, Mockito.times(1)).navigatePlot(newNavType);
    }
    
    @Test
    public void WHEN_plot_name_set_THEN_plot_name_can_be_read() {
    	model.setPlotName("my_plot_name");
    	assertEquals(model.getPlotName(), "my_plot_name");
    }
    
    @Test
    public void WHEN_plot_message_set_THEN_plot_message_can_be_read() {
    	model.setPlotMessage("my_plot_message");
    	assertEquals(model.getPlotMessage(), "my_plot_message");
    }
    
    @Test
    public void WHEN_forward_state_set_THEN_forward_state_can_be_read() {
    	model.setForwardState(true);
    	assertEquals(model.getForwardState(), true);
    }
    
    @Test
    public void WHEN_back_state_set_THEN_back_state_can_be_read() {
    	model.setBackState(true);
    	assertEquals(model.getBackState(), true);
    }
    
    @Test
    public void WHEN_nav_type_set_THEN_type_sent_to_view_model() {
    	model.toggleZoomAndPan("ZOOM");
    	assertEquals(model.getNavMode(), MatplotlibNavigationType.ZOOM);
    }
    
    @Test
    public void WHEN_model_is_closed_THEN_websocket_closed_and_worker_thread_shutdown() {
    	model.close();
    	Mockito.verify(websocket, Mockito.times(1)).close();
    	Mockito.verify(workerThread, Mockito.times(1)).shutdown();
    }
    
    @Test
    public void WHEN_model_connected_THEN_viewmodel_notified() throws IOException {
    	Mockito.doNothing().when(websocket).connect();
    	model.connect();
    	Mockito.verify(websocket, Mockito.times(1)).connect();
    	Mockito.verify(viewModel, Mockito.times(1)).onConnectionStatus(true);
    }
    
    @Test
    public void WHEN_model_disconnected_THEN_viewmodel_notified() throws IOException {
    	Mockito.doNothing().when(websocket).connect();
    	Mockito.doNothing().when(websocket).close();
    	model.connect();
    	Mockito.verify(websocket, Mockito.times(1)).connect();
    	Mockito.verify(viewModel, Mockito.times(1)).onConnectionStatus(true);
    	model.close();
    	Mockito.verify(websocket, Mockito.times(1)).close();
    	Mockito.verify(viewModel, Mockito.times(1)).onConnectionStatus(false);
    }
    
    @Test
    public void WHEN_no_plot_name_has_been_set_THEN_has_sensible_default() throws IOException {
    	assertEquals(model.getPlotName(), "Figure 1");
    }
    
    
}
