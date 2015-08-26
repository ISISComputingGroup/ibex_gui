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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Text;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;

public class SaveSynopticDialog extends TitleAreaDialog {

    private Text txtName;

    private String newName = "";
    private List<String> existingSynoptics;

    public SaveSynopticDialog(Shell parent, String currentName,
	    Collection<String> existingSynoptics) {
	super(parent);
	setShellStyle(SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
	this.existingSynoptics = new ArrayList<>(existingSynoptics);
    }

    public String getNewName() {
	return newName;
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
	GridLayout gl_container = new GridLayout(1, false);
	container.setLayout(gl_container);

	Composite composite = new Composite(container, SWT.NONE);
	composite.setLayout(new GridLayout(2, false));
	GridData gd_composite = new GridData(SWT.FILL, SWT.CENTER, true, false,
		1, 1);
	gd_composite.widthHint = 95;
	composite.setLayoutData(gd_composite);

	Label lblConfigurationName = new Label(composite, SWT.NONE);
	lblConfigurationName.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER,
		false, false, 1, 1));
	lblConfigurationName.setText("Name:");

	txtName = new Text(composite, SWT.BORDER);
	GridData gd_txtName = new GridData(SWT.FILL, SWT.CENTER, true, false,
		1, 1);
	gd_txtName.widthHint = 383;
	txtName.setLayoutData(gd_txtName);
	txtName.setBounds(0, 0, 76, 21);
	new Label(composite, SWT.NONE);

	Label lblConfigurationNameCannot = new Label(composite, SWT.NONE);
	lblConfigurationNameCannot.setLayoutData(new GridData(SWT.CENTER,
		SWT.CENTER, false, false, 1, 1));
	lblConfigurationNameCannot
		.setText("Name must start with a letter and contain only [A-Z], [0-9] and \"_\"");
	new Label(composite, SWT.NONE);
	new Label(composite, SWT.NONE);

	txtName.addModifyListener(new ModifyListener() {
	    @Override
	    public void modifyText(ModifyEvent arg0) {
		update();
	    }
	});

	update();

	return container;
    }

    /**
     * Create contents of the button bar.
     * 
     * @param parent
     */
    @Override
    protected void createButtonsForButtonBar(Composite parent) {
	Button button = createButton(parent, IDialogConstants.OK_ID,
		IDialogConstants.OK_LABEL, true);
	button.setEnabled(false);
	button.addSelectionListener(new SelectionAdapter() {
	    @Override
	    public void widgetSelected(SelectionEvent e) {
	    }
	});
	createButton(parent, IDialogConstants.CANCEL_ID,
		IDialogConstants.CANCEL_LABEL, false);
    }

    @Override
    protected void okPressed() {
	if (validate(name())) {
	    newName = name();
	    // Warn about overwriting if already exists
	    if (isDuplicate(newName)) {
		boolean userCancelled = askUserWhetherToOverwrite(newName);
		if (userCancelled) {
		    return;
		}
	    }

	    super.okPressed();
	    close();
	}
	// else ignore the click
    }

    private boolean askUserWhetherToOverwrite(String newName) {
	MessageBox box = new MessageBox(getShell(), SWT.ICON_WARNING | SWT.YES
		| SWT.NO);
	box.setMessage("The synoptic \"" + newName
		+ "\" already exists. \n Do you want to replace it?");

	// Message boxes return the ID of the button to close, so need to check
	// that value..
	return box.open() != SWT.YES;
    }

    private String name() {
	return txtName.getText().trim();
    }

    private void update() {
	checkInput(name());
    }

    private void allowSaving(boolean canSave) {
	Button ok = getButton(IDialogConstants.OK_ID);
	if (ok != null) {
	    ok.setEnabled(canSave);
	}
    }

    private boolean isDuplicate(final String name) {
	return Iterables.any(existingSynoptics, new Predicate<String>() {
	    @Override
	    public boolean apply(String existing) {
		return compareIgnoringCase(existing, name);
	    }
	});
    }

    private boolean compareIgnoringCase(String text, String other) {
	return text.toUpperCase().equals(other.toUpperCase());
    }

    private Boolean validate(String name) {
	// Must start with a letter and contain no spaces
	return name.matches("^[a-zA-Z][a-zA-Z0-9_]*$");
    }

    private void checkInput(String name) {
	setErrorMessage(null);
	setMessage(null);

	String error = getErrorMessage(name);
	if (error.length() > 0) {
	    setErrorMessage(error);
	    allowSaving(false);
	    return;
	}

	allowSaving(true);
	String warning = getWarningMessage(name);
	if (warning.length() > 0) {
	    setMessage(warning);
	}
    }

    private String getErrorMessage(String name) {
	if (name.length() == 0) {
	    return "Name cannot be blank";
	}

	if (!validate(name)) {
	    return "Name contains invalid characters";
	}

	return "";
    }

    private String getWarningMessage(String name) {
	if (isDuplicate(name)) {
	    return "A synoptic with this name already exists";
	}

	return "";
    }
}
