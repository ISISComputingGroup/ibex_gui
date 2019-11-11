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

package uk.ac.stfc.isis.ibex.ui.logplotter;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.IPerspectiveRegistry;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;

import uk.ac.stfc.isis.ibex.logger.IsisLog;

/**
 * This class is needed to display OPI PVs in the databrowser.
 */
public class OpenLogPlotterPopup extends org.csstudio.trends.databrowser3.OpenDataBrowserPopup {
	
	private static final String LOG_PLOTTER_PERSPECTIVE_ID = "uk.ac.stfc.isis.ibex.client.e4.product.perspective.logplotter";

	/**
	 * Override CSStudio command to switch to log plotter perspective first, before adding items to databrowser.
	 */
	@Override
    public Object execute(final ExecutionEvent event) {       
		try {
			switchToLogPlotter();
			super.execute(event);
			// We can't get the editor from the parent so turn off save changes dialog for all editors
			LogPlotterHistoryPresenter.getCurrentDataBrowsers().forEach(db -> db.getModel().setSaveChanges(false));
			return null;
		} catch (ExecutionException | RuntimeException e) {
			IsisLog.getLogger(getClass()).error(e.getMessage(), e);
			return null;
		}
    }
	
	/**
	 * This is a bit of a hack to switch perspectives in a way that is compatible with both E4 and the E3 compatibility layer.
	 * 
	 * Can't use our normal perspective provider as that requires dependency injection, which we can't use with E3-style extension
	 * points. Can't move away from E3 extension points as this is what the CSStudio view uses.
	 */
	private static void switchToLogPlotter() {
		final IWorkbench workbench = PlatformUI.getWorkbench();
        final IPerspectiveRegistry registry = workbench.getPerspectiveRegistry();
        final IWorkbenchPage page = workbench.getActiveWorkbenchWindow().getActivePage();
        page.setPerspective(registry.findPerspectiveWithId(LOG_PLOTTER_PERSPECTIVE_ID));
	}
}