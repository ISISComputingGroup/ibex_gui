
/*
 * This file is part of the ISIS IBEX application. Copyright (C) 2012-2021
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

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeoutException;

import org.csstudio.csdata.ProcessVariable;
import org.csstudio.csdata.TimestampedPV;
import org.csstudio.ui.util.AdapterUtil;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;

import uk.ac.stfc.isis.ibex.configserver.ConfigServer;
import uk.ac.stfc.isis.ibex.configserver.Configurations;
import uk.ac.stfc.isis.ibex.configserver.editing.BlockFactory;
import uk.ac.stfc.isis.ibex.configserver.editing.EditableBlock;
import uk.ac.stfc.isis.ibex.configserver.editing.EditableConfiguration;
import uk.ac.stfc.isis.ibex.ui.configserver.ConfigurationServerUI;
import uk.ac.stfc.isis.ibex.ui.configserver.ConfigurationViewModels;
import uk.ac.stfc.isis.ibex.ui.configserver.editing.blocks.EditBlockDialog;


/**
 * The handler class for creating a new block with a default PV.
 */
public class NewBlockHandler extends AbstractHandler {
    static final ConfigServer SERVER = Configurations.getInstance().server();
    static final Shell SHELL = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
    /**
     * Open the new block dialogue.
     *
     * @param event The event that caused the open.
     * @return 
     * @throws ExecutionException
     */
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
        try {
    	    String pvAddress = getPVFromEvent(event);
            createDialog(pvAddress);
        } catch (Exception ex) {
        
            MessageDialog.openError(SHELL, "Error", ex.getMessage());
        }
        
	    return null;
    }
	
	
	/**
	 * Prevent adding block without permission.
	 */
	@Override
	public boolean isEnabled() {
		return SERVER.setCurrentConfig().canWrite();
	}
	
    /**
     * Create the dialog for creating the new block.
     * 
     * @param pvAddress The PV to create the block for
     * @throws TimeoutException thrown if connecting to the server times out
     * @throws IOException thrown if the server cannot be written to
     */
    public void createDialog(String pvAddress) throws TimeoutException, IOException {

    	ConfigurationViewModels configurationViewModels = ConfigurationServerUI.getDefault().configurationViewModels();

        EditableConfiguration config = configurationViewModels.getCurrentConfig();
        BlockFactory blockFactory = new BlockFactory(config);
        EditableBlock added = blockFactory.createNewBlock(Optional.of(pvAddress));
        Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();

        EditBlockDialog dialog = new EditBlockDialog(shell, added, config);
        if (dialog.open() == Window.OK) {
            SERVER.setCurrentConfig().write(config.asConfiguration());
        }

    }
    
    /**
     * Gets the first PV address from the selection.
     * 
     * @param event The selection that caused the open
     * @throws Exception if no PV could be found in the selection
     * @return The PV address
     */
    private String getPVFromEvent(ExecutionEvent event) throws Exception {
        String pvAddress = "";
        
        ISelection selection = HandlerUtil.getActiveMenuSelection(event);
        if (selection == null) {
            // This works for double-clicks.
            selection = HandlerUtil.getCurrentSelection(event);
        }

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
            } else {
                throw new Exception("No PV found for selection");
            }
        }
        
        return pvAddress;
    }

}
