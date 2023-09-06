
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
package uk.ac.stfc.isis.ibex.ui.dae.spectraplots;

import org.csstudio.opibuilder.util.MacrosInput;
import org.eclipse.core.runtime.Path;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.PartInitException;
import uk.ac.stfc.isis.ibex.instrument.Instrument;
import uk.ac.stfc.isis.ibex.opis.OPIViewCreationException;
import uk.ac.stfc.isis.ibex.opis.Opi;
import uk.ac.stfc.isis.ibex.opis.OpiView;

/**
 * The WebLinksOpiTargetView shows a stand-alone OPI for weblinks.
 */
public class CombinedSpectraPlotsOpiTargetView extends OpiView {
    /**
     * Class ID.
     */
    public static final String ID = "uk.ac.stfc.isis.ibex.ui.weblinks.CombinedSpectraPlotsOpiTargetView";

    /**
     * File name of the web links OPI.
     */
    private static final String COMBINED_SPECTRA_PLOTS_OPI = "spectra_combined_plot.opi";
    
    private final String pvPrefix = Instrument.getInstance().currentInstrument().pvPrefix();

    /**
     * {@inheritDoc}
     */
	@Override
	protected Path opi() throws OPIViewCreationException {
		return Opi.getDefault().opiProvider().pathFromName(COMBINED_SPECTRA_PLOTS_OPI);
	}
	
	/**
	 * Override toolbars to not exist (they appear in the dashboard which looks weird).
	 */
	@Override
	public void createToolbarButtons() {
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void init(IViewSite site) throws PartInitException {
		super.init(site);
		try {
			initialiseOPI();
		} catch (OPIViewCreationException e) {
			throw new PartInitException(e.getMessage(), e);
		}
	}

	@Override
	protected MacrosInput macros() {
		MacrosInput macros = emptyMacrosInput();
		macros.put("INST", Instrument.getInstance().currentInstrument().name());
		macros.put("P", Instrument.getInstance().currentInstrument().pvPrefix());
		return macros;
	}
}
