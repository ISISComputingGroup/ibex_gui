
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

package uk.ac.stfc.isis.ibex.ui.mainmenu.instrument;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

import uk.ac.stfc.isis.ibex.instrument.InstrumentInfo;
import uk.ac.stfc.isis.ibex.ui.mainmenu.MainMenuUI;
import uk.ac.stfc.isis.ibex.ui.mainmenu.instrument.custom.CustomInstrumentDialog;
import uk.ac.stfc.isis.ibex.validators.ErrorMessage;

@SuppressWarnings("checkstyle:magicnumber")
public class InstrumentDialog extends TitleAreaDialog {
	
    private static final String WINDOW_TITLE = "Instrument Selector";
    private static final String AREA_TITLE = "Select an Instrument";
	
	private InstrumentInfo selectedInstrument;
    private InstrumentSelectionPanel selectorPanel;
    private InstrumentSelectionViewModel selectorViewModel;
    private PropertyChangeListener errorListener;
    private Button okButton;

	protected InstrumentDialog(Shell parentShell) {
		super(parentShell);

        selectorViewModel = new InstrumentSelectionViewModel(MainMenuUI.INSTRUMENT.instruments());
        configureErrorListener();
        selectorViewModel.addPropertyChangeListener("error", errorListener);
	}
	
    public InstrumentInfo selectedInstrument() {
        return selectedInstrument;
    }

	@Override
	protected void configureShell(Shell shell) {
		super.configureShell(shell);
		shell.setText(WINDOW_TITLE);
	}

	@Override
	protected Point getInitialSize() {
        return new Point(300, 400);
	}
	
	@Override
	protected Control createDialogArea(Composite parent) {
        setTitle(AREA_TITLE);

		Composite container = (Composite) super.createDialogArea(parent);
        selectorPanel = new InstrumentSelectionPanel(container, SWT.NONE, selectorViewModel);
        selectorPanel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		return container;
	}
	
	@Override
	protected void okPressed() {
        selectedInstrument = null;
        if (selectorViewModel.selectedInstrumentExists()) {
            selectedInstrument = selectorViewModel.getSelectedInstrument();
            super.okPressed();
            return;
        }

        if (selectorViewModel.getSelectedName().isEmpty()) {
            super.okPressed();
            return;
        }

        CustomInstrumentDialog customConfigDialog = new CustomInstrumentDialog(null,
                selectorViewModel.getSelectedName());
        if (customConfigDialog.open() == Window.OK) {
            selectedInstrument = customConfigDialog.getSelectedCustomInstrument();
        } else {
            return;
        }

        super.okPressed();
	}

    @Override
    public void create() {
        super.create();
        selectorViewModel.validate();
    }

    @Override
    protected void createButtonsForButtonBar(Composite parent) {
        okButton = createButton(parent, IDialogConstants.OK_ID, "OK", true);
        createButton(parent, IDialogConstants.CANCEL_ID, "Cancel", false);
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
                ErrorMessage errorMessage = selectorViewModel.getError();
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
