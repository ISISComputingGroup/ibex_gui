
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

package uk.ac.stfc.isis.ibex.e4.product;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchAdvisor;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;

public class ApplicationWorkbenchAdvisor extends WorkbenchAdvisor {
	
	private static final String DIALOG_BOX_TITLE = "Close the application?";
	private static final String DIALOG_QUESTION = "Are you sure you want to close this application?";
	
	/**
	 * Override the workbench window advisor from an E3 one to an E4 one.
	 * 
	 * Leaving it as an E3 one causes extra menus to appear from CS-Studio.
	 * 
	 * @return the E4 Application Workbench Window Advisor.
	 */
    @Override
    public WorkbenchWindowAdvisor createWorkbenchWindowAdvisor(IWorkbenchWindowConfigurer configurer) {
        return new ApplicationWorkbenchWindowAdvisor(configurer);
    }

    /**
     * Gets the initial perspective to display.
     * 
     * @return null, this causes eclipse to not display a default perspective as we handle this ourselves.
     */
	@Override
    public String getInitialWindowPerspectiveId() {
		return null;
	}
	
	@Override
	public void preStartup() {
		getWorkbenchConfigurer().setSaveAndRestore(false);
	}
	
	/**
	 * Tasks to do before shutdown.
	 * 
	 * @return true to actually perform the shutdown, false otherwise.
	 */
	@Override
	public boolean preShutdown() {
		Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
        getWorkbenchConfigurer().setSaveAndRestore(false);
		return MessageDialog.openQuestion(shell, DIALOG_BOX_TITLE, DIALOG_QUESTION);
	}
}
