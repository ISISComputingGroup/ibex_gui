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

import java.util.Comparator;

import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

import uk.ac.stfc.isis.ibex.configserver.editing.EditableIoc;
import uk.ac.stfc.isis.ibex.ui.widgets.HelpButton;

import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.dialogs.FilteredTree;

/**
 * Dialog panel for selecting a new IOC to add to a configuration.
 */
public class AddPanel extends Composite {
	private final Button expandButton;
	private final Button  collapseButton;
	
    private FilteredTree availableIocsTree;
    private static final int TREE_HEIGHT = 300;
    private static final int SPACING = 25;

    private static final String HELP_LINK = "https://github.com/ISISComputingGroup/ibex_user_manual/wiki/Create-And-Manage-Configurations#iocs-tab";
	private HelpButton helpButton;

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
        
      //Add Expand and Collapse Tree buttons
  		Composite expansionComposite = new Composite(this, SWT.FILL);
  		expansionComposite.setLayout(new GridLayout(2, true));
  		expandButton = new Button(expansionComposite, SWT.NONE);
  		expandButton.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
  		expandButton.setText("\u25BC Expand All");
  		expandButton.addSelectionListener(new SelectionAdapter() {
  			@Override
  			public void widgetSelected(SelectionEvent e) {
  				availableIocsTree.getViewer().expandAll();
  			}
  		});
  		
  		collapseButton = new Button(expansionComposite, SWT.NONE);
  		collapseButton.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
  		collapseButton.setText("\u25B2 Collapse All");
  		collapseButton.addSelectionListener(new SelectionAdapter() {
  			@Override
  			public void widgetSelected(SelectionEvent e) {
  				availableIocsTree.getViewer().collapseAll();
  			}
  		});

        // Add selection tree
  		Composite treeComposite = new Composite(this, SWT.FILL);
  		treeComposite.setLayout(new GridLayout(1, true));
        availableIocsTree = new FilteredTree(treeComposite, SWT.FULL_SELECTION, new IocPatternFilter(), true, true);
        final var viewer = availableIocsTree.getViewer();
        viewer.setContentProvider(new IOCContentProvider());
        viewer.setLabelProvider(new IOCLabelProvider());
        viewer.setComparator(new IOCViewerComparator(Comparator.naturalOrder()));
        viewer.setInput(viewModel.getAvailableIocs());
        
        GridData gdIocTable = new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1);
        gdIocTable.heightHint = TREE_HEIGHT;
        treeComposite.setLayoutData(gdIocTable);
        
        helpButton = new HelpButton(treeComposite, HELP_LINK);

        bind(viewModel);
    }

    /**
     * Binds view model values to widgets.
     * 
     * @param viewModel
     *            The IOC view model.
     */
    private void bind(final AddPanelViewModel viewModel) {

    	final var viewer = availableIocsTree.getViewer();
    	viewer.addSelectionChangedListener(new ISelectionChangedListener() {
           @Override
           public void selectionChanged(SelectionChangedEvent event) {
               TreeItem selectedIoc = viewer.getTree().getSelection()[0];
               if (selectedIoc.getData() instanceof EditableIoc) {
            	   viewModel.setSelectedName(selectedIoc.getText());
            	   viewModel.setCurrentSelection(EditableIoc.class.cast(selectedIoc.getData()).getDescription());
               } else {
            	   viewModel.setSelectedName(null);
            	   viewModel.setCurrentSelection(null);
               }
              
            }
        });
        
        // Enable selection by double click.
    	viewer.getTree().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseDoubleClick(MouseEvent e) {
                viewModel.iocConfirmed();
            }
        });
    }
}
