package uk.ac.stfc.isis.ibex.ui.moxas.views;

import javax.annotation.PostConstruct;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

import uk.ac.stfc.isis.ibex.configserver.Configurations;

/**
 * The view to be displayed in the Moxa perspective.
 */
public class MoxasView {

	Configurations control;

	/**
	 * Create the View part.
	 * 
	 * @param parent The SWT parent composite
	 */
	@PostConstruct
	public void createPartControl(Composite parent) {
		parent.setLayout(new GridLayout(1, true));
		control = Configurations.getInstance();
		MoxasViewModel iocControlViewModel = new MoxasViewModel(control);
		MoxaInfoPanel iocControlView = new MoxaInfoPanel(parent, SWT.FILL, iocControlViewModel);
		iocControlView.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

	}

}
