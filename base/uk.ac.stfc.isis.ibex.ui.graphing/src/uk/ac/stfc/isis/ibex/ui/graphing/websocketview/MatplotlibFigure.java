package uk.ac.stfc.isis.ibex.ui.graphing.websocketview;

import java.beans.PropertyChangeListener;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;


/**
 * Composite representing a matplotlib figure and all relevant controls/indicators, as well
 * as the underlying plot canvas.
 */
public class MatplotlibFigure extends Composite {

	private Canvas plotCanvas;
	private MatplotlibRenderer imageRenderer;
	private Label labelConnectionStatus;
	private final MatplotlibFigureViewModel viewModel;
	
	private final PropertyChangeListener connectionNameListener;

	public MatplotlibFigure(Composite parent, int style, int figureNumber) {
		super(parent, style);
		
		viewModel = new MatplotlibFigureViewModel(figureNumber);
		this.setLayout(new GridLayout(1, false));
		
		final Composite container = new Composite(this, SWT.NONE);
		container.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		
		container.setLayout(new GridLayout(1, false));
		
		labelConnectionStatus = new Label(container, SWT.NONE);
		labelConnectionStatus.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
		labelConnectionStatus.setText(viewModel.getPlotName().getValue());
		
		connectionNameListener = viewModel.getPlotName()
				.addUiThreadPropertyChangeListener(e -> labelConnectionStatus.setText(e.getNewValue().toString()));
		
		plotCanvas = new Canvas(container, SWT.NONE);
		plotCanvas.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		
		imageRenderer = new MatplotlibRenderer(plotCanvas, figureNumber, viewModel);
		
		plotCanvas.addControlListener(new ControlAdapter() {
			@Override
			public void controlResized(ControlEvent e) {
				Rectangle bounds = plotCanvas.getBounds();
				imageRenderer.canvasResized(bounds.width, bounds.height);
			}
		});
		
		imageRenderer.canvasResized(plotCanvas.getBounds().width, plotCanvas.getBounds().height);
	}
	
	/**
	 * Sets the connection name.
	 * @param text the name
	 */
	public void setConnectionName(String text) {
		labelConnectionStatus.setText(text);
	}
	
	/**
	 * Sets focus on the canvas.
	 */
	public void focus() {
		plotCanvas.setFocus();
	}
	
	/**
	 * Disposes this composite.
	 */
	public void dispose() {
		viewModel.getPlotName().removePropertyChangeListener(connectionNameListener);
		viewModel.close();
		
		imageRenderer.close();
		plotCanvas.dispose();
		super.dispose();
	}
}