
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

package uk.ac.stfc.isis.ibex.ui.ioccontrol;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Hashtable;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.dialogs.FilteredTree;

import uk.ac.stfc.isis.ibex.configserver.IocControl;
import uk.ac.stfc.isis.ibex.configserver.IocState;
import uk.ac.stfc.isis.ibex.ui.ioccontrol.table.IOCConfigProvider;
import uk.ac.stfc.isis.ibex.ui.ioccontrol.table.IOCContentProvider;
import uk.ac.stfc.isis.ibex.ui.ioccontrol.table.IOCLabelProvider;
import uk.ac.stfc.isis.ibex.ui.ioccontrol.table.IOCList;
import uk.ac.stfc.isis.ibex.ui.ioccontrol.table.IOCPatternFilter;
import uk.ac.stfc.isis.ibex.ui.ioccontrol.table.IOCRunModeProvider;
import uk.ac.stfc.isis.ibex.ui.ioccontrol.table.IOCStatusProvider;
import uk.ac.stfc.isis.ibex.ui.ioccontrol.table.IOCViewerComparator;

/**
 * A panel that lists the available IOCs and allows you to control them.
 */
public class IocPanel extends Composite {
	
	private final Display display = Display.getDefault();
	private final Button expandButton;
	private final Button  collapseButton;
	private FilteredTree availableIocsTree;
    private IocButtonPanel buttons;
	private IocControl control;
	private Hashtable<String, IOCList> availableIocs;
	
	private static final int COLUMN_WIDTH = 70;
	
	private PropertyChangeListener updateTable = new PropertyChangeListener() {	
		@Override
		public void propertyChange(PropertyChangeEvent arg0) {
			display.asyncExec(new Runnable() {
				@Override
				public void run() {
					setIocs();
				}
			});
		}
	};
	
	/**
	 * The constructor for the panel.
	 * @param parent The parent composite that this panel belongs to.
	 * @param control The model that this panel uses to control IOCs
	 * @param style The SWT style for the panel.
	 */
	public IocPanel(Composite parent, final IocControl control, int style) {
		super(parent, style);
		
		setLayout(new GridLayout(1, false));
		
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
  		treeComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
  		treeComposite.setLayout(new GridLayout(1, true));

        availableIocsTree = new FilteredTree(treeComposite, SWT.FULL_SELECTION, new IOCPatternFilter(), true, true);
        final var viewer = availableIocsTree.getViewer();
        viewer.setContentProvider(new IOCContentProvider());
        viewer.setComparator(new IOCViewerComparator(Comparator.naturalOrder()));
        
        TreeViewerColumn mainColumn = new TreeViewerColumn(viewer, SWT.NONE);
        mainColumn.getColumn().setText("Ioc");
        mainColumn.setLabelProvider(new IOCLabelProvider());
        
        TreeViewerColumn statusColumn = new TreeViewerColumn(viewer, SWT.NONE);
        statusColumn.getColumn().setText("Status");
        statusColumn.getColumn().setWidth(COLUMN_WIDTH);
        statusColumn.getColumn().setAlignment(SWT.CENTER);
        statusColumn.setLabelProvider(new IOCStatusProvider());
        
        TreeViewerColumn configColumn = new TreeViewerColumn(viewer, SWT.NONE);
        configColumn.getColumn().setText("In Config?");
        configColumn.getColumn().setWidth(COLUMN_WIDTH);
        configColumn.getColumn().setAlignment(SWT.CENTER);
        configColumn.setLabelProvider(new IOCConfigProvider());
        
        if (IocPanelHandler.getDisplayRunMode()) {
        	TreeViewerColumn runModeColumn = new TreeViewerColumn(viewer, SWT.NONE);
        	runModeColumn.getColumn().setText("Mode");
        	runModeColumn.getColumn().setWidth(COLUMN_WIDTH);
        	runModeColumn.getColumn().setAlignment(SWT.CENTER);
        	runModeColumn.setLabelProvider(new IOCRunModeProvider());
        }
        
        Collection<IocState> rows = control.iocs().getValue();
        availableIocs = new Hashtable<String, IOCList>();
        availableIocs = updateHashtable(rows);
    	
        viewer.setInput(availableIocs);
        viewer.getTree().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
        viewer.getTree().setHeaderVisible(true);
        viewer.getTree().setLinesVisible(true);
    	mainColumn.getColumn().pack();
		
		this.control = control;
		control.iocs().addPropertyChangeListener(updateTable, true);
		
		viewer.addSelectionChangedListener(new ISelectionChangedListener() {
			@Override
			public void selectionChanged(SelectionChangedEvent arg0) {
				if (viewer.getTree().getSelection().length != 0) {
					Object selection = viewer.getTree().getSelection()[0].getData();
					if (selection instanceof IocState) {
						buttons.setIoc(IocState.class.cast(selection));
						return;
					}
				} 
				buttons.setIoc(null);	
			}
		});
        buttons = new IocButtonPanel(this, SWT.NONE, control);
		buttons.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
	}
	

	private Hashtable<String, IOCList> updateHashtable(Collection<IocState> rows) {
    	String description = "";
    	availableIocs.put("Running", new IOCList("Running"));
    	availableIocs.put("In Config", new IOCList("In Config"));
    	for (IocState ioc : rows) {
    		description = ioc.getDescription();
    		if (!availableIocs.containsKey(description)) {
    			availableIocs.put(description, new IOCList(description));
    		}
    		availableIocs.get(description).add(ioc);
    		if (ioc.getIsRunning()) {
    			availableIocs.get("Running").add(ioc);
    		}
    		if (ioc.getInCurrentConfig()) {
    			availableIocs.get("In Config").add(ioc);
    		}
    	}
    	for (String key: availableIocs.keySet()) {
    		availableIocs.get(key).sort(Comparator.naturalOrder());
    	}
		return availableIocs;
	}	
	
	
	@Override
	public void dispose() {
		super.dispose();
		control.iocs().removePropertyChangeListener(updateTable);
	}
 
	
	private void setIocs() {		 
		if (control.iocs().isSet()) {
			int[] selectedIndex = getCurrentSelection();
            ArrayList<String> elementsToExpand = getElementsToExpand();
            
            Collection<IocState> iocs = control.iocs().getValue();
			availableIocs.clear();
			availableIocs = updateHashtable(iocs);
			final var viewer = availableIocsTree.getViewer();
			viewer.setInput(availableIocs);
            
            setElementsToExpand(elementsToExpand);
            final var tree = viewer.getTree();
            if (selectedIndex[0] != -1 && selectedIndex[1] != -1) {
            	TreeItem parent = tree.getItem(selectedIndex[0]);
            	if (parent.getItemCount() < selectedIndex[1]) {
            		tree.setSelection(parent.getItem(selectedIndex[1]));	
            	} else {
            		tree.setSelection(parent);
            	}
            }
		}
	}

	
	private void setElementsToExpand(ArrayList<String> elementsToExpand) {
		ArrayList<TreePath> paths = new ArrayList<TreePath>();
		for (String topLevel :elementsToExpand) {
			IOCList[] iocArray = {availableIocs.get(topLevel)};
			paths.add(new TreePath(iocArray));
		}
		availableIocsTree.getViewer().setExpandedTreePaths(paths.toArray(new TreePath[0]));
	}

	
	private ArrayList<String> getElementsToExpand() {
		ArrayList<String> descriptionsToExpand = new ArrayList<String>();
		for (Object list :availableIocsTree.getViewer().getExpandedElements()) {
			if (list instanceof ArrayList<?>) {
		        ArrayList<?> expandedList = ArrayList.class.cast(list);
		        IocState firstInList = IocState.class.cast(expandedList.get(0));
		        descriptionsToExpand.add(firstInList.getDescription());
			}
		}
		// These two if statements make sure that items that are only expanded in Running or In Config don't
		// Get get expanded both there, and in their description.
		final var tree = availableIocsTree.getViewer().getTree();
		if (tree.getItem(0).getExpanded()) {
			descriptionsToExpand.remove(0);
		}
		if (tree.getItem(1).getExpanded()) {
			descriptionsToExpand.remove(0);
		}
		descriptionsToExpand.add("Running");
		descriptionsToExpand.add("In Config");
		return descriptionsToExpand;
	}
	
	private int[] getCurrentSelection() {
		int[] selectedIndex = {-1, -1};
		TreeItem selected = null;
		
		final var tree = availableIocsTree.getViewer().getTree();
		if (tree.getSelection().length > 0) {
			selected = tree.getSelection()[0];
		}

		if (selected != null) {
			TreeItem selectedParent = selected.getParentItem();
			if (selectedParent != null) {
				TreeItem[] treeItemList = tree.getItems(); 			
				selectedIndex[0] = treeListIndex(selectedParent, treeItemList, 0);
				selectedIndex[1] = treeListIndex(selected, selectedParent.getItems(), 1);
			}
		}
		return selectedIndex;
	}

	private int treeListIndex(TreeItem item, TreeItem[] list, int textField) {
		for (int i = 0; i < list.length; i++) {
			if (list[i].getText(textField).equals(item.getText(textField))) {
				return i;
			}
		}
		return 0;
	}
}
