
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
import uk.ac.stfc.isis.ibex.ui.widgets.DoubleEditingSupport;
import uk.ac.stfc.isis.ibex.ui.widgets.DoubleEditingSupportBlankIfNull;
import uk.ac.stfc.isis.ibex.ui.widgets.DoubleEditingSupport;
import uk.ac.stfc.isis.ibex.ui.widgets.IntegerEditingSupport;

import java.util.Arrays;
import java.util.Collection;

import org.eclipse.core.databinding.observable.map.IObservableMap;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.ViewerColumn;
import org.eclipse.swt.SWT;

/**
 * A table that contains ScriptGeneratorRows.
 *
 */
public class ScriptGeneratorTable extends DataboundTable<ScriptGeneratorRow> {
	
	private TableViewerColumn position;
	private TableViewerColumn trans;
	private TableViewerColumn transWait;
	private TableViewerColumn sans;
	private TableViewerColumn sansWait;
	private TableViewerColumn period;
	private TableViewerColumn sampleName;
	private TableViewerColumn thickness;
	private Collection<ScriptGeneratorRow> rows;
	
	public ScriptGeneratorTable (Composite parent, int style, int tableStyle, boolean isRowVisibilityShown) {
		super(parent, style, ScriptGeneratorRow.class, tableStyle | SWT.BORDER);
		
		initialise();
	}
	
	@Override
	protected void addColumns() {
		position();
		trans();
		transWait();
		sans();
		sansWait();
		period();
		sampleName();
		thickness();
	}
	
	@Override
	public void setRows(Collection<ScriptGeneratorRow> rows) {
		super.setRows(rows);
		
		this.rows = rows;
	}
	
	public Collection<ScriptGeneratorRow> getRows() {
		return this.rows;
	}

	private void position() {
		position = createColumn("POSITION", 3);
		position.setLabelProvider(new DataboundCellLabelProvider<ScriptGeneratorRow>(
				observeProperty("position")) {
			@Override
			protected String valueFromRow(ScriptGeneratorRow row) {
				return row.getPosition() == null ? "" : String.valueOf(row.getPosition());
			}
		});
		
		
		position.setEditingSupport(new DoubleEditingSupportBlankIfNull<ScriptGeneratorRow>(viewer(), ScriptGeneratorRow.class) {
			@Override
			protected Double valueFromRow(ScriptGeneratorRow row) {
				return row.getPosition();
			}

			@Override
			protected void setValueForRow(ScriptGeneratorRow row, Double position) {
				if (row.wasNull()) {
					rows.add(new ScriptGeneratorRow());
					setRows(rows);
				}
				row.setPosition(position);
			}
		});
	}
	
	private void trans() {
		trans = createColumn("TRANS", 3);
		trans.setLabelProvider(new DataboundCellLabelProvider<ScriptGeneratorRow>(
				observeProperty("trans")) {
			@Override
			protected String valueFromRow(ScriptGeneratorRow row) {
				return String.valueOf(row.getTrans());
			}
		});
		trans.setEditingSupport(new DoubleEditingSupport<ScriptGeneratorRow>(viewer(), ScriptGeneratorRow.class) {
			@Override
			protected Double valueFromRow(ScriptGeneratorRow row) {
				return row.getTrans();
			}

			@Override
			protected void setValueForRow(ScriptGeneratorRow row, Double position) {
				row.setPosition(position);
			}
		});
	}
	
	private void transWait() {
		transWait = createColumn("TRANS_WAIT", 3);
		transWait.setLabelProvider(new DataboundCellLabelProvider<ScriptGeneratorRow>(
				observeProperty("transWait")) {
			@Override
			protected String valueFromRow(ScriptGeneratorRow row) {
				return String.valueOf(row.getTransWait());
			}
		});
		transWait.setEditingSupport(new DoubleEditingSupport<ScriptGeneratorRow>(viewer(), ScriptGeneratorRow.class) {
			@Override
			protected Double valueFromRow(ScriptGeneratorRow row) {
				return row.getPosition();
			}

			@Override
			protected void setValueForRow(ScriptGeneratorRow row, Double position) {
				row.setPosition(position);
			}
		});
	}
	
	private void sans() {
		sans = createColumn("SANS", 3);
		sans.setLabelProvider(new DataboundCellLabelProvider<ScriptGeneratorRow>(
				observeProperty("sans")) {
			@Override
			protected String valueFromRow(ScriptGeneratorRow row) {
				return String.valueOf(row.getSans());
			}
		});
		sans.setEditingSupport(new DoubleEditingSupport<ScriptGeneratorRow>(viewer(), ScriptGeneratorRow.class) {
			@Override
			protected Double valueFromRow(ScriptGeneratorRow row) {
				return row.getPosition();
			}

			@Override
			protected void setValueForRow(ScriptGeneratorRow row, Double position) {
				row.setPosition(position);
			}
		});
	}
	
	private void sansWait() {
		sansWait = createColumn("SANS_WAIT", 3);
		sansWait.setLabelProvider(new DataboundCellLabelProvider<ScriptGeneratorRow>(
				observeProperty("sans")) {
			@Override
			protected String valueFromRow(ScriptGeneratorRow row) {
				return String.valueOf(row.getSansWait());
			}
		});
		sansWait.setEditingSupport(new DoubleEditingSupport<ScriptGeneratorRow>(viewer(), ScriptGeneratorRow.class) {
			@Override
			protected Double valueFromRow(ScriptGeneratorRow row) {
				return row.getPosition();
			}

			@Override
			protected void setValueForRow(ScriptGeneratorRow row, Double position) {
				row.setPosition(position);
			}
		});
	}
	
	private void period() {
		period = createColumn("PERIOD", 3);
		period.setLabelProvider(new DataboundCellLabelProvider<ScriptGeneratorRow>(
				observeProperty("period")) {
			@Override
			protected String valueFromRow(ScriptGeneratorRow row) {
				return row.getPeriod() == null ? "" : String.valueOf(row.getPeriod());
			}
		});
		period.setEditingSupport(new DoubleEditingSupport<ScriptGeneratorRow>(viewer(), ScriptGeneratorRow.class) {
			@Override
			protected Double valueFromRow(ScriptGeneratorRow row) {
				return row.getPosition();
			}

			@Override
			protected void setValueForRow(ScriptGeneratorRow row, Double position) {
				row.setPosition(position);
			}
		});
	}
	
	private void sampleName() {
		sampleName = createColumn("SAMPLE_NAME", 3);
		sampleName.setLabelProvider(new DataboundCellLabelProvider<ScriptGeneratorRow>(
				observeProperty("sampleName")) {
			@Override
			protected String valueFromRow(ScriptGeneratorRow row) {
				return row.getSampleName();
			}
		});
		sampleName.setEditingSupport(new StringEditingSupport<ScriptGeneratorRow>(viewer(), ScriptGeneratorRow.class) {
			@Override
			protected String valueFromRow(ScriptGeneratorRow row) {
				return row.getSampleName();
			}

			@Override
			protected void setValueForRow(ScriptGeneratorRow row, String sampleName) {
				row.setSampleName(sampleName);
			}
		});
	}
	
	private void thickness() {
		thickness = createColumn("THICKNESS", 3);
		thickness.setLabelProvider(new DataboundCellLabelProvider<ScriptGeneratorRow>(
				observeProperty("thickness")) {
			@Override
			protected String valueFromRow(ScriptGeneratorRow row) {
				return String.valueOf(row.getThickness());
			}
		});
		thickness.setEditingSupport(new DoubleEditingSupport<ScriptGeneratorRow>(viewer(), ScriptGeneratorRow.class) {
			@Override
			protected Double valueFromRow(ScriptGeneratorRow row) {
				return row.getPosition();
			}

			@Override
			protected void setValueForRow(ScriptGeneratorRow row, Double position) {
				row.setPosition(position);
			}
		});
	}
}
