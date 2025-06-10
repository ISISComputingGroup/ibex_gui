
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

package uk.ac.stfc.isis.ibex.ui.help;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.browser.IWebBrowser;

import uk.ac.stfc.isis.ibex.logger.IsisLog;
import uk.ac.stfc.isis.ibex.logger.LoggerUtils;

/**
 * The handler for opening the user manual via the menu.
 */
public class ManualHandler {

    private static final String USER_MANUAL_ADDRESS = "https://shadow.nd.rl.ac.uk/ibex_user_manual";

    /**
     * Opens the 'User Manual' URL in help menu.
     */
	@Execute
	public void execute() {

        URL url = null;
        try {
            url = new URI(USER_MANUAL_ADDRESS).toURL();
        } catch (MalformedURLException | URISyntaxException ex) {
            LoggerUtils.logErrorWithStackTrace(IsisLog.getLogger(getClass()), ex.getMessage(), ex);
        }

        try {
            IWebBrowser browser = PlatformUI.getWorkbench().getBrowserSupport().getExternalBrowser();
            browser.openURL(url);
        } catch (PartInitException ex) {
        	LoggerUtils.logErrorWithStackTrace(IsisLog.getLogger(getClass()), ex.getMessage(), ex);
        }              
	}
	
	/**
	 * Help menu option always available (not instrument dependent).
	 * @return True
	 */
	@CanExecute
	public boolean canExecute() {
		return true;
	}
}
