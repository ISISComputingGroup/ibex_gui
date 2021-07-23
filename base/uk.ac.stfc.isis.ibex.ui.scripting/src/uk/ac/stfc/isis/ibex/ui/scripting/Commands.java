
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

package uk.ac.stfc.isis.ibex.ui.scripting;

import uk.ac.stfc.isis.ibex.instrument.Instrument;
import uk.ac.stfc.isis.ibex.instrument.InstrumentInfo;

/**
 * Static factory class for commands to be sent to genie python.
 */
public final class Commands {

    /**
     * Python command to set the current instrument name.
     */
    private static final String SET_INSTRUMENT_CMD = "set_instrument('%s')\n";

    /**
     * Python command to scale down matplotlib plot dimensions (hardcoded for now).
     */
    public static final String COMPACT_PLOT_CMD = "matplotlib.rcParams['figure.figsize'] = [5.8, 2.2] \n";

    /**
     * The command to initialise genie.
     */
    public static final String GENIE_INITIALISATION_CMDS = "import matplotlib \n"
            + "matplotlib.use('module://genie_python.matplotlib_backend.ibex_web_backend') \n"
            + "from genie_python.genie_startup import * \n" 
            + "import os \n"
            + "os.environ[\"FROM_IBEX\"] = str(True) \n";

    private Commands() {
    }

    /**
     * Creates the command to switch genie to the current instrument.
     * 
     * @return the command to switch genie to the current instrument.
     */
    public static String getSetInstrumentCommand() {
        InstrumentInfo info = Instrument.getInstance().currentInstrument();
        String instrumentName = info.hostName().equals("localhost") ? info.pvPrefix() : info.hostName();

        return String.format(SET_INSTRUMENT_CMD, instrumentName);
    }

    /**
     * Creates a set of initialisation commands to run when creating the
     * genie_python console.
     * 
     * @param compactPlot whether or not to include the compact plot python command.
     * @return initial python commands
     */
    public static String getInitialisationCommands(boolean compactPlot) {
        String initCommands = GENIE_INITIALISATION_CMDS;
        initCommands += compactPlot ? COMPACT_PLOT_CMD : "";

        return initCommands;
    }
}
