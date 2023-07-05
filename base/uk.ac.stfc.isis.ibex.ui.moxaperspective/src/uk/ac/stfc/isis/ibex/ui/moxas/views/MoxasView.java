package uk.ac.stfc.isis.ibex.ui.moxas.views;

import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.ExpandBar;
import org.eclipse.swt.widgets.ExpandItem;
import org.eclipse.ui.dialogs.FilteredTree;

import uk.ac.stfc.isis.ibex.configserver.Configurations;
import uk.ac.stfc.isis.ibex.configserver.MoxaMappings;

import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;

import javax.annotation.PostConstruct;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class MoxasView  {
	
	MoxaMappings control;

	@PostConstruct
    public void createPartControl(Composite parent) {
    	parent.setLayout(new GridLayout(1, true));
    	
    	control = Configurations.getInstance().moxaMappings();
    	
    			
    	MoxasViewModel iocControlViewModel = new MoxasViewModel(control);
    	MoxaInfoPanel iocControlView = new MoxaInfoPanel(parent, SWT.FILL, iocControlViewModel);
    	iocControlView.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
    	
    }

}
