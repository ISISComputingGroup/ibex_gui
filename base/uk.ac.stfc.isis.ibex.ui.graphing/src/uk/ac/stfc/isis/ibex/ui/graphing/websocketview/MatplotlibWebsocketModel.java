package uk.ac.stfc.isis.ibex.ui.graphing.websocketview;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.Logger;
import org.eclipse.swt.graphics.ImageData;
import uk.ac.stfc.isis.ibex.logger.IsisLog;

public class MatplotlibWebsocketModel implements Closeable, AutoCloseable {
	
	private static final Logger LOG = IsisLog.getLogger(MatplotlibWebsocketModel.class);
	
	private final ScheduledExecutorService workerThread;
	
	private final Set<IPlotUpdateListener> updateListeners = new CopyOnWriteArraySet<IPlotUpdateListener>();
	
	private final MatplotlibWebsocketEndpoint connection;
	
	private Optional<ImageData> imageData = Optional.empty();
	
	private boolean isConnected = false;
	
	private final Runnable tryConnectTask = () -> {
		if (!isConnected) {
			try {
				connect();
			} catch (IOException | RuntimeException e) {
				// ignore
			}
		}
	};
	
	public MatplotlibWebsocketModel(String hostName, int port, int figNum) {
	    this.workerThread = createWorkerThread();
	    this.connection = new MatplotlibWebsocketEndpoint(this, hostName, port, figNum);
	    
	    workerThread.scheduleWithFixedDelay(tryConnectTask, 0, 5, TimeUnit.SECONDS);
	}
	
	/**
	 * Creates the thread pool for use by this class.
	 * @return the thread pool
	 */
	protected ScheduledExecutorService createWorkerThread() {
		return Executors.newSingleThreadScheduledExecutor(new ThreadFactoryBuilder().setNameFormat("MatplotLibWebsocketModel %d").build());
	}
	
	/**
	 * Disconnect from the matplotlib backend.
	 * @throws IOException if the disconnection failed
	 */
	@Override
	public void close() throws IOException {
		LOG.info(String.format("%s disconnecting from %s", getClass().getName(), connection));
		setConnectionStatus(false);
		updateListeners.clear();
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
	 * Subscribes a plot update listener to this model.
	 * @param listener the listener
	 */
	public void subscribe(IPlotUpdateListener listener) {
		updateListeners.add(listener);
	}

	/**
	 * Unsubscribes a plot update listener from this model.
	 * @param listener the listener
	 */
	public void unsubscribe(IPlotUpdateListener listener) {
		updateListeners.remove(listener);
	}

	/**
	 * Attemps to connect to the server.
	 * @throws IOException if could not connect
	 */
	public void connect() throws IOException {
		this.connection.connect();
		setConnectionStatus(true);
	}

	/**
	 * Sets the image data received by the websocket.
	 * @param message the message
	 */
	public void setImageData(final InputStream message) {
	    this.imageData = Optional.of(new ImageData(message));
	    updateListeners.forEach(IPlotUpdateListener::imageUpdated);
	}
	
	/**
	 * Forces the server to draw a frame and resend it.
	 * 
	 * @see MatplotlibWebsocketEndpoint.forceServerRefresh
	 */
	public void forceServerRefresh() {
		workerThread.submit(connection::forceServerRefresh);
	}

	/**
	 * Notifies the server that the canvas size has changed.
	 * @param width the new width
	 * @param height the new height
	 */
	public void canvasResized(int width, int height) {
		workerThread.submit(() -> connection.canvasResized(width, height));
	}
	
	private void setConnectionStatus(final boolean isConnected) {
		if (isConnected == this.isConnected) {
			return;
		}
		LOG.info(String.format("%s server %s", isConnected ? "Connected to" : "Disconnected from", this.connection));
		this.isConnected = isConnected;
		updateListeners.forEach(listener -> listener.onConnectionStatus(isConnected));
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
		updateListeners.forEach(IPlotUpdateListener::imageUpdated);
	}
	
	/**
	 * The name of the server this model is connected to.
	 * @return the server name
	 */
	public String getServerName() {
		return connection.toString();
	}
}