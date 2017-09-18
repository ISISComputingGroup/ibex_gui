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
package uk.ac.stfc.isis.ibex.ui.experimentdetails;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.inject.Inject;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;

/**
 *
 */
public class SampleParametersPanel {

    private ParametersTable sampleParameters;
    private Label lblSampleParameters;
    private final ViewModel viewModel;

    /**
     * @param parent
     */
    @Inject
    public SampleParametersPanel(Composite parent) {

        viewModel = ViewModel.getInstance();
        
        sampleParameters = new ParametersTable(parent);
        GridData gdSampleParameters = new GridData(SWT.LEFT, SWT.FILL, false, true, 4, 1);
        gdSampleParameters.widthHint = 600;
        gdSampleParameters.minimumWidth = 600;
        gdSampleParameters.minimumHeight = 200;
        gdSampleParameters.heightHint = 200;
        sampleParameters.setLayoutData(gdSampleParameters);
        sampleParameters.enableEditing(viewModel.rbNumber.canSetText().getValue());
        
        updateSampleParameters();
        viewModel.model.addPropertyChangeListener(new PropertyChangeListener() {        
            @Override
            public void propertyChange(PropertyChangeEvent arg0) {
                updateSampleParameters();
                
            }
        });

        viewModel.rbNumber.canSetText().addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent event) {
                boolean canSet = (boolean) event.getNewValue();
                sampleParameters.enableEditing(canSet);
            }
        });
        new Label(parent, SWT.NONE);
    }

    protected void updateSampleParameters() {
        Display.getDefault().asyncExec(new Runnable() {
            @Override
            public void run() {
                sampleParameters.setRows(viewModel.model.getSampleParameters());
            }
        });
    }
}
