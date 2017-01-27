
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

package uk.ac.stfc.isis.ibex.ui.configserver.editing.pvs;

import java.util.HashSet;

import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

import uk.ac.stfc.isis.ibex.configserver.configuration.PVDefaultValue;
import uk.ac.stfc.isis.ibex.ui.configserver.editing.iocs.IIocDependentPanel;
import uk.ac.stfc.isis.ibex.ui.configserver.editing.iocs.IocViewModel;
import uk.ac.stfc.isis.ibex.validators.MessageDisplayer;


/**
 * The panel for displaying and editing the PV values of a given IOC.
 */
@SuppressWarnings("checkstyle:magicnumber")
public class IocPVsEditorPanel extends Composite implements IIocDependentPanel {
	private IocPVsTable iocPVsTable;
	private IocPVDetailsPanel details;
    private IocViewModel viewModel;
	private Button btnAdd;
    private Button btnRemove;
    private final String newPVName = "NEW_PV";
	
    /**
     * Constructor for the panel.
     * 
     * @param parent
     *            The parent composite that this panel belongs to.
     * @param style
     *            The SWT style of the panel.
     * @param messageDisplayer
     *            The message displayer to post errors to.
     */
    public IocPVsEditorPanel(Composite parent, int style, final MessageDisplayer messageDisplayer) {
		super(parent, style);
		setLayout(new GridLayout(1, false));
		
		iocPVsTable = new IocPVsTable(this, SWT.NONE, 0);
		GridData gdIocPVsTable = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
        gdIocPVsTable.heightHint = 150;
		iocPVsTable.setLayoutData(gdIocPVsTable);
        iocPVsTable.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (btnRemove.isEnabled() && (e.keyCode == SWT.DEL)) {
                    removeSelectedPV();
                }
            }
        });
		
		Composite composite = new Composite(this, SWT.NONE);
		composite.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1));
		composite.setLayout(new GridLayout(2, false));
		
		btnAdd = new Button(composite, SWT.NONE);
		GridData gdBtnAdd = new GridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1);
		gdBtnAdd.widthHint = 70;
		btnAdd.setLayoutData(gdBtnAdd);
		btnAdd.setText("Add");
		btnAdd.setEnabled(false);
		
        btnRemove = new Button(composite, SWT.NONE);
		GridData gdBtnRemove = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gdBtnRemove.widthHint = 70;
		btnRemove.setLayoutData(gdBtnRemove);
		btnRemove.setText("Remove");
		btnRemove.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
                removeSelectedPV();
			}
		});
		btnRemove.setEnabled(false);
		
		btnAdd.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
                PVDefaultValue selected = new PVDefaultValue(generateNewName(), "NEW_VALUE");
                viewModel.getPvVals().add(selected);
                iocPVsTable.setRows(viewModel.getPvVals());
                iocPVsTable.setSelection(viewModel.getPvVals().size() - 1);

                // Why is this not happening automatically
                btnRemove.setEnabled(true);
                details.setPV(selected, viewModel);
			}
		});

		details = new IocPVDetailsPanel(this, SWT.NONE, messageDisplayer);
		Composite detailsComposite = details;
		detailsComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		iocPVsTable.addSelectionChangedListener(new ISelectionChangedListener() {
			@Override
			public void selectionChanged(SelectionChangedEvent arg0) {
				PVDefaultValue selected = iocPVsTable.firstSelectedRow();
				btnRemove.setEnabled(selected != null);
                details.setPV(selected, viewModel);
			}
		});
		details.setEnabled(false);
	}


    private String generateNewName() {
        HashSet<String> names = new HashSet<String>();
        for (PVDefaultValue pv : viewModel.getPvVals()) {
            names.add(pv.getName());
        }
        String name;
        int i = 0;
        do {
            name = newPVName;
            if (i > 0) {
                name = name + Integer.toString(i);
            }
            i++;
        } while (names.contains(name));
        return name;
    }

	@Override
    public void setViewModel(IocViewModel viewModel) {
        this.viewModel = viewModel;
        iocPVsTable.setRows(viewModel != null ? viewModel.getPvVals() : null);
        boolean enabled = viewModel != null && viewModel.getIoc().isEditable();
		setEnabled(enabled);
		btnAdd.setEnabled(enabled);
		details.setEnabled(enabled);
        details.setPVs(viewModel.getIoc().getAvailablePVs());
	}

    private void removeSelectedPV() {
        viewModel.getPvVals().remove(iocPVsTable.firstSelectedRow());
        iocPVsTable.setRows(viewModel.getPvVals());
    }
}
