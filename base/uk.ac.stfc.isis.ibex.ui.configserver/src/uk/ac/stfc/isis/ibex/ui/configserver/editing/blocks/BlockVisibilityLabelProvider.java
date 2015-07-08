
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

package uk.ac.stfc.isis.ibex.ui.configserver.editing.blocks;

import org.eclipse.core.databinding.observable.map.IObservableMap;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;

import uk.ac.stfc.isis.ibex.configserver.configuration.Block;
import uk.ac.stfc.isis.ibex.configserver.editing.EditableBlock;
import uk.ac.stfc.isis.ibex.ui.configserver.editing.ButtonCellLabelProvider;

public class BlockVisibilityLabelProvider extends ButtonCellLabelProvider {

	protected BlockVisibilityLabelProvider(IObservableMap[] attributeMaps) {
		super(attributeMaps);
	}

	@Override
	public void update(final ViewerCell cell) {
		super.update(cell);

		cell.setText("");
		final EditableBlock block = (EditableBlock) cell.getElement();		
		final Button checkBox = getButton(cell, SWT.CHECK);
		
		checkBox.setSelection(block.getIsVisible());	
		checkBox.setText(isVisible(block));

		checkBox.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				block.setIsVisible(checkBox.getSelection());
				checkBox.setText(isVisible(block));
			}
		});
		
		checkBox.setEnabled(block.isEditable());
	}
	
	private String isVisible(Block block) {
		return block.getIsVisible() ? "Yes" : "No";
	}
}
