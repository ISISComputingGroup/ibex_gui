
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

package uk.ac.stfc.isis.ibex.ui.synoptic.editor.instrument;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

import uk.ac.stfc.isis.ibex.synoptic.model.desc.ComponentDescription;
import uk.ac.stfc.isis.ibex.ui.synoptic.editor.model.SynopticViewModel;

/**
 * This Composite provides the controls relating to the tree view of the instrument
 * components.
 * 
 */
public class InstrumentTreeControls extends Composite {
	
	private SynopticViewModel synopticViewModel;

	private Button btnDelete;
	private Button btnAdd;
	private Button btnShowBeam;
	private Button btnCopyComponent;
	
	public InstrumentTreeControls(Composite parent,
			SynopticViewModel instrument) {
		super(parent, SWT.NONE);

		this.synopticViewModel = instrument;

        instrument.addPropertyChangeListener("refreshTree", new PropertyChangeListener() {
			@Override
            public void propertyChange(PropertyChangeEvent evt) {
                refresh();
            }
		});

        setLayout(new GridLayout(2, true));
		GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
		this.setLayoutData(gridData);

		createControls();
	}

	public void createControls() {
		btnAdd = new Button(this, SWT.NONE);
		btnAdd.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		btnAdd.setText("Add Component");
		btnAdd.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				synopticViewModel.addNewComponent();
			}
		});
		
		btnCopyComponent = new Button(this, SWT.NONE);
		btnCopyComponent.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		btnCopyComponent.setText("Copy Component");
        btnCopyComponent.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                synopticViewModel.copySelectedComponent();
            }
        });
        
        btnDelete = new Button(this, SWT.NONE);
        btnDelete.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
        btnDelete.setText("Delete Component");
        btnDelete.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                synopticViewModel.removeSelectedComponent();
            }
        });
		
		btnShowBeam = new Button(this, SWT.CHECK | SWT.CENTER);
        btnShowBeam.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, false, 1, 1));
		btnShowBeam.setText("Show Beam");
		btnShowBeam.setSelection(synopticViewModel.getShowBeam());
		
		btnShowBeam.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent event) {
	            Button btn = (Button) event.getSource();
	            synopticViewModel.setShowBeam(btn.getSelection());			
			}
		});

        synopticViewModel.addPropertyChangeListener("compSelection", new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                @SuppressWarnings("unchecked")
                List<ComponentDescription> newSelection = (List<ComponentDescription>) evt.getNewValue();
				btnDelete.setEnabled(newSelection != null && !newSelection.isEmpty());
				btnCopyComponent.setEnabled(newSelection != null && !newSelection.isEmpty() && synopticViewModel.selectedHaveSameParent());
			}
		});
	}

	public void refresh() {
		btnShowBeam.setSelection(synopticViewModel.getShowBeam());
	}
}
