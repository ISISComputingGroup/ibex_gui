
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

package uk.ac.stfc.isis.ibex.ui.synoptic.editor.target;

import java.util.List;

import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

import uk.ac.stfc.isis.ibex.synoptic.model.desc.ComponentDescription;
import uk.ac.stfc.isis.ibex.synoptic.model.desc.Property;
import uk.ac.stfc.isis.ibex.ui.synoptic.editor.model.IComponentSelectionListener;
import uk.ac.stfc.isis.ibex.ui.synoptic.editor.model.IInstrumentUpdateListener;
import uk.ac.stfc.isis.ibex.ui.synoptic.editor.model.SynopticViewModel;
import uk.ac.stfc.isis.ibex.ui.synoptic.editor.model.UpdateTypes;

public class TargetPropertyList extends Composite {
	private ListViewer list;
	private Button btnDelete;
	private Button btnAdd;
	
	private SynopticViewModel instrument;
	
	public TargetPropertyList(Composite parent, final SynopticViewModel instrument) 
	{
		super(parent, SWT.NONE);
		
		this.instrument = instrument;
		
		instrument.addComponentSelectionListener(new IComponentSelectionListener() {			
			@Override
			public void selectionChanged(List<ComponentDescription> oldSelection, List<ComponentDescription> newSelection) {
				if (newSelection != null && newSelection.size() == 1) {
					showPropertyList(newSelection.iterator().next());
				} else {
					showPropertyList(null);
				}
			}
		});
		
		instrument.addInstrumentUpdateListener(new IInstrumentUpdateListener() {	
			@Override
			public void instrumentUpdated(UpdateTypes updateType) {
				if (updateType == UpdateTypes.EDIT_PROPERTY 
					|| updateType == UpdateTypes.NEW_PROPERTY 
					|| updateType == UpdateTypes.DELETE_PROPERTY) {
					
					int selected;
					
					switch (updateType) {
						case EDIT_PROPERTY:
							selected = list.getList().getSelectionIndex();
							break;
						case NEW_PROPERTY: 
							selected = list.getList().getItemCount();
							break;
						case DELETE_PROPERTY:
						default:
							selected = -1;
							break;
					}
					showPropertyList(instrument.getSelectedComponent());
					list.refresh();
					list.getList().setSelection(selected);
					setButtonStates();
				}
			}
		});
		
		GridLayout compositeLayout = new GridLayout(1, false);
		compositeLayout.marginHeight = 0;
		compositeLayout.marginWidth = 0;
		
		setLayout(compositeLayout);
		setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		createControls(this);
	}
	
	public void createControls(Composite parent) {
		list = new ListViewer(parent, SWT.BORDER | SWT.V_SCROLL);
		list.getList().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
	    list.setContentProvider(new PropertyContentProvider());
	    list.setLabelProvider(new PropertyLabelProvider());
	    
	    list.addSelectionChangedListener(new ISelectionChangedListener() {
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				instrument.setSelectedProperty(getSelectedProperty());
				setButtonStates();
			}
		});
	    
	    Composite controlComposite = new Composite(parent, SWT.NONE);
	    controlComposite.setLayout(new GridLayout(1, false));
	    controlComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
	    {
		    btnAdd = new Button(controlComposite, SWT.NONE);
		    btnAdd.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		    btnAdd.setText("Add New Property");
			btnAdd.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					instrument.addNewProperty();
				}
			});
		    
		    btnDelete = new Button(controlComposite, SWT.NONE);
			btnDelete.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
			btnDelete.setText("Remove Property");
			btnDelete.setEnabled(false);
			btnDelete.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					instrument.removeSelectedProperty();
				}
			});
	    } 
	}
	
	public void showPropertyList(ComponentDescription component) {
		if (component != null && component.target() != null) {
			list.setInput(component.target().properties());
		} else {
			list.setInput(null);
		}
	}
	
	public Property getSelectedProperty() {
		IStructuredSelection selection = (IStructuredSelection) list.getSelection();
		return (Property) selection.getFirstElement();
	}
	
	private void setButtonStates() {
		btnDelete.setEnabled(getSelectedProperty() != null);
	}
}
