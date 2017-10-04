
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
import java.net.URL;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.browser.IWebBrowser;

/**
 * The handler for opening the user manual via the menu.
 */
public class ManualHandler {

    private static final String USER_MANUAL_ADDRESS = "http://shadow.nd.rl.ac.uk/ibex_user_manual/Home";

	@Execute
	public void execute() {

        URL url = null;
        try {
            url = new URL(USER_MANUAL_ADDRESS);
        } catch (MalformedURLException ex) {
            ex.printStackTrace();
        }

        try {
            IWebBrowser browser = PlatformUI.getWorkbench().getBrowserSupport().getExternalBrowser();
            browser.openURL(url);
        } catch (PartInitException ex) {
            ex.printStackTrace();
        }
      
	}

}
