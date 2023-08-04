package uk.ac.stfc.isis.ibex.ui.moxas.views;

import javax.annotation.PostConstruct;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

import uk.ac.stfc.isis.ibex.configserver.Configurations;

public class MoxasView  {
	
	Configurations control;

	@PostConstruct
    public void createPartControl(Composite parent) {
    	parent.setLayout(new GridLayout(1, true));
    	control = Configurations.getInstance();
    	MoxasViewModel iocControlViewModel = new MoxasViewModel(control);
    	MoxaInfoPanel iocControlView = new MoxaInfoPanel(parent, SWT.FILL, iocControlViewModel);
    	iocControlView.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
    	
    }

}
