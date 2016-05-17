
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

package uk.ac.stfc.isis.ibex.ui.dae.experimentsetup.timechannels;

import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

import uk.ac.stfc.isis.ibex.dae.experimentsetup.timechannels.TimeRegime;
import uk.ac.stfc.isis.ibex.dae.experimentsetup.timechannels.TimeRegimeMode;
import uk.ac.stfc.isis.ibex.dae.experimentsetup.timechannels.TimeRegimeRow;

@SuppressWarnings({ "checkstyle:magicnumber", "checkstyle:localvariablename" })
public class TimeRegimeView extends Composite {
	
	private Table table;	
	private TableViewer viewer;
	private Composite tableComposite;
	private TableColumnLayout tableLayout;
	private Label lblTimeRegime;
	
	public TimeRegimeView(Composite parent, int style) {
		super(parent, style);
		setLayout(new GridLayout(1, false));
		
		lblTimeRegime = new Label(this, SWT.NONE);
		lblTimeRegime.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, false, 1, 1));
		lblTimeRegime.setText("Time Regime");
				
		createTableViewer();
		setTable();
	}
	
	public void setTitle(String title) {
		lblTimeRegime.setText(title);
	}
	
	public void setModel(TimeRegime model) {	
		viewer.setInput(model.rows());
	}
	
	private void createTableViewer() {
		createTableLayout();

		viewer = new TableViewer(tableComposite, SWT.MULTI | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.BORDER);
		
		TableViewerColumn from = addColumn("From", 25);
		from.setEditingSupport(new FromEditingSupport(viewer, TimeRegimeRow.class));
		
		TableViewerColumn to = addColumn("To", 25);
		to.setEditingSupport(new ToEditingSupport(viewer, TimeRegimeRow.class));
		
		TableViewerColumn step = addColumn("Step", 25);
		step.setEditingSupport(new StepEditingSupport(viewer, TimeRegimeRow.class));
		
		TableViewerColumn mode = addColumn("Mode", 25);		
		mode.setEditingSupport(new TimeRegimeModeEditingSupport(viewer, TimeRegimeRow.class, TimeRegimeMode.class));
		
		viewer.setLabelProvider(new TimeRegimeLabelProvider());		
		viewer.setContentProvider(ArrayContentProvider.getInstance());		
	}
		
	private void createTableLayout() {
		// TableColumn layout needs it's own composite to work
		tableComposite = new Composite(this, SWT.NONE);
		GridData gd_composite = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		gd_composite.widthHint = 200;
		tableComposite.setLayoutData(gd_composite);
		
		// NB: WindowBuilder doesn't know about TableColumnLayout...
		tableLayout = new TableColumnLayout();
		tableComposite.setLayout(tableLayout);
	}
	
	private TableViewerColumn addColumn(String name, int weight) {
		TableViewerColumn viewerColumn = new TableViewerColumn(viewer, SWT.NONE);
		TableColumn column = viewerColumn.getColumn();
		column.setText(name);
		column.setResizable(false);
		tableLayout.setColumnData(column, new ColumnWeightData(weight, 50, false));
		
		return viewerColumn;
	}
	
	private void setTable() {
		table = viewer.getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible(true);		
	}
}
