package uk.ac.stfc.isis.ibex.ui.graphing.websocketview;

import javax.annotation.PostConstruct;

import org.apache.logging.log4j.Logger;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import uk.ac.stfc.isis.ibex.logger.IsisLog;

public class MatplotlibSingleFigureView {
	private static final Logger LOG = IsisLog.getLogger(MatplotlibSingleFigureView.class);
	
	@PostConstruct
	public void createComposite(Composite parent, MPart part) {
		String figNumProp = part.getProperties().get("FigureNumber");
		try {
		    int figureNumber = Integer.parseInt(figNumProp);
		    new MatplotlibFigure(parent, SWT.NONE, figureNumber);
		} catch (NumberFormatException e) {
			LOG.info(String.format("Cannot create figure with number %s", figNumProp));
		}
	}
}
