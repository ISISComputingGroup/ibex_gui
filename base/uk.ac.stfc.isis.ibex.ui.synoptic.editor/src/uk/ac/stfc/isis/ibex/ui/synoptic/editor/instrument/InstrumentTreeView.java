
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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.typed.BeanProperties;
import org.eclipse.jface.databinding.viewers.typed.ViewerProperties;
import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSourceAdapter;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
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
import uk.ac.stfc.isis.ibex.ui.synoptic.editor.component.ComponentDropListener;
import uk.ac.stfc.isis.ibex.ui.synoptic.editor.component.ComponentLabelProvider;
import uk.ac.stfc.isis.ibex.ui.synoptic.editor.model.IInstrumentUpdateListener;
import uk.ac.stfc.isis.ibex.ui.synoptic.editor.model.SynopticViewModel;
import uk.ac.stfc.isis.ibex.ui.synoptic.editor.model.UpdateTypes;

/**
 * The view of the whole instrument within the editor.
 */
public class InstrumentTreeView extends Composite {
    private SynopticViewModel synopticViewModel;

    private TreeViewer treeViewer;
    private MenuItem mnuDeleteSelected;
    private MenuItem mnuCopySelected;

    /**
     * Constructor for the view.
     * 
     * @param parent
     *            The parent composite that this view belongs to.
     * @param synopticViewModel
     *            The view model that this view is based on.
     */
    public InstrumentTreeView(Composite parent, final SynopticViewModel synopticViewModel) {
	super(parent, SWT.NONE);

	this.synopticViewModel = synopticViewModel;

	synopticViewModel.addInstrumentUpdateListener(new IInstrumentUpdateListener() {
	    @Override
	    public void instrumentUpdated(UpdateTypes updateType) {
		if (updateType == UpdateTypes.COPY_COMPONENT) {
		    for (ComponentDescription selectedComponent : synopticViewModel.getSelectedComponents()) {
			treeViewer.setExpandedState(selectedComponent, true);
		    }
		}
		refresh();
	    }
	});

	synopticViewModel.addPropertyChangeListener("refreshTree", new PropertyChangeListener() {
	    @Override
	    public void propertyChange(PropertyChangeEvent evt) {
		refresh();
	    }
	});

	setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
	setLayout(new GridLayout(1, false));

	createControls(this);
    }

    /**
     * Creates the controls for the view.
     * 
     * @param parent
     *            The parent composite that this is part of.
     */
    public void createControls(Composite parent) {
	treeViewer = new TreeViewer(parent, SWT.MULTI | SWT.BORDER);
	treeViewer.getTree().setLayoutData(
		new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

	// support drag and drop
	int operations = DND.DROP_MOVE;
	Transfer[] transferTypes = new Transfer[] {LocalSelectionTransfer.getTransfer()};
	treeViewer.addDragSupport(operations, transferTypes, new DragSourceAdapter());

	treeViewer.addDropSupport(operations, transferTypes,
		new ComponentDropListener(treeViewer, synopticViewModel));

	treeViewer.setContentProvider(new ComponentContentProvider());
	treeViewer.setLabelProvider(new ComponentLabelProvider());
	treeViewer.setInput(synopticViewModel.getSynoptic());
	treeViewer.expandAll();

	Tree tree = treeViewer.getTree();

	// Right click context menu
	Menu contextMenu = new Menu(this.getShell(), SWT.POP_UP);
	tree.setMenu(contextMenu);

	mnuCopySelected = new MenuItem(contextMenu, SWT.NONE);
	mnuCopySelected.setText("Copy Component");
	mnuCopySelected.addListener(SWT.Selection, new Listener() {
	    @Override
	    public void handleEvent(Event event) {
		synopticViewModel.copySelectedComponent();
	    }
	});

	mnuDeleteSelected = new MenuItem(contextMenu, SWT.NONE);
	mnuDeleteSelected.setText("Delete Component");
	mnuDeleteSelected.addListener(SWT.Selection, new Listener() {
	    @Override
	    public void handleEvent(Event event) {
		synopticViewModel.removeSelectedComponent();
	    }
	});

	bind();
    }

    private void bind() {
	DataBindingContext cnt = new DataBindingContext();

	cnt.bindList(ViewerProperties.multipleSelection().observe(treeViewer),
		BeanProperties.list("selectedComponents").observe(synopticViewModel));

	// Set menu items to disabled under some selections
	synopticViewModel.addPropertyChangeListener("selectedComponents", new PropertyChangeListener() {
	    @Override
	    public void propertyChange(PropertyChangeEvent evt) {
		List<ComponentDescription> selected = synopticViewModel.getSelectedComponents();
		mnuDeleteSelected.setEnabled(selected != null && !selected.isEmpty());
		mnuCopySelected.setEnabled(
			selected != null && !selected.isEmpty() && synopticViewModel.selectedHaveSameParent());
	    }
	});

	// Delete and copy key
	treeViewer.getTree().addKeyListener(new KeyAdapter() {
	    @Override
	    public void keyPressed(KeyEvent e) {
		if (e.keyCode == SWT.DEL) {
		    synopticViewModel.removeSelectedComponent();
		} else if ((e.stateMask == SWT.CTRL) && (e.keyCode == 'c')) {
		    synopticViewModel.copySelectedComponent();
		}
	    }
	});
    }

    /**
     * Refresh the view.
     */
    public void refresh() {
	treeViewer.refresh();
    }
}
