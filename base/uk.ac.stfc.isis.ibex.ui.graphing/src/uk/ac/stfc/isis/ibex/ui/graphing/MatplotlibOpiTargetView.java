
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

import org.csstudio.opibuilder.util.MacrosInput;
import uk.ac.stfc.isis.ibex.opis.OPIViewCreationException;
import uk.ac.stfc.isis.ibex.targets.OpiTarget;
import uk.ac.stfc.isis.ibex.ui.targets.OpiTargetView;

/**
 * The WebLinksOpiTargetView shows a stand-alone OPI for weblinks.
 */
public class MatplotlibOpiTargetView extends OpiTargetView {
    /**
     * Class ID.
     */
    public static final String ID = "uk.ac.stfc.isis.ibex.ui.graphing.MatplotlibOpiTargetView";
    
    /**
     * Name of the OPI.
     */
    private static final String NAME = "Matplotlib";

    /**
     * File name of the web links OPI.
     */
    private static final String OPI = "matplotlib.opi";
    
    /**
     * This is the default URL, but it should get set each time explicitly
     */
    private static String url = "http://127.0.0.1:8988";
    
    /**
     * Displays the matplotlib OPI with the given URL.
     * @param url the url where the plot is
     * @throws OPIViewCreationException  when opi can not be created
     */
    public static void displayPlotopiWithUrl(String url) throws OPIViewCreationException {
    	MatplotlibOpiTargetView.url = url;
    	displayOpi();
    }
	
	/**
     * Display the OPI for a given target.
     *
     * @throws OPIViewCreationException when opi can not be created
     */
    public static void displayOpi() throws OPIViewCreationException {
    	OpiTarget target = new OpiTarget(NAME, OPI);
        OpiTargetView.displayOpi(target, ID);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public MacrosInput macros() {
    	MacrosInput macros = super.macros();
    	macros.put("URL", url);
    	return macros;
    }

}
