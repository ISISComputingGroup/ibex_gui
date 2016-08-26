
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

package uk.ac.stfc.isis.ibex.ui.configserver.dialogs;

import java.util.Collection;

import org.eclipse.swt.widgets.Shell;

import uk.ac.stfc.isis.ibex.configserver.configuration.ConfigInfo;

/**
 * Dialog for asking the user to select a single configuration or component.
 */
public class ConfigSelectionDialog extends MultipleConfigsSelectionDialog {
	
	/**
	 * @param parentShell The shell to create the dialog in.
	 * @param title The title of the dialog box.
	 * @param available A collection of the available configurations/components for the user to select from.
	 * @param isComponent Whether the user is selecting from a list of components.
	 * @param includeCurrent Whether the current configuration should be included in the list presented to the user.
	 */
	public ConfigSelectionDialog(
			Shell parentShell, 
			String title,
            Collection<ConfigInfo> available, boolean isComponent, boolean includeCurrent) {
        super(parentShell, title, available, isComponent, includeCurrent);
	}
	
	/**
     * Get the name of the configuration/component that the user has chosen.
     * 
     * @return The chosen configuration/component.
     */
	public String selectedConfig() {
        return selected.toArray(new String[1])[0];
    }

    /**
     * @return A string corresponding to the type of item in the list.
     */
    @Override
    protected String getTypeString() {
        String plural = super.getTypeString();

        // Remove final s, if it exists
        String singular;
        if (plural != null && plural.length() > 0 && plural.charAt(plural.length() - 1) == 's') {
            singular = plural.substring(0, plural.length() - 1);
        } else {
            singular = plural;
        }

        return singular;
    }
}
