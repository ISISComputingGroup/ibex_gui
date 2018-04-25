
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

package uk.ac.stfc.isis.ibex.ui.ioccontrol.table;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.eclipse.core.databinding.observable.map.IObservableMap;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TableColumn;

import uk.ac.stfc.isis.ibex.configserver.IocState;
import uk.ac.stfc.isis.ibex.ui.ioccontrol.StateLabelProvider;
import uk.ac.stfc.isis.ibex.ui.tables.ColumnComparator;
import uk.ac.stfc.isis.ibex.ui.tables.DataboundCellLabelProvider;
import uk.ac.stfc.isis.ibex.ui.tables.DataboundTable;

/**
 * Table allowing IOCs to be started and stopped.
 * 
 * Note that all columns in this table are not resizable as the H_SCROLL 
 * has been removed and resizing could cause columns to disappear.
 * 
 * The H_SCROLL has been removed as it was appearing despite no extra 
 * data being in the table (unsure why)
 */
@SuppressWarnings("checkstyle:magicnumber")
public class IocTable extends DataboundTable<IocState> {

	//private IocComparator comparator = new IocComparator();
	
	/**
	 * A table that shows the status of all IOCs on the instrument.
	 * @param parent the parent composite for the table.
	 * @param style The style of the viewer.
	 * @param tableStyle The style of the table.
	 */
	public IocTable(Composite parent, int style, int tableStyle) {
        super(parent, style, IocState.class, tableStyle | SWT.NO_SCROLL | SWT.V_SCROLL);	
		initialise();
	}
	
	@Override
    public void setRows(Collection<IocState> rows) {
        List<IocState> states = new ArrayList<>(rows);
		Collections.sort(states);
		super.setRows(states);
		refresh();
	}
	
	@Override
	protected void addColumns() {
		name();
		description();
		state();
	}

	private void name() {
		TableViewerColumn name = createColumn("Name", 4, false);
        name.setLabelProvider(new DataboundCellLabelProvider<IocState>(observeProperty("name")) {
			@Override
            protected String valueFromRow(IocState row) {
				return row.getName();
			}
		});
	}
	
	private void description() {
		TableViewerColumn description = createColumn("Description", 4, false);
        description.setLabelProvider(new DataboundCellLabelProvider<IocState>(observeProperty("description")) {
			@Override
            protected String valueFromRow(IocState row) {
				return row.getDescription();
			}
		});	
	}
	
	private void state() {
		TableViewerColumn status = createColumn("Status", 2, false);
		IObservableMap[] stateProperties = {observeProperty("isRunning")};
		status.setLabelProvider(new StateLabelProvider(stateProperties));
	}
	
	@Override
	protected ColumnComparator comparator() {
		return new IocComparator();
	}
}
