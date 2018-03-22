 /*
 * This file is part of the ISIS IBEX application.
 * Copyright (C) 2012-2017 Science & Technology Facilities Council.
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

import javax.annotation.PostConstruct;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

import uk.ac.stfc.isis.ibex.dae.detectordiagnostics.IDetectorDiagnosticsViewModelBinding;
import uk.ac.stfc.isis.ibex.dae.detectordiagnostics.SpectraToDisplay;
import uk.ac.stfc.isis.ibex.ui.dae.DaeUI;

/**
 * The panel containing the detector diagnostics table and controls.
 */
@SuppressWarnings("checkstyle:magicnumber")
public class DetectorDiagnosticsPanel {
    
    private DataBindingContext bindingContext = new DataBindingContext();
    private Combo comboSpectraTypeSelector;
    private Spinner spinnerPeriodSelector;
    private Spinner spinnerStartingSpectrumNumber;
    private Spinner spinnerNumberOfSpectra;
    private Text spinnerIntegralTimeRangeFrom;
    private Text spinnerIntegralTimeRangeTo;
    private Spinner spinnerMaximumFrames;

    private static final int FIXED_WIDTH = 800;
    private static final int FIXED_HEIGHT = 300;
    private static final int MAX_NUMBER_OF_SPECTRA = 256;
    private static final int MAX_SPECTRA_NUMBER = 1000000;
    private static final int MAX_SPECTRA_PERIODS = Integer.MAX_VALUE;
    private static final int MAX_FRAMES = Integer.MAX_VALUE;
    private Label errorLabel;
    private DetectorDiagnosticsViewModel model;
    
    /**
     * Constructor.
     */
    public DetectorDiagnosticsPanel() {
        model = DaeUI.getDefault().viewModel().detectorDiagnostics();
    }

    /**
     * Instantiates this viewpart.
     * 
     * @param parent The parent composite obtained from the eclipse context
     */
    @PostConstruct
    public void createPart(Composite parent) {
        ScrolledComposite scrolled = new ScrolledComposite(parent, SWT.H_SCROLL | SWT.V_SCROLL);
        scrolled.setExpandHorizontal(true);
        scrolled.setExpandVertical(true);
        scrolled.setMinSize(FIXED_WIDTH, FIXED_HEIGHT);

        Composite content = new Composite(scrolled, SWT.NONE);
        content.setLayout(new GridLayout(1, false));
        scrolled.setContent(content);
		
        Composite container = new Composite(content, SWT.NONE);
        GridData layoutData = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
        container.setLayoutData(layoutData);
        container.setLayout(new GridLayout(7, true));
        
        GridData centeredGridItem = new GridData(SWT.CENTER, SWT.CENTER, true, true);
        centeredGridItem.minimumHeight = 20;
        centeredGridItem.minimumWidth = 105;
        
        createLabel(container, "Spectra to display", centeredGridItem);
        createLabel(container, "Spectra periods", centeredGridItem);      
        createLabel(container, "Starting spectrum", centeredGridItem);        
        createLabel(container, "Number of spectra", centeredGridItem);        
        createLabel(container, "Integral lower limit", centeredGridItem);        
        createLabel(container, "Integral upper limit", centeredGridItem);
        createLabel(container, "Maximum frames", centeredGridItem);
        
        comboSpectraTypeSelector = new Combo(container, SWT.READ_ONLY); 
        comboSpectraTypeSelector.setItems(getDropdownMenuItemsArray());
        comboSpectraTypeSelector.setLayoutData(centeredGridItem);
                 
        spinnerPeriodSelector = new Spinner(container, SWT.BORDER);          
        spinnerPeriodSelector.setMinimum(0);
        spinnerPeriodSelector.setMaximum(MAX_SPECTRA_PERIODS);
        spinnerPeriodSelector.setLayoutData(centeredGridItem);
        
        spinnerStartingSpectrumNumber = new Spinner(container, SWT.BORDER);          
        spinnerStartingSpectrumNumber.setMinimum(0);
        spinnerStartingSpectrumNumber.setMaximum(MAX_SPECTRA_NUMBER);
        spinnerStartingSpectrumNumber.setLayoutData(centeredGridItem);
        
        spinnerNumberOfSpectra = new Spinner(container, SWT.BORDER); 
        spinnerNumberOfSpectra.setMinimum(0);
        spinnerNumberOfSpectra.setMaximum(MAX_NUMBER_OF_SPECTRA);
        spinnerNumberOfSpectra.setLayoutData(centeredGridItem);
        
        spinnerIntegralTimeRangeFrom = new Text(container, SWT.BORDER);    
        spinnerIntegralTimeRangeFrom.addVerifyListener(new NumericalVerifyListener());
        spinnerIntegralTimeRangeFrom.setLayoutData(centeredGridItem);
        
        spinnerIntegralTimeRangeTo = new Text(container, SWT.BORDER); 
        spinnerIntegralTimeRangeTo.addVerifyListener(new NumericalVerifyListener());
        spinnerIntegralTimeRangeTo.setLayoutData(centeredGridItem);
        
        spinnerMaximumFrames = new Spinner(container, SWT.BORDER); 
        spinnerMaximumFrames.setMinimum(0);
        spinnerMaximumFrames.setMaximum(MAX_FRAMES);
        spinnerMaximumFrames.setLayoutData(centeredGridItem);
        
        errorLabel = new Label(content, SWT.LEAD);
        GridData labelLayoutData = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
        errorLabel.setLayoutData(labelLayoutData);
        errorLabel.setForeground(SWTResourceManager.getColor(SWT.COLOR_RED));

        DetectorDiagnosticsTable table = new DetectorDiagnosticsTable(content, SWT.NONE, SWT.NONE);
        table.bind();
        
        GridData layout = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
        table.setLayoutData(layout);
        
        this.setModel(model);
 
    }
    
    private void createLabel(Composite parent, String text, GridData layout) {
        Label label = new Label(parent, SWT.NONE);
        label.setText(text);
        label.setLayoutData(layout);
        label.setAlignment(SWT.CENTER);
    }
    
    /**
     * Set the model to bind to for the panel.
     * 
     * @param model
     *            the detector diagnostics model
     */
    private void setModel(IDetectorDiagnosticsViewModelBinding model) {
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
        bindingContext.bindValue(WidgetProperties.enabled().observe(spinnerMaximumFrames),
                BeanProperties.value("diagnosticsEnabled").observe(model));

        bindingContext.bindValue(SWTObservables.observeText(errorLabel),
                BeanProperties.value("writeToEnableDiagnosticError").observe(model));

    }
    
    private String[] getDropdownMenuItemsArray() {
        List<String> result = new ArrayList<>();
        for (SpectraToDisplay s : SpectraToDisplay.values()) {
            result.add(s.toString());
        }
        return result.toArray(new String[result.size()]);
    }

}
