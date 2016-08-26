
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

import org.eclipse.jface.databinding.viewers.ObservableListContentProvider;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.List;
import org.eclipse.wb.swt.ResourceManager;

/**
 * 
 */
public class DeviceScreenListView extends Composite {

    /** The groups viewer. */
    private ListViewer devicesViewer;

    /** The group list. */
    private List devicesList;

    private Composite orderComposite;
    private Composite btnsComposite;

    /**
     * @param parent
     * @param style
     */
    public DeviceScreenListView(Composite parent) {
        super(parent, SWT.NONE);
        setLayout(new GridLayout(1, false));
        setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true, 1, 1));

        Group grpList = new Group(this, SWT.NONE);
        grpList.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
        grpList.setText("Device Screens");
        grpList.setLayout(new GridLayout(2, false));

        devicesViewer = new ListViewer(grpList, SWT.BORDER | SWT.V_SCROLL | SWT.SINGLE);
        ObservableListContentProvider contentProvider = new ObservableListContentProvider();
        devicesViewer.setContentProvider(contentProvider);
//        devicesViewer.setInput(BeanProperties.list(EditableConfiguration.EDITABLE_GROUPS)
//                .observe(configurationViewModels.getConfigModel().getValue()));

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
    }

    public void createControls(Composite parent) {

    }

}
