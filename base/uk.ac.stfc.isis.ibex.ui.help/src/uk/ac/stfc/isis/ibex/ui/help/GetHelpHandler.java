
/*
* This file is part of the ISIS IBEX application.
* Copyright (C) 2012-2019 Science & Technology Facilities Council.
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

import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.swt.widgets.Shell;

/**
 * The handler for opening the get help dialog window via the menu.
 */
public class GetHelpHandler {
    
    /**
     * Opens the 'Get help' dialog window in help menu.
     * @param shell The shell to open 'Get help' window in help menu.
     */
    @Execute
    public void execute(Shell shell) {
        GetHelpDialogBox dialog = new GetHelpDialogBox(shell);
        dialog.open();
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
