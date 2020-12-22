
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
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import uk.ac.stfc.isis.ibex.configserver.configuration.ConfigInfo;
import uk.ac.stfc.isis.ibex.ui.dialogs.SelectionDialog;


/**
 * Dialog for asking the user to select a multiple configurations or components.
 */
public class MultipleConfigsSelectionDialog extends SelectionDialog {
	
    /**
     * The collection of the available configurations/components for the user to
     * select from.
     */
    protected final Collection<ConfigInfo> available;

    /**
     * Is the dialog to do with components? (as opposed to configs)
     */
    protected boolean isComponent;

    /**
     * The currently selected items.
     */
    protected Collection<String> selected = new ArrayList<>();
	
    /**
     * Include the current config in the list of available items.
     */
    protected boolean includeCurrent;

    /**
     * Any additional options to be applied when generating the selection list
     * template.
     */
    protected int extraListOptions;
    
    /**
     * Component or config names as key and protection flag as value.
     */
    protected Map<String, Boolean> compOrConfigNamesWithFlags;

	/**
     * @param parentShell The shell to create the dialog in.
     * @param title The title of the dialog box.
     * @param available A collection of the available configurations/components
     *            for the user to select from.
     * @param isComponent Whether the user is selecting from a list of
     *            components.
     * @param includeCurrent Whether the current config/component should be
     *            included in the list of available items
     * @param compOrConfigNamesWithFlags Component or config names as key and protection flag as value.
     */
	public MultipleConfigsSelectionDialog(
			Shell parentShell, 
			String title,
            Collection<ConfigInfo> available,
            Map<String, Boolean> compOrConfigNamesWithFlags,
            boolean isComponent, boolean includeCurrent) {
		super(parentShell, title);
		this.available = available;
		this.isComponent = isComponent;
        this.includeCurrent = includeCurrent;
        this.extraListOptions = SWT.MULTI;
        this.compOrConfigNamesWithFlags = compOrConfigNamesWithFlags;
	}

	/**
	 * @return A collection of the configurations/components that the user has selected.
	 */
	public Collection<String> selectedConfigs() {
		return selected;
	}

    @Override
   protected void okPressed() {
       Collection<String> selectedItems = asString(items.getSelection());
       
       /* The following ensures that double clicking on the description/white space doesn't
        * launch the process using a description/null as the name.
        */    
       List<String> names = ConfigInfo.names(available);
       boolean anyMatch = selectedItems.stream().anyMatch(names::contains);
       if (anyMatch) {
    	   selected = asString(items.getSelection());
    	   super.okPressed();
       }
   }

	@Override
    protected void createSelection(Composite container) {
	    
		Label lblSelect = new Label(container, SWT.NONE);
        lblSelect.setText("Select " + getTypeString() + ":");
        List<String> columnNames = Arrays.asList("Name", "Description");
        items = createTable(container, SWT.BORDER | SWT.V_SCROLL | extraListOptions, columnNames);

        SortedMap<String, String> namesAndDescriptions = new TreeMap<String, String>(String.CASE_INSENSITIVE_ORDER);
        if (includeCurrent) {
            namesAndDescriptions = ConfigInfo.namesAndDescriptions(available);
        } else {
        	namesAndDescriptions = ConfigInfo.namesAndDescriptionsWithoutCurrent(available);
        }
      
		setMultipleColumnItems(namesAndDescriptions, compOrConfigNamesWithFlags);
		
		// show protected configuration message only if relevant (the list has protected configurations)
		Boolean hasProtectedConfigs = ConfigInfo.hasProtectedElement(available);
		if (hasProtectedConfigs) {
			Group group = new Group(container, SWT.SHADOW_IN);
			group.setLayout(new RowLayout(SWT.HORIZONTAL));
			
			Label messageImageLabel = new Label(group, SWT.NONE);
			messageImageLabel.setImage(JFaceResources.getImage(DLG_IMG_MESSAGE_WARNING)); 
			
			Label messageLabel = new Label(group, SWT.NONE);
			messageLabel.setFont(JFaceResources.getDialogFont());
			messageLabel.setText("Represents Protected " + getTypeString());
		}
			
	}
	
    /**
     * @return A string corresponding to the type of item in the list.
     */
    protected String getTypeString() {
        return isComponent ? "components" : "configurations";
	}
}
