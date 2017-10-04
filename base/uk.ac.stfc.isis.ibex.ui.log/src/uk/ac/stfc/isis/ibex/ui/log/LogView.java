
/*
* This file is part of the ISIS IBEX application.
* Copyright (C) 2012-2015 Science & Technology Facilities Council.
* All rights reserved.
*
* This program is distributed in the hope that it will be useful.
* This program and the accompanying materials are made available under the
* terms of the Eclipse Public License v1.0 which accompanies this distribution.
* EXCEPT AS EXPRESSLY SET FORTH IN THE ECLIPSE PUBLIC LICENSE V1.0, THE PROGRAM 
* AND ACCOMPANYING MATERIALS ARE PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES 
* OR CONDITIONS OF ANY KIND.  See the Eclipse Public License v1.0 for more details.
*
* You should have received a copy of the Eclipse Public License v1.0
* along with this program; if not, you can obtain a copy from
* https://www.eclipse.org/org/documents/epl-v10.php or 
* http://opensource.org/licenses/eclipse-1.0.php
*/

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

import javax.annotation.PostConstruct;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.layout.FillLayout;

import uk.ac.stfc.isis.ibex.ui.log.widgets.LogDisplay;
import uk.ac.stfc.isis.ibex.ui.log.widgets.LogDisplayModel;

/**
 * An IOC Log View control.
 */
@SuppressWarnings("checkstyle:magicnumber")
public class LogView {
	
	private LogViewModel viewModel;
	private LogDisplayModel logDisplayModel;
		
	/**
	 * Instantiates a new log view.
	 */
	public LogView() {
		viewModel = Activator.getDefault().viewModel();
		logDisplayModel = new LogDisplayModel(viewModel.getMessageProducer());
	}
	
	/**
	 * Create contents of the view part.
	 * 
	 * @param parent the composite in which this control should live 
	 */
	@PostConstruct
	public void createPartControl(Composite parent) {		
		parent.setLayout(new FillLayout(SWT.VERTICAL | SWT.HORIZONTAL));
		
		
		ScrolledComposite container = new ScrolledComposite(parent, SWT.H_SCROLL | SWT.V_SCROLL);
		
		container.setLayout(new FillLayout(SWT.HORIZONTAL | SWT.VERTICAL));
		container.setMinSize(700, 300);
		container.setExpandHorizontal(true);
		container.setExpandVertical(true);
		
		LogDisplay logDisplay = new LogDisplay(container, logDisplayModel);
		container.setContent(logDisplay);
	}

}
