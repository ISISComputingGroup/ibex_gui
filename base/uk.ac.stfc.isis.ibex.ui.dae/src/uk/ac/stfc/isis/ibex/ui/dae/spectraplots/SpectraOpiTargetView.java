
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

import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.csstudio.opibuilder.util.MacrosInput;
import org.eclipse.core.runtime.Path;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.PartInitException;
import uk.ac.stfc.isis.ibex.instrument.Instrument;
import uk.ac.stfc.isis.ibex.logger.IsisLog;
import uk.ac.stfc.isis.ibex.logger.LoggerUtils;
import uk.ac.stfc.isis.ibex.opis.OPIViewCreationException;
import uk.ac.stfc.isis.ibex.opis.Opi;
import uk.ac.stfc.isis.ibex.ui.targets.OpiTargetView;

/**
 * An OPI target view pointing at a spectra plot.
 */
public class SpectraOpiTargetView extends OpiTargetView {
    /**
     * Class ID.
     */
    public static final String ID = "uk.ac.stfc.isis.ibex.ui.dae.SpectraOpiTargetView";
    
    private static final int NUMBER_OF_PLOTS = 4;

    /**
     * File name of the web links OPI.
     */
    private static final String OPI_FILE = "spectra_plots.opi";
    
    private final Collection<SpectraPlotConfiguration> plotConfigurations;
    
    /**
     * Create a new instance.
     */
    public SpectraOpiTargetView() {
        plotConfigurations = IntStream.range(0, NUMBER_OF_PLOTS)
            .mapToObj(SpectraPlotConfiguration::new)
            .collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Path opi() throws OPIViewCreationException {
        return Opi.getDefault().opiProvider().pathFromName(OPI_FILE);
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
            this.initializeMacros();
        } catch (OPIViewCreationException e) {
            LoggerUtils.logErrorWithStackTrace(IsisLog.getLogger(getClass()), e.getMessage(), e);
            throw new PartInitException(e.getMessage(), e);
        }
    }
    
    /**
     * Initialises the macros.
     */
    public void initializeMacros() {
        plotConfigurations.stream().forEach(conf -> conf.initializeFromPreferenceStore());
    }

    @Override
    public MacrosInput macros() {
        MacrosInput macros = emptyMacrosInput();
        macros.put("P", Instrument.getInstance().getPvPrefix());
        macros.put("NAME", "Spectra Plot");
        
        plotConfigurations.stream().forEach(conf -> macros.getMacrosMap().putAll(conf.getMacros()));
        
        return macros;
    }
    
}
