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
package uk.ac.stfc.isis.ibex.ui.log.widgets;

import java.util.Set;

import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Widget;

import uk.ac.stfc.isis.ibex.log.message.LogMessage;
import uk.ac.stfc.isis.ibex.log.message.LogMessageFields;
import uk.ac.stfc.isis.ibex.ui.log.comparator.LogMessageComparator;
import uk.ac.stfc.isis.ibex.ui.log.filter.LogMessageFilter;

/**
 *
 */
public class LogDisplayTable extends Composite {

    private static final LogMessageFields[] COLUMNS = { LogMessageFields.CLIENT_NAME, LogMessageFields.CLIENT_HOST,
            LogMessageFields.CONTENTS, LogMessageFields.EVENT_TIME, LogMessageFields.CREATE_TIME,
            LogMessageFields.SEVERITY, LogMessageFields.TYPE, LogMessageFields.APPLICATION_ID };

    private LogDisplayModel model;
    TableViewer tableViewer;

    /**
     * Constructor.
     * 
     * @param parent
     *            the parent composite
     * @param model
     *            the model
     */
    public LogDisplayTable(Composite parent, LogDisplayModel model) {
        super(parent, SWT.NONE);
        setModel(model);
    }

    /**
     * Sets the model used by this table.
     * 
     * @param model
     *            the model
     */
    public void setModel(LogDisplayModel model) {
        this.model = model;
    }

    public void createLayout(LogMessageComparator comparator) {
        // Add log message table
        tableViewer = new TableViewer(this, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.BORDER);
        tableViewer.setComparator(comparator);
        tableViewer.getTable().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

        createTableColumns(tableViewer, comparator);
    }

    public void updateFilter(final Set<LogMessageFilter> filters) {
        Display.getDefault().asyncExec(new Runnable() {
            @Override
            public void run() {
                if (safeToUse(tableViewer.getTable())) {
                    tableViewer.setFilters(filters.toArray(new ViewerFilter[] {}));
                }
            }
        });
    }

    /**
     * Create all the table columns (one for each property of LogMessage).
     * 
     * @param viewer
     *            the table viewer
     */
    void createTableColumns(TableViewer viewer, final LogMessageComparator comparator) {
        if (viewer == null) {
            return;
        }

        for (final LogMessageFields field : COLUMNS) {
            final TableViewerColumn viewerColumn = new TableViewerColumn(viewer, SWT.NONE);
            final TableColumn column = viewerColumn.getColumn();

            String title = field.getDisplayName();
            int width = field.getDefaultColumnWidth();

            column.setText(title);
            column.setWidth(width);
            column.setResizable(width > 0);
            column.setMoveable(true);

            // Sort table by selected column when a column header is clicked
            final Table realTable = viewer.getTable();
            SelectionAdapter selectionAdapter = new SelectionAdapter() {
                @Override
                public void widgetSelected(SelectionEvent e) {
                    comparator.setColumn(field);
                    int dir = comparator.getDirection();
                    realTable.setSortDirection(dir);
                    realTable.setSortColumn(column);
                }
            };
            column.addSelectionListener(selectionAdapter);

            // Set the message property to be displayed in the column
            viewerColumn.setLabelProvider(new ColumnLabelProvider() {
                @Override
                public String getText(Object element) {
                    LogMessage msg = (LogMessage) element;
                    return msg.getProperty(field);
                }
            });
        }
    }

    private boolean safeToUse(Widget widget) {
        if (widget != null && !widget.isDisposed()) {
            return true;
        }
        return false;
    }
}
