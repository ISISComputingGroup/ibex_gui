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

import java.util.ArrayList;
import java.util.List;

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
import org.eclipse.swt.widgets.Text;

import uk.ac.stfc.isis.ibex.dae.detectordiagnostics.DetectorDiagnosticsViewModel;
import uk.ac.stfc.isis.ibex.dae.detectordiagnostics.SpectraToDisplay;

/**
 * The panel containing the detector diagnostics table and controls.
 */
@SuppressWarnings("checkstyle:magicnumber")
public class DetectorDiagnosticsPanel extends Composite {
    
    private DetectorDiagnosticsViewModel model = DetectorDiagnosticsViewModel.getInstance();
    private DataBindingContext bindingContext = new DataBindingContext();
    private Combo comboSpectraTypeSelector;
    private Spinner spinnerPeriodSelector;
    private Spinner spinnerStartingSpectrumNumber;
    private Spinner spinnerNumberOfSpectra;
    private Text spinnerIntegralTimeRangeFrom;
    private Text spinnerIntegralTimeRangeTo;
    private Spinner spinnerMaximumFrames;
    
    private static final int MAX_NUMBER_OF_SPECTRA = 256;
    private static final int MAX_SPECTRA_NUMBER = 1000000;
    private static final int MAX_SPECTRA_PERIODS = Integer.MAX_VALUE;
    private static final int MAX_FRAMES = Integer.MAX_VALUE;
    
    /**
     * Constructor.
     * @param parent the parent
     */
    public DetectorDiagnosticsPanel(Composite parent) {
        super(parent, SWT.NONE);
        
        setLayout(new GridLayout(7, true));
        
        new Label(this, SWT.NONE).setText("Spectra to display");
        new Label(this, SWT.NONE).setText("Spectra periods");      
        new Label(this, SWT.NONE).setText("Starting spectrum number");        
        new Label(this, SWT.NONE).setText("Number of spectra");        
        new Label(this, SWT.NONE).setText("Integral lower limit");        
        new Label(this, SWT.NONE).setText("Integral upper limit");
        new Label(this, SWT.NONE).setText("Maximum frames");
        
        comboSpectraTypeSelector = new Combo(this, SWT.READ_ONLY); 
        comboSpectraTypeSelector.setItems(getDropdownMenuItemsArray());
        
                 
        spinnerPeriodSelector = new Spinner(this, SWT.BORDER);          
        spinnerPeriodSelector.setMinimum(0);
        spinnerPeriodSelector.setMaximum(MAX_SPECTRA_PERIODS);
        
        spinnerStartingSpectrumNumber = new Spinner(this, SWT.BORDER);          
        spinnerStartingSpectrumNumber.setMinimum(0);
        spinnerStartingSpectrumNumber.setMaximum(MAX_SPECTRA_NUMBER);
        
        spinnerNumberOfSpectra = new Spinner(this, SWT.BORDER); 
        spinnerNumberOfSpectra.setMinimum(0);
        spinnerNumberOfSpectra.setMaximum(MAX_NUMBER_OF_SPECTRA);
        
        spinnerIntegralTimeRangeFrom = new Text(this, SWT.BORDER);    
        spinnerIntegralTimeRangeFrom.addVerifyListener(new NumericalVerifyListener());
        
        spinnerIntegralTimeRangeTo = new Text(this, SWT.BORDER); 
        spinnerIntegralTimeRangeTo.addVerifyListener(new NumericalVerifyListener());
        
        spinnerMaximumFrames = new Spinner(this, SWT.BORDER); 
        spinnerMaximumFrames.setMinimum(0);
        spinnerMaximumFrames.setMaximum(MAX_FRAMES);
        
        DetectorDiagnosticsTable table = new DetectorDiagnosticsTable(parent, SWT.NONE, SWT.NONE);
        table.bind();
        
        GridData layout = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
        table.setLayoutData(layout);
        
        bind();
 
    }
    
    private void bind() {
        bindingContext.bindValue(WidgetProperties.singleSelectionIndex().observe(comboSpectraTypeSelector), BeanProperties.value("spectraType").observe(model));
        bindingContext.bindValue(WidgetProperties.enabled().observe(comboSpectraTypeSelector), BeanProperties.value("diagnosticsEnabled").observe(model));
        
        bindingContext.bindValue(WidgetProperties.selection().observe(spinnerPeriodSelector), BeanProperties.value("period").observe(model)); 
        bindingContext.bindValue(WidgetProperties.enabled().observe(spinnerPeriodSelector), BeanProperties.value("diagnosticsEnabled").observe(model));
        
        bindingContext.bindValue(WidgetProperties.selection().observe(spinnerStartingSpectrumNumber), BeanProperties.value("startingSpectrumNumber").observe(model)); 
        bindingContext.bindValue(WidgetProperties.enabled().observe(spinnerStartingSpectrumNumber), BeanProperties.value("diagnosticsEnabled").observe(model));
        
        bindingContext.bindValue(WidgetProperties.selection().observe(spinnerNumberOfSpectra), BeanProperties.value("numberOfSpectra").observe(model)); 
        bindingContext.bindValue(WidgetProperties.enabled().observe(spinnerNumberOfSpectra), BeanProperties.value("diagnosticsEnabled").observe(model)); 
        
        bindingContext.bindValue(WidgetProperties.text(SWT.Modify).observe(spinnerIntegralTimeRangeFrom), BeanProperties.value("integralTimeRangeFrom").observe(model));  
        bindingContext.bindValue(WidgetProperties.enabled().observe(spinnerIntegralTimeRangeFrom), BeanProperties.value("diagnosticsEnabled").observe(model));
        
        bindingContext.bindValue(WidgetProperties.text(SWT.Modify).observe(spinnerIntegralTimeRangeTo), BeanProperties.value("integralTimeRangeTo").observe(model)); 
        bindingContext.bindValue(WidgetProperties.enabled().observe(spinnerIntegralTimeRangeTo), BeanProperties.value("diagnosticsEnabled").observe(model)); 
        
        bindingContext.bindValue(WidgetProperties.selection().observe(spinnerMaximumFrames), BeanProperties.value("maxFrames").observe(model));  
        bindingContext.bindValue(WidgetProperties.enabled().observe(spinnerMaximumFrames), BeanProperties.value("diagnosticsEnabled").observe(model)); 
    }
    
    private String[] getDropdownMenuItemsArray() {
        List<String> result = new ArrayList<>();
        for (SpectraToDisplay s : SpectraToDisplay.values()) {
            result.add(s.toString());
        }
        return result.toArray(new String[result.size()]);
    }

}
