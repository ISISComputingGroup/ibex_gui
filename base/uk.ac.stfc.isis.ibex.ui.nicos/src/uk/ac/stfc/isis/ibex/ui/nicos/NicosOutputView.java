
/*
* This file is part of the ISIS IBEX application.
* Copyright (C) 2012-2016 Science & Technology Facilities Council.
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

package uk.ac.stfc.isis.ibex.ui.nicos;

import javax.annotation.PostConstruct;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import uk.ac.stfc.isis.ibex.nicos.Nicos;
import uk.ac.stfc.isis.ibex.nicos.NicosModel;
import uk.ac.stfc.isis.ibex.ui.nicos.models.ConnectionStatusConverter;

/**
 * The main view for the NICOS Script Output view.
 */
@SuppressWarnings("checkstyle:magicnumber")
public class NicosOutputView {
	
	/**
	 * The public ID of this class.
	 */
	public static final String ID = "uk.ac.stfc.isis.ibex.ui.nicos.NicosView";
    
    private static final int FIXED_WIDTH = 750;
    private static final int FIXED_HEIGHT = 225;
	
    private DataBindingContext bindingContext = new DataBindingContext();
	
    private NicosModel model;


	/**
	 * The default constructor for the view.
	 */
	public NicosOutputView() {
        model = Nicos.getDefault().getModel();
	}

	@PostConstruct
	public void createPartControl(Composite parent) {
		ScrolledComposite scrolledComposite = new ScrolledComposite(parent, SWT.H_SCROLL | SWT.V_SCROLL);
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);
		Composite nicosComposite = new Composite(scrolledComposite, SWT.NONE);
		scrolledComposite.setContent(nicosComposite);
        scrolledComposite.setMinSize(new Point(FIXED_WIDTH, FIXED_HEIGHT));
		
		GridLayout glParent = new GridLayout(2, false);
		glParent.marginHeight = 10;
		glParent.marginWidth = 10;
		glParent.marginBottom = 57;
		nicosComposite.setLayout(glParent);

        // Connection info
        Composite connectionGrp = new Composite(nicosComposite, SWT.NONE);
        connectionGrp.setLayoutData(new GridData(SWT.FILL, SWT.BOTTOM, true, false, 2, 1));
        GridLayout connLayout = new GridLayout(2, false);
        connLayout.marginHeight = 10;
        connLayout.marginWidth = 10;
        connectionGrp.setLayout(connLayout);

        Label lblConnectionStatus = new Label(connectionGrp, SWT.NONE);
        GridData connStatusLayoutData = new GridData(SWT.BEGINNING, SWT.FILL, false, true, 1, 1);
        connStatusLayoutData.widthHint = 100;
        lblConnectionStatus.setLayoutData(connStatusLayoutData);
        bindingContext.bindValue(WidgetProperties.text().observe(lblConnectionStatus),
                BeanProperties.value("connectionStatus").observe(model), null, new ConnectionStatusConverter());

        Label lblConnectionError = new Label(connectionGrp, SWT.NONE);
        lblConnectionError.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
        bindingContext.bindValue(WidgetProperties.text().observe(lblConnectionError),
                BeanProperties.value("connectionErrorMessage").observe(model));
		
		
		StyledText txtOutput = new StyledText(nicosComposite, SWT.BORDER);
		txtOutput.setEditable(false);
		txtOutput.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
	}

}
