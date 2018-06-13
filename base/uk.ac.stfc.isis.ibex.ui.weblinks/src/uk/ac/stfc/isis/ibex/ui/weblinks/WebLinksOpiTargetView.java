
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
package uk.ac.stfc.isis.ibex.ui.weblinks;

import java.util.LinkedHashMap;

import org.csstudio.opibuilder.runmode.DisplayOpenManager;
import org.csstudio.opibuilder.runmode.RunnerInput;
import org.csstudio.opibuilder.util.MacrosInput;
import org.csstudio.trends.databrowser2.Messages;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.osgi.util.NLS;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.PartInitException;

import uk.ac.stfc.isis.ibex.instrument.Instrument;
import uk.ac.stfc.isis.ibex.opis.OPIViewCreationException;
import uk.ac.stfc.isis.ibex.opis.Opi;
import uk.ac.stfc.isis.ibex.opis.OpiView;

/**
 * The Class SynopticOpiTargetView which shows tabbed OPIs for syniptics.
 */
public class WebLinksOpiTargetView extends OpiView {

    /**
     * Class ID.
     */
    public static final String ID = "uk.ac.stfc.isis.ibex.ui.weblinks.WebLinksOpiTargetView"; //$NON-NLS-1$

    /**
     * File name of the web links OPI.
     */
    private static final String WEB_LINKS_OPI = "weblinks.opi";

    /**
     * {@inheritDoc}
     */
	@Override
	protected Path opi() throws OPIViewCreationException {
		Path p = Opi.getDefault().opiProvider().pathFromName(WEB_LINKS_OPI);
		System.out.println(p);
		return p;
	}
	
	/**
     * Initialise OPI from a path.
     *
     * @throws OPIViewCreationException the OPI view creation exception
     */
	@Override
    public void initialiseOPI() throws OPIViewCreationException {
    	LinkedHashMap<String, String> macrosMap = new LinkedHashMap<String, String>();
    	macrosMap.put("INST", Instrument.getInstance().currentInstrument().name());
        MacrosInput macros = new MacrosInput(macrosMap, false);
    	
		final RunnerInput input = new RunnerInput(opi(), new DisplayOpenManager(this), macros);
		try {
            setOPIInput(input);
		} catch (PartInitException e) {
			throw new OPIViewCreationException(e.getMessage());
		}
    }
	
	/**
	 * Overide toolbars to not exist (they appear in the dashboard which looks weird).
	 */
	@Override
	public void createToolbarButtons(){
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
			MessageDialog.openError(getSite().getShell(), Messages.Error, NLS.bind(Messages.ErrorFmt, e.getMessage()));
		}
	}
}
