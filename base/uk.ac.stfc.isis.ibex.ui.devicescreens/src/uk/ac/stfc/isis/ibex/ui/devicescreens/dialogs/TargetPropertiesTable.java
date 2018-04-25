 /*
 * This file is part of the ISIS IBEX application.
 * Copyright (C) 2012-2016 Science & Technology Facilities Council.
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
package uk.ac.stfc.isis.ibex.ui.devicescreens.dialogs;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import uk.ac.stfc.isis.ibex.devicescreens.desc.PropertyDescription;
import uk.ac.stfc.isis.ibex.ui.tables.DataboundCellLabelProvider;
import uk.ac.stfc.isis.ibex.ui.tables.DataboundTable;

/**
 * A table that holds the properties for a target.
 */
@SuppressWarnings("checkstyle:magicnumber")
public class TargetPropertiesTable extends DataboundTable<PropertyDescription> {

    /**
     * Default constructor for the table. Creates all the correct columns.
     * 
     * @param parent
     *            The parent composite that this table belongs to.
     * @param style
     *            The SWT style of the composite that this creates.
     * @param tableStyle
     *            The SWT style of the table.
     */
    public TargetPropertiesTable(Composite parent, int style, int tableStyle) {
        super(parent, style, PropertyDescription.class, tableStyle | SWT.BORDER);
        initialise();
    }

    @Override
    protected void addColumns() {
        name();
        value();
    }

    private void name() {
        createColumn("Name", 2, new DataboundCellLabelProvider<PropertyDescription>(observeProperty("key")) {
            @Override
			public String stringFromRow(PropertyDescription row) {
                return row.getKey();
            }
        });
    }

    private void value() {
        createColumn("Value", 4, new DataboundCellLabelProvider<PropertyDescription>(observeProperty("value")) {
            @Override
			public String stringFromRow(PropertyDescription row) {
                return row.getValue();
            }
        });
    }

}
