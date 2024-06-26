
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

import uk.ac.stfc.isis.ibex.instrument.Instrument;

/**
 * The workbench advisor for the application.
 */
public class ApplicationWorkbenchAdvisor extends WorkbenchAdvisor {

    org.eclipse.ui.application.IWorkbenchConfigurer configurer;
	
	private static final String DIALOG_BOX_TITLE = "Close the application?";
	private static final String DIALOG_QUESTION = """
			Are you sure you want to close this application?
			
			Closing this GUI will not terminate the IBEX server processes that are communicating with devices and logging data in the background.
			
			If you wish to shutdown all IBEX components use the \"stop ibex server\" link in the Windows start menu.
			If you wish to start or restart IBEX, use the \"start ibex server\" link in the Windows start menu"
												""";

	private ApplicationWorkbenchWindowAdvisor windowAdvisor;
	
    @Override
    public WorkbenchWindowAdvisor createWorkbenchWindowAdvisor(IWorkbenchWindowConfigurer configurer) {
    	windowAdvisor = new ApplicationWorkbenchWindowAdvisor(configurer);
        return windowAdvisor;
    }

	@Override
    public String getInitialWindowPerspectiveId() {
		return null;
	}
	
	@Override
	public void initialize(org.eclipse.ui.application.IWorkbenchConfigurer configurer) {
	    super.initialize(configurer);
        this.configurer = configurer;

        // set save and restore to false here to avoid restoring
        // window layout restored in ApplicationWorkbenchWindowAdvisor
        configurer.setSaveAndRestore(false);
	}
	
	@Override
	public boolean preShutdown() {
		Instrument.getInstance().setInitial();
		
        // set save and restore true here to make sure we save settings
        // these are actually restored in ApplicationWorkbenchWindowAdvisor
        configurer.setSaveAndRestore(true);

		Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
		if (windowAdvisor.shutDown) {
			return true;
		}
		return MessageDialog.openQuestion(shell, DIALOG_BOX_TITLE, DIALOG_QUESTION);
	}
}
