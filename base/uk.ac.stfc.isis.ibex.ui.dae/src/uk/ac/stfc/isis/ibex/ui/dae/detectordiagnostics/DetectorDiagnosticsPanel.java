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

/**
 * 
 */
package uk.ac.stfc.isis.ibex.ui.dae.detectordiagnostics;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import uk.ac.stfc.isis.ibex.dae.detectordiagnostics.DetectorDiagnosticsModel;
import uk.ac.stfc.isis.ibex.model.Action;
import uk.ac.stfc.isis.ibex.ui.dae.run.ActionButton;

/**
 *
 */
public class DetectorDiagnosticsPanel extends Composite {
    
    private DetectorDiagnosticsViewModel viewModel;
    
    public DetectorDiagnosticsPanel(Composite parent){
        super(parent, SWT.NONE);
        
        setLayout(new FillLayout(SWT.HORIZONTAL));
        

        
        ActionButton button = new ActionButton(this, SWT.CENTER, new Action(){

            @Override
            public void execute() {
                DetectorDiagnosticsModel.getInstance().setRange(0, 20);
            }
            
        });  
        button.setText("hi");
        button.setEnabled(true);
                
        Label lblSpectraTable = new Label(parent, SWT.NONE);
        lblSpectraTable.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1));
        lblSpectraTable.setText("Spectra:");
        
        Table table = new Table(parent, SWT.NONE, SWT.NONE);
        
        GridData layout = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
        table.setLayoutData(layout);

        
    }

    /**
     * Sets a new view model to use.
     * 
     * @param viewModel the new view model to use
     */
    public void setModel(DetectorDiagnosticsViewModel viewModel) {
        this.viewModel = viewModel;
    }

}
