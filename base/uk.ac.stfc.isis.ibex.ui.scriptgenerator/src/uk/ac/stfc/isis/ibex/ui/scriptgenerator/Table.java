
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

import java.util.Collection;

import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import uk.ac.stfc.isis.ibex.scriptgenerator.row.Row;
import uk.ac.stfc.isis.ibex.ui.tables.DataboundCellLabelProvider;
import uk.ac.stfc.isis.ibex.ui.tables.DataboundTable;
import uk.ac.stfc.isis.ibex.ui.widgets.DoubleEditingSupportBlankIfNull;
import uk.ac.stfc.isis.ibex.ui.widgets.IntegerEditingSupportBlankIfNull;
import uk.ac.stfc.isis.ibex.ui.widgets.StringEditingSupport;

/**
 * A table that contains ScriptGeneratorRows.
 *
 */
@SuppressWarnings("checkstyle:magicnumber")
public class Table extends DataboundTable<Row> {
	
	private TableViewerColumn position;
	private TableViewerColumn trans;
	private TableViewerColumn transWait;
	private TableViewerColumn sans;
	private TableViewerColumn sansWait;
	private TableViewerColumn period;
	private TableViewerColumn sampleName;
	private TableViewerColumn thickness;
	private Collection<Row> rows;
	
	/**
	 * The default constructor.
	 * @param parent the parent composite
	 * @param style the style of the DataboundTable composite
	 * @param tableStyle the style of the table
	 */
	public Table(Composite parent, int style, int tableStyle) {
		super(parent, style, Row.class, tableStyle | SWT.BORDER);
		
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
	public void setRows(Collection<Row> rows) {
		super.setRows(rows);
		
		this.rows = rows;
	}

	private void position() {
		position = createColumn("POSITION", 3);
		position.setLabelProvider(new DataboundCellLabelProvider<Row>(
				observeProperty("position")) {
			@Override
			protected String valueFromRow(Row row) {
				return row.getPosition() == null ? "" : String.valueOf(row.getPosition());
			}
		});
		
		
		position.setEditingSupport(new DoubleEditingSupportBlankIfNull<Row>(viewer(), Row.class) {
			@Override
			protected Double valueFromRow(Row row) {
				return row.getPosition();
			}

			@Override
			protected void setValueForRow(Row row, Double position) {
				addRowIfNull(row);
				
				row.setPosition(position);
			}
		});
	}
	
	private void trans() {
		trans = createColumn("TRANS", 3);
		trans.setLabelProvider(new DataboundCellLabelProvider<Row>(
				observeProperty("trans")) {
			@Override
			protected String valueFromRow(Row row) {
				return row.getTrans() == null ? "" : String.valueOf(row.getTrans());
			}
		});
		trans.setEditingSupport(new DoubleEditingSupportBlankIfNull<Row>(viewer(), Row.class) {
			@Override
			protected Double valueFromRow(Row row) {
				return row.getTrans();
			}

			@Override
			protected void setValueForRow(Row row, Double trans) {
				addRowIfNull(row);
				
				row.setTrans(trans);
			}
		});
	}
	
	private void transWait() {
		transWait = createColumn("TRANS_WAIT", 3);
		transWait.setLabelProvider(new DataboundCellLabelProvider<Row>(
				observeProperty("transWait")) {
			@Override
			protected String valueFromRow(Row row) {
				return row.getTransWait() == null ? "" : String.valueOf(row.getTransWait());
			}
		});
		transWait.setEditingSupport(new DoubleEditingSupportBlankIfNull<Row>(viewer(), Row.class) {
			@Override
			protected Double valueFromRow(Row row) {
				return row.getTransWait();
			}

			@Override
			protected void setValueForRow(Row row, Double transWait) {
				addRowIfNull(row);
				
				row.setTransWait(transWait);
			}
		});
	}
	
	private void sans() {
		sans = createColumn("SANS", 3);
		sans.setLabelProvider(new DataboundCellLabelProvider<Row>(
				observeProperty("sans")) {
			@Override
			protected String valueFromRow(Row row) {
				return row.getSans() == null ? "" : String.valueOf(row.getSans());
			}
		});
		sans.setEditingSupport(new DoubleEditingSupportBlankIfNull<Row>(viewer(), Row.class) {
			@Override
			protected Double valueFromRow(Row row) {
				return row.getSans();
			}

			@Override
			protected void setValueForRow(Row row, Double sans) {
				addRowIfNull(row);
				
				row.setSans(sans);
			}
		});
	}
	
	private void sansWait() {
		sansWait = createColumn("SANS_WAIT", 3);
		sansWait.setLabelProvider(new DataboundCellLabelProvider<Row>(
				observeProperty("sans")) {
			@Override
			protected String valueFromRow(Row row) {
				return row.getSansWait() == null ? "" : String.valueOf(row.getSansWait());
			}
		});
		sansWait.setEditingSupport(new DoubleEditingSupportBlankIfNull<Row>(viewer(), Row.class) {
			@Override
			protected Double valueFromRow(Row row) {
				return row.getSansWait();
			}

			@Override
			protected void setValueForRow(Row row, Double sansWait) {
				addRowIfNull(row);
				
				row.setSansWait(sansWait);
			}
		});
	}
	
	private void period() {
		period = createColumn("PERIOD", 3);
		period.setLabelProvider(new DataboundCellLabelProvider<Row>(
				observeProperty("period")) {
			@Override
			protected String valueFromRow(Row row) {
				return row.getPeriod() == null ? "" : String.valueOf(row.getPeriod());
			}
		});
		period.setEditingSupport(new IntegerEditingSupportBlankIfNull<Row>(viewer(), Row.class) {
			@Override
			protected Integer valueFromRow(Row row) {
				return row.getPeriod();
			}

			@Override
			protected void setValueForRow(Row row, Integer period) {
				addRowIfNull(row);
				
				row.setPeriod(period);
			}
		});
	}
	
	private void sampleName() {
		sampleName = createColumn("SAMPLE_NAME", 3);
		sampleName.setLabelProvider(new DataboundCellLabelProvider<Row>(
				observeProperty("sampleName")) {
			@Override
			protected String valueFromRow(Row row) {
				return row.getSampleName();
			}
		});
		sampleName.setEditingSupport(new StringEditingSupport<Row>(viewer(), Row.class) {
			@Override
			protected String valueFromRow(Row row) {
				return row.getSampleName();
			}

			@Override
			protected void setValueForRow(Row row, String sampleName) {
				addRowIfNull(row);
				
				row.setSampleName(sampleName);
			}
		});
	}
	
	private void thickness() {
		thickness = createColumn("THICKNESS", 3);
		thickness.setLabelProvider(new DataboundCellLabelProvider<Row>(
				observeProperty("thickness")) {
			@Override
			protected String valueFromRow(Row row) {
				return row.getThickness() == null ? "" : String.valueOf(row.getThickness());
			}
		});
		thickness.setEditingSupport(new DoubleEditingSupportBlankIfNull<Row>(viewer(), Row.class) {
			@Override
			protected Double valueFromRow(Row row) {
				return row.getThickness();
			}

			@Override
			protected void setValueForRow(Row row, Double thickness) {
				addRowIfNull(row);
				
				row.setThickness(thickness);
			}
		});
	}
	
	private void addRowIfNull(Row row) {
		if (row.wasNull()) {
			rows.add(new Row());
			setRows(rows);
		}
	}
}
