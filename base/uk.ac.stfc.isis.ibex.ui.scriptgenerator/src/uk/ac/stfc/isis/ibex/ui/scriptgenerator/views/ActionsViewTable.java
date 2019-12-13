 /*
 * This file is part of the ISIS IBEX application.
 * Copyright (C) 2012-2019 Science & Technology Facilities Council.
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

/**
 * 
 */
package uk.ac.stfc.isis.ibex.ui.scriptgenerator.views;

import java.util.ArrayList;

import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import uk.ac.stfc.isis.ibex.scriptgenerator.table.ScriptGeneratorAction;
import uk.ac.stfc.isis.ibex.ui.tables.ColumnComparator;
import uk.ac.stfc.isis.ibex.ui.tables.DataboundTable;
import uk.ac.stfc.isis.ibex.ui.tables.NullComparator;
import uk.ac.stfc.isis.ibex.ui.tables.SortableObservableMapCellLabelProvider;

/**
 * A table that holds the properties for a target.
 */
@SuppressWarnings("checkstyle:magicnumber")
public class ActionsViewTable extends DataboundTable<ScriptGeneratorAction> {
	
	private ScriptGeneratorViewModel scriptGeneratorViewModel;
    
	/**
     * Default constructor for the table. Creates all the correct columns.
     * 
     * @param parent
     *            The parent composite that this table belongs to.
     * @param style
     *            The SWT style of the composite that this creates.
     * @param tableStyle
     *            The SWT style of the table.
     * @param actionsTable 
     * 			  The table of actions (rows) to display/write data to.
     */
    public ActionsViewTable(Composite parent, int style, int tableStyle, ScriptGeneratorViewModel scriptGeneratorViewModel) {
        super(parent, style, tableStyle | SWT.BORDER);
        this.scriptGeneratorViewModel = scriptGeneratorViewModel;
        initialise();
        
        scriptGeneratorViewModel.addActionParamPropertyListener(this);
    }
	
    /**
     * Using a null comparator here stops the columns getting reordered in the UI.
     */
	@Override
	protected ColumnComparator<ScriptGeneratorAction> comparator() {
		return new NullComparator<>();
	}
	
	/**
	 * Updates the table columns after a config change.
	 */
	@Override
	public void updateTableColumns() {
		super.updateTableColumns();
		setRows(new ArrayList<ScriptGeneratorAction>());
	}

	/**
	 * Add action parameter and validity check columns.
	 */
	@Override
	protected void addColumns() {
		scriptGeneratorViewModel.addColumns(this);
	}
	
	/**
	 * Create a TableViewerColumn.
	 * 
	 * @param columnName The title of the column.
	 * @param widthWeighting The sizing weight for the width.
	 * @param labelProvider The object to provide labels for the TableViewerColumn.
	 * @return the table viewer column.
	 */
	@Override
	public TableViewerColumn createColumn(String columnName, int widthWeighting,
			SortableObservableMapCellLabelProvider<ScriptGeneratorAction> labelProvider) {
		return super.createColumn(columnName, widthWeighting, labelProvider);
	}
}
