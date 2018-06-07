
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
package uk.ac.stfc.isis.ibex.ui.scripting;

import org.python.pydev.shared_interactive_console.console.ui.ScriptConsoleManager;

import uk.ac.stfc.isis.ibex.instrument.InstrumentInfo;
import uk.ac.stfc.isis.ibex.instrument.InstrumentInfoReceiverAdapter;
import uk.ac.stfc.isis.ibex.ui.PerspectiveReopener;

/**
 * This closes and reopens the scripting perspective when instrument is switched.
 */
public class ConsoleSettings extends InstrumentInfoReceiverAdapter {
    private PerspectiveReopener scriptingPerspectiveReopener = new PerspectiveReopener(Perspective.ID);

    @Override
    public void setInstrument(InstrumentInfo instrument) {
        scriptingPerspectiveReopener.reopenPerspective();
    }

    @Override
    public void preSetInstrument(InstrumentInfo instrument) {
        ScriptConsoleManager.getInstance().closeAll();
        scriptingPerspectiveReopener.closePerspective();
    }
}
