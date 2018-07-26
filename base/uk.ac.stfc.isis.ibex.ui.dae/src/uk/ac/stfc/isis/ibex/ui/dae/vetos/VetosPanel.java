
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

package uk.ac.stfc.isis.ibex.ui.dae.vetos;

import javax.annotation.PostConstruct;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;

import uk.ac.stfc.isis.ibex.ui.dae.DaeUI;
import uk.ac.stfc.isis.ibex.ui.dae.DaeViewModel;
import uk.ac.stfc.isis.ibex.ui.dae.widgets.MessageBox;

/**
 * The panel that gives information on the vetoes arriving from the DAE.
 */
public class VetosPanel {

	private MessageBox vetos;
    private DaeViewModel model;
	
    /**
     * The constructor for the panel.
     * 
     * @param parent
     *            The parent composite that the panel belongs to.
     * @param style
     *            The SWT style flags for the panel.
     */
	public VetosPanel() {
        model = DaeUI.getDefault().viewModel();
	}

    @PostConstruct
    public void createPart(Composite parent) {
        parent.setLayout(new FillLayout(SWT.HORIZONTAL));
		
        vetos = new MessageBox(parent, SWT.NONE);
		vetos.setTitle("Vetos");

        setModel(model);
	}
	
    /**
     * Set the view model from which the veto status information is obtained.
     * 
     * @param viewModel
     *            The view model which contains the veto status data.
     */
    private void setModel(DaeViewModel viewModel) {
		vetos.setModel(viewModel.vetos());
	}
}
