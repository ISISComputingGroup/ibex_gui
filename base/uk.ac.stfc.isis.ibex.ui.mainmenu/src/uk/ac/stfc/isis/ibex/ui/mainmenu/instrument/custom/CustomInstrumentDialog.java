
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

package uk.ac.stfc.isis.ibex.ui.mainmenu.instrument.custom;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

import uk.ac.stfc.isis.ibex.instrument.InstrumentInfo;
import uk.ac.stfc.isis.ibex.validators.ErrorMessage;

/**
 * A dialog for the definition of a new custom instrument.
 * 
 */
@SuppressWarnings("checkstyle:magicnumber")
public class CustomInstrumentDialog extends TitleAreaDialog {

    private static final String WINDOW_TITLE = "Instrument Setup";
    private static final String AREA_TITLE = "Setup Custom Instrument";

    private CustomInstrumentPanel configPanel;
    private InstrumentInfo selectedCustomInstrument;
    private CustomIntrumentViewModel configViewModel;
    private PropertyChangeListener errorListener;
    private Button okButton;

    public CustomInstrumentDialog(Shell parentShell, String instrumentName) {
        super(parentShell);

        configViewModel = new CustomIntrumentViewModel(instrumentName);
        configureErrorListener();
        configViewModel.addPropertyChangeListener("error", errorListener);
    }

    public InstrumentInfo getSelectedCustomInstrument() {
        return selectedCustomInstrument;
    }

    @Override
    protected void configureShell(Shell shell) {
        super.configureShell(shell);
        shell.setText(WINDOW_TITLE);
    }

    @Override
    protected Point getInitialSize() {
        return new Point(400, 250);
    }

    @Override
    protected Control createDialogArea(Composite parent) {
        setTitle(AREA_TITLE);

        Composite container = (Composite) super.createDialogArea(parent);
        configPanel = new CustomInstrumentPanel(container, SWT.NONE, configViewModel);
        configPanel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
        return container;
    }

    @Override
    protected void okPressed() {
        selectedCustomInstrument = configViewModel.getSelectedInstrument();
        super.okPressed();
    }

    @Override
    protected void createButtonsForButtonBar(Composite parent) {
        okButton = createButton(parent, IDialogConstants.OK_ID, "OK", true);
        createButton(parent, IDialogConstants.CANCEL_ID, "Cancel", false);
    }

    @Override
    public void create() {
        super.create();
        configViewModel.validate();
    }

    private void setOkEnabled(boolean enabled) {
        if (okButton != null) {
            okButton.setEnabled(enabled);
        }
    }

    private void configureErrorListener() {
        errorListener = new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                ErrorMessage errorMessage = configViewModel.getError();
                if (errorMessage.isError()) {
                    setErrorMessage(errorMessage.getMessage());
                    setOkEnabled(false);
                    return;
                }

                setOkEnabled(true);
                setErrorMessage(null);
            }
        };
    }
}
