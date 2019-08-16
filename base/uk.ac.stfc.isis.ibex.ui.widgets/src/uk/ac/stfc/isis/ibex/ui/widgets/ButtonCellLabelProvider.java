
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

package uk.ac.stfc.isis.ibex.ui.widgets;

import org.eclipse.core.databinding.observable.map.IObservableMap;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;

/**
 * A cell provider that puts a button within a table cell.
 * 
 * @param <T> the type of the data in the row
 */
public abstract class ButtonCellLabelProvider<T> extends ControlCellLabelProvider<Button, T> {
	
    /**
     * 
     * @param attributeMaps A map of the attributes that this cell will observe.
     */
	protected ButtonCellLabelProvider(IObservableMap attributeMaps) {
		super(attributeMaps);
	}

	@Override
	protected Button createControl(final ViewerCell cell, int style) {
		Button button = new Button(composite(cell), style);
		button.setText("");
		button.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		
		return button;
	}
}