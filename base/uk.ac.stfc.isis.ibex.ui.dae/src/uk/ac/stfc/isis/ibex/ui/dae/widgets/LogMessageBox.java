
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

package uk.ac.stfc.isis.ibex.ui.dae.widgets;

import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import uk.ac.stfc.isis.ibex.log.ILogMessageProducer;
import uk.ac.stfc.isis.ibex.log.message.LogMessageFields;
import uk.ac.stfc.isis.ibex.ui.log.filter.LogMessageFilter;
import uk.ac.stfc.isis.ibex.ui.log.widgets.LogDisplay;
import uk.ac.stfc.isis.ibex.ui.log.widgets.LogDisplayModel;

/**
 * A composite which contains a view of the IOC log messages produced by the
 * DAE.
 */
public class LogMessageBox extends Composite {
	private Label title;
	private LogDisplay logDisplay;
	
    /**
     * The constructor for the composite.
     * 
     * @param parent
     *            The parent composite that the panel belongs to.
     * @param style
     *            The SWT style flags for the panel.
     */
	public LogMessageBox(Composite parent, int style) {
		super(parent, style);
		setLayout(new GridLayout(1, false));
		
		title = new Label(this, SWT.NONE);
		title.setText("Log Messages:");
        title.setFont(JFaceResources.getFontRegistry().getBold(JFaceResources.DEFAULT_FONT));
				
		logDisplay = new LogDisplay(this, null);
		logDisplay.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		String daeName = "ISISDAE_01";
		
		LogMessageFilter filter = new LogMessageFilter(LogMessageFields.CLIENT_NAME, daeName, false);
		
		logDisplay.addMessageFilter(filter);
	}
	
    /**
     * Set the model to obtain the log messages from.
     * 
     * @param model
     *            A log message producer that gives messages from the IOCs.
     */
	public void setModel(ILogMessageProducer model) {
		// Create model and subscribe to updates from Log
		LogDisplayModel logDisplayModel = new LogDisplayModel(model);
		logDisplay.setModel(logDisplayModel);
	}
}