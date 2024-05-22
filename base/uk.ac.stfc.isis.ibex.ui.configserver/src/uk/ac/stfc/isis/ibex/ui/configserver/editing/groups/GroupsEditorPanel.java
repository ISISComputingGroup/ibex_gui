
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

package uk.ac.stfc.isis.ibex.ui.configserver.editing.groups;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.beans.typed.BeanProperties;
import org.eclipse.core.databinding.observable.list.IObservableList;
import org.eclipse.jface.databinding.swt.typed.WidgetProperties;
import org.eclipse.jface.databinding.viewers.ObservableListContentProvider;
import org.eclipse.jface.databinding.viewers.typed.ViewerProperties;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.ResourceManager;
import org.eclipse.wb.swt.SWTResourceManager;

import uk.ac.stfc.isis.ibex.configserver.Configurations;
import uk.ac.stfc.isis.ibex.configserver.editing.EditableBlock;
import uk.ac.stfc.isis.ibex.configserver.editing.EditableConfiguration;
import uk.ac.stfc.isis.ibex.ui.configserver.ConfigurationViewModels;
import uk.ac.stfc.isis.ibex.ui.configserver.editing.DoubleListEditor;
import uk.ac.stfc.isis.ibex.ui.widgets.buttons.IBEXButton;
import uk.ac.stfc.isis.ibex.validators.BlockServerNameValidator;
import uk.ac.stfc.isis.ibex.validators.GroupNameValidator;
import uk.ac.stfc.isis.ibex.validators.MessageDisplayer;

/**
 * Group Editor Panel editing of groups for a component or configuration. Allows
 * groups to be added, removed, names edited block added and removed
 */
@SuppressWarnings({"magicnumber", "unused" })
public class GroupsEditorPanel extends Composite {
    /** Editor for blocks, those available and those unavailable. */
	private final DoubleListEditor<EditableBlock> blocksEditor;

    /** Current group name. */
	private Text name;

    /** The component details. */
	private Label componentDetails;

    /** The groups viewer. */
	private ListViewer groupsViewer;
	
	/** The read-only groups viewer. */
	private ListViewer readOnlyGroupsViewer;

    /** The group list. */
	private List groupList;
	
	/** The read-only group list. */
	private List readOnlyGroupList;

    /** binding context. */
	private DataBindingContext bindingContext = new DataBindingContext();

    /** Strategy for updating values. */
    private UpdateValueStrategy<String, String> strategy = new UpdateValueStrategy<>();


    /**
     * Instantiates a new groups editor panel.
     *
     * @param parent the parent
     * @param style the style
     * @param messageDisplayer a way of displaying messages to the user
     * @param configurationViewModels the configuration view models
     * @throws TimeoutException
     */
    public GroupsEditorPanel(Composite parent, int style, final MessageDisplayer messageDisplayer,
            final ConfigurationViewModels configurationViewModels) {
		super(parent, style);

        final GroupEditorViewModel groupEditorViewModel = configurationViewModels.groupEditorViewModel();

        setLayout(new GridLayout(2, false));
        
		Group grpGroups = new Group(this, SWT.NONE);
        grpGroups.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
        grpGroups.setText("Groups");
        grpGroups.setLayout(new GridLayout(3, false));

        groupsViewer = new ListViewer(grpGroups, SWT.BORDER | SWT.V_SCROLL | SWT.SINGLE);
        ObservableListContentProvider<String> contentProvider = new ObservableListContentProvider<>();
        groupsViewer.setContentProvider(contentProvider);

        groupsViewer.setInput(BeanProperties.list(EditableConfiguration.EDITABLE_GROUPS)
                .observe(configurationViewModels.getConfigModel().getValue()));

		groupList = groupsViewer.getList();

		GridData gd_viewer = new GridData(SWT.FILL, SWT.FILL, true, true, 2, 4);
		gd_viewer.widthHint = 125;
		groupList.setLayoutData(gd_viewer);
        groupList.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.keyCode == SWT.DEL) {
                    groupEditorViewModel.removeGroup(groupList.getSelectionIndex());
                }
            }
        });
        
		Group grpSelectedGroup = new Group(this, SWT.NONE);
		grpSelectedGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		grpSelectedGroup.setText("Selected group");
		grpSelectedGroup.setLayout(new GridLayout(3, false));

		Label lblEditName = new Label(grpSelectedGroup, SWT.NONE);
		GridData gd_lblEditName = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_lblEditName.horizontalIndent = 10;
		lblEditName.setLayoutData(gd_lblEditName);
		lblEditName.setText("Name:");

		name = new Text(grpSelectedGroup, SWT.BORDER);
		GridData gd_name = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_name.widthHint = 125;
		name.setLayoutData(gd_name);
		name.setEnabled(false);

		componentDetails = new Label(grpSelectedGroup, SWT.NONE);
		componentDetails.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.ITALIC));
		componentDetails.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));

		Group grpBlocks = new Group(grpSelectedGroup, SWT.NONE);
		grpBlocks.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 3, 1));
		grpBlocks.setLayout(new GridLayout(1, false));
		grpBlocks.setText("Blocks");

		blocksEditor = new DoubleListEditor<EditableBlock>(grpBlocks, SWT.NONE, "name", true);
		blocksEditor.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		blocksEditor.addSelectionListenerForSelecting(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
                groupEditorViewModel.getSelectedGroup(groupList.getSelectionIndex()).ifPresent(
                		g -> g.toggleSelection(blocksEditor.unselectedItems()));
			}
		});

		blocksEditor.addSelectionListenerForUnselecting(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
                groupEditorViewModel.getSelectedGroup(groupList.getSelectionIndex()).ifPresent(
                		g -> g.toggleSelection(blocksEditor.selectedItems()));
			}
		});

		blocksEditor.addSelectionListenerForMovingUp(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				groupEditorViewModel.getSelectedGroup(groupList.getSelectionIndex()).ifPresent(
						g -> {
								g.moveBlockUp(blocksEditor.selectedItem());
								blocksEditor.refreshViewer();
							 });
			}
		});

		blocksEditor.addSelectionListenerForMovingDown(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				groupEditorViewModel.getSelectedGroup(groupList.getSelectionIndex()).ifPresent(
						g -> {
								g.moveBlockDown(blocksEditor.selectedItem());
								blocksEditor.refreshViewer();
							 });
			}
		});

		// bind group name change box to selected group name with validation
        BlockServerNameValidator groupRules = Configurations.getInstance().variables().groupRules.getValue();
        final GroupNameValidator groupNamesValidator = new GroupNameValidator(
                configurationViewModels.getConfigModel().getValue(), messageDisplayer, groupRules);
        
        strategy.setBeforeSetValidator(groupNamesValidator);
        bindingContext.bindValue(
                WidgetProperties.text(SWT.Modify).observe(name), ViewerProperties.singleSelection()
                .value(BeanProperties.value("name", String.class)).observe(groupsViewer),
                strategy, null);

		name.addModifyListener(e -> groupsViewer.refresh());

		IObservableList<EditableBlock> selectedBlocks = ViewerProperties.singleSelection().list(BeanProperties.list("selectedBlocks", EditableBlock.class)).observe(groupsViewer);
		IObservableList<EditableBlock> unselectedBlocks = ViewerProperties.singleSelection().list(BeanProperties.list("unselectedBlocks", EditableBlock.class)).observe(groupsViewer);
		
		
		GridData gd_spacerButton = new GridData(SWT.LEFT, SWT.CENTER, false, true, 1, 1);
		gd_spacerButton.heightHint = 50;
		
		Button spacerButton = new IBEXButton(grpGroups, SWT.NONE)
				.layoutData(gd_spacerButton)
				.visible(false)
				.get();
		
		GridData gd_btnUp = new GridData(SWT.LEFT, SWT.CENTER, false, true, 1, 1);
		gd_btnUp.widthHint = 25;
		
		Button btnUp = new IBEXButton(grpGroups, SWT.NONE, evt -> {
			groupEditorViewModel.moveGroupUp(groupList.getSelectionIndex());
		})
				.layoutData(gd_btnUp)
				.image(ResourceManager.getPluginImage("uk.ac.stfc.isis.ibex.ui", "icons/move_up.png"))
				.get();
		
		GridData gd_btnDown = new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1);
		gd_btnDown.widthHint = 25;
		
		Button btnDown = new IBEXButton(grpGroups, SWT.NONE, evt -> {
			groupEditorViewModel.moveGroupDown(groupList.getSelectionIndex());
		})
				.layoutData(gd_btnUp)
				.image(ResourceManager.getPluginImage("uk.ac.stfc.isis.ibex.ui", "icons/move_down.png"))
				.get();

		
		Button spacerButton2 = new IBEXButton(grpGroups, SWT.NONE)
				.layoutData(new GridData(SWT.LEFT, SWT.CENTER, false, true, 1, 1))
				.visible(false)
				.get();
		
		GridData gd_btnAdd = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnAdd.widthHint = 70;
		
		Button btnAdd = new IBEXButton(grpGroups, SWT.NONE, event -> {
			groupEditorViewModel.addNewGroup();
		})
				.layoutData(gd_btnAdd)
				.text("Add")
				.get();
		
		
		GridData gd_btnRemove = new GridData(SWT.CENTER, SWT.CENTER, true, false, 1, 1);
		gd_btnRemove.widthHint = 70;
		
		final Button btnRemove = new IBEXButton(grpGroups, SWT.NONE, evt -> {
			groupEditorViewModel.removeGroup(groupList.getSelectionIndex());
		})
				.layoutData(gd_btnRemove)
				.text("Remove")
				.enabled(false)
				.get();

		new Label(grpGroups, SWT.NONE);

		groupsViewer.addSelectionChangedListener(new ISelectionChangedListener() {

            @Override
			public void selectionChanged(SelectionChangedEvent arg0) {
                int selectionIndex = groupList.getSelectionIndex();
                groupNamesValidator.setSelectedIndex(selectionIndex);
                if (selectionIndex == -1) {
                    groupNamesValidator.validate("");
                } else {
                    groupNamesValidator.validate(groupList.getSelection()[0]);
                }
                boolean canEditSelected = groupEditorViewModel.canEditSelected(selectionIndex);

                btnRemove.setEnabled(canEditSelected);
                name.setEnabled(canEditSelected);
                blocksEditor.setEnabled(canEditSelected);

			}
		});

		blocksEditor.bind(unselectedBlocks, selectedBlocks);
		

		if (!configurationViewModels.getConfigModel().getValue().getIsComponent()) {
			// Read-only group selection.
			Group grpReadOnlyGroups = new Group(this, SWT.NONE);
			grpReadOnlyGroups.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
			grpReadOnlyGroups.setText("Component Groups");
			grpReadOnlyGroups.setLayout(new GridLayout(3, false));
	
	        readOnlyGroupsViewer = new ListViewer(grpReadOnlyGroups, SWT.BORDER | SWT.V_SCROLL | SWT.SINGLE);
	        ObservableListContentProvider<String> readOnlycontentProvider = new ObservableListContentProvider<>();
	        readOnlyGroupsViewer.setContentProvider(readOnlycontentProvider);
	
	        readOnlyGroupsViewer.setInput(BeanProperties.list(EditableConfiguration.READ_ONLY_GROUPS)
	                .observe(configurationViewModels.getConfigModel().getValue()));
	
	        readOnlyGroupList = readOnlyGroupsViewer.getList();
	        
			GridData gd_readOnlyViewer = new GridData(SWT.FILL, SWT.FILL, true, true, 2, 4);
			gd_readOnlyViewer.widthHint = 125;
			readOnlyGroupList.setLayoutData(gd_readOnlyViewer);
			readOnlyGroupList.setToolTipText("Blocks and groups defined in components are read-only. To edit these groups, edit the component which defines them.");
			
			
			// Selected group blocks.
			Group grpSelectedReadOnlyGroup = new Group(this, SWT.NONE);
			grpSelectedReadOnlyGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
			grpSelectedReadOnlyGroup.setText("Blocks");
			grpSelectedReadOnlyGroup.setLayout(new GridLayout(1, false));
			grpSelectedReadOnlyGroup.setToolTipText("Blocks and groups defined in components are read-only. To edit these blocks, edit the component which defines them.");
			
			final List readOnlyBlocksList = new List(grpSelectedReadOnlyGroup, SWT.BORDER | SWT.SINGLE | SWT.V_SCROLL);
			GridData gd_readOnlyBlocksList = new GridData(SWT.FILL, SWT.FILL, true, true, 2, 4);
			gd_readOnlyBlocksList.widthHint = 125;
			readOnlyBlocksList.setLayoutData(gd_readOnlyBlocksList);
			readOnlyBlocksList.setEnabled(false);
			
			readOnlyGroupsViewer.addSelectionChangedListener(new ISelectionChangedListener() {
	
	            @Override
				public void selectionChanged(SelectionChangedEvent arg0) {
	                var selection = readOnlyGroupList.getSelection();
	                var groups = configurationViewModels.getConfigModel().getValue().getReadOnlyGroups();
	                for (var group : groups) {
	                	if (group.getName().equals(selection[0])) {
	                		readOnlyBlocksList.setItems(group.getBlocks().toArray(String[]::new));
	                	}
	                }
				}
			});
		}
	}
}
