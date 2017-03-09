
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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.jface.databinding.viewers.ObservableListContentProvider;
import org.eclipse.jface.databinding.viewers.ViewersObservables;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.List;

import uk.ac.stfc.isis.ibex.ui.devicescreens.models.EditDeviceScreensDescriptionViewModel;

/**
 * The main panel for the configure device screens dialog.
 */
@SuppressWarnings("checkstyle:magicnumber")
public class ConfigureDeviceScreensListPanel extends Composite {

    /** The screens viewer. */
    private ListViewer screensViewer;

    /** The view model. */
    private EditDeviceScreensDescriptionViewModel viewModel;

    /** binding context. */
    private DataBindingContext bindingContext = new DataBindingContext();

    /**
     * The constructor.
     * 
     * @param parent the main composite
     * @param style the SWT style
     * @param viewModel the view model
     */
    public ConfigureDeviceScreensListPanel(Composite parent, int style, EditDeviceScreensDescriptionViewModel viewModel) {
        super(parent, style);
        this.viewModel = viewModel;

        setLayout(new GridLayout(1, false));

        createListGroup(this);
    }

    /**
     * Creates the screens list part of the display.
     * 
     * @param mainComposite the parent composite
     */
    private void createListGroup(Composite mainComposite) {
        Group grpList = new Group(mainComposite, SWT.NONE);
        grpList.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true, 1, 1));
        grpList.setText("Device Screens");
        grpList.setLayout(new GridLayout(2, false));

        screensViewer = new ListViewer(grpList, SWT.BORDER | SWT.V_SCROLL | SWT.MULTI);
        screensViewer.setContentProvider(new ObservableListContentProvider());
        screensViewer.setInput(BeanProperties.list("screens").observe(viewModel));

        viewModel.addPropertyChangeListener("name", new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                screensViewer.refresh();
            }
        });

        List devicesList = screensViewer.getList();
        GridData gdViewer = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
        devicesList.setLayoutData(gdViewer);
        devicesList.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.keyCode == SWT.DEL) {
                    viewModel.deleteSelectedScreens();
                }
            }
        });

        Composite orderComposite = new Composite(grpList, SWT.NONE);
        orderComposite.setLayout(new GridLayout(1, false));
        orderComposite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, true, 1, 1));

        Composite btnsComposite = new Composite(grpList, SWT.NONE);
        btnsComposite.setLayout(new GridLayout(2, false));
        btnsComposite.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1));

        Button btnAdd = new Button(btnsComposite, SWT.NONE);
        btnAdd.setText("Add");
        GridData gdBtnAdd = new GridData(SWT.LEFT, SWT.BOTTOM, false, false, 1, 1);
        gdBtnAdd.widthHint = 100;
        btnAdd.setLayoutData(gdBtnAdd);
        btnAdd.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                viewModel.addScreen();
            }
        });

        Button btnDelete = new Button(btnsComposite, SWT.NONE);
        btnDelete.setText("Delete");
        GridData gdBtnDelete = new GridData(SWT.LEFT, SWT.BOTTOM, false, false, 1, 1);
        gdBtnDelete.widthHint = 100;
        btnDelete.setLayoutData(gdBtnDelete);
        btnDelete.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                viewModel.deleteSelectedScreens();
            }
        });

        bindingContext.bindList(ViewersObservables.observeMultiSelection(screensViewer),
                BeanProperties.list("selectedScreens").observe(viewModel));
    }

}
