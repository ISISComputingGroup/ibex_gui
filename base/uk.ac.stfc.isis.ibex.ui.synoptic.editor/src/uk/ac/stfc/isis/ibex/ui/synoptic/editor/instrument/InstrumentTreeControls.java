
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

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

import uk.ac.stfc.isis.ibex.synoptic.model.desc.ComponentDescription;
import uk.ac.stfc.isis.ibex.ui.synoptic.editor.model.IComponentSelectionListener;
import uk.ac.stfc.isis.ibex.ui.synoptic.editor.model.IInstrumentUpdateListener;
import uk.ac.stfc.isis.ibex.ui.synoptic.editor.model.SynopticViewModel;
import uk.ac.stfc.isis.ibex.ui.synoptic.editor.model.UpdateTypes;

/**
 * This Composite provides the controls relating to the tree view of the instrument
 * components.
 * 
 */
public class InstrumentTreeControls extends Composite {
	
	private SynopticViewModel instrumentViewModel;

	private Button btnDelete;
	private Button btnAdd;
	private Button btnShowBeam;
	private Button btnCopyComponent;
	
	public InstrumentTreeControls(Composite parent,
			SynopticViewModel instrument) {
		super(parent, SWT.NONE);

		this.instrumentViewModel = instrument;

		instrument.addInstrumentUpdateListener(new IInstrumentUpdateListener() {
			@Override
			public void instrumentUpdated(UpdateTypes updateType) {
				refresh();
			}
		});

		instrument.addComponentSelectionListener(new IComponentSelectionListener() {
			@Override
			public void selectionChanged(
					ComponentDescription oldSelection, 
					ComponentDescription newSelection) {
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
				instrumentViewModel.addNewComponent();
			}
		});
		
		btnCopyComponent = new Button(this, SWT.NONE);
		btnCopyComponent.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		btnCopyComponent.setText("Copy Component");
		
        btnDelete = new Button(this, SWT.NONE);
        btnDelete.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
        btnDelete.setText("Delete Component");
        btnDelete.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                instrumentViewModel.removeSelected();
            }
        });

		btnShowBeam = new Button(this, SWT.CHECK | SWT.CENTER);
        btnShowBeam.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, false, 1, 1));
		btnShowBeam.setText("Show Beam");
		btnShowBeam.setSelection(instrumentViewModel.getShowBeam());
		
		btnShowBeam.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent event) {
	            Button btn = (Button) event.getSource();
	            instrumentViewModel.setShowBeam(btn.getSelection());			
			}
		});

		refresh();
	}

	public void refresh() {
		ComponentDescription selected = instrumentViewModel.getSelectedComponent();
		btnShowBeam.setSelection(instrumentViewModel.getShowBeam());
        btnCopyComponent.setEnabled(selected != null);
		btnDelete.setEnabled(selected != null);
	}
}
