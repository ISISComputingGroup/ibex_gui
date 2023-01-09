package uk.ac.stfc.isis.ibex.ui.graphing.websocketview;

import java.io.Closeable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.logging.log4j.Logger;
import org.eclipse.swt.events.MouseEvent;
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
	
	private final SettableUpdatedValue<MatplotlibButtonState> homeState;
	private final SettableUpdatedValue<MatplotlibButtonState> backState;
	private final SettableUpdatedValue<MatplotlibButtonState> forwardState;
	private final SettableUpdatedValue<MatplotlibButtonState> zoomState;
	private final SettableUpdatedValue<MatplotlibButtonState> panState;
	private final SettableUpdatedValue<MatplotlibNavigationType> navMode;
	
	private final SettableUpdatedValue<MatplotlibCursorPosition> startPos;
	private final SettableUpdatedValue<MatplotlibCursorPosition> endPos;
	private final SettableUpdatedValue<Boolean> dragState;
	
	
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
	 * We use this when a user event (panning) causes a redraw that needs to be immediately shown.
	 */
	private final AtomicBoolean allowImmediateRedraw = new AtomicBoolean(false);

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
		
		backState = new SettableUpdatedValue<MatplotlibButtonState>(MatplotlibButtonState.DISABLED);
		forwardState = new SettableUpdatedValue<MatplotlibButtonState>(MatplotlibButtonState.DISABLED);
		homeState = new SettableUpdatedValue<MatplotlibButtonState>(MatplotlibButtonState.DISABLED);
		zoomState = new SettableUpdatedValue<MatplotlibButtonState>(MatplotlibButtonState.DISABLED);
		panState = new SettableUpdatedValue<MatplotlibButtonState>(MatplotlibButtonState.DISABLED);
		navMode = new SettableUpdatedValue<MatplotlibNavigationType>(MatplotlibNavigationType.NONE);
		
		startPos = new SettableUpdatedValue<MatplotlibCursorPosition>(new MatplotlibCursorPosition(0, 0, true));
		endPos = new SettableUpdatedValue<MatplotlibCursorPosition>(new MatplotlibCursorPosition(0, 0, true));
		dragState = new SettableUpdatedValue<Boolean>(false);
		
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
		backState = new SettableUpdatedValue<MatplotlibButtonState>(MatplotlibButtonState.DISABLED);
		forwardState = new SettableUpdatedValue<MatplotlibButtonState>(MatplotlibButtonState.DISABLED);
		homeState = new SettableUpdatedValue<MatplotlibButtonState>(MatplotlibButtonState.DISABLED);
		zoomState = new SettableUpdatedValue<MatplotlibButtonState>(MatplotlibButtonState.DISABLED);
		panState = new SettableUpdatedValue<MatplotlibButtonState>(MatplotlibButtonState.DISABLED);
		navMode = new SettableUpdatedValue<MatplotlibNavigationType>(MatplotlibNavigationType.NONE);

		startPos = new SettableUpdatedValue<MatplotlibCursorPosition>(new MatplotlibCursorPosition(0, 0, true));
		endPos = new SettableUpdatedValue<MatplotlibCursorPosition>(new MatplotlibCursorPosition(0, 0, true));
		dragState = new SettableUpdatedValue<Boolean>(false);
		
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
	 * Sets the plot message 
	 */

	/**
	 * Gets the message.
	 * @return the message
	 */
	public UpdatedValue<String> getPlotMessage() {
		return plotMessage;
	}
	
	/**
	 * Updates whether 'back' is enabled.
	 */
	public void updateBackState() {
		if (model.isConnected()) { 
			backState.setValue(model.getBackState() ? MatplotlibButtonState.ENABLED_INACTIVE : MatplotlibButtonState.DISABLED);
		} else { 
			backState.setValue(MatplotlibButtonState.DISABLED); 
		}
	}
	
	/**
	 * @return whether 'back' is enabled
	 */
	public UpdatedValue<MatplotlibButtonState> getBackButtonState() {
		return backState;
	}
	
	/**
	 * Updates whether 'forward' is enabled.
	 */
	public void updateForwardState() {
		if (model.isConnected()) { 
			forwardState.setValue(model.getForwardState() ? MatplotlibButtonState.ENABLED_INACTIVE : MatplotlibButtonState.DISABLED);
		} else { 
			forwardState.setValue(MatplotlibButtonState.DISABLED); 
		}
	}
	 
	/**
	 * @return whether 'forward' is enabled
	 */
	public UpdatedValue<MatplotlibButtonState> getForwardButtonState() { 
		return forwardState;
	}
	
	/**
	 * Updates which navigation mode the plot is 
	 * currently in (ZOOM/PAN/NONE).
	 */
	public void updateNavMode() {
		if (model.isConnected()) { 
			if (Objects.equals(MatplotlibNavigationType.PAN, model.getNavMode())) {
				panState.setValue(MatplotlibButtonState.ENABLED_ACTIVE);
				zoomState.setValue(MatplotlibButtonState.ENABLED_INACTIVE);
			} else if (Objects.equals(MatplotlibNavigationType.ZOOM, model.getNavMode())) {
				panState.setValue(MatplotlibButtonState.ENABLED_INACTIVE);
				zoomState.setValue(MatplotlibButtonState.ENABLED_ACTIVE);
			} else {
				panState.setValue(MatplotlibButtonState.ENABLED_INACTIVE);
				zoomState.setValue(MatplotlibButtonState.ENABLED_INACTIVE);
			}
		} else { 
			panState.setValue(MatplotlibButtonState.DISABLED); 
			zoomState.setValue(MatplotlibButtonState.DISABLED); 
		}
	}
	
	/**
	 * @return navigation mode (ZOOM or PAN)
	 */
	public UpdatedValue<MatplotlibNavigationType> getNavMode() {
		return navMode;
	}
	
	/**
	 * @return pan enabled state
	 */
	public UpdatedValue<MatplotlibButtonState> getPanButtonState() {
		return panState;
	}
	
	/**
	 * @return zoom enabled state
	 */
	public UpdatedValue<MatplotlibButtonState> getZoomButtonState() {
		return zoomState;
	}

	/**
	 * @return home enabled state
	 */
	public UpdatedValue<MatplotlibButtonState> getHomeButtonState() {
		return homeState;
	}
	
	/**
	 * @return drag selection state
	 */
	public UpdatedValue<Boolean> getDragState() {
		return dragState;
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
				allowImmediateRedraw.set(true);
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
		if (allowImmediateRedraw.getAndSet(false) && !updateExecutor.isShutdown()) {
			updateExecutor.execute(this::redrawIfRequired);
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
		updatePlotMessage();
		updateForwardState();
		updateBackState();
		updateNavMode();
		
		if (isConnected) {
			model.forceServerRefresh();
		}
		
		// If disconnected, cancel any pending resize request to the server.
		// If connected, send resize request to server to ensure it in sync with local size.
		serverResizeRequired.set(isConnected);
		homeState.setValue(isConnected ? MatplotlibButtonState.ENABLED_INACTIVE : MatplotlibButtonState.DISABLED);
	}
	
	/**
	 * Sets a new cursor position.
	 * @param cursorPosition the new cursor position
	 */
	public void setCursorPosition(final MatplotlibCursorPosition cursorPosition) {
		this.cursorPosition = cursorPosition;
		cursorPositionChanged.set(true);
		
		// if zoom and drag are enabled manage drag selection
		if (zoomState.getValue() == MatplotlibButtonState.ENABLED_ACTIVE && dragState.getValue()) {
			setSelectionBounds(MatplotlibDragSelectionType.DRAG_UPDATE, cursorPosition);
		}
	}
	
	/**
	 * Notifies the websocket model that a mouse button as been pressed/released over the figure.
	 * @param cursorPosition the cursor position
	 * @param pressType the type of mouse event
	 */
	public void notifyButtonPressed(MatplotlibCursorPosition cursorPosition, MatplotlibPressType pressType) {
		allowImmediateRedraw.set(true);
		model.notifyButtonPress(cursorPosition, pressType);
		
		// if zoom is enabled manage drag selection
		if (zoomState.getValue() == MatplotlibButtonState.ENABLED_ACTIVE) {
			switch (pressType) {
				case BUTTON_PRESS:
					setSelectionBounds(MatplotlibDragSelectionType.DRAG_START, cursorPosition);
					break;
				case BUTTON_RELEASE:
					setSelectionBounds(MatplotlibDragSelectionType.DRAG_END, cursorPosition);
					break;
				default:
					break;
			}
		}
	}
	
	/**
	 * Navigates the matplotlib graph depending on which navigation 
	 * button is selected.
	 * @param navType
	 */
	public void navigatePlot(MatplotlibButtonType navType) {
		allowImmediateRedraw.set(true);
		model.navigatePlot(navType);
	}

	/**
	 * Sets the bounds of the zoom drag selection box.
	 * @param dragType
	 * @param cursorPosition
	 */
	public void setSelectionBounds(MatplotlibDragSelectionType dragType, MatplotlibCursorPosition cursorPosition) {
		if (dragType == MatplotlibDragSelectionType.DRAG_START) {
			startPos.setValue(cursorPosition);
			endPos.setValue(cursorPosition);
			dragState.setValue(true);
		} else if (dragType == MatplotlibDragSelectionType.DRAG_UPDATE) {
			endPos.setValue(cursorPosition);
			dragState.setValue(true);
		} else {
			endPos.setValue(cursorPosition);
			dragState.setValue(false);
		}
	}
	
	/**
	 * Gets the bounds of the zoom selection box.
	 * @return map of: min x and y distances, width, height
	 */
	public Map<String, Integer> getSelectionBounds() {
		 int minX = Math.min(startPos.getValue().x(), endPos.getValue().x());
         int minY = Math.min(startPos.getValue().y(), endPos.getValue().y());

         int maxX = Math.max(startPos.getValue().x(), endPos.getValue().x());
         int maxY = Math.max(startPos.getValue().y(), endPos.getValue().y());

         int width = maxX - minX;
         int height = maxY - minY;
          
         Map<String, Integer> bounds = new HashMap<>();
         
         bounds.put("minX", minX);
         bounds.put("minY", minY);
         bounds.put("width", width);
         bounds.put("height", height);
         
         return bounds;
	}

}
