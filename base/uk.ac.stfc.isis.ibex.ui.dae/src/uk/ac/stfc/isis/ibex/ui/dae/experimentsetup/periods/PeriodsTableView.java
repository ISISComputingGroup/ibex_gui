
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

package uk.ac.stfc.isis.ibex.ui.dae.experimentsetup.periods;

import java.util.List;

import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.layout.FillLayout;

import uk.ac.stfc.isis.ibex.dae.experimentsetup.periods.Period;
import uk.ac.stfc.isis.ibex.dae.experimentsetup.periods.PeriodType;
import uk.ac.stfc.isis.ibex.ui.dae.experimentsetup.DaeExperimentSetupTableViewer;
import uk.ac.stfc.isis.ibex.ui.dae.experimentsetup.PanelViewModel;

/**
 * The class responsible for creating a the periods table.
 */
@SuppressWarnings("checkstyle:magicnumber")
public class PeriodsTableView extends Composite {
	
	private Table table;	
	private DaeExperimentSetupTableViewer viewer;
	private Composite tableComposite;
	private TableColumnLayout tableLayout;
	
	@SuppressWarnings("unused")
	private TableViewerColumn period;
	
	private TableViewerColumn type;
	private TableViewerColumn frames;
	private TableViewerColumn binaryOutput;
	private TableViewerColumn label;
	private PanelViewModel panelViewModel;
	
	/**
     * Standard constructor.
     * 
     * @param parent The parent composite.
     * @param style The SWT style.
     * @param panelViewModel The viewModel that helps manipulate the panels.
     */
	public PeriodsTableView(Composite parent, int style, PanelViewModel panelViewModel) {
		super(parent, style);
		this.panelViewModel = panelViewModel;
		setLayout(new FillLayout(SWT.HORIZONTAL));
		// TableColumn layout needs its own composite to work
		tableComposite = new Composite(this, SWT.NONE);
		
		// NB: WindowBuilder doesn't know about TableColumnLayout...
		tableLayout = new TableColumnLayout();
		tableComposite.setLayout(tableLayout);
		
		createTableViewer();
		setTable();
	}
	
	/**
	 * Sets the periods used in the table.
	 * @param periods
	 *               the periods used in the table.
	 */
	public void setPeriods(List<Period> periods) {	
		viewer.setInput(periods);
	}
	
	private void createTableViewer() {
		viewer = new DaeExperimentSetupTableViewer(tableComposite, SWT.MULTI | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.BORDER, 
		        panelViewModel, "periodsTable");
		
		period = addColumn("Period", 10);	
		
		type = addColumn("Type", 15);
		type.setEditingSupport(new TypeEditingSupport(viewer, Period.class, PeriodType.class));
		
		frames = addColumn("Frames", 10);
		frames.setEditingSupport(new FramesEditingSupport(viewer, Period.class));
		
		binaryOutput = addColumn("Binary output", 10);		
		binaryOutput.setEditingSupport(new BinaryOutputEditingSupport(viewer, Period.class));
		
		label = addColumn("Label", 55);		
		label.setEditingSupport(new LabelEditingSupport(viewer, Period.class));
		
		viewer.setLabelProvider(new PeriodLabelProvider());		
		viewer.setContentProvider(ArrayContentProvider.getInstance());
	}
	
	private TableViewerColumn addColumn(String name, int weight) {
		TableViewerColumn viewerColumn = new TableViewerColumn(viewer, SWT.NONE);
		TableColumn column = viewerColumn.getColumn();
		column.setWidth(100);
		column.setText(name);
		column.setResizable(true);
		
		tableLayout.setColumnData(column, new ColumnWeightData(weight, 50, false));

		return viewerColumn;
	}
	
	private void setTable() {
		table = viewer.getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible(true);		
	}
	
	/**
     * Creates a cache of the applied values for the table.
     */
    public void resetCachedValues() {
        panelViewModel.resetTableViewerCachedValues(viewer);
    }
    
    /**
     * Removes the listeners out dated when changes were applied.
     */
    public void removeListener() {
        panelViewModel.removesTableViewersListeners(viewer);
    }
    
    /**
     * Creates a cache of the applied values for the different widgets.
     */
    public void createInitialCachedValues() {
        panelViewModel.createInitialTableViewerCachedValues(viewer);
    }
    
    /**
     * Goes over every call and adds a label to a it if its value is different from the one applied on the instrument.
     */
    public void ifTableValueDifferentFromCachedValueThenChangeLabel() {
        panelViewModel.ifTableViewerValuesDifferentFromCachedValueThenChangeLabel(viewer);
    }
    
}
