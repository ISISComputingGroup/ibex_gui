
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
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.TreeItem;


import uk.ac.stfc.isis.ibex.configserver.IocControl;
import uk.ac.stfc.isis.ibex.configserver.IocState;
import uk.ac.stfc.isis.ibex.ui.ioccontrol.table.IOCConfigProvider;
import uk.ac.stfc.isis.ibex.ui.ioccontrol.table.IOCContentProvider;
import uk.ac.stfc.isis.ibex.ui.ioccontrol.table.IOCLabelProvider;
import uk.ac.stfc.isis.ibex.ui.ioccontrol.table.IOCNameProvider;
import uk.ac.stfc.isis.ibex.ui.ioccontrol.table.IOCStatusProvider;
import uk.ac.stfc.isis.ibex.ui.ioccontrol.table.IOCViewerComparator;

/**
 * A panel that lists the available IOCs and allows you to control them.
 */
public class IocPanel extends Composite {
	
	private final Display display = Display.getDefault();
	private TreeViewer availableIocsTree;
    private IocButtonPanel buttons;
	private IocControl control;
	private Hashtable<String, ArrayList<IocState>> availableIocs;
	private static final int COLUMN_WIDTH = 100;
	
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
		
		// Add selection tree
        availableIocsTree = new TreeViewer(this, SWT.FULL_SELECTION);
        availableIocsTree.setContentProvider(new IOCContentProvider());
        availableIocsTree.setComparator(new IOCViewerComparator(Comparator.naturalOrder()));
        TreeViewerColumn mainColumn = new TreeViewerColumn(availableIocsTree, SWT.NONE);
        mainColumn.getColumn().setText("Ioc");
        mainColumn.getColumn().setWidth(COLUMN_WIDTH);
        mainColumn.setLabelProvider(new IOCLabelProvider());
        
        TreeViewerColumn nameColumn = new TreeViewerColumn(availableIocsTree, SWT.NONE);
        nameColumn.getColumn().setText("Name");
        nameColumn.getColumn().setWidth(COLUMN_WIDTH);
        nameColumn.getColumn().setAlignment(SWT.RIGHT);
        nameColumn.setLabelProvider(new IOCNameProvider());
        
        TreeViewerColumn statusColumn = new TreeViewerColumn(availableIocsTree, SWT.NONE);
        statusColumn.getColumn().setText("Name");
        statusColumn.getColumn().setWidth(COLUMN_WIDTH);
        statusColumn.getColumn().setAlignment(SWT.RIGHT);
        statusColumn.setLabelProvider(new IOCStatusProvider());
        
        TreeViewerColumn configColumn = new TreeViewerColumn(availableIocsTree, SWT.NONE);
        configColumn.getColumn().setText("Name");
        configColumn.getColumn().setWidth(COLUMN_WIDTH);
        configColumn.getColumn().setAlignment(SWT.RIGHT);
        configColumn.setLabelProvider(new IOCConfigProvider());
        
        Collection<IocState> rows = control.iocs().getValue();
        availableIocs = new Hashtable<String, ArrayList<IocState>>();
        availableIocs = updateHashtable(rows);
    	
    	
    	availableIocsTree.setInput(availableIocs);
    	availableIocsTree.getTree().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		this.control = control;
		control.iocs().addPropertyChangeListener(updateTable, true);
		
		availableIocsTree.addSelectionChangedListener(new ISelectionChangedListener() {
			@Override
			public void selectionChanged(SelectionChangedEvent arg0) {
				if (availableIocsTree.getTree().getSelection().length != 0) {
					Object selection = availableIocsTree.getTree().getSelection()[0].getData();
					if (selection instanceof IocState) {
						buttons.setIoc(IocState.class.cast(selection));
					}
				} else {
					buttons.setIoc(null);
				}
			}
		});

        buttons = new IocButtonPanel(this, SWT.NONE, control);
		buttons.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
	}

	private Hashtable<String, ArrayList<IocState>> updateHashtable(Collection<IocState> rows) {
    	String description = "";
    	for (IocState ioc : rows) {
    		description = ioc.getDescription();
    		if (!availableIocs.containsKey(description)) {
    			availableIocs.put(description, new ArrayList<IocState>());
    		}
    		availableIocs.get(description).add(ioc);
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
			availableIocsTree.setInput(availableIocs);
            
            setElementsToExpand(elementsToExpand);
            TreeItem parent = availableIocsTree.getTree().getItem(selectedIndex[0]);
            availableIocsTree.getTree().setSelection(parent.getItem(selectedIndex[1]));	
		}
	}

	private void setElementsToExpand(ArrayList<String> elementsToExpand) {
		Object[] newExpandedOjects = new Object[elementsToExpand.size()];
		for (int i = 0; i < elementsToExpand.size(); i++) {
			newExpandedOjects[i] = availableIocs.get(elementsToExpand.get(i));
		}
		
		availableIocsTree.setExpandedElements(newExpandedOjects);
	}

	private ArrayList<String> getElementsToExpand() {
		ArrayList<String> descriptionsToExpand = new ArrayList<String>();
		for (Object list :availableIocsTree.getExpandedElements()) {
			if (list instanceof ArrayList<?>) {
		        ArrayList<?> expandedList = ArrayList.class.cast(list);
		        IocState firstInList = IocState.class.cast(expandedList.get(0));
		        descriptionsToExpand.add(firstInList.getDescription());
			}
		}
		return descriptionsToExpand;
	}

	private int[] getCurrentSelection() {
		int[] selectedIndex = {0, 0};
		TreeItem selected = availableIocsTree.getTree().getSelection()[0];
		if (selected != null) {
			TreeItem selectedParent = selected.getParentItem();
			TreeItem[] treeItemList = availableIocsTree.getTree().getItems(); 			
			selectedIndex[0] = treeListIndex(selectedParent, treeItemList, 0);
			selectedIndex[1] = treeListIndex(selected, selectedParent.getItems(), 1);
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
