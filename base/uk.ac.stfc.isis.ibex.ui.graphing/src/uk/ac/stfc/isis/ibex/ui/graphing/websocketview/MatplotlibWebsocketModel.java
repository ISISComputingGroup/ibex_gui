package uk.ac.stfc.isis.ibex.ui.graphing.websocketview;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import java.io.Closeable;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.Logger;
import org.eclipse.swt.graphics.ImageData;

import uk.ac.stfc.isis.ibex.logger.IsisLog;
import uk.ac.stfc.isis.ibex.logger.LoggerUtils;
import uk.ac.stfc.isis.ibex.ui.graphing.utils.ByteBufferInputStream;

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
	 * {@inheritDoc}
	 */
	public Optional<ImageData> getImageData() {
		return imageData;
	}

	public void subscribe(IPlotUpdateListener listener) {
		updateListeners.add(listener);
	}

	public void unsubscribe(IPlotUpdateListener listener) {
		updateListeners.remove(listener);
	}

	public void connect() throws IOException {
		this.connection.connect();
		setConnectionStatus(true);
	}

	public void setImageData(ByteBuffer message) {
		try (var inputStream = new ByteBufferInputStream(message)) { 
			LOG.info("model: setting image to " + message);
		    this.imageData = Optional.of(new ImageData(inputStream));
		
		    updateListeners.forEach(IPlotUpdateListener::imageUpdated);
		} catch (IOException e) {
			LoggerUtils.logErrorWithStackTrace(LOG, e.getMessage(), e);
		}
	}

	public void forceServerRedraw() {
		workerThread.submit(connection::forceServerRedraw);
	}

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
	
	public boolean isConnected() {
		return isConnected;
	}
	
	public void onConnectionClose() {
		setConnectionStatus(false);
		this.imageData = Optional.empty();
		updateListeners.forEach(IPlotUpdateListener::imageUpdated);
	}
	
	public String getServerName() {
		return connection.toString();
	}
}