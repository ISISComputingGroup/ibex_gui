package uk.ac.stfc.isis.ibex.ui.graphing.websocketview;

import java.beans.PropertyChangeListener;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.logging.log4j.Logger;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

import uk.ac.stfc.isis.ibex.logger.IsisLog;

/**
 * A Matplotlib View targeting all current figures.
 */
public class MatplotlibAllFiguresView {
	private static final Logger LOG = IsisLog.getLogger(MatplotlibAllFiguresView.class);
	private Set<MatplotlibFigure> figures = new HashSet<>();
	private Composite parent;
	
	private PropertyChangeListener primaryFiguresChangedListener;
	
	/**
	 * Construct this view.
	 * @param parent the parent
	 * @param part the part
	 */
	@PostConstruct
	public void createComposite(final Composite parent, final MPart part) {
		parent.setLayout(new GridLayout(1, true));
		this.parent = parent;
		recreateFigures();
		primaryFiguresChangedListener = Activator.getPrimaryFigures()
				.addUiThreadPropertyChangeListener(evt -> recreateFigures());
	}
	
	private void disposeFigures() {
		for (var f : figures) {
			f.dispose();
		}
		figures.clear();
	}
	
	private void recreateFigures() {
		LOG.info("recreating figures");
		disposeFigures();
		for (int figNum : Activator.getPrimaryFigures().getValue()) {
			var figure = new MatplotlibFigure(parent, SWT.NONE, figNum);
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
		Activator.getPrimaryFigures().removePropertyChangeListener(primaryFiguresChangedListener);
		disposeFigures();
	}
}
