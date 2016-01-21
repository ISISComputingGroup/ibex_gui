
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

package uk.ac.stfc.isis.ibex.ui.synoptic.editor.pv;

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
import org.eclipse.wb.swt.ResourceManager;

import uk.ac.stfc.isis.ibex.synoptic.model.desc.ComponentDescription;
import uk.ac.stfc.isis.ibex.synoptic.model.desc.PV;
import uk.ac.stfc.isis.ibex.ui.synoptic.editor.model.IComponentSelectionListener;
import uk.ac.stfc.isis.ibex.ui.synoptic.editor.model.IInstrumentUpdateListener;
import uk.ac.stfc.isis.ibex.ui.synoptic.editor.model.SynopticViewModel;
import uk.ac.stfc.isis.ibex.ui.synoptic.editor.model.UpdateTypes;

public class PVList extends Composite {
	private ListViewer list;
	
	private Button btnDelete;
	private Button btnAdd;
	private Button btnUp;
	private Button btnDown;
	
	private SynopticViewModel instrument;
	
	public PVList(Composite parent, final SynopticViewModel instrument) {
		super(parent, SWT.NONE);
		
		this.instrument = instrument;
		
		GridLayout compositeLayout = new GridLayout(2, false);
		compositeLayout.marginHeight = 0;
		compositeLayout.marginWidth = 0;
		
		setLayout(compositeLayout);
		setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		instrument.addComponentSelectionListener(new IComponentSelectionListener() {			
			@Override
			public void selectionChanged(List<ComponentDescription> oldSelection, List<ComponentDescription> newSelection) {
				if (newSelection != null && newSelection.size() == 1) {
					showPvList(newSelection.iterator().next());
				} else {
					showPvList(null);
				}
			}
		});
		
		instrument.addInstrumentUpdateListener(new IInstrumentUpdateListener() {	
			@Override
			public void instrumentUpdated(UpdateTypes updateType) {
				if (updateType == UpdateTypes.EDIT_PV) {
					list.refresh();
					setButtonStates();
				}
			}
		});
		
		createControls(this);
	}
	
	public void createControls(Composite parent) {
		list = new ListViewer(parent, SWT.BORDER | SWT.V_SCROLL);
		list.getList().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
	    list.setContentProvider(new PvContentProvider());
	    list.setLabelProvider(new PvLabelProvider());
	    
	    list.addSelectionChangedListener(new ISelectionChangedListener() {
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				updateSelection();
			}
		});
	    
	    Composite moveComposite = new Composite(parent, SWT.NONE);
	    moveComposite.setLayout(new GridLayout(1, false));
	    moveComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));

        btnUp = new Button(moveComposite, SWT.NONE);
        btnUp.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
        btnUp.setImage(ResourceManager.getPluginImage("uk.ac.stfc.isis.ibex.ui", "icons/move_up.png"));
        btnUp.setEnabled(false);
        btnUp.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                instrument.promoteSelectedPV();
            }
        });

        btnDown = new Button(moveComposite, SWT.NONE);
        btnDown.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
        btnDown.setImage(ResourceManager.getPluginImage("uk.ac.stfc.isis.ibex.ui", "icons/move_down.png"));
        btnDown.setEnabled(false);
        btnDown.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                instrument.demoteSelectedPV();
            }
        });
	    
	    Composite controlComposite = new Composite(parent, SWT.NONE);
	    controlComposite.setLayout(new GridLayout(1, false));
	    controlComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

        btnAdd = new Button(controlComposite, SWT.NONE);
        btnAdd.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
        btnAdd.setText("Add New PV");
        btnAdd.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                int index = instrument.addNewPV();
                list.getList().select(index);
                updateSelection();
            }
        });

        btnDelete = new Button(controlComposite, SWT.NONE);
        btnDelete.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
        btnDelete.setText("Remove PV");
        btnDelete.setEnabled(false);
        btnDelete.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                instrument.removeSelectedPV();
            }
        });
	}
	
	public void showPvList(ComponentDescription component) {
		if (component != null) {
			list.setInput(component.pvs());
		}
	}
	
	public PV getSelectedPV() {
		IStructuredSelection selection = (IStructuredSelection) list.getSelection();
		return (PV) selection.getFirstElement();
	}
	
	private void setButtonStates() {
		btnDelete.setEnabled(getSelectedPV() != null);
		btnUp.setEnabled(instrument.canPromotePV());
		btnDown.setEnabled(instrument.canDemotePV());
	}

	private void updateSelection() {
		instrument.setSelectedPV(getSelectedPV());
		setButtonStates();
	}
}
