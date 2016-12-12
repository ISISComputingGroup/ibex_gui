
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

package uk.ac.stfc.isis.ibex.ui.synoptic;

import uk.ac.stfc.isis.ibex.instrument.InstrumentInfo;
import uk.ac.stfc.isis.ibex.instrument.InstrumentInfoReceiver;

/**
 * The activator class controls the plug-in life cycle.
 */
public class SynopticSettings implements InstrumentInfoReceiver {

    @Override
    public void setInstrument(InstrumentInfo instrument) {
        // nothing to do on set instrument
    }

    /**
     * Close the open OPIs they will refer to the old instrument.
     * 
     * @param instrument the instrument being switched to
     */
    @Override
    public void preSetInstrument(InstrumentInfo instrument) {
        Activator.getDefault().presenter().closeAllOPIs();
    }

    @Override
    public void postSetInstrument(InstrumentInfo instrument) {
        // nothing to do on post set instrument
    }
}