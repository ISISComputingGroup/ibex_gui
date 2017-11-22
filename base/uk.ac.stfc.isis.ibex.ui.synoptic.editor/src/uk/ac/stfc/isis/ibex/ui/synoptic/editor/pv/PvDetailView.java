
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

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.jface.databinding.viewers.ViewersObservables;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.swt.SWT;
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

import uk.ac.stfc.isis.ibex.synoptic.model.desc.IO;

/**
 * Composite that shows the options to set the PV details, and allows
 * choosing a PV either by a block, or by a PV. Choosing a block will
 * also automatically set the name.
 *
 */
@SuppressWarnings("checkstyle:magicnumber")
public class PvDetailView extends Composite {
	private Composite noSelectionComposite;
    private Composite selectionComposite;
	private Composite fieldsComposite;
	
    private PvDetailViewModel model;

    private IO[] modeList = IO.values();
	
	private Text txtName;
	private Text txtAddress;
	private ComboViewer cmboMode;
	
	private Button btnPickPV;
	
	private Composite buttonsComposite;
	private Button btnSelectBlock;

    /**
     * The constructor for the panel used to edit PV details.
     * 
     * @param parent
     *            The composite that holds this panel.
     * @param model
     *            The view model that provides the display logic for the panel.
     */
    public PvDetailView(Composite parent, PvDetailViewModel model) {
		super(parent, SWT.NONE);
		
        this.model = model;
		setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		createControls(this);

        bind(model);
	}
	
    /**
     * Creates the controls to be displayed on the panel.
     * 
     * @param parent
     *            The composite to display the controls on.
     */
	public void createControls(Composite parent) {	
		setLayout(new GridLayout(1, false));

		noSelectionComposite = new Composite(parent, SWT.NONE);
		noSelectionComposite.setLayoutData(new GridData(SWT.CENTER, SWT.FILL, true, false, 1, 1));
		noSelectionComposite.setLayout(new FillLayout());

        Label lblNoSelection = new Label(noSelectionComposite, SWT.NONE);
        lblNoSelection.setText("Select a PV to view/edit details");
		
        selectionComposite = new Composite(parent, SWT.NONE);
        selectionComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
        selectionComposite.setLayout(new GridLayout(1, false));

        fieldsComposite = new Composite(selectionComposite, SWT.NONE);
		fieldsComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		fieldsComposite.setLayout(new GridLayout(2, false));

        Label lblName = new Label(fieldsComposite, SWT.NONE);
        lblName.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        lblName.setText("Name");

        txtName = new Text(fieldsComposite, SWT.BORDER);
        txtName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

        Label lblAddress = new Label(fieldsComposite, SWT.NONE);
        lblAddress.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        lblAddress.setText("Address");

        txtAddress = new Text(fieldsComposite, SWT.BORDER);
        txtAddress.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

        Label lblMode = new Label(fieldsComposite, SWT.NONE);
        lblMode.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        lblMode.setText("Mode");

        cmboMode = new ComboViewer(fieldsComposite, SWT.READ_ONLY);
        cmboMode.getCombo().setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

        cmboMode.setContentProvider(ArrayContentProvider.getInstance());
        cmboMode.setInput(modeList);
        cmboMode.getCombo().select(0);

        buttonsComposite = new Composite(selectionComposite, SWT.NONE);
        GridData gdComposite = new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1);
        gdComposite.widthHint = 210;
        buttonsComposite.setLayoutData(gdComposite);
        buttonsComposite.setLayout(new RowLayout(SWT.HORIZONTAL));

        btnSelectBlock = new Button(buttonsComposite, SWT.NONE);
        btnSelectBlock.setLayoutData(new RowData(100, SWT.DEFAULT));
        btnSelectBlock.setText("Select Block");
        btnSelectBlock.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                model.openBlockDialog();
            }
        });

        btnPickPV = new Button(buttonsComposite, SWT.NONE);
        btnPickPV.setLayoutData(new RowData(100, SWT.DEFAULT));
        btnPickPV.setText("Select PV");
        btnPickPV.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                model.openPvDialog();
            }
        });
	}
	
    private void bind(PvDetailViewModel model) {
        UpdateValueStrategy notConverter = new UpdateValueStrategy() {
            @Override
            public Object convert(Object value) {
                return !(Boolean) value;
            }
        };

        DataBindingContext bindingContext = new DataBindingContext();

        bindingContext.bindValue(WidgetProperties.visible().observe(selectionComposite),
                BeanProperties.value("selectionVisible").observe(model));
        bindingContext.bindValue(WidgetProperties.visible().observe(noSelectionComposite),
                BeanProperties.value("selectionVisible").observe(model), null, notConverter);
        bindingContext.bindValue(SWTObservables.observeText(txtName, SWT.Modify),
                BeanProperties.value("pvName").observe(model));
        bindingContext.bindValue(SWTObservables.observeText(txtAddress, SWT.Modify),
                BeanProperties.value("pvAddress").observe(model));
        bindingContext.bindValue(ViewersObservables.observeSingleSelection(cmboMode),
                BeanProperties.value("pvMode").observe(model));
    }
}
