
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
import org.eclipse.ui.IPerspectiveDescriptor;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.WorkbenchException;

import uk.ac.stfc.isis.ibex.alarm.AlarmConnectionCloser;
import uk.ac.stfc.isis.ibex.instrument.InstrumentInfo;
import uk.ac.stfc.isis.ibex.ui.alarm.Alarms;
import uk.ac.stfc.isis.ibex.ui.mainmenu.MainMenuUI;
import uk.ac.stfc.isis.ibex.ui.scripting.Consoles;
import uk.ac.stfc.isis.ibex.ui.synoptic.Activator;

/**
 * Handles the changing of the instrument pointed at.
 *
 */
public class InstrumentHandler extends AbstractHandler {

    private final Consoles consoles = Consoles.getDefault();
    private final Alarms alarms = Alarms.getDefault();
    private final Activator synoptic = Activator.getDefault();

    @Override
    public Object execute(ExecutionEvent arg0) throws ExecutionException {
        Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
        InstrumentDialog dialog = new InstrumentDialog(shell);

        if (dialog.open() == Window.CANCEL || dialog.selectedInstrument() == null) {
            return null;
        }

        if (consoles.anyActive()) {
            if (shouldCloseAllConsoles(dialog)) {
                consoles.closeAll();
            } else {
                return null;
            }
        }

        IPerspectiveDescriptor activePerspective = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
                .getPerspective();

        AlarmConnectionCloser alarmConnectionCloser = alarms.closeAll();

        // Close any OPIs in the synoptic
        synoptic.closeAllOPIs();

        InstrumentInfo selected = dialog.selectedInstrument();
        setInstrument(selected);
        
        IPerspectiveDescriptor scriptingPerspective = PlatformUI.getWorkbench().getPerspectiveRegistry()
                .findPerspectiveWithId(uk.ac.stfc.isis.ibex.ui.scripting.Perspective.ID);

        IPerspectiveDescriptor alarmPerspective = PlatformUI.getWorkbench().getPerspectiveRegistry()
                .findPerspectiveWithId(uk.ac.stfc.isis.ibex.ui.alarm.AlarmPerspective.ID);

        // If the scripting perspective is currently open just create the
        // console as the scripting perspective does when it is first created.
        //
        // Similarly if the alarm perspective is open it is closed, then reopend
        // here.
        //
        // Else close the perspective and let the scripting perspective sort
        // itself out when it gets opened again.
        if (activePerspective == scriptingPerspective) {
            consoles.createConsole();
        } else if (activePerspective == alarmPerspective) {
            try {
                PlatformUI.getWorkbench().showPerspective(uk.ac.stfc.isis.ibex.ui.alarm.AlarmPerspective.ID,
                        PlatformUI.getWorkbench().getActiveWorkbenchWindow());
            } catch (WorkbenchException e) {
                e.printStackTrace();
            }
        } else {
            PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().closePerspective(scriptingPerspective,
                    false, false);
        }

        alarms.updateAlarmModel();

        alarmConnectionCloser.close();
        return null;
    }

    private boolean shouldCloseAllConsoles(InstrumentDialog dialog) {
        return MessageDialog.openConfirm(dialog.getShell(), "Confirm Instrument Switch",
                "All console scripting sessions for this instrument will be closed.\nWould you like to continue?");
    }

    private void setInstrument(InstrumentInfo selected) {
        MainMenuUI.INSTRUMENT.setInstrument(selected);
    }
}
