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
package uk.ac.stfc.isis.ibex.ui.dae.detectordiagnostics;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.widgets.Composite;

import uk.ac.stfc.isis.ibex.ui.tables.DataboundCellLabelProvider;
import uk.ac.stfc.isis.ibex.ui.tables.DataboundTable;

/**
 *
 */
public class Table extends DataboundTable<TableRow> {
    
    /**
     * Instantiates a new device screens table.
     *
     * @param parent the parent
     * @param style the style
     * @param tableStyle the table style
     */
    public Table(Composite parent, int style, int tableStyle) {
        super(parent, style, TableRow.class, tableStyle);
        
        initialise();
        
        Collection<TableRow> rows = new ArrayList<>();
        rows.add(new TableRow());
        rows.add(new TableRow());
        
        this.setRows(rows);
    }
    
    @Override
    protected void addColumns() {
        number();
        value();
    }

    private void number() {
        TableViewerColumn number = createColumn("Spectrum number", 20);
        number.setLabelProvider(new DataboundCellLabelProvider<TableRow>(observeProperty("number")) {
            @Override
            protected String valueFromRow(TableRow row) {
                return row.getNumber();
            }
        });
        // setSortListener(name.getColumn(), DeviceScreensComparator.SortedOnType.NAME);
    }
    
    private void value() {
        TableViewerColumn value = createColumn("Value", 20);
        value.setLabelProvider(new DataboundCellLabelProvider<TableRow>(observeProperty("value")) {
            @Override
            protected String valueFromRow(TableRow row) {
                return row.getValue();
            }
        });
        // setSortListener(name.getColumn(), DeviceScreensComparator.SortedOnType.NAME);
    }
}
