package uk.ac.stfc.isis.ibex.ui.graphing.websocketview;

import java.io.Closeable;

import org.apache.logging.log4j.Logger;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Display;

import uk.ac.stfc.isis.ibex.logger.IsisLog;
import uk.ac.stfc.isis.ibex.logger.LoggerUtils;

/**
 * The view for a matplotlib canvas.
 */
public class MatplotlibRenderer implements Closeable {

	private static final Logger LOG = IsisLog.getLogger(MatplotlibRenderer.class);
	
	private Image image;
	private final Canvas canvas;
	
	private final MatplotlibRendererViewModel viewModel;
	
	public MatplotlibRenderer(Canvas canvas, int figNum) {
		this.viewModel = new MatplotlibRendererViewModel(this, figNum);
		
		this.canvas = canvas;
		image = new Image(Display.getDefault(), 500, 500);
		
		canvas.addPaintListener(event -> event.gc.drawImage(image, 0, 0));
	}
	
	public void drawImage(ImageData imageData) {
		try {
			if (!image.isDisposed()) {
				image.dispose();
			}
			image = new Image(Display.getDefault(), imageData);
			canvas.redraw();
		} catch (Exception e) {
			LoggerUtils.logErrorWithStackTrace(LOG, e.getMessage(), e);
			throw e;
		}
	}

	@Override
	public void close() {
		viewModel.close();
		if (!image.isDisposed()) {
			image.dispose();
		}
	}

	public void canvasResized(int width, int height) {
		viewModel.canvasResized(width, height);
	}
}
