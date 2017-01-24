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
package uk.ac.stfc.isis.ibex.ui.configserver.editing.iocs;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import uk.ac.stfc.isis.ibex.configserver.editing.EditableConfiguration;
import uk.ac.stfc.isis.ibex.configserver.editing.EditableIoc;
import uk.ac.stfc.isis.ibex.validators.MessageDisplayer;

/**
 *
 */
public class IocDialog extends TitleAreaDialog implements MessageDisplayer {
    EditableConfiguration config;
    EditableIoc ioc;
    boolean isNew;

    Button btnPrev;
    Button btnOk;
    Composite content;
    StackLayout stack;
    IocDialogAddPanel addIocPanel;
    IocDialogEditPanel editIocPanel;
    private IocViewModel viewModel;

    /** Error messages that are displayed. <Source, message> */
    private Map<String, String> errorMessages = new HashMap<String, String>();

    private static final Display DISPLAY = Display.getCurrent();

    SelectionListener nextListener = new SelectionListener() {
        @Override
        public void widgetSelected(SelectionEvent e) {
            updateStack(editIocPanel);
            nextPage();
        }

        @Override
        public void widgetDefaultSelected(SelectionEvent e) {
        }
    };

    SelectionListener prevListener = new SelectionListener() {
        @Override
        public void widgetSelected(SelectionEvent e) {
            updateStack(addIocPanel);
            previousPage();
        }

        @Override
        public void widgetDefaultSelected(SelectionEvent e) {
        }
    };

    private void previousPage() {
        btnPrev.setVisible(false);
        btnOk.addSelectionListener(nextListener);
        btnOk.setData(IDialogConstants.NO_ID);
        btnOk.setText("Next");
    }

    private void nextPage() {
        viewModel.setIocByName();
        this.ioc = viewModel.getIoc();
        editIocPanel.setViewModel(viewModel);
        btnPrev.setVisible(true);
        btnOk.removeSelectionListener(nextListener);
        btnOk.setData(IDialogConstants.OK_ID);
        btnOk.setText(IDialogConstants.OK_LABEL);
    }

    /**
     * @param parent
     * @param style
     */
    public IocDialog(Shell parent, EditableConfiguration config, EditableIoc ioc, boolean isNew) {
        super(parent);
        this.isNew = isNew;
        this.config = config;
        this.ioc = ioc;
        this.viewModel = new IocViewModel(config);
    }

    @Override
    protected void createButtonsForButtonBar(Composite parent) {
        if (isNew) {
            btnPrev = createButton(parent, IDialogConstants.NO_ID, "Previous", false);
            btnPrev.addSelectionListener(prevListener);
            btnPrev.setVisible(false);

            btnOk = createButton(parent, IDialogConstants.NO_ID, "Next", false);
            btnOk.addSelectionListener(nextListener);
        } else {
            btnOk = createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, false);
        }

        createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
        btnOk.setFocus();
    }

    @Override
    protected Control createDialogArea(Composite parent) {
        content = new Composite(parent, SWT.NONE);
        content.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        stack = new StackLayout();
        content.setLayout(stack);

        addIocPanel = new IocDialogAddPanel(content, SWT.NONE, config);
        addIocPanel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

        editIocPanel = new IocDialogEditPanel(content, this, SWT.NONE);
        editIocPanel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

        if (isNew) {
            addIocPanel.setViewModel(viewModel);
            stack.topControl = addIocPanel;
            this.setTitle("Add IOC");
        } else {
            viewModel.setIoc(this.ioc);
            editIocPanel.setViewModel(viewModel);
            stack.topControl = editIocPanel;
            this.setTitle("Edit IOC");
        }

        content.layout();

        return addIocPanel;
    }

    @Override
    public void setErrorMessage(String source, String error) {
        errorMessages.put(source, error);
        showErrorMessage();
    }

    /**
     * Show the current error messages.
     */
    protected void showErrorMessage() {
        StringBuilder sb = new StringBuilder();
        for (String key : errorMessages.keySet()) {
            if (errorMessages.get(key) != null) {
                sb.append(errorMessages.get(key));
                sb.append("  ");
            }
        }

        if (sb.length() > 0) {
            setErrorMessage(sb.toString());
            btnOk.setEnabled(false);
        } else {
            setErrorMessage(null);
            btnOk.setEnabled(true);
        }
    }

    private void updateStack(final Control top) {
        DISPLAY.asyncExec(new Runnable() {
            @Override
            public void run() {
                stack.topControl = top;
                content.layout();
            }
        });
    }

    @Override
    protected void okPressed() {
        viewModel.updateIoc();
        if (isNew) {
            config.addIoc(viewModel.getIoc());
        }
        super.okPressed();
    }
}
