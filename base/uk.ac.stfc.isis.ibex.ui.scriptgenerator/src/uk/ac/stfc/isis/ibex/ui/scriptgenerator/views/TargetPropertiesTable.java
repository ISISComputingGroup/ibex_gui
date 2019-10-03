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

import uk.ac.stfc.isis.ibex.scriptgenerator.ColumnDescription;
import uk.ac.stfc.isis.ibex.scriptgenerator.ScriptGeneratorSingleton;
import uk.ac.stfc.isis.ibex.scriptgenerator.table.ScriptGeneratorRow;
import uk.ac.stfc.isis.ibex.scriptgenerator.table.ScriptGeneratorTable;
import uk.ac.stfc.isis.ibex.ui.tables.DataboundCellLabelProvider;
import uk.ac.stfc.isis.ibex.ui.tables.DataboundTable;

/**
 * A table that holds the properties for a target.
 */
@SuppressWarnings("checkstyle:magicnumber")
public class TargetPropertiesTable extends DataboundTable<ScriptGeneratorRow> {

    private ScriptGeneratorTable scriptGeneratorTable;

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
    public TargetPropertiesTable(Composite parent, int style, int tableStyle, ScriptGeneratorTable scriptGeneratorTable) {
        super(parent, style, tableStyle | SWT.BORDER);
        this.scriptGeneratorTable = scriptGeneratorTable;
        initialise();
    }

    @Override
    protected void addColumns() {    	
        for (ColumnDescription column:this.scriptGeneratorTable.getColumns()) {
        	createColumn(column.getName(), 2,
        			new DataboundCellLabelProvider<ScriptGeneratorRow>(observeProperty("fixme")) {

				@Override
				protected String stringFromRow(ScriptGeneratorRow row) {
					// TODO Auto-generated method stub
					return row.getData();
				}
			});
        }
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
//        value.setEditingSupport(new StringEditingSupport<PropertyDescription>(viewer(), PropertyDescription.class) {
//
//            @Override
//            protected String valueFromRow(PropertyDescription row) {
//                return row.getValue();
//            }
//
//            @Override
//            protected void setValueForRow(PropertyDescription row, String value) {
//                row.setValue(value);
//            }
//        });
//    }

}
