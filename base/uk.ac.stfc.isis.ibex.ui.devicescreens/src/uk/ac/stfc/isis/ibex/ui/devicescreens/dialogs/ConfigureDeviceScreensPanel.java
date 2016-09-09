
/*
 * This file is part of the ISIS IBEX application.
 * Copyright (C) 2012-2016 Science & Technology Facilities Council.
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

/**
 * 
 */
package uk.ac.stfc.isis.ibex.ui.devicescreens.dialogs;

import java.util.Collection;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.jface.databinding.viewers.ObservableListContentProvider;
import org.eclipse.jface.databinding.viewers.ViewerProperties;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.ResourceManager;

import uk.ac.stfc.isis.ibex.devicescreens.desc.DeviceDescription;
import uk.ac.stfc.isis.ibex.ui.devicescreens.models.DeviceScreensDescriptionViewModel;

/**
 * 
 */
public class ConfigureDeviceScreensPanel extends Composite {

    /** The groups viewer. */
    private ListViewer devicesViewer;

    /** The group list. */
    private List devicesList;

    private Composite targetComposite;

    private Composite orderComposite;
    private Composite btnsComposite;

    private Collection<String> availableOPIs;
    private Composite detailsComposite;
    private TargetNameWidget targetSelect;

    private DeviceScreensDescriptionViewModel viewModel;

    /** binding context. */
    private DataBindingContext bindingContext = new DataBindingContext();

    /**
     * @param parent
     * @param style
     */
    public ConfigureDeviceScreensPanel(Composite parent, int style, Collection<String> availableOPIs,
            DeviceScreensDescriptionViewModel viewModel) {
        super(parent, style);
        this.viewModel = viewModel;
        this.availableOPIs = availableOPIs;

        setLayout(new GridLayout(1, true));

        targetComposite = new Composite(this, SWT.NONE);
        targetComposite.setLayout(new GridLayout(2, false));
        targetComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

        createListGroup();
        createTargetGroup();

    }

    private void createListGroup() {
        Group grpList = new Group(targetComposite, SWT.NONE);
        grpList.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
        grpList.setText("Device Screens");
        grpList.setLayout(new GridLayout(2, false));

        devicesViewer = new ListViewer(grpList, SWT.BORDER | SWT.V_SCROLL | SWT.SINGLE);
        ObservableListContentProvider contentProvider = new ObservableListContentProvider();
        devicesViewer.setContentProvider(contentProvider);
        devicesViewer.setInput(BeanProperties.list("screens").observe(viewModel));

        devicesList = devicesViewer.getList();
        GridData gdViewer = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
        // gdViewer.widthHint = 125;
        devicesList.setLayoutData(gdViewer);
        devicesList.addKeyListener(new KeyListener() {
            @Override
            public void keyReleased(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.keyCode == SWT.DEL) {
                    // groupEditorViewModel.removeGroup(groupList.getSelectionIndex());
                }
            }
        });

        orderComposite = new Composite(grpList, SWT.NONE);
        orderComposite.setLayout(new GridLayout(1, false));
        orderComposite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, true, 1, 1));

        Button btnUp = new Button(orderComposite, SWT.NONE);
        GridData gdBtnUp = new GridData(SWT.LEFT, SWT.BOTTOM, false, false, 1, 1);
        gdBtnUp.widthHint = 25;
        btnUp.setLayoutData(gdBtnUp);
        btnUp.setImage(ResourceManager.getPluginImage("uk.ac.stfc.isis.ibex.ui", "icons/move_up.png"));
        btnUp.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                // groupEditorViewModel.moveGroupUp(groupList.getSelectionIndex());
            }
        });

        Button btnDown = new Button(orderComposite, SWT.NONE);
        GridData gdBtnDown = new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1);
        gdBtnDown.widthHint = 25;
        btnDown.setLayoutData(gdBtnDown);
        btnDown.setImage(ResourceManager.getPluginImage("uk.ac.stfc.isis.ibex.ui", "icons/move_down.png"));
        btnDown.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                // groupEditorViewModel.moveGroupDown(groupList.getSelectionIndex());
            }
        });

        btnsComposite = new Composite(grpList, SWT.NONE);
        btnsComposite.setLayout(new GridLayout(2, false));
        btnsComposite.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1));

        Button btnAdd = new Button(btnsComposite, SWT.NONE);
        btnAdd.setText("Add");
        GridData gdBtnAdd = new GridData(SWT.LEFT, SWT.BOTTOM, false, false, 1, 1);
        gdBtnAdd.widthHint = 100;
        btnAdd.setLayoutData(gdBtnAdd);

        Button btnDelete = new Button(btnsComposite, SWT.NONE);
        btnDelete.setText("Delete");
        GridData gdBtnDelete = new GridData(SWT.LEFT, SWT.BOTTOM, false, false, 1, 1);
        gdBtnDelete.widthHint = 100;
        btnDelete.setLayoutData(gdBtnDelete);

        devicesViewer.addSelectionChangedListener(new ISelectionChangedListener() {

            @Override
            public void selectionChanged(SelectionChangedEvent arg0) {
                // int selectionIndex = devicesList.getSelectionIndex();
                // viewModel.setSelectedScreen(selectionIndex);
//                groupNamesValidator.setSelectedIndex(selectionIndex);
//                if (selectionIndex == -1) {
//                    groupNamesValidator.validate("");
//                } else {
//                    groupNamesValidator.validate(groupList.getSelection()[0]);
//                }
//                boolean canEditSelected = groupEditorViewModel.canEditSelected(selectionIndex);
//
//                btnRemove.setEnabled(canEditSelected);
//                name.setEnabled(canEditSelected);
//                blocksEditor.setEnabled(canEditSelected);

            }
        });
    }

    private void createTargetGroup() {
        Group grpDetails = new Group(targetComposite, SWT.NONE);
        grpDetails.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
        grpDetails.setText("Device Screens");
        grpDetails.setLayout(new GridLayout(1, false));

        detailsComposite = new Composite(grpDetails, SWT.NONE);
        detailsComposite.setLayout(new GridLayout(2, false));
        detailsComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));

        Label lblName = new Label(detailsComposite, SWT.NONE);
        lblName.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, false, false, 1, 1));
        lblName.setText("Name");

        Text txtName = new Text(detailsComposite, SWT.BORDER);
        txtName.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1));

        bindingContext.bindValue(
                WidgetProperties.text(SWT.Modify).observe(txtName), ViewerProperties.singleSelection()
                        .value(BeanProperties.value("name", DeviceDescription.class)).observe(devicesViewer),
                null, null);

        txtName.addModifyListener(new ModifyListener() {
            @Override
            public void modifyText(ModifyEvent arg0) {
                devicesViewer.refresh();
            }
        });

        Label lblTarget = new Label(detailsComposite, SWT.NONE);
        lblTarget.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, false, false, 1, 1));
        lblTarget.setText("Target");

        targetSelect = new TargetNameWidget(detailsComposite, availableOPIs, viewModel);
        targetSelect.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        targetSelect.bindToSelected(devicesViewer);

        Label lblDescription = new Label(detailsComposite, SWT.NONE);
        lblDescription.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, false, false, 1, 1));
        lblDescription.setText("Description");

        TargetDescriptionWidget desc = new TargetDescriptionWidget(detailsComposite, viewModel);
        GridData gdDescription = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
        gdDescription.heightHint = 70;
        desc.setLayoutData(gdDescription);
        desc.bindToSelected(devicesViewer);

        TargetPropertiesView propertiesView = new TargetPropertiesView(detailsComposite);
        propertiesView.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
    }

}
