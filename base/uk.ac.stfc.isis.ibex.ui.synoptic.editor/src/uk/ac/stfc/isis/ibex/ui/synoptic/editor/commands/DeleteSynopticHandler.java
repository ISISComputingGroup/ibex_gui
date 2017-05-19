
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

package uk.ac.stfc.isis.ibex.ui.synoptic.editor.commands;

import java.io.IOException;
import java.util.Collection;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

import uk.ac.stfc.isis.ibex.epics.writing.SameTypeWriter;
import uk.ac.stfc.isis.ibex.synoptic.Synoptic;
import uk.ac.stfc.isis.ibex.ui.synoptic.editor.dialogs.MultipleSynopticsSelectionDialog;

/**
 * Handler class for deleting synoptics.
 */
public class DeleteSynopticHandler extends AbstractHandler {

    private static final String TITLE = "Delete Synoptics";
    private static final Synoptic SYNOPTIC = Synoptic.getInstance();
    private static final Shell SHELL = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();

    /**
     * This is an inner anonymous class inherited from SameTypeWriter with added
     * functionality for disabling the command if the underlying PV cannot be
     * written to.
     */
    protected final SameTypeWriter<Collection<String>> synopticService = new SameTypeWriter<Collection<String>>() {
        @Override
        public void onCanWriteChanged(boolean canWrite) {
            setBaseEnabled(canWrite);
        };
    };

    /**
     * Constructor that adds a listener to disable the handler if the
     * destination is disabled.
     */
	public DeleteSynopticHandler() {
        synopticService.writeTo(SYNOPTIC.delete());
        SYNOPTIC.delete().subscribe(synopticService);
	}	
	
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {		
        MultipleSynopticsSelectionDialog dialog =
                new MultipleSynopticsSelectionDialog(SHELL, TITLE, SYNOPTIC.availableEditableSynoptics());
		if (dialog.open() == Window.OK) {
			try {
                synopticService.write(dialog.selectedSynoptics());
            } catch (IOException e) {
                throw new ExecutionException("Failed to write to PV", e);
            }
		}
		return null;
	}
}
