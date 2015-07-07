
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

package uk.ac.stfc.isis.ibex.ui.experimentdetails;

import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.widgets.Composite;

import uk.ac.stfc.isis.ibex.experimentdetails.Parameter;
import uk.ac.stfc.isis.ibex.ui.tables.DataboundCellLabelProvider;
import uk.ac.stfc.isis.ibex.ui.tables.DataboundTable;
import uk.ac.stfc.isis.ibex.ui.widgets.StringEditingSupport;

public class ParametersTable extends DataboundTable<Parameter> {

	public ParametersTable(Composite parent, int style, int tableStyle) {
		super(parent, style, Parameter.class, tableStyle);
		initialise();
	}

	@Override
	protected void addColumns() {
		name();
		units();
		value();
	}

	private void name() {
		TableViewerColumn name = createColumn("Name", 4);
		name.setLabelProvider(new DataboundCellLabelProvider<Parameter>(observeProperty("name")) {
			@Override
			protected String valueFromRow(Parameter row) {
				return row.getName();
			}
		});		
	}
	
	private void units() {
		TableViewerColumn name = createColumn("Units", 1);
		name.setLabelProvider(new DataboundCellLabelProvider<Parameter>(observeProperty("units")) {
			@Override
			protected String valueFromRow(Parameter row) {
				return row.getUnits();
			}
		});		
	}
	
	private void value() {
		TableViewerColumn name = createColumn("Value", 2);
		name.setLabelProvider(new DataboundCellLabelProvider<Parameter>(observeProperty("value")) {
			@Override
			protected String valueFromRow(Parameter row) {
				return row.getValue();
			}
		});	
		name.setEditingSupport(new StringEditingSupport<Parameter>(viewer(), Parameter.class) {
			@Override
			protected String valueFromRow(Parameter row) {
				return row.getValue();
			}

			@Override
			protected void setValueForRow(Parameter row, String value) {
				row.setValue(value);
			}
		});
	}
}
