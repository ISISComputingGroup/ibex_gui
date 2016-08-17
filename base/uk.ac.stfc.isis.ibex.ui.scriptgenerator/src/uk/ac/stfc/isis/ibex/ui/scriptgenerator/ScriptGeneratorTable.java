
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

package uk.ac.stfc.isis.ibex.ui.scriptgenerator;
import org.eclipse.swt.widgets.Composite;

import uk.ac.stfc.isis.ibex.scriptgenerator.PythonBuilder;
import uk.ac.stfc.isis.ibex.scriptgenerator.ScriptGeneratorRow;
import uk.ac.stfc.isis.ibex.ui.tables.DataboundCellLabelProvider;
import uk.ac.stfc.isis.ibex.ui.tables.DataboundTable;
import uk.ac.stfc.isis.ibex.ui.widgets.StringEditingSupport;
import uk.ac.stfc.isis.ibex.scriptgenerator.ScriptGeneratorRow;

import java.util.Arrays;
import java.util.Collection;

import org.eclipse.core.databinding.observable.map.IObservableMap;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.ViewerColumn;
import org.eclipse.swt.SWT;

public class ScriptGeneratorTable extends DataboundTable<ScriptGeneratorRow> {
	
	protected TableViewerColumn name;
	protected TableViewerColumn temperature;
	protected TableViewerColumn wait;
	
	private final StringEditingSupport<ScriptGeneratorRow> valueEditingSupport = new StringEditingSupport<ScriptGeneratorRow>(viewer(), ScriptGeneratorRow.class) {
		@Override
		protected String valueFromRow(ScriptGeneratorRow row) {
			return row.getName();
		}

		@Override
		protected void setValueForRow(ScriptGeneratorRow row, String name) {
			row.setName(name);
		}
	};
	
	public ScriptGeneratorTable (Composite parent, int style, int tableStyle, boolean isRowVisibilityShown) {
		super(parent, style, ScriptGeneratorRow.class, tableStyle | SWT.BORDER);
		
		initialise();
	}
	
	public void enableEditing(boolean enabled) {
		valueEditingSupport.setEnabled(enabled);
	}
	
	@Override
	protected void addColumns() {
		name();
		temperature();
		waitValue();
	}
	
	@Override
	public void setRows(Collection<ScriptGeneratorRow> rows) {
		super.setRows(rows);
	}
	
	private void name() {
		name = createColumn("Name", 3);
		name.setLabelProvider(new DataboundCellLabelProvider<ScriptGeneratorRow>(
				observeProperty("name")) {
			@Override
			protected String valueFromRow(ScriptGeneratorRow row) {
				return row.getName();
			}
		});
		name.setEditingSupport(valueEditingSupport);
	}
	
	private void temperature() {
		temperature = createColumn("Temperature", 3);
		temperature.setLabelProvider(new DataboundCellLabelProvider<ScriptGeneratorRow>(
				observeProperty("temperature")) {
			@Override
			protected String valueFromRow(ScriptGeneratorRow row) {
				return String.valueOf(row.getTemperature());
			}
		});
	}
	
	private void waitValue() {
		wait = createColumn("Wait", 3);
		wait.setLabelProvider(new DataboundCellLabelProvider<ScriptGeneratorRow>(
				observeProperty("wait")) {
			@Override
			protected String valueFromRow(ScriptGeneratorRow row) {
				return String.valueOf(row.getWait());
			}
		});
	}
}
