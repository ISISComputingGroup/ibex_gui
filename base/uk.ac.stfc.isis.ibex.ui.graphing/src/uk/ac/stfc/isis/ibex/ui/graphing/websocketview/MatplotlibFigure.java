package uk.ac.stfc.isis.ibex.ui.graphing.websocketview;

import java.beans.PropertyChangeListener;
import java.util.Map;

import org.apache.logging.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.MouseTrackAdapter;
import org.eclipse.swt.events.MouseTrackListener;
import org.eclipse.swt.graphics.GC;
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
	
	private final Composite container;
	private final MatplotlibFigureViewModel viewModel;

	private final Canvas plotCanvas;
	private final Label labelConnectionStatus;
	private final Label plotMessage;
	private Image plotImage;
	private MatplotlibToolbar toolBar;
	
	private final PropertyChangeListener connectionNameListener;
	private final PropertyChangeListener imageListener;
	private final PropertyChangeListener plotMessageListener;
	private final MouseTrackListener mouseTrackListener;
	private final MouseMoveListener mouseMoveListener;
	private final MouseListener mouseListener;

	private static final int NUM_COLUMNS = 2;

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
		
		var containerLayout = new GridLayout(NUM_COLUMNS, false);
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
		plotCanvas.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, NUM_COLUMNS, 1));
		
		plotImage = new Image(Display.getDefault(), 500, 500);
		
		toolBar = new MatplotlibToolbar(viewModel, container);
		
		plotCanvas.addControlListener(new ControlAdapter() {
			@Override
			public void controlResized(ControlEvent e) {
				Rectangle bounds = plotCanvas.getBounds();
				viewModel.canvasResized(bounds.width, bounds.height);
			}
		});
		
		plotCanvas.addPaintListener(event -> event.gc.drawImage(plotImage, 0, 0));
		
		viewModel.canvasResized(plotCanvas.getBounds().width, plotCanvas.getBounds().height);
		
		mouseTrackListener = new MouseTrackAdapter() {
			@Override
			public void mouseExit(MouseEvent e) {
				viewModel.setCursorPosition(MatplotlibCursorPosition.OUTSIDE_CANVAS);
			}
			
			@Override
			public void mouseEnter(MouseEvent e) {
				viewModel.setCursorPosition(new MatplotlibCursorPosition(e.x, e.y, true));
			}
		};

		mouseMoveListener = new MouseMoveListener() {
			@Override
			public void mouseMove(MouseEvent e) {
				viewModel.setCursorPosition(new MatplotlibCursorPosition(e.x, e.y, true));
				
				if (viewModel.getDragState().getValue()) {
					plotCanvas.redraw();
				}
			}
		};
		
		mouseListener = new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				viewModel.notifyButtonPressed(new MatplotlibCursorPosition(e.x, e.y, true), MatplotlibPressType.BUTTON_PRESS);
			}
			
			@Override
			public void mouseUp(MouseEvent e) {
				viewModel.notifyButtonPressed(new MatplotlibCursorPosition(e.x, e.y, true), MatplotlibPressType.BUTTON_RELEASE);
				
				if (viewModel.getDragState().getValue()) {
					plotCanvas.redraw();
				}
			}
		};
		
		plotCanvas.addMouseTrackListener(mouseTrackListener);
		plotCanvas.addMouseMoveListener(mouseMoveListener);
		plotCanvas.addMouseListener(mouseListener);
		
		plotCanvas.addListener(SWT.Paint, e -> {
            if (viewModel.getDragState().getValue()) {
                GC gc = e.gc;
                
                Map<String, Integer> bounds = viewModel.getSelectionBounds();
                
                gc.setLineStyle(SWT.LINE_DASH);
                gc.drawRectangle(bounds.get("minX"), bounds.get("minY"), bounds.get("width"), bounds.get("height"));
            }
        });
		
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
			if (!plotImage.isDisposed()) {
				plotImage.dispose();
				plotImage = new Image(Display.getDefault(), imageData);
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
		
		if (!plotImage.isDisposed()) {
			plotImage.dispose();
		}
		
		if (!plotCanvas.isDisposed()) {
			plotCanvas.removeMouseTrackListener(mouseTrackListener);
			plotCanvas.removeMouseMoveListener(mouseMoveListener);
			plotCanvas.removeMouseListener(mouseListener);
			plotCanvas.dispose();
		}
		labelConnectionStatus.dispose();
		
		toolBar.dispose();
		
		plotMessage.dispose();
		container.dispose();
		super.dispose();
	}
}