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
	
	int canvasWidth = 0;
	int canvasHeight = 0;
	
	private final ScheduledExecutorService PERIODIC_UPDATES = 
			Executors.newSingleThreadScheduledExecutor(
					new ThreadFactoryBuilder().setNameFormat("MatplotlibRendererViewModel update checks %d").build());
	
	public MatplotlibRendererViewModel(MatplotlibRenderer renderer, int figNum) {
		this.model = Activator.getModel(figNum);
		this.renderer = renderer;
		
		PERIODIC_UPDATES.scheduleWithFixedDelay(this::redrawIfRequired, 50, 50, TimeUnit.MILLISECONDS);
		PERIODIC_UPDATES.scheduleWithFixedDelay(this::updateCanvasSizeIfRequired, 100, 100, TimeUnit.MILLISECONDS);
		PERIODIC_UPDATES.scheduleWithFixedDelay(model::forceServerRedraw, 5, 5, TimeUnit.SECONDS);

		model.subscribe(this);
		model.forceServerRedraw();
	}
	
	private ImageData generateBlankImage() {
		final PaletteData palette = new PaletteData(new RGB(0xFF, 0xFF, 0xFF));
		return new ImageData(this.canvasWidth, this.canvasHeight, 8, palette);
	}
	
	private void redrawIfRequired() {
		try {
			if (clientRedrawRequired.getAndSet(false)) {
				LOG.info("redraw was required");
				try {
					final ImageData imageData = model.getImageData().orElseGet(this::generateBlankImage);
					Display.getDefault().asyncExec(() -> renderer.drawImage(imageData));
				} catch (IllegalStateException e) {
					LOG.info("Image not available");
				}
			}
		} catch (Exception e) {
			LoggerUtils.logErrorWithStackTrace(LOG, e.getMessage(), e);
		}
	}

	@Override
	public void imageUpdated() {
		LOG.info("renderer: imageupdated");
		clientRedrawRequired.set(true);
	}
	
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

	@Override
	public void onConnectionStatus(boolean isConnected) {
		if (isConnected) {
			model.forceServerRedraw();
		}
		
		// If disconnected, cancel any pending resize request to the server.
		// If connected, send resize request to server to ensure it in sync with local size.
		serverResizeRequired.set(isConnected);
	}
}
