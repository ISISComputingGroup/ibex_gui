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

package uk.ac.stfc.isis.ibex.ui.beamstatus.views;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

import org.apache.logging.log4j.Logger;
import org.eclipse.jface.action.Action;

import org.eclipse.jface.action.MenuManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IPerspectiveRegistry;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;

import uk.ac.stfc.isis.ibex.beamstatus.FacilityPV;
import uk.ac.stfc.isis.ibex.logger.IsisLog;
import uk.ac.stfc.isis.ibex.logger.LoggerUtils;
import uk.ac.stfc.isis.ibex.ui.blocks.groups.BlocksMenu;
import uk.ac.stfc.isis.ibex.ui.blocks.presentation.Presenter;
import uk.ac.stfc.isis.ibex.ui.configserver.commands.NewBlockHandler;

/**
 * Right-click menu from Beam Information.
 * 
 */
public class BeamInfoMenu extends MenuManager {

	private static final String LOG_PLOTTER_PERSPECTIVE_ID = "uk.ac.stfc.isis.ibex.client.e4.product.perspective.logplotter";
	private static final Logger LOG = IsisLog.getLogger(BeamInfoMenu.class);

	/**
	 * The constructor, creates the menu for when the specific facility PV is
	 * right-clicked on.
	 *
	 * @param facilityPV the selected PV
	 */
	public BeamInfoMenu(FacilityPV facilityPV) {

		// Creating right-click menu

		add(new Action("Add block to config: " + facilityPV.pv) { // Opening configuration dialog window
			@Override
			public void run() {

				try {
					var handler = new NewBlockHandler();
					handler.setLocal(false);
					handler.createDialog(facilityPV.pv);

				} catch (TimeoutException e) {
					LoggerUtils.logErrorWithStackTrace(LOG, e.getMessage(), e);
				} catch (IOException e) {
					LoggerUtils.logErrorWithStackTrace(LOG, e.getMessage(), e);
				}
				super.run();
			}
		});

		add(new Action("Open in Log Plotter: " + facilityPV.pv) { // Opening log plotter window
			public void run() {
				if (BlocksMenu.canAddPlot()) {
					switchToLogPlotter();
					Presenter.pvHistoryPresenter().newDisplay(facilityPV.pv, facilityPV.pv);
				} else {
					Shell shell = PlatformUI.getWorkbench().getDisplay().getActiveShell();
					MessageBox messageBox = new MessageBox(shell, SWT.ICON_WARNING | SWT.OK);
					messageBox.setText("Failed to open in Log Plotter");
					messageBox.setMessage("Make the Log Plotter perspective visible.");
					messageBox.open();
				}
			}
		});
		add(new Action("Copy to clipboard: " + facilityPV.pv) { // Opening log plotter window
			public void run() {
				StringSelection selection = new StringSelection(facilityPV.pv);
				Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
				clipboard.setContents(selection, selection);
			}
		});
	}

	/**
	 * Switching perspective to log plotter
	 */
	private static void switchToLogPlotter() {
		final IWorkbench workbench = PlatformUI.getWorkbench();
		final IPerspectiveRegistry registry = workbench.getPerspectiveRegistry();
		final IWorkbenchPage page = workbench.getActiveWorkbenchWindow().getActivePage();
		page.setPerspective(registry.findPerspectiveWithId(LOG_PLOTTER_PERSPECTIVE_ID));
	}

}
