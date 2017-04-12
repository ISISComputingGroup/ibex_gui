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

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;

import uk.ac.stfc.isis.ibex.configserver.editing.EditableConfiguration;
import uk.ac.stfc.isis.ibex.configserver.editing.EditableIoc;
import uk.ac.stfc.isis.ibex.validators.MessageDisplayer;

/**
 * Dialog window for adding and editing IOCs.
 */
public class IocDialog extends TitleAreaDialog implements MessageDisplayer {
    protected EditableConfiguration config;

    protected Button btnPrev;
    protected Button btnOk;
    protected Composite content;
    protected EditPanel editIocPanel;
    protected TempEditableIoc tempIoc;

    /** Error messages that are displayed. <Source, message> */
    protected Map<String, String> errorMessages = new HashMap<String, String>();

    protected final boolean readOnly;
    protected static final Display DISPLAY = Display.getCurrent();

    /**
     * Constructor for the IOC dialog.
     * 
     * @param parent
     *            The parent composite.
     * @param config
     *            The configuration currently being edited.
     * @param ioc
     *            The IOC to add.
     */
    public IocDialog(Shell parent, EditableConfiguration config, EditableIoc ioc, boolean readOnly) {
        super(parent);
        this.config = config;
        this.tempIoc = new TempEditableIoc(ioc);
        this.readOnly = readOnly;
    }

    @Override
    protected void createButtonsForButtonBar(Composite parent) {
        // Only edit page is relevant if pre-existing IOC.
        btnOk = createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, false);
        createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
        btnOk.setFocus();
    }

    @Override
    protected Control createDialogArea(Composite parent) {
        content = new Composite(parent, SWT.NONE);
        content.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        content.setLayout(new GridLayout(1, false));

        editIocPanel = new EditPanel(content, SWT.NONE, this);
        editIocPanel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        editIocPanel.setIOC(tempIoc);

        this.setTitle("Edit IOC: " + tempIoc.getName());

        content.layout();

        if (readOnly) {
            disableControls(this.editIocPanel);
            this.setTitle("View IOC: " + tempIoc.getName());
        }

        return editIocPanel;
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
     * Update IOC and add to configuration when confirming changes.
     */
    @Override
    protected void okPressed() {
        tempIoc.saveIoc();
        super.okPressed();
    }

    /**
     * Disables a control and all its descendant elements, but re-enables the
     * ancestors of Tabfolders contained within to allow their navigation.
     * 
     * @param control
     *            The top control of all content to be disabled
     */
    private void disableControls(Control control) {
        if (control instanceof TabFolder) {
            enableAncestors(control);
        } else {
            control.setEnabled(false);
        }
        if (control instanceof Composite) {
            Composite comp = (Composite) control;
            for (Control c : comp.getChildren()) {
                disableControls(c);
            }
        }
    }

    /**
     * Recursively enables the ancestor tree of a given control.
     * 
     * @param control
     *            The control whose parents should be enabled.
     */
    private void enableAncestors(Control control) {
        control.setEnabled(true);
        if (!(control.getParent() instanceof Shell)) {
            enableAncestors(control.getParent());
        }
    }
}
