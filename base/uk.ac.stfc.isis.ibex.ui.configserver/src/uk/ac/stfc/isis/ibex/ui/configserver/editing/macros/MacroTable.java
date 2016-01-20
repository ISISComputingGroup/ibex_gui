
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

import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import uk.ac.stfc.isis.ibex.configserver.configuration.Macro;
import uk.ac.stfc.isis.ibex.ui.tables.DataboundCellLabelProvider;
import uk.ac.stfc.isis.ibex.ui.tables.DataboundTable;

@SuppressWarnings("checkstyle:magicnumber")
public class MacroTable extends DataboundTable<Macro> {
	
	public MacroTable(Composite parent, int style, int tableStyle) {
		super(parent, style, Macro.class, tableStyle | SWT.BORDER);

		initialise();
	}
	
	@Override
	public void setRows(Collection<Macro> rows) {
		super.setRows(rows);
	}

	@Override
	protected void addColumns() {
		name();
		value();
		description();
		pattern();
	}
	
	private void name() {
		TableViewerColumn desc = createColumn("Macro name", 6);
		desc.setLabelProvider(new DataboundCellLabelProvider<Macro>(observeProperty("name")) {
			@Override
			protected String valueFromRow(Macro row) {
				return row.getName();
			}
		});
	}
	
	private void value() {
		TableViewerColumn desc = createColumn("Value", 5);
		desc.setLabelProvider(new DataboundCellLabelProvider<Macro>(observeProperty("value")) {
			@Override
			protected String valueFromRow(Macro row) {
				return row.getValue();
			}
		});
	}
	
	private void description() {
		TableViewerColumn desc = createColumn("Description", 8);
		desc.setLabelProvider(new DataboundCellLabelProvider<Macro>(observeProperty("description")) {
			@Override
			protected String valueFromRow(Macro row) {
				return row.getDescription();
			}
		});	
	}
	
	private void pattern() {
		TableViewerColumn desc = createColumn("Pattern", 8);
		desc.setLabelProvider(new DataboundCellLabelProvider<Macro>(observeProperty("pattern")) {
			@Override
			protected String valueFromRow(Macro row) {
				return row.getPattern();
			}
		});	
	}
}
