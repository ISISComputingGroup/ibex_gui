
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
package uk.ac.stfc.isis.ibex.ui.mainmenu.instrument;

import java.util.Collection;

import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

import uk.ac.stfc.isis.ibex.instrument.InstrumentInfo;

/**
 * 
 */
public class InstrumentTable extends Composite {
    private Collection<InstrumentInfo> instruments;
    private TableViewer viewer;
    private TableColumnLayout tableColumnLayout;

    public InstrumentTable(Composite parent, int style, int tableStyle, Collection<InstrumentInfo> instruments) {
        super(parent, SWT.NONE);

        this.instruments = instruments;

        GridLayout compositeLayout = new GridLayout(1, false);
        compositeLayout.marginHeight = 0;
        compositeLayout.marginWidth = 0;
        setLayout(compositeLayout);
        setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

        createTable(this, tableStyle);
    }

    public void addSelectionListener(SelectionListener listener) {
        viewer.getTable().addSelectionListener(listener);
    }

    public void removeSelectionListener(SelectionListener listener) {
        viewer.getTable().removeSelectionListener(listener);
    }

    public InstrumentInfo getSelectedInstrument() {
        Table table = viewer.getTable();
        String selectedInstrumentName = table.getItem(table.getSelectionIndex()).getText(0);
        return getInstrumentFromName(selectedInstrumentName);
    }

    private void createTable(Composite parent, int tableStyle) {
        Composite tableComposite = new Composite(parent, SWT.NONE);
        tableComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

        viewer = new TableViewer(tableComposite, tableStyle);
        viewer.setContentProvider(ArrayContentProvider.getInstance());
        viewer.setInput(instruments);

        Table table = viewer.getTable();
        table.setHeaderVisible(true);
        table.setLinesVisible(true);

        tableColumnLayout = new TableColumnLayout();
        tableComposite.setLayout(tableColumnLayout);

        createColumn();
        viewer.refresh();
    }

    private void createColumn() {
        TableViewerColumn nameColViewer = new TableViewerColumn(viewer, SWT.LEFT);
        TableColumn nameColumn = nameColViewer.getColumn();
        nameColumn.setText("Name");
        nameColumn.setResizable(false);
        nameColViewer.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
                InstrumentInfo instrument = (InstrumentInfo) element;
                return instrument.name();
            }
        });

        tableColumnLayout.setColumnData(nameColumn, new ColumnWeightData(100, false));
    }

    private InstrumentInfo getInstrumentFromName(String name) {
        for (InstrumentInfo instrument : instruments) {
            if (name.equals(instrument.name())) {
                return instrument;
            }
        }

        return new InstrumentInfo("");
    }
}
