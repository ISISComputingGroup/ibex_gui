
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

package uk.ac.stfc.isis.ibex.ui.scriptgenerator;

import org.eclipse.core.databinding.observable.map.IObservableMap;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Combo;

import uk.ac.stfc.isis.ibex.scriptgenerator.Row;
import uk.ac.stfc.isis.ibex.ui.widgets.ComboCellLabelProvider;

public class TransWaitLabelProvider extends ComboCellLabelProvider {

	protected TransWaitLabelProvider(IObservableMap[] attributeMaps) {
		super(attributeMaps);
	}
	
	@Override
	public void update(final ViewerCell cell) {
		super.update(cell);
		
		final Row row = (Row) cell.getElement();
		final Combo combo = getControl(cell, SWT.CHECK);
		
		combo.setSelection(row.getTransWait());
		
		checkBox.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				row.setTransWait(combo.getSelection());
			}
		});
	}

}
