
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

package uk.ac.stfc.isis.ibex.ui.configserver.editing;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;

import uk.ac.stfc.isis.ibex.configserver.editing.EditableConfiguration;
import uk.ac.stfc.isis.ibex.ui.configserver.ConfigurationViewModels;
import uk.ac.stfc.isis.ibex.ui.configserver.editing.blocks.BlocksEditorPanel;
import uk.ac.stfc.isis.ibex.ui.configserver.editing.components.ComponentEditorPanel;
import uk.ac.stfc.isis.ibex.ui.configserver.editing.groups.GroupsEditorPanel;
import uk.ac.stfc.isis.ibex.ui.configserver.editing.iocs.IocOverviewPanel;
import uk.ac.stfc.isis.ibex.ui.configserver.editing.summary.SummaryPanel;
import uk.ac.stfc.isis.ibex.validators.MessageDisplayer;

/**
 * The panel that contains all the information for editing a configuration.
 */
public class ConfigEditorPanel extends Composite {
    
    /**
     * The name of the blocks tab. Needs to be public so it can be switched to
     * from elsewhere.
     */
    public static final String BLOCK_TAB_NAME = "Blocks";

	private final IocOverviewPanel iocs;
	private final BlocksEditorPanel blocks;
	private final GroupsEditorPanel groups;
	private final ComponentEditorPanel components;
	private final SummaryPanel summary;
	
    private TabFolder editorTabs;

    /**
     * The constructor for the overall panel that is used for editing and
     * viewing a configuration.
     * 
     * @param parent
     *            The composite that holds this panel.
     * @param style
     *            An integer giving the panel style using SWT style flags.
     * @param dialog
     *            The message displayer used to show error messages to the user.
     * @param isComponent
     *            Whether the configuration being displayed is a component or
     *            not.
     * @param configurationViewModels
     *            A class holding a number of view models for displaying
     *            configuration data to the user.
     */
    public ConfigEditorPanel(Composite parent, int style, MessageDisplayer dialog, boolean isComponent,
            ConfigurationViewModels configurationViewModels) {
		super(parent, style);
		GridLayout gridLayout = new GridLayout(1, false);
		gridLayout.verticalSpacing = 0;
		gridLayout.marginWidth = 0;
		gridLayout.marginHeight = 0;
		gridLayout.horizontalSpacing = 0;
		setLayout(gridLayout);
		
        summary = new SummaryPanel(this, SWT.NONE, dialog);
		summary.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		
        editorTabs = new TabFolder(this, SWT.NONE);
		editorTabs.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

        if (isComponent) {
            components = null;
        } else {
            TabItem componentsTab = new TabItem(editorTabs, SWT.NONE);
            componentsTab.setText("Components");

            components = new ComponentEditorPanel(editorTabs, SWT.NONE, dialog);
            componentsTab.setControl(components);
        }

		iocs = new IocOverviewPanel(editorTabs, SWT.NONE, dialog);
		
		TabItem iocsTab = new TabItem(editorTabs, SWT.NONE);
		iocsTab.setText("IOCs");
		iocsTab.setControl(iocs);
		
		TabItem blocksTab = new TabItem(editorTabs, SWT.NONE);
        blocksTab.setText(BLOCK_TAB_NAME);
		
		blocks = new BlocksEditorPanel(editorTabs, SWT.NONE);
		blocksTab.setControl(blocks);
		
		TabItem groupsTab = new TabItem(editorTabs, SWT.NONE);
		groupsTab.setText("Groups");
		
        groups = new GroupsEditorPanel(editorTabs, SWT.NONE, dialog, configurationViewModels);
		groupsTab.setControl(groups);
	}

    /**
     * Set which EditableConfiguration the dialog is editing.
     * 
     * @param config
     *            The configuration to edit.
     */
	public void setConfigToEdit(EditableConfiguration config) {		
		iocs.setConfig(config);
		blocks.setConfig(config);
		if (components != null) {
			components.setConfig(config);
		}
		summary.setConfig(config);
	}
	
	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		iocs.setEnabled(enabled);
		blocks.setEnabled(enabled);
		groups.setEnabled(enabled);
		if (components != null) {
			components.setEnabled(enabled);
		}
	}

    /**
     * Opens the dialog box for editing a specific block.
     * 
     * @param blockName
     *            The name of the block to edit.
     */
    public void openEditBlockDialog(String blockName) {
        blocks.openEditBlockDialog(blockName);
    }
}
