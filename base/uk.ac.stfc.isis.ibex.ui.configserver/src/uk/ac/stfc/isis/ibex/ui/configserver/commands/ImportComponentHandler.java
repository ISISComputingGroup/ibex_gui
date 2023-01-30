/*
 * This file is part of the ISIS IBEX application.
 * Copyright (C) 2012-2023 Science & Technology Facilities Council.
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

package uk.ac.stfc.isis.ibex.ui.configserver.commands;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

import uk.ac.stfc.isis.ibex.configserver.configuration.Configuration;
import uk.ac.stfc.isis.ibex.configserver.json.JsonConverters;
import uk.ac.stfc.isis.ibex.instrument.Instrument;
import uk.ac.stfc.isis.ibex.logger.IsisLog;
import uk.ac.stfc.isis.ibex.logger.LoggerUtils;
import uk.ac.stfc.isis.ibex.ui.configserver.ConfigurationServerUI;
import uk.ac.stfc.isis.ibex.ui.configserver.ConfigurationViewModels;
import uk.ac.stfc.isis.ibex.ui.configserver.ImportVariables;
import uk.ac.stfc.isis.ibex.ui.configserver.ImportConverter;
import uk.ac.stfc.isis.ibex.ui.configserver.dialogs.EditConfigDialog;
import uk.ac.stfc.isis.ibex.ui.configserver.dialogs.ImportComponentDialog;

/**
 * Handles the selection of the import component menu item.
 */
public class ImportComponentHandler extends DisablingConfigHandler<Configuration> {
	
	private static final String TITLE = "Select Instrument";
    private static final String SUB_TITLE = "Select Instrument to Import Component From";
	private static final String EDIT_TITLE = "Import Component";
    private static final String EDIT_SUB_TITLE = "Editing an Import Component";
	
	/**
     * The constructor.
     */
    public ImportComponentHandler() {
    	super(SERVER.saveAs());
    }

    /**
     * Open an import component dialogue.
     *
     * @param shell shell to use
     * @throws TimeoutException If getting the blank config fails.
     */
    @Override
    public void safeExecute(Shell shell) throws TimeoutException {
    	ImportVariables importVariables = new ImportVariables(new JsonConverters());
    	ImportComponentDialog importConfigDialog = new ImportComponentDialog(shell, TITLE, SUB_TITLE, importVariables);
    	if (importConfigDialog.open() == Window.OK && importConfigDialog.getSelectedComponent() != null) {
    		openWarningMessageBox(shell);
    		
    		ConfigurationViewModels configurationViewModels = ConfigurationServerUI.getDefault().configurationViewModels();
        	var blank = configurationViewModels.getBlankConfig();
        	var editableIocs = SERVER.iocs().getValue();
        	ImportConverter.convert(importConfigDialog.getSelectedComponent(), blank, importVariables.getRemotePrefix(), Instrument.getInstance().getPvPrefix(), editableIocs);
        	
        	blank.setIsComponent(true);
        	EditConfigDialog editDialog = new EditConfigDialog(shell, EDIT_TITLE, EDIT_SUB_TITLE, blank, true, configurationViewModels, false);
        	if (editDialog.open() == Window.OK) {
        		try {
        			SERVER.saveAsComponent().write(editDialog.getComponent());
				} catch (IOException e) {
					LoggerUtils.logErrorWithStackTrace(IsisLog.getLogger(getClass()), e.getMessage(), e);
					new MessageDialog(shell, "Error", null,
							"Failed to save import component '%s'.".formatted(editDialog.getComponent().getName()),
							MessageDialog.ERROR, new String[] {"OK"}, 0).open();
				}
        	}
    		
        }
    }
    
    private void openWarningMessageBox(Shell shell) {
    	MessageBox messageBox = new MessageBox(shell, SWT.ICON_WARNING | SWT.OK);
        messageBox.setText("Warning: Importing a component from a different instrument!");
        
        String warningString = String.join("\n\n",
        	"When editing the component check the following:",
        	"- Communication settings for IOCs. (COM port, IP address, etc.)",
        	"- Remote IOCs.",
        	"- IOCs that rely on autosave values or settings area items.",
        	"- Instrument prefix of non-local blocks.");
        
        messageBox.setMessage(warningString);
        messageBox.open();
    }
}
