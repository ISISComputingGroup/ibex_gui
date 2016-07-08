
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

import java.util.Arrays;
import java.util.Collection;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import uk.ac.stfc.isis.ibex.configserver.configuration.ConfigInfo;

@SuppressWarnings("checkstyle:magicnumber")
/**
 * Dialog for asking the user to select a single configuration or component.
 */
public class ConfigSelectionDialog extends Dialog {
	
	private final String title;
	private final Collection<ConfigInfo> available;
	
	private Text selectedText;
    protected List items;
	private boolean isComponent;
    private boolean includeCurrent;

	private String selectedName;
	
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
		super(parentShell);
		this.title = title;
		this.available = available;
		this.isComponent = isComponent;
        this.includeCurrent = includeCurrent;
	}
	
	/**
	 * Get the configuartion/component that the user has chosen
	 * @return The chosen configuration/component.
	 */
	public String selectedConfig() {
		return selectedName;
	}
	
	@Override
	protected void configureShell(Shell shell) {
		super.configureShell(shell);
		shell.setText(title);
	}

	@Override
	protected Point getInitialSize() {
		return new Point(450, 300);
	}
	
	@Override
	protected void okPressed() {
		selectedName = selectedText.getText();
		super.okPressed();
	}
	
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		createConfigSelection(container);
		
		return container;
	}
	
	private void createConfigSelection(Composite container) {
		Label lblSelect = new Label(container, SWT.NONE);
		lblSelect.setText("Select a " + getTypeString().toLowerCase() + ":");
		
		items = new List(container, SWT.BORDER | SWT.V_SCROLL);
		items.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
        String[] names;
        if (includeCurrent) {
            names = ConfigInfo.names(available).toArray(new String[0]);
        } else {
            names = ConfigInfo.namesWithoutCurrent(available).toArray(new String[0]);
        }
		Arrays.sort(names, String.CASE_INSENSITIVE_ORDER);
		items.setItems(names);
		
		items.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				okPressed();
			}
		});
	}
	
	private String getTypeString() {
		return isComponent ? "Component" : "Configuration";
	}
}
