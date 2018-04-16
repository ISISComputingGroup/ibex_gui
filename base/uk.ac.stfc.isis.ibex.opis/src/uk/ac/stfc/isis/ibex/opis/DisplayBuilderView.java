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

package uk.ac.stfc.isis.ibex.opis;
import org.csstudio.display.builder.model.macros.Macros;
import org.csstudio.display.builder.rcp.DisplayInfo;
import org.csstudio.display.builder.rcp.RuntimeViewPart;

/**
 * The Class OpiView to show an OPI.
 */
public abstract class DisplayBuilderView extends RuntimeViewPart {

    private Macros macros = new Macros();

    /**
     * Initialise OPI from a path.
     *
     * @throws OPIViewCreationException the OPI view creation exception
     */
    public void initialiseOPI() throws OPIViewCreationException {
        DisplayInfo info = new DisplayInfo(opi(), "Display builder view", macros(), false);
		loadDisplayFile(info);
    }

    /**
     * Macros that the OPI has.
     *
     * @return the macros for input to the OPI
     */
    protected Macros macros() {
        return macros;
    }

    /**
     * Get the OPI path.
     *
     * @return the path
     * @throws OPIViewCreationException if the path can not be found or can not
     *             be read
     */
    protected abstract String opi() throws OPIViewCreationException;
}