package uk.ac.stfc.isis.ibex.ui.graphing.websocketview;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.logging.log4j.Logger;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import uk.ac.stfc.isis.ibex.logger.IsisLog;

/**
 * A Matplotlib View targeting all current figures.
 */
public class MatplotlibAllFiguresView {
	private static final Logger LOG = IsisLog.getLogger(MatplotlibSingleFigureView.class);
	private MatplotlibFigure figure;
	
	/**
	 * Construct this view.
	 * @param parent the parent
	 * @param part the part
	 */
	@PostConstruct
	public void createComposite(Composite parent, MPart part) {
		String figNumProp = part.getProperties().get("FigureNumber");
		try {
		    int figureNumber = Integer.parseInt(figNumProp);
		    figure = new MatplotlibFigure(parent, SWT.NONE, figureNumber);
		} catch (NumberFormatException e) {
			LOG.info(String.format("Cannot create figure with number %s", figNumProp));
		}
	}
	
	/**
	 * Destroy this view and it's child-views.
	 */
	@PreDestroy
	public void dispose() {
		if (figure != null && !figure.isDisposed()) {
			figure.dispose();
		}
		figure = null;
	}
}
