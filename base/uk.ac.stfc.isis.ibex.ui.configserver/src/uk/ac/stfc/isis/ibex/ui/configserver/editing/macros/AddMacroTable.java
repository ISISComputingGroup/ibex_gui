
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

public class AddMacroTable extends DataboundTable<Macro> {
	private MacroAddressFilter filter;
	
	public AddMacroTable(Composite parent, int style, int tableStyle) {
		super(parent, style, Macro.class, tableStyle | SWT.BORDER);

		initialise();
	
		filter = new MacroAddressFilter();
		this.viewer().addFilter(filter);
	}
	
	@Override
	public void setRows(Collection<Macro> rows) {
		super.setRows(rows);
	}

	@Override
	protected void addColumns() {
		name();
		description();
		pattern();
	}
	
	private void name() {
		TableViewerColumn desc = createColumn("Macro name", 8);
		desc.setLabelProvider(new DataboundCellLabelProvider<Macro>(observeProperty("name")) {
			@Override
			protected String valueFromRow(Macro row) {
				return row.getName();
			}
		});	
	}
	
	private void description() {
		TableViewerColumn desc = createColumn("Description", 6);
		desc.setLabelProvider(new DataboundCellLabelProvider<Macro>(observeProperty("description")) {
			@Override
			protected String valueFromRow(Macro row) {
				return row.getDescription();
			}
		});	
	}
	
	private void pattern() {
		TableViewerColumn desc = createColumn("Pattern", 6);
		desc.setLabelProvider(new DataboundCellLabelProvider<Macro>(observeProperty("pattern")) {
			@Override
			protected String valueFromRow(Macro row) {
				return row.getPattern();
			}
		});	
	}
	
	public void setFilter(String search) {
		filter.setSearchText(search);
		this.viewer().refresh();
	}
}
