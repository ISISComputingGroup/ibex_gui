
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
import uk.ac.stfc.isis.ibex.runcontrol.RunControlServer;
import uk.ac.stfc.isis.ibex.ui.configserver.dialogs.MessageDisplayer;
import uk.ac.stfc.isis.ibex.ui.configserver.editing.blocks.BlocksEditorPanel;
import uk.ac.stfc.isis.ibex.ui.configserver.editing.components.ComponentEditorPanel;
import uk.ac.stfc.isis.ibex.ui.configserver.editing.groups.GroupsEditorPanel;
import uk.ac.stfc.isis.ibex.ui.configserver.editing.iocs.IIocDependentPanel;
import uk.ac.stfc.isis.ibex.ui.configserver.editing.iocs.IIocPanelCreator;
import uk.ac.stfc.isis.ibex.ui.configserver.editing.iocs.IocSelectorPanel;
import uk.ac.stfc.isis.ibex.ui.configserver.editing.iocs.IocsEditorPanel;
import uk.ac.stfc.isis.ibex.ui.configserver.editing.macros.MacroPanel;
import uk.ac.stfc.isis.ibex.ui.configserver.editing.pvs.IocPVsEditorPanel;
import uk.ac.stfc.isis.ibex.ui.configserver.editing.pvsets.IocPVSetsEditorPanel;
import uk.ac.stfc.isis.ibex.ui.configserver.editing.summary.SummaryPanel;

public class ConfigEditorPanel extends Composite {

	private final IocsEditorPanel iocs;
	private final BlocksEditorPanel blocks;
	private final GroupsEditorPanel groups;
	private final ComponentEditorPanel components;
	private final IocSelectorPanel iocMacros;
	private final IocSelectorPanel iocPVs;
	private final IocSelectorPanel iocPVSets;
	private final SummaryPanel summary;
	
	public ConfigEditorPanel(Composite parent, int style, MessageDisplayer dialog, boolean isComponent) {
		super(parent, style);
		GridLayout gridLayout = new GridLayout(1, false);
		gridLayout.verticalSpacing = 0;
		gridLayout.marginWidth = 0;
		gridLayout.marginHeight = 0;
		gridLayout.horizontalSpacing = 0;
		setLayout(gridLayout);
		
		TabFolder editorTabs = new TabFolder(this, SWT.NONE);
		editorTabs.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		iocs = new IocsEditorPanel(editorTabs, SWT.NONE, dialog);
		
		TabItem iocsTab = new TabItem(editorTabs, SWT.NONE);
		iocsTab.setText("IOCs");
		iocsTab.setControl(iocs);
		
		TabItem blocksTab = new TabItem(editorTabs, SWT.NONE);
		blocksTab.setText("Blocks");
		
		blocks = new BlocksEditorPanel(editorTabs, SWT.NONE);
		blocksTab.setControl(blocks);
		
		TabItem groupsTab = new TabItem(editorTabs, SWT.NONE);
		groupsTab.setText("Groups");
		
		groups = new GroupsEditorPanel(editorTabs, SWT.NONE, dialog);
		groupsTab.setControl(groups);
		
		if (isComponent) {
			components = null;
		}
		else {
			TabItem componentsTab = new TabItem(editorTabs, SWT.NONE);
			componentsTab.setText("Components");
			
			components = new ComponentEditorPanel(editorTabs, SWT.NONE, dialog);
			componentsTab.setControl(components);
		}
		
		TabItem tbtmIocMacros = new TabItem(editorTabs, SWT.NONE);
		tbtmIocMacros.setText("IOC Macros");
		
		final MessageDisplayer msgDisp = dialog;
		IIocPanelCreator macroFactory = new IIocPanelCreator() {
			@Override
			public IIocDependentPanel factory(Composite parent) {
				return new MacroPanel(parent, SWT.NONE);
			}
		};
		iocMacros = new IocSelectorPanel(editorTabs, SWT.NONE, macroFactory);
		tbtmIocMacros.setControl(iocMacros);
		
		TabItem tbtmIocPvValues = new TabItem(editorTabs, SWT.NONE);
		tbtmIocPvValues.setText("IOC PV Values");
		
		IIocPanelCreator pvsFactory = new IIocPanelCreator() {
			@Override
			public IIocDependentPanel factory(Composite parent) {
				return new IocPVsEditorPanel(parent, SWT.NONE, msgDisp);
			}
		};
		iocPVs = new IocSelectorPanel(editorTabs, SWT.NONE, pvsFactory);
		tbtmIocPvValues.setControl(iocPVs);
		
		TabItem tbtmIocPvSets = new TabItem(editorTabs, SWT.NONE);
		tbtmIocPvSets.setText("IOC PV Sets");
		
		IIocPanelCreator pvSetsFactory = new IIocPanelCreator() {
			@Override
			public IIocDependentPanel factory(Composite parent) {
				return new IocPVSetsEditorPanel(parent, SWT.NONE, msgDisp);
			}
		};
		iocPVSets = new IocSelectorPanel(editorTabs, SWT.NONE, pvSetsFactory);
		tbtmIocPvSets.setControl(iocPVSets);
		
		TabItem summaryTab = new TabItem(editorTabs, SWT.NONE);
		summaryTab.setText("Summary");
		
		summary = new SummaryPanel(editorTabs, SWT.NONE, dialog);
		summaryTab.setControl(summary);
	}

	public void setConfigToEdit(EditableConfiguration config) {		
		iocs.setConfig(config);
		blocks.setConfig(config);
		groups.setConfig(config);
		if (components != null) components.setConfig(config);
		iocMacros.setConfig(config);
		iocPVs.setConfig(config);
		iocPVSets.setConfig(config);
		summary.setConfig(config);
	}
	
    public void setRunControlServer(RunControlServer runControl) {
        blocks.setRunControlServer(runControl);
    }

	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		iocs.setEnabled(enabled);
		blocks.setEnabled(enabled);
		groups.setEnabled(enabled);
		if (components != null) components.setEnabled(enabled);
		iocMacros.setEnabled(enabled);
		iocPVs.setEnabled(enabled);
		iocPVSets.setEnabled(enabled);
	}
}
