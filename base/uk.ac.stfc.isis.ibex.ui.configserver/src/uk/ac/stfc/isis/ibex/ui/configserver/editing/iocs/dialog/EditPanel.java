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

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.jface.databinding.viewers.ViewersObservables;
import org.eclipse.jface.resource.FontDescriptor;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;

import uk.ac.stfc.isis.ibex.configserver.configuration.SimLevel;
import uk.ac.stfc.isis.ibex.configserver.editing.EditableIoc;
import uk.ac.stfc.isis.ibex.ui.configserver.editing.macros.IocMacroPanel;
import uk.ac.stfc.isis.ibex.ui.configserver.editing.pvs.IocPVsEditorPanel;
import uk.ac.stfc.isis.ibex.ui.configserver.editing.pvsets.IocPVSetsEditorPanel;
import uk.ac.stfc.isis.ibex.validators.MessageDisplayer;

/**
 * Dialog panel for editing the settings of an IOC.
 */
public class EditPanel extends Composite {

    private static final int NUM_COLS = 6;
    private static final int SPACING = 10;

    private Text selectedIoc;
    private Button autoStart;
    private Button autoRestart;
    private ComboViewer simLevel;

    private IocPVsEditorPanel pvVals;
    private IocPVSetsEditorPanel pvSets;

    /**
     * Constructor for the Edit IOC panel.
     * 
     * @param parent
     *            The parent composite.
     * @param style
     *            The SWT style
     * @param dialog
     *            The dialog displaying error messages.
     * @param editViewModel
     *            The view model for this panel.
     */
    public EditPanel(Composite parent, int style, final MessageDisplayer dialog, EditPanelViewModel editViewModel) {
        super(parent, style);
        this.setLayout(new GridLayout());

        // Add IOC details
        Composite cmpIocDetails = new Composite(this, SWT.NONE);
        cmpIocDetails.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

        GridLayout gl = new GridLayout(NUM_COLS, true);
        gl.verticalSpacing = SPACING;
        cmpIocDetails.setLayout(gl);

        // Selected IOC readback
        Label lblSelectedIoc = new Label(cmpIocDetails, SWT.NONE);
        lblSelectedIoc.setText("Selected:");
        lblSelectedIoc.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));

        FontDescriptor boldDescriptor = FontDescriptor.createFrom(lblSelectedIoc.getFont()).setStyle(SWT.BOLD);
        Font boldFont = boldDescriptor.createFont(lblSelectedIoc.getDisplay());
        lblSelectedIoc.setFont(boldFont);

        selectedIoc = new Text(cmpIocDetails, SWT.BORDER);
        selectedIoc.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, NUM_COLS - 1, 1));
        selectedIoc.setEditable(false);

        // General IOC Settings
        Label lblSimLevel = new Label(cmpIocDetails, SWT.NONE);
        lblSimLevel.setText("Sim. Level");
        lblSimLevel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));

        simLevel = new ComboViewer(cmpIocDetails, SWT.READ_ONLY);
        simLevel.getCombo().setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 2, 1));
        simLevel.setContentProvider(new ArrayContentProvider());
        simLevel.setInput(SimLevel.values());

        new Label(cmpIocDetails, SWT.NONE);

        autoStart = new Button(cmpIocDetails, SWT.CHECK);
        autoStart.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));
        autoStart.setText("Auto-Start");

        autoRestart = new Button(cmpIocDetails, SWT.CHECK);
        autoRestart.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));
        autoRestart.setText("Auto-Restart");

        TabFolder iocSettings = new TabFolder(this, SWT.NONE);
        iocSettings.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

        // Settings tabs
        IocMacroPanel macrosPanel = new IocMacroPanel(iocSettings, SWT.NONE, editViewModel.getMacrosViewModel());
        pvVals = new IocPVsEditorPanel(iocSettings, SWT.NONE, dialog);
        pvSets = new IocPVSetsEditorPanel(iocSettings, SWT.NONE);

        TabItem macrosTab = new TabItem(iocSettings, SWT.NONE);
        macrosTab.setText("Macros");
        macrosTab.setControl(macrosPanel);

        TabItem pvValuesTab = new TabItem(iocSettings, SWT.NONE);
        pvValuesTab.setText("PV Values");
        pvValuesTab.setControl(pvVals);

        TabItem pvSetsTab = new TabItem(iocSettings, SWT.NONE);
        pvSetsTab.setText("PV Sets");
        pvSetsTab.setControl(pvSets);
    }

    /**
     * Sets the IOC used by the panel.
     * 
     * @param editableIoc
     *            The IOC.
     */
    public void setIOC(final EditableIoc editableIoc) {
        pvVals.setIOC(editableIoc);
        pvSets.setIOC(editableIoc);

        bind(editableIoc);
    }

    /**
     * Binds view model values to widgets.
     * 
     * @param editableIoc
     *            The IOC view model.
     */
    private void bind(EditableIoc editableIoc) {
        DataBindingContext bindingContext = new DataBindingContext();
        bindingContext.bindValue(WidgetProperties.text(SWT.NONE).observe(selectedIoc),
                BeanProperties.value("name").observe(editableIoc));
        bindingContext.bindValue(WidgetProperties.selection().observe(autoStart),
                BeanProperties.value("autostart").observe(editableIoc));
        bindingContext.bindValue(WidgetProperties.selection().observe(autoRestart),
                BeanProperties.value("restart").observe(editableIoc));
        bindingContext.bindValue(ViewersObservables.observeSingleSelection(simLevel),
                BeanProperties.value("simLevel").observe(editableIoc));
    }

}
