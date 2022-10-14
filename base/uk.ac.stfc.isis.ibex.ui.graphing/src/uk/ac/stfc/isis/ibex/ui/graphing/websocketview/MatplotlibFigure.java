package uk.ac.stfc.isis.ibex.ui.graphing.websocketview;

import java.beans.PropertyChangeListener;

import org.apache.logging.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseTrackListener;
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
@SuppressWarnings("checkstyle:magicnumber")
public class MatplotlibFigure extends Composite {
	
	private static final Logger LOG = IsisLog.getLogger(MatplotlibFigure.class);

	private final Canvas plotCanvas;
	private final Label labelConnectionStatus;
	private final Label plotMessage;
	private final Composite container;
	private final MatplotlibFigureViewModel viewModel;
	
	private final PropertyChangeListener connectionNameListener;
	private final PropertyChangeListener imageListener;
	private final PropertyChangeListener plotMessageListener;
	private Image image;
	private final MouseTrackListener mouseTrackListener;

	/**
	 * Create the composite.
	 * @param parent the parent
	 * @param style the style
	 * @param url the url
	 * @param figureNumber the figure numbers
	 */
	public MatplotlibFigure(Composite parent, int style, String url, int figureNumber) {
		super(parent, style);
		
		viewModel = new MatplotlibFigureViewModel(url, figureNumber);
		var layout = new GridLayout(1, false);
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		this.setLayout(layout);
		
		container = new Composite(this, SWT.NONE);
		container.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		
		var containerLayout = new GridLayout(2, false);
		containerLayout.marginWidth = 0;
		containerLayout.marginHeight = 0;
		container.setLayout(containerLayout);
		
		labelConnectionStatus = new Label(container, SWT.NONE);
		labelConnectionStatus.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
		labelConnectionStatus.setText(viewModel.getPlotName().getValue());
		
		plotMessage = new Label(container, SWT.NONE);
		plotMessage.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
		plotMessage.setText("");
		
		plotCanvas = new Canvas(container, SWT.NONE);
		plotCanvas.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
		
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
		
		mouseTrackListener = new MouseTrackListener() {
			@Override
			public void mouseHover(MouseEvent e) {
				viewModel.setCursorPosition(new MatplotlibCursorPosition(e.x, e.y, true));
			}
			
			@Override
			public void mouseExit(MouseEvent e) {
				viewModel.setCursorPosition(MatplotlibCursorPosition.OUTSIDE_CANVAS);
			}
			
			@Override
			public void mouseEnter(MouseEvent e) {
				viewModel.setCursorPosition(new MatplotlibCursorPosition(e.x, e.y, true));
			}
		};
		
		plotCanvas.addMouseTrackListener(mouseTrackListener);
		
		connectionNameListener = viewModel.getPlotName()
				.addUiThreadPropertyChangeListener(e -> {
					if (!labelConnectionStatus.isDisposed()) {
					    labelConnectionStatus.setText(e.getNewValue().toString());
					}
				});
		plotMessageListener = viewModel.getPlotMessage()
				.addUiThreadPropertyChangeListener(e -> {
					if (!plotMessage.isDisposed()) {
					    plotMessage.setText(e.getNewValue().toString());
					}
				});
		imageListener = viewModel.getImage()
				.addUiThreadPropertyChangeListener(e -> drawImage((ImageData) e.getNewValue()));
	}
	
	/**
	 * Draws the provided image data to screen.
	 * @param imageData the image data
	 */
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
		viewModel.getPlotMessage().removePropertyChangeListener(plotMessageListener);
		viewModel.close();
		
		if (!image.isDisposed()) {
			image.dispose();
		}
		plotCanvas.removeMouseTrackListener(mouseTrackListener);
		plotCanvas.dispose();
		labelConnectionStatus.dispose();
		plotMessage.dispose();
		container.dispose();
		super.dispose();
	}
}