package uk.ac.stfc.isis.ibex.ui.goniometer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

public class GoniometerView extends ViewPart {

	public GoniometerView() {
	}	
	
	public static final String ID = "uk.ac.stfc.isis.ibex.ui.goniometer.GoniometerView";	
	
	@Override
	public void createPartControl(Composite parent) {
		GoniometerPanel goniometer = new GoniometerPanel(parent, SWT.NONE);
	}

	@Override
	public void setFocus() {

	}

}
