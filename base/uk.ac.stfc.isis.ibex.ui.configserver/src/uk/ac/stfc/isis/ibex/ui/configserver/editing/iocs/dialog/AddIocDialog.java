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

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

import com.google.common.base.Strings;

import uk.ac.stfc.isis.ibex.configserver.editing.EditableConfiguration;
import uk.ac.stfc.isis.ibex.configserver.editing.EditableIoc;

/**
 *
 */
public class AddIocDialog extends IocDialog {

    private StackLayout stack;
    private AddPanel addIocPanel;
    private AddPanelViewModel addViewModel;

    /**
     * @param parent
     * @param config
     * @param ioc
     */
    public AddIocDialog(Shell parent, EditableConfiguration config, EditableIoc ioc) {
        super(parent, config, ioc, false);
        this.addViewModel = new AddPanelViewModel(config.getAvailableIocs());
    }

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
        editIocPanel.setIOC(tempIoc);
        updateStack(editIocPanel);
        btnPrev.setVisible(true);
        btnOk.removeSelectionListener(nextListener);
        btnOk.setData(IDialogConstants.OK_ID);
        btnOk.setText(IDialogConstants.OK_LABEL);
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

    @Override
    protected void createButtonsForButtonBar(Composite parent) {
        btnPrev = createButton(parent, IDialogConstants.NO_ID, "Previous", false);
        btnPrev.addSelectionListener(prevListener);
        btnPrev.setVisible(false);

        btnOk = createButton(parent, IDialogConstants.NO_ID, "Next", false);
        btnOk.addSelectionListener(nextListener);
        btnOk.setEnabled(false);

        createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);

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
        editIocPanel.setIOC(tempIoc);

        stack.topControl = addIocPanel;
        this.setTitle("Add IOC");

        content.layout();

        return addIocPanel;
    }

    /**
     * Update IOC and add to configuration when confirming changes.
     */
    @Override
    protected void okPressed() {
        tempIoc.saveIoc();
        config.addIoc(tempIoc);
        super.okPressed();
    }
}
