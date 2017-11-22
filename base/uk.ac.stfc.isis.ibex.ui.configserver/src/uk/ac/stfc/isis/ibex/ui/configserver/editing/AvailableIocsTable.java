/*
 * This file is part of the ISIS IBEX application. Copyright (C) 2012-2016
 * Science & Technology Facilities Council. All rights reserved.
 *
 * This program is distributed in the hope that it will be useful. This program
 * and the accompanying materials are made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution. EXCEPT AS
 * EXPRESSLY SET FORTH IN THE ECLIPSE PUBLIC LICENSE V1.0, THE PROGRAM AND
 * ACCOMPANYING MATERIALS ARE PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES
 * OR CONDITIONS OF ANY KIND. See the Eclipse Public License v1.0 for more
 * details.
 *
 * You should have received a copy of the Eclipse Public License v1.0 along with
 * this program; if not, you can obtain a copy from
 * https://www.eclipse.org/org/documents/epl-v10.php or
 * http://opensource.org/licenses/eclipse-1.0.php
 */

/**
 * 
 */
package uk.ac.stfc.isis.ibex.ui.configserver.editing;

import java.util.Collection;

import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import uk.ac.stfc.isis.ibex.configserver.configuration.Ioc;
import uk.ac.stfc.isis.ibex.configserver.editing.EditableIoc;
import uk.ac.stfc.isis.ibex.ui.tables.DataboundCellLabelProvider;
import uk.ac.stfc.isis.ibex.ui.tables.DataboundTable;

/**
 * A table displaying all IOCs available to the current instrument.
 */
public class AvailableIocsTable extends DataboundTable<EditableIoc> {

    /**
     * Table constructor.
     * 
     * @param parent
     *            The parent composite.
     * @param style
     *            The SWT style.
     * @param tableStyle
     *            The SWT table style.
     */
    public AvailableIocsTable(Composite parent, int style, int tableStyle) {
        super(parent, style, EditableIoc.class, tableStyle | SWT.BORDER);
        initialise();
    }

    @Override
    public void setRows(Collection<EditableIoc> rows) {
        super.setRows(rows);
    }

    @Override
    protected void addColumns() {
        name();
        description();
    }

    private void name() {
        TableViewerColumn desc = createColumn("Name", 1);
        desc.setLabelProvider(new DataboundCellLabelProvider<Ioc>(observeProperty("name")) {
            @Override
            protected String valueFromRow(Ioc row) {
                return row.getName();
            }
        });
    }

    private void description() {
        TableViewerColumn desc = createColumn("Description", 2);
        desc.setLabelProvider(new DataboundCellLabelProvider<EditableIoc>(observeProperty("description")) {
            @Override
            protected String valueFromRow(EditableIoc row) {
                return row.getDescription();
            }
        });
    }

}
