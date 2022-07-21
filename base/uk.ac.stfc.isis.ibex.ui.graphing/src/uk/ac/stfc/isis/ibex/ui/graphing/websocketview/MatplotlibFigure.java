package uk.ac.stfc.isis.ibex.ui.graphing.websocketview;

import java.beans.PropertyChangeListener;

import org.apache.logging.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;

import uk.ac.stfc.isis.ibex.logger.IsisLog;
import uk.ac.stfc.isis.ibex.logger.LoggerUtils;


/**
 * Composite representing a matplotlib figure and all relevant controls/indicators, as well
 * as the underlying plot canvas.
 */
public class MatplotlibFigure extends Composite {
	
	private static final Logger LOG = IsisLog.getLogger(MatplotlibFigure.class);

	private final Canvas plotCanvas;
	private final Label labelConnectionStatus;
	private final Composite container;
	private final MatplotlibFigureViewModel viewModel;
	
	private final PropertyChangeListener connectionNameListener;
	private final PropertyChangeListener imageListener;
	private Image image;

	public MatplotlibFigure(Composite parent, int style, int figureNumber) {
		super(parent, style);
		
		viewModel = new MatplotlibFigureViewModel(figureNumber);
		var layout = new GridLayout(1, false);
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		this.setLayout(layout);
		
		container = new Composite(this, SWT.NONE);
		container.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		
		var containerLayout = new GridLayout(1, false);
		containerLayout.marginWidth = 0;
		containerLayout.marginHeight = 0;
		container.setLayout(containerLayout);
		
		labelConnectionStatus = new Label(container, SWT.NONE);
		labelConnectionStatus.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
		labelConnectionStatus.setText(viewModel.getPlotName().getValue());
		
		plotCanvas = new Canvas(container, SWT.NONE);
		plotCanvas.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		
		image = new Image(Display.getDefault(), 500, 500);
		
		plotCanvas.addControlListener(new ControlAdapter() {
			@Override
			public void controlResized(ControlEvent e) {
				Rectangle bounds = plotCanvas.getBounds();
				viewModel.canvasResized(bounds.width, bounds.height);
			}
		});
		
		plotCanvas.addPaintListener(event -> event.gc.drawImage(image, 0, 0));
		
		viewModel.canvasResized(plotCanvas.getBounds().width, plotCanvas.getBounds().height);
		
		connectionNameListener = viewModel.getPlotName()
				.addUiThreadPropertyChangeListener(e -> {
					if (!labelConnectionStatus.isDisposed()) {
					    labelConnectionStatus.setText(e.getNewValue().toString());
					}
				});
		imageListener = viewModel.getImage()
				.addUiThreadPropertyChangeListener(e -> drawImage((ImageData) e.getNewValue()));
	}
	
	public void drawImage(ImageData imageData) {
		try {
			if (!image.isDisposed()) {
				image.dispose();
				image = new Image(Display.getDefault(), imageData);
			}
			if (!plotCanvas.isDisposed()) {
			    plotCanvas.redraw();
			}
		} catch (Exception e) {
			LoggerUtils.logErrorWithStackTrace(LOG, e.getMessage(), e);
			throw e;
		}
	}
	
	/**
	 * Sets the connection name.
	 * @param text the name
	 */
	public void setConnectionName(String text) {
		labelConnectionStatus.setText(text);
	}
	
	/**
	 * Disposes this composite.
	 */
	@Override
	public void dispose() {
		viewModel.getPlotName().removePropertyChangeListener(connectionNameListener);
		viewModel.getImage().removePropertyChangeListener(imageListener);
		viewModel.close();
		
		if (!image.isDisposed()) {
			image.dispose();
		}
		
		plotCanvas.dispose();
		labelConnectionStatus.dispose();
		container.dispose();
		super.dispose();
	}
}