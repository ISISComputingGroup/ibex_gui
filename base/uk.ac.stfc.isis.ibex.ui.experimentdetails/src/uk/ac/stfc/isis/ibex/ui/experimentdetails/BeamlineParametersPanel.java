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

package uk.ac.stfc.isis.ibex.ui.experimentdetails;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.annotation.PostConstruct;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

/**
 *	Panel for Beamline parameters.
 */
public class BeamlineParametersPanel {

    private ParametersTable beamParameters;
    private final ViewModel viewModel;

    /**
     * Instantiates a new beamline parameters panel.
     */
    public BeamlineParametersPanel() {
    	viewModel = ViewModel.getInstance();
    }
    
    /**
     * Creates controls.
     * 
     * @param parent parent composite
     */
    @PostConstruct
    public void cCreateControls(Composite parent) {

        beamParameters = new ParametersTable(parent, SWT.BEGINNING);
        beamParameters.enableEditing(viewModel.rbNumber.canSetText().getValue());

        updateBeamParameters();
        viewModel.model.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent arg0) {
                updateBeamParameters();
            }
        });

        viewModel.rbNumber.canSetText().addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent event) {
                boolean canSet = (boolean) event.getNewValue();
                beamParameters.enableEditing(canSet);
            }
        });
    }

    /**
     * Update beam parameters from the model (uses the GUI thread).
     */
    protected void updateBeamParameters() {
        Display.getDefault().asyncExec(new Runnable() {
            @Override
            public void run() {
                beamParameters.setRows(viewModel.model.getBeamParameters());
            }
        });
    }
}
