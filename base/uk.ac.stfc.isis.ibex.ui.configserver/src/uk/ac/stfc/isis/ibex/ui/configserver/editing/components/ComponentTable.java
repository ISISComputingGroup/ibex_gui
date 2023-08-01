/*
 * This file is part of the ISIS IBEX application.
 * Copyright (C) 2012-2023 Science & Technology Facilities Council.
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

package uk.ac.stfc.isis.ibex.ui.configserver.editing.components;

import java.util.Collection;

import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

import uk.ac.stfc.isis.ibex.configserver.configuration.Configuration;

/**
 * A table of components. Can be filtered by component name.
 */
public class ComponentTable extends Composite {
    private TableViewer viewer;
    private TableColumnLayout tableColumnLayout;
    private ComponentNameSearch search;

    /**
     * Creates the table.
     * @param parent the parent composite
     * @param style the SWT style flags
     * @param tableStyle the SWT style flags for the underlying table
     */
    public ComponentTable(Composite parent, int style, int tableStyle) {
        super(parent, SWT.NONE);

        GridLayout compositeLayout = new GridLayout(1, false);
        compositeLayout.marginHeight = 0;
        compositeLayout.marginWidth = 0;
        setLayout(compositeLayout);
        setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

        createTable(this, tableStyle);

        search = new ComponentNameSearch();
        viewer.addFilter(search);
    }

    /**
     * Adds a listener for selection changes to the instrument table viewer.
     * 
     * @param listener a selection changed listener
     */
    public void addSelectionChangedListener(ISelectionChangedListener listener) {
        viewer.addSelectionChangedListener(listener);
    }

    /**
     * Removes the given selection change listener from the instrument table viewer.
     *
     * @param listener a selection changed listener
     */
    public void removeSelectionChangedListener(ISelectionChangedListener listener) {
        viewer.removeSelectionChangedListener(listener);
    }

    /**
     * Sets the text to search for and refreshes the table.
     * 
     * @param searchText the text to search for
     */
    public void setSearch(String searchText) {
        search.setSearchText(searchText);
        viewer.refresh();
    }
    
    /**
     * Sets the components to display.
     * 
     * @param collection The collection of components.
     */
    public void setComponents(Collection<?> collection) {
    	viewer.setInput(collection);
    }
    
    /**
     * Enable or disable component selection.
     * 
     * @param state The new enabled state.
     */
    public void enabled(boolean state) {
    	viewer.getControl().setEnabled(state);
    }

    private void createTable(Composite parent, int tableStyle) {
        Composite tableComposite = new Composite(parent, SWT.NONE);
        tableComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

        viewer = new TableViewer(tableComposite, tableStyle);
        viewer.getControl().setEnabled(false);
        viewer.setContentProvider(ArrayContentProvider.getInstance());

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
            	Configuration component = (Configuration) element;
                return component.getDisplayName();
            }
        });

        final int weight = 100;
        tableColumnLayout.setColumnData(nameColumn, new ColumnWeightData(weight, false));
    }
}
