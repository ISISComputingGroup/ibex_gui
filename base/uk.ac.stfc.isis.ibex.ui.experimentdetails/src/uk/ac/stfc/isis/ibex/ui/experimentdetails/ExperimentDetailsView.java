package uk.ac.stfc.isis.ibex.ui.experimentdetails;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

public class ExperimentDetailsView extends ViewPart {
	public static final String ID = "uk.ac.stfc.isis.ibex.ui.experimentdetails.experimentdetailsview";
		
	public ExperimentDetailsView() {
	}

	@Override
	public void createPartControl(Composite parent) {
		parent.setLayout(new FillLayout(SWT.HORIZONTAL));
		ExperimentDetailsPanel experimentDetails = new ExperimentDetailsPanel(parent, SWT.NONE);
	}

	@Override
	public void setFocus() {

	}

}
