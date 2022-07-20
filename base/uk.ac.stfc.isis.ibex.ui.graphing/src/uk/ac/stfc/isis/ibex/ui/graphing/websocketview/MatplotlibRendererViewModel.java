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
import org.eclipse.swt.widgets.Display;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import uk.ac.stfc.isis.ibex.logger.IsisLog;
import uk.ac.stfc.isis.ibex.logger.LoggerUtils;


/**
 * The view model for a matplotlib canvas.
 */
public class MatplotlibRendererViewModel extends PlotUpdateAdapter implements Closeable {
	
	private final MatplotlibWebsocketModel model;
	private final MatplotlibRenderer renderer;
	private static final Logger LOG = IsisLog.getLogger(MatplotlibRendererViewModel.class);
	
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
	
	private static final int MAX_DRAW_RATE_MS = 50;
	
	/**
	 * The maximum frequency at which we might send resize requests to the server.
	 */
	private static final int MAX_RESIZE_RATE_MS = 100;
	
	/**
	 * The rate at which we ask the server to send new frames, even if no new frames have been pushed.
	 * This is required in order to get updates to some dynamic plots.
	 */
	private static final int FORCED_REDRAW_RATE_MS = 2000;
	
	int canvasWidth = 0;
	int canvasHeight = 0;
	
	private final ScheduledExecutorService PERIODIC_UPDATES = 
			Executors.newSingleThreadScheduledExecutor(
					new ThreadFactoryBuilder()
					    .setNameFormat("MatplotlibRendererViewModel update checks %d")
					    .build());
	
	/**
	 * Creates a new renderer view model.
	 * @param renderer the renderer that this viewmodel is for
	 * @param figNum the figure number
	 */
	public MatplotlibRendererViewModel(MatplotlibRenderer renderer, int figNum, MatplotlibWebsocketModel model) {
		this.model = model;
		this.renderer = renderer;
		
		PERIODIC_UPDATES.scheduleWithFixedDelay(this::redrawIfRequired, MAX_DRAW_RATE_MS, MAX_DRAW_RATE_MS, TimeUnit.MILLISECONDS);
		PERIODIC_UPDATES.scheduleWithFixedDelay(this::updateCanvasSizeIfRequired, MAX_RESIZE_RATE_MS, MAX_RESIZE_RATE_MS, TimeUnit.MILLISECONDS);
		PERIODIC_UPDATES.scheduleWithFixedDelay(model::forceServerRefresh, FORCED_REDRAW_RATE_MS, FORCED_REDRAW_RATE_MS, TimeUnit.MILLISECONDS);

		model.subscribe(this);
		model.forceServerRefresh();
	}
	
	private ImageData generateBlankImage() {
		final PaletteData palette = new PaletteData(new RGB(0xFF, 0xFF, 0xFF));
		return new ImageData(
				this.canvasWidth <= 0 ? 1 : this.canvasWidth, 
				this.canvasHeight <= 0 ? 1 : this.canvasHeight, 
			    8, palette);
	}
	
	private void redrawIfRequired() {
		try {
			if (clientRedrawRequired.getAndSet(false)) {
				final ImageData imageData = model.getImageData().orElseGet(this::generateBlankImage);
				Display.getDefault().asyncExec(() -> renderer.drawImage(imageData));
			}
		} catch (Exception e) {
			LoggerUtils.logErrorWithStackTrace(LOG, e.getMessage(), e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void imageUpdated() {
		clientRedrawRequired.set(true);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void close() {
		model.unsubscribe(this);
	}
	
	private void updateCanvasSizeIfRequired() {
		if (serverResizeRequired.getAndSet(false)) {
			model.canvasResized(canvasWidth, canvasHeight);
		}
	}

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
	 * {@inheritDoc}
	 */
	@Override
	public void onConnectionStatus(boolean isConnected) {
		if (isConnected) {
			model.forceServerRefresh();
		}
		
		// If disconnected, cancel any pending resize request to the server.
		// If connected, send resize request to server to ensure it in sync with local size.
		serverResizeRequired.set(isConnected);
	}
}
