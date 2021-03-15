
/*
 * This file is part of the ISIS IBEX application. Copyright (C) 2012-2016
 * Science & Technology Facilities Council. All rights reserved.
 *
 * This program is distributed in the hope that it will be useful. This program
 * and the accompanying materials are made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution. EXCEPT AS
 * EXPRESSLY SET FORTH IN THE ECLIPSE PUBLIC LICENSE V1.0, THE PROGRAM AND
 * ACCOMPANYING MATERIALS ARE PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES
 * OR CONDITIONS OF ANY KIND. See the Eclipse Public License v1.0 for more
 * details.
 *
 * You should have received a copy of the Eclipse Public License v1.0 along with
 * this program; if not, you can obtain a copy from
 * https://www.eclipse.org/org/documents/epl-v10.php or
 * http://opensource.org/licenses/eclipse-1.0.php
 */

package uk.ac.stfc.isis.ibex.ui.configserver.commands;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeoutException;

import org.csstudio.csdata.ProcessVariable;
import org.csstudio.csdata.TimestampedPV;
import org.csstudio.ui.util.AdapterUtil;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;

import uk.ac.stfc.isis.ibex.configserver.ConfigServer;
import uk.ac.stfc.isis.ibex.configserver.Configurations;
import uk.ac.stfc.isis.ibex.ui.configserver.commands.helpers.NewBlockHelper;

/**
 * The handler class for creating a new block with a default PV.
 */
public class NewBlockHandler extends AbstractHandler {
    static final ConfigServer SERVER = Configurations.getInstance().server();

    /**
     * Open the edit block dialogue.
     *
     * @param shell shell to open
     * @return 
     * @throws TimeoutException
     */
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
	    ISelection selection = HandlerUtil.getActiveMenuSelection(event);
        if (selection == null) {
            // This works for double-clicks.
            selection = HandlerUtil.getCurrentSelection(event);
        }
        
        String pvAddress = "";

        try {
            // Add received PVs with default archive data sources
            final List<TimestampedPV> timestampedPVs = Arrays
                    .asList(AdapterUtil.convert(selection,
                            TimestampedPV.class));
            if (!timestampedPVs.isEmpty()) {
                pvAddress = timestampedPVs.get(0).getName();
            } else {
                final List<ProcessVariable> pvs = Arrays.asList(AdapterUtil.convert(
                        selection, ProcessVariable.class));
                if (!pvs.isEmpty()) {
                    pvAddress = pvs.get(0).getName();
                }
            }
        } catch (Exception ex) {
            Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
            MessageDialog.openError(shell,
                    "Error", ex.getMessage());
        }
	    new NewBlockHelper(null, SERVER).createDialog(pvAddress);
        return null;
    }

}
