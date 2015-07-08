
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
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.core.databinding.beans.PojoProperties;

import uk.ac.stfc.isis.ibex.configserver.configuration.ConfigInfo;

public class ConfigSelectionDialog extends Dialog {
	
	private final String title;
	private final Collection<ConfigInfo> available;
	
	private Text selectedText;
	private List items;
	private boolean isComponent;

	private String selectedName;
	
	public ConfigSelectionDialog(
			Shell parentShell, 
			String title,
			Collection<ConfigInfo> available, boolean isComponent) {
		super(parentShell);
		this.title = title;
		this.available = available;
		this.isComponent = isComponent;
	}
	
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
		String [] names = ConfigInfo.names(available).toArray(new String[0]);
		Arrays.sort(names);
		items.setItems(names);
		
		Composite selected = new Composite(container, SWT.NONE);
		selected.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		GridLayout gl_selected = new GridLayout(2, false);
		gl_selected.marginWidth = 0;
		gl_selected.marginHeight = 0;
		selected.setLayout(gl_selected);
		
		Label lblType = new Label(selected, SWT.NONE);
		lblType.setText(getTypeString() + ":");
		
		selectedText = new Text(selected, SWT.BORDER | SWT.READ_ONLY);
		selectedText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		initDataBindings();
		
		items.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				okPressed();
			}
		});
	}
	
	 
	protected DataBindingContext initDataBindings() {
		DataBindingContext bindingContext = new DataBindingContext();
		
		IObservableValue observeSelectionItemsObserveWidget = WidgetProperties.selection().observe(items);
		IObservableValue textSelectedObserveValue = PojoProperties.value("text").observe(selectedText);
		bindingContext.bindValue(observeSelectionItemsObserveWidget, textSelectedObserveValue, null, null);
		
		return bindingContext;
	}
	
	private String getTypeString() {
		return isComponent ? "Component" : "Configuration";
	}
}
