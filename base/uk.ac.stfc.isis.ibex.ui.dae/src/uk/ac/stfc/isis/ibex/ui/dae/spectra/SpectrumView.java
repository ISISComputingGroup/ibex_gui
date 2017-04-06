
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

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
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
	private Button update;

	private DataBindingContext bindingContext;
	private UpdatableSpectrum spectrum;
	
    /**
     * Instantiates a new spectrum view.
     *
     * @param parent the parent
     * @param style the style
     */
    @SuppressWarnings({ "checkstyle:magicnumber", "checkstyle:localvariablename" })
	public SpectrumView(Composite parent, int style) {
		super(parent, style);
		setLayout(new GridLayout(7, false));
		
		Label lblSpectrum = new Label(this, SWT.NONE);
		lblSpectrum.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblSpectrum.setText("Spectrum:");
		
        number = new Spinner(this, SWT.BORDER);
		GridData gd_number = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_number.widthHint = 30;
		number.setLayoutData(gd_number);
        number.setMinimum(0);
        number.setMaximum(999);
		
		Label lblPeriod = new Label(this, SWT.NONE);
		lblPeriod.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblPeriod.setText("Period:");
		
        period = new Spinner(this, SWT.BORDER);
		GridData gd_period = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_period.widthHint = 30;
		period.setLayoutData(gd_period);
        period.setMinimum(0);
        period.setMaximum(999);

		new Label(this, SWT.NONE);
		new Label(this, SWT.NONE);
		
		update = new Button(this, SWT.NONE);
		update.setText("Set plot");
		GridData gd_update = new GridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1);
		gd_update.widthHint = 60;
		update.setLayoutData(gd_update);
		update.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (spectrum != null) {
					spectrum.update();
                    spectrumFigure.updateData();
				}
			}
		});
		
		spectrumFigure = new SpectrumPlot(this, SWT.NONE);
		spectrumFigure.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 7, 1));
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
		bindingContext.bindValue(WidgetProperties.enabled().observe(update), BeanProperties.value("requiresUpdate").observe(updatableSpectrum));

		spectrumFigure.setModel(updatableSpectrum);
	}
}
