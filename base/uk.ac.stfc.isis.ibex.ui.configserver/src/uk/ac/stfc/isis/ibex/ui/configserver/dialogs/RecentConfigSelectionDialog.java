
/*
* This file is part of the ISIS IBEX application.
* Copyright (C) 2012-2017 Science & Technology Facilities Council.
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import uk.ac.stfc.isis.ibex.ui.dialogs.SelectionDialog;

/**
 * Dialog for asking the user to select a multiple configurations or components.
 */
public class RecentConfigSelectionDialog extends SelectionDialog {

    /**
     * The collection of the available configurations/components for the user to
     * select from.
     */
    protected List<String> recentConfigs;

    /**
     * The currently selected items.
     */
    protected Collection<String> selectedConfigs = new ArrayList<>();

    /**
     * Allows for more than one item to be selected. In this class we set extraListOptions to 0 as we only want to select and load one item.
     */
    protected int extraListOptions;

    /**
     * The constructor that creates a dialog allowing to load a recently used config.
     * @param parentShell The shell to create the dialog in.
     * @param title The title of the dialog box.
     * @param recentConfigs A collection of the available configurations/components
     *            for the user to select from.
     */
    public RecentConfigSelectionDialog(
            Shell parentShell, 
            String title,
            List<String> recentConfigs) {
        super(parentShell, title);
        this.recentConfigs = recentConfigs;
        this.extraListOptions = 0;
    }

    /**
     * @return A collection of the configurations/components that the user has selected.
     */
    public Collection<String> selectedConfigs() {
        return selectedConfigs;
    }

    @Override
    protected void okPressed() {
        selectedConfigs = asString(items.getSelection());
        super.okPressed();
    }

    @Override
    protected void createSelection(Composite container) {
        Label lblSelect = new Label(container, SWT.NONE);
        lblSelect.setText("Select one of the last five recently loaded configuration:");
        items = createTable(container, SWT.BORDER | SWT.V_SCROLL | extraListOptions);
        String[] names;
        names = recentConfigs.toArray(new String[0]);
        setItems(names);
    }

    /**
     * Get the name of the configuration/component that the user has chosen.
     * 
     * @return The chosen configuration/component. Only returns first item of the list as we only want to select one item.
     */
    public String selectedConfig() {
        return selectedConfigs.toArray(new String[1])[0];
    }

}
