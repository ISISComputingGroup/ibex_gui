
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

package uk.ac.stfc.isis.ibex.ui.dae.spectra;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;

import uk.ac.stfc.isis.ibex.dae.spectra.UpdatableSpectrum;

/**
 * Spectrum View - single graph with associated text controls.
 */
public class SpectrumView extends Composite {
	
    private Spinner number;
    private Spinner period;
	private SpectrumPlot spectrumFigure;

	private DataBindingContext bindingContext;
	private UpdatableSpectrum spectrum;
	
    private static final int MAXIMUM_MONITOR_SPECTRUM = 1000000;
    private static final int SPINNER_WIDTH = 40;
    
    /**
     * Instantiates a new spectrum view.
     *
     * @param parent the parent
     * @param style the style
     */
    @SuppressWarnings({ "checkstyle:magicnumber", "checkstyle:localvariablename" })
	public SpectrumView(Composite parent, int style) {
		super(parent, style);
        setLayout(new GridLayout(6, false));
		
		Label lblSpectrum = new Label(this, SWT.NONE);
		lblSpectrum.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblSpectrum.setText("Spectrum:");
		
        number = new Spinner(this, SWT.BORDER);
		GridData gd_number = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
        gd_number.widthHint = SPINNER_WIDTH;
		number.setLayoutData(gd_number);
        number.setMinimum(0);
        number.setMaximum(MAXIMUM_MONITOR_SPECTRUM);
		
		Label lblPeriod = new Label(this, SWT.NONE);
		lblPeriod.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblPeriod.setText("Period:");
		
        period = new Spinner(this, SWT.BORDER);
		GridData gd_period = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
        gd_period.widthHint = SPINNER_WIDTH;
		period.setLayoutData(gd_period);
        period.setMinimum(0);
        period.setMaximum(MAXIMUM_MONITOR_SPECTRUM);

		new Label(this, SWT.NONE);
		new Label(this, SWT.NONE);

		spectrumFigure = new SpectrumPlot(this, SWT.NONE);
        spectrumFigure.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 6, 1));
	}

    /**
     * Sets the model for the spectrum.
     *
     * @param updatableSpectrum the new model
     */
	public void setModel(UpdatableSpectrum updatableSpectrum) {	
		spectrum = updatableSpectrum;
		
		bindingContext = new DataBindingContext();
		bindingContext.bindValue(WidgetProperties.selection().observe(number), BeanProperties.value("number").observe(updatableSpectrum));
		bindingContext.bindValue(WidgetProperties.selection().observe(period), BeanProperties.value("period").observe(updatableSpectrum));

        spectrum.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if (spectrum.getRequiresUpdate()) {
                    spectrumFigure.update();
                    spectrum.update();
                }
            }
        });

		spectrumFigure.setModel(updatableSpectrum);
	}
}
