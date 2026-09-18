
/*
* This file is part of the ISIS IBEX application.
* Copyright (C) 2012-2025 Science & Technology Facilities Council.
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
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;

import uk.ac.stfc.isis.ibex.configserver.editing.EditableConfiguration;
import uk.ac.stfc.isis.ibex.ui.configserver.ConfigurationViewModels;
import uk.ac.stfc.isis.ibex.ui.configserver.editing.blocks.BlocksEditorPanel;
import uk.ac.stfc.isis.ibex.ui.configserver.editing.blocks.BlocksEditorViewModel;
import uk.ac.stfc.isis.ibex.ui.configserver.editing.components.ComponentEditorPanel;
import uk.ac.stfc.isis.ibex.ui.configserver.editing.groups.GroupsEditorPanel;
import uk.ac.stfc.isis.ibex.ui.configserver.editing.iocs.IocOverviewPanel;
import uk.ac.stfc.isis.ibex.ui.configserver.editing.macros.GlobalMacroOverviewPanel;
import uk.ac.stfc.isis.ibex.ui.configserver.editing.summary.SummaryPanel;
import uk.ac.stfc.isis.ibex.validators.MessageDisplayer;

/**
 * The panel that contains all the information for editing a configuration.
 */
public class ConfigEditorPanel extends Composite {
	private final IocOverviewPanel iocs;
	private final BlocksEditorPanel blocks;
	private final GroupsEditorPanel groups;
	private final ComponentEditorPanel components;
	private final SummaryPanel summary;
    private TabItem blocksTab;

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
     * @param config
     *            The current config
     * @param configurationViewModels
     *            A class holding a number of view models for displaying
     *            configuration data to the user.
     * @throws TimeoutException
     */
    public ConfigEditorPanel(Composite parent, int style, MessageDisplayer dialog, EditableConfiguration config,
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

		editorTabs.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                if (editorTabs.getSelection()[0].equals(blocksTab)) {
                    blocks.showMnemonics();
                }
            }
        });

        if (config.getIsComponent()) {
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

        blocksTab = new TabItem(editorTabs, SWT.NONE);
        blocksTab.setText("Blocks");

		blocks = new BlocksEditorPanel(editorTabs, SWT.NONE, new BlocksEditorViewModel(config));
		blocksTab.setControl(blocks);

		TabItem groupsTab = new TabItem(editorTabs, SWT.NONE);
		groupsTab.setText("Groups");

        groups = new GroupsEditorPanel(editorTabs, SWT.NONE, dialog, configurationViewModels);
		groupsTab.setControl(groups);

		setConfigToEdit(config);
		
		if (!config.getGlobalmacros().isEmpty()) {
			final GlobalMacroOverviewPanel globalMacros = new GlobalMacroOverviewPanel(editorTabs, config);
			TabItem globalMacrosTab = new TabItem(editorTabs, SWT.NONE);
			globalMacrosTab.setText("Global Macros");
			globalMacrosTab.setControl(globalMacros);
		}
	}

    /**
     * Set which EditableConfiguration the dialog is editing.
     *
     * @param config
     *            The configuration to edit.
     */
	private void setConfigToEdit(EditableConfiguration config) {
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
     * Selects the block tab within the configuration editor panel.
     */
    public void selectBlocksTab() {
        editorTabs.setSelection(blocksTab);
    }

}
