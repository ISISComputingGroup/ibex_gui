
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

package uk.ac.stfc.isis.ibex.e4.ui.perspectiveswitcher.controls;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.core.databinding.observable.map.IObservableMap;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import uk.ac.stfc.isis.ibex.e4.ui.perspectiveswitcher.PerspectiveInfo;
import uk.ac.stfc.isis.ibex.e4.ui.perspectiveswitcher.PerspectivesVisibleModel;
import uk.ac.stfc.isis.ibex.ui.tables.DataboundCellLabelProvider;
import uk.ac.stfc.isis.ibex.ui.tables.DataboundTable;
import uk.ac.stfc.isis.ibex.ui.widgets.CheckboxLabelProvider;

/**
 * Table allowing IOCs to be started and stopped.
 * 
 * Note that all columns in this table are not resizable as the H_SCROLL has
 * been removed and resizing could cause columns to disappear.
 * 
 * The H_SCROLL has been removed as it was appearing despite no extra data being
 * in the table (unsure why)
 */
@SuppressWarnings("checkstyle:magicnumber")
public class PerspectivesTable extends DataboundTable<PerspectiveInfo> {   
    /**
     * A table that shows the status of all IOCs on the instrument.
     * 
     * @param parent
     *            the parent composite for the table.
     * @param style
     *            The style of the viewer.
     * @param tableStyle
     *            The style of the table.
     */
    public PerspectivesTable(Composite parent, int style, int tableStyle) {
        super(parent, style, tableStyle | SWT.BORDER);
        initialise();
    }

    @Override
    public void setRows(Collection<PerspectiveInfo> rows) {
        List<PerspectiveInfo> states = new ArrayList<>(rows);
        //Collections.sort(states);
        super.setRows(states);
        refresh();
    }

    @Override
    protected void addColumns() {
        name();
        visibleRemotely();
        visibleLocally();
    }

    private void name() {
        createColumn("Name", 4, true, new DataboundCellLabelProvider<PerspectiveInfo>(observeProperty("name")) {
            @Override
            protected String stringFromRow(PerspectiveInfo row) {
                return row.getName();
            }
        });
    }

    private void visibleRemotely() {
        IObservableMap[] observables = {observeProperty("visibleRemotely"), observeProperty("remoteEditable")};
        createColumn("Visible to Users", 4, true, new CheckboxLabelProvider<PerspectiveInfo>(observables) {
            @Override
            protected boolean checked(PerspectiveInfo perspective) {
                return perspective.getVisibleRemotely();
            }
            @Override
            protected void setChecked(PerspectiveInfo perspective, boolean checked) {
                perspective.setVisibleRemotely(checked);
            }
            @Override
            protected boolean isEditable(PerspectiveInfo model) {
                return model.getRemoteEditable();
            }
        });
    }

    private void visibleLocally() {
        createColumn("Visible Locally", 4, true, new CheckboxLabelProvider<PerspectiveInfo>(observeProperty("visibleLocally")) {
            @Override
            protected boolean checked(PerspectiveInfo perspective) {
                return perspective.getVisibleLocally();
            }
            @Override
            protected void setChecked(PerspectiveInfo perspective, boolean checked) {
                perspective.setVisibleLocally(checked);
            }
            @Override
            protected boolean isEditable(PerspectiveInfo model) {
                return true;
            }
        });
    }
}
