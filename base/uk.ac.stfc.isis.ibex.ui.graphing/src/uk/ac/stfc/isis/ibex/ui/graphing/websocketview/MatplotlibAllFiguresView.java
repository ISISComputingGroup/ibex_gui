package uk.ac.stfc.isis.ibex.ui.graphing.websocketview;

import java.beans.PropertyChangeListener;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

import uk.ac.stfc.isis.ibex.model.UpdatedValue;

/**
 * A Matplotlib View targeting all current figures.
 */
public abstract class MatplotlibAllFiguresView {
	private Set<MatplotlibFigure> figures = new HashSet<>();
	private Composite parent;
	
	private PropertyChangeListener figuresChangedListener;
	private PropertyChangeListener urlListener;
	
	/**
	 * Construct this view.
	 * @param parent the parent
	 * @param part the part
	 */
	@PostConstruct
	public synchronized void createComposite(final Composite parent, final MPart part) {
		var layout = new GridLayout(1, true);
		layout.marginWidth = 0;
		layout.marginHeight = 0;
		parent.setLayout(layout);
		this.parent = parent;
		recreateFigures();
		figuresChangedListener = getFigures()
				.addUiThreadPropertyChangeListener(evt -> recreateFigures());
		urlListener = getConnectionUrl()
				.addUiThreadPropertyChangeListener(evt -> recreateFigures());
	}
	
	private synchronized void disposeFigures() {
		for (var f : figures) {
			f.dispose();
		}
		figures.clear();
	}
	
	private synchronized void recreateFigures() {
		disposeFigures();
		for (int figNum : getFigures().getValue()) {
			var figure = new MatplotlibFigure(parent, SWT.NONE, getConnectionUrl().getValue(), figNum);
			figure.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
			figures.add(figure);
		}
		parent.layout();
	}
	
	/**
	 * Destroy this view and it's child-views.
	 */
	@PreDestroy
	public void dispose() {
		getFigures().removePropertyChangeListener(figuresChangedListener);
		getConnectionUrl().removePropertyChangeListener(urlListener);
		disposeFigures();
	}
	
	protected abstract UpdatedValue<List<Integer>> getFigures();
	protected abstract UpdatedValue<String> getConnectionUrl();
	
}
