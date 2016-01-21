
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

package uk.ac.stfc.isis.ibex.ui.configserver.editing.pvs;

import java.util.Collection;

import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.widgets.Composite;

import uk.ac.stfc.isis.ibex.configserver.configuration.PVDefaultValue;
import uk.ac.stfc.isis.ibex.ui.tables.DataboundCellLabelProvider;
import uk.ac.stfc.isis.ibex.ui.tables.DataboundTable;

public class IocPVsTable extends DataboundTable<PVDefaultValue> {
	public IocPVsTable(Composite parent, int style, int tableStyle) {
		super(parent, style, PVDefaultValue.class, tableStyle | SWT.BORDER);

		initialise();
	}

	@Override
	protected void addColumns() {
		name();
		value();
	}
	
	@Override
	public void setRows(Collection<PVDefaultValue> rows) {
		super.setRows(rows);
	}
	
	private void name() {
		TableViewerColumn name = createColumn("Name", 3);
		name.setLabelProvider(new DataboundCellLabelProvider<PVDefaultValue>(observeProperty("name")) {
			@Override
			protected String valueFromRow(PVDefaultValue row) {
				return row.getName();
			}
		});	
	}
	
	private void value() {
		TableViewerColumn name = createColumn("Value", 3);
		name.setLabelProvider(new DataboundCellLabelProvider<PVDefaultValue>(observeProperty("value")) {
			@Override
			protected String valueFromRow(PVDefaultValue row) {
				return row.getValue();
			}
		});	
	}
	
	public void setSelection(int i) {
		table().setSelection(i);
	}

    @Override
    public void addKeyListener(KeyListener listener) {
        this.viewer().getTable().addKeyListener(listener);
    }
}
