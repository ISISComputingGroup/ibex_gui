package uk.ac.stfc.isis.ibex.ui.graphing.websocketview;

import java.beans.PropertyChangeListener;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;


public class MatplotlibFigure extends Composite {

	private Canvas plotCanvas;
	private MatplotlibRenderer imageRenderer;
	private Label labelConnectionStatus;
	private final MatplotlibFigureViewModel viewModel;
	
	private final Set<PropertyChangeListener> subscriptions = new HashSet<>();
	
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
		
		connectionNameListener = viewModel.getConnectionName()
				.addUiThreadPropertyChangeListener(e -> labelConnectionStatus.setText(e.getNewValue().toString()));
		
		plotCanvas = new Canvas(container, SWT.NONE);
		plotCanvas.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		
		imageRenderer = new MatplotlibRenderer(plotCanvas, figureNumber);
		
		plotCanvas.addControlListener(new ControlAdapter() {
			@Override
			public void controlResized(ControlEvent e) {
				Rectangle bounds = plotCanvas.getBounds();
				imageRenderer.canvasResized(bounds.width, bounds.height);
			}
		});
	}
	
	public void setConnectionName(String text) {
		labelConnectionStatus.setText(text);
	}
	
	public void focus() {
		plotCanvas.setFocus();
	}
	
	public void dispose() {
		subscriptions.forEach(c -> viewModel.getConnectionName().removePropertyChangeListener(connectionNameListener));
		subscriptions.clear();
		
		imageRenderer.close();
		plotCanvas.dispose();
		super.dispose();
	}
}