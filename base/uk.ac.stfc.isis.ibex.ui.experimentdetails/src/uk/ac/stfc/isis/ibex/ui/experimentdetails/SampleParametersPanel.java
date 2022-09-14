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
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

/**
 * The panel that shows sample parameters.
 */
public class SampleParametersPanel {

    private ParametersTable sampleParameters;
    private final ExperimentDetailsViewModel viewModel;

    /**
     * Create the panel.
     * @param parent The parent composite that this panel is within.
     */
    @Inject
    public SampleParametersPanel(Composite parent) {

        viewModel = ExperimentDetailsViewModel.getInstance();
        
        sampleParameters = new ParametersTable(parent, SWT.BEGINNING);
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
    }

    /**
     * Updates the UI of the sample parameters table.
     */
    protected void updateSampleParameters() {
        Display.getDefault().asyncExec(new Runnable() {
            @Override
            public void run() {
                sampleParameters.setRows(viewModel.model.getSampleParameters());
            }
        });
    }
}
