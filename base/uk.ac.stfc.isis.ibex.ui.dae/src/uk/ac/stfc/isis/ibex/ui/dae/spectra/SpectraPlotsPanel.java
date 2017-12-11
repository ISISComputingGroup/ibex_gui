
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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

import uk.ac.stfc.isis.ibex.dae.spectra.UpdatableSpectrum;

/**
 * A panel that allows the user to plot various spectra from the DAE.
 */
@SuppressWarnings("checkstyle:magicnumber")
public class SpectraPlotsPanel extends Composite {

	private static final int NUMBER_OF_SPECTRA = 4;
	private List<SpectrumView> spectraPlots = new ArrayList<>(NUMBER_OF_SPECTRA);
	private Composite plots;
	
    /**
     * The constructor for the panel.
     * 
     * @param parent
     *            The parent composite that the panel belongs to.
     * @param style
     *            The SWT style flags for the panel.
     */
	public SpectraPlotsPanel(Composite parent, int style) {
		super(parent, style);
		setLayout(new GridLayout(1, false));
		
		plots = new Composite(this, SWT.NONE);
		plots.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		plots.setLayout(new GridLayout(2, false));
		
		for (int i = 0; i < NUMBER_OF_SPECTRA; i++) {
			SpectrumView plot = new SpectrumView(plots, SWT.NONE);
			plot.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
			spectraPlots.add(plot);
		}
	}
	
    /**
     * Set the model which the spectra graphs are updated from.
     * 
     * @param spectra
     *            A list of NUMBER_OF_SPECTRA updating spectrum
     */
	public void setModel(List<? extends UpdatableSpectrum> spectra) {
		
		if (spectra.size() != NUMBER_OF_SPECTRA) {
			throw new RuntimeException("The number of spectra view models should be the same as the number of spectra!");
		}
		
		for (int i = 0; i < NUMBER_OF_SPECTRA; i++) {
			spectraPlots.get(i).setModel(spectra.get(i));
		}
	}
}
