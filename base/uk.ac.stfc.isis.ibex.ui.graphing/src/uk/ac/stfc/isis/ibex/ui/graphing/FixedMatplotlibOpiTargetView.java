
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
package uk.ac.stfc.isis.ibex.ui.graphing;

import org.apache.logging.log4j.Logger;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import uk.ac.stfc.isis.ibex.logger.IsisLog;
import uk.ac.stfc.isis.ibex.opis.OPIViewCreationException;
import uk.ac.stfc.isis.ibex.targets.OpiTarget;
import uk.ac.stfc.isis.ibex.ui.targets.OpiTargetView;

/**
 * The WebLinksOpiTargetView shows a stand-alone OPI for weblinks.
 */
public class FixedMatplotlibOpiTargetView extends OpiTargetView {

    /**
     * Class ID.
     */
    public static final String ID = "uk.ac.stfc.isis.ibex.ui.graphing.FixedMatplotlibOpiTargetView";
    
    /**
     * Name of the OPI.
     */
    private static final String NAME = "Matplotlib";

    /**
     * File name of the compact matplotlib OPI.
     */
    private static final String OPI = "matplotlib_compact.opi";
    
    /**
     * The OPI target for the compact matplotlib opi.
     */
    private static final OpiTarget TARGET = new OpiTarget(NAME, OPI);
    
    /**
     * Logger for errors.
     */
    private static final Logger LOG = IsisLog.getLogger(FixedMatplotlibOpiTargetView.class);
    
    /**
     * Macro name for the URL.
     */
    private static final String URL_MACRO_NAME = "URL";
    
    /**
     * The default URL for the page showing the matplotlib plot.
     */
    private static final String DEFAULT_URL = "http://127.0.0.1:8988";

    /**
     * Display the OPI for the matplotlib graph in a fixed view.
     * 
     * @param url the url for the graph.
     *
     * @throws OPIViewCreationException when opi can not be created
     */
    public static synchronized void displayOpi(final String url) {
		if (TARGET.properties().containsKey(URL_MACRO_NAME)) {
			TARGET.properties().remove(URL_MACRO_NAME);
		}
		TARGET.addProperty(URL_MACRO_NAME, url);
		
        IWorkbenchPage workbenchPage = null;
        FixedMatplotlibOpiTargetView view = null;
        try {
            workbenchPage = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
            view = (FixedMatplotlibOpiTargetView) workbenchPage.findView(ID);
            view.setOpi(TARGET);
            view.setPartName(TARGET.name());
		} catch (OPIViewCreationException e) {
			LOG.error(e.getMessage(), e);
		}
    }

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void init(IViewSite site) throws PartInitException {
		super.init(site);
		try {
			TARGET.addProperty(URL_MACRO_NAME, DEFAULT_URL);
			setOpi(TARGET);
		} catch (OPIViewCreationException e) {
			throw new PartInitException(e.getMessage(), e);
		}
	}
}
