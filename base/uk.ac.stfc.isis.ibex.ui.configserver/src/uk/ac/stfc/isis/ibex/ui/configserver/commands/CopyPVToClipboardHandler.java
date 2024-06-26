/*
 * This file is part of the ISIS IBEX application. Copyright (C) 2012-2024
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

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.util.Arrays;
import java.util.List;
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

/**
 * The handler class for copying a PV name to the clipboard by the user.
 *
 */
public class CopyPVToClipboardHandler extends AbstractHandler {

	static final Shell SHELL = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
	String configName;

	/**
	 * Copy the selected PV name to the clipboard.
	 * 
	 * 
	 * @param event The event that caused the open.
	 * @throws ExecutionException
	 * @return null
	 */

	public Object execute(ExecutionEvent event) throws ExecutionException {

		try {

			String pvAddress = getPVFromEvent(event);
			
			StringSelection selection = new StringSelection(pvAddress);
			Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
			clipboard.setContents(selection, selection);

		} catch (Exception ex) {
			MessageDialog.openError(SHELL, "Error", ex.getMessage());
		}

		return null;
	}
	
	/**
	 * Set enabled to true as we want to still be able to add a PV to the clipboard even if the server is down.
	 */
	@Override
	public boolean isEnabled() {
		return true;
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
		final List<TimestampedPV> timestampedPVs = Arrays.asList(AdapterUtil.convert(selection, TimestampedPV.class));
		if (!timestampedPVs.isEmpty()) {
			pvAddress = timestampedPVs.get(0).getName();
		} else {
			final List<ProcessVariable> pvs = Arrays.asList(AdapterUtil.convert(selection, ProcessVariable.class));
			if (!pvs.isEmpty()) {
				pvAddress = pvs.get(0).getName();
			} else {
				throw new Exception("No PV found for selection");
			}
		}

		return pvAddress;
	}
}
