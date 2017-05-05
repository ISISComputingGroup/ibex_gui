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

import java.util.Arrays;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;

import uk.ac.stfc.isis.ibex.dae.detectordiagnostics.DetectorDiagnosticsModel;
import uk.ac.stfc.isis.ibex.dae.detectordiagnostics.SpectraToDisplay;

/**
 *
 */
public class DetectorDiagnosticsPanel extends Composite {
    
    private DetectorDiagnosticsModel model = DetectorDiagnosticsModel.getInstance();
    private DataBindingContext bindingContext = new DataBindingContext();
    
    public DetectorDiagnosticsPanel(Composite parent){
        super(parent, SWT.NONE);
        
        setLayout(new GridLayout(7, false));
        
        Label label1 = new Label(this, SWT.NONE);
        label1.setText("Spectra to display");
        
        Label label2 = new Label(this, SWT.NONE);
        label2.setText("Spectra periods");
        
        Label label3 = new Label(this, SWT.NONE);
        label3.setText("Starting spectrum number");
        
        Label label4 = new Label(this, SWT.NONE);
        label4.setText("Number of spectra to display");
        
        Label label5 = new Label(this, SWT.NONE);
        label5.setText("Integral time range (from)");
        
        Label label6 = new Label(this, SWT.NONE);
        label6.setText("Integral time range (to)");
        
        Label label7 = new Label(this, SWT.NONE);
        label7.setText("Maximum frames for count rate");
        
        Combo comboSpectraTypeSelector = new Combo(this, SWT.READ_ONLY); 
        comboSpectraTypeSelector.setItems(
                Arrays.asList(SpectraToDisplay.values())
                .stream()
                .map(n -> n.toString())
                .toArray(String[]::new));
        
        bindingContext.bindValue(WidgetProperties.singleSelectionIndex().observe(comboSpectraTypeSelector), BeanProperties.value("spectraType").observe(model));  
        comboSpectraTypeSelector.setEnabled(true);
        
        Spinner spinnerPeriodSelector = new Spinner(this, SWT.BORDER); 
        bindingContext.bindValue(WidgetProperties.selection().observe(spinnerPeriodSelector), BeanProperties.value("period").observe(model));  
        spinnerPeriodSelector.setEnabled(true);
        spinnerPeriodSelector.setMinimum(0);
        spinnerPeriodSelector.setMaximum(Short.MAX_VALUE);
        
        Spinner spinnerStartingSpectrumNumber = new Spinner(this, SWT.BORDER); 
        bindingContext.bindValue(WidgetProperties.selection().observe(spinnerStartingSpectrumNumber), BeanProperties.value("startingSpectrumNumber").observe(model));  
        spinnerStartingSpectrumNumber.setEnabled(true);
        spinnerStartingSpectrumNumber.setMinimum(0);
        spinnerStartingSpectrumNumber.setMaximum(Integer.MAX_VALUE);
        
        Spinner spinnerNumberOfSpectra = new Spinner(this, SWT.BORDER); 
        bindingContext.bindValue(WidgetProperties.selection().observe(spinnerNumberOfSpectra), BeanProperties.value("numberOfSpectra").observe(model));  
        spinnerNumberOfSpectra.setEnabled(true);
        spinnerNumberOfSpectra.setMinimum(0);
        spinnerNumberOfSpectra.setMaximum(Byte.MAX_VALUE * 2);
        
        Spinner spinnerIntegralTimeRangeFrom = new Spinner(this, SWT.BORDER); 
        bindingContext.bindValue(WidgetProperties.selection().observe(spinnerIntegralTimeRangeFrom), BeanProperties.value("integralTimeRangeFrom").observe(model));  
        spinnerIntegralTimeRangeFrom.setEnabled(true);
        spinnerIntegralTimeRangeFrom.setMinimum(0);
        spinnerIntegralTimeRangeFrom.setMaximum(Integer.MAX_VALUE);
        
        Spinner spinnerIntegralTimeRangeTo = new Spinner(this, SWT.BORDER); 
        bindingContext.bindValue(WidgetProperties.selection().observe(spinnerIntegralTimeRangeTo), BeanProperties.value("integralTimeRangeTo").observe(model));  
        spinnerIntegralTimeRangeTo.setEnabled(true);
        spinnerIntegralTimeRangeTo.setMinimum(0);
        spinnerIntegralTimeRangeTo.setMaximum(Integer.MAX_VALUE);
        
        Spinner spinnerMaximumFrames = new Spinner(this, SWT.BORDER); 
        bindingContext.bindValue(WidgetProperties.selection().observe(spinnerMaximumFrames), BeanProperties.value("maxFrames").observe(model));  
        spinnerMaximumFrames.setEnabled(true);
        spinnerMaximumFrames.setMinimum(0);
        spinnerMaximumFrames.setMaximum(Integer.MAX_VALUE);
               
        Label lblSpectraTable = new Label(parent, SWT.NONE);
        lblSpectraTable.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1));
        lblSpectraTable.setText("Spectra:");
        
        Table table = new Table(parent, SWT.NONE, SWT.NONE);
        table.bind();
        
        GridData layout = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
        table.setLayoutData(layout);

        
    }

}
