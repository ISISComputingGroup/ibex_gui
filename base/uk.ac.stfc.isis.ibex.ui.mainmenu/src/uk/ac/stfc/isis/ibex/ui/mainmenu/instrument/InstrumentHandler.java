
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

package uk.ac.stfc.isis.ibex.ui.mainmenu.instrument;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

//import uk.ac.stfc.isis.ibex.alarm.AlarmConnectionCloser;
import uk.ac.stfc.isis.ibex.instrument.InstrumentInfo;
import uk.ac.stfc.isis.ibex.ui.mainmenu.MainMenuUI;
import uk.ac.stfc.isis.ibex.ui.scripting.Consoles;

/**
 * Handles the changing of the instrument pointed.
 *
 */
public class InstrumentHandler extends AbstractHandler {

    @Override
    public Object execute(ExecutionEvent arg0) throws ExecutionException {
        InstrumentInfo selected = getUserSelectedInstrument();
        if (selected == null) {
            return null;
        }

        MainMenuUI.INSTRUMENT.setInstrumentForAllPlugins(selected);
        

        return null;
    }

    /**
     * Get the user to select an instrument and confirm closing python console
     * windows if necessary.
     * 
     * @return selected instrument; null for do not switch instrument
     */
    private InstrumentInfo getUserSelectedInstrument() {
        final Consoles consoles = Consoles.getDefault();
        InstrumentInfo selected;
        Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
        InstrumentDialog dialog = new InstrumentDialog(shell);

        if (dialog.open() == Window.CANCEL) {
            return null;
        }

        selected = dialog.selectedInstrument();
        if (selected == null) {
            return null;
        }

        if (consoles.anyActive()) {
            if (!shouldCloseAllConsoles(dialog)) {
                return null;
            }
        }
        return selected;
    }

    private boolean shouldCloseAllConsoles(InstrumentDialog dialog) {
        return MessageDialog.openConfirm(dialog.getShell(), "Confirm Instrument Switch",
                "All console scripting sessions for this instrument will be closed.\nWould you like to continue?");
    }
}
