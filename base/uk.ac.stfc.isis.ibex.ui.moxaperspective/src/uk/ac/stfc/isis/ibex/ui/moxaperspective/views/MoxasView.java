package uk.ac.stfc.isis.ibex.ui.moxaperspective.views;

import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.ExpandBar;
import org.eclipse.swt.widgets.ExpandItem;
import org.eclipse.ui.dialogs.FilteredTree;

import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;

import javax.annotation.PostConstruct;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class MoxasView  {

	@PostConstruct
    public void createPartControl(Composite parent) {
    	parent.setLayout(new GridLayout(1, true));
    	
    			
    	MoxasViewModel iocControlViewModel = new MoxasViewModel();
    	MoxaInfoPanel iocControlView = new MoxaInfoPanel(parent, SWT.FILL, iocControlViewModel);
    	iocControlView.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
    	
    }

}
