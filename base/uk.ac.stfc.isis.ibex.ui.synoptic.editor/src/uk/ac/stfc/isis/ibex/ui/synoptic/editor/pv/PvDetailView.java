
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

import java.util.Arrays;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import uk.ac.stfc.isis.ibex.instrument.pv.PVType;
import uk.ac.stfc.isis.ibex.synoptic.model.desc.IO;
import uk.ac.stfc.isis.ibex.synoptic.model.desc.PV;
import uk.ac.stfc.isis.ibex.ui.synoptic.editor.blockselector.BlockSelector;
import uk.ac.stfc.isis.ibex.ui.synoptic.editor.model.IPVSelectionListener;
import uk.ac.stfc.isis.ibex.ui.synoptic.editor.model.SynopticViewModel;
import uk.ac.stfc.isis.ibex.validators.PvValidator;

/**
 * Composite that shows the options to set the PV details, and allows
 * choosing a PV either by a block, or by a PV. Choosing a block will
 * also automatically set the name.
 *
 */
@SuppressWarnings("checkstyle:magicnumber")
public class PvDetailView extends Composite {
	private Composite labelComposite;
	private Composite fieldsComposite;
	
	private SynopticViewModel instrument;
	
	private PV selectedPv;
	
	private boolean updateLock;
	
	private Text txtName;
	private Text txtAddress;
	private ComboViewer cmboMode;
	private ComboViewer cmboType;
	
	private Button btnPickPV;
	
	private static IO[] modeList = IO.values();
	private static PVType[] typeList = PVType.values();
	private Composite buttonsComposite;
	private Button btnSelectBlock;

    private Label lblError;

	public PvDetailView(Composite parent, SynopticViewModel instrument) {
		super(parent, SWT.NONE);
		
		this.instrument = instrument;
		setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		if (instrument != null) {
			instrument.addPVSelectionListener(new IPVSelectionListener() {			
				@Override
				public void selectionChanged(PV oldSelection, PV newSelection) {
					showPV(newSelection);
				}
			});
		}
		createControls(this);
		
		buttonsComposite = new Composite(this, SWT.NONE);
		GridData gdComposite = new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1);
		gdComposite.widthHint = 210;
		buttonsComposite.setLayoutData(gdComposite);
		RowLayout rlButtonsComposite = new RowLayout(SWT.HORIZONTAL);
		buttonsComposite.setLayout(rlButtonsComposite);
		
		btnSelectBlock = new Button(buttonsComposite, SWT.NONE);
		btnSelectBlock.setLayoutData(new RowData(100, SWT.DEFAULT));
		btnSelectBlock.setText("Select Block");
		btnSelectBlock.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				openBlockDialog();
				updateModel();
			}
		});
		
		btnPickPV = new Button(buttonsComposite, SWT.NONE);
		btnPickPV.setLayoutData(new RowData(100, SWT.DEFAULT));
		btnPickPV.setText("Select PV");
		btnPickPV.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				openPvDialog();
				updateModel();
			}
		});
		showPV(null);
	}
	
	public void createControls(Composite parent) {	
		setLayout(new GridLayout(1, false));
		labelComposite = new Composite(parent, SWT.NONE);
		labelComposite.setLayoutData(new GridData(SWT.CENTER, SWT.FILL, true, false, 1, 1));
		labelComposite.setLayout(new FillLayout());
		{
			Label lblNoSelection = new Label(labelComposite, SWT.NONE);
			lblNoSelection.setText("Select a PV to view/edit details");
		}
		
		fieldsComposite = new Composite(parent, SWT.NONE);
		fieldsComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		fieldsComposite.setLayout(new GridLayout(2, false));
		{
			Label lblName = new Label(fieldsComposite, SWT.NONE);
			lblName.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
			lblName.setText("Name");
			
			txtName = new Text(fieldsComposite, SWT.BORDER);
			txtName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
			
			txtName.addModifyListener(new ModifyListener() {
				@Override
				public void modifyText(ModifyEvent e) {
					updateModel();
				}
			});
			
			Label lblAddress = new Label(fieldsComposite, SWT.NONE);
			lblAddress.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
			lblAddress.setText("Address");
			
			txtAddress = new Text(fieldsComposite, SWT.BORDER);
			txtAddress.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
			
			txtAddress.addModifyListener(new ModifyListener() {
				@Override
				public void modifyText(ModifyEvent e) {
                    updateAddress();
				}
			});
			
			Label lblMode = new Label(fieldsComposite, SWT.NONE);
			lblMode.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
			lblMode.setText("Mode");
			
			cmboMode = new ComboViewer(fieldsComposite, SWT.READ_ONLY);
			cmboMode.getCombo().setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
			
			cmboMode.setContentProvider(ArrayContentProvider.getInstance());
			cmboMode.setInput(modeList);
			cmboMode.getCombo().select(0);
			cmboMode.addSelectionChangedListener(new ISelectionChangedListener() {
				@Override
				public void selectionChanged(SelectionChangedEvent event) {
					updateModel();
				}
			});
			
			Label lblType = new Label(fieldsComposite, SWT.NONE);
			lblType.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
			lblType.setText("Type");
			
			cmboType = new ComboViewer(fieldsComposite, SWT.READ_ONLY);
			GridData gdCmboType = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
			cmboType.getCombo().setLayoutData(gdCmboType);
			GridData gdBtnUp = new GridData(SWT.LEFT, SWT.BOTTOM, false, true, 1, 1);
			gdBtnUp.widthHint = 80;
			
			cmboType.setContentProvider(ArrayContentProvider.getInstance());
			cmboType.setInput(typeList);
			cmboType.getCombo().select(0);
			cmboType.addSelectionChangedListener(new ISelectionChangedListener() {
				@Override
				public void selectionChanged(SelectionChangedEvent event) {
					updateModel();
				}
			});

            lblError = new Label(fieldsComposite, SWT.NONE);
            lblError.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1));
		}
	}

	private void showPV(PV componentPv) {
		updateLock = true;
		
		selectedPv = componentPv;
		
		if (selectedPv != null) {
			fieldsComposite.setVisible(true);
			buttonsComposite.setVisible(true);
			labelComposite.setVisible(false);
			
			txtName.setText(selectedPv.displayName());
			PVType type = selectedPv.getPvType();
			int typeIndex = Arrays.asList(typeList).indexOf(type);
			cmboType.getCombo().select(typeIndex);
			
			//Use the full address to avoid confusion
			txtAddress.setText(selectedPv.fullAddress());
			
			IO mode = selectedPv.recordType().io();
			typeIndex = Arrays.asList(modeList).indexOf(mode);
			cmboMode.getCombo().select(typeIndex);
		} else {
			fieldsComposite.setVisible(false);
			buttonsComposite.setVisible(false);
			labelComposite.setVisible(true);
			
			txtName.setText("");
			txtAddress.setText("");
			cmboMode.getCombo().select(0);
			cmboType.getCombo().select(0);
		}
		
		updateLock = false;
	}
	
    private void updateAddress() {
        PvValidator addressValid = new PvValidator();
        if (addressValid.validatePvAddress(txtAddress.getText())) {
            lblError.setText("");
            updateModel();
        } else {
            lblError.setText(addressValid.getErrorMessage());
        }
    }
	private void updateModel() {
		if (!updateLock && selectedPv != null) {
			int typeIndex = cmboMode.getCombo().getSelectionIndex();
			IO mode = Arrays.asList(modeList).get(typeIndex);
			String name = txtName.getText();
			String address = txtAddress.getText();
			typeIndex = cmboType.getCombo().getSelectionIndex();
			PVType type = Arrays.asList(typeList).get(typeIndex);
			instrument.updateSelectedPV(name, address, mode, type);
		}
	}
	
	private void openPvDialog() {
		PvSelector selectPV = new PvSelector();
		try {
			selectPV.execute(null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		txtAddress.setText(selectPV.getPvAddress());
	}
	
	private void openBlockDialog() {
		BlockSelector selectPV = new BlockSelector();
		try {
			selectPV.execute(null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		txtName.setText(selectPV.getBlockName());
		txtAddress.setText(selectPV.getPvAddress());
	}
}
