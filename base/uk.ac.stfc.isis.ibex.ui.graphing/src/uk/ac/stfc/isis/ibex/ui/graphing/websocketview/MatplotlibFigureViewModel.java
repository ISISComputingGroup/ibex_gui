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

/**
 * The viewmodel for a matplotlib figure.
 */
public class MatplotlibFigureViewModel implements Closeable {
	
	private static final Logger LOG = IsisLog.getLogger(MatplotlibFigureViewModel.class);
	
	private final MatplotlibWebsocketModel model;
	
	private final SettableUpdatedValue<String> plotName;
	private final SettableUpdatedValue<String> plotMessage;
	private final SettableUpdatedValue<ImageData> image;
	
	private final SettableUpdatedValue<Boolean> backEnabled;
	private final SettableUpdatedValue<Boolean> forwardEnabled;
	private final SettableUpdatedValue<String> navMode;
	
	private static final int PALETTE_BIT_DEPTH = 8;
	
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
	 *  Similar to above - we want to avoid sending cursor events to the backend every single time we
	 *  hover over the graph.
	 */
	private final AtomicBoolean cursorPositionChanged = new AtomicBoolean(true);
	
	/**
	 * The maximum frequency at which we might draw updates to the plot, if new data is available.
	 * 
	 * Setting this too fast could cause excessive CPU consumption if the image is being rapidly updated
	 * by the server.
	 */
	private static final int MAX_DRAW_RATE_MS = 250;
	
	/**
	 * The maximum frequency at which we might send resize requests to the server.
	 * 
	 * Every N ms, the viewmodel will check whether the size has been updated and send the server
	 * a resize request if so.
	 */
	private static final int MAX_RESIZE_RATE_MS = 500;
	
	/**
	 * The maximum frequency at which we might send metadata (e.g. cursor position) to the server.
	 */
	private static final int MAX_METADATA_EVENT_RATE = 50;
	
	/**
	 * The rate at which we ask the server to send new frames, even if no new frames have been pushed.
	 * This is required in order to get updates to some dynamic plots.
	 */
	private static final int FORCED_REDRAW_RATE_MS = 2000;
	
	private MatplotlibCursorPosition cursorPosition = MatplotlibCursorPosition.OUTSIDE_CANVAS;
	
	private final ScheduledExecutorService updateExecutor;
	
	int canvasWidth = 0;
	int canvasHeight = 0;

	/**
	 * Constructor.
	 * @param url the url serving these plots
	 * @param figureNumber the figure number
	 */
	public MatplotlibFigureViewModel(String url, int figureNumber) {
		this.model = new MatplotlibWebsocketModel(this, url, figureNumber);
		
		plotName = new SettableUpdatedValue<String>(
				String.format("[Disconnected] %s", model.getPlotName(), figureNumber));
		image = new SettableUpdatedValue<ImageData>(generateBlankImage());
		plotMessage = new SettableUpdatedValue<String>("");
		backEnabled = new SettableUpdatedValue<Boolean>(false);
		forwardEnabled = new SettableUpdatedValue<Boolean>(false);
		navMode = new SettableUpdatedValue<String>("NONE");
		
		updateExecutor  = 
				Executors.newSingleThreadScheduledExecutor(
						new ThreadFactoryBuilder()
						    .setNameFormat("MatplotlibFigureViewModel " + url + " update thread %d")
						    .build());
		
		scheduleUpdates();
	}
	
	/**
	 * Constructor for testing.
	 * @param url the url serving these plots
	 * @param figureNumber the figure number
	 * @param model the model
	 * @param executor a threadpool
	 */
	public MatplotlibFigureViewModel(String url, int figureNumber, MatplotlibWebsocketModel model, ScheduledExecutorService executor) {
		this.model = model;
		
		plotName = new SettableUpdatedValue<String>(
				String.format("[Disconnected] %s", model.getPlotName(), figureNumber));
		image = new SettableUpdatedValue<ImageData>(generateBlankImage());
		plotMessage = new SettableUpdatedValue<String>("");
		backEnabled = new SettableUpdatedValue<Boolean>(false);
		forwardEnabled = new SettableUpdatedValue<Boolean>(false);
		navMode = new SettableUpdatedValue<String>("NONE");
		
		updateExecutor  = executor;
		
		scheduleUpdates();
	}
	
	private void scheduleUpdates() {
		updateExecutor.scheduleWithFixedDelay(this::redrawIfRequired, 0, MAX_DRAW_RATE_MS, TimeUnit.MILLISECONDS);
		updateExecutor.scheduleWithFixedDelay(this::updateCanvasSizeIfRequired, 0, MAX_RESIZE_RATE_MS, TimeUnit.MILLISECONDS);
		updateExecutor.scheduleWithFixedDelay(this::updateCursorPositionIfRequired, 0, MAX_METADATA_EVENT_RATE, TimeUnit.MILLISECONDS);
		updateExecutor.scheduleWithFixedDelay(model::forceServerRefresh, 0, FORCED_REDRAW_RATE_MS, TimeUnit.MILLISECONDS);
	}
	
	/**
	 * Gets the model associated with this viewmodel.
	 * @return the model
	 */
	public MatplotlibWebsocketModel getModel() {
		return model;
	}

	/**
	 * Gets an UpdatedValue which contains the image data to be drawn.
	 * @return the image
	 */
	public UpdatedValue<ImageData> getImage() {
		return image;
	}
	
	/**
	 * Updates the plot name from the model.
	 */
	public void updatePlotName() {
		if (model.isConnected()) {
			plotName.setValue(model.getPlotName());
		} else {
			plotName.setValue(
					String.format("[Disconnected] %s", model.getPlotName()));
		}
	}
	
	/**
	 * Gets the connection name.
	 * @return the connection name
	 */
	public UpdatedValue<String> getPlotName() {
		return plotName;
	}
	
	/**
	 * Updates the plot message from the model.
	 */
	public void updatePlotMessage() {
		if (model.isConnected()) {
			final var message = model.getPlotMessage();
			plotMessage.setValue(message);
		} else {
			plotMessage.setValue("");
		}
	}

	/**
	 * Gets the message.
	 * @return the message
	 */
	public UpdatedValue<String> getPlotMessage() {
		return plotMessage;
	}
	
	/**
	 * Updates whether 'back' is enabled
	 */
	public void updateBackEnabled() {
		if (model.isConnected()) { 
			  backEnabled.setValue(model.getBackEnabled()); 
		} else { 
			  backEnabled.setValue(false); 
		}
	}
	
	/**
	 * @return whether 'back' is enabled
	 */
	public UpdatedValue<Boolean> getBackEnabled() {
		return backEnabled;
	}
	
	/**
	 * Updates whether 'forward' is enabled
	 */
	public void updateForwardEnabled() {
		if (model.isConnected()) { 
			forwardEnabled.setValue(model.getForwardEnabled()); 
		} else { 
			forwardEnabled.setValue(false); 
		}
	}
	 
	/**
	 * @return whether 'forward' is enabled
	 */
	public UpdatedValue<Boolean> getForwardEnabled() { 
		return forwardEnabled;
	}
	
	/**
	 * Updates which navigation mode the plot is 
	 * currently in (ZOOM or PAN)
	 */
	public void updateNavMode() {
		if (model.isConnected()) { 
			navMode.setValue(model.getNavMode()); 
		} else { 
			navMode.setValue("NONE"); 
		}
	}
	
	/**
	 * @return the navigation mode (ZOOM or PAN)
	 */
	public UpdatedValue<String> getNavMode(){
		return navMode;
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
				PALETTE_BIT_DEPTH, palette);
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
	
	private void updateCursorPositionIfRequired() {
		try {
			if (cursorPositionChanged.getAndSet(false)) {
				model.cursorPositionChanged(cursorPosition);
			}
		} catch (Exception e) {
			LoggerUtils.logErrorWithStackTrace(LOG, e.getMessage(), e);
		}
	}
	
	private void updateCanvasSizeIfRequired() {
		try {
			if (serverResizeRequired.getAndSet(false)) {
				model.canvasResized(canvasWidth, canvasHeight);
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
		updatePlotMessage();
		if (isConnected) {
			model.forceServerRefresh();
		}
		
		// If disconnected, cancel any pending resize request to the server.
		// If connected, send resize request to server to ensure it in sync with local size.
		serverResizeRequired.set(isConnected);
	}
	
	/**
	 * Sets a new cursor position.
	 * @param cursorPosition the new cursor position
	 */
	public void setCursorPosition(final MatplotlibCursorPosition cursorPosition) {
		this.cursorPosition = cursorPosition;
		cursorPositionChanged.set(true);
	}
	
	/**
	 * Notifies the websocket model that a mouse button as been pressed/released over the figure 
	 * @param pressType
	 */
	public void notifyButtonPressed(String pressType) {
		model.notifyButtonPress(cursorPosition, pressType);
	}
	
	/**
	 * Navigates the matplotlib graph depending on which navigation 
	 * button is selected
	 * @param navType
	 */
	public void navigatePlot(String navType) {
		model.navigatePlot(navType);
	}


}
