
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

package uk.ac.stfc.isis.ibex.ui.configserver.editing.macros;

import java.util.Collection;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.google.common.base.Strings;

import uk.ac.stfc.isis.ibex.configserver.configuration.Macro;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

/**
 * This panel allows macro names and values to be set, and shows a list of available macros for an
 * IOC.
 * 
 * In the list of available macros a description and pattern is also shown. If the macro being set
 * is in this list, the macro value is also checked against the pattern. 
 * 
 */
public class IocMacroDetailsPanel extends Composite {
	private Text name;
	private Text value;
	private DataBindingContext bindingContext;
	private MacroTable displayMacrosTable;
	private UpdateValueStrategy valueStrategy = new UpdateValueStrategy();
	private MacroValueValidator valueValidator;
	private Button setMacroButton;
	private Label macroValueErrorLabel;
	
	private Macro macro;
	
	public IocMacroDetailsPanel(Composite parent, int style) {
		super(parent, style);
		setLayout(new FillLayout(SWT.HORIZONTAL));
		
		Group grpSelectedPv = new Group(this, SWT.NONE);
		grpSelectedPv.setText("Selected Macro");
		grpSelectedPv.setLayout(new GridLayout(3, false));
		
		Label lblName = new Label(grpSelectedPv, SWT.NONE);
		lblName.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblName.setText("Name");
		
		name = new Text(grpSelectedPv, SWT.BORDER);
		name.setEditable(false);
		name.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		new Label(grpSelectedPv, SWT.NONE);
		
		Label lblValue = new Label(grpSelectedPv, SWT.NONE);
		lblValue.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblValue.setText("Value");
		
		value = new Text(grpSelectedPv, SWT.BORDER);
		value.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		value.setEnabled(false);
		
		setMacroButton = new Button(grpSelectedPv, SWT.NONE);
		setMacroButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				macro.setValue(value.getText());
			}
		});
		setMacroButton.setText("Set Macro");
		new Label(grpSelectedPv, SWT.NONE);
		
		macroValueErrorLabel = new Label(grpSelectedPv, SWT.NONE);
		GridData gd_macroValueErrorLabel = new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1);
		gd_macroValueErrorLabel.heightHint = 20;
		gd_macroValueErrorLabel.widthHint = 300;
		macroValueErrorLabel.setLayoutData(gd_macroValueErrorLabel);
		macroValueErrorLabel.setText("placeholder placeholder placeholder placeholder");
		
		displayMacrosTable = new MacroTable(grpSelectedPv, SWT.NONE, 0);
		GridData gd_availableMacrosTable = new GridData(SWT.FILL, SWT.FILL, true, true, 3, 1);
		gd_availableMacrosTable.widthHint = 428;
		displayMacrosTable.setLayoutData(gd_availableMacrosTable);
		new Label(grpSelectedPv, SWT.NONE);
		new Label(grpSelectedPv, SWT.NONE);
		new Label(grpSelectedPv, SWT.NONE);
		displayMacrosTable.addSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent arg0) {
				IStructuredSelection selection = (IStructuredSelection) arg0.getSelection();
				if (selection.size() > 0) {
					Macro macro = (Macro) selection.getFirstElement();
					name.setText(macro.getName());
					setMacro(macro, true);
				}
			}
		});
	}
	
	public void setMacros(Collection<Macro> macros, boolean canEdit) {
		this.macro = null;
		
		if (bindingContext != null) {
			bindingContext.dispose();
		}
		
		if (macro == null) {
			setValueEditable(false);
		} else {
			setValueEditable(canEdit);
		}
		
		setEnabled(canEdit);
				
		bindingContext = new DataBindingContext();
		valueValidator = new MacroValueValidator(macro, macroValueErrorLabel);
		valueStrategy.setBeforeSetValidator(valueValidator);
				
		bindingContext.bindValue(WidgetProperties.text(SWT.Modify).observe(value), BeanProperties.value("value").observe(macro), valueStrategy, null);
		
		displayMacrosTable.setRows(macros);
	}
	
	public void setMacro(Macro macro, boolean canEdit) {
		this.macro = macro;
		
		if (macro == null) {
			setValueEditable(false);
		} else {
			setValueEditable(canEdit);
		}
		
		valueValidator.setMacro(macro);
		
		value.setText(Strings.nullToEmpty(macro.getValue()));
	}
	
	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		displayMacrosTable.setEnabled(enabled);
	}
	
	private void setValueEditable(boolean enabled) {
		value.setEnabled(enabled);
		if (!enabled) {
			value.setText("");
		}
	}
}
