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
package uk.ac.stfc.isis.ibex.ui.configserver.editing.iocs;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import uk.ac.stfc.isis.ibex.configserver.editing.EditableConfiguration;
import uk.ac.stfc.isis.ibex.ui.configserver.editing.AvailableIocsTable;

/**
 * Dialog panel for selecting a new IOC to add to a configuration.
 */
public class IocDialogAddPanel extends Composite {
    private AvailableIocsTable availableIocsTable;
    private Text selectedIocRb;
    private static final int TABLE_HEIGHT = 300;
    private static final int SPACING = 25;


    /**
     * Constructor for the Add IOC panel.
     * 
     * @param parent
     *            The parent composite.
     * @param style
     *            The SWT style.
     * @param config
     *            The configuration currently being edited.
     */
    public IocDialogAddPanel(Composite parent, int style, final EditableConfiguration config) {
        super(parent, style);
        GridLayout glPanel = new GridLayout(2, false);
        glPanel.verticalSpacing = SPACING;
        this.setLayout(glPanel);

        // Add selection table
        availableIocsTable = new AvailableIocsTable(this, SWT.NONE, SWT.FULL_SELECTION);
        availableIocsTable.setRows(config.getUnselectedIocs());

        GridData gdIocTable = new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1);
        gdIocTable.heightHint = TABLE_HEIGHT;
        availableIocsTable.setLayoutData(gdIocTable);

        // Add selection readback
        Label lblSelectedIoc = new Label(this, SWT.NONE);
        lblSelectedIoc.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        lblSelectedIoc.setText("Selected:");

        selectedIocRb = new Text(this, SWT.BORDER);
        selectedIocRb.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        selectedIocRb.setEditable(false);
    }

    /**
     * Sets the IOC view model used by the panel.
     * 
     * @param viewModel
     *            The view model.
     */
    public void setViewModel(final IocViewModel viewModel) {
        bind(viewModel);

        availableIocsTable.addSelectionChangedListener(new ISelectionChangedListener() {
            @Override
            public void selectionChanged(SelectionChangedEvent event) {
                String selectedIocName = availableIocsTable.firstSelectedRow().getName();
                viewModel.setName(selectedIocName);
            }
        });
        
        // Enable selection by double click.
        availableIocsTable.addMouseListener(new MouseListener() {
            
            @Override
            public void mouseUp(MouseEvent e) {
            }
            @Override
            public void mouseDown(MouseEvent e) {
            }
            @Override
            public void mouseDoubleClick(MouseEvent e) {
                String selectedIocName = availableIocsTable.firstSelectedRow().getName();
                viewModel.setIocByName(selectedIocName);
            }
        });
    }

    /**
     * Binds view model values to widgets.
     * 
     * @param viewModel
     *            The IOC view model.
     */
    private void bind(IocViewModel viewModel) {
        DataBindingContext bindingContext = new DataBindingContext();
        bindingContext.bindValue(WidgetProperties.text(SWT.Modify).observe(selectedIocRb),
                BeanProperties.value("name").observe(viewModel));
    }
}
