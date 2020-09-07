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
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TableItem;

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
     * The time stamps relating to when recent configs were last modified.
     */
    protected List<String> recentTimestamps;
     /**
     * The currently selected items.
     */
    protected Collection<String> selectedConfigs = new ArrayList<>();
     /**
     * Allows for more than one item to be selected. In this class we set
     * extraListOptions to 0 as we only want to select and load one item.
     */
    protected int extraListOptions;
     /**
     * The constructor that creates a dialog allowing to load a recently used
     * config.
     * 
     * @param parentShell
     *            The shell to create the dialog in.
     * @param title
     *            The title of the dialog box.
     * @param recentConfigs
     *            A list of the recently loaded configurations for
     *            the user to select from.
     * @param recentTimestamps
     *            A list of the time stamps of when recent configs were last
     *            modified.
     */
    public RecentConfigSelectionDialog(Shell parentShell, String title, List<String> recentConfigs,
            List<String> recentTimestamps) {
        super(parentShell, title);
        this.recentConfigs = recentConfigs;
        this.recentTimestamps = recentTimestamps;
        this.extraListOptions = 0;
    }
     /**
     * @return A collection of the configurations/components that the user has
     *         selected.
     */
    public Collection<String> selectedConfigs() {
        return selectedConfigs;
    }
     @Override
    protected void okPressed() {
        Collection<String> selectedItems = asString(items.getSelection());
        
        /* The following ensures that double clicking on a time stamp/white space doesn't
         * launch the load process using a time stamp/null as a configuration name.
         */
        boolean anyMatch = selectedItems.stream().anyMatch(recentConfigs::contains);
        if (anyMatch) {
        	selectedConfigs = asString(items.getSelection());
        	super.okPressed();
        }
        
    }
     @Override
    protected void createSelection(Composite container) {
    	 Label lblSelect = new Label(container, SWT.NONE);
         lblSelect.setText("Select configuration:");
         List<String> columnNames = Arrays.asList("Configuration", "Last updated");
         items = createTable(container, SWT.BORDER | SWT.V_SCROLL | extraListOptions, columnNames);
         setMultipleColumnItems(recentConfigs, recentTimestamps);
     }
      /**
      * Get the name of the configuration/component that the user has chosen.
      * 
      * @return The chosen configuration/component. Only returns first item of
      *         the list as we only want to select one item.
      */
     public String selectedConfig() {
         return selectedConfigs.toArray(new String[1])[0];
     }
     
      /**
      * Sets a list of configurations and their time stamps as items in a
      * selection table with two columns.
      * 
      * @param names
      *            The names of the recent configurations.
      * @param timestamps
      *            The time stamps for the recent configurations.
      */
     protected void setMultipleColumnItems(List<String> names, List<String> timestamps) {
         this.items.clearAll();
         int i = 0;
         String[] columns = new String[2];
         if (timestamps.size() == 0) {
             setItems(names);
         } else {
             for (String name : names) {
                 TableItem item = new TableItem(this.items, SWT.NONE);
                 columns[0] = name;
                 columns[1] = timestamps.get(i);
                 item.setText(columns);
                 
                 i++;
             }
         }
     }
     
	 /**
	  * {@inheritDoc}
	  */
	 @Override
	 protected boolean isResizable() {
	     return true;
	 }
}
