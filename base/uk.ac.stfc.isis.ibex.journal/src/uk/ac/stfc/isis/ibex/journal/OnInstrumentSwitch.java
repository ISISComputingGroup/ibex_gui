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
package uk.ac.stfc.isis.ibex.journal;

import uk.ac.stfc.isis.ibex.instrument.InstrumentInfo;
import uk.ac.stfc.isis.ibex.instrument.InstrumentInfoReceiver;

/**
 * This class is responsible for updating all connections when an instrument is
 * switched.
 */
public class OnInstrumentSwitch implements InstrumentInfoReceiver {

    /**
     * Updates the journal model on switching instrument.
     * 
     * @param instrument
     *            The instrument to switch to.
     */
    @Override
    public void setInstrument(InstrumentInfo instrument) {
        // Nothing to be done
    }

    @Override
    public void preSetInstrument(InstrumentInfo instrument) {
        // Nothing to be done
    }

    @Override
    public void postSetInstrument(InstrumentInfo instrument) {
        JournalModel model = Journal.getDefault().getModel();
        model.clearRuns();
        model.refresh();
    }
}
