package uk.ac.stfc.isis.ibex.synopticlike;

import javax.annotation.PostConstruct;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

public class OpiPart {
	
	Text label;
	
	@PostConstruct
	public void createComposite(Composite parent) {
		parent.setLayout(new GridLayout(1, false));
		
		label = new Text(parent, SWT.BORDER);
		label.setMessage("Some OPI");
		label.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
	}

}
