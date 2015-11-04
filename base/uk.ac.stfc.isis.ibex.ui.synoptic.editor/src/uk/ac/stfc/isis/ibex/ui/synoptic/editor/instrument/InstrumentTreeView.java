
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

/*
 * Copyright (C) 2013-2014 Research Councils UK (STFC)
 *
 * This file is part of the Instrument Control Project at ISIS.
 *
 * This code and information are provided "as is" without warranty of any 
 * kind, either expressed or implied, including but not limited to the
 * implied warranties of merchantability and/or fitness for a particular 
 * purpose.
 */
package uk.ac.stfc.isis.ibex.ui.synoptic.editor.instrument;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Tree;

import uk.ac.stfc.isis.ibex.synoptic.model.desc.ComponentDescription;
import uk.ac.stfc.isis.ibex.ui.synoptic.editor.component.ComponentContentProvider;
import uk.ac.stfc.isis.ibex.ui.synoptic.editor.component.ComponentDragListener;
import uk.ac.stfc.isis.ibex.ui.synoptic.editor.component.ComponentDropListener;
import uk.ac.stfc.isis.ibex.ui.synoptic.editor.component.ComponentLabelProvider;
import uk.ac.stfc.isis.ibex.ui.synoptic.editor.model.IComponentSelectionListener;
import uk.ac.stfc.isis.ibex.ui.synoptic.editor.model.IInstrumentUpdateListener;
import uk.ac.stfc.isis.ibex.ui.synoptic.editor.model.SynopticViewModel;
import uk.ac.stfc.isis.ibex.ui.synoptic.editor.model.UpdateTypes;

public class InstrumentTreeView extends Composite {
	private SynopticViewModel synopticViewModel;
	private ComponentDescription currentDragSource;

	private TreeViewer treeViewer;
	private MenuItem mnuDeleteSelected;
    private MenuItem mnuCopySelected;


	public InstrumentTreeView(Composite parent,
 final SynopticViewModel synopticViewModel) {
		super(parent, SWT.NONE);

		this.synopticViewModel = synopticViewModel;
        this.synopticViewModel.addInstrumentUpdateListener(new IInstrumentUpdateListener() {
            @Override
            public void instrumentUpdated(UpdateTypes updateType) {
                if (updateType == UpdateTypes.NEW_INSTRUMENT) {
                    setTreeInput();
                } else if (updateType == UpdateTypes.COPY_COMPONENT) {
                    treeViewer.setExpandedState(synopticViewModel.getSelectedComponent(), true);
                }
                refresh();
            }
        });
				
		this.synopticViewModel
			.addComponentSelectionListener(new IComponentSelectionListener() {
				@Override
				public void selectionChanged(
					ComponentDescription oldSelection,
					ComponentDescription newSelection) {

					if (newSelection != null) {
						treeViewer.setSelection(new StructuredSelection(newSelection));
					}
			}
		});

		setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		setLayout(new GridLayout(1, false));

		createControls(this);
	}

	public void createControls(Composite parent) {
		treeViewer = new TreeViewer(parent, SWT.SINGLE | SWT.BORDER);
		treeViewer.getTree().setLayoutData(
				new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		// support drag and drop
		int operations = DND.DROP_MOVE;
		Transfer[] transferTypes = new Transfer[] { TextTransfer.getInstance() };
		treeViewer.addDragSupport(operations, transferTypes,
				new ComponentDragListener(this));
		treeViewer.addDropSupport(operations, transferTypes,
				new ComponentDropListener(this, synopticViewModel));

		treeViewer.setContentProvider(new ComponentContentProvider());
		treeViewer.setLabelProvider(new ComponentLabelProvider());
		setTreeInput();

		Tree tree = treeViewer.getTree();

		// Set viewModel selection when selection made
		tree.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				ComponentDescription selected = getSelected();
				synopticViewModel.setSelectedComponent(selected);
				mnuDeleteSelected.setEnabled(selected != null);
			}
		});

		// Delete key
		tree.addKeyListener(new KeyListener() {
			@Override
			public void keyReleased(KeyEvent e) {
			}

			@Override
			public void keyPressed(KeyEvent e) {
				if (e.keyCode == SWT.DEL) {
					synopticViewModel.removeSelected();
					refresh();
				}
			}
		});

		// Right click context menu
		Menu contextMenu = new Menu(this.getShell(), SWT.POP_UP);
		tree.setMenu(contextMenu);

        mnuCopySelected = new MenuItem(contextMenu, SWT.NONE);
        mnuCopySelected.setText("Copy Component");
        mnuCopySelected.addListener(SWT.Selection, new Listener() {
            @Override
            public void handleEvent(Event event) {
                synopticViewModel.copySelected();
                refresh();
            }
        });

		mnuDeleteSelected = new MenuItem(contextMenu, SWT.NONE);
        mnuDeleteSelected.setText("Delete Component");
		mnuDeleteSelected.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event event) {
				synopticViewModel.removeSelected();
				refresh();
			}
		});
	}

	public ComponentDescription getSelected() {
		IStructuredSelection selection = (IStructuredSelection) treeViewer
				.getSelection();
		return (ComponentDescription) selection.getFirstElement();
	}

	public ComponentDescription getCurrentDragSource() {
		return currentDragSource;
	}

	public void setCurrentDragSource(ComponentDescription currentDragSource) {
		this.currentDragSource = currentDragSource;
	}

	public void refresh() {
		treeViewer.refresh();
	}

	public TreeViewer getTreeViewer() {
		return treeViewer;
	}

	private void setTreeInput() {
		treeViewer.setInput(synopticViewModel.getInstrument());
		treeViewer.expandAll();
	}
}
