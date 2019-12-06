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
import java.util.List;

import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.jface.viewers.ColumnViewerToolTipSupport;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import uk.ac.stfc.isis.ibex.scriptgenerator.ActionParameter;
import uk.ac.stfc.isis.ibex.scriptgenerator.ScriptGeneratorSingleton;
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
    private ScriptGeneratorSingleton scriptGeneratorModel;
    
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
    public ActionsViewTable(Composite parent, int style, int tableStyle, ActionsTable actionsTable, ScriptGeneratorSingleton scriptGeneratorModel) {
        super(parent, style, tableStyle | SWT.BORDER);
        this.actionsTable = actionsTable;
        this.scriptGeneratorModel = scriptGeneratorModel;
        initialise();
        
        actionsTable.addPropertyChangeListener("actionParameters", e -> Display.getCurrent().asyncExec(
        		() -> updateTableColumns()));
    }

    
    /**
     * Adds a parameter to this actions table.
     */
    @Override
    protected void addColumns() {    	
    	// Add action parameter columns
        for (ActionParameter actionParameter: actionsTable.getActionParameters()) {
			String columnName = actionParameter.getName();
			TableViewerColumn column = createColumn(
					columnName, 
					2,
					new DataboundCellLabelProvider<ScriptGeneratorAction>(observeProperty(columnName)) {
						@Override
						protected String stringFromRow(ScriptGeneratorAction row) {
							return row.getActionParameterValue(actionParameter);
						}
						
			        	@Override
			        	public String getToolTipText(Object element) {
			        		return ActionsViewTable.this.getScriptGenActionToolTipText((ScriptGeneratorAction) element);
			        	}
					});
			
	        column.setEditingSupport(new StringEditingSupport<ScriptGeneratorAction>(viewer(), ScriptGeneratorAction.class) {
	
	            @Override
	            protected String valueFromRow(ScriptGeneratorAction row) {
	                return row.getActionParameterValue(actionParameter);
	            }
	
	            @Override
	            protected void setValueForRow(ScriptGeneratorAction row, String value) {
	                row.setActionParameterValue(actionParameter, value);
	                scriptGeneratorModel.onTableChange();
	                ActionsViewTable.this.updateValidityChecks();
	            }
	        });	
        }
        // Add validity notifier column
        TableViewerColumn validityColumn = createColumn("Validity", 
        		1, 
        		new DataboundCellLabelProvider<ScriptGeneratorAction>(observeProperty("validity")) {
        	@Override
			protected String stringFromRow(ScriptGeneratorAction row) {
        		if (row.isValid()) {
        			return "\u2714"; // A tick for valid
        		}
				return "\u2718"; // Unicode cross for invalidity
			}
        	
        	@Override
        	public String getToolTipText(Object element) {
        		return ActionsViewTable.this.getScriptGenActionToolTipText((ScriptGeneratorAction) element);
        	}
        	
        });
        validityColumn.getColumn().setAlignment(SWT.CENTER);
        
        ColumnViewerToolTipSupport.enableFor(this.viewer());
	}
    
    private String getScriptGenActionToolTipText(ScriptGeneratorAction action) {
    	if (action.isValid()) {
			return null; 
		}
		return "The reason this row is invalid is:\n" + action.getInvalidityReason() +
				"\n\nClick \"Get Validity Errors\" button for more info"; // Show reason on next line as a tooltip
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
	 * Update the tables action rows to be coloured if invalid.
	 */
	public void updateValidityChecks() {
		List<ScriptGeneratorAction> actions = actionsTable.getActions();
		TableItem[] items = table().getItems();
		int validityColumnIndex = table().getColumnCount()-1;
		var display = Display.getCurrent();
		Color invalidDarkColor = display.getSystemColor(SWT.COLOR_RED);
		Color invalidLightColor = new Color(display, 255, 204, 203);
		Color validColor = display.getSystemColor(SWT.COLOR_GREEN);
		Color clearColor = display.getSystemColor(SWT.COLOR_WHITE);
		for (int i = 0; i < actions.size(); i++) {
			if (actions.get(i).isValid()) {
				items[i].setBackground(clearColor);
				items[i].setBackground(validityColumnIndex, validColor);
			} else {
				items[i].setBackground(invalidLightColor);
				items[i].setBackground(validityColumnIndex, invalidDarkColor);
			}
		}
		invalidLightColor.dispose();
	}
}
