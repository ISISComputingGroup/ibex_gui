 /*
 * This file is part of the ISIS IBEX application.
 * Copyright (C) 2012-2016 Science & Technology Facilities Council.
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

/**
 * 
 */
package uk.ac.stfc.isis.ibex.ui.mainmenu.managermode;

import org.eclipse.swt.widgets.Shell;
import uk.ac.stfc.isis.ibex.managermode.IManagerModeModel;

/**
 * A dialog to ask the user to temporarily authenticate (for example, to save layouts).
 */
public class TemporaryAuthenticationDialog extends ManagerModeDialog {
    
    private final String areaTitle;

    /**
     * Constructor.
     * 
     * @param parentShell
     *            the parent shell
     * @param model
     *            the view model
     * @param areaTitle
     * 			  the title of the dialog area
     */
    public TemporaryAuthenticationDialog(Shell parentShell, IManagerModeModel model, String areaTitle) {
        super(parentShell, model);
        this.areaTitle = areaTitle;

    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public String getAreaTitle() {
    	return areaTitle;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getWindowTitle() {
    	return "Authentication";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getQuestion() {
    	return "Please enter the manager password:";
    }

}
