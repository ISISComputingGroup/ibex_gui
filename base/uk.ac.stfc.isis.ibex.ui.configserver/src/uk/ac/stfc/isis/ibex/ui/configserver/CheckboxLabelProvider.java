
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

package uk.ac.stfc.isis.ibex.ui.configserver;

import org.eclipse.core.databinding.observable.map.IObservableMap;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;

import uk.ac.stfc.isis.ibex.ui.widgets.ButtonCellLabelProvider;

/**
 * A LabelProvider that adds a checkbox to a cell in a table. 
 *
 * @param <T> The model to get the/set the information for the checkbox
 */
public abstract class CheckboxLabelProvider<T> extends ButtonCellLabelProvider<T> {
	
	/**
	 * The default constructor for the CheckboxLabelProvider.
	 * @param stateProperties The properties that this label provider should be observing
	 */
	public CheckboxLabelProvider(IObservableMap stateProperties) {
		super(stateProperties);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void update(final ViewerCell cell) {
		super.update(cell);

		cell.setText("");
		final T ioc = (T) cell.getElement();		
		final Button checkBox = (Button) getControl(cell, SWT.CHECK);
		
		checkBox.setSelection(checked(ioc));	
		checkBox.setText(stringFromRow(ioc));

		checkBox.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				setChecked(ioc, checkBox.getSelection());
				checkBox.setText(stringFromRow(ioc));
			}
		});
		
		checkBox.setEnabled(isEditable(ioc));
	}
	
	@Override
	protected String stringFromRow(T model) {
		return checked(model) ? "Yes" : "No";
	}
	
	protected abstract boolean checked(T model);
	
	protected abstract void setChecked(T model, boolean checked);
	
	protected abstract boolean isEditable(T model);
}
