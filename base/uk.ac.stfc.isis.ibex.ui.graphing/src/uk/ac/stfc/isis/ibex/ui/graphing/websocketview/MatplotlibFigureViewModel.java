package uk.ac.stfc.isis.ibex.ui.graphing.websocketview;

import java.io.Closeable;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.logging.log4j.Logger;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.graphics.RGB;
import com.google.common.util.concurrent.ThreadFactoryBuilder;

import uk.ac.stfc.isis.ibex.logger.IsisLog;
import uk.ac.stfc.isis.ibex.logger.LoggerUtils;
import uk.ac.stfc.isis.ibex.model.SettableUpdatedValue;
import uk.ac.stfc.isis.ibex.model.UpdatedValue;

public class MatplotlibFigureViewModel implements Closeable {
	
	private static final Logger LOG = IsisLog.getLogger(MatplotlibFigureViewModel.class);
	
	private final MatplotlibWebsocketModel model;
	
	private final SettableUpdatedValue<String> plotName;
	private final SettableUpdatedValue<ImageData> image;
	
	/** 
	 *  We use this pattern to limit the rate at which we redraw the UI - without this,
	 *  we may get updates faster than we can actually handle.
	 */
	private final AtomicBoolean clientRedrawRequired = new AtomicBoolean(true);
	
	/** 
	 *  Similar to above - we want to avoid sending resize requests to the backend for every
	 *  pixel changed.
	 */
	private final AtomicBoolean serverResizeRequired = new AtomicBoolean(true);
	
	/**
	 * The maximum frequency at which we might draw updates to the plot if new data is available.
	 */
	private static final int MAX_DRAW_RATE_MS = 100;
	
	/**
	 * The maximum frequency at which we might send resize requests to the server.
	 */
	private static final int MAX_RESIZE_RATE_MS = 250;
	
	/**
	 * The rate at which we ask the server to send new frames, even if no new frames have been pushed.
	 * This is required in order to get updates to some dynamic plots.
	 */
	private static final int FORCED_REDRAW_RATE_MS = 2000;
	
	private final ScheduledExecutorService updateExecutor;
	
	int canvasWidth = 0;
	int canvasHeight = 0;

	public MatplotlibFigureViewModel(int figureNumber) {
		this.model = new MatplotlibWebsocketModel(this, "127.0.0.1", 8988, figureNumber);
		
		plotName = new SettableUpdatedValue<String>(
				String.format("[Disconnected] %s", model.getPlotName(), figureNumber));
		image = new SettableUpdatedValue<ImageData>(generateBlankImage());
		
		updateExecutor  = 
				Executors.newSingleThreadScheduledExecutor(
						new ThreadFactoryBuilder()
						    .setNameFormat("MatplotlibFigureViewModel " + model.getServerName() + " update thread %d")
						    .build());
		
		updateExecutor.scheduleWithFixedDelay(this::redrawIfRequired, MAX_DRAW_RATE_MS, MAX_DRAW_RATE_MS, TimeUnit.MILLISECONDS);
		updateExecutor.scheduleWithFixedDelay(this::updateCanvasSizeIfRequired, MAX_RESIZE_RATE_MS, MAX_RESIZE_RATE_MS, TimeUnit.MILLISECONDS);
		updateExecutor.scheduleWithFixedDelay(model::forceServerRefresh, FORCED_REDRAW_RATE_MS, FORCED_REDRAW_RATE_MS, TimeUnit.MILLISECONDS);

		model.forceServerRefresh();
	}
	
	public MatplotlibWebsocketModel getModel() {
		return model;
	}
	
	private void updatePlotName() {
		if (model.isConnected()) {
			plotName.setValue(model.getPlotName());
		} else {
			plotName.setValue(
					String.format("[Disconnected] %s", model.getPlotName()));
		}
	}
	
	/**
	 * Changes the name of this plot.
	 * @param plotName the new name
	 */
	public void onPlotNameChange(String plotName) {
		updatePlotName();
	}
	
	/**
	 * Gets the connection name.
	 * @return the connection name
	 */
	public UpdatedValue<String> getPlotName() {
		return plotName;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void close() {
		updateExecutor.shutdown();
		model.close();
	}
	

	private ImageData generateBlankImage() {
		final PaletteData palette = new PaletteData(new RGB(0xFF, 0xFF, 0xFF));
		return new ImageData(
				this.canvasWidth <= 0 ? 1 : this.canvasWidth, 
				this.canvasHeight <= 0 ? 1 : this.canvasHeight, 
			    8, palette);
	}
	
	/**
	 * Gets an UpdatedValue which contains the image data to be drawn.
	 * @return the image
	 */
	public UpdatedValue<ImageData> getImage() {
		return image;
	}
	
	private void redrawIfRequired() {
		try {
			if (clientRedrawRequired.getAndSet(false)) {
				final ImageData imageData = model.getImageData().orElseGet(this::generateBlankImage);
				image.setValue(imageData);
			}
		} catch (Exception e) {
			LoggerUtils.logErrorWithStackTrace(LOG, e.getMessage(), e);
		}
	}

	/**
	 * Notifies this viewmodel that a new image is available.
	 */
	public void imageUpdated() {
		clientRedrawRequired.set(true);
	}
	
	private void updateCanvasSizeIfRequired() {
		if (serverResizeRequired.getAndSet(false)) {
			model.canvasResized(canvasWidth, canvasHeight);
		}
	}

	/**
	 * Update the canvas size in the model.
	 * @param width new width
	 * @param height new height
	 */
	public void canvasResized(int width, int height) {
		if (width == this.canvasWidth && height == this.canvasHeight) {
			return;
		}
		this.canvasWidth = width;
		this.canvasHeight = height;
		clientRedrawRequired.set(true);
		serverResizeRequired.set(true);
	}

	/**
	 * Notifies this view model about a new connection status.
	 * @param isConnected true if connected
	 */
	public void onConnectionStatus(boolean isConnected) {
		updatePlotName();
		if (isConnected) {
			model.forceServerRefresh();
		}
		
		// If disconnected, cancel any pending resize request to the server.
		// If connected, send resize request to server to ensure it in sync with local size.
		serverResizeRequired.set(isConnected);
	}
}
