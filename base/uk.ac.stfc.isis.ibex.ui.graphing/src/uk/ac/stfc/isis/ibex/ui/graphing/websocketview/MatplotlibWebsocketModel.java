package uk.ac.stfc.isis.ibex.ui.graphing.websocketview;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.eclipse.swt.graphics.ImageData;

/**
 * A websocket-based matplotlib model.
 */
public class MatplotlibWebsocketModel implements Closeable, AutoCloseable {
	
	private final ScheduledExecutorService workerThread;
	
	private final MatplotlibFigureViewModel viewModel;
	
	private final MatplotlibWebsocketEndpoint connection;
	
	private static final int RECONNECTION_WAIT_TIME_S = 5;
	
	private String plotName;
	private String plotMessage;
	private MatplotlibNavigationType navMode;
	private MatplotlibCursorPosition cursorPosition = MatplotlibCursorPosition.OUTSIDE_CANVAS;
	private boolean backState;
	private boolean forwardState;
	
	private Optional<ImageData> imageData = Optional.empty();
	
	private boolean isConnected = false;
	private boolean isDiffImage = false;
	
	private final Runnable tryConnectTask = () -> {
		if (!isConnected) {
			try {
				connect();
			} catch (IOException | RuntimeException e) {
				// ignore
			}
		}
	};
	
	/**
	 * Constructor.
	 * @param viewModel the viewmodel
	 * @param url the connection url
	 * @param figNum the figure number
	 */
	public MatplotlibWebsocketModel(MatplotlibFigureViewModel viewModel, String url, int figNum) {
		this.viewModel = viewModel;
	    this.workerThread = createWorkerThread(url);
	    this.connection = new MatplotlibWebsocketEndpoint(this, url, figNum);
	    this.plotName = String.format("Figure %d", figNum);
	    this.plotMessage = "";
	    
	    workerThread.scheduleWithFixedDelay(tryConnectTask, 0, RECONNECTION_WAIT_TIME_S, TimeUnit.SECONDS);
	}
	
	/**
	 * Constructor.
	 * @param viewModel the view model
	 * @param url the connection url
	 * @param figNum the figure number
	 * @param connection the websocket connection
	 * @param workerThread the worker thread
	 */
	public MatplotlibWebsocketModel(MatplotlibFigureViewModel viewModel, String url, int figNum, MatplotlibWebsocketEndpoint connection, ScheduledExecutorService workerThread) {
		this.viewModel = viewModel;
	    this.workerThread = workerThread;
	    this.connection = connection;
	    this.plotName = String.format("Figure %d", figNum);
	    this.plotMessage = "";
	    
	    workerThread.scheduleWithFixedDelay(tryConnectTask, 0, RECONNECTION_WAIT_TIME_S, TimeUnit.SECONDS);
	}
	
	/**
	 * Creates the thread pool for use by this class.
	 * @return the thread pool
	 */
	private ScheduledExecutorService createWorkerThread(String url) {
		return Executors.newSingleThreadScheduledExecutor(
				new ThreadFactoryBuilder()
				    .setNameFormat("MatplotLibWebsocketModel " + url + " reconnect thread %d")
				    .build()
				);
	}
	
	/**
	 * Disconnect from the matplotlib backend.
	 */
	@Override
	public void close() {
		setConnectionStatus(false);
		workerThread.shutdown();
		connection.close();
	}

	/**
	 * Gets the image data.
	 * @return the image data
	 */
	public Optional<ImageData> getImageData() {
		return imageData;
	}

	/**
	 * Attemps to connect to the server.
	 * @throws IOException if could not connect
	 */
	public void connect() throws IOException {
		if (!isConnected()) {
			this.connection.connect();
			setConnectionStatus(true);
		}
	}
	
	/**
	 * Sets whether the next frame received from the server will be
	 * a diff or full frame.
	 * @param isDiffImage whether the next frame is a diff
	 */
	public void setIsDiffImage(boolean isDiffImage) {
		this.isDiffImage = isDiffImage;
	}

	/**
	 * Sets the image data received by the websocket.
	 * @param message the message
	 */
	public void setImageData(final InputStream message) {
		// We can't handle diff frames easily, so ignore them and we'll catch up
		// later with an explicit redraw.
		if (!isDiffImage) {
		    this.imageData = Optional.of(new ImageData(message));
		    viewModel.imageUpdated();
		}
	}
	
	/**
	 * Forces the server to draw a frame and resend it.
	 */
	public void forceServerRefresh() {
		workerThread.submit(() -> {
			connection.forceServerRefresh();
			connection.cursorPositionChanged(cursorPosition);
		});
	}

	/**
	 * Notifies the server that the canvas size has changed.
	 * @param width the new width
	 * @param height the new height
	 */
	public void canvasResized(int width, int height) {
		workerThread.submit(() -> connection.canvasResized(width, height));
	}

	/**
	 * Notifies the server that the cursor position has changed.
	 * @param position the cursor position
	 */
	public void cursorPositionChanged(final MatplotlibCursorPosition position) {
		this.cursorPosition = position;
		workerThread.submit(() -> {
			connection.cursorPositionChanged(position);
			connection.forceServerRefresh();
		});
	}
	
	private void setConnectionStatus(final boolean isConnected) {
		if (isConnected == this.isConnected) {
			return;
		}
		this.isConnected = isConnected;
		viewModel.onConnectionStatus(isConnected);
	}
	
	/**
	 * Notifies the server that the plot is to be navigated, force refresh
	 * the server 
	 * @param navType the button's navigation type
	 */
	public void navigatePlot(MatplotlibButtonType navType) {
		workerThread.submit(() -> {
			connection.navigatePlot(navType);
		    connection.forceServerRefresh();
		});
	}
	
	/**
	 * Notifies the server that a mouse button as been pressed/released over the figure 
	 * @param position the cursor position
	 * @param pressType the button event type
	 */
	public void notifyButtonPress(final MatplotlibCursorPosition position, MatplotlibPressType pressType) {
		workerThread.submit(() -> {
		    connection.notifyButtonPress(position, pressType);
		    connection.forceServerRefresh();
		});
	}
	
	/**
	 * Sets a new name for this plot.
	 * @param newName the new name
	 */
	public void setPlotName(String newName) {
		plotName = newName;
		viewModel.updatePlotName();
	}
	
	/**
	 * Gets the name of this plot.
	 * @return the new name
	 */
	public String getPlotName() {
		return plotName;
	}
	
	/**
	 * Sets a new message for this plot.
	 * @param newMessage the new name
	 */
	public void setPlotMessage(String newMessage) {
		plotMessage = newMessage;
		viewModel.updatePlotMessage();
	}
	
	/**
	 * Gets the message of this plot.
	 * @return the new name
	 */
	public String getPlotMessage() {
		return plotMessage;
	}
	
	/**
	 * Sets the new value for whether the back
	 * button is enabled (or not)
	 * @param backEnabled
	 */
	public void setBackState(boolean backEnabled) {
		this.backState = backEnabled;
		viewModel.updateBackState();
	}
	
	/**
	 * @return whether 'back' is enabled
	 */
	public boolean getBackState() {
		return backState;
	}
	
	/**
	 * Sets the new value for whether the forwards
	 * button is enabled (or not)
	 * @param forwardEnabled
	 */
	public void setForwardState(boolean forwardEnabled) { 
		this.forwardState = forwardEnabled; 
		viewModel.updateForwardState(); 
	}
	
	/**
	 * @return whether 'forwards' is enabled
	 */
	public boolean getForwardState() {
		return forwardState;
	}
	
	/**
	 * Sets the new value for which navigation
	 * mode the plot is in (NONE/ZOOM/PAN)
	 * @param navMode
	 */
	public void toggleZoomAndPan(String navMode) {
		switch(navMode) {
			case "ZOOM":
				this.navMode = MatplotlibNavigationType.ZOOM;
				break;
			case "PAN":
				this.navMode = MatplotlibNavigationType.PAN;
				break;
			case "NONE":
				this.navMode = MatplotlibNavigationType.NONE;
				break;
			default:
				break;
		}
		viewModel.updateNavMode();
	}
	
	/**
	 * @return the current navigation mode
	 */
	public MatplotlibNavigationType getNavMode() {
		return navMode;
	}
	
	/**
	 * Whether the server is currently connected.
	 * @return true if server is connected
	 */
	public boolean isConnected() {
		return isConnected;
	}
	
	/**
	 * Handler for connection closed events.
	 */
	public void onConnectionClose() {
		setConnectionStatus(false);
		this.imageData = Optional.empty();
		viewModel.imageUpdated();
	}
	
	/**
	 * The name of the server this model is connected to.
	 * @return the server name
	 */
	public String getServerName() {
		return String.format("%s", connection);
	}

}