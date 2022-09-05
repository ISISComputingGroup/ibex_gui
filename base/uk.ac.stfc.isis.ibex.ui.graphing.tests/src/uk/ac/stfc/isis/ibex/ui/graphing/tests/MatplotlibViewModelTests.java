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
import org.mockito.runners.MockitoJUnitRunner;

import uk.ac.stfc.isis.ibex.ui.graphing.websocketview.MatplotlibFigureViewModel;
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
    public void WHEN_canvas_resized_THEN_resize_request_sent_to_model() {
    	Mockito.verify(workerThread, Mockito.times(3)).scheduleWithFixedDelay(runnableCaptor.capture(), Mockito.anyLong(), Mockito.anyLong(), Mockito.any());
    	viewModel.canvasResized(11, 22);
    	
    	// index 0 = redraw, 1 = canvas size, 2 = force refresh
    	Runnable updateCanvasSizeIfRequired = runnableCaptor.getAllValues().get(1);
    	
    	// index 0 = redraw, 1 = canvas size, 2 = force refresh
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
    	Mockito.verify(workerThread, Mockito.times(3)).scheduleWithFixedDelay(runnableCaptor.capture(), Mockito.anyLong(), Mockito.anyLong(), Mockito.any());
    	
    	// index 0 = redraw, 1 = canvas size, 2 = force refresh
    	runnableCaptor.getAllValues().get(2).run();
    	Mockito.verify(model, Mockito.times(1)).forceServerRefresh();
    }
    
    @Test
    public void WHEN_redraw_required_THEN_image_updated() {
    	Mockito.verify(workerThread, Mockito.times(3)).scheduleWithFixedDelay(runnableCaptor.capture(), Mockito.anyLong(), Mockito.anyLong(), Mockito.any());
    	
    	Mockito.when(model.getImageData()).thenReturn(
    			Optional.of(new ImageData(new ByteArrayInputStream(MatplotlibModelTests.FAKE_IMAGE_BYTES))));
    	
    	var mockListener = Mockito.mock(PropertyChangeListener.class);
    	viewModel.getImage().addPropertyChangeListener(mockListener);
    	viewModel.imageUpdated();
    	
    	// index 0 = redraw, 1 = canvas size, 2 = force refresh
    	runnableCaptor.getAllValues().get(0).run();
    	Mockito.verify(model, Mockito.times(1)).getImageData();
    	// Assert that our listener gets a property change event for the new image.
    	Mockito.verify(mockListener, Mockito.times(1)).propertyChange(Mockito.any());
    }
    
}
