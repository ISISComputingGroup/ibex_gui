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
import org.eclipse.core.databinding.beans.typed.BeanProperties;
import org.eclipse.jface.databinding.swt.typed.WidgetProperties;
import org.eclipse.jface.databinding.viewers.typed.ViewerProperties;
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
import uk.ac.stfc.isis.ibex.ui.configserver.editing.macros.MacroPanel;
import uk.ac.stfc.isis.ibex.ui.configserver.editing.pvs.IocPVsEditorPanel;
import uk.ac.stfc.isis.ibex.ui.configserver.editing.pvsets.IocPVSetsEditorPanel;
import uk.ac.stfc.isis.ibex.ui.widgets.buttons.IBEXButton;
import uk.ac.stfc.isis.ibex.validators.MessageDisplayer;

/**
 * Dialog panel for editing the settings of an IOC.
 */
public class EditPanel extends Composite {
	
	// Don't show the remote PV prefix to the user until feature is ready 
	private static final boolean ALLOW_REMOTE_PV_PREFIX_CHANGE = false;

    private static final int NUM_COLS = 6;
    private static final int SPACING = 10;

    private Text selectedIoc;
    private Button autoStart;
    private Button autoRestart;
    private ComboViewer simLevel;

    private MacroPanel macros;
    private IocPVsEditorPanel pvVals;
    private IocPVSetsEditorPanel pvSets;
	private Label remotePvPrefixLbl;
	private Text remotePvPrefixTxt;
	
    /**
     * Constructor for the Edit IOC panel.
     * 
     * @param parent
     *            The parent composite.
     * @param style
     *            The SWT style
     * @param dialog
     *            The dialog displaying error messages.
     */
    public EditPanel(Composite parent, int style, MessageDisplayer dialog) {
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
        lblSimLevel.setToolTipText(
        		"By default, the simulation level file is set to NONE, meaning that the IOC will not run in simulation mode.\n"
        		+ "Under normal circumstances, you should not change the default setting.\n"
        		+ "Simulation mode is used for running the IOC without the actual physical device."
        );
        lblSimLevel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));

        simLevel = new ComboViewer(cmpIocDetails, SWT.READ_ONLY);
        simLevel.getCombo().setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 2, 1));
        simLevel.setContentProvider(new ArrayContentProvider());
        simLevel.setInput(SimLevel.values());

        new Label(cmpIocDetails, SWT.NONE);
        
        autoStart = new IBEXButton(cmpIocDetails, SWT.CHECK)
        		.layoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false))
        		.text("Auto-Start")
        		.tooltip("If set, the IOC will be started/restarted whenever the configuration is changed.\n"
        		+ "If not set then if the IOC is not running it will remained stopped after config change,\n"
        		+ "and if it is running it will remain running throughout the config change.\n"
        		+ "\n"
        		+ "Warning: if not set and the IOC is running, any changes you make (e.g. a macro change)\n"
        		+ "will not be set on the IOC until you restart it manually.")
        		.get();
        
        autoRestart = new IBEXButton(cmpIocDetails, SWT.CHECK)
        		.layoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false))
        		.text("Auto-Restart")
        		.tooltip("If set, the IOC will be automatically restarted if it is terminated unexpectedly.\n"
                		+ "If the IOC is stopped from the client then it will not be restarted.")
        		.get();
        
        if (ALLOW_REMOTE_PV_PREFIX_CHANGE) {
	        remotePvPrefixLbl = new Label(cmpIocDetails, SWT.NONE);
	        remotePvPrefixLbl.setText("Remote PV prefix: ");
	        remotePvPrefixLbl.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));
	        
	        remotePvPrefixTxt = new Text(cmpIocDetails, SWT.BORDER);
	        remotePvPrefixTxt.setEditable(true);
	        remotePvPrefixTxt.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, NUM_COLS - 1, 1));
        }

        TabFolder iocSettings = new TabFolder(this, SWT.NONE);
        iocSettings.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

        // Settings tabs
        macros = new MacroPanel(iocSettings, SWT.NONE);
        pvVals = new IocPVsEditorPanel(iocSettings, SWT.NONE, dialog);
        pvSets = new IocPVSetsEditorPanel(iocSettings, SWT.NONE, dialog);

        TabItem macrosTab = new TabItem(iocSettings, SWT.NONE);
        macrosTab.setText("Macros");
        macrosTab.setControl(macros);

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

        macros.setIOC(editableIoc);
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
        bindingContext.bindValue(WidgetProperties.buttonSelection().observe(autoStart),
                BeanProperties.value("autostart").observe(editableIoc));
        bindingContext.bindValue(WidgetProperties.buttonSelection().observe(autoRestart),
                BeanProperties.value("restart").observe(editableIoc));
        bindingContext.bindValue(ViewerProperties.singleSelection().observe(simLevel),
                BeanProperties.value("simLevel").observe(editableIoc));
        bindingContext.bindValue(WidgetProperties.enabled().observe(autoStart),
                BeanProperties.value("editable").observe(editableIoc));
        bindingContext.bindValue(WidgetProperties.enabled().observe(autoRestart),
                BeanProperties.value("editable").observe(editableIoc));
        bindingContext.bindValue(WidgetProperties.enabled().observe(simLevel.getCombo()),
                BeanProperties.value("editable").observe(editableIoc));
        if (ALLOW_REMOTE_PV_PREFIX_CHANGE) {
        	bindingContext.bindValue(WidgetProperties.text(SWT.Modify).observe(remotePvPrefixTxt), 
        		BeanProperties.value("remotePvPrefix").observe(editableIoc));
        }
    }
}
