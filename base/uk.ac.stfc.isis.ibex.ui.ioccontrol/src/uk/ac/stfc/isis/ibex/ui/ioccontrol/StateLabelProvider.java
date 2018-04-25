
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

package uk.ac.stfc.isis.ibex.ui.ioccontrol;

import org.eclipse.core.databinding.observable.map.IObservableMap;
import org.eclipse.jface.databinding.viewers.ObservableMapCellLabelProvider;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.graphics.Color;
import org.eclipse.wb.swt.SWTResourceManager;

import uk.ac.stfc.isis.ibex.configserver.IocState;
import uk.ac.stfc.isis.ibex.ui.tables.SortableObservableMapCellLabelProvider;

public class StateLabelProvider extends SortableObservableMapCellLabelProvider<IocState> {

	public static final String TEXT_RUNNING = "Running";
	public static final String TEXT_NOT_RUNNING = "Stopped";
	
	public static final Color COLOR_RUNNING = SWTResourceManager.getColor(19, 145, 44); // Green
	public static final Color COLOR_STOPPED = SWTResourceManager.getColor(173, 66, 66); // RED

	public StateLabelProvider(IObservableMap attributeMaps) {
		super(attributeMaps);
	}

	@Override
	public void update(ViewerCell cell) {
		super.update(cell);

        IocState state = (IocState) cell.getElement();
        boolean isRunning = state != null && state.getIsRunning();
        cell.setText(stringFromRow(state));
		cell.setForeground(isRunning ? COLOR_RUNNING : COLOR_STOPPED);
	}

	@Override
	public String stringFromRow(IocState row) {
		boolean isRunning = row != null && row.getIsRunning();
		return isRunning ? TEXT_RUNNING : TEXT_NOT_RUNNING;
	}
}
