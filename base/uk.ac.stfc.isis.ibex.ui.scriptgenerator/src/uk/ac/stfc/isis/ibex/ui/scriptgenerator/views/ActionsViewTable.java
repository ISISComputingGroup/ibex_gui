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
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.TableColumn;

import uk.ac.stfc.isis.ibex.scriptgenerator.ActionParameter;
import uk.ac.stfc.isis.ibex.scriptgenerator.table.ScriptGeneratorAction;
import uk.ac.stfc.isis.ibex.scriptgenerator.table.ActionsTable;
import uk.ac.stfc.isis.ibex.ui.tables.ColumnComparator;
import uk.ac.stfc.isis.ibex.ui.tables.DataboundCellLabelProvider;
import uk.ac.stfc.isis.ibex.ui.tables.DataboundTable;
import uk.ac.stfc.isis.ibex.ui.tables.NullComparator;
import uk.ac.stfc.isis.ibex.ui.widgets.StringEditingSupport;

/**
 * A table that holds the properties for a target.
 */
@SuppressWarnings("checkstyle:magicnumber")
public class ActionsViewTable extends DataboundTable<ScriptGeneratorAction> {

    private ActionsTable actionsTable;
	/**
     * Default constructor for the table. Creates all the correct columns.
     * 
     * @param parent
     *            The parent composite that this table belongs to.
     * @param style
     *            The SWT style of the composite that this creates.
     * @param tableStyle
     *            The SWT style of the table.
     * @param scriptGeneratorTable 
     */
    public ActionsViewTable(Composite parent, int style, int tableStyle, ActionsTable actionsTable) {
        super(parent, style, tableStyle | SWT.BORDER);
        this.actionsTable = actionsTable;
        initialise();
        
        actionsTable.addPropertyChangeListener("actionParameters", e -> Display.getCurrent().asyncExec(
        		() -> updateTableColumns()));
    }

    
    /**
     * Adds a parameter to this actions table.
     */
    @Override
    protected void addColumns() {    	
        for (ActionParameter actionParameter: actionsTable.getActionParameters()) {
			String columnName = actionParameter.getName();
			TableViewerColumn column = createColumn(
					columnName, 
					2,
					new DataboundCellLabelProvider<ScriptGeneratorAction>(observeProperty(columnName)) {
						@Override
						protected String stringFromRow(ScriptGeneratorAction row) {
							return row.getActionParameterValue(columnName);
						}
						
					});
			
	        column.setEditingSupport(new StringEditingSupport<ScriptGeneratorAction>(viewer(), ScriptGeneratorAction.class) {
	
	            @Override
	            protected String valueFromRow(ScriptGeneratorAction row) {
	                return row.getActionParameterValue(columnName);
	            }
	
	            @Override
	            protected void setValueForRow(ScriptGeneratorAction row, String value) {
	                row.setActionParameterValue(columnName, value);
	            }
	        });	
        }
	}
	
    /**
     * Using a null comparator here stops the columns getting reordered in the UI.
     */
	@Override
	protected ColumnComparator<ScriptGeneratorAction> comparator() {
		return new NullComparator<>();
	}
	
	@Override
	public void updateTableColumns() {
		super.updateTableColumns();
		setRows(new ArrayList<ScriptGeneratorAction>());
	}
}
