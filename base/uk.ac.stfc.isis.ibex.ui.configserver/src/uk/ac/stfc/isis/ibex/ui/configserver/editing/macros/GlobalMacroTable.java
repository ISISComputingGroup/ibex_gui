/*
* This file is part of the ISIS IBEX application.
* Copyright (C) 2012-2025 Science & Technology Facilities Council.
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
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import uk.ac.stfc.isis.ibex.ui.tables.DataboundCellLabelProvider;
import uk.ac.stfc.isis.ibex.ui.tables.DataboundTable;

/**
 * The table for viewing global macros.
 */
public class GlobalMacroTable extends DataboundTable<GlobalMacroViewModel> {
	private static final int COLUMN_WIDTH = 5;
	private enum Columns {
		IOC_NAME("IOC Name", "iocName"), MACRO_NAME("Macro Name", "macroName"), MACRO_VALUE("Macro Value", "macroValue");

		private String columnName;
		private String propertyName;
		Columns(String columnName, String propertyName) {
			this.columnName = columnName;
			this.propertyName = propertyName;
		}
	}

	/**
	 * Constructor for the table.
	 * 
	 * @param parent     The composite to put the table in.
	 * @param style      The SWT style of this databound table.
	 * @param tableStyle The SWT style of the inner table object.
	 */
	public GlobalMacroTable(Composite parent, int style, int tableStyle) {
		super(parent, style, tableStyle | SWT.BORDER);
		initialise();
	}

	/**
	 * Sets the rows of the table.
	 * @param rows The rows to set.
	 */
	@Override
	public void setRows(Collection<GlobalMacroViewModel> rows) {
		super.setRows(rows);
		super.refresh();
	}

	/**
	 * Adds the columns to the table.
	 */
	@Override
	protected void addColumns() {
		iocName();
		macroName();
		macroValue();
	}

	/**
	 * Creates the IOC Name column.
	 */
	private void iocName() {
		createColumn(Columns.IOC_NAME.columnName, COLUMN_WIDTH,
				new DataboundCellLabelProvider<GlobalMacroViewModel>(observeProperty(Columns.IOC_NAME.propertyName)) {
					@Override
					protected String stringFromRow(GlobalMacroViewModel row) {
						return row.getIocName();
					}
				});
	}

	/**
	 * Creates the Macro Name column.
	 */
	private void macroName() {
		createColumn(Columns.MACRO_NAME.columnName, COLUMN_WIDTH,
				new DataboundCellLabelProvider<GlobalMacroViewModel>(observeProperty(Columns.MACRO_NAME.propertyName)) {
					@Override
					protected String stringFromRow(GlobalMacroViewModel row) {
						return row.getMacroName();
					}
				});
	}

	/**
	 * Creates the Macro value column.
	 */
	private void macroValue() {
		createColumn(Columns.MACRO_VALUE.columnName, COLUMN_WIDTH, new DataboundCellLabelProvider<GlobalMacroViewModel>(
				observeProperty(Columns.MACRO_VALUE.propertyName)) {
			@Override
			protected String stringFromRow(GlobalMacroViewModel row) {
				return row.getMacroValue();
			}
		});
	}
}
