package uk.ac.stfc.isis.ibex.ui.rotatingbench;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

public class RotatingBenchView extends ViewPart {
	
	public RotatingBenchView() {
	}

	public static final String ID = "uk.ac.stfc.isis.ibex.ui.rotatingbench.RotatingBenchView";
	
	@Override
	public void createPartControl(Composite parent) {
		RotatingBenchPanel rotatingBench = new RotatingBenchPanel(parent, SWT.NONE);
	}

	@Override
	public void setFocus() {
		
	}

}
