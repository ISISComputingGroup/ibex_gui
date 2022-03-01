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
package uk.ac.stfc.isis.ibex.ui.configserver.editing.iocs.dialog;

import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

import uk.ac.stfc.isis.ibex.configserver.editing.EditableIoc;
import uk.ac.stfc.isis.ibex.ui.configserver.editing.AvailableIocsTable;

import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.ISelectionListener;

import uk.ac.stfc.isis.ibex.ui.configserver.editing.iocs.dialog.IOCContentProvider;
/**
 * Dialog panel for selecting a new IOC to add to a configuration.
 */
public class AddPanel extends Composite {
    private TreeViewer availableIocsTree;
    private static final int TREE_HEIGHT = 300;
    private static final int SPACING = 25;


    /**
     * Constructor for the Add IOC panel.
     * 
     * @param parent
     *            The parent composite.
     * @param style
     *            The SWT style.
     * @param viewModel
     *            The view model that contains the data for this panel.
     */
    public AddPanel(Composite parent, int style, final AddPanelViewModel viewModel) {
        super(parent, style);
        GridLayout glPanel = new GridLayout(2, false);
        glPanel.verticalSpacing = SPACING;
        this.setLayout(glPanel);

        // Add selection table
        TreeViewer treeViewer = new TreeViewer(this, SWT.FULL_SELECTION);
        treeViewer.setContentProvider(new IOCContentProvider());
        treeViewer.setLabelProvider(new IOCLabelProvider());
        treeViewer.setInput(viewModel.getAvailableIocs());
        availableIocsTree = treeViewer;
        //availableIocsTable.setRows(viewModel.getAvailableIocs());

        GridData gdIocTable = new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1);
        gdIocTable.heightHint = TREE_HEIGHT;
        
        availableIocsTree.getTree().setLayoutData(gdIocTable);

        bind(viewModel);
    }

    /**
     * Binds view model values to widgets.
     * 
     * @param viewModel
     *            The IOC view model.
     */
    private void bind(final AddPanelViewModel viewModel) {

        availableIocsTree.addSelectionChangedListener(new ISelectionChangedListener() {
           @Override
           public void selectionChanged(SelectionChangedEvent event) {
               TreeItem selectedIoc = availableIocsTree.getTree().getSelection()[0];
               if(selectedIoc.getData() instanceof EditableIoc) {
            	   viewModel.setSelectedName(selectedIoc.getText());
            	   viewModel.setCurrentSelection(EditableIoc.class.cast(selectedIoc.getData()).getDescription());
               }
              
            }
        });
        
        // Enable selection by double click.
        availableIocsTree.getTree().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseDoubleClick(MouseEvent e) {
                viewModel.iocConfirmed();
            }
        });
    }
}
