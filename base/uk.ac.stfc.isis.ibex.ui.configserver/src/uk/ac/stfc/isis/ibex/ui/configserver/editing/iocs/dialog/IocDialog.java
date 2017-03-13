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
package uk.ac.stfc.isis.ibex.ui.configserver.editing.iocs.dialog;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import com.google.common.base.Strings;

import uk.ac.stfc.isis.ibex.configserver.editing.EditableConfiguration;
import uk.ac.stfc.isis.ibex.configserver.editing.EditableIoc;
import uk.ac.stfc.isis.ibex.validators.MessageDisplayer;

/**
 * Dialog window for adding and editing IOCs.
 */
public class IocDialog extends TitleAreaDialog implements MessageDisplayer {
    EditableConfiguration config;
    boolean isNew;

    Button btnPrev;
    Button btnOk;
    Composite content;
    StackLayout stack;
    AddPanel addIocPanel;
    EditPanel editIocPanel;
    private TempEditableIoc tempIoc;
    private AddPanelViewModel addViewModel;

    /** Error messages that are displayed. <Source, message> */
    private Map<String, String> errorMessages = new HashMap<String, String>();

    private static final Display DISPLAY = Display.getCurrent();

    SelectionListener nextListener = new SelectionAdapter() {
        @Override
        public void widgetSelected(SelectionEvent e) {
            nextPage();
        }
    };

    SelectionListener prevListener = new SelectionAdapter() {
        @Override
        public void widgetSelected(SelectionEvent e) {
            previousPage();
        }
    };

    /**
     * Sets dialog content to the first page.
     */
    private void previousPage() {
        updateStack(addIocPanel);
        btnPrev.setVisible(false);
        btnOk.addSelectionListener(nextListener);
        btnOk.setData(IDialogConstants.NO_ID);
        btnOk.setText("Next");
    }

    /**
     * Sets dialog content to the second page.
     */
    private void nextPage() {
        tempIoc = addViewModel.getSelectedIoc();
        editIocPanel.setViewModel(tempIoc);
        updateStack(editIocPanel);
        btnPrev.setVisible(true);
        btnOk.removeSelectionListener(nextListener);
        btnOk.setData(IDialogConstants.OK_ID);
        btnOk.setText(IDialogConstants.OK_LABEL);
    }

    /**
     * Constructor for the IOC dialog.
     * 
     * @param parent
     *            The parent composite.
     * @param config
     *            The configuration currently being edited.
     * @param ioc
     *            The IOC to add.
     * @param isNew
     *            Flag indicating whether IOC to edit is newly added or
     *            pre-existing.
     */
    public IocDialog(Shell parent, EditableConfiguration config, EditableIoc ioc, boolean isNew) {
        super(parent);
        this.isNew = isNew;
        this.config = config;
        this.addViewModel = new AddPanelViewModel(config.getAvailableIocs());
        this.tempIoc = new TempEditableIoc(ioc);
    }

    @Override
    protected void createButtonsForButtonBar(Composite parent) {
        if (isNew) {
            // Make IOC selection page available if new IOC.
            btnPrev = createButton(parent, IDialogConstants.NO_ID, "Previous", false);
            btnPrev.addSelectionListener(prevListener);
            btnPrev.setVisible(false);

            btnOk = createButton(parent, IDialogConstants.NO_ID, "Next", false);
            btnOk.addSelectionListener(nextListener);
            btnOk.setEnabled(false);
        } else {
            // Only edit page is relevant if pre-existing IOC.
            btnOk = createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, false);
        }

        createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
        btnOk.setFocus();

        // Disable OK button if no IOC is selected.
        addViewModel.addPropertyChangeListener("selectedName", new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                String name = (String) evt.getNewValue();
                if (!Strings.isNullOrEmpty(name)) {
                    btnOk.setEnabled(true);
                    btnOk.setFocus();
                } else {
                    btnOk.setEnabled(false);
                }
            }
        });

        // Enables selection by double click
        addViewModel.addPropertyChangeListener("confirmed", new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                nextPage();
            }
        });
    }

    @Override
    protected Control createDialogArea(Composite parent) {
        content = new Composite(parent, SWT.NONE);
        content.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        stack = new StackLayout();
        content.setLayout(stack);

        addIocPanel = new AddPanel(content, SWT.NONE, addViewModel);
        addIocPanel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

        editIocPanel = new EditPanel(content, SWT.NONE, this);
        editIocPanel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        editIocPanel.setViewModel(tempIoc);

        if (isNew) {
            stack.topControl = addIocPanel;
            this.setTitle("Add IOC");
        } else {
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

    /**
     * Method for swapping content in the dialog window when moving between
     * pages.
     * 
     * @param top
     *            The panel to be displayed.
     */
    private void updateStack(final Control top) {
        DISPLAY.asyncExec(new Runnable() {
            @Override
            public void run() {
                stack.topControl = top;
                content.layout();
            }
        });
    }

    /**
     * Update IOC and add to configuration when confirming changes.
     */
    @Override
    protected void okPressed() {
        tempIoc.saveIoc();
        if (isNew) {
            config.addIoc(tempIoc);
        }
        super.okPressed();
    }
}
