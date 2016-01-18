
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
import org.eclipse.swt.widgets.Composite;

import uk.ac.stfc.isis.ibex.configserver.EditableIocState;
import uk.ac.stfc.isis.ibex.ui.ioccontrol.StateLabelProvider;
import uk.ac.stfc.isis.ibex.ui.tables.DataboundCellLabelProvider;
import uk.ac.stfc.isis.ibex.ui.tables.DataboundTable;

@SuppressWarnings("checkstyle:magicnumber")
public class IocTable extends DataboundTable<EditableIocState> {

	public IocTable(Composite parent, int style, int tableStyle) {
		super(parent, style, EditableIocState.class, tableStyle);
		
		initialise();
	}

	@Override
	public void setRows(Collection<EditableIocState> rows) {
		List<EditableIocState> states = new ArrayList<>(rows);
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
		TableViewerColumn name = createColumn("Name", 4);
		name.setLabelProvider(new DataboundCellLabelProvider<EditableIocState>(observeProperty("name")) {
			@Override
			protected String valueFromRow(EditableIocState row) {
				return row.getName();
			}
		});		
	}
	
	private void description() {
		TableViewerColumn name = createColumn("Description", 4);
		name.setLabelProvider(new DataboundCellLabelProvider<EditableIocState>(observeProperty("description")) {
			@Override
			protected String valueFromRow(EditableIocState row) {
				return row.getDescription();
			}
		});		
	}
	
	private void state() {
		TableViewerColumn name = createColumn("Status", 2);
		IObservableMap[] stateProperties = {observeProperty("isRunning")};
		name.setLabelProvider(new StateLabelProvider(stateProperties));		
	}
}
