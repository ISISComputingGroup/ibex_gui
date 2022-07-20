package uk.ac.stfc.isis.ibex.ui.graphing.websocketview;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.Logger;
import org.eclipse.swt.graphics.ImageData;
import uk.ac.stfc.isis.ibex.logger.IsisLog;

public class MatplotlibWebsocketModel implements Closeable, AutoCloseable {
	
	private static final Logger LOG = IsisLog.getLogger(MatplotlibWebsocketModel.class);
	
	private final ScheduledExecutorService workerThread;
	
	private final MatplotlibFigureViewModel viewModel;
	
	private final MatplotlibWebsocketEndpoint connection;
	
	private String plotName;
	
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
	
	public MatplotlibWebsocketModel(MatplotlibFigureViewModel viewModel, String hostName, int port, int figNum) {
		this.viewModel = viewModel;
	    this.workerThread = createWorkerThread();
	    this.connection = new MatplotlibWebsocketEndpoint(this, hostName, port, figNum);
	    this.plotName = String.format("Figure %d", figNum);
	    
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
	public void close() {
		LOG.info(String.format("%s disconnecting from %s", getClass().getName(), connection));
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
	 * Sets the image data received by the websocket.
	 * @param message the message
	 */
	public void setImageData(final InputStream message) {
	    this.imageData = Optional.of(new ImageData(message));
	    viewModel.imageUpdated();
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
		viewModel.onConnectionStatus(isConnected);
	}
	
	/**
	 * Sets a new name for this plot.
	 * @param newName the new name
	 */
	public void setPlotName(String newName) {
		plotName = newName;
		viewModel.onPlotNameChange(newName);
	}
	
	/**
	 * Gets the name of this plot.
	 * @return the new name
	 */
	public String getPlotName() {
		return plotName;
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
		return connection.toString();
	}
}