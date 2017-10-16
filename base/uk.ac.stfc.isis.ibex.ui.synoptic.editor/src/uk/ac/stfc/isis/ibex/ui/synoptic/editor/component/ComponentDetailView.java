
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

package uk.ac.stfc.isis.ibex.ui.synoptic.editor.component;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.jface.databinding.viewers.ViewersObservables;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import uk.ac.stfc.isis.ibex.ui.synoptic.editor.pv.PVList;
import uk.ac.stfc.isis.ibex.ui.synoptic.editor.pv.PvListViewModel;

/**
 * UI section that allows the user to view and edit the details of a component:
 * name, type, and PVs.
 */
@SuppressWarnings("checkstyle:magicnumber")
public class ComponentDetailView extends Composite {
    private static final String SELECT_COMPONENT = "Select a component to view/edit details";

    private ComponentDetailViewModel compDetailsViewModel;
    private Composite selectionComposite;
    private Composite noSelectionComposite;
	private Text txtName;
	private ComboViewer cmboType;
	private Label lblTypeIcon;
    private Label lblNoSelection;
    private boolean selectionCausedByMouseClick = false;
	private PVList pvList;

    /**
     * The constructor.
     * 
     * @param parent
     *            the parent composite
     * @param compDetailsViewModel
     *            the view model
     * @param pvListViewModel
     *            the view model specifically for the pv list
     */
	public ComponentDetailView(Composite parent,
            final ComponentDetailViewModel compDetailsViewModel, final PvListViewModel pvListViewModel) {
		super(parent, SWT.NONE);

        this.compDetailsViewModel = compDetailsViewModel;
		
		setLayout(new GridLayout(1, false));
		setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		createControls(this, pvListViewModel);

        bind(compDetailsViewModel);
	}

    /**
     * Create the widgets for the view.
     * 
     * @param parent
     *            the parent composite (this)
     * @param pvListModel
     *            the view model for the pv list
     */
	public void createControls(final Composite parent, PvListViewModel pvListModel) {
        noSelectionComposite = new Composite(parent, SWT.NONE);
        noSelectionComposite.setLayoutData(new GridData(SWT.CENTER, SWT.FILL, true,
				false, 1, 1));
        noSelectionComposite.setLayout(new GridLayout());

        lblNoSelection = new Label(noSelectionComposite, SWT.NONE);
        lblNoSelection.setText(SELECT_COMPONENT);

        selectionComposite = new Composite(parent, SWT.NONE);
        selectionComposite.setLayout(new GridLayout(2, false));
        selectionComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
				false, 1, 1));

        Label lblName = new Label(selectionComposite, SWT.NONE);
        lblName.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        lblName.setText("Name");

        txtName = new Text(selectionComposite, SWT.BORDER);
        txtName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

        Label lblType = new Label(selectionComposite, SWT.NONE);
        lblType.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        lblType.setText("Type");

        cmboType = new ComboViewer(selectionComposite, SWT.READ_ONLY);
        cmboType.getCombo().setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

        cmboType.setContentProvider(ArrayContentProvider.getInstance());
        cmboType.getCombo().addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(org.eclipse.swt.events.FocusEvent e) {
                if (!selectionCausedByMouseClick) {
                    StructuredSelection selection = (StructuredSelection) cmboType.getSelection();
                    compDetailsViewModel.updateModelType((String) selection.getFirstElement());
                }
            }
        });

        cmboType.getCombo().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseUp(MouseEvent e) {
                selectionCausedByMouseClick = true;
            }
        });

        cmboType.addSelectionChangedListener(new ISelectionChangedListener() {
            @Override
            public void selectionChanged(SelectionChangedEvent event) {
                StructuredSelection selection = (StructuredSelection) cmboType.getSelection();

                compDetailsViewModel.updateModelType((String) selection.getFirstElement());
                selectionCausedByMouseClick = false;
            }
        });

        Label lblIcon = new Label(selectionComposite, SWT.NONE);
        lblIcon.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, false, false, 1, 1));
        lblIcon.setText("Icon");

        lblTypeIcon = new Label(selectionComposite, SWT.BORDER);
        GridData iconGridData = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
        iconGridData.widthHint = 32;
        iconGridData.heightHint = 32;
        lblTypeIcon.setLayoutData(iconGridData);
        lblTypeIcon.setAlignment(SWT.CENTER);

        Label lblPvs = new Label(selectionComposite, SWT.NONE);
        lblPvs.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, false, false, 1, 1));
        lblPvs.setText("PVs");

        pvList = new PVList(selectionComposite, pvListModel);
        GridData pvGridData = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
        pvGridData.heightHint = 150;
        pvList.setLayoutData(pvGridData);
	}
	
    private void bind(ComponentDetailViewModel model) {
        UpdateValueStrategy notConverter = new UpdateValueStrategy() {
            @Override
            public Object convert(Object value) {
                return !(Boolean) value;
            }
        };

        cmboType.setInput(ComponentDetailViewModel.typeList);

        DataBindingContext bindingContext = new DataBindingContext();

        bindingContext.bindValue(WidgetProperties.visible().observe(selectionComposite),
                BeanProperties.value("selectionVisible").observe(model));
        bindingContext.bindValue(WidgetProperties.visible().observe(noSelectionComposite),
                BeanProperties.value("selectionVisible").observe(model), null, notConverter);
        bindingContext.bindValue(SWTObservables.observeText(txtName, SWT.Modify),
                BeanProperties.value("componentName").observe(model));
        bindingContext.bindValue(ViewersObservables.observeSingleSelection(cmboType),
                BeanProperties.value("compType").observe(model));
        bindingContext.bindValue(WidgetProperties.image().observe(lblTypeIcon),
                BeanProperties.value("typeIcon").observe(model));
    }
}
