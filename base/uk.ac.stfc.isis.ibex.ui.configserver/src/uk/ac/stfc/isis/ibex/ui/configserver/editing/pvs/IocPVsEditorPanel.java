
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

import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

import uk.ac.stfc.isis.ibex.configserver.configuration.PVDefaultValue;
import uk.ac.stfc.isis.ibex.configserver.editing.DefaultName;
import uk.ac.stfc.isis.ibex.configserver.editing.EditableIoc;
import uk.ac.stfc.isis.ibex.ui.configserver.editing.iocs.IIocDependentPanel;
import uk.ac.stfc.isis.ibex.ui.widgets.buttons.IBEXButtonBuilder;
import uk.ac.stfc.isis.ibex.validators.MessageDisplayer;


/**
 * The panel for displaying and editing the PV values of a given IOC.
 */
@SuppressWarnings("checkstyle:magicnumber")
public class IocPVsEditorPanel extends Composite implements IIocDependentPanel {
	private IocPVsTable iocPVsTable;
	private IocPVDetailsPanel details;
    private EditableIoc ioc;
	private Button btnAdd;
	private Button btnCopy;
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
                //copies selected blocks if press "Ctrl + D".
                } else if (btnCopy.isEnabled() && e.character == 0x4) {
                	copySelected();
                }
            }
        });
		
		Composite composite = new Composite(this, SWT.NONE);
		composite.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1));
		composite.setLayout(new GridLayout(3, false));
		
		btnAdd = new IBEXButtonBuilder(composite, SWT.NONE)
				.customLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1))
				.width(70)
				.text("Add")
				.build();
		
		btnAdd.setEnabled(false);
		
		btnCopy = new IBEXButtonBuilder(composite, SWT.NONE)
				.customLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1))
				.width(70)
				.text("&Duplicate")
				.build();

		btnCopy.setEnabled(false);
		btnCopy.addListener(SWT.Selection, e -> copySelected());
		
		btnRemove = new IBEXButtonBuilder(composite, SWT.NONE)
				.customLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1))
				.width(70)
				.text("Remove")
				.build();

		btnRemove.addListener(SWT.Selection, e -> removeSelectedPV());
		btnRemove.setEnabled(false);
		
		btnAdd.addListener(SWT.Selection, e -> addPVValue(new PVDefaultValue(generateNewName(newPVName), "NEW_VALUE")));

		details = new IocPVDetailsPanel(this, SWT.NONE, messageDisplayer);
		Composite detailsComposite = details;
		detailsComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		iocPVsTable.addSelectionChangedListener(new ISelectionChangedListener() {
			@Override
			public void selectionChanged(SelectionChangedEvent arg0) {
				PVDefaultValue selected = iocPVsTable.firstSelectedRow();
				btnRemove.setEnabled(selected != null);
				btnCopy.setEnabled(selected != null);
                details.setPV(selected, ioc);
			}
		});
		details.setEnabled(false);
	}


    private String generateNewName(String rootName) {
    	List<String> existingNames = ioc.getPvs().stream()
    			.map(p -> p.getName())
    			.collect(Collectors.toList());
    	
        DefaultName namer = new DefaultName(rootName);
        return namer.getUnique(existingNames);
    }

	@Override
    public void setIOC(EditableIoc ioc) {
        this.ioc = ioc;
        iocPVsTable.setRows(ioc != null ? ioc.getPvs() : null);
        boolean enabled = ioc != null && ioc.isEditable();
		btnAdd.setEnabled(enabled);
		btnCopy.setEnabled(enabled);
		details.setEnabled(enabled);
        details.setPVs(ioc.getAvailablePVs());
	}

    private void removeSelectedPV() {
        ioc.getPvs().remove(iocPVsTable.firstSelectedRow());
        iocPVsTable.setRows(ioc.getPvs());
    }
    
    private void copySelected() {
    	PVDefaultValue selected = iocPVsTable.firstSelectedRow();
    	addPVValue(new PVDefaultValue(generateNewName(selected.getName()), selected.getValue()));
    }
    
    private void addPVValue(PVDefaultValue pvToAdd) {
        ioc.getPvs().add(pvToAdd);
        iocPVsTable.setRows(ioc.getPvs());
        iocPVsTable.setSelected(pvToAdd);

        btnRemove.setEnabled(true);
        btnCopy.setEnabled(true);
        details.setPV(pvToAdd, ioc);
    }
}
