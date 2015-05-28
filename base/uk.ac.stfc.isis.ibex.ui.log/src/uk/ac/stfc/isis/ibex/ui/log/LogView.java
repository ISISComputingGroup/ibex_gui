/*
 * Copyright (C) 2013-2014 Research Councils UK (STFC)
 *
 * This file is part of the Instrument Control Project at ISIS.
 *
 * This code and information are provided "as is" without warranty of any 
 * kind, either expressed or implied, including but not limited to the
 * implied warranties of merchantability and/or fitness for a particular 
 * purpose.
 */
package uk.ac.stfc.isis.ibex.ui.log;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.swt.layout.FillLayout;

import uk.ac.stfc.isis.ibex.ui.log.widgets.LogDisplay;
import uk.ac.stfc.isis.ibex.ui.log.widgets.LogDisplayModel;

public class LogView extends ViewPart {
	public static final String ID = "uk.ac.stfc.isis.ibex.ui.log.views.LogView"; //$NON-NLS-1$
	
	private LogViewModel viewModel;
	private LogDisplayModel logDisplayModel;
		
	public LogView() {
		setPartName("LogView");
		viewModel = Activator.getDefault().viewModel();
		logDisplayModel = new LogDisplayModel(viewModel.getMessageProducer());
	}
	
	/**
	 * Create contents of the view part.
	 */
	@Override
	public void createPartControl(Composite parent) {		
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(new FillLayout(SWT.HORIZONTAL));
		
		new LogDisplay(container, logDisplayModel);
	}

	@Override
	public void setFocus() {
		// Set the focus
	}
}
