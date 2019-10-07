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

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.databinding.beans.typed.BeanProperties;
import org.eclipse.core.databinding.observable.IChangeListener;
import org.eclipse.core.databinding.observable.IDisposeListener;
import org.eclipse.core.databinding.observable.IStaleListener;
import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.core.databinding.observable.map.IMapChangeListener;
import org.eclipse.core.databinding.observable.map.IObservableMap;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import uk.ac.stfc.isis.ibex.scriptgenerator.ActionParameter;
import uk.ac.stfc.isis.ibex.scriptgenerator.ScriptGeneratorSingleton;
import uk.ac.stfc.isis.ibex.scriptgenerator.table.ScriptGeneratorAction;
import uk.ac.stfc.isis.ibex.scriptgenerator.table.ActionsTable;
import uk.ac.stfc.isis.ibex.ui.tables.DataboundCellLabelProvider;
import uk.ac.stfc.isis.ibex.ui.tables.DataboundTable;
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
    }

    @Override
    protected void addColumns() {    	
        for (ActionParameter actionParameter:this.actionsTable.getActionParameters()) {
        	addColumn(actionParameter);
        }
    }

	private void addColumn(ActionParameter actionParameter) {
		String columnName = actionParameter.getName();
		TableViewerColumn column = createColumn(
				columnName, 
				2,
				new DataboundCellLabelProvider<ScriptGeneratorAction>(observeProperty(columnName)) {
					@Override
					protected String stringFromRow(ScriptGeneratorAction row) {
						// TODO Auto-generated method stub
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

//    private void name() {
//        createColumn("Name", 1, new DataboundCellLabelProvider<ScriptGeneratorSingleton>(observeProperty("iteratedNumber")) {
//            @Override
//			protected String stringFromRow(ScriptGeneratorSingleton row) {
//                return row.getIteratedNumber();
//            }
//        });
//    }

//    private void value() {
//        TableViewerColumn value = createColumn("Value", 4, new DataboundCellLabelProvider<PropertyDescription>(observeProperty("value")) {
//            @Override
//			protected String stringFromRow(PropertyDescription row) {
//                return row.getValue();
//            }
//        });
//        
//
//    }

}
