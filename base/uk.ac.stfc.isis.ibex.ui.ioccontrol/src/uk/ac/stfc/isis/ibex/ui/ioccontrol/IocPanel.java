
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

import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

import uk.ac.stfc.isis.ibex.configserver.IocControl;
import uk.ac.stfc.isis.ibex.configserver.IocState;
import uk.ac.stfc.isis.ibex.configserver.editing.EditableIoc;
import uk.ac.stfc.isis.ibex.ui.ioccontrol.table.IocTable;
import uk.ac.stfc.isis.ibex.ui.ioccontrol.table.IOCConfigProvider;
import uk.ac.stfc.isis.ibex.ui.ioccontrol.table.IOCContentProvider;
import uk.ac.stfc.isis.ibex.ui.ioccontrol.table.IOCLabelProvider;
import uk.ac.stfc.isis.ibex.ui.ioccontrol.table.IOCNameProvider;
import uk.ac.stfc.isis.ibex.ui.ioccontrol.table.IOCStatusProvider;

/**
 * A panel that lists the available IOCs and allows you to control them.
 */
public class IocPanel extends Composite {
	
	private final Display display = Display.getDefault();
	
	private IocTable table;
    private IocButtonPanel buttons;
	private IocControl control;
	
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
        TreeViewer availableIocsTree = new TreeViewer(this, SWT.FULL_SELECTION);
        availableIocsTree.setContentProvider(new IOCContentProvider());
        
        TreeViewerColumn mainColumn = new TreeViewerColumn(availableIocsTree, SWT.NONE);
        mainColumn.getColumn().setText("Ioc");
        mainColumn.getColumn().setWidth(100);
        mainColumn.setLabelProvider(new IOCLabelProvider());
        
        TreeViewerColumn nameColumn = new TreeViewerColumn(availableIocsTree, SWT.NONE);
        nameColumn.getColumn().setText("Name");
        nameColumn.getColumn().setWidth(100);
        nameColumn.getColumn().setAlignment(SWT.RIGHT);
        nameColumn.setLabelProvider(new IOCNameProvider());
        
        TreeViewerColumn statusColumn = new TreeViewerColumn(availableIocsTree, SWT.NONE);
        statusColumn.getColumn().setText("Name");
        statusColumn.getColumn().setWidth(100);
        statusColumn.getColumn().setAlignment(SWT.RIGHT);
        statusColumn.setLabelProvider(new IOCStatusProvider());
        
        TreeViewerColumn configColumn = new TreeViewerColumn(availableIocsTree, SWT.NONE);
        configColumn.getColumn().setText("Name");
        configColumn.getColumn().setWidth(100);
        configColumn.getColumn().setAlignment(SWT.RIGHT);
        configColumn.setLabelProvider(new IOCConfigProvider());
        
        

        
        Collection<IocState> rows = control.iocs().getValue();
        Hashtable<String, ArrayList<IocState>> availableIocs = new Hashtable<String, ArrayList<IocState>>();
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
    	availableIocsTree.setInput(availableIocs);
    	availableIocsTree.getTree().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		this.control = control;
		control.iocs().addPropertyChangeListener(updateTable, true);
		
		availableIocsTree.addSelectionChangedListener(new ISelectionChangedListener() {
			@Override
			public void selectionChanged(SelectionChangedEvent arg0) {
				Object selection = availableIocsTree.getTree().getSelection()[0].getData();
				if (selection instanceof IocState) {
					buttons.setIoc(IocState.class.cast(selection));
				}	
			}
		});

        buttons = new IocButtonPanel(this, SWT.NONE, control);
		buttons.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
	}
	
	@Override
	public void dispose() {
		super.dispose();
		control.iocs().removePropertyChangeListener(updateTable);
	}

	private void setIocs() {		
		if (control.iocs().isSet()) {
            IocState selected = table.firstSelectedRow();
			
            Collection<IocState> rows = control.iocs().getValue();
			if (rows != null) {
				table.setRows(rows);
				resetLastSelectedIoc(selected, rows);
			}			
		}
	}

    private void resetLastSelectedIoc(IocState lastSelected, Collection<IocState> rows) {
		if (lastSelected == null) {
			return;
		}
		
		// Preserve selection if possible
        for (IocState row : rows) {
			if (row.getName().equals(lastSelected.getName())) {
                table.setSelected(row);
				return;
			}
		}
	}
}
