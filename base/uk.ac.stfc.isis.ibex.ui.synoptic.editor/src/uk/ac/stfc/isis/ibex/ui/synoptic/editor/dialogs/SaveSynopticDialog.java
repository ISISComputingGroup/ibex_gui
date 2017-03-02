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

package uk.ac.stfc.isis.ibex.ui.synoptic.editor.dialogs;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * The dialog used to save a synoptic.
 */
@SuppressWarnings("checkstyle:magicnumber")
public class SaveSynopticDialog extends TitleAreaDialog {

    private Text txtName;
    private Button okButton;

    private SaveSynopticViewModel model;

    /**
     * Constructor for a dialog box to ask the user what name they wish to save
     * a synoptic under.
     * 
     * @param parent
     *            The parent shell
     * @param model
     *            The view model for this dialog
     */
    public SaveSynopticDialog(Shell parent, SaveSynopticViewModel model) {
        super(parent);
        this.model = model;
        setShellStyle(SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
    }

    @Override
    protected void configureShell(Shell shell) {
        super.configureShell(shell);
        shell.setText("Save Synoptic As");
    }

    @Override
    protected Control createDialogArea(Composite parent) {
        setTitle("Save Synoptic");
        Composite container = (Composite) super.createDialogArea(parent);
        GridLayout glContainer = new GridLayout(1, false);
        container.setLayout(glContainer);

        Composite composite = new Composite(container, SWT.NONE);
        composite.setLayout(new GridLayout(2, false));
        GridData gdComposite = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
        gdComposite.widthHint = 95;
        composite.setLayoutData(gdComposite);

        Label lblConfigurationName = new Label(composite, SWT.NONE);
        lblConfigurationName.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        lblConfigurationName.setText("Name:");

        txtName = new Text(composite, SWT.BORDER);
        GridData gdTxtName = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
        gdTxtName.widthHint = 383;
        txtName.setLayoutData(gdTxtName);
        txtName.setBounds(0, 0, 76, 21);

        return container;
    }

    private void bind() {
        DataBindingContext bindingContext = new DataBindingContext();

        model.addPropertyChangeListener("error", new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                setErrorMessage(model.getError().getMessage());
            }
        });

        setErrorMessage(model.getError().getMessage());

        bindingContext.bindValue(WidgetProperties.text(SWT.Modify).observe(txtName),
                BeanProperties.value("synopticName").observe(model));

        bindingContext.bindValue(WidgetProperties.enabled().observe(okButton),
                BeanProperties.value("savingAllowed").observe(model));
    }

    /**
     * Create contents of the button bar.
     * 
     * @param parent
     */
    @Override
    protected void createButtonsForButtonBar(Composite parent) {
        okButton = createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
        createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);

        bind(); // Need to bind after we create the buttons
    }

    @Override
    protected void okPressed() {
        if (model.isDuplicate() && !askUserWhetherToOverwrite()) {
            return;
        }
         
        super.okPressed();
         
    }

    private boolean askUserWhetherToOverwrite() {
        MessageBox box = new MessageBox(getShell(), SWT.ICON_WARNING | SWT.YES | SWT.NO);
        box.setMessage(
                "The synoptic \"" + model.getSynopticName() + "\" already exists. \n Do you want to replace it?");

        // Message boxes return the ID of the button to close, so need to check
        // that value..
        return box.open() != SWT.YES;
    }
}
